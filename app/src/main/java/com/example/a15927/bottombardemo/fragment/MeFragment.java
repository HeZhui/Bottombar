package com.example.a15927.bottombardemo.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.AppStr;
import com.example.a15927.bottombardemo.Utils.FileUtils;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCheck;
import com.example.a15927.bottombardemo.functiontools.UserQuery;
import com.example.a15927.bottombardemo.functiontools.UserVO;
import com.example.a15927.bottombardemo.meactivity.About_us;
import com.example.a15927.bottombardemo.meactivity.CollectionInfor;
import com.example.a15927.bottombardemo.meactivity.LoginSetting;
import com.example.a15927.bottombardemo.meactivity.MeLogin;
import com.example.a15927.bottombardemo.meactivity.MyForBuy;
import com.example.a15927.bottombardemo.meactivity.MyShop;
import com.example.a15927.bottombardemo.meactivity.UserInfor;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class MeFragment extends Fragment {
    private String TAG = "Test";
    //初始化
    private RelativeLayout Login;
    private ImageView image_plus;
    private TextView user;
    private RelativeLayout  setting,forShop,forBuy,collection,about_us;

    private boolean login = false;
    private String token;

    private UserVO userInfo = new UserVO();
    private int op_query = 90006;    //查询
    private String url = "http://47.105.185.251:8081/Proj31/user";
    //进度条一
    Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        //绑定控件，设置监听
        Login = (RelativeLayout) view.findViewById(R.id.login_me);
        image_plus = (ImageView)view.findViewById( R.id.image_plus ) ;
        user = (TextView)view.findViewById( R.id.user );
        forShop = (RelativeLayout) view.findViewById( R.id.my_shop );
        forBuy = (RelativeLayout) view.findViewById( R.id.my_request );
        collection = (RelativeLayout)view.findViewById( R.id.my_collect );
        about_us = (RelativeLayout)view.findViewById( R.id.about_us );
        setting = (RelativeLayout) view.findViewById( R.id.set_up );
        //bug:只要登录过一次,login==true
        //取出从登录界面存储的登录是否成功的标志
        SharedPreferences User = getActivity().getSharedPreferences( "data", Context.MODE_PRIVATE );
        //如果未找到该值，则使用get方法中传入的默认值false代替
        login = User.getBoolean( "login", false );
        Log.i( TAG, "MeFragment: login is "+login );
        token = User.getString( "token","" );
        //如果已经登录成功
        if(login == true){
            //从登录界面data取出用户名
            String username = User.getString( "uname","" );
            Log.i( TAG, "MeFragment: username is "+username );
            user.setText( username );
            user.setTextSize( 20 );
            user.setTextColor( Color.parseColor("#eeeeee") );

            //设置属性
            UserCheck userCheck = new UserCheck();
            userCheck.setOpType( op_query );
            userCheck.setToken( token );
            //封装Json串
            Gson gson = new Gson();
            String reqJson = gson.toJson( userCheck,UserCheck.class );
            //进度框显示方法一
            progressDialog = DialogUIUtils.showLoadingDialog( getActivity(), "正在查询..." );
            progressDialog.show();
            //application全局变量
            AppStr appStr = (AppStr)getActivity().getApplication();
            appStr.setState( false );
            postRequest(reqJson);
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //没有登录成功
                if(login == false) {
                    //进入登录界面
                    Intent intent_login = new Intent( getActivity(), MeLogin.class );
                    startActivity( intent_login );
                }
                //登录成功
                else{
                    //进入用户详情界面
                    Bundle data = new Bundle(  );
                    data.putSerializable( "userInfo",userInfo );
                    Intent intent_info = new Intent( getActivity(), UserInfor.class );
                    intent_info.putExtras( data );
                    startActivity( intent_info );
                }
            }
        });

        forShop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login == true){
                    //进入我的店铺
                    Intent shop = new Intent( getActivity(), MyShop.class );
                    startActivity( shop );
                }else{
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), "登录之后才能查看哦，请先登录账号！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );

        forBuy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login == true){
                    //进入我的店铺
                    Intent forBuy = new Intent( getActivity(), MyForBuy.class );
                    startActivity( forBuy );
                }else{
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), "登录之后才能查看哦，请先登录账号！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );

        collection.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login == true){
                    //进入我的店铺
                    Intent collect = new Intent( getActivity(), CollectionInfor.class );
                    startActivity( collect );
                }else{
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), "登录之后才能查看哦，请先登录账号！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );

        about_us.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent as = new Intent( getActivity(), About_us.class );
                startActivity( as );
            }
        } );

        setting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_set = new Intent( getActivity(), LoginSetting.class );
                startActivity( intent_set );
            }
        } );
        return view;
    }

    private void postRequest(String reqJson) {
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "获取数据失败了" + e.toString() );
                //application全局变量
                AppStr appStr = (AppStr)getActivity().getApplication();
                appStr.setState( true );
                getActivity().runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        Toast.makeText( getActivity(), "网络不给力！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d( TAG, "获取数据成功了" );
                    final String responseData = response.body().string();
                    Log.i( TAG, "onResponse: responseData is " + responseData );
                    //application全局变量
                    AppStr appStr = (AppStr)getActivity().getApplication();
                    appStr.setState( true );
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Gson g = new Gson();
                            UserQuery userQuery = g.fromJson( responseData,UserQuery.class );
                            int flag = userQuery.getFlag();
                            Log.i( TAG, "run: flag is "+flag );
                            userInfo = userQuery.getUser();
                            Log.i( TAG, "run: userVO is "+ userInfo);
                            if(flag == 200){
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: success" );
                                Toast.makeText( getActivity(), "查询成功！", Toast.LENGTH_SHORT ).show();
                                Bitmap bitmap = FileUtils.Bytes2Bimap( userInfo.getUimage());
                                image_plus.setImageBitmap( bitmap );
                            }
                            else if (flag == 30001){
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: token无效" );
                                Toast.makeText( getActivity(), "当前登录信息无效，请重新登录", Toast.LENGTH_SHORT ).show();
                            }
                            else{
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询失败" );
                                Toast.makeText( getActivity(), "获取用户信息失败", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
                }
            }
        } );
    }
}