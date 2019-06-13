package com.founder.econdaily.modules.magazine.repository;

import com.founder.econdaily.modules.magazine.dto.MagazineVo;
import com.founder.econdaily.modules.magazine.entity.MagCatalog;
import com.founder.econdaily.modules.magazine.entity.MagazineArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public List<MagCatalog> queryCatalogsByPdDate(String pdDate, String magId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GROUP_CONCAT(SYS_TOPIC) as artiTopics, a_column as columnName " +
                "FROM `xy_magarticle` where a_magazineID = ? and a_pubTime = ? group by a_columnID " +
                "order by SYS_DOCUMENTID DESC ");
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


    public List<MagazineArticle> queryMagazineArticles(Integer pageNo, Integer limit) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, SYS_TOPIC as title, a_content as content FROM `xy_magarticle` ")
                .append("limit ?, ?");
        List<MagazineArticle> list = jdbcTemplate.query(sql.toString(), new Object[]{(pageNo - 1) * limit, limit}, new BeanPropertyRowMapper(MagazineArticle.class));
        return list != null && list.size() > 0 ? list : null;
    }

    public Integer queryMagazineArticleTotal() {
        StringBuffer sql = new StringBuffer("SELECT count(1) FROM `xy_magarticle` ");
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{}, Integer.class);
    }

}
