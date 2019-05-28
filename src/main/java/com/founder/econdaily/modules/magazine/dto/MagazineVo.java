package com.founder.econdaily.modules.magazine.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MagazineVo {
    private String magId;
    private String magName;
    private Date PdDate;
    private String coverPic;
    private List<Map<String, Object>> magCatalogs;

    public List<Map<String, Object>> getMagCatalogs() {
        return magCatalogs;
    }

    public void setMagCatalogs(List<Map<String, Object>> magCatalogs) {
        this.magCatalogs = magCatalogs;
    }

    public String getMagId() {
        return magId;
    }

    public void setMagId(String magId) {
        this.magId = magId;
    }

    public String getMagName() {
        return magName;
    }

    public void setMagName(String magName) {
        this.magName = magName;
    }

    public Date getPdDate() {
        return PdDate;
    }

    public void setPdDate(Date pdDate) {
        PdDate = pdDate;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }
}
