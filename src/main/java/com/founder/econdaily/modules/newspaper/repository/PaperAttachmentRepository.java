package com.founder.econdaily.modules.newspaper.repository;

import com.founder.econdaily.modules.newspaper.entity.PaperAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaperAttachmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    private List<PaperAttachment> findAttchsByArticleIdAndLibId(String articleId, String libId, String attType) {
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

    /*public String findMainBodyPicByArticleIdAndLibId(String articleId, String libId) {
        List<PaperAttachment> list = this.findAttchsByArticleIdAndLibId(articleId, libId, PaperAttachment.ATT_TYPE_COVER_LAYOUT);
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : null;
    }*/
}
