package com.example.a15927.bottombardemo.item;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

public class ItemGoodsActivity extends AppCompatActivity {
    private String TAG= "Test";
    private ImageView img,back_s;
    private TextView back,g_goodsName,g_id,g_quality,g_uint,g_typeName,g_price,g_phone,g_qq,g_weixin,g_userId,g_userName,g_decription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.item_goods );
        init();
        Intent intent = getIntent();
        ItemGoods itemGoods = (ItemGoods) intent.getSerializableExtra( "goodsList" );
        adapterGoodsInfor(itemGoods);
    }

    private void adapterGoodsInfor(ItemGoods itemGoods) {
        byte[] img_array = itemGoods.getGoodsImg();
        if(img_array == null ){
            img.setImageResource( R.drawable.kimg );
            Log.i( TAG, "adapterGoodsInfor: 商品图片为空！"+img_array );
        }else{
            Bitmap bitmap = BitmapFactory.decodeByteArray( img_array,0,img_array.length,null );
            img.setImageBitmap( bitmap );
        }
        g_id.setText( itemGoods.getGoodsID() );
        g_goodsName.setText( itemGoods.getGoodsName() );
        g_quality.setText( String.valueOf( itemGoods.getQuality() ) );
        g_price.setText( String.valueOf( itemGoods.getPrice() ) );
        g_uint.setText( itemGoods.getUnit() );
        g_typeName.setText( itemGoods.getGoodsTypeName() );
        g_userName.setText( itemGoods.getUname() );
        g_userId.setText( itemGoods.getUserid() );
        g_phone.setText( itemGoods.getUphone() );
        g_qq.setText( itemGoods.getQq() );
        g_weixin.setText( itemGoods.getWeixin() );
        if(itemGoods.getDescription() == null){
            g_decription.setText( "还没有关于当前商品的说明哦！" );
        }else {
            g_decription.setText( itemGoods.getDescription() );
        }
    }

    private void init() {
        img = (ImageView)findViewById( R.id.g_img );
        g_id = (TextView)findViewById( R.id.g_goods_id );
        g_goodsName = (TextView)findViewById( R.id.g_goods_name );
        g_quality = (TextView)findViewById( R.id.g_quality_num );
        g_uint =(TextView)findViewById( R.id.uint );
        g_price = (TextView)findViewById( R.id.price_num );
        g_typeName = (TextView)findViewById( R.id.g_typeName );
        g_userName = (TextView)findViewById( R.id.g_username );
        g_userId = (TextView)findViewById( R.id.g_id_user );
        g_phone = (TextView)findViewById( R.id.g_phone_user );
        g_qq = (TextView)findViewById( R.id.g_qq_user );
        g_weixin = (TextView)findViewById( R.id.g_weixin_user );
        g_decription = (TextView)findViewById( R.id.g_description_goods );
        back_s = (ImageView)findViewById( R.id.back_s );
        back_s.setOnClickListener( new View.OnClickListener() {
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
