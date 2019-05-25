package com.example.a15927.bottombardemo.functiontools;

import java.util.Arrays;

public class UserVO {
    private int opType;//操作类型
    private String uid;
    private String uname;
    private String upassword;
    private byte[] uimage;
    private String uphone;
    private int sex;
    private String qq;
    private String weixin;
//
//    private String token; // 查询或更新用户时，需要用到token


    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public void setUimage(byte[] uimage) {
        this.uimage = uimage;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getOpType() {
        return opType;
    }

    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public byte[] getUimage() {
        return uimage;
    }

    public String getUphone() {
        return uphone;
    }

    public int getSex() {
        return sex;
    }

    public String getuname() {
        return uname;
    }

    public String getupassword() {
        return upassword;
    }

    public void setuname(String uname) {
        this.uname = uname;
    }

    public void setupassword(String upassword) {
        this.upassword = upassword;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    @Override
    public String toString() {
        return "{" +
                "opType=" + opType +
                ", uid='" + uid + '\'' +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                ", uimage=" + Arrays.toString( uimage ) +
                ", uphone='" + uphone + '\'' +
                ", sex=" + sex +
                '}';
    }
}
