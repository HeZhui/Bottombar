package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/27.
 */

public class GoodsBack {
    private int flag;
    private String message;

    public int getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "flag=" + flag +
                ", message='" + message + '\'' +
                '}';
    }
}
