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

public class MyShop extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private ImageView back_shop;
    private RecyclerView recyclerView_shop;
    private View netFailed, nothing_find;
    private SpringView springView_shop;

    private boolean login;
    private String url = "http://192.168.0.6:8081/Proj31/shopandbuy";

    //进度条一
    Dialog progressDialog;

    private int state = 1;    //摊位
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_shop );

        back_shop = (ImageView) findViewById( R.id.back_shop );
        back_shop.setOnClickListener( this );
        recyclerView_shop = (RecyclerView) findViewById( R.id.recycler_shop );
        netFailed = findViewById( R.id.layout_net_failed_shop );
        nothing_find = findViewById( R.id.nothing_find );
        springView_shop = (SpringView)findViewById( R.id.springView_shop );

        postRequest();

        springView_shop.setHeader( new DefaultHeader( MyShop.this ) );
        springView_shop.setFooter( new DefaultFooter( MyShop.this ) );
        springView_shop.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshPage = 1;
                checkType = 2;
                postRequest();
                springView_shop.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                if(moreGoodsList != null){
                    loadPage++;
                }else{
                    loadPage = 1;
                }
                checkType = 1;
                postRequest();
                springView_shop.onFinishFreshAndLoad();
            }
        } );
    }

    private void postRequest( ) {
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String token = sp.getString( "token", "" );
        login = sp.getBoolean( "login", false );
        Log.i( TAG, "token is  " + token );
        Log.i( TAG, "login is  " + login );
        Gson gson = new Gson();
        UserBuy shop = new UserBuy();
        shop.setPageSize( pageSize );
        if(checkType == 1){
            shop.setPage( loadPage );
        }else{
            shop.setPage( refreshPage );
        }
        shop.setCheckType( checkType );
        shop.setToken(token);
        shop.setCondition( state );

        String jsonStr = gson.toJson( shop, UserBuy.class );
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
                        String errorData = TestAndVerify.judgeError( MyShop.this );
                        Toast.makeText( MyShop.this, errorData, Toast.LENGTH_SHORT ).show();
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
                if (flag == 200) {
                    if (goodsList.size() == 0) {
                        if((refreshPage == 1 || loadPage == 1) && moreGoodsList.size() == 0){
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recyclerView_shop.setVisibility( View.GONE );
                                    netFailed.setVisibility( View.GONE );
                                    nothing_find.setVisibility( View.VISIBLE );
                                    Toast.makeText( MyShop.this, "您的店铺目前没有任何商品哦！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }else{
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recyclerView_shop.setVisibility( View.VISIBLE );
                                    netFailed.setVisibility( View.GONE );
                                    nothing_find.setVisibility( View.GONE );
                                    Toast.makeText( MyShop.this, "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }
                    } else {
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
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                recyclerView_shop.setVisibility( View.VISIBLE );
                                netFailed.setVisibility( View.GONE );
                                nothing_find.setVisibility( View.GONE );
                                if (checkType == 1){
                                    Toast.makeText( MyShop.this, "加载成功！", Toast.LENGTH_SHORT ).show();
                                }else{
                                    Toast.makeText( MyShop.this, "刷新成功！", Toast.LENGTH_SHORT ).show();
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager( MyShop.this, LinearLayoutManager.VERTICAL, false );
                                recyclerView_shop.setLayoutManager( layoutManager );
                                adapter = new GoodsAdapter(MyShop.this, moreGoodsList );
                                recyclerView_shop.setAdapter( adapter );
                                if(checkType == 1){
                                    recyclerView_shop.scrollToPosition( adapter.getItemCount()-1 );
                                }
                            }
                        } );
                    }
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
