package com.founder.econdaily.modules.newspaper.entity;

import java.util.Date;
import java.util.List;

import com.founder.econdaily.common.util.DateParseUtil;

public class PaperArticleVo {

    private String id;
    private String title;
    private String subTitle;
    private String leadTitle;
    private String content;
    private String authors;
    private String source;
    private String paperName;
    private String abstra;
    private String plDate;
    private String pubTime;
    private String createTime;
    private String layoutId;
    private List<String> pics;

    public List<String> getContPics() {
        return contPics;
    }

    public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setContPics(List<String> contPics) {
        this.contPics = contPics;
    }

    private List<String> contPics;

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
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

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
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

    public String getPlDate() {
        return plDate;
    }

    public void setPlDate(String plDate) {
        this.plDate = plDate;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public static PaperArticleVo parseEntityWithArticle(PaperArticle paperArticle) {
        PaperArticleVo pav = new PaperArticleVo();
        pav.setAbstra(paperArticle.getAbstra());
        pav.setAuthors(paperArticle.getAuthors());
        pav.setContent(paperArticle.getContent());
        pav.setId(paperArticle.getId());
        pav.setLayoutId(paperArticle.getLayoutId());
        pav.setLeadTitle(paperArticle.getLeadTitle());
        pav.setPaperName(paperArticle.getPaperName());
        if (paperArticle.getPlDate() != null) {
            pav.setPlDate(DateParseUtil.dateToString(paperArticle.getPlDate(), DateParseUtil.DATETIME_STRICK));
        }
        if (paperArticle.getCreateTime() != null) {
            pav.setCreateTime(DateParseUtil.dateToString(paperArticle.getCreateTime(), DateParseUtil.DATETIME_STRICK));
        }
        if (paperArticle.getPubTime() != null) {
            pav.setPubTime(DateParseUtil.dateToString(paperArticle.getPubTime(), DateParseUtil.DATETIME_STRICK));
        }
        pav.setPics(paperArticle.getPics());
        pav.setContPics(paperArticle.getPics());
        pav.setSubTitle(paperArticle.getSubTitle());
        pav.setSource(paperArticle.getSource());
        return pav;
    }
}
