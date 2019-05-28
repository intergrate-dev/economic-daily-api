package com.founder.econdaily.modules.magazine.entity;

import java.util.Date;

public class Magazine {
    public static final String LAYOUT_LIB_ID = "60";

    private String id;
    private String magName;
    private Date magCode;

    private String pdPpaperID;
    private String pdJName;
    private Date PdDate;
    private String pdUrl;
    private String coverPic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMagName() {
        return magName;
    }

    public void setMagName(String magName) {
        this.magName = magName;
    }

    public Date getMagCode() {
        return magCode;
    }

    public void setMagCode(Date magCode) {
        this.magCode = magCode;
    }

    public String getPdPpaperID() {
        return pdPpaperID;
    }

    public void setPdPpaperID(String pdPpaperID) {
        this.pdPpaperID = pdPpaperID;
    }

    public String getPdJName() {
        return pdJName;
    }

    public void setPdJName(String pdJName) {
        this.pdJName = pdJName;
    }

    public Date getPdDate() {
        return PdDate;
    }

    public void setPdDate(Date pdDate) {
        PdDate = pdDate;
    }

    public String getPdUrl() {
        return pdUrl;
    }

    public void setPdUrl(String pdUrl) {
        this.pdUrl = pdUrl;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }
}
