package com.founder.econdaily.modules.newspaper.entity;

public class PaperVo {
    private String plPaper;
    private String plPaperCode;
    private String plPaperID;
    private String PlDate;
    private String plUrl;
    private String plName;
    private String coverPic;
    private Integer layoutCount;

    public String getPlPaperCode() {
        return plPaperCode;
    }

    public void setPlPaperCode(String plPaperCode) {
        this.plPaperCode = plPaperCode;
    }

    public String getPlPaper() {
        return plPaper;
    }

    public void setPlPaper(String plPaper) {
        this.plPaper = plPaper;
    }

    public String getPlPaperID() {
        return plPaperID;
    }

    public void setPlPaperID(String plPaperID) {
        this.plPaperID = plPaperID;
    }

    public String getPlDate() {
        return PlDate;
    }

    public void setPlDate(String plDate) {
        PlDate = plDate;
    }

    public String getPlUrl() {
        return plUrl;
    }

    public void setPlUrl(String plUrl) {
        this.plUrl = plUrl;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Integer getLayoutCount() {
        return layoutCount;
    }

    public void setLayoutCount(Integer layoutCount) {
        this.layoutCount = layoutCount;
    }
}
