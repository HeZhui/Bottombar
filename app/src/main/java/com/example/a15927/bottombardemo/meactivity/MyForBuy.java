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
import com.example.a15927.bottombardemo.adapter.ShopAdapter;
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

public class MyForBuy extends AppCompatActivity {
    private String TAG = "Test";
    private RecyclerView recycler_buy;
    private View net_failed_buy;
    private View nothing_find_buy;
    private ImageView back_buy;

    private int state = 2; //求购
    private boolean login;
    private List<ItemGoods> goodsList = new ArrayList<>();
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

    }

    private void getData() {
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
                        Toast.makeText( MyForBuy.this, "当前网络状况不给力哦！", Toast.LENGTH_SHORT ).show();
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
                if(goodsList.size() == 0){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "run: goodsList's size is 0" );
                            recycler_buy.setVisibility( View.GONE );
                            net_failed_buy.setVisibility( View.GONE );
                            nothing_find_buy.setVisibility( View.VISIBLE );
                            Toast.makeText( MyForBuy.this, "您暂时没有任何想买的东西哦！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }else{
                    if(flag == 200){
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功！" );
                                recycler_buy.setVisibility( View.VISIBLE );
                                net_failed_buy.setVisibility( View.GONE );
                                nothing_find_buy.setVisibility( View.GONE );
                                Toast.makeText( MyForBuy.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                                LinearLayoutManager layoutManager = new LinearLayoutManager( MyForBuy.this,LinearLayoutManager.VERTICAL,false );
                                recycler_buy.setLayoutManager( layoutManager );
                                ShopAdapter adapter = new ShopAdapter(MyForBuy.this, goodsList );
                                recycler_buy.setAdapter( adapter );
                            }
                        } );
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
            }
        } );
    }

    //绑定控件
    private void initView() {
        recycler_buy = (RecyclerView) findViewById( R.id.recycler_buy );
        net_failed_buy = findViewById( R.id.layout_net_failed_buy );
        nothing_find_buy = findViewById( R.id.buy_nothing );
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
