package com.founder.econdaily.modules.magazine.entity;

import javax.validation.constraints.Pattern;

public class MagazineParam {
    @Pattern(regexp = "^[a-zA-Z]+$", message = "参数paperCode格式不正确")
    private String magCode;
    @Pattern(regexp="^[0-9]{4}-[0-9]{2}-[0-9]{2}$", message="参数pdDate格式不正确")
    private String pdDate;
    @Pattern(regexp="^[0-9]{4}$", message="参数year格式不正确")
    private String year;
    @Pattern(regexp = "^[0-9]+$", message = "参数articleId格式不正确")
    private String articleId;

    public String getMagCode() {
        return magCode;
    }

    public void setMagCode(String magCode) {
        this.magCode = magCode;
    }

    public String getPdDate() {
        return pdDate;
    }

    public void setPdDate(String pdDate) {
        this.pdDate = pdDate;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
