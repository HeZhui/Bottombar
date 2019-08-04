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
import com.example.a15927.bottombardemo.adapter.ShopAdapter;
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

public class MyForBuy extends AppCompatActivity {
    private String TAG = "Test";
    private RecyclerView recycler_buy;
    private View net_failed_buy;
    private SpringView springView_myBuy;
    private View nothing_find_buy;
    private ImageView back_buy;

    private int state = 2; //求购
    //分页状态
    public int loadPage = 1;
    public int refreshPage = 1;
    //当前分页  1------加载，  2-----------刷新
    protected int checkType = 1;
    //每页数目
    public int pageSize = 5;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private List<ItemGoods> moreGoodsList = new ArrayList<>(  );
    ShopAdapter adapter = null;

    private boolean login;

    private String url = "http://47.105.185.251:8081/Proj31/shopandbuy";
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_for_buy );
        //绑定控件
        initView();
        //获取数据
        getData();
        springView_myBuy.setHeader( new DefaultHeader( MyForBuy.this ) );
        springView_myBuy.setFooter( new DefaultFooter( MyForBuy.this ) );
        springView_myBuy.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                refreshPage = 1;
                checkType = 2;
                getData();
                springView_myBuy.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                if(moreGoodsList != null){
                    loadPage++;
                }else{
                    loadPage = 1;
                }
                checkType = 1;
                getData();
                springView_myBuy.onFinishFreshAndLoad();
            }
        } );
    }

    private void getData() {
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String token = sp.getString( "token", "" );
        login = sp.getBoolean( "login", false );
        Log.i( TAG, "token is  " + token );
        Log.i( TAG, "login is  " + login );
        Gson gson = new Gson();
        UserBuy myBuy = new UserBuy();
        myBuy.setPageSize( pageSize );
        if(checkType == 1){
            myBuy.setPage( loadPage );
        }else{
            myBuy.setPage( refreshPage );
        }
        myBuy.setCheckType( checkType );
        myBuy.setToken(token);
        myBuy.setCondition( state );
        String jsonStr = gson.toJson( myBuy, UserBuy.class );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( MyForBuy.this, "正在查询..." );
        progressDialog.show();
        //发送数据
        PostWith.sendPostWithOkhttp( url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        Log.i( TAG, "onFailure: 获取数据失败！" );
                        recycler_buy.setVisibility( View.GONE );
                        net_failed_buy.setVisibility( View.VISIBLE );
                        nothing_find_buy.setVisibility( View.GONE );
                        String errorData = TestAndVerify.judgeError( MyForBuy.this );
                        Toast.makeText( MyForBuy.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
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
                        if((loadPage == 1 || refreshPage == 1) && moreGoodsList.size() == 0){
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recycler_buy.setVisibility( View.GONE );
                                    net_failed_buy.setVisibility( View.GONE );
                                    nothing_find_buy.setVisibility( View.VISIBLE );
                                    Toast.makeText( MyForBuy.this, "您暂时没有任何想买的东西哦！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }else{
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    recycler_buy.setVisibility( View.VISIBLE );
                                    net_failed_buy.setVisibility( View.GONE );
                                    nothing_find_buy.setVisibility( View.GONE );
                                    dismiss( progressDialog );
                                    Toast.makeText( MyForBuy.this, "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
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
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                recycler_buy.setVisibility( View.VISIBLE );
                                net_failed_buy.setVisibility( View.GONE );
                                nothing_find_buy.setVisibility( View.GONE );
                                if(checkType == 1){
                                    Toast.makeText( MyForBuy.this, "加载成功！", Toast.LENGTH_SHORT ).show();
                                }else{
                                    Toast.makeText( MyForBuy.this, "刷新成功！", Toast.LENGTH_SHORT ).show();
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager( MyForBuy.this,LinearLayoutManager.VERTICAL,false );
                                recycler_buy.setLayoutManager( layoutManager );
                                adapter = new ShopAdapter(MyForBuy.this, moreGoodsList );
                                recycler_buy.setAdapter( adapter );
                                if(checkType == 1){
                                    recycler_buy.scrollToPosition( adapter.getItemCount()-1 );
                                }
                            }
                        } );
                    }
                }else if (flag == 30001){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "run: token is invalid" );
                            recycler_buy.setVisibility( View.VISIBLE );
                            net_failed_buy.setVisibility( View.GONE );
                            nothing_find_buy.setVisibility( View.GONE );
                            if(login == true){
                                Toast.makeText( MyForBuy.this, "登录信息已无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                            }else{
                                Toast.makeText( MyForBuy.this, "您还没有登录账号，请先登录哦！", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
                }else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            recycler_buy.setVisibility( View.VISIBLE );
                            net_failed_buy.setVisibility( View.GONE );
                            nothing_find_buy.setVisibility( View.GONE );
                            Toast.makeText( MyForBuy.this, "查询失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }

    //绑定控件
    private void initView() {
        recycler_buy = (RecyclerView) findViewById( R.id.recycler_buy );
        net_failed_buy = findViewById( R.id.layout_net_failed_buy );
        nothing_find_buy = findViewById( R.id.buy_nothing );
        springView_myBuy = (SpringView)findViewById( R.id.springView_myBuy );
        back_buy = (ImageView) findViewById( R.id.back_buy );
        back_buy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //结束此活动
                finish();
            }
        } );

    }
}
