package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.MainActivity;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.MD5Utils;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserBO;
import com.example.a15927.bottombardemo.functiontools.UserQuery;
import com.example.a15927.bottombardemo.functiontools.UserVO;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;


public class MeLogin extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_arrow;
    private CheckBox cb_mima;
    private EditText in_username, in_password;
    private Button meLogin, register;
    private TextView forgetpassword;
    private String url = "http://47.105.185.251:8081/Proj31/login";//http://192.168.2.134:8081/Proj20/login
    private int opType = 90002;
    private String TAG = "Test";

    //判断是否是登录过
    private boolean login = false;

    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.me_login );

        back_arrow = (ImageView) findViewById( R.id.arrow_back );
        back_arrow.setOnClickListener( this );

        cb_mima = (CheckBox) findViewById( R.id.cb_mima );
        cb_mima.setOnClickListener( this );

        in_username = (EditText) findViewById( R.id.in_username );
        in_password = (EditText) findViewById( R.id.in_password );

        meLogin = (Button) findViewById( R.id.login_in );
        meLogin.setOnClickListener( this );

        register = (Button) findViewById( R.id.register_in );
        register.setOnClickListener( this );

        forgetpassword = (TextView) findViewById( R.id.forget_to );
        forgetpassword.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.login_in:
                // 登录程序
                Login();
                break;
            case R.id.register_in:
                Intent intent_register = new Intent( MeLogin.this, RegisterIn.class );
                startActivity( intent_register );
                break;
            case R.id.forget_to:
                Intent intent_reset = new Intent( MeLogin.this, ResetPassword.class );
                startActivity( intent_reset );
                break;
            case R.id.cb_mima:

                break;
            default:
                break;
        }
    }

    //登录
    public void Login() {
        final String username = in_username.getText().toString().trim();
        final String password = in_password.getText().toString().trim();

        //检查数据格式是否正确
        if (TextUtils.isEmpty( username ) | TextUtils.isEmpty( password )) {
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    Toast.makeText( MeLogin.this, "用户名和密码不可为空！", Toast.LENGTH_SHORT ).show();
                }
            } );
        } else {
            //创建实例对象
            UserBO userBO = new UserBO();
            //设置属性值
            userBO.setOpType( opType );
            userBO.setUname( username );
            userBO.setUpassword( MD5Utils.getMD5( password ) );
            //封装为json串
            Gson gson = new Gson();
            String userJsonStr = gson.toJson( userBO, UserBO.class );
            Log.i( TAG, "Login: userJsonStr  is " + userJsonStr );

            //进度框显示方法一
            progressDialog = DialogUIUtils.showLoadingDialog( MeLogin.this, "正在登录" );
            progressDialog.show();
            //发送请求
            postDataLogin( username, userJsonStr );
        }
    }

    public void postDataLogin(final String uname, String userJsonStr) {
        PostWith.sendPostWithOkhttp( url, userJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //这里是子线程
                Log.d( TAG, "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        String errorData = TestAndVerify.judgeError( MeLogin.this );
                        Toast.makeText( MeLogin.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d( TAG, "获取数据成功了" );
                    final String str = response.body().string();
                    Log.d( TAG, "response.body().string() is " + str );
                    Gson gson = new Gson();
                    UserQuery user = gson.fromJson( str,UserQuery.class );
                    UserVO userVO = user.getUser();
                    int flag = user.getFlag();
                    if(flag == 200){
                        //保存Token
                        SharedPreferences.Editor editor = getSharedPreferences( "data", MODE_PRIVATE ).edit();
                        login = true;
                        editor.putBoolean( "login",login );
                        editor.putString( "uname", uname );
                        editor.putString( "token", user.getToken() );
                        editor.putString( "picDir",userVO.getPicDir() );
                        editor.putString( "ps",userVO.getPs());
                        editor.putString( "uid",userVO.getUid() );
                        editor.commit();
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                dismiss(progressDialog);
                                Toast.makeText( MeLogin.this, "登录成功!", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( MeLogin.this, MainActivity.class );
                                startActivity( intent );
                            }
                        } );
                    } else if (flag == 20005) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                dismiss(progressDialog);
                                Toast.makeText( MeLogin.this, "用户名或密码不正确，登录失败！", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    } else {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                dismiss(progressDialog);
                                Toast.makeText( MeLogin.this, "登录失败！", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                }
            }
        });
    }
}

