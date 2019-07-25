package com.example.a15927.bottombardemo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

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

     //检验正整数
     public static boolean checkPlus(String str){
         String regEx = "^[1-9]\\d*$";
         Pattern p = Pattern.compile( regEx );
         Matcher m = p.matcher( str );
         if(m.matches()){
             return true;
         }else{
             return false;
         }
     }

    //检验中文
     public static boolean checkChinese(String str){
         String regEx = "[\\u4e00-\\u9fa5]";
         Pattern p = Pattern.compile( regEx );
         Matcher m = p.matcher( str );
         if(m.matches()){
             return true;
         }else{
             return false;
         }
     }

    //检验价钱
    public static boolean checkMoney(String str){
        String regEx = "^[0-9]+(.[0-9]{1,2})?$";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if(m.matches()){
            return true;
        }else{
            return false;
        }
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

    //密码(包含字母和数字的组合,长度在6-10之间)
    public static boolean checkPw(String str){
        String regEx = "^[A-Za-z0-9]{6,10}$";
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
    public static boolean checkIllegal(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if (m.find()) {
            return false;
        }else{
            return true;
        }
    }

    /*
     *删除输入框非法字符
     */
    public static void deleteIllegal(final EditText editText, final Context context){
        editText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = editText.getText().toString();
                String regEx = "[^a-zA-Z0-9]";  //只能输入字母或数字
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(editable);
                String str = m.replaceAll("").trim();    //删掉不是字母或数字的字符
                if(!editable.equals(str)){
                    Toast.makeText( context, "禁止输入非法字符！", Toast.LENGTH_SHORT ).show();
                    editText.setText(str);  //设置EditText的字符
                    editText.setSelection(str.length()); //因为删除了字符，要重写设置新的光标所在位置
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
    }

}
