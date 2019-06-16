package com.example.a15927.bottombardemo.meactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.a15927.bottombardemo.R;

public class MyShop extends AppCompatActivity {
    private String TAG = "Test";
    private ImageView back_shop;
    private RecyclerView recyclerView_shop;
    private View netFailed,nothing_find;

    private String url = "http://47.105.185.251:8081/Proj31/shop";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_shop );

        back_shop = (ImageView)findViewById( R.id.back_shop );
        recyclerView_shop = (RecyclerView)findViewById( R.id.recycler_shop );
        netFailed = findViewById( R.id.layout_net_failed_shop );
        nothing_find = findViewById( R.id.nothing_find );


    }
}
