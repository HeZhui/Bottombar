package com.example.a15927.bottombardemo.functiontools;

import android.util.Log;

/**
 * Created by Administrator on 2019/3/22.
 */

public class MyLog {
    private static final String TAG = "Test";
    private static boolean debug = false;//开关

    //使用构造函数初始化
    public MyLog(){}

    public static void print(String msg) {
        if (debug) {
            Log.i(TAG, msg);
        }
    }
}
