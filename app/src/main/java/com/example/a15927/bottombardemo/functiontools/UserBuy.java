package com.example.a15927.bottombardemo.functiontools;

public class UserBuy {
    private int opType;
    private String token;
    private int pageSize;
    private int page;
    private int checkType; //查询方式       1----------加载               2----------------刷新
    private int condition;//区别是摊位（ 1 ），还是求购（ 2 ）
    private String userid;
    public int getOpType() {
        return opType;
    }

    public String getToken() {
        return token;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
