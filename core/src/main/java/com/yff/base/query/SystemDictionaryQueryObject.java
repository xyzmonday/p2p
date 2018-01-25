package com.yff.base.query;

public class SystemDictionaryQueryObject extends QueryObject{
    private String keyword;
    private String parentId;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
