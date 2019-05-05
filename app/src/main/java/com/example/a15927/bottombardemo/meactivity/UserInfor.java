package com.example.a15927.bottombardemo.meactivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.longsh.optionframelibrary.OptionBottomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UserInfor extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_login;//返回
    private TextView back,change_image;//返回与更换头像
    private ImageView userImage;//用户头像
    private TextView userId,userAccount,userSex,userPhone;//用户信息
    private EditText description;//个性说明

    //拍照功能参数
    //imageUri照片真实路径
    private Uri imageUri;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_infor );
        //初始化绑定控件
        initView();
    }

    private void initView() {
        back_login = (ImageView)findViewById( R.id.back_login );
        back_login.setOnClickListener( this );

        back = (TextView)findViewById( R.id.back );
        back.setOnClickListener( this );

        userImage = (ImageView)findViewById( R.id.user_image );
        userImage.setOnClickListener( this );

        change_image = (TextView)findViewById( R.id.change_image ) ;
        change_image.setOnClickListener( this );

        userId = (TextView)findViewById( R.id.id_text );
        userAccount = (TextView)findViewById( R.id.userNo );
        userPhone =(TextView)findViewById( R.id.mail_text );
        userSex = (TextView)findViewById( R.id.sex_Info );
        description = (EditText)findViewById( R.id.description_text );


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_login:
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.user_image:

                break;
            case R.id.change_image:
                changeImage();
                break;
            default:
                break;

        }

    }

    private void changeImage() {
        //底部弹框Dialog
        //获取拍照和手机相册的权利
        List<String> stringList = new ArrayList<>();
        stringList.add( "拍照" );
        stringList.add( "从相册选择" );
        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog( UserInfor.this, stringList );
        optionBottomDialog.setItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取消底部弹框
                //optionBottomDialog.dismiss();
                switch (position) {
                    case 0:
                        //测试使用，验证是否为position= 0
                        //Toast.makeText(RegisterIn.this,"position 0", Toast.LENGTH_SHORT ).show();

                        //创建File对象，用于存储拍照后的图片,并把图片命名为output_image.jpg
                        //存储在手机SD卡的应用关联缓存目录下，应用关联缓存目录是指SD卡中专门用于存放当前应用缓存数据的位置
                        //调用getExternalCacheDir()获取这个目录
                        File outputImage = new File( getExternalCacheDir(), "output_image.jpg" );
                        //                                try {
                        //                                    if (outputImage.exists()) {
                        //                                        outputImage.delete();
                        //                                    }
                        //                                    outputImage.createNewFile();
                        //                                } catch (IOException e) {
                        //                                    e.printStackTrace();
                        //                                }
                        //判断版本号
                        if (Build.VERSION.SDK_INT >= 24) {
                            //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
                            imageUri = FileProvider.getUriForFile( UserInfor.this, "com.example.a15927.bottombardemo.fileprovider", outputImage );
                        } else {
                            //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
                            imageUri = Uri.fromFile( outputImage );
                        }

                        //启动相机程序
                        //隐式Intent
                        Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                        //putExtra()指定图片的输出地址，填入之前获得的Uri对象
                        intent_photo.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
                        startActivityForResult( intent_photo, TAKE_PHOTO );
                        //底部弹框消失
                        optionBottomDialog.dismiss();
                        break;
                    case 1:
                        //测试使用，验证是否为position= 1
                        //Toast.makeText(RegisterIn.this,"position 1", Toast.LENGTH_SHORT ).show();

                        //动态获取权限-------WRITE_EXTERNAL_STORAGE--------
                        if (ContextCompat.checkSelfPermission( UserInfor.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions( UserInfor.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1 );
                        } else {
                            //打开相册
                            openAlbum();
                        }
                        // Intent intent_album = new Intent( "android.intent.action.GET_CONTENT" );
                        // intent_album.setType( "image/*" );
                        // startActivity( intent_album );
                        //底部弹框消失
                        optionBottomDialog.dismiss();
                        break;
                }
            }

        });
    }


    //打开相册
    private void openAlbum() {
        Intent intent_album = new Intent( "android.intent.action.GET_CONTENT" );
        intent_album.setType( "image/*" );
        //startActivity( intent_album );
        //需要返回给此活动一个消息，如果打开相册成功，则需要显示图片到活动中
        startActivityForResult( intent_album, CHOOSE_PHOTO );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                // openAlbum();
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText( UserInfor.this,"you denied the permission!",Toast.LENGTH_LONG ).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //打开相机成功
                    try {
                        //将拍摄的照片的照片显示出来
                        //需要对拍摄的照片进行处理编辑
                        //拍照成功的话，就调用BitmapFactory的decodeStream()方法把output_image.jpg解析成Bitmap对象，然后显示
                        //imageUri是拍摄的照片的真实路径
                        Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                        //打印出照片的路径
                        Log.i( "Test", "register imageUri is " + imageUri );
                        //存储Uri对象，即照片的真实路径
                        //SharedPreferences.Editor image = getSharedPreferences( "data", MODE_PRIVATE ).edit();
                        String uri = imageUri.toString();
                        //image.putString( "imageUri", uri );
                        Log.i( "Test", "onActivityResult: uri is " + uri );
                        //显示到指定位置
                        userImage.setImageBitmap( bitmap );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //打开相册
                    //openAlbum();
                    Log.i( "Test", "onActivityResult: 执行到打开相册了" );
                    handleImageOnKitKat( data );
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        Log.i( "Test", "onActivityResult: 执行到打开相册子程序了" );
        String imagePath = null;
        Uri uri = data.getData();
        Log.i( "Test", "onActivityResult: 执行到data.getData()了" );
        if (DocumentsContract.isDocumentUri( this, uri )) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId( uri );
            Log.i( "Test", " 执行到打开相册文件documents" );
            if ("com.android.providers.media.documents".equals( uri.getAuthority() )) {
                //解析出数字格式的id
                Log.i( "Test", "handleImageOnKitKat: documents " );
                String id = docId.split( ":" )[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection );
            } else if ("com.android.providers.downloads.documents".equals( uri.getAuthority() )) {
                Log.i( "Test", "onActivityResult: 执行到打开id了" );
                Uri contentUri = ContentUris.withAppendedId( Uri.parse( "ontent://downloads/public_downloads" ), Long.valueOf( docId ) );
                imagePath = getImagePath( contentUri, null );
            } else if ("content".equalsIgnoreCase( uri.getScheme() )) {
                Log.i( "Test", "onActivityResult: 执行到打开相册content了" );
                //如果是content类型的Uri,则通过普通方式处理
                imagePath = getImagePath( uri, null );
                Log.i( "Test", "handleImageOnKitKat: imagePath is " + imagePath );
            } else if ("file".equalsIgnoreCase( uri.getScheme() )) {
                //如果是file类型的Uri,直接获取图片路径即可
                Log.i( "Test", "onActivityResult: 执行到打开相册file了" );
                imagePath = uri.getPath();
            }
            Log.i( "Test", "onActivityResult: 执行到显示了" );
            displayImage( imagePath );
        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的路径
        Log.i( "Test", "onActivityResult: 执行到获取路径了" + externalContentUri + "       section is " + selection );
        Cursor cursor = getContentResolver().query( externalContentUri, null, selection, null, null );
        Log.i( "Test", "getImagePath: cursor is " + cursor );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i( "Test", "onActivityResult: 执行到cursor了" );
                path = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Media.DATA ) );
            }
            cursor.close();
        }
        Log.i( "Test", "onActivityResult: return path了" );
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Log.i( "Test", "onActivityResult: 即将显示了" );
            Bitmap bitmap = BitmapFactory.decodeFile( imagePath );
            userImage.setImageBitmap( bitmap );
        } else {
            Log.i( "Test", "onActivityResult: 执行到打不开相册了" );
            Toast.makeText( this, "failed to get image", Toast.LENGTH_SHORT ).show();
        }
    }

}

