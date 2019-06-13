package com.founder.econdaily.modules.newspaper.service;

import com.alibaba.fastjson.JSONArray;
import com.founder.econdaily.modules.newspaper.entity.PaperLayout;
import org.springframework.scheduling.annotation.Async;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created in 2019/2/26 15:27
 * @description ：
 */
public interface NewsPaperService {

    Map<String, Object> findNewtests() throws Exception;

    @Async
    List<PaperLayout> cacheNewtestPapers() throws Exception;

    Map<String, Object> findPaperDatesByAttachId(String attachId);

    Map<String, Object> queryTopLayoutByPaperCodeAndPlDate(String paperCode, String plDate) throws ParseException;

    Map<String, Object> queryByPaperIdAndPlDate(String plDate, String paperId, String layout) throws ParseException;

    Map<String, Object> queryByArticleId(String articleId);
}
