package com.founder.econdaily.modules.historySource.service;

import java.util.Map;

/**
 * Created in 2019/2/27 14:53
 * @description ：
 */
public interface HistorySourceService {

    Map<String, Object> queryPaperArticles(Integer pageNo, Integer limit) throws Exception;

    Map<String,Object> queryMagazineArticles(Integer pageNo, Integer limit) throws Exception;
}
