package com.example.a15927.bottombardemo.meactivity;

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

import com.example.a15927.bottombardemo.MainActivity;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.dialog.MyDialog;

public class LoginSetting extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private ImageView back_login;
    private TextView login_out,back,safety;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_setting );

        back_login = (ImageView) findViewById( R.id.back_login );
        back_login.setOnClickListener( this );
        back = (TextView)findViewById( R.id.back );
        back.setOnClickListener( this );
        safety = (TextView)findViewById( R.id.safety );
        safety.setOnClickListener( this );
        login_out = (TextView)findViewById( R.id.login_out );
        login_out.setOnClickListener( this );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.back_login:
                finish();
                break;
            case R.id.safety:
                Intent safe = new Intent( LoginSetting.this,ResetPassword.class );
                startActivity( safe );
                break;
            case R.id.login_out:
                checkOut();
                break;
            default:
                break;
        }
    }

    private void checkOut() {
        SharedPreferences User = getSharedPreferences( "data", Context.MODE_PRIVATE );
        //如果未找到该值，则使用get方法中传入的默认值false代替
        boolean login = User.getBoolean( "login", false );
        Log.i( TAG, "MeFragment: login is "+login );
        if(login == true){
            //弹出一个弹框，询问是否退出
            MyDialog.show(this, "确定退出登录吗?", new MyDialog.OnConfirmListener() {
                @Override
                public void onConfirmClick() {
                    //这里写点击确认后的逻辑
                    SharedPreferences.Editor set_sp = getSharedPreferences( "data", Context.MODE_PRIVATE ).edit();
                    set_sp.putBoolean( "login",false );
                    set_sp.commit();
                    Intent intent_back = new Intent( LoginSetting.this, MainActivity.class );
                    startActivity( intent_back );
                }
            });
        }
        else{
            Toast.makeText( this, "您处于未登录状态，无法退出登录！", Toast.LENGTH_SHORT ).show();
        }
    }
}
