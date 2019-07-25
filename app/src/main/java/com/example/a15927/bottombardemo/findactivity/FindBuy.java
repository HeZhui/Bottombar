package com.example.a15927.bottombardemo.findactivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindBuy extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private EditText name_buy, description_buy;
    private TextView commit_buy, buying;
    private ImageView back_buy;

    //查找码
    private int opType = 90004;
    private String url = "http://47.105.185.251:8081/Proj31/shop";
    //下拉框
    private Spinner spinner_buy;
    private static int spinner_position;

    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.find_buy );

        //初始化并设置监听
        buying = (TextView) findViewById( R.id.buying );
        buying.setOnClickListener( this );

        name_buy = (EditText) findViewById( R.id.name_buy );
        description_buy = (EditText) findViewById( R.id.description_buy );

        commit_buy = (TextView) findViewById( R.id.commit_buy );
        commit_buy.setOnClickListener( this );

        back_buy = (ImageView) findViewById( R.id.back_buy );
        back_buy.setOnClickListener( this );

        //下拉框
        spinner_buy = (Spinner) findViewById( R.id.spinner_buy );
        //适配器
        spinner_buy.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_position = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_buy:
                //返回上一级
                finish();
                break;
            case R.id.buying:
                finish();
                break;
            case R.id.commit_buy:
                //获取求购的商品信息及求购者的联系方式
                String goodsName = name_buy.getText().toString().trim();
                String description = description_buy.getText().toString().trim();
                if (goodsName.length() == 0 ) {
                    Toast.makeText( this, "请完善需求信息！", Toast.LENGTH_SHORT ).show();
                } else {
                    if(!TestAndVerify.checkIllegal( goodsName )){
                        Toast.makeText( this, "商品名称禁止输入非法字符！", Toast.LENGTH_SHORT ).show();
                        return;
                    }
                    if(description != null){
                        if(!TestAndVerify.checkIllegal( description )){
                            Toast.makeText( this, "描述禁止输入非法字符！", Toast.LENGTH_SHORT ).show();
                            return;
                        }
                    }
                    //进度框显示方法一
                    progressDialog = DialogUIUtils.showLoadingDialog( FindBuy.this,"正在发布商品" );
                    progressDialog.show();
                    require( goodsName, description );
                }
                break;
        }
    }


    public void require(String goodsName, String description) {
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String uid = sp.getString( "uid", "" );
        String token = sp.getString( "token", "" );
        Log.i( TAG, "require:uid is  "+uid );
        Log.i( TAG, "token is  " + token );
        //设置属性
        ItemGoods goodsBuy = new ItemGoods();
        goodsBuy.setToken( token );
        goodsBuy.setOpType( opType );
        goodsBuy.setDescription( description );
        goodsBuy.setGoodsName( goodsName );
        goodsBuy.setUserid( uid );
        goodsBuy.setGoodsType( String.valueOf( spinner_position ) );
        Gson gson = new Gson();
        String jsonStr = gson.toJson( goodsBuy, ItemGoods.class );
        Log.i( TAG, "require: jsonStr is " + jsonStr );
        //发送Post
        PostWith.sendPostWithOkhttp( url, jsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( "Test", "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        progressDialog.dismiss();
                        String errorData = TestAndVerify.judgeError( FindBuy.this );
                        Toast.makeText( FindBuy.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d( "Test", "获取数据成功了" );
                //获取后台返回结果
                final String responseData = response.body().string();
                Log.i( "Test", responseData );
                int flag = 0;
                //解析Json数据
                Log.i( "Test", "开始解析" );
                Gson gson = new Gson();
                UserCO goodsBack = gson.fromJson( responseData, UserCO.class );
                Log.i( "Test", "解析完毕" );
                flag = goodsBack.getFlag();
                //成功获取返回消息
                if (flag == 200) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Log.i( "Test", "run: 发布成功" );
                            //取消进度框一
                            progressDialog.dismiss();
                            Toast.makeText( FindBuy.this, "发布成功", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else if (flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            progressDialog.dismiss();
                            Toast.makeText( FindBuy.this, "发布失败！登录信息已失效,请重新登录。", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            progressDialog.dismiss();
                            Toast.makeText( FindBuy.this, "出现错误！,发布失败", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
