package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.Utils.FileUtils;
import com.example.a15927.bottombardemo.Utils.ImageUtils;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCheck;
import com.example.a15927.bottombardemo.functiontools.UserQuery;
import com.example.a15927.bottombardemo.functiontools.UserVO;
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

public class UserInfor extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";
    private ImageView back_login;//返回
    private TextView back,change_image;//返回与更换头像
    private ImageView userImage;//用户头像
    private TextView userId,userAccount,userSex,userPhone;//用户信息
    private EditText description;//个性说明

    private int op_query = 90006;    //查询
    private int op_update = 90007;  //更新
    private String url = "http://47.105.185.251:8081/Proj31/user";

    ///拍照功能参数
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;

    //imageUri照片真实路径
    private Uri imageUri;
    //照片存储
    File filePath;

    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.user_infor );
        //初始化绑定控件
        initView();
        //查询用户信息并显示
        //取出从登录界面存储token
        SharedPreferences User = getSharedPreferences( "data", Context.MODE_PRIVATE );
        //如果未找到该值，则使用get方法中传入的默认值false代替
        String token  = User.getString( "token", "" );
        //设置属性
        UserCheck userCheck = new UserCheck();
        userCheck.setOpType( op_query );
        userCheck.setToken( token );
        //封装Json串
        Gson gson = new Gson();
        String reqJson = gson.toJson( userCheck,UserCheck.class );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( UserInfor.this, "正在查询..." );
        progressDialog.show();
        //发送请求
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( TAG, "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        Toast.makeText( UserInfor.this, "网络不给力！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d( TAG, "获取数据成功了" );
                    final String responseData = response.body().string();
                    Log.i( TAG, "onResponse: responseData is " + responseData );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Gson g = new Gson();
                            UserQuery userQuery = g.fromJson( responseData,UserQuery.class );
                            int flag = userQuery.getFlag();
                            Log.i( TAG, "run: flag is "+flag );
                            UserVO userVO = userQuery.getUser();
                            Log.i( TAG, "run: userVO is "+ userQuery.getUser());
                            if(flag == 200){
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询成功" );
                                userId.setText( userVO.getUid() );
                                userAccount.setText( userVO.getUname() );
                                userPhone.setText( userVO.getUphone() );
                                if(userVO.getSex() == 1){
                                    userSex.setText( "女");
                                }else {
                                    userSex.setText( "男" );
                                }
                                Bitmap bitmap = FileUtils.Bytes2Bimap( userVO.getUimage() );
                                Bitmap zoomBit = FileUtils.zoomBitmap( bitmap,720,1080 );
                                userImage.setImageBitmap( zoomBit );
                            }
                            else if (flag == 30001){
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: token无效" );
                                Toast.makeText( UserInfor.this, "当前登录信息无效，请重新登录", Toast.LENGTH_SHORT ).show();
                            }
                            else{
                                //取消进度框一
                                dismiss( progressDialog );
                                Log.i( TAG, "run: 查询失败" );
                                Toast.makeText( UserInfor.this, "获取用户信息失败", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
                }
            }
        } );

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
                switch (position) {
                    case 0:
                        //启动相机程序
                        //隐式Intent
                        Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                        imageUri = ImageUtils.getImageUri( UserInfor.this );
                        //putExtra()指定图片的输出地址，填入之前获得的Uri对象
                        intent_photo.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
                        startActivityForResult( intent_photo, TAKE_PHOTO );
                        //底部弹框消失
                        optionBottomDialog.dismiss();
                        break;
                    case 1:
                        //打开相册
                        Intent intent_album = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                        //需要返回给此活动一个消息，如果打开相册成功，则需要显示图片到活动中
                        startActivityForResult( intent_album, CHOOSE_PHOTO );
                        //底部弹框消失
                        optionBottomDialog.dismiss();
                        break;
                }
            }
        });
    }

    //剪切图片
    private void startImageCrop(File saveToFile,Uri uri) {
        if(uri == null){
            return ;
        }
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        Log.i( "Test", "startImageCrop: " + "执行到压缩图片了" + "uri is " + uri );
        intent.setDataAndType( uri, "image/*" );//设置Uri及类型
        //uri权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra( "crop", "true" );//
        intent.putExtra( "aspectX", 1 );//X方向上的比例
        intent.putExtra( "aspectY", 1 );//Y方向上的比例
        intent.putExtra( "outputX", 60 );//裁剪区的X方向宽
        intent.putExtra( "outputY", 60 );//裁剪区的Y方向宽
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
        Log.i( "Test", "startImageCrop: " + "即将跳到剪切图片" );
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
                    Log.i( "Test", "onActivityResult TakePhoto : "+imageUri );
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
                        Log.i( "Test", "onActivityResult: uriImage is " +imageUri );
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        //设置照片存储文件及剪切图片
                        File saveFile = ImageUtils.setTempFile( UserInfor.this );
                        filePath = ImageUtils.getTempFile();
                        startImageCrop( saveFile,imageUri );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_IMAGE:
                if(resultCode == RESULT_OK){
                    Log.i( "Test", "onActivityResult: CROP_IMAGE" + "进入了CROP");
                    // 通过图片URI拿到剪切图片
                    //bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //通过FileName拿到图片
                    Bitmap bitmap = BitmapFactory.decodeFile( filePath.toString() );
                    //把裁剪后的图片展示出来
                    userImage.setImageBitmap( bitmap );
                    //ImageUtils.Compress( bitmap );
                }
                break;
            default:
                break;
        }
    }

}

