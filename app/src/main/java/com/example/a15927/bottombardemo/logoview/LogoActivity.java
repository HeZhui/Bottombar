package com.example.a15927.bottombardemo.logoview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.a15927.bottombardemo.MainActivity;
import com.example.a15927.bottombardemo.R;

public class LogoActivity extends AppCompatActivity {
    private Handler myHandler = new Handler( );
    private ImageView iv_logo;
//    private ImageView fle;
//    private ImageView mar;
//    private TextView fle_text;
    private RelativeLayout rc_logo;

//    private static final long DELAY_TIME = 3000L;
//    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_logo );

        /**
         * 绑定控件
         */
        iv_logo = (ImageView) findViewById( R.id.iv_logo );
        rc_logo = (RelativeLayout)findViewById( R.id.rc_logo );
//        fle = (ImageView) findViewById( R.id.fle );
//        mar = (ImageView) findViewById( R.id.mar );
//        fle_text = (TextView) findViewById( R.id.fle_text );
        startAlphaAnimation();
        myHandler.postDelayed( logoRunnable,3000 );//推迟执行Runnable，但是此方法会造成内存泄漏，必须结束时销毁handler
//        preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
//        boolean isSplashShown = preferences.getBoolean("isSplashShown",false);
//        if(isSplashShown){
//            startActivity(new Intent(LogoActivity.this,MainActivity.class));
//            finish();
//        }
//        new Handler().postDelayed(new Runnable(){
//
//            @Override
//            public void run() {
//                startActivity(new Intent(LogoActivity.this,MainActivity.class));
//                SharedPreferences.Editor editor=preferences.edit();
//                editor.putBoolean("isSplashShown", true);
//                editor.commit();
//                finish();
//            }
//        },DELAY_TIME);
    }

    /**
     * 开始动画
     */
    public void  startAlphaAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation( 0.1f,1.0f );//开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
        alphaAnimation.setDuration( 3000 );//动画执行时间(毫秒)
        iv_logo.startAnimation( alphaAnimation );//开始动画
        rc_logo.startAnimation( alphaAnimation );
//        fle.startAnimation( alphaAnimation );
//        fle_text.startAnimation( alphaAnimation );
//        mar.startAnimation( alphaAnimation );
    }

    /**
     * 管理logo的Runnable
     */
    Runnable logoRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent_logo = new Intent(  );
            intent_logo.setClass( LogoActivity.this, MainActivity.class );
            startActivity( intent_logo );
            finish();
        }
    };

    @Override
    protected void onDestroy(){
        if(myHandler!=null){
            myHandler.removeCallbacks( logoRunnable );//移除所有的消息和回调，简单一句话就是清空了消息队列
            myHandler = null;
        }
        super.onDestroy();
    }
}
