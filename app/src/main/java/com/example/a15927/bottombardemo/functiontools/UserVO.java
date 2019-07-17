package com.example.a15927.bottombardemo.functiontools;

import java.io.Serializable;
import java.util.Arrays;

public class UserVO implements Serializable {
    private int opType;//操作类型
    private String uid;
    private String uname;
    private String upassword;
    private byte[] uimage;
    private String uphone;
    private int sex;
    private String qq;
    private String weixin;
    private String token; // 查询或更新用户时，需要用到token

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public byte[] getUimage() {
        return uimage;
    }

    public void setUimage(byte[] uimage) {
        this.uimage = uimage;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "opType=" + opType +
                ", uid='" + uid + '\'' +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                ", uimage=" + Arrays.toString( uimage ) +
                ", uphone='" + uphone + '\'' +
                ", sex=" + sex +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
