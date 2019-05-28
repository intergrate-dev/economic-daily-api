package com.founder.econdaily.modules.magazine.service;

import java.util.Map;

/**
 * Created in 2019/2/27 14:53
 * @description ：
 */
public interface MagazineService {

    Map<String, Object> queryMagNewtests() throws Exception;

    Map<String, Object> queryByPdDateAndMagCode(String pdDate, String magCode) throws Exception;

    Map<String, Object> queryMagazineDetailAndArticle(String articleId) throws Exception;

    Map<String,Object> queryMagazineInfoByMagCode(String magCode);

    Map<String,Object> queryMagazineInfoByMagCodeAndYear(String magCode, String pdDate);

    Map<String,Object> queryByParams(String magName, String pdDate, Integer pageNo, Integer limit);
}
