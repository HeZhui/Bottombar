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
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserBuy;
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

public class CollectionInf extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private ImageView col_back;
    private SpringView springView_col;
    private RecyclerView recycler_collect;
    private View net_failed_col,col_nothing;

    private int state = 3; //收藏
    //分页状态
    public int loadPage = 1;
    public int refreshPage = 1;
    //当前分页  1------加载，  2-----------刷新
    protected int checkType = 1;
    //每页数目
    public int pageSize = 5;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private List<ItemGoods> moreGoodsList = new ArrayList<>(  );
    GoodsAdapter adapter = null;

    private boolean login;

    private String url = "http://47.105.185.251:8081/Proj31/shopandbuy";
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.collection );

        initView();

        refresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i( TAG, "onRestart: reStart" );
        if(moreGoodsList != null){
            moreGoodsList.clear();
        }
        loadPage = 1;
        refreshPage = 1;
        refresh();
    }

    /**
     * 刷新列表
     */
    public void refresh(){
        getCol();

        springView_col.setHeader( new DefaultHeader( CollectionInf.this ) );
        springView_col.setFooter( new DefaultFooter( CollectionInf.this ) );
        springView_col.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshPage = 1;
                checkType = 2;
                getCol();
                springView_col.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                if(moreGoodsList.size() != 0){
                    loadPage++;
                }else{
                    loadPage = 1;
                }
                checkType = 1;
                getCol();
                springView_col.onFinishFreshAndLoad();
            }
        } );
    }


    /*
     * 获取收藏的物品信息
     */
    private void getCol() {
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String token = sp.getString( "token", "" );
        String uid = sp.getString( "uid","" );
        login = sp.getBoolean( "login", false );
        Log.i( TAG, "token is  " + token );
        Log.i( TAG, "login is  " + login );
        Gson gson = new Gson();
        UserBuy col = new UserBuy();
        col.setPageSize( pageSize );
        if(checkType == 1){
            col.setPage( loadPage );
        }else{
            col.setPage( refreshPage );
        }
        col.setCheckType( checkType );
        col.setToken(token);
        col.setUserid( uid );
        col.setCondition( state );
        String reqJson = gson.toJson( col,UserBuy.class );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( CollectionInf.this, "正在查询..." );
        progressDialog.show();
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //取消进度框一
                dismiss( progressDialog );
                Log.i( TAG, "onFailure: 获取数据失败！" );
                recycler_collect.setVisibility( View.GONE );
                net_failed_col.setVisibility( View.VISIBLE );
                col_nothing.setVisibility( View.GONE );
                String errorData = TestAndVerify.judgeError( CollectionInf.this );
                Toast.makeText( CollectionInf.this, errorData, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i( TAG, "onResponse: run success" );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: responseData is "+responseData );
                Gson gson = new Gson();
                Goods goods = gson.fromJson( responseData,Goods.class );
                int flag = goods.getFlag();
                goodsList = goods.getGoodsList();
                if(flag == 200){
                    if(goodsList.size() == 0){
                        if( (loadPage == 1 || refreshPage == 1) && moreGoodsList.size() == 0){
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recycler_collect.setVisibility( View.GONE );
                                    net_failed_col.setVisibility( View.GONE );
                                    col_nothing.setVisibility( View.VISIBLE );
                                    Toast.makeText( CollectionInf.this, "您暂时没有任何收藏的商品哦！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }else{
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    recycler_collect.setVisibility( View.VISIBLE );
                                    net_failed_col.setVisibility( View.GONE );
                                    col_nothing.setVisibility( View.GONE );
                                    dismiss( progressDialog );
                                    Toast.makeText( CollectionInf.this, "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }
                    }else{
                        //刷新消息应该显示在最上面
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
                        //下拉加载
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
                        //切换到主线程
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                recycler_collect.setVisibility( View.VISIBLE );
                                net_failed_col.setVisibility( View.GONE );
                                col_nothing.setVisibility( View.GONE );
                                if(checkType == 1){
                                    Toast.makeText( CollectionInf.this, "加载成功！", Toast.LENGTH_SHORT ).show();
                                }else{
                                    Toast.makeText( CollectionInf.this, "刷新成功！", Toast.LENGTH_SHORT ).show();
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager( CollectionInf.this,LinearLayoutManager.VERTICAL,false );
                                recycler_collect.setLayoutManager( layoutManager );
                                adapter = new GoodsAdapter(CollectionInf.this, moreGoodsList );
                                recycler_collect.setAdapter( adapter );
                                if(checkType == 1){
                                    recycler_collect.scrollToPosition( adapter.getItemCount()-1 );
                                }
                            }
                        } );
                    }
                }else if(flag == 30001){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "run: token is invalid" );
                            recycler_collect.setVisibility( View.VISIBLE );
                            net_failed_col.setVisibility( View.GONE );
                            col_nothing.setVisibility( View.GONE );
                            if(login == true){
                                Toast.makeText( CollectionInf.this, "登录信息已无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                            }else{
                                Toast.makeText( CollectionInf.this, "您还没有登录账号，请先登录哦！", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
                }else{
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            recycler_collect.setVisibility( View.VISIBLE );
                            net_failed_col.setVisibility( View.GONE );
                            col_nothing.setVisibility( View.GONE );
                            Toast.makeText( CollectionInf.this, "查询失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }

    /*
     *初始化绑定控件
     */
    private void initView() {
        col_back = (ImageView)findViewById( R.id.back_collect );
        col_back.setOnClickListener( this );
        springView_col = (SpringView)findViewById( R.id.springView_col );
        recycler_collect = (RecyclerView)findViewById( R.id.recycler_collect );
        net_failed_col = findViewById( R.id.net_failed_col );
        col_nothing = findViewById( R.id.col_nothing );
    }

    /*
    *控件的监听事件
    */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_collect:
                finish();
                break;
        }
    }
}
