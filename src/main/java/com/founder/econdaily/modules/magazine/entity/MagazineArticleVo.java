package com.founder.econdaily.modules.magazine.entity;

import com.founder.econdaily.common.util.DateParseUtil;

import java.util.Date;
import java.util.List;

public class MagazineArticleVo {
    public static final String ARTICLE_LIB_ID = "63";

    private String id;
    private String title;
    private String subTitle;
    private String leadTitle;
    private String content;
    private String authors;
    private String source;
    private String paperName;
    private String abstra;
    private String pubTime;
    private String createTime;
    private String type;
    private List<String> pics;

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLeadTitle() {
        return leadTitle;
    }

    public void setLeadTitle(String leadTitle) {
        this.leadTitle = leadTitle;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAbstra() {
        return abstra;
    }

    public void setAbstra(String abstra) {
        this.abstra = abstra;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static MagazineArticleVo parseEntity(MagazineArticle magArticle) {
        MagazineArticleVo magArtiVo = new MagazineArticleVo();
        magArtiVo.setAbstra(magArticle.getAbstra());
        magArtiVo.setAuthors(magArticle.getAuthors());
        magArtiVo.setContent(magArticle.getContent());
        if (magArticle.getCreateTime() != null) {
            magArtiVo.setCreateTime(DateParseUtil.dateToString(magArticle.getCreateTime(), DateParseUtil.DATETIME_STRICK));
        }
        if (magArticle.getPubTime() != null) {
            magArtiVo.setPubTime(DateParseUtil.dateToString(magArticle.getPubTime(), DateParseUtil.DATETIME_STRICK));
        }
        magArtiVo.setId(magArticle.getId());
        magArtiVo.setLeadTitle(magArticle.getLeadTitle());
        magArtiVo.setPaperName(magArticle.getPaperName());
        magArtiVo.setPics(magArticle.getPics());
        magArtiVo.setType(magArticle.getType());
        magArtiVo.setSubTitle(magArticle.getSubTitle());
        magArtiVo.setTitle(magArticle.getTitle());
        magArtiVo.setSource(magArticle.getSource());
        return magArtiVo;
    }
}
