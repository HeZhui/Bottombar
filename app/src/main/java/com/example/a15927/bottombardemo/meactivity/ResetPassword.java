package com.example.a15927.bottombardemo.meactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener{
    private ImageView arrow_back;
    private TextView back;
    private EditText input_username;
    private EditText input_password;
    private EditText password_confirm;
    private Button comit_info;
    private int opType=90007;
    private String url = "http://118.89.217.225:8080/Proj20/user";

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

    }


        //检查数据格式是否正确
//        String resMsg = checkDataValid( username,password,re_passsword );
//        if (!resMsg.equals( "" )) {
//            showResponse( resMsg );
//        }

        //搜索数据库中用户名
        // 若不存在，则提示用户不存在
        //若用户名存在，则继续重置
       //重置完了之后，将数据库中的此用户的密码重写
       // Toast.makeText(ResetPassword.this,"重置密码成功",Toast.LENGTH_SHORT).show();
        //Intent reset_intent = new Intent(  );
        //startActivity(reset_intent);


//    private void showResponse(final String msg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(ResetPassword.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private String checkDataValid(String account,String pwd,String pwd_confirm){
//        if(TextUtils.isEmpty(account) | TextUtils.isEmpty(pwd) | TextUtils.isEmpty(pwd_confirm))
//            return "账户或密码不能为空";
//        if(!pwd.equals(pwd_confirm))
//            return "两次输入的密码不一致";
//        if(account.length() != 11 && !account.contains("@"))
//            return "用户名不是有效的手机号或者邮箱";
//        return "";
//    }




}
