package com.example.a15927.bottombardemo.functiontools;

public class UserBuy {
    private int opType;
    private String token;
    private int pageSize;
    private int page;

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

    @Override
    public String toString() {
        return "UserBuy{" +
                "opType=" + opType +
                ", token='" + token + '\'' +
                ", pageSize=" + pageSize +
                ", page=" + page +
                '}';
    }
}
