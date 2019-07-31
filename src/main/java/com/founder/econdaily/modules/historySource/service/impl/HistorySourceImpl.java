package com.founder.econdaily.modules.historySource.service.impl;

import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.historySource.service.HistorySourceService;
import com.founder.econdaily.modules.magazine.entity.MagazineArticle;
import com.founder.econdaily.modules.magazine.entity.MagazineArticleVo;
import com.founder.econdaily.modules.magazine.repository.MagazineAttachmentRepository;
import com.founder.econdaily.modules.magazine.repository.MagazineRepository;
import com.founder.econdaily.modules.newspaper.entity.PaperArticle;
import com.founder.econdaily.modules.newspaper.entity.PaperArticleVo;
import com.founder.econdaily.modules.newspaper.entity.PaperAttachment;
import com.founder.econdaily.modules.newspaper.repository.NewsPaperRepository;
import com.founder.econdaily.modules.newspaper.repository.PaperAttachmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HistorySourceImpl implements HistorySourceService {

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(HistorySourceImpl.class);

    @Autowired
    private NewsPaperRepository newsPaperRepository;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private PaperAttachmentRepository paperAttachmentRepository;

    @Autowired
    private MagazineAttachmentRepository magazineAttachmentRepository;

    @Override
    public Map<String, Object> queryPaperArticles(Integer pageNo, Integer limit, String st, String et) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String picUrl = null;
        List<String> pics = null;
        List<PaperArticle> paperArticles = newsPaperRepository.queryPaperArticles(pageNo, limit, st, et);
        List<PaperArticleVo> vos = new ArrayList<>();
        for (PaperArticle paperArticle : paperArticles) {
            pics = new ArrayList<String>();
            picUrl = paperAttachmentRepository.findCoverByArticleIdAndLibId(paperArticle.getId(), PaperArticle.ARTICLE_LIB_ID, PaperAttachment.ATT_TYPE_COVER_ARTICLE);
            if (!StringUtils.isEmpty(picUrl)) {
                pics.add(picUrl);
            }
            paperArticle.setPics(pics);
            vos.add(PaperArticleVo.parseEntityWithArticle(paperArticle));
        }
        map.put("list", vos);
        Integer total = newsPaperRepository.queryPaperArticleTotal(st, et);
        map.put("totalCount", total);
        map.put("pageCount", Math.ceil(total * 1.0 / limit));
        return map;
    }

    

    @Override
    public Map<String, Object> queryMagazineArticles(Integer pageNo, Integer limit, String st, String et) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<MagazineArticle> magazineArticles = magazineRepository.queryMagazineArticles(pageNo, limit, st, et);
        List<MagazineArticleVo> mavs = new ArrayList<>();
        for (MagazineArticle magazineArticle : magazineArticles) {
            // a_picBig  期刊存储;imported/jingji\2015\01\01\Figure-0176-01.jpg
            // http://172.17.41.49/magazine/jjrb/pic/200502/18/figure_0025_0042.jpg
            /*magazineArticle.setPic(magazineAttachmentRepository.findCoverByArticleIdAndLibId(magazineArticle.getId(), MagazineArticle.ARTICLE_LIB_ID,
                    DateParseUtil.dateToStringWithSplit(magazineArticle.getPubTime())));*/
            String pics = magazineAttachmentRepository.queryArticlePicsByArticleIdAndLibId(magazineArticle.getId(), MagazineArticle.ARTICLE_LIB_ID);
            if (!StringUtils.isEmpty(pics)) {
                magazineArticle.setPics(Arrays.asList(pics.split(RegxUtil.COMMA_SPLIT)));
            }
            mavs.add(MagazineArticleVo.parseEntity(magazineArticle));
        }
        map.put("list", mavs);
        Integer total = magazineRepository.queryMagazineArticleTotal(st, et);
        map.put("totalCount", total);
        map.put("pageCount", Math.ceil(total * 1.0 / limit));
        return map;
    }


}
