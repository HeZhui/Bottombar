package com.example.a15927.bottombardemo.item;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

public class ItemBuyActivity extends AppCompatActivity {
    private String TAG = "Test";
    private ImageView b_img,back_g;
    private TextView back,b_id,b_goodsName,b_goodsType,b_userName,b_userId,b_phone,b_qq,b_weixin,b_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.buy_item );
        init();
        Intent intent = getIntent();
        ItemGoods itemBuy = (ItemGoods) intent.getSerializableExtra( "itemBuy" );
        adapterBuyInfo(itemBuy);
    }

    private void adapterBuyInfo(ItemGoods itemBuy) {
        byte[] img_array = itemBuy.getGoodsImg();
        b_img.setImageBitmap( BitmapFactory.decodeByteArray( img_array,0,img_array.length,null ) );
        b_id.setText( itemBuy.getGoodsID() );
        b_goodsName.setText( itemBuy.getGoodsName() );
        b_goodsType.setText( itemBuy.getGoodsTypeName() );
        b_userName.setText( itemBuy.getUname() );
        b_userId.setText( itemBuy.getUserid() );
        b_phone.setText( itemBuy.getUphone() );
        b_qq.setText( itemBuy.getQq() );
        b_weixin.setText( itemBuy.getWeixin() );
        b_description.setText( itemBuy.getDescription() );
    }

    private void init() {
        b_img = (ImageView)findViewById( R.id.b_img );
        b_id = (TextView)findViewById( R.id.b_id );
        b_goodsName = (TextView)findViewById( R.id.b_goodsName );
        b_goodsType = (TextView)findViewById( R.id.b_goodsType );
        b_userName = (TextView)findViewById( R.id.b_userName );
        b_userId = (TextView)findViewById( R.id.b_id_user );
        b_phone = (TextView)findViewById( R.id.b_phone_user );
        b_qq = (TextView)findViewById( R.id.b_qq_user );
        b_weixin = (TextView)findViewById( R.id.b_weixin_user );
        b_description =(TextView)findViewById( R.id.b_description_buy );
        back_g = (ImageView)findViewById( R.id.back_g );
        back_g.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        back = (TextView)findViewById( R.id.back );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
    }
}
