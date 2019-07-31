package com.founder.econdaily.modules.magazine.entity;

import java.util.Date;

public class ArticleTopic {

    private String id;
    private String title;

	public String getId() {
		return id;
    }
    
	public String getTitle() {
		return title;
    }
    
	public void setTitle(String title) {
		this.title = title;
    }
    
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ArticleTopic [id=" + id + ", title=" + title + "]";
	}
    
	
}
