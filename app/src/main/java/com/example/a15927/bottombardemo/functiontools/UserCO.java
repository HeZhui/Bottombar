package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/24.
 */

public class UserCO {
    private int flag;
    private String message;
    private String token;

    public int getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
