package com.founder.econdaily.modules.newspaper.repository;

import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.modules.newspaper.entity.Paper;
import com.founder.econdaily.modules.newspaper.entity.PaperArticle;
import com.founder.econdaily.modules.newspaper.entity.PaperLayout;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class NewsPaperRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(NewsPaperRepository.class);

    /**
     * 查询所有报纸最新一期的版面信息
     *
     * @return
     */
    public List<PaperLayout> findNewtestPaper() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id , pl_paper as plPaper, pl_paperID as plPaperID, pl_date as plDate from ( ")
                .append("SELECT /* parallel */ pl_paperID, SYS_DOCUMENTID, pl_paper, pl_date FROM `xy_paperlayout` order by pl_date DESC limit 100000 ")
                .append(") aa group by aa.pl_paperID");
        List<PaperLayout> list = null;
        list = jdbcTemplate.query(sql.toString(), new Object[]{}, new BeanPropertyRowMapper(PaperLayout.class));
        return list;
    }

    public List<PaperLayout> findByPaperIdAndPlDate(String paperId, Date plDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id , pl_paper as plPaper, pl_paperID as plPaperID, pl_date as plDate ")
                .append("FROM xy_paperlayout where pl_paperID = ? and pl_date = ? group by pl_layout ");
        List<PaperLayout> list = null;
        list = jdbcTemplate.query(sql.toString(), new Object[]{paperId, plDate}, new BeanPropertyRowMapper(PaperLayout.class));
        return list;
    }

    public PaperLayout queryArticleAndPaperDate(String attachId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select arti.a_paper as plPaper, lay.pl_date as plDate from xy_paperattachment as att ")
                .append("join xy_paperarticle as arti on att.att_articleID = arti.SYS_DOCUMENTID ")
                .append("join xy_paperlayout as lay on arti.a_layoutID = lay.SYS_DOCUMENTID where att.SYS_DOCUMENTID = ? ");
        List<PaperLayout> list = null;
        list = jdbcTemplate.query(sql.toString(), new Object[]{attachId}, new BeanPropertyRowMapper(PaperLayout.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }


    public Paper queryPaperByCode(String paperCode) {
        StringBuffer sql = new StringBuffer();
        sql.append("select SYS_DOCUMENTID as id, pa_name as paperName from xy_paper where pa_code = ? ");
        List<Paper> list = jdbcTemplate.query(sql.toString(), new Object[]{paperCode}, new BeanPropertyRowMapper(Paper.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public Paper queryPaperByName(String paperName) {
        StringBuffer sql = new StringBuffer();
        sql.append("select SYS_DOCUMENTID as id, pa_code as paperCode from xy_paper where pa_name = ? ");
        List<Paper> list = jdbcTemplate.query(sql.toString(), new Object[]{paperName}, new BeanPropertyRowMapper(Paper.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<PaperLayout> queryCatalogsByPlDate(String plDate, String paperId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, pl_layoutName as plName from xy_paperlayout where pl_paperID = ? ")
                .append("and pl_date = ? ");
        // DateParseUtil.dateToString(plDate)
        List<PaperLayout> list = jdbcTemplate.query(sql.toString(), new Object[]{paperId, plDate},
                new BeanPropertyRowMapper(PaperLayout.class));
        return list != null && list.size() > 0 ? list : null;
    }

    public List<PaperArticle> queryContentsByTopLayoutId(String layoutId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, SYS_TOPIC as title, a_content as content FROM `xy_paperarticle` ")
                .append("where a_layoutID = ? ");
        List<PaperArticle> list = jdbcTemplate.query(sql.toString(), new Object[]{layoutId}, new BeanPropertyRowMapper(PaperArticle.class));
        return list != null && list.size() > 0 ? list : null;
    }

    public PaperLayout queryByPaperIdAndPlDate(String plDate, String paperId, String layout) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, pl_layout as layout, pl_layoutName as plName, pl_date as plDate ")
                .append("FROM xy_paperlayout where pl_paperID = ? and pl_date = ? and pl_layout = ? ");
        List<PaperLayout> list = jdbcTemplate.query(sql.toString(), new Object[]{Integer.parseInt(paperId), plDate,
                Integer.parseInt(layout)}, new BeanPropertyRowMapper(PaperLayout.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public PaperArticle queryByArticleId(String articleId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select SYS_DOCUMENTID as id, SYS_TOPIC as title, a_subTitle as subTitle, a_leadTitle as leadTitle, a_abstract as abstra, " +
                "a_source as source, SYS_AUTHORS as authors, a_pubTime as pubTime, a_layoutID as layoutId from xy_paperarticle " +
                "where SYS_DOCUMENTID = ? ");
        List<PaperArticle> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId}, new BeanPropertyRowMapper(PaperArticle.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }


    public List<PaperArticle> queryPaperArticles(Integer pageNo, Integer limit) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, SYS_TOPIC as title, a_content as content FROM `xy_paperarticle` ")
                .append("limit ?, ?");
        List<PaperArticle> list = jdbcTemplate.query(sql.toString(), new Object[]{(pageNo - 1) * limit, limit}, new BeanPropertyRowMapper(PaperArticle.class));
        return list != null && list.size() > 0 ? list : null;
    }

    public Integer queryPaperArticleTotal() {
        StringBuffer sql = new StringBuffer("SELECT count(1) FROM `xy_paperarticle` ");
        return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{}, Integer.class);
    }

    public PaperLayout queryLayoutById(String layoutId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, pl_layout as layout, pl_layoutName as plName, pl_date as plDate, pl_paperID as plPaperID ")
                .append("FROM xy_paperlayout where SYS_DOCUMENTID = ? ");
        List<PaperLayout> list = jdbcTemplate.query(sql.toString(), new Object[]{Integer.parseInt(layoutId)}, new BeanPropertyRowMapper(PaperLayout.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

}
