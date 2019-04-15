package com.example.a15927.bottombardemo.functiontools;

import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/3/19.
 */

public class GetWith {

    public void getDataWithOkhttp(String username, String password , final TextView textView){
        final String url = "";
        new Thread( new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url( url )
                            .build();
                    Response response = okHttpClient.newCall( request ).execute();
                    final String responseData = response.body().string();
//                    runOnUiThread( new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText( responseData );
//                        }
//                    } );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } ).start();
    }

}
