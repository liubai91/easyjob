package com.easyjob.dto;

public class JobCriteria {

    private String name;

    private Integer pageCur;

    private Integer pageSize;

    private String state;

    private Boolean enable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageCur() {
        return pageCur;
    }

    public void setPageCur(Integer pageCur) {
        this.pageCur = pageCur;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
