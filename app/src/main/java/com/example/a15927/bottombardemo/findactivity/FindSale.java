package com.example.a15927.bottombardemo.findactivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.AppStr;
import com.example.a15927.bottombardemo.Utils.ImageUtils;
import com.example.a15927.bottombardemo.Utils.PostPicToYun;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
import com.google.gson.Gson;
import com.longsh.optionframelibrary.OptionBottomDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class FindSale extends AppCompatActivity implements View.OnClickListener {
    private ImageView photo_taken, back_sale;
    private EditText text_name, text_price, text_description,quality_sale,unit_sale;
    private TextView commit_sale, back_sale_text;
    private String TAG = "Test";
    //服务类型
    private int opType = 90003;
    private String url = "http://47.105.185.251:8081/Proj31/sale";

    //相机参数
    private static Uri imageUri;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;
    //照片存储
    File filePath ;

    //下拉框
    private Spinner spinner_sale;
    private static int spinner_position = 1;

    //进度条一
    Dialog progressDialog;
    private int imageClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.find_sale );

        //初始化并设置监听
        back_sale_text = (TextView) findViewById( R.id.back_sale_text );
        back_sale_text.setOnClickListener( this );

        photo_taken = (ImageView) findViewById( R.id.addopt_photoright );
        photo_taken.setOnClickListener( this );

        back_sale = (ImageView) findViewById( R.id.back_sale );
        back_sale.setOnClickListener( this );

        commit_sale = (TextView) findViewById( R.id.commit_sale );
        commit_sale.setOnClickListener( this );

        text_name = (EditText) findViewById( R.id.name_sale );
        text_price = (EditText) findViewById( R.id.price_sale );
        quality_sale = (EditText)findViewById( R.id.quality_sale );
        unit_sale = (EditText)findViewById( R.id.unit_sale );
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
            case R.id.back_sale_text:
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
                break;
            case R.id.commit_sale:
                commitSale(  );
                break;
        }
    }

    //通过URI获取图片并裁剪存入之前的地址
    private void startImageCrop(File saveToFile,Uri uri) {
        if(uri == null){
            return ;
        }
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        Log.i( TAG, "startImageCrop: " + "执行到裁剪图片了" + "uri is " + uri );
        intent.setDataAndType( uri, "image/*" );//设置Uri及类型
        //uri权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra( "crop", "true" );//
        intent.putExtra( "aspectX", 1 );//X方向上的比例
        intent.putExtra( "aspectY", 1 );//Y方向上的比例
        intent.putExtra( "outputX", 500 );//裁剪区的X方向宽
        intent.putExtra( "outputY", 500 );//裁剪区的Y方向宽
        intent.putExtra( "scale", true );//是否保留比例
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.JPEG.toString() );//图片的输出格式
        intent.putExtra( "return-data", false );//是否将数据保留在Bitmap中返回
        /*
        * return-data:是将结果保存在data中返回，在onActivityResult中，直接调用intent.getdata()就可以获取值了，这里设为fase，就是不让它保存在data中
        *MediaStore.EXTRA_OUTPUT：由于我们不让它保存在Intent的data域中，但我们总要有地方来保存我们的图片，这个参数就是转移保存地址的，对应Value中保存的URI就是指定的保存地址。
        */
        //判断文件是否存在
        if (!saveToFile.getParentFile().exists()) {
            saveToFile.getParentFile().mkdirs();
        }
        //将剪切后的图片存储到此文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveToFile));
        startActivityForResult( intent, CROP_IMAGE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //拍照成功的话，就调用BitmapFactory的decodeStream()方法把图片解析成Bitmap对象，然后显示
                    //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    Log.i( TAG, "onActivityResult TakePhoto : "+imageUri );
                    //设置照片存储文件及剪切图片
                    File saveFile = ImageUtils.getTempFile();
                    filePath = ImageUtils.getTempFile();
                    startImageCrop( saveFile,imageUri );
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //选中相册照片显示
                    try {
                        imageUri = data.getData(); //获取系统返回的照片的Uri
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
                    Bitmap bitmap = BitmapFactory.decodeFile( filePath.toString() );
                    //把裁剪后的图片展示出来
                    photo_taken.setImageBitmap( bitmap );
                    imageClick = 1;
                    //application全局变量
                    AppStr appStr = (AppStr)getApplication();
                    appStr.setState( false );
                    //图片上传腾讯云
                    Toast.makeText( FindSale.this, "即将上传商品图片至腾讯云！", Toast.LENGTH_SHORT ).show();
                    PostPicToYun.PostPic( FindSale.this,filePath ,"goods");
                }
                break;
            default:
                break;
        }
    }

    public void commitSale( ) {
        String goods_name = text_name.getText().toString().trim();
        String goods_price = text_price.getText().toString().trim();
        String quality = quality_sale.getText().toString().trim();
        String unit = unit_sale.getText().toString().trim();
        String goods_description = text_description.getText().toString().trim();

        //检验
        if(( goods_name == null|| goods_name.isEmpty())  || ( goods_price == null || goods_price.isEmpty()) || quality == null || quality.isEmpty() || unit == null || unit.isEmpty()){
            Toast.makeText( FindSale.this, "请填写完整的商品信息", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(!TestAndVerify.checkIllegal( goods_name )){
            Toast.makeText( FindSale.this, "商品名称禁止输入非法字符！", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(!TestAndVerify.checkMoney( goods_price )){
            Toast.makeText( FindSale.this, "商品价格格式不对！", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(!TestAndVerify.checkPlus( quality )){
            Toast.makeText( FindSale.this, "商品数量格式不对，请输入正整数！", Toast.LENGTH_SHORT ).show();
            return;
        }

        if(goods_description != null ){
            if(!TestAndVerify.checkIllegal( goods_description )){
                Toast.makeText( FindSale.this, "商品说明禁止输入非法字符！", Toast.LENGTH_SHORT ).show();
                return;
            }
        }

        if(filePath == null){
            Toast.makeText( this, "您还没有商品的照片呢，请给您的商品拍个照吧！", Toast.LENGTH_SHORT ).show();
            return;
        }
        
        //取出token和userId
        SharedPreferences sp = getSharedPreferences( "data", MODE_PRIVATE );
        String token = sp.getString( "token", "" );
        String uid = sp.getString( "uid","" );

        Log.i( TAG, "token is  " + token );
        Log.i( TAG, "commitSale: uid is "+uid );

        ItemGoods goods = new ItemGoods();
        //设置属性
        goods.setOpType( opType );
        goods.setGoodsType( String.valueOf( spinner_position ) );
        goods.setGoodsName( goods_name );
        goods.setPrice( Float.parseFloat( goods_price ) );
        goods.setUnit( unit );
        goods.setQuality( Integer.parseInt( quality ) );

        if(imageClick == 1){
            AppStr appStr = (AppStr)getApplication();
            if(appStr.isState() == true){
                String yunFlag = PostPicToYun.getYunFlag();
                Log.i( TAG, "PostToYun: "+yunFlag );
                String picUrl = PostPicToYun.getPicUrl();
                Log.i( TAG, "PostToYun: "+picUrl );
                if(yunFlag != null && yunFlag.equals( "OK" )){
                    if(picUrl != null || picUrl.length() > 20){
                        goods.setGoodsImg( picUrl );
                    }
                }else{
                    Toast.makeText( this, "图片上传失败！", Toast.LENGTH_SHORT ).show();
                    Log.i( TAG, "PostToYun: 上传失败！");
                    return;
                }
            }else{
                Toast.makeText( FindSale.this, "图片上传尚未成功，请稍作等待！", Toast.LENGTH_SHORT ).show();
                return;
            }
        }else{
            Toast.makeText( this, "您还没有商品的照片呢，请给您的商品拍个照吧！", Toast.LENGTH_SHORT ).show();
            return;
        }

        goods.setUserid( uid );
        goods.setToken( token );
        goods.setDescription( goods_description );
        //组成Json串
        Gson gson = new Gson();
        String goodsJsonStr = gson.toJson( goods, ItemGoods.class );
        Log.i( TAG, goodsJsonStr.toString() );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( FindSale.this,"正在发布商品" );
        progressDialog.show();
        PostWith postWith = new PostWith();
        postWith.sendPostWithOkhttp( url, goodsJsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss(progressDialog);
                        String errorData = TestAndVerify.judgeError( FindSale.this );
                        Toast.makeText( FindSale.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d( TAG, "获取数据成功了" );
                //获取后台返回结果
                final String responseData = response.body().string();
                Log.i( TAG, "后台返回的结果是："+responseData );
                Gson gson = new Gson();
                UserCO buyBack = gson.fromJson( responseData, UserCO.class );
                Log.i( TAG, buyBack.toString() );
                int flag = buyBack.getFlag();
                if (flag == 200) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss(progressDialog);
                            Toast.makeText( FindSale.this, "发布成功!", Toast.LENGTH_SHORT ).show();
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
                            Toast.makeText( FindSale.this, "商品发布失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}
