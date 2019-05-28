package com.founder.econdaily.common.entity;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pageNo;
    private int pageSize;
    private long totalCount;
    private long totalPage;
    private List<T> result;


    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

	public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalPage = totalCount / this.pageSize + 1;
        this.totalCount = totalCount;
    }

	public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    /*public NextPageData getNextPageData() {
        return nextPageData;
    }

    public void setNextPageData(NextPageData nextPageData) {
        this.nextPageData = nextPageData;
    }*/

    public static void main(String[] args) {
        Integer total = 483;
        System.out.println(total/10 + 1);
    }
}
