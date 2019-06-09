package com.example.a15927.bottombardemo.sortactivity;

/**
 * Created by Administrator on 2019/6/9.
 */

public class SortGoodsRo {

    private String token;
    private String goodsType; //可以根据类别查义
    private String goodsName; //可以根据商品名称
    private int QueryType;    //查询类型 1-------按类型查询               2---------按名称查询

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getQueryType() {
        return QueryType;
    }

    public void setQueryType(int queryType) {
        QueryType = queryType;
    }
}
