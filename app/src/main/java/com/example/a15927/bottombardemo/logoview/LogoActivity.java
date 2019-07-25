package com.example.a15927.bottombardemo.logoview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15927.bottombardemo.MainActivity;
import com.example.a15927.bottombardemo.R;

public class LogoActivity extends AppCompatActivity {
    private Handler myhander = new Handler(  );
    private ImageView iv_logo;
    private ImageView fle;
    private ImageView mar;
    private TextView fle_text;

//    private static final long DELAY_TIME = 3000L;
//    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_logo );

        iv_logo = (ImageView) findViewById( R.id.iv_logo );
        fle = (ImageView) findViewById( R.id.fle );
        mar = (ImageView) findViewById( R.id.mar );
        fle_text = (TextView) findViewById( R.id.fle_text );
        startAlphaAnimation();
        myhander.postDelayed( r,3000 );
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

    public void  startAlphaAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation( 0.1f,1.0f );
        alphaAnimation.setDuration( 3000 );//开始动画
        iv_logo.startAnimation( alphaAnimation );
        fle.startAnimation( alphaAnimation );
        fle_text.startAnimation( alphaAnimation );
        mar.startAnimation( alphaAnimation );
    }

    Runnable r = new Runnable() {
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
        if(myhander!=null){
            myhander.removeCallbacks( r );
            myhander = null;
        }
        super.onDestroy();
    }
}
