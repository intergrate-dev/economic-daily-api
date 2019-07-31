package com.founder.econdaily.modules.newspaper.entity;

import java.util.Date;
import java.util.List;

public class ArticleMapping {

    private String articleID;
    private List<String> mapping;

	public String getArticleID() {
		return articleID;
    }
    
	public List<String> getMapping() {
		return mapping;
    }
    
	public void setMapping(List<String> mapping) {
		this.mapping = mapping;
    }
    
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}


}