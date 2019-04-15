package com.example.a15927.bottombardemo.functiontools;

import java.util.List;

/**
 * Created by Administrator on 2019/3/28.
 */

public class Goods extends ItemGoods {
    private List<ItemGoods> goodsList;
    private int flag;
    private String message;
    private String token;

    public List<ItemGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ItemGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "goodsList=" + goodsList +
                ", flag=" + flag +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
