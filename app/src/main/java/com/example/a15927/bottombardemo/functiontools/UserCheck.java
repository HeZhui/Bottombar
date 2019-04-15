package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/26.
 */

public class UserCheck {
    private int opType;//操作类型
    private String uid;
    private String uname;
    private String uphone;
    private int sex;
    private String qq;
    private String weixin;
    private String token; // 查询或更新用户时，需要用到token

    public int getOpType() {
        return opType;
    }

    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getUphone() {
        return uphone;
    }

    public int getSex() {
        return sex;
    }

    public String getQq() {
        return qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public String getToken() {
        return token;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "opType=" + opType +
                ", uid='" + uid + '\'' +
                ", uname='" + uname + '\'' +
                ", uphone='" + uphone + '\'' +
                ", sex=" + sex +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
