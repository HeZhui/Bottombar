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
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindBuy extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private EditText name_buy, phone_buy, description_buy;
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
        phone_buy = (EditText) findViewById( R.id.phone_buy );
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
                String phone = phone_buy.getText().toString().trim();
                String description = description_buy.getText().toString().trim();
                if (goodsName.length() == 0 || phone.length() == 0) {
                    Toast.makeText( this, "请完善需求信息！", Toast.LENGTH_SHORT ).show();
                } else {
                    if (phone.length() == 11) {
                        if (checkPhone( phone ) == true) {
                            //进度框显示方法一
                            progressDialog = DialogUIUtils.showLoadingDialog( FindBuy.this,"正在发布商品" );
                            progressDialog.show();
                            require( goodsName, phone, description );
                        } else {
                            Toast.makeText( this, "手机号码有误，请重新输入！", Toast.LENGTH_SHORT ).show();
                        }
                    } else {
                        Toast.makeText( this, "手机号码有误，请重新输入！", Toast.LENGTH_SHORT ).show();
                    }
                    break;
                }
        }
    }

    //正则表达式验证手机号码
    private boolean checkPhone(String str) {
        if (str == null) {
            return false;
        }
        String regEx = "^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\\d{8}$";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void require(String goodsName, String phone, String description) {
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String uname = sp.getString( "uname", "" );
        String token = sp.getString( "token", "" );
        Log.i( "Test", "uname is  " + uname );
        Log.i( "Test", "token is  " + token );
        //设置属性
        GoodsBuy goodsBuy = new GoodsBuy();
        goodsBuy.setToken( token );
        goodsBuy.setOpType( opType );
        goodsBuy.setDescription( description );
        goodsBuy.setGoodsName( goodsName );
        goodsBuy.setGoodsType( String.valueOf( spinner_position ) );
        goodsBuy.setPhone( phone );
        Gson gson = new Gson();
        String jsonStr = gson.toJson( goodsBuy, GoodsBuy.class );
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
                        Toast.makeText( FindBuy.this, "网络不给力哦！", Toast.LENGTH_SHORT ).show();
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
                            Toast.makeText( FindBuy.this, "登录信息已失效,请重新登录", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            progressDialog.dismiss();
                            Toast.makeText( FindBuy.this, "出现错误！,登录失败", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
