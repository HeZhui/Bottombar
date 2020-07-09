package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.MD5Utils;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
import com.example.a15927.bottombardemo.functiontools.UserCheck;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "Test";
    private ImageView arrow_back;
    private TextView back;
    private EditText input_username;
    private EditText input_password;
    private EditText password_confirm;
    private Button comit_info;
    private int opType=90008;//重置密码
    private String url = "http://192.168.0.6:8081/Proj31/user";
    private String token;
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.reset_password );

        arrow_back = (ImageView) findViewById( R.id.arrow_back );
        back = (TextView) findViewById( R.id.back );
        input_username = (EditText) findViewById( R.id.user_input );
        input_password = (EditText) findViewById( R.id.password_input );
        password_confirm = (EditText) findViewById( R.id.password_confirm );
        comit_info = (Button) findViewById( R.id.comit_info );
        comit_info.setOnClickListener( this );
        arrow_back.setOnClickListener( this );
        back.setOnClickListener( this );

        SharedPreferences User = getSharedPreferences( "data", Context.MODE_PRIVATE );
        //如果未找到该值，则使用get方法中传入的默认值false代替
        token = User.getString( "token", "" );
        Log.i( TAG, "ResetPwd : login is "+token );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comit_info:
                passreset();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.arrow_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void passreset(){
        String username = input_username.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        String re_passsword = password_confirm.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            Toast.makeText( ResetPassword.this,"用户名不能为空",Toast.LENGTH_SHORT ).show();
        }
        else{
            if(password.equals( "" )){
                Toast.makeText( ResetPassword.this,"密码不能为空",Toast.LENGTH_SHORT ).show();
            }
            else{
                if(re_passsword.equals( "" )){
                    Toast.makeText( ResetPassword.this,"第二次输入密码不能为空",Toast.LENGTH_SHORT ).show();
                }
                else{
                   if(password.equals( re_passsword )) {
                       //进度框显示方法一
                       progressDialog = DialogUIUtils.showLoadingDialog( ResetPassword.this, "正在重置..." );
                       progressDialog.show();
                       resetPassword(username,password);
                   }
                   else{
                       Toast.makeText( ResetPassword.this,"两次密码不一致",Toast.LENGTH_SHORT ).show();
                   }
                }
            }
        }
    }

    public void resetPassword(String uname,String upassword){
        UserCheck reset = new UserCheck();
        reset.setUsername( uname );
        reset.setPassword( MD5Utils.getMD5( upassword ));
        reset.setOpType( opType );
        reset.setToken( token );
        Gson gson = new Gson();
        String reqJson = gson.toJson( reset,UserCheck.class );
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i( TAG, "onFailure: post" );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        String errorData = TestAndVerify.judgeError( ResetPassword.this );
                        Toast.makeText( ResetPassword.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Test","获取数据成功了");
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " +responseData);
                //开始解析返回数据
                Log.i( TAG, "开始解析数据" );
                Gson gson = new Gson();
                //把属性给到对应的对象中
                UserCO userCO = gson.fromJson( responseData,UserCO.class );
                Log.i( TAG, "解析数据完毕" );
                int flag = userCO.getFlag();
                if(flag == 200){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( ResetPassword.this, "重置密码成功！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else if(flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( ResetPassword.this, "当前登录信息已失效！请重新登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }else{
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( ResetPassword.this, "重置密码失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
