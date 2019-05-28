package com.founder.econdaily.modules.newspaper.entity;

import javax.validation.constraints.Pattern;

public class NewsPaperParam {
    @Pattern(regexp = "^[0-9]+$", message = "参数attachId格式不正确")
    private String attachId;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "参数paperCode格式不正确")
    private String paperCode;
    @Pattern(regexp="^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message="参数plDate格式不正确")
    private String plDate;
    @Pattern(regexp = "^[0-9]+$", message = "参数layout格式不正确")
    private String layout;
    @Pattern(regexp = "^[0-9]+$", message = "参数articleId格式不正确")
    private String articleId;

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getPlDate() {
        return plDate;
    }

    public void setPlDate(String plDate) {
        this.plDate = plDate;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
