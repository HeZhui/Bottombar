package com.example.a15927.bottombardemo.homeactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;

public class HomeSearch extends AppCompatActivity {
    private TextView quit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_search );
        quit_button = (TextView) findViewById(R.id.back_to);
        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回HomeFragment
                //销毁目前的活动
                finish();
            }
        } );
    }
}
