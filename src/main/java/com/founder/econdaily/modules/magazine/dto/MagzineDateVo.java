package com.founder.econdaily.modules.magazine.dto;

import java.util.Date;

public class MagzineDateVo {
    private String magId;
    private String latestYear;
    private String magName;
    private Date pdDate;

    public String getMagId() {
        return magId;
    }

    public void setMagId(String magId) {
        this.magId = magId;
    }

    public Date getPdDate() {
        return pdDate;
    }

    public void setPdDate(Date pdDate) {
        this.pdDate = pdDate;
    }

    public String getLatestYear() {
        return latestYear;
    }

    public void setLatestYear(String latestYear) {
        this.latestYear = latestYear;
    }

    public String getMagName() {
        return magName;
    }

    public void setMagName(String magName) {
        this.magName = magName;
    }
}
