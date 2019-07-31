package com.founder.econdaily.modules.magazine.entity;

public class CommonParam extends BaseParam {
    private Integer pageNo;
    private Integer limit;
    
    public Integer getPageNo() {
		return pageNo;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}


}
