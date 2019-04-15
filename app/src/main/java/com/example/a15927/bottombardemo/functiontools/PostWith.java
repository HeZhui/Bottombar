package com.example.a15927.bottombardemo.functiontools;

import android.util.Log;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2019/3/21.
 */

public class PostWith {
    public static String TAG = "Test";

    //okhttp3  post网络请求函数
    public static void  sendPostWithOkhttp( String url,  String JsonStr,okhttp3.Callback callback){//
        //创建实例对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //包装方法体
        Log.i( TAG, "reqJson is " +JsonStr);
        RequestBody requestBody = new  FormBody.Builder()
                .add( "reqJson", JsonStr )
                .build();

        final Request request = new Request.Builder()
                .post( requestBody )
                .url( url )
                .build();
        okHttpClient.newCall( request ).enqueue( callback );

//        okHttpClient.newCall( request ).enqueue( new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //对异常情况进行处理
//                Log.d(TAG,"获取数据失败了"+e.toString());
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //得到服务器返回的具体内容
//                if(response.isSuccessful()) {//回调的方法执行在子线程。
//                    Log.d( TAG, "获取数据成功了   postWith()" );
//                    responseData = response.body().string();
//                }
//            }
//        } );
    }

}
