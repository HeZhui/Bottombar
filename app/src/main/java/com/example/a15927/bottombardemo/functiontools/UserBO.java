package com.example.a15927.bottombardemo.functiontools;

/**
 * Created by Administrator on 2019/3/24.
 */

public class UserBO {
    private int opType;
    private String uname;
    private  String upassword;

    public int getOpType() {
        return opType;
    }

    public String getUname() {
        return uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    @Override
    public String toString() {
        return "{" +
                "opType=" + opType +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                '}';
    }
}
