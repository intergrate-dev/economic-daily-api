package com.founder.econdaily.modules.newspaper.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.founder.econdaily.common.redis.RedisService;
import com.founder.econdaily.common.util.DateParseUtil;
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
        }
        long time3 = System.currentTimeMillis();
        System.out.println("prase_1: " + (time2 - time1) + ", prase_2: " + (time3 - time2));
        map.put("newtests", newtestPapers);
        return map;
    }

    @Override
    public JSONArray cacheNewtestPapers1() throws Exception {
        return null;
    }

    @Override
    @Async
    public List<PaperLayout> cacheNewtestPapers() throws Exception {
        //List<PaperLayout> newtestPapers = new ArrayList<PaperLayout>();
        List<PaperLayout> newtestPapers = newsPaperRepository.findNewtestPaper();
        /*PaperLayout paperLayout_1 = new PaperLayout("paper1", "1", "经济日报", 30);
        PaperLayout paperLayout_2 = new PaperLayout("paper2", "2", "华商日报", 16);
        newtestPapers.add(paperLayout_1);
        newtestPapers.add(paperLayout_2);*/
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
        map.put("plDate", paperLayout.getPlDate());
        map.put("days", null);
        map.put("total", 0);
        if (!StringUtils.isEmpty(formatDate)) {
            String[] split = formatDate.split("-");
            PaperDateStatis paperDateStatis = paperDateRepository.queryDatesByYearAndMonth(split[0], split[1]);
            if (paperDateStatis != null && !StringUtils.isEmpty(paperDateStatis.getDays())) {
                map.put("days", ((String) paperDateStatis.getDays()).split(","));
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

        //List<PaperLayout> paperLayouts = newsPaperRepository.queryCatalogsByPlDate(DateParseUtil.stringToDate(plDate), paper.getId());
        List<PaperLayout> paperLayouts = newsPaperRepository.queryCatalogsByPlDate(plDate, paper.getId());
        if (paperLayouts != null && paperLayouts.size() > 0) {
            String plId = paperLayouts.get(0).getId();
            List<String> cList = new ArrayList<String>();
            for (PaperLayout catalog : paperLayouts) {
                cList.add(catalog.getPlName());
            }
            map.put("plCatalogs", cList);
            List<String> days = new ArrayList<String>();
            if (!StringUtils.isEmpty(plDate)) {
                String[] split = plDate.split("-");
                PaperDateStatis paperDateStatis = paperDateRepository.queryDatesByYearAndMonth(split[0], split[1]);
                if (paperDateStatis != null && !StringUtils.isEmpty(paperDateStatis.getDays())) {
                    for (String day : paperDateStatis.getDays().split(",")) {
                        if (Integer.parseInt(day) <= Integer.parseInt(split[2])) {
                            days.add(day);
                        }
                    }
                }
            }
            map.put("coordinate", days);
            // TODO 版面原图 （本版、本期各版）？
            this.setPlContentList(map, plId);
            this.setPlPic(map, plId);
        }

        return map;
    }

    @Override
    public Map<String, Object> queryByPaperIdAndPlDate(String plDate, String paperCode, String layout) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();
        //Date parseDate = DateParseUtil.stringToDate(plDate);
        Paper paper = newsPaperRepository.queryPaperByCode(paperCode);
        if (paper == null) {
            return map;
        }
        PaperLayout paperLayout = newsPaperRepository.queryByPaperIdAndPlDate(plDate, paper.getId(), layout);
        if (paperLayout == null) {
            return map;
        }
        List<String> cList = new ArrayList<String>();
        List<String> contentList = new ArrayList<String>();
        String plId = paperLayout.getId();

        this.setPlContentList(map, plId);
        this.setPlCatalogs(map, plDate, paper.getId());
        this.setPlPic(map, plId);
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
        paperArticle.setPics(Arrays.asList(attachmentRepository.findCoverByArticle(articleId).split(",")));
        map.put("article", JSONObject.toJSON(paperArticle));
        //版面图
        this.setPlPic(map, paperArticle.getLayoutId());
        //本版内容坐标 ？？
        this.setPlContentList(map, paperArticle.getLayoutId());
        //this.setMainBodyPic(map, paperArticle.getId());
        return map;
    }

    private void setPlContentList(Map<String, Object> map, String plId) {
        List<String> contentList = new ArrayList<String>();
        List<PaperArticle> contents = newsPaperRepository.queryContentsByTopLayoutId(plId);
        for (PaperArticle content : contents) {
            contentList.add(content.getTitle());
        }
        map.put("plContents", contentList);
    }

    private void setPlPic(Map<String, Object> map, String plId) {
        map.put("plPic", attachmentRepository.findCoverByArticleIdAndLibId(plId, PaperLayout.LAYOUT_LIB_ID, PaperAttachment.ATT_TYPE_COVER_LAYOUT));
    }

    /*private void setMainBodyPic(Map<String, Object> map, String articleId) {
        map.put("plPic", attachmentRepository.findMainBodyPicByArticleIdAndLibId(articleId, PaperLayout.LAYOUT_LIB_ID));
    }*/


    private void setPlCatalogs(Map<String, Object> map, String parseDate, String paperId) {
        List<String> cList = new ArrayList<String>();
        List<PaperLayout> catalogs = newsPaperRepository.queryCatalogsByPlDate(parseDate, paperId);
        for (PaperLayout catalog : catalogs) {
            cList.add(catalog.getPlName());
        }
        map.put("plCatalogs", cList);
    }


}
