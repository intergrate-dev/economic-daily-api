package com.founder.econdaily.modules.auth.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author yuan-pc
 */
@Entity
@Table(name = "v_user")
@Data
public class VUserEntity {
    @Id
    @Column(name = "uid")
    private int uid;
    @Column(name = "uname")
    private String uname;
    @Column(name = "desc")
    private String desc;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/
}
