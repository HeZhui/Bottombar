package com.example.a15927.bottombardemo.item;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

public class ItemBuyActivity extends AppCompatActivity {
    private String TAG = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.item_goods );
        init();
        Intent intent = getIntent();
        ItemGoods itemBuy = (ItemGoods) intent.getSerializableExtra( "itemBuy" );
        Log.i( TAG, "onCreate: "+itemBuy.getDescription() );
    }

    private void init() {
    }
}
