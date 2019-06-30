package com.example.a15927.bottombardemo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.sortactivity.SortGoodsRo;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2019/6/13.
 */

public class GetListsUtils {
    public static String TAG = "Test";
    private static String url = "http://47.105.185.251:8081/Proj31/sort";
    private static int status = 0;
    private static List<ItemGoods> goodsList = new ArrayList<>(  );
    public static boolean isSuccess;
    private static int flag;
    public static List<ItemGoods> getLists(final Context context, final String goodsType, int QueryType){
        SharedPreferences sp = context.getSharedPreferences( "data",MODE_PRIVATE );
        String uname = sp.getString( "uname","" );
        String token = sp.getString( "token","" );
        Log.i( "Test", "uname is  " +uname);
        Log.i( "Test", "token is  " +token );
        //重置statue
        status = 0;
        //初始化对象books
        SortGoodsRo books = new SortGoodsRo();
        //设置属性
        books.setToken( token );
        books.setQueryType( QueryType );
        books.setGoodsType( goodsType );
        books.setGoodsName( null );
        Gson gson = new Gson();
        //组成json串
        String jsonStr = gson.toJson( books,SortGoodsRo.class );
        Log.i( TAG , "组成的Json串是: "+jsonStr );
        PostWith.sendPostWithOkhttp( url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i( TAG, "run: failed" );
                status = 1;
                Log.i( TAG, "onFailure: statue is "+status );
                isSuccess = false;
                Log.i( TAG, "onFailure: isSuccess is "+ isSuccess );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i( TAG, "run: success" );
                status = 2;
                isSuccess = true;
                Log.i( TAG, "onResponse: isSuccess is "+isSuccess );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " +responseData);
                //开始解析返回数据
                Log.i( TAG, "开始解析数据" );
                Gson gson = new Gson();
                //把属性给到对应的对象中
                Goods goods = gson.fromJson( responseData,Goods.class );
                Log.i( TAG, "解析数据完毕" );
                flag = goods.getFlag();
                Log.i( TAG, "flag is " +flag);
                goodsList = goods.getGoodsList();
                if(goodsList != null){
                    Log.i( TAG, "goodsList" +goodsList);
                }
            }
        } );
        return goodsList;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        GetListsUtils.status = status;
    }

    public static List<ItemGoods> getGoodsList() {
        return goodsList;
    }

    public static void setGoodsList(List<ItemGoods> goodsList) {
        GetListsUtils.goodsList = goodsList;
    }

    public static boolean isSuccess() {
        return isSuccess;
    }

    public static void setIsSuccess(boolean isSuccess) {
        GetListsUtils.isSuccess = isSuccess;
    }

    public static int getFlag() {
        return flag;
    }

    public static void setFlag(int flag) {
        GetListsUtils.flag = flag;
    }
}
