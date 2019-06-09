package com.example.a15927.bottombardemo.findactivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.MyTools.FileUtils;
import com.example.a15927.bottombardemo.MyTools.ImageUtils;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.GoodsPut;
import com.example.a15927.bottombardemo.functiontools.Goodsback;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserVO;
import com.google.gson.Gson;
import com.longsh.optionframelibrary.OptionBottomDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.functiontools.DialogUIUtils.dismiss;

public class FindSale extends AppCompatActivity implements View.OnClickListener {
    private ImageView photo_taken, back_sale;
    private EditText text_name, text_price, mobile, text_description;
    private TextView comit_sale, saling;
    private static String TAG = "Test";
    //服务类型
    private int opType = 90003;
    private String url = "http://47.105.185.251:8081/Proj31/sale";//http://192.168.2.134:8080/Proj20/sale

    //相机参数
    private static Uri imageUri;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;
    //照片存储
    File filePath;

    //下拉框
    private Spinner spinner_sale;
    private static int spinner_position;

    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.find_sale );

        //初始化并设置监听
        saling = (TextView) findViewById( R.id.saling );
        saling.setOnClickListener( this );

        photo_taken = (ImageView) findViewById( R.id.addopt_photoright );
        photo_taken.setOnClickListener( this );

        back_sale = (ImageView) findViewById( R.id.back_sale );
        back_sale.setOnClickListener( this );

        comit_sale = (TextView) findViewById( R.id.comit_sale );
        comit_sale.setOnClickListener( this );

        text_name = (EditText) findViewById( R.id.name_sale );
        text_price = (EditText) findViewById( R.id.price_sale );

        mobile = (EditText) findViewById( R.id.number_sale );
        text_description = (EditText) findViewById( R.id.description_sale );

        //下拉框
        spinner_sale = (Spinner) findViewById( R.id.spinner_sale );
        //适配器
        spinner_sale.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_position = position + 1;
                //测试用
                //Toast.makeText( FindSale.this, "position" + spinner_position, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_sale:
                //返回上一级
                finish();
                break;
            case R.id.saling:
                finish();
                break;
            case R.id.addopt_photoright:
                //底部弹框Dialog
                //获取拍照和手机相册的权利
                List<String> stringList = new ArrayList<>();
                stringList.add( "拍照" );
                stringList.add( "从相册选择" );
                final OptionBottomDialog optionBottomDialog = new OptionBottomDialog( FindSale.this, stringList );
                optionBottomDialog.setItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //拍照逻辑
                                Intent photoIn = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                                imageUri = ImageUtils.getImageUri( FindSale.this );
                                //putExtra()指定图片的输出地址，填入之前获得的Uri对象
                                photoIn.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
                                startActivityForResult( photoIn, TAKE_PHOTO );
                                //底部弹框消失
                                optionBottomDialog.dismiss();
                                break;
                            case 1:
                                //相册选择逻辑
//                                Intent picsIn = new Intent( Intent.ACTION_GET_CONTENT );
                                Intent picsIn = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                //picsIn.setType( "image/*" );//设置选择的数据类型为图片类型
                                startActivityForResult( picsIn, CHOOSE_PHOTO );
                                //底部弹框消失
                                optionBottomDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                } );

                //                //创建File对象，用于存储拍照后的图片,并把图片命名为output_image.jpg
                //                File outputImage = new File( getExternalCacheDir(), "output_image.jpg" );
                //                try {
                //                    if (outputImage.exists()) {
                //                        outputImage.delete();
                //                    }
                //                    outputImage.createNewFile();
                //                } catch (IOException e) {
                //                    e.printStackTrace();
                //                }
                //                //判断版本号
                //                if (Build.VERSION.SDK_INT >= 24) {
                //                    imageUri = FileProvider.getUriForFile( FindSale.this, "com.example.a15927.bottombardemo.fileprovider", outputImage );
                //                } else {
                //                    imageUri = Uri.fromFile( outputImage );
                //                }
                //                //打开相机程序
                //                Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                //                intent_photo.putExtra( MediaStore.EXTRA_OUTPUT,imageUri );
                //                //startActivity( intent_photo );
                //                startActivityForResult( intent_photo, TAKE_PHOTO );
                break;
            case R.id.comit_sale:
                // 获取填写的商品名字
                String goods_name = text_name.getText().toString().trim();
                //获取填写的商品价格
                String goods_price = text_price.getText().toString().trim();
                //获取填写的卖家联系方式
                String mobile_phone = mobile.getText().toString().trim();
                //获取填写的商品描述
                String goods_description = text_description.getText().toString().trim();

                if( goods_name == null || goods_price == null  ){
                    Toast.makeText( FindSale.this, "请填写完整的商品信息", Toast.LENGTH_SHORT ).show();
                }else{
                    //进度框显示方法一
                    progressDialog = DialogUIUtils.showLoadingDialog( FindSale.this,"正在发布商品" );
                    progressDialog.show();
                    comitsale( goods_name, goods_price, mobile_phone, goods_description );
                }
                break;
        }
    }

    private void startImageCrop(File saveToFile,Uri uri) {
        if(uri == null){
            return ;
        }
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        Log.i( TAG, "startImageCrop: " + "执行到压缩图片了" + "uri is " + uri );
        intent.setDataAndType( uri, "image/*" );//设置Uri及类型
        //uri权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra( "crop", "true" );//
        intent.putExtra( "aspectX", 1 );//X方向上的比例
        intent.putExtra( "aspectY", 1 );//Y方向上的比例
        intent.putExtra( "outputX", 150 );//裁剪区的X方向宽
        intent.putExtra( "outputY", 150 );//裁剪区的Y方向宽
        intent.putExtra( "scale", true );//是否保留比例
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.PNG.toString() );
        intent.putExtra( "return-data", false );//是否将数据保留在Bitmap中返回dataParcelable相应的Bitmap数据，防止造成OOM
        //判断文件是否存在
        //File saveToFile = ImageUtils.getTempFile();
        if (!saveToFile.getParentFile().exists()) {
            saveToFile.getParentFile().mkdirs();
        }
        //将剪切后的图片存储到此文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveToFile));
        Log.i( TAG, "startImageCrop: " + "即将跳到剪切图片" );
        startActivityForResult( intent, CROP_IMAGE );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //将拍摄的照片的照片显示出来
                    //需要对拍摄的照片进行处理编辑
                    //拍照成功的话，就调用BitmapFactory的decodeStream()方法把图片解析成Bitmap对象，然后显示
                    Log.i( TAG, "onActivityResult TakePhoto : "+imageUri );
                    //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //photo_taken.setImageBitmap( bitmap );
                    //设置照片存储文件及剪切图片
                    File saveFile = ImageUtils.getTempFile();
                    filePath = ImageUtils.getTempFile();
                    startImageCrop( saveFile,imageUri );
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //选中相册照片显示
                    Log.i( "Test", "onActivityResult: 执行到打开相册了" );
                    try {
                        imageUri = data.getData(); //获取系统返回的照片的Uri
                        Log.i( TAG, "onActivityResult: uriImage is " +imageUri );
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
//                        photo_taken.setImageBitmap(bitmap);
                        //设置照片存储文件及剪切图片
                        File saveFile = ImageUtils.setTempFile( FindSale.this );
                        filePath = ImageUtils.getTempFile();
                        startImageCrop( saveFile,imageUri );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_IMAGE:
                if(resultCode == RESULT_OK){
                    Log.i( TAG, "onActivityResult: CROP_IMAGE" + "进入了CROP");
                    // 通过图片URI拿到剪切图片
                    //bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //通过FileName拿到图片
                    Bitmap bitmap = BitmapFactory.decodeFile( filePath.toString() );
                    //把裁剪后的图片展示出来
                    photo_taken.setImageBitmap( bitmap );
                    //ImageUtils.Compress( bitmap );
                }
                break;
            default:
                break;
        }
    }

    public void comitsale(String goods_name, String goods_price, String mobile_phone, String goods_description) {


        //构造部分属性
        GoodsPut goodsPut = new GoodsPut();
        String goodsID = UUID.randomUUID().toString().replaceAll( "-", "" );
        UserVO userVO = new UserVO();
        String uuid = UUID.randomUUID().toString().replaceAll( "-", "" );
//        Resources res = getResources();
//        Bitmap bmp = BitmapFactory.decodeResource( res, R.drawable.chen );//从drawable中取一个图片（以后大家需要从相册中取，或者相机中取）。
        //filePath = ImageUtils.getTempFile( );
        Log.i( TAG, "comitsale: filePath is "+filePath.toString() );
        Bitmap bmp =BitmapFactory.decodeFile( filePath.toString() );
        //bitmp转bytes
        byte[] uimages = FileUtils.Bitmap2Bytes( bmp );

        //取出token
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String uname = sp.getString( "uname", "" );
        String token = sp.getString( "token", "" );
        Log.i( "Test", "uname is  " + uname );
        Log.i( "Test", "token is  " + token );

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
        String goodsJsonStr = gson.toJson( goodsPut, GoodsPut.class );
        Log.i( "Test", goodsJsonStr.toString() );

        //        sendGoodsData(url,goodsJsonStr);
        PostWith postWith = new PostWith();
        postWith.sendPostWithOkhttp( url, goodsJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( "Test", "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( FindSale.this, "网络不给力！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d( "Test", "获取数据成功了" );
                //获取后台返回结果
                final String responseData = response.body().string();
                Log.i( "Test", responseData );
                //json转String
                Gson re_gson = new Gson();
                Goodsback goodsback = re_gson.fromJson( responseData, Goodsback.class );
                Log.i( "Test", goodsback.toString() );
                int flag = goodsback.getFlag();
                if (flag == 200) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss(progressDialog);
                            Toast.makeText( FindSale.this, "发布成功", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else if (flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss(progressDialog);
                            Toast.makeText( FindSale.this, "登录信息已无效，请重新登录", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss(progressDialog);
                            Toast.makeText( FindSale.this, "商品发布出错", Toast.LENGTH_SHORT ).show();
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
