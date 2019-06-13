package com.founder.econdaily.modules.newspaper.entity;

public class PaperAttachment {

    public static final String ATT_TYPE_PDF_LAYOUT = "6";
    public static final String ATT_TYPE_COVER_LAYOUT = "5";
    public static final String ATT_TYPE_COVER_ARTICLE = "0";

    private String id;
    private String articleID;
    private String attType;
    private String attUrl;
    private String attContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getAttType() {
        return attType;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public String getAttUrl() {
        return attUrl;
    }

    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    public String getAttContent() {
        return attContent;
    }

    public void setAttContent(String attContent) {
        this.attContent = attContent;
    }
}
