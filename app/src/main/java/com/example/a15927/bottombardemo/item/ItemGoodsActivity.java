package com.example.a15927.bottombardemo.item;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

public class ItemGoodsActivity extends AppCompatActivity {
    private String TAG= "Test";
    private ImageView img;
    private TextView name,number,quality,g_type,price,phone,qq,weixin,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.buy_item );
        init();
        Intent intent = getIntent();
        ItemGoods itemGoods = (ItemGoods) intent.getSerializableExtra( "goodsList" );
        adapterGoodsInfor(itemGoods);
        Log.i( TAG, "onCreate: " +itemGoods.getGoodsName());
    }

    private void adapterGoodsInfor(ItemGoods itemGoods) {
        String goodsName = itemGoods.getGoodsName();
        String goodsId = itemGoods.getGoodsID();
        String goodsType = itemGoods.getGoodsTypeName();
        float price = itemGoods.getPrice();


    }

    private void init() {
    }
}
