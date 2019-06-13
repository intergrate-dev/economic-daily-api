package com.founder.econdaily.modules.newspaper.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.founder.ark.common.utils.StringUtil;
import com.founder.econdaily.common.redis.RedisService;
import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.newspaper.entity.*;
import com.founder.econdaily.modules.newspaper.repository.NewsPaperRepository;
import com.founder.econdaily.modules.newspaper.repository.PaperAttachmentRepository;
import com.founder.econdaily.modules.newspaper.repository.PaperDateRepository;
import com.founder.econdaily.modules.newspaper.service.NewsPaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

/**
 * @description ：
 */
@Service
public class NewsPaperServiceImpl implements NewsPaperService {
    @Autowired
    private NewsPaperRepository newsPaperRepository;

    @Autowired
    private PaperAttachmentRepository attachmentRepository;

    @Autowired
    private PaperDateRepository paperDateRepository;

    @Autowired
    private RedisService redisService;


    private static final Logger logger = LoggerFactory.getLogger(NewsPaperServiceImpl.class);


    @Override
    public Map<String, Object> findNewtests() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        long time1 = System.currentTimeMillis();
        List<PaperLayout> newtestPapers = null;
        List<PaperVo> pvs = new ArrayList<PaperVo>();
        if (redisService.exists(NewsPaperKey.netestPapers, NewsPaperKey.NEWS_PAPER_KEY)) {
            newtestPapers = JSON.parseObject(redisService.get(NewsPaperKey.netestPapers, NewsPaperKey.NEWS_PAPER_KEY),
                    new TypeReference<ArrayList<PaperLayout>>() {
                    });
        }
        if (newtestPapers == null) {
            newtestPapers = this.cacheNewtestPapers();
        }
        long time2 = System.currentTimeMillis();
        for (PaperLayout paperLayout : newtestPapers) {
            paperLayout.setCoverPic(attachmentRepository.findCoverByArticleIdAndLibId(paperLayout.getId(),
                    PaperLayout.LAYOUT_LIB_ID, PaperAttachment.ATT_TYPE_COVER_LAYOUT));
            List<PaperLayout> list = newsPaperRepository.findByPaperIdAndPlDate(paperLayout.getPlPaperID(), paperLayout.getPlDate());
            Integer count = list != null && list.size() > 0 ? list.size() : null;
            paperLayout.setLayoutCount(count);
            pvs.add(this.parseEntityByLayout(paperLayout));
        }
        long time3 = System.currentTimeMillis();
        System.out.println("prase_1: " + (time2 - time1) + ", prase_2: " + (time3 - time2));
        map.put("newtests", pvs);
        return map;
    }

    private PaperVo parseEntityByLayout(PaperLayout paperLayout) {
        PaperVo pv = new PaperVo();
        pv.setPlName(paperLayout.getPlName());
        pv.setCoverPic(paperLayout.getCoverPic());
        pv.setLayoutCount(paperLayout.getLayoutCount());
        pv.setPlDate(DateParseUtil.dateToString(paperLayout.getPlDate()));
        pv.setPlPaper(paperLayout.getPlPaper());
        Paper paper = newsPaperRepository.queryPaperByName(paperLayout.getPlPaper());
        if (paper != null) {
            pv.setPlPaperCode(paper.getPaperCode());
        }
        pv.setPlUrl(paperLayout.getPlUrl());
        return pv;
    }

    @Async
    @Override
    public List<PaperLayout> cacheNewtestPapers() throws Exception {
        List<PaperLayout> newtestPapers = newsPaperRepository.findNewtestPaper();
        redisService.set(NewsPaperKey.netestPapers, NewsPaperKey.NEWS_PAPER_KEY, JSON.toJSONString(newtestPapers));
        return newtestPapers;
    }

    @Override
    public Map<String, Object> findPaperDatesByAttachId(String attachId) {
        Map<String, Object> map = new HashMap<String, Object>();
        PaperLayout paperLayout = newsPaperRepository.queryArticleAndPaperDate(attachId);
        if (paperLayout == null) {
            return map;
        }
        String formatDate = DateParseUtil.dateToString(paperLayout.getPlDate());
        map.put("paperName", paperLayout.getPlPaper());
        map.put("plDate", DateParseUtil.dateToString(paperLayout.getPlDate()));
        map.put("days", null);
        map.put("total", 0);
        if (!StringUtils.isEmpty(formatDate)) {
            String[] split = formatDate.split(RegxUtil.STRIP_SPLIT);
            PaperDateStatis paperDateStatis = paperDateRepository.queryDatesByYearAndMonth(split[0], split[1]);
            if (paperDateStatis != null && !StringUtils.isEmpty(paperDateStatis.getDays())) {
                map.put("days", ((String) paperDateStatis.getDays()).split(RegxUtil.COMMA_SPLIT));
                map.put("total", paperDateStatis.getTotal());
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> queryTopLayoutByPaperCodeAndPlDate(String paperCode, String plDate) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        Paper paper = newsPaperRepository.queryPaperByCode(paperCode);
        if (paper == null) {
            return map;
        }
        List<String> otherAttArtiIds = new ArrayList<String>();
        List<PaperLayout> paperLayouts = newsPaperRepository.queryCatalogsByPlDate(plDate, paper.getId());
        if (paperLayouts == null) {
            return map;
        }

        String plId = paperLayouts.get(0).getId();
        this.setCoordinate(plDate, map);
        this.setPlCatalogs(map, plDate, paper.getId(), otherAttArtiIds, plId);
        this.setPlContentList(map, plId);
        //本期版面图、pdf
        this.setPlPicAndPdf(map, plId);
        //本期各版版面图、pdf
        this.setOtherPlPicAndPdf(map, otherAttArtiIds);
        return map;
    }

    private void setCoordinate(String plDate, Map<String, Object> map) {
        List<String> days = new ArrayList<String>();
        if (!StringUtils.isEmpty(plDate)) {
            String[] split = plDate.split(RegxUtil.STRIP_SPLIT);
            PaperDateStatis paperDateStatis = paperDateRepository.queryDatesByYearAndMonth(split[0], split[1]);
            if (paperDateStatis != null && !StringUtils.isEmpty(paperDateStatis.getDays())) {
                for (String day : paperDateStatis.getDays().split(RegxUtil.COMMA_SPLIT)) {
                    if (Integer.parseInt(day) <= Integer.parseInt(split[2])) {
                        days.add(day);
                    }
                }
            }
        }
        map.put("coordinate", days);
    }

    private void setOtherPlPicAndPdf(Map<String, Object> map, List<String> otherAttArtiIds) {
        StringBuffer buff = new StringBuffer();
        for (String attArtiId : otherAttArtiIds) {
            buff.append(attArtiId).append(RegxUtil.COMMA_SPLIT);
        }
        String articleIds = buff.substring(0, buff.length() - 1);
        Map<String, String> aMap = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<PaperAttachment> attachs = attachmentRepository.findOtherPlPicAndPdf(articleIds, PaperLayout.LAYOUT_LIB_ID);
        for (PaperAttachment attach : attachs) {
            if (StringUtil.isNotBlank(attach.getAttUrl())) {
                aMap = new HashMap<String, String>();
                String[] split = attach.getAttUrl().split(RegxUtil.COMMA_SPLIT);
                if (split != null && split.length > 0) {
                    fillPropertity(aMap, split[0]);
                }
                if (split != null && split.length > 1) {
                    fillPropertity(aMap, split[1]);
                }
            }
            list.add(aMap);
        }
        map.put("otherPeriodAtts", list);
    }

    private void fillPropertity(Map<String, String> aMap, String s) {
        if (s.endsWith(RegxUtil.FILE_JPG)) {
            aMap.put("pic", s);
        }
        if (s.endsWith(RegxUtil.FILE_PDF)) {
            aMap.put("pdf", s);
        }
    }

    @Override
    public Map<String, Object> queryByPaperIdAndPlDate(String plDate, String paperCode, String layout) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        Paper paper = newsPaperRepository.queryPaperByCode(paperCode);
        if (paper == null) {
            return map;
        }
        PaperLayout paperLayout = newsPaperRepository.queryByPaperIdAndPlDate(plDate, paper.getId(), layout);
        if (paperLayout == null) {
            return map;
        }
        String plId = paperLayout.getId();
        List<String> otherAttArtiIds = new ArrayList<String>();

        this.setPlContentList(map, plId);
        this.setCoordinate(plDate, map);
        this.setPlCatalogs(map, plDate, paper.getId(), otherAttArtiIds, plId);
        //本期版面图、pdf
        this.setPlPicAndPdf(map, plId);
        //本期各版版面图、pdf
        this.setOtherPlPicAndPdf(map, otherAttArtiIds);
        return map;
    }

    @Override
    public Map<String, Object> queryByArticleId(String articleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        PaperArticle paperArticle = newsPaperRepository.queryByArticleId(articleId);
        if (paperArticle == null) {
            return map;
        }
        //文章图集图片
        paperArticle.setPics(Arrays.asList(attachmentRepository.findCoverByArticle(articleId).split(RegxUtil.COMMA_SPLIT)));
        map.put("article", JSONObject.toJSON(this.parseEntityWithArticle(paperArticle)));
        PaperLayout paperLayout = newsPaperRepository.queryLayoutById(paperArticle.getLayoutId());
        String plDate = DateParseUtil.dateToString(paperLayout.getPlDate());
        this.setCoordinate(plDate, map);

        List<String> otherAttArtiIds = new ArrayList<String>();
        this.setPlCatalogs(map, plDate, paperLayout.getPlPaperID(), otherAttArtiIds, paperArticle.getLayoutId());
        //本期版面图、pdf
        this.setPlPicAndPdf(map, paperArticle.getLayoutId());
        //本期各版版面图、pdf
        //this.setOtherPlPicAndPdf(map, otherAttArtiIds);

        //本版内容坐标 ？？
        this.setPlContentList(map, paperArticle.getLayoutId());
        //this.setMainBodyPic(map, paperArticle.getId());
        return map;
    }

    private PaperArticleVo parseEntityWithArticle(PaperArticle paperArticle) {
        PaperArticleVo pav = new PaperArticleVo();
        pav.setAbstra(paperArticle.getAbstra());
        pav.setAuthors(paperArticle.getAuthors());
        pav.setContent(paperArticle.getContent());
        pav.setId(paperArticle.getId());
        pav.setLayoutId(paperArticle.getLayoutId());
        pav.setLeadTitle(paperArticle.getLeadTitle());
        pav.setPaperName(paperArticle.getPaperName());
        pav.setPlDate(DateParseUtil.dateToString(paperArticle.getPlDate()));
        pav.setPubTime(DateParseUtil.dateToString(paperArticle.getPubTime()));
        pav.setPics(paperArticle.getPics());
        pav.setContPics(new ArrayList<String>());
        pav.setSubTitle(paperArticle.getSubTitle());
        pav.setSource(paperArticle.getSource());
        return pav;
    }

    private void setPlContentList(Map<String, Object> map, String plId) {
        List<String> contentList = new ArrayList<String>();
        List<PaperArticle> contents = newsPaperRepository.queryContentsByTopLayoutId(plId);
        if (contents != null) {
            for (PaperArticle content : contents) {
                contentList.add(content.getTitle());
            }
        }
        map.put("plContents", contentList);
    }

    private void setPlPicAndPdf(Map<String, Object> map, String plId) {
        map.put("plPic", attachmentRepository.findCoverByArticleIdAndLibId(plId, PaperLayout.LAYOUT_LIB_ID, PaperAttachment.ATT_TYPE_COVER_LAYOUT));
        map.put("plPdf", attachmentRepository.findCoverByArticleIdAndLibId(plId, PaperLayout.LAYOUT_LIB_ID, PaperAttachment.ATT_TYPE_PDF_LAYOUT));
    }

    private void setPlCatalogs(Map<String, Object> map, String parseDate, String paperId, List<String> otherAttArtiIds, String plId) {
        List<String> cList = new ArrayList<String>();
        List<PaperLayout> catalogs = newsPaperRepository.queryCatalogsByPlDate(parseDate, paperId);
        for (PaperLayout catalog : catalogs) {
            cList.add(catalog.getPlName());
            if (!plId.equals(catalog.getId())) {
                otherAttArtiIds.add(catalog.getId());
            }
        }
        map.put("plCatalogs", cList);
    }


}
