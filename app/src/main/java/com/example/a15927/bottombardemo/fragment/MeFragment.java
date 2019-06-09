package com.example.a15927.bottombardemo.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.meactivity.LoginSetting;
import com.example.a15927.bottombardemo.meactivity.MeLogin;
import com.example.a15927.bottombardemo.meactivity.UserInfor;

public class MeFragment extends Fragment {
    private String TAG = "Test";
    //初始化
    private RelativeLayout Login;
    private ImageView image_plus;
    private TextView user,setting;

    private boolean login = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        //绑定控件，设置监听
        Login = (RelativeLayout) view.findViewById(R.id.login_me);
        image_plus = (ImageView)view.findViewById( R.id.image_plus ) ;
        user = (TextView)view.findViewById( R.id.user );

        setting = (TextView)view.findViewById( R.id.setting );
        //bug:只要登录过一次,login==true
        //取出从登录界面存储的登录是否成功的标志
        SharedPreferences User = getActivity().getSharedPreferences( "data", Context.MODE_PRIVATE );
        //如果未找到该值，则使用get方法中传入的默认值false代替
        login = User.getBoolean( "login", false );
        Log.i( TAG, "MeFragment: login is "+login );
        //如果已经登录成功
        if(login == true){
            //从登录界面data取出用户名
            String username = User.getString( "uname","" );
            Log.i( TAG, "MeFragment: username is "+username );
            String filePath = User.getString( "filePath","" );
            Log.i( TAG, "onCreateView: filePath is "+filePath );
            user.setText( username );
            user.setTextSize( 20 );
            user.setTextColor( Color.parseColor("#000000") );
            Bitmap bitmap = BitmapFactory.decodeFile( filePath );
            image_plus.setImageBitmap( bitmap );
        }

        setting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_set = new Intent( getActivity(), LoginSetting.class );
                startActivity( intent_set );
            }
        } );

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
                    Intent intent_lnfo = new Intent( getActivity(), UserInfor.class );
                    startActivity( intent_lnfo );
                }
            }
        });

        return view;

    }

}