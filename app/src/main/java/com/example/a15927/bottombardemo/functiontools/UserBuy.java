package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/28.
 */

public class UserBuy {
    private int opType;
    private String token;
    private int count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
                ", count=" + count +
                ", page=" + page +
                '}';
    }
}
