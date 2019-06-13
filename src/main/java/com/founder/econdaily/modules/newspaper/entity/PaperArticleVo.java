package com.founder.econdaily.modules.newspaper.entity;

import java.util.Date;
import java.util.List;

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
    private String layoutId;
    private List<String> pics;

    public List<String> getContPics() {
        return contPics;
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
}
