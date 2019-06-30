package com.example.a15927.bottombardemo.homeactivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
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

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class HomeSearch extends AppCompatActivity {
    private String TAG = "Test";
    private EditText sou;
    private ImageView search;
    private TextView quit_button;
    private RecyclerView recycler_search;
    private View net_failed, nothing_find;

    private String url = "http://47.105.185.251:8081/Proj31/sort";
    private int QueryType = 2;//代表按照商品名称查询
    private List<ItemGoods> goodsList = new ArrayList<>();
    private boolean login;
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home_search );
        sou = (EditText) findViewById( R.id.type_thing );
        quit_button = (TextView) findViewById( R.id.back_to );
        search = (ImageView) findViewById( R.id.search );
        recycler_search = (RecyclerView) findViewById( R.id.recycler_search );
        net_failed = findViewById( R.id.net_failed_search );
        nothing_find = findViewById( R.id.nothing_find_search );

        quit_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sou.getText().toString().trim().length() == 0) {
                    //销毁目前的活动
                    finish();
                } else {
                    //撤销输入框的内容
                    sou.setText( "" );
                }
            }
        } );

        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sou.getText().toString().trim().length() == 0) {
                    Toast.makeText( HomeSearch.this, "请先输入要查找的商品名称！", Toast.LENGTH_SHORT ).show();
                } else {
                    String goodsName = sou.getText().toString().trim();
                    SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
                    String uname = sp.getString( "uname", "" );
                    String token = sp.getString( "token", "" );
                    login = sp.getBoolean( "login", false );
                    Log.i( "Test", "uname is  " + uname );
                    Log.i( "Test", "token is  " + token );
                    //初始化对象books
                    SortGoodsRo home = new SortGoodsRo();
                    //设置属性
                    home.setToken( token );
                    home.setQueryType( QueryType );
                    home.setGoodsName( goodsName );
                    home.setGoodsType( null );
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson( home, SortGoodsRo.class );
                    Log.i( TAG, "组成的Json串是: " + jsonStr );
                    //进度框显示方法一
                    progressDialog = DialogUIUtils.showLoadingDialog( HomeSearch.this, "正在查询..." );
                    progressDialog.show();
                    initGoods( jsonStr );
                }
            }
        } );
    }

    private void initGoods(String jsonStr) {
        PostWith.sendPostWithOkhttp( url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        Log.i( TAG, "run: failed" );
                        recycler_search.setVisibility( View.GONE );
                        net_failed.setVisibility( View.VISIBLE );
                        nothing_find.setVisibility( View.GONE );
                        Toast.makeText( HomeSearch.this, "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
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
                    Log.i( TAG, "onResponse: goodsList size is 0" );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            recycler_search.setVisibility( View.GONE );
                            net_failed.setVisibility( View.GONE );
                            nothing_find.setVisibility( View.VISIBLE );
                            Toast.makeText( HomeSearch.this, "抱歉，没有搜索到相关信息哦！", Toast.LENGTH_SHORT ).show();
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
                                recycler_search.setVisibility( View.VISIBLE );
                                net_failed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.GONE );
                                Toast.makeText( HomeSearch.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                                //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                                LinearLayoutManager layoutManager = new LinearLayoutManager( HomeSearch.this, LinearLayoutManager.VERTICAL, false );
                                recycler_search.setLayoutManager( layoutManager );
                                GoodsAdapter adapter = new GoodsAdapter( HomeSearch.this,goodsList );
                                recycler_search.setAdapter( adapter );
                            }
                        } );
                    } else if (flag == 30001) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                recycler_search.setVisibility( View.GONE );
                                net_failed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.VISIBLE );
                                if (login == true) {
                                    Toast.makeText( HomeSearch.this, "登录信息已无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                                } else {
                                    Toast.makeText( HomeSearch.this, "您还没有登录，请先登录哦！", Toast.LENGTH_SHORT ).show();
                                }

                            }
                        } );
                    } else {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                recycler_search.setVisibility( View.GONE );
                                net_failed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.VISIBLE );
                                Toast.makeText( HomeSearch.this, "查询失败！", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                }
            }
        } );
    }
}
