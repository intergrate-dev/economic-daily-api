package com.founder.econdaily.modules.newspaper.repository;

import com.founder.econdaily.modules.newspaper.entity.PaperAttachment;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class PaperAttachmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(NewsPaperRepository.class);

    public String findCoverByArticle(String articleId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GROUP_CONCAT(att_url) as attUrl FROM `xy_paperattachment` where att_articleID = ? ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : "";
    }

    public List<PaperAttachment> findAttchsByArticleIdAndLibId(String articleId, String libId, String attType) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, att_url as attUrl FROM xy_paperattachment where att_articleID = ? ")
                .append("and att_articleLibID = ? and att_type = ? ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId, libId, attType}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list;
    }

    public String findCoverByArticleIdAndLibId(String articleId, String libId, String attType) {
        List<PaperAttachment> list = this.findAttchsByArticleIdAndLibId(articleId, libId, attType);
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : null;
    }

    public List<PaperAttachment> findOtherPlPicAndPdf(String articleIds, String libId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GROUP_CONCAT(att_url) attUrl,att_articleID articleID, SYS_DOCUMENTID id from (SELECT att_articleID, ")
                .append("att_url, att_type, SYS_DOCUMENTID FROM xy_paperattachment where att_articleID in (" + articleIds +
                ") and att_articleLibID = :libId) aa GROUP BY att_articleID ");
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("libId", libId);
        PaperAttachment att = null;
        List<PaperAttachment> list = new ArrayList<PaperAttachment>();
        List<PaperAttachment> mList = namedParameterJdbcTemplate.query(sql.toString(), parameters, new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                PaperAttachment att = new PaperAttachment();
                att.setAttUrl(rs.getString("attUrl"));
                att.setId(rs.getString("id"));
                return att;
            }
        });
        return mList;
    }

}
