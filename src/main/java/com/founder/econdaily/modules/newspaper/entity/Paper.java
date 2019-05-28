package com.founder.econdaily.modules.newspaper.entity;

import java.util.Date;

public class Paper {

    private String id;
    private String paperName;
    private Date paperCode;

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

    public Date getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(Date paperCode) {
        this.paperCode = paperCode;
    }
}
