package com.example.a15927.bottombardemo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2019/7/20.
 */

public class TestAndVerify {

    //服务器出错 or 网络出错
    public static  String  judgeError(Context context){
        String  responseError = null;
        int netSituation = getConnectedType( context );
        if(netSituation == 1){
            responseError = "服务器开小差喽！";
        }else{
            responseError = "亲，当前网络已断开！";
        }
        return responseError;
    }

    //测试网络状态
     public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
     }

    //检验腾讯QQ
    public static boolean checkQQ(String str){
        String regEx = "[1-9][0-9]{4,}";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    //检验微信号（数字及字母的组合）
    public static boolean checkWeixin(String str){
        String regEx = "^[A-Za-z0-9]+$";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    //强密码(必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间)
    public static boolean checkPw(String str){
        String regEx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    //正则表达式验证手机号码
    public static boolean checkPhone(String str) {
        if (str == null) {
            return false;
        }
        String regEx = "^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\\d{8}$";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    //是否输入非法字符
    public static boolean checkName(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if (m.find()) {
           return false;
        }else{
            return true;
        }
    }
}
