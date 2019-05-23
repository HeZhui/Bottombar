package com.example.a15927.bottombardemo.functiontools;

import java.util.Arrays;

/**
 * Created by Administrator on 2019/3/27.
 */

public class GoodsPut {
    private int opType;     //操作类型（发布，维护等）
    private String goodsID;  //ID
    private String goodsType; //商品所属类
    private String goodsName; //商品名
    private float price;       // 价格
    private String unit;      //单位
    private float quality;   //数量
    private String userid;   //发布人ID
    private byte[] goodsImg; //商品图片
    private String uname;
    private String uphone;
    private String qq;
    private String weixin;
    private String token;    //token
    //private String goodsTypeName; //所属类名（可选字段）
    private int sex;

    public int getSex() {
        return sex;
    }

    public String getUname() {
        return uname;
    }

    public String getUphone() {
        return uphone;
    }

    public String getQq() {
        return qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public int getOpType() {
        return opType;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public float getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public float getQuality() {
        return quality;
    }

    public String getUserid() {
        return userid;
    }

    public byte[] getGoodsImg() {
        return goodsImg;
    }

    public String getToken() {
        return token;
    }

//    public String getGoodsTypeName() {
//        return goodsTypeName;
//    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setGoodsImg(byte[] goodsImg) {
        this.goodsImg = goodsImg;
    }

    public void setToken(String token) {
        this.token = token;
    }

//    public void setGoodsTypeName(String goodsTypeName) {
//        this.goodsTypeName = goodsTypeName;
//    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "{" +
                "opType=" + opType +
                ", goodsID='" + goodsID + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", quality=" + quality +
                ", userid='" + userid + '\'' +
                ", goodsImg=" + Arrays.toString( goodsImg ) +
                ", uname='" + uname + '\'' +
                ", uphone='" + uphone + '\'' +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", token='" + token + '\'' +
                ", sex=" + sex +
                '}';
    }

}
