package com.yff.base.query;


abstract public class QueryObject {

    //当前页码
    private Integer pageNum = 1;
    //每一页大大小
    private Integer pageSize = 5;

    //计算查询记录的起始值
    public int getStart() {
        return (pageNum - 1) * pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
