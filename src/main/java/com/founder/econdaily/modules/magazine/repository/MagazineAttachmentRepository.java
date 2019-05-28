package com.founder.econdaily.modules.magazine.repository;

import com.founder.econdaily.modules.magazine.entity.MagAttachment;
import com.founder.econdaily.modules.newspaper.entity.PaperArticle;
import com.founder.econdaily.modules.newspaper.entity.PaperAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MagazineAttachmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(MagazineRepository.class);


    /**
     * 期刊版面图片
     * @param articleId
     * @param libId
     * @param objId
     * @param attType
     * @return
     */
    private List<PaperAttachment> findAttchsByArticleIdAndLibId(String articleId, String libId, String objId, String attType) {
        /*SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
        where att_articleLibID = 60
        and att_articleID = 1
        and att_objID = 20151216
        and att_type = 5
        limit 1*/
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SYS_DOCUMENTID as id, att_url as attUrl FROM xy_attachment where att_articleID = ? ")
                .append("and att_articleLibID = ? and att_objID = ? and att_type = ? ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId, libId, objId, attType}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list;
    }

    public String findCoverByArticleIdAndLibId(String articleId, String libId, String objId) {
        List<PaperAttachment> list = this.findAttchsByArticleIdAndLibId(articleId, libId, objId, MagAttachment.ATT_TYPE_COVER);
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : null;
    }

    private List<PaperAttachment> queryCoversByOtherPdDay(String articleId, String libId, String objId, String attType) {
        StringBuffer sql = new StringBuffer();
        String ss = objId.substring(0, 5).concat("%");
        sql.append("SELECT SYS_DOCUMENTID as id, att_url as attUrl FROM xy_attachment where att_articleID = ? ")
                .append("and att_articleLibID = ? and att_objID LIKE ? and att_objID <> ? and att_type = ? group by att_objID ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId, libId, ss, objId, attType}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list;
    }

    /**
     * 当年往期
     * @param articleId
     * @param libId
     * @param objId
     * @return
     */
    public String queryCoversByOtherPdDay(String articleId, String libId, String objId) {
        List<PaperAttachment> list = this.queryCoversByOtherPdDay(articleId, libId, objId, MagAttachment.ATT_TYPE_COVER);
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : null;
    }

    private List<PaperAttachment> queryCoversAll(String articleId, String libId, String objId, String attType) {
        StringBuffer sql = new StringBuffer();
        String ss = objId.substring(0, 5).concat("%");
        sql.append("SELECT SYS_DOCUMENTID as id, att_url as attUrl FROM xy_attachment where att_articleID = ? ")
                .append("and att_articleLibID = ? and att_objID LIKE ? and att_type = ? group by att_objID ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId, libId, ss, attType}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list;
    }

    /**
     * 当年全部
     * @param articleId
     * @param libId
     * @param objId
     * @return
     */
    public String queryCoversAll(String articleId, String libId, String objId) {
        List<PaperAttachment> list = this.queryCoversAll(articleId, libId, objId, MagAttachment.ATT_TYPE_COVER);
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : null;
    }

    public String findCoverByArticle(String articleId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GROUP_CONCAT(att_url) as attUrl FROM `xy_attachment` where att_articleID = ? ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : "";
    }

    /**
     * 期刊文章图片
     * @param articleId
     * @param libId
     * @return
     */
    public String queryArticlePicsByArticleIdAndLibId(String articleId, String libId) {
        /*SELECT SYS_DOCUMENTID AS id, att_url AS attUrl, att_articleLibID, att_objID, att_type
        FROM xy_attachment WHERE att_articleID = 589888 AND att_articleLibID = 63*/
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT GROUP_CONCAT(att_url) as attUrl FROM xy_attachment where att_articleID = ? ")
                .append("and att_articleLibID = ? ");
        List<PaperAttachment> list = jdbcTemplate.query(sql.toString(), new Object[]{articleId, libId}, new BeanPropertyRowMapper(PaperAttachment.class));
        return list != null && list.size() > 0 ? list.get(0).getAttUrl() : "";
    }
}
