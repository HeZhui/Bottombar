package com.example.a15927.bottombardemo.findactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.GoodsPut;
import com.example.a15927.bottombardemo.functiontools.Goodsback;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserVO;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindSale extends AppCompatActivity implements View.OnClickListener{
    private ImageView photo_taken,back_sale;
    private EditText text_name,text_price,mobile,text_description;
    private TextView comit_sale,saling;

    //服务类型
    private int opType = 90003;
    private  String url = "http://localhost:8081/Proj31/sale";//http://118.89.217.225:8080/Proj20/sale

    //相机参数
    private Uri imageUri;
    private static final int TAKE_PHOTO = 1;

    private Spinner spinner_sale;
    private int spinner_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_sale );

        //初始化并设置监听
        saling = (TextView) findViewById( R.id.saling );
        saling.setOnClickListener( this );

        photo_taken = (ImageView)findViewById( R.id.addopt_photoright );
        photo_taken.setOnClickListener( this );

        back_sale = (ImageView)findViewById( R.id.back_sale);
        back_sale.setOnClickListener( this );

        comit_sale = (TextView)findViewById( R.id.comit_sale );
        comit_sale.setOnClickListener( this );

        text_name = (EditText)findViewById( R.id.name_sale );
        text_price = (EditText)findViewById( R.id.price_sale );

        mobile = (EditText)findViewById( R.id.number_sale );
        text_description = (EditText)findViewById( R.id.description_sale );

        //下拉框
        spinner_sale = (Spinner) findViewById( R.id.spinner_sale );
        //适配器
        spinner_sale.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_position = position;
                Toast.makeText( FindSale.this,"position"+position,Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_sale:
                //返回上一级
                finish();
                break;
            case R.id.saling:
                finish();
                break;
            case R.id.addopt_photoright:
                //创建File对象，用于存储拍照后的图片,并把图片命名为output_image.jpg
                File outputImage = new File( getExternalCacheDir(), "output_image.jpg" );
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //判断版本号
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile( FindSale.this, "com.example.a15927.bottombardemo.fileprovider", outputImage );
                } else {
                    imageUri = Uri.fromFile( outputImage );
                }
                //打开相机程序
                Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                intent_photo.putExtra( MediaStore.EXTRA_OUTPUT,imageUri );
                //startActivity( intent_photo );
                startActivityForResult( intent_photo, TAKE_PHOTO );
                break;
            case R.id.comit_sale:
                // 获取填写的商品名字
                String goods_name = text_name.getText().toString();
                //获取填写的商品价格
                String goods_price = text_price.getText().toString();
                //获取填写的卖家联系方式
                String mobile_phone = mobile.getText().toString();
                //获取填写的商品描述
                String goods_description = text_description.getText().toString();

                comitsale(goods_name,goods_price,mobile_phone,goods_description);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片的照片显示出来
                        //需要对拍摄的照片进行处理编辑
                        //拍照成功的话，就调用BitmapFactory的decodeStream()方法把output_image.jpg解析成Bitmap对象，然后显示
                        Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                        photo_taken.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void comitsale(String goods_name, String goods_price, String mobile_phone, String goods_description){
        //构造部分属性
        GoodsPut goodsPut = new GoodsPut();
        String goodsID = UUID.randomUUID().toString();
        UserVO userVO = new UserVO();
        String uuid = UUID.randomUUID().toString();
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.chen);//从drawable中取一个图片（以后大家需要从相册中取，或者相机中取）。
        //bitmp转bytes
        byte[] uimages = userVO.Bitmap2Bytes(bmp);

        //取出token
        SharedPreferences sp = getSharedPreferences( "data",MODE_PRIVATE );
        String uname = sp.getString( "uname","" );
        String token = sp.getString( "token","" );
        Log.i( "Test", "uname is  " +uname);
        Log.i( "Test", "token is  " +token );

        //设置属性
        goodsPut.setOpType( opType );
        goodsPut.setGoodsID( goodsID );
        goodsPut.setGoodsType( String.valueOf( spinner_position ) );
        goodsPut.setGoodsName( goods_name );
        goodsPut.setPrice( Float.parseFloat( goods_price ) );
        goodsPut.setUnit( "本" );
        goodsPut.setQuality( 1 );
        goodsPut.setUserid( uuid );
        goodsPut.setGoodsImg( uimages );
        goodsPut.setUname( uname );
        goodsPut.setUphone( mobile_phone );
        goodsPut.setSex( 0 );
        goodsPut.setQq( "1574367589" );
        goodsPut.setWeixin( "15645681259" );
        goodsPut.setToken( token );

        //组成Json串
        Gson gson = new Gson();
        String goodsJsonStr = gson.toJson( goodsPut,GoodsPut.class );
        Log.i( "Test", goodsJsonStr );

//        sendGoodsData(url,goodsJsonStr);
        PostWith postWith = new PostWith();
        postWith.sendPostWithOkhttp( url, goodsJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Test","获取数据失败了"+e.toString());
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( FindSale.this, "数据错误", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Test","获取数据成功了");
                //获取后台返回结果
                final String responseData = response.body().string();
                Log.i( "Test",responseData );
                //json转String
                Goodsback goodsback = new Goodsback();
                Gson re_gson = new Gson();
                goodsback = re_gson.fromJson( responseData,Goodsback.class );
                Log.i( "Test", goodsback.toString() );
                int flag = goodsback.getFlag();
                if(flag == 200){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( FindSale.this,"发布成功",Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
                else if(flag == 30001){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( FindSale.this,"登录信息已无效",Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
                else{
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( FindSale.this,"商品发布出错",Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }

//    public void sendGoodsData(String url, final String goodsJsonStr){
//        //创建OkHttpClient对象。
//        OkHttpClient client = new OkHttpClient();
//        //请求体
//        RequestBody requestBody = new FormBody.Builder()
//                .add( "reqJson", goodsJsonStr)
//                .build();
//        //发送请求
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody )
//                .build();
//        //异步执行，获取返回结果
//        client.newCall( request ).enqueue( new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("Test","获取数据失败了"+e.toString());
//                runOnUiThread( new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText( FindSale.this, "数据错误", Toast.LENGTH_SHORT ).show();
//                    }
//                } );
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("Test","获取数据成功了");
//                //获取后台返回结果
//                final String responseData = response.body().string();
//                Log.i( "Test",responseData );
//                //json转String
//                Goodsback goodsback = new Goodsback();
//                Gson re_gson = new Gson();
//                goodsback = re_gson.fromJson( responseData,Goodsback.class );
//                Log.i( "Test", goodsback.toString() );
//                int flag = goodsback.getFlag();
//                if(flag == 200){
//                    runOnUiThread( new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText( FindSale.this,"发布成功",Toast.LENGTH_SHORT ).show();
//                        }
//                    } );
//                }
//                else if(flag == 30001){
//                    runOnUiThread( new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText( FindSale.this,"登录信息已无效",Toast.LENGTH_SHORT ).show();
//                        }
//                    } );
//                }
//                else{
//                    runOnUiThread( new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText( FindSale.this,"商品发布出错",Toast.LENGTH_SHORT ).show();
//                        }
//                    } );
//                }
//            }
//        } );
//
//    }

}
