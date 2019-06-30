package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/26.
 */

public class UserCheck {
    private int state;
    private int opType;//操作类型
    private String username;
    private String password;
    private byte[] img;
    private String uphone;
    private int sex;
    private String qq;
    private String weixin;
    private String token; // 查询或更新用户时，需要用到token

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
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
}
