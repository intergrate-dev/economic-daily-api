package com.founder.econdaily.modules.magazine.repository;

import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.modules.magazine.dto.MagazineVo;
import com.founder.econdaily.modules.magazine.entity.MagCatalog;
import com.founder.econdaily.modules.magazine.entity.MagazineArticle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MagazineRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(MagazineRepository.class);


    public MagazineVo queryByMagCode(String magCode) {
        StringBuffer sql = new StringBuffer();
        sql.append("select SYS_DOCUMENTID as magId, pa_name as magName from xy_magazine where pa_code = ? ");
        List<MagazineVo> list = jdbcTemplate.query(sql.toString(), new Object[]{magCode}, new BeanPropertyRowMapper(MagazineVo.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public MagazineVo queryByMagName(String magName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select SYS_DOCUMENTID as magId, pa_code as magCode from xy_magazine where pa_name = ? ");
        List<MagazineVo> list = jdbcTemplate.query(sql.toString(), new Object[]{magName}, new BeanPropertyRowMapper(MagazineVo.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<MagCatalog> queryCatalogsByPdDate(String pdDate, String magId, Boolean simple) {
        StringBuffer sql = new StringBuffer();
        if (simple) {
            sql.append("SELECT GROUP_CONCAT(SYS_TOPIC) ");
        } else{
            sql.append("SELECT GROUP_CONCAT(SYS_TOPIC, '-', SYS_DOCUMENTID) ");
        }
        // sql.append("SELECT GROUP_CONCAT(SYS_TOPIC) as artiTopics, a_column as columnName " +
        sql.append("as artiTopics, a_column as columnName FROM `xy_magarticle` where a_magazineID = ? and a_pubTime = ? " +
                "group by a_columnID order by SYS_DOCUMENTID DESC ");
        logger.info("--------------------- magCatalogs sql: {}, magId: {}, paDate: {}, simple: {}", sql.toString(), magId, pdDate, simple);
        List<MagCatalog> list = jdbcTemplate.query(sql.toString(), new Object[]{magId, pdDate},
                new BeanPropertyRowMapper(MagCatalog.class));
        return list != null && list.size() > 0 ? list : null;
    }

    public MagazineArticle queryArticleByArticleId(String articleId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_TOPIC as title, a_subTitle as subTitle, a_leadTitle as leadTitle, SYS_AUTHORS as authors, " +
                "a_abstract as abstra, a_pubTime as pubTime, a_content as content, a_type as type from xy_magarticle where SYS_DOCUMENTID = ? ");
        List<MagazineArticle> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId}, new BeanPropertyRowMapper(MagazineArticle.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<MagazineArticle> queryMagazineArticles(Integer pageNo, Integer limit, String st, String et) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, SYS_TOPIC as title, a_content as content, SYS_CREATED as createTime FROM `xy_magarticle` where 1 = 1 ");
        this.extracted(sql, st, et);
        sql.append("order by SYS_CREATED desc ");
        if(pageNo != null && limit != null) {
            sql.append("limit ?, ? ");
        }
        List<MagazineArticle> list = jdbcTemplate.query(sql.toString(), new Object[]{(pageNo - 1) * limit, limit}, new BeanPropertyRowMapper(MagazineArticle.class));
        return list != null && list.size() > 0 ? list : null;
    }

	public Integer queryMagazineArticleTotal(String st, String et) {
        StringBuffer sql = new StringBuffer("SELECT count(1) FROM `xy_magarticle` where 1 = 1 ");
        this.extracted(sql, st, et);
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{}, Integer.class);
    }
    
    private void extracted(StringBuffer sql, String st, String et) {
        if (!StringUtils.isEmpty(st)) {
            sql.append("and SYS_CREATED > '").append(st).append(" 00:00:00' ");
        }
        if (!StringUtils.isEmpty(et)) {
            sql.append("and SYS_CREATED < '").append(et).append(" 23:59:59' ");
        }
    }

    public String queryEarlyCreateTime(String pdPpaperID) {
        List<Date> list = jdbcTemplate.query("SELECT MIN(SYS_CREATED) from xy_magarticle where a_magazineID = ? ", new Object[]{pdPpaperID},
                new BeanPropertyRowMapper(Date.class));
        return list != null && list.size() > 0 ? DateParseUtil.dateToString(list.get(0), DateParseUtil.DATETIME_STRICK) : null;
    }
}
