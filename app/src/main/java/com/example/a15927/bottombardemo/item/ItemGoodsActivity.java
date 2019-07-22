package com.example.a15927.bottombardemo.item;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ItemGoodsActivity extends AppCompatActivity {
    private String TAG = "Test";
    private ImageView img,back_s,col_image;
    private TextView back,g_goodsName,g_id,g_quality,g_unit,g_typeName,g_price,g_phone,g_qq,g_weixin,g_userId,g_userName,g_description;
    private String url = "http://47.105.185.251:8081/Proj31/collection";
    private int opType = 2;//0----未收藏     1-----收藏    2----查询收藏状态
    private int state = 0; //0----未收藏     1-----收藏
    public int lastState; //记录上次的状态
    ItemGoods itemGoods = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.item_goods );
        init();
        Intent intent = getIntent();
        itemGoods = (ItemGoods) intent.getSerializableExtra( "goodsList" );
        adapterGoodsInf(itemGoods);
        checkCondition(reqJsonStr( ));
    }

    /*
     *组成Json串
     */
    public String reqJsonStr(){
        //从sp中取出token
        SharedPreferences User = getSharedPreferences( "data", Context.MODE_PRIVATE );
        String token = User.getString( "token","" );
        ItemGoods col = new ItemGoods();
        col.setOpType( opType );
        col.setToken( token );
        col.setGoodsID( itemGoods.getGoodsID() );
        col.setUserid( itemGoods.getUserid() );
        Gson gson = new Gson();
        String reqJson = gson.toJson( col,ItemGoods.class );
        return reqJson;
    }

    /*
     *查询当前收藏状态
     */
    private void checkCondition(String reqJson) {
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i( TAG, "onFailure: 连接失败!" );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        col_image.setImageResource( R.drawable.col_normal );
                        String errorData = TestAndVerify.judgeError( ItemGoodsActivity.this );
                        Toast.makeText( ItemGoodsActivity.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i( TAG, "onResponse: 获取数据成功！" );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " +responseData);
                Gson gson = new Gson();
                UserCO userco = gson.fromJson( responseData,UserCO.class );
                int flag = userco.getFlag();
                if(flag == 50004){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            col_image.setImageResource( R.drawable.col_ok );
                            state = 1;
                            lastState = state;
                        }
                    } );
                }else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            col_image.setImageResource( R.drawable.col_normal );
                            state = 0;
                            lastState = state;
                        }
                    } );
                }
            }
        } );
    }

    /*
     *显示当前商品信息
     */
    private void adapterGoodsInf(ItemGoods itemGoods) {
        Glide.with( ItemGoodsActivity.this ).load( itemGoods.getGoodsImg() ).centerCrop().error( R.drawable.kimg ).into( img );
        g_id.setText( itemGoods.getGoodsID() );
        g_goodsName.setText( itemGoods.getGoodsName() );
        g_quality.setText( String.valueOf( itemGoods.getQuality() ) );
        g_price.setText( String.valueOf( itemGoods.getPrice() ) );
        g_unit.setText( itemGoods.getUnit() );
        g_typeName.setText( itemGoods.getGoodsTypeName() );
        g_userName.setText( itemGoods.getUname() );
        g_userId.setText( itemGoods.getUserid() );
        g_phone.setText( itemGoods.getUphone() );
        g_qq.setText( itemGoods.getQq() );
        g_weixin.setText( itemGoods.getWeixin() );
        if(itemGoods.getDescription() == null){
            g_description.setText( "还没有关于当前商品的说明哦！" );
        }else {
            g_description.setText( itemGoods.getDescription() );
        }
    }

    /*
     *初始化绑定控件
     */
    private void init() {
        img = (ImageView)findViewById( R.id.g_img );
        g_id = (TextView)findViewById( R.id.g_goods_id );
        g_goodsName = (TextView)findViewById( R.id.g_goods_name );
        g_quality = (TextView)findViewById( R.id.g_quality_num );
        g_unit =(TextView)findViewById( R.id.uint );
        g_price = (TextView)findViewById( R.id.price_num );
        g_typeName = (TextView)findViewById( R.id.g_typeName );
        g_userName = (TextView)findViewById( R.id.g_username );
        g_userId = (TextView)findViewById( R.id.g_id_user );
        g_phone = (TextView)findViewById( R.id.g_phone_user );
        g_qq = (TextView)findViewById( R.id.g_qq_user );
        g_weixin = (TextView)findViewById( R.id.g_weixin_user );
        g_description = (TextView)findViewById( R.id.g_description_goods );
        back_s = (ImageView)findViewById( R.id.back_s );
        back_s.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        back = (TextView)findViewById( R.id.back );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        col_image = (ImageView)findViewById( R.id.col_image );
        col_image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectGoods();
            }
        } );
    }

    /*
     *执行收藏操作（取消或者添加）
     */
    private void collectGoods() {
        if(lastState == 1){
            opType = 0;
        }else{
            opType = 1;
        }
        String reqJson = reqJsonStr( );
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i( TAG, "onFailure: 连接失败!" );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        String errorData = TestAndVerify.judgeError( ItemGoodsActivity.this );
                        Toast.makeText( ItemGoodsActivity.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i( TAG, "onResponse: 获取数据成功！" );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " +responseData);
                Gson gson = new Gson();
                UserCO userco = gson.fromJson( responseData,UserCO.class );
                int flag = userco.getFlag();
                if(flag == 200){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            if(opType == 1){
                                col_image.setImageResource( R.drawable.col_ok );
                                state = 1;
                                Toast.makeText( ItemGoodsActivity.this, "收藏成功！", Toast.LENGTH_SHORT ).show();
                            }else{
                                col_image.setImageResource( R.drawable.col_normal );
                                state = 0;
                                Toast.makeText( ItemGoodsActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT ).show();
                            }
                            lastState = state;
                        }
                    } );
                }else if (flag == 30001){
                    Log.i( TAG, "run: token无效" );
                    Toast.makeText( ItemGoodsActivity.this, "当前登录信息无效，请重新登录", Toast.LENGTH_SHORT ).show();
                }else{
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( ItemGoodsActivity.this, "操作失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
