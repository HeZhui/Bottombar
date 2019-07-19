package com.example.a15927.bottombardemo.sortactivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class Sort extends AppCompatActivity {
    private String TAG = "Test";

    private RecyclerView recycler_sort;
    private View netFailed;
    private SpringView springView_sort;
    private ImageView arrow_back_sort;
    private TextView text_sort;

    private String url = "http://47.105.185.251:8081/Proj31/sort";
    private int QueryType = 1;//代表按照商品类别查询
    private String goodsType;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private List<ItemGoods> moreGoodsList = new ArrayList<>(  );

    //分页状态
    public int page = 1;
    //当前分页  1------加载，  2-----------刷新
    protected int checkType = 1;
    //每页数目
    public int pageSize = 5;
    private String token;

    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sort );

        Intent intent = getIntent();
        String type = intent.getStringExtra( "type" );
        Log.i( TAG, "onCreate: type is " +type);

        text_sort = (TextView)findViewById( R.id.text_sort );
        recycler_sort = (RecyclerView) findViewById( R.id.recycler_sort );
        springView_sort = (SpringView) findViewById( R.id.springView_sort );
        netFailed = findViewById( R.id.layout_net_failed_sort );
        arrow_back_sort = (ImageView) findViewById( R.id.arrow_back_sort );
        arrow_back_sort.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );

        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String uname = sp.getString( "uname", "" );
        token = sp.getString( "token", "" );
        Log.i( "Test", "uname is  " + uname );
        Log.i( "Test", "token is  " + token );
        switch (type){
            case "electric":
                goodsType = "电器";
                text_sort.setText( "分类：电器" );
                break;
            case "makeup":
                goodsType = "日常用品";
                text_sort.setText( "分类：日常用品" );
                break;
            case "clothes":
                goodsType = "服饰";
                text_sort.setText( "分类：服饰" );
                break;
            case "sports":
                goodsType = "体育用品";
                text_sort.setText( "分类：体育用品" );
                break;
            case "book":
                goodsType = "书籍";
                text_sort.setText( "分类：书籍" );
                break;
            case "camera":
                goodsType = "数码";
                text_sort.setText( "分类：数码" );
                break;
            default:
                break;
        }
        initGoods( );
        loadAndRefresh();
    }

    private void loadAndRefresh() {
        springView_sort.setHeader( new DefaultHeader( this ) );
        springView_sort.setFooter( new DefaultFooter( this ) );
        springView_sort.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                checkType = 2;
                initGoods();
                springView_sort.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                page++;
                checkType = 1;
                Log.i( TAG, "onRefresh: page is " + page );
                initGoods();
                springView_sort.onFinishFreshAndLoad();
            }
        } );
    }

    //加载商品信息
    private void initGoods() {
        //初始化对象sort
        SortGoodsRo sort = new SortGoodsRo();
        //设置属性
        sort.setToken( token );
        sort.setQueryType( QueryType );
        sort.setGoodsType( goodsType );
        sort.setGoodsName( null );
        sort.setPageSize( pageSize );
        sort.setPage( page );
        sort.setCheckType( checkType );

        Gson gson = new Gson();
        String jsonStr = gson.toJson( sort, SortGoodsRo.class );

        Log.i( TAG, "组成的Json串是: " + jsonStr );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( Sort.this, "正在查询..." );
        progressDialog.show();
        //发送okhttp请求
        PostWith.sendPostWithOkhttp( url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        Log.i( TAG, "run: failed" );
                        recycler_sort.setVisibility( View.GONE );
                        netFailed.setVisibility( View.VISIBLE );
                        Toast.makeText( Sort.this, "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
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
                // Log.i( TAG, "goodsList" +goodsList);
                //flag判断
                if (flag == 200) {
                    if(goodsList.size() == 0){
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框
                                dismiss( progressDialog );
                                Toast.makeText( Sort.this, "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                    if(goodsList.size() <= pageSize && goodsList.size() != 0){
                        for (int i = 0; i < goodsList.size(); i++) {
                            boolean repeat = false;
                            for (int j = 0; j < moreGoodsList.size(); j++) {
                                if (moreGoodsList.get( j ).getGoodsID().equals( goodsList.get( i ).getGoodsID() )) {
                                    repeat = true;
                                    break;
                                }
                            }
                            if (repeat == false) {
                                moreGoodsList.add( goodsList.get( i ) );
                            }
                        }
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                Toast.makeText( Sort.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                                //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                                LinearLayoutManager layoutManager = new LinearLayoutManager( Sort.this );
                                recycler_sort.setLayoutManager( layoutManager );
                                GoodsAdapter adapter = new GoodsAdapter( Sort.this,moreGoodsList );
                                recycler_sort.setAdapter( adapter );
                            }
                        } );
                    }
                } else if (flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( Sort.this, "登录信息无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( Sort.this, "查询失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
