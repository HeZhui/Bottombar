package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCheck;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class MyShop extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private ImageView back_shop;
    private RecyclerView recyclerView_shop;
    private View netFailed, nothing_find;

    private int state = 1;    //摊位
    private boolean login;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private String url = "http://47.105.185.251:8081/Proj31/shopandbuy";
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_shop );

        back_shop = (ImageView) findViewById( R.id.back_shop );
        back_shop.setOnClickListener( this );
        recyclerView_shop = (RecyclerView) findViewById( R.id.recycler_shop );
        netFailed = findViewById( R.id.layout_net_failed_shop );
        nothing_find = findViewById( R.id.nothing_find );

        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String uname = sp.getString( "uname", "" );
        String token = sp.getString( "token", "" );
        login = sp.getBoolean( "login", false );
        Log.i( TAG, "uname is  " + uname );
        Log.i( TAG, "token is  " + token );
        Log.i( TAG, "login is  " + login );
        Gson gson = new Gson();
        UserCheck userCheck = new UserCheck();
        userCheck.setUsername( uname );
        userCheck.setToken( token );
        userCheck.setState( state );
        String jsonStr = gson.toJson( userCheck, UserCheck.class );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( MyShop.this, "正在查询..." );
        progressDialog.show();
        //发送请求
        PostWith.sendPostWithOkhttp( url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        Log.i( TAG, "run: failed" );
                        recyclerView_shop.setVisibility( View.GONE );
                        nothing_find.setVisibility( View.GONE );
                        netFailed.setVisibility( View.VISIBLE );
                        Toast.makeText( MyShop.this, "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i( TAG, "run: success" );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " + responseData );
                //开始解析返回数据
                Log.i( TAG, "开始解析数据" );
                Gson gson = new Gson();
                //把属性给到对应的对象中
                Goods goods = gson.fromJson( responseData, Goods.class );
                Log.i( TAG, "解析数据完毕" );
                int flag = goods.getFlag();
                Log.i( TAG, "flag " + flag );
                goodsList = goods.getGoodsList();
                if (goodsList.size() == 0) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "onResponse: goodsList's size is 0" );
                            recyclerView_shop.setVisibility( View.GONE );
                            netFailed.setVisibility( View.GONE );
                            nothing_find.setVisibility( View.VISIBLE );
                            Toast.makeText( MyShop.this, "您的店铺目前没有任何商品哦！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    if (flag == 200) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                recyclerView_shop.setVisibility( View.VISIBLE );
                                netFailed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.GONE );
                                Toast.makeText( MyShop.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                                LinearLayoutManager layoutManager = new LinearLayoutManager( MyShop.this, LinearLayoutManager.VERTICAL, false );
                                recyclerView_shop.setLayoutManager( layoutManager );
                                GoodsAdapter adapter = new GoodsAdapter(MyShop.this, goodsList );
                                recyclerView_shop.setAdapter( adapter );
                            }
                        } );
                    } else if (flag == 30001) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: token is invalid" );
                                recyclerView_shop.setVisibility( View.VISIBLE );
                                netFailed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.GONE );
                                if (login == true) {
                                    Toast.makeText( MyShop.this, "登录信息已无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                                } else {
                                    Toast.makeText( MyShop.this, "您还没有登录，请先登录账户哦！", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } );
                    } else {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                recyclerView_shop.setVisibility( View.VISIBLE );
                                netFailed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.GONE );
                                Toast.makeText( MyShop.this, "查询失败！", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                }
            }
        } );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_shop:
                finish();
        }
    }
}
