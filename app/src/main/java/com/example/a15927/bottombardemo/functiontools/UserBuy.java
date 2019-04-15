package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/28.
 */

public class UserBuy {
    private int opType;
    private String token;

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

    @Override
    public String toString() {
        return "{" +
                "opType=" + opType +
                ", token='" + token + '\'' +
                '}';
    }
}
