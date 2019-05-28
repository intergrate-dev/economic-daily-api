package com.founder.econdaily.modules.newspaper.entity;

import java.io.Serializable;
import java.util.Date;

public class PaperLayout implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String LAYOUT_LIB_ID = "49";

    private String id;
    private String plPaper;
    private String plPaperID;
    private Date PlDate;
    private String plUrl;
    private String plName;
    private String coverPic;
    private Integer layoutCount;

    public PaperLayout() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getPlDate() {
        return PlDate;
    }

    public void setPlDate(Date plDate) {
        PlDate = plDate;
    }

    public String getPlUrl() {
        return plUrl;
    }

    public void setPlUrl(String plUrl) {
        this.plUrl = plUrl;
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

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }

    public PaperLayout(String plPaper, String plPpaperID, String plName, Integer layoutCount) {
        this.plPaper = plPaper;
        this.plPaperID = plPpaperID;
        this.plName = plName;
        this.layoutCount = layoutCount;
    }
}
