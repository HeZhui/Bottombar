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
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.sortactivity.SortGoodsRo;
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

public class HomeSearch extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private EditText sou;
    private ImageView search;
    private TextView quit_button;
    private RecyclerView recycler_search;
    private View net_failed, nothing_find;
    private SpringView springView_homeSearch;

    private String url = "http://47.105.185.251:8081/Proj31/sort";
    private int QueryType = 2;//代表按照商品名称查询
    private String goodsType;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private List<ItemGoods> moreGoodsList = new ArrayList<>(  );

    //分页状态
    public int refreshPage = 1;   //刷新
    public int loadPage = 1;      //加载
    //当前分页  1------加载，  2-----------刷新
    protected int checkType = 1;
    //每页数目
    public int pageSize = 5;

    //登录标志
    private boolean login;
    private String token;
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home_search );
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        token = sp.getString( "token", "" );
        login = sp.getBoolean( "login", false );
        Log.i( TAG, "login is "+login );
        Log.i( TAG, "token is  " + token );

        initView();

        //上拉刷新下拉加载
        springView_homeSearch.setFooter( new DefaultFooter( HomeSearch.this ) );
        springView_homeSearch.setHeader( new DefaultHeader( HomeSearch.this ) );
        springView_homeSearch.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshPage = 1;
                checkType = 2;
                checkAndPost();
                springView_homeSearch.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                if(moreGoodsList != null){
                    loadPage++;
                }else{
                    loadPage = 1;
                }
                checkType = 1;
                checkAndPost();
                springView_homeSearch.onFinishFreshAndLoad();
            }
        } );
    }

    /*
     *检验输入框和形成Json串
     */
    private void checkAndPost() {
        if (sou.getText().toString().trim().length() == 0) {
            Toast.makeText( HomeSearch.this, "请先输入要查找的商品名称！", Toast.LENGTH_SHORT ).show();
            return;
        } else {
            String goodsName = sou.getText().toString().trim();
            //初始化对象books
            SortGoodsRo home = new SortGoodsRo();
            //设置属性
            home.setToken( token );
            home.setQueryType( QueryType );
            home.setGoodsName( goodsName );
            home.setPageSize( pageSize );
            if(checkType == 1){
                home.setPage( loadPage );
            }else{
                home.setPage( refreshPage );
            }
            home.setCheckType( checkType );
            Gson gson = new Gson();
            String jsonStr = gson.toJson( home, SortGoodsRo.class );
            Log.i( TAG, "组成的Json串是: " + jsonStr );
            initGoods( jsonStr );
        }
    }

    /*
     *发送请求
     */
    private void initGoods(String jsonStr) {
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( HomeSearch.this, "正在查询..." );
        progressDialog.show();
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
                        String errorData = TestAndVerify.judgeError( HomeSearch.this );
                        Toast.makeText( HomeSearch.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i( TAG, "run: success" );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " + responseData );
                Gson gson = new Gson();
                //把属性给到对应的对象中
                Goods goods = gson.fromJson( responseData, Goods.class );
                int flag = goods.getFlag();
                Log.i( TAG, "flag " + flag );
                goodsList = goods.getGoodsList();
                Log.i( TAG, "onResponse: "+goodsList.size() );
                if (flag == 200) {
                    //返回列表为空
                    if (goodsList.size() == 0) {
                        if((refreshPage == 1 || loadPage == 1) && moreGoodsList.size() == 0){
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recycler_search.setVisibility( View.GONE );
                                    net_failed.setVisibility( View.GONE );
                                    nothing_find.setVisibility( View.VISIBLE );
                                    Toast.makeText( HomeSearch.this, "抱歉，没有搜索到相关信息哦！", Toast.LENGTH_SHORT ).show();
                                    return;
                                }
                            } );
                        }else{
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recycler_search.setVisibility( View.VISIBLE );
                                    net_failed.setVisibility( View.GONE );
                                    nothing_find.setVisibility( View.GONE );
                                    Toast.makeText( HomeSearch.this, "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
                                    return;
                                }
                            } );
                        }
                    } else {
                        //刷新
                        if(checkType == 2){
                            for(int i = 0; i < moreGoodsList.size(); i++ ){
                                boolean repeat  = false;
                                for(int j = 0; j< goodsList.size(); j++){
                                    if(goodsList.get( j ).getGoodsID().equals( moreGoodsList.get( i).getGoodsID() )){
                                        repeat = true;
                                        break;
                                    }
                                }
                                if(repeat == false){
                                    goodsList.add( moreGoodsList.get( i ) );
                                }
                            }
                            moreGoodsList = goodsList;
                        }
                        //加载
                        if(checkType == 1){
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
                        }
                        //切换至主线程
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                recycler_search.setVisibility( View.VISIBLE );
                                net_failed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.GONE );
                                if(checkType == 2){
                                    Toast.makeText( HomeSearch.this, "刷新成功！", Toast.LENGTH_SHORT ).show();
                                }else{
                                    Toast.makeText( HomeSearch.this, "加载成功！", Toast.LENGTH_SHORT ).show();
                                }
                                //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                                LinearLayoutManager layoutManager = new LinearLayoutManager( HomeSearch.this, LinearLayoutManager.VERTICAL, false );
                                recycler_search.setLayoutManager( layoutManager );
                                GoodsAdapter adapter = new GoodsAdapter( HomeSearch.this,moreGoodsList );
                                recycler_search.setAdapter( adapter );
                            }
                        } );
                    }
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
        } );
    }

    /*
     *点击事件的监听
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_to:
                if (sou.getText().toString().trim().length() == 0) {
                    //销毁目前的活动
                    finish();
                } else {
                    //撤销输入框的内容
                    sou.setText( "" );
                }
                break;
            case R.id.search:
                checkAndPost();
                break;
        }
    }

    /*
     *初始化绑定控件
     */
    private void initView() {
        sou = (EditText) findViewById( R.id.type_thing );
        quit_button = (TextView) findViewById( R.id.back_to );
        quit_button.setOnClickListener(this);
        search = (ImageView) findViewById( R.id.search );
        search.setOnClickListener( this );
        recycler_search = (RecyclerView) findViewById( R.id.recycler_search );
        net_failed = findViewById( R.id.net_failed_search );
        nothing_find = findViewById( R.id.nothing_find_search );
        springView_homeSearch = (SpringView)findViewById( R.id.springView_homeSearch );
    }

}
