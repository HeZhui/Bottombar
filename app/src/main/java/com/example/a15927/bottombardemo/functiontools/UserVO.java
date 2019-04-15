package com.example.a15927.bottombardemo.functiontools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by Administrator on 2019/3/19.
 */

public class UserVO {
    private int opType;//操作类型
    private String uid;
    private String uname;
    private String upassword;
    private byte[] uimage;
    private String uphone;
    private int sex;
//    private String qq;
//    private String weixin;
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

    // bitmap转bytes
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    // bytes转bitmap
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    // bitmap 缩放
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
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
