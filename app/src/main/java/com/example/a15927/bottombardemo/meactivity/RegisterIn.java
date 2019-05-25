package com.example.a15927.bottombardemo.meactivity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.MyTools.FileUtils;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
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


public class RegisterIn extends AppCompatActivity implements View.OnClickListener {
    private EditText input_username;
    private EditText input_password;
    private EditText password_confirm;
    private Button comit_register;
    private ImageView takephoto;
    private ImageView arrow_back;
    private TextView back;

    //拍照功能参数
    //imageUri照片真实路径
    private Uri imageUri;
    //private Uri cropImageUri;
    File outputImage;
//    File cropImage;
    private static String filePath = null;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    //访问的服务器网页
    private int opType = 90001;
    private String url = "http://47.105.185.251:8081/Proj31/register";//http://192.168.2.134:8081/Proj20/register

    //进度条一
    Dialog progressDialog;
    //进度条二
    //private LoadingDialog dialog;

    //    private Handler mHandler = new Handler() {
    //        public void dispatchMessage(android.os.Message msg) {
    //            if (dialog != null && dialog.isShowing()) {
    //                dialog.dismiss();
    //            }
    //        };
    //    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.register_in );

        //绑定控件并设置监听
        input_username = (EditText) findViewById( R.id.user_input );
        input_password = (EditText) findViewById( R.id.password_input );
        password_confirm = (EditText) findViewById( R.id.password_confirm );
        takephoto = (ImageView) findViewById( R.id.takephoto );
        takephoto.setOnClickListener( this );
        arrow_back = (ImageView) findViewById( R.id.arrow_back );
        arrow_back.setOnClickListener( this );
        back = (TextView) findViewById( R.id.back );
        back.setOnClickListener( this );
        comit_register = (Button) findViewById( R.id.register );
        comit_register.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takephoto:
                //底部弹框Dialog
                //获取拍照和手机相册的权利
                List<String> stringList = new ArrayList<>();
                stringList.add( "拍照" );
                stringList.add( "从相册选择" );

                final OptionBottomDialog optionBottomDialog = new OptionBottomDialog( RegisterIn.this, stringList );
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
                                outputImage = new File( getExternalCacheDir(), "output_image.jpg" );
                                Log.i( "Test", "onItemClick:outputImage is "+outputImage );
                                //压缩后存储的文件位置
//                                cropImage = new File (getExternalCacheDir(),"crop_image.jpg");
//                                Log.i( "Test", "onItemClick:cropImage is "+cropImage );
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
                                    //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
                                    imageUri = FileProvider.getUriForFile( RegisterIn.this, "com.example.a15927.bottombardemo.fileprovider", outputImage );
                                    //cropImageUri = FileProvider.getUriForFile( RegisterIn.this,"com.example.a15927.bottombardemo.fileprovider",outputImage );
                                    Log.i( "Test", "onItemClick:------1--------- " );
                                } else {
                                    //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
                                    imageUri = Uri.fromFile( outputImage );
                                    //cropImageUri = Uri.fromFile( cropImage );
                                    Log.i( "Test", "onItemClick:------2--------- " );
                                }

                                //启动相机程序
                                //隐式Intent
                                Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                                //putExtra()指定图片的输出地址，填入之前获得的Uri对象
                                intent_photo.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
                                //startActivity( intent_photo );
                                startActivityForResult( intent_photo, TAKE_PHOTO );
                                //底部弹框消失
                                optionBottomDialog.dismiss();
                                break;
                            case 1:
                                //测试使用，验证是否为position= 1
                                //Toast.makeText(RegisterIn.this,"position 1", Toast.LENGTH_SHORT ).show();

                                //动态获取权限-------WRITE_EXTERNAL_STORAGE--------
                                if (ContextCompat.checkSelfPermission( RegisterIn.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions( RegisterIn.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1 );
                                } else {
                                    //打开相册
                                    openAlbum();
                                }
                                //底部弹框消失
                                optionBottomDialog.dismiss();
                                break;
                        }
                    }
                } );
                break;
            case R.id.register:
                allcomit();
                break;
            case R.id.arrow_back:
                finish();
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    //打开相册
    private void openAlbum() {
        Intent intent_album = new Intent( "android.intent.action.GET_CONTENT" );
        intent_album.setType( "image/*" );
        //startActivity( intent_album );
        //需要返回给此活动一个消息，如果打开相册成功，则需要显示图片到活动中
        startActivityForResult( intent_album, CHOOSE_PHOTO );
    }

    //获取权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText( RegisterIn.this, "you denied the permission!", Toast.LENGTH_LONG ).show();
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

                        //拍照成功的话，就调用BitmapFactory的decodeStream()方法把output_image.jpg解析成Bitmap对象，然后显示
                        //imageUri是拍摄的照片的真实路径
                        Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                        Log.i( "Test", "register imageUri is " + imageUri );
                        filePath = outputImage.toString();
                        //显示到指定位置
                        takephoto.setImageBitmap( bitmap );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //选中相册照片显示
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
        //Log.i( "Test", "执行到打开相册子程序了" );
        String imagePath = null;
        Uri uri = data.getData();
        //Log.i( "Test", " 执行到data.getData()了" );
        if (DocumentsContract.isDocumentUri( this, uri )) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId( uri );
            //Log.i( "Test", " 执行到打开相册文件documents" );
            if ("com.android.providers.media.documents".equals( uri.getAuthority() )) {
                //解析出数字格式的id
                //Log.i( "Test", "handleImageOnKitKat: selection " );
                String id = docId.split( ":" )[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection );
            } else if ("com.android.providers.downloads.documents".equals( uri.getAuthority() )) {
                //Log.i( "Test", "执行到documents了" );
                Uri contentUri = ContentUris.withAppendedId( Uri.parse( "ontent://downloads/public_downloads" ), Long.valueOf( docId ) );
                imagePath = getImagePath( contentUri, null );
            } else if ("content".equalsIgnoreCase( uri.getScheme() )) {
                //Log.i( "Test", "执行到content了" );
                //如果是content类型的Uri,则通过普通方式处理
                imagePath = getImagePath( uri, null );
                Log.i( "Test", "handleImageOnKitKat: imagePath is " + imagePath );
            } else if ("file".equalsIgnoreCase( uri.getScheme() )) {
                //如果是file类型的Uri,直接获取图片路径即可
                //Log.i( "Test", "执行到file了" );
                imagePath = uri.getPath();
            }
            Log.i( "Test", " 执行到显示了" );
            displayImage( imagePath );
            filePath = imagePath;
        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的路径
        Log.i( "Test", " 执行到获取路径了" + externalContentUri + "       section is " + selection );
        Cursor cursor = getContentResolver().query( externalContentUri, null, selection, null, null );
        Log.i( "Test", "getImagePath: cursor is " + cursor );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                //Log.i( "Test", "执行到cursor了" );
                path = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Media.DATA ) );
            }
            cursor.close();
        }
        //Log.i( "Test", " return path了" );
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Log.i( "Test", "onActivityResult: 即将显示了" );
            Bitmap bitmap = BitmapFactory.decodeFile( imagePath );
            takephoto.setImageBitmap( bitmap );
        } else {
            //Log.i( "Test", "onActivityResult: 执行到打不开相册了" );
            Toast.makeText( this, "failed to get image", Toast.LENGTH_SHORT ).show();
        }
    }

    //注册
    public void allcomit() {
        String inputUsername = input_username.getText().toString().trim();
        String inputpassword = input_password.getText().toString().trim();
        String confirmpassword = password_confirm.getText().toString().trim();

        //数据库写入，再弹出注册成功过的消息，跳入登录活动页面

        if (TextUtils.isEmpty( inputUsername )) {
            Toast.makeText( RegisterIn.this, "用户名不能为空", Toast.LENGTH_SHORT ).show();
        } else {
            if (inputpassword.equals( "" )) {
                Toast.makeText( RegisterIn.this, "密码不能为空", Toast.LENGTH_SHORT ).show();
            } else {
                if (confirmpassword.equals( "" )) {
                    Toast.makeText( RegisterIn.this, "第二次输入密码不能为空", Toast.LENGTH_SHORT ).show();
                } else {
                    if (inputpassword.equals( confirmpassword )) {
                        //进度框显示方法一
                        progressDialog = DialogUIUtils.showLoadingDialog( RegisterIn.this, "正在注册" );
                        progressDialog.show();

                        //                        //进度条显示方法二
                        //                        dialog = new LoadingDialog(RegisterIn.this,R.layout.tips_load);
                        //                        //点击物理返回键是否可取消dialog
                        //                        dialog.setCancelable(true);
                        //                        //点击dialog之外 是否可取消
                        //                        dialog.setCanceledOnTouchOutside(true);
                        //                        //显示
                        //                        dialog.show();

                        register( inputUsername, inputpassword );
                    } else
                        Toast.makeText( RegisterIn.this, "两次密码不一致", Toast.LENGTH_SHORT ).show();
                }
            }
        }
    }

    public void register(String uname, String upassword) {
        //存储照片路径
        SharedPreferences.Editor image = getSharedPreferences( "data", MODE_PRIVATE ).edit();
        image.putString( "filePath", filePath );
        Log.i( "Test", "filePath is " + filePath );
        image.commit();//提交
        //需要对拍摄的照片进行压缩处理
        //Error:无法访问BufferedImage 找不到java.awt.image.BufferedImage的类文件
//        try {
//            Thumbnails.of( imageUri.toString())
//                    .scale(1f)
//                    .outputQuality(0.5f)
//                    .toFile(cropImage.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
        Bitmap bitmap =BitmapFactory.decodeFile( filePath );
        //压缩图片
        Bitmap cropBitMap = FileUtils.zoomBitmap( bitmap, 240, 240 );

        //生成部分属性
        UserVO userVO = new UserVO();
        String uuid = UUID.randomUUID().toString().replaceAll( "-","" );
        //Resources res = getResources();
       // Bitmap bitmap = BitmapFactory.decodeResource( res, R.drawable.chen );//从drawable中取一个图片（以后大家需要从相册中取，或者相机中取）。

        //bitmp转bytes
        byte[] uimages = FileUtils.Bitmap2Bytes( cropBitMap );
        userVO.setOpType( opType );
        userVO.setUid( uuid );
        userVO.setuname( uname );
        userVO.setUpassword( upassword );
        userVO.setUimage( uimages );
        userVO.setUphone( "15927305629" );
        userVO.setSex( 1 );

        //String转json
        Gson gson = new Gson();
        String JsonStr = gson.toJson( userVO, UserVO.class );

        //Post请求
        PostWith.sendPostWithOkhttp( url, JsonStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //异常情况的逻辑
                Log.d( "Test", "获取数据失败了" + e.toString() );
                //切换为主线程
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        //取消进度条二
                        //mHandler.sendEmptyMessage(1);
                        Toast.makeText( RegisterIn.this, "数据错误", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d( "Test", "获取数据成功了" );
                    //获取后台返回结果
                    final String responseData = response.body().string();
                    //json转String
                    Gson g = new Gson();
                    UserCO userCO = g.fromJson( responseData, UserCO.class );
                    Log.i( "Test", userCO.toString() );
                    int flag = userCO.getFlag();
                    Log.i( "Test", String.valueOf( flag ) );
                    //                    String message = userCO.getMessage();
                    //                    Log.i( "Test", message );
                    //                    String token = userCO.getToken();
                    //当token无返回值时，为null,但是Log打印时message不可为空，故而出现此步崩溃
                    //Log.i( "Test", token );

                    if (flag == 200) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                //取消进度条二
                                //mHandler.sendEmptyMessage(1);
                                Toast.makeText( RegisterIn.this, "注册成功", Toast.LENGTH_SHORT ).show();
                                Intent intentTras = new Intent( RegisterIn.this, MeLogin.class );
                                startActivity( intentTras );
                            }
                        } );
                    } else if (flag == 10002) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                //取消进度条二
                                //mHandler.sendEmptyMessage(1);
                                Toast.makeText( RegisterIn.this, "该用户名已存在，注册失败！", Toast.LENGTH_SHORT ).show();
                            }
                        } );

                    } else {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                //取消进度条二
                                //mHandler.sendEmptyMessage(1);
                                Toast.makeText( RegisterIn.this, "注册失败", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                }
            }
        } );
    }
}
