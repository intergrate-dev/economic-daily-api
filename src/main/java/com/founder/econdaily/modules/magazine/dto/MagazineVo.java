package com.founder.econdaily.modules.magazine.dto;

import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.modules.magazine.entity.Magazine;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MagazineVo {
    private String magId;
    private String magName;
    private String magCode;
    private String PdDate;
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

    public String getPdDate() {
        return PdDate;
    }

    public void setPdDate(String pdDate) {
        PdDate = pdDate;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getMagCode() {
        return magCode;
    }

    public void setMagCode(String magCode) {
        this.magCode = magCode;
    }

    /*public static MagazineVo parseEntity(Magazine magazine) {
        MagazineVo mv = new MagazineVo();
        mv.setPdDate(DateParseUtil.dateToString(magazine.getPdDate()));
        mv.setCoverPic(magazine.getCoverPic());
        mv.setMagName(magazine.getPdJName());
        return mv;
    }*/
}
