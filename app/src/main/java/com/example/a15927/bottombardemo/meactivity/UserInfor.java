package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
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
import com.example.a15927.bottombardemo.MainActivity;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.AppStr;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCheck;
import com.example.a15927.bottombardemo.functiontools.UserQuery;
import com.example.a15927.bottombardemo.functiontools.UserVO;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class UserInfor extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private ImageView back_login;//返回
    private TextView back,update_info;//返回与更换头像
    private ImageView userImage;//用户头像
    private TextView userId,userAccount,userSex,userPhone,userQQ,userWeixin,ps;//用户信息

    private UserVO userInfo = null;
    private int op_query = 90006;    //查询
    private String url = "http://192.168.0.6:8081/Proj31/user";

    //进度条一
    Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.user_infor );
        //初始化绑定控件
        initView();

        //从sp中取出token
        SharedPreferences User = getSharedPreferences( "data", Context.MODE_PRIVATE );
        String token = User.getString( "token","" );
        //设置属性
        UserCheck userCheck = new UserCheck();
        userCheck.setOpType( op_query );
        userCheck.setToken( token );
        //封装Json串
        Gson gson = new Gson();
        String reqJson = gson.toJson( userCheck,UserCheck.class );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( UserInfor.this, "正在查询..." );
        progressDialog.show();
        //application全局变量
        final AppStr appStr = (AppStr)getApplication();
        appStr.setState( false );
        postRequest(reqJson);
    }

    private void postRequest(String reqJson) {
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //application全局变量
                        AppStr appStr = (AppStr)getApplication();
                        appStr.setState( true );
                        //取消进度框一
                        dismiss( progressDialog );
                        String errorData = TestAndVerify.judgeError( UserInfor.this );
                        Toast.makeText( UserInfor.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d( TAG, "获取数据成功了" );
                    final String responseData = response.body().string();
                    Log.i( TAG, "onResponse: responseData is " + responseData );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Gson g = new Gson();
                            UserQuery userQuery = g.fromJson( responseData,UserQuery.class );
                            int flag = userQuery.getFlag();
                            Log.i( TAG, "run: flag is "+flag );
                            userInfo = new UserVO();
                            userInfo = userQuery.getUser();
                            Log.i( TAG, "run: userVO is "+ userInfo);
                            //application全局变量
                            AppStr appStr = (AppStr)getApplication();
                            appStr.setState( true );
                            if(flag == 200){
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: success" );
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        showUserInfo( userInfo );
                                        Toast.makeText( UserInfor.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                                    }
                                } );
                            } else if (flag == 30001){
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: token无效" );
                                Toast.makeText( UserInfor.this, "当前登录信息无效，请重新登录", Toast.LENGTH_SHORT ).show();
                            }
                            else{
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询失败" );
                                Toast.makeText( UserInfor.this, "获取用户信息失败", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
                }
            }
        } );
    }

    private void showUserInfo(UserVO userVO) {
        Glide.with( UserInfor.this ).load( userVO.getPicDir() ).centerCrop().error( R.drawable.kimg ).into( userImage );
        userId.setText( userVO.getUid() );
        userAccount.setText( userVO.getUname() );
        String gender = userVO.getSex() == 1 ? "男" : "女";
        userSex.setText( gender );
        userPhone.setText( userVO.getUphone() );
        userQQ.setText( userVO.getQq() );
        userWeixin.setText( userVO.getWeixin() );
        if(userVO.getPs() == null){
            ps.setText( "您还没有个性签名哦！" );
        }else{
            ps.setText( userVO.getPs() );
        }
    }

    private void initView() {
        back_login = (ImageView)findViewById( R.id.back_login );
        back_login.setOnClickListener( this );

        back = (TextView)findViewById( R.id.back );
        back.setOnClickListener( this );

        userImage = (ImageView)findViewById( R.id.user_image );
        userImage.setOnClickListener( this );

        update_info = (TextView)findViewById( R.id.update_info ) ;
        update_info.setOnClickListener( this );

        userId = (TextView)findViewById( R.id.id_text );
        userAccount = (TextView)findViewById( R.id.userNo );
        userPhone =(TextView)findViewById( R.id.mail_text );
        userSex = (TextView)findViewById( R.id.sex_Info );
        userQQ = (TextView)findViewById( R.id.QQ_text );
        userWeixin = (TextView)findViewById( R.id.weixin_text );
        ps = (TextView)findViewById( R.id.description_text );
    }

    @Override
    public void onClick(View v) {
        AppStr appStr = (AppStr)getApplication();
        switch(v.getId()) {
            case R.id.back_login:
                if(appStr.isUpdateInf()){
                    Intent intent = new Intent( UserInfor.this, MainActivity.class );
                    startActivity( intent );
                    finish();
                }else{
                    finish();
                }
                break;
            case R.id.back:
                if(appStr.isUpdateInf()){
                    Intent intent = new Intent( UserInfor.this, MainActivity.class );
                    startActivity( intent );
                    finish();
                }else{
                    finish();
                }
                break;
            case R.id.user_image:

                break;
            case R.id.update_info:
                Bundle data = new Bundle(  );
                data.putSerializable( "userInf", userInfo);
                Intent intent = new Intent( UserInfor.this,UpdateUserInfo.class );
                intent.putExtras( data );
                startActivity( intent );
                appStr.setUpdateInf( false );
                finish();
                break;
            default:
                break;
        }
    }
}

