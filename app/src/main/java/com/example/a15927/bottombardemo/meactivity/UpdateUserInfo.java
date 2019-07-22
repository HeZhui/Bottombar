package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.AppStr;
import com.example.a15927.bottombardemo.Utils.ImageUtils;
import com.example.a15927.bottombardemo.Utils.PostPicToYun;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserCO;
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

public class UpdateUserInfo extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";

    //声明对象
    private ImageView update_image,update_bk;
    private EditText update_username,update_sex,update_phone,update_QQ,update_weixin,update_ps;
    private TextView update_back;
    private Button update_ok;

    ///拍照功能参数
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;

    //imageUri照片真实路径
    private Uri imageUri;
    //照片存储
    File filePath;

    private int op_update = 90007;  //更新
    private String url = "http://47.105.185.251:8081/Proj31/user";

    //进度条一
    Dialog progressDialog;
    //对象地地址
    private String picDir;
    //重置头像标志
    private int statusPic = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.update_user_info );

        //初始化
        initView();

        Intent intent = getIntent();
        UserVO uservo = (UserVO)intent.getSerializableExtra( "userInf" );
        if(uservo != null) {
            picDir = uservo.getPicDir();
            showHint( uservo );
        }
    }

    //回显用户信息
    private void showHint(UserVO uservo) {
        Glide.with( UpdateUserInfo.this ).load( uservo.getPicDir() ).centerCrop().error( R.drawable.kimg ).into( update_image );
        update_username.setText( uservo.getUname() );
        if(uservo.getSex() == 1){
            update_sex.setText( "男" );
        }else{
            update_sex.setText( "女" );
        }
        update_phone.setText( uservo.getUphone() );
        update_QQ.setText( uservo.getQq() );
        update_weixin.setText( uservo.getWeixin() );
        update_ps.setText( uservo.getPs() );
    }

    //绑定控件
    private void initView() {
        update_image = (ImageView)findViewById( R.id.update_image );
        update_image.setOnClickListener( this );
        update_bk = (ImageView)findViewById( R.id.update_bk );
        update_bk.setOnClickListener( this );
        update_back = (TextView) findViewById( R.id.update_back );
        update_back.setOnClickListener( this );
        update_username = (EditText)findViewById( R.id.update_username );
        update_sex = (EditText)findViewById( R.id.update_sex );
        update_phone = (EditText)findViewById( R.id.update_phone );
        update_QQ = (EditText)findViewById( R.id.update_QQ );
        update_weixin = (EditText)findViewById( R.id.update_weixin );
        update_ps = (EditText)findViewById( R.id.update_ps );
        update_ok = (Button)findViewById( R.id.update_ok );
        update_ok.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_image:
                changeImage();
                break;
            case R.id.update_bk:
                finish();
                break;
            case R.id.update_back:
                finish();
                break;
            case R.id.update_ok:
                if (statusPic == 1) {
                    String yunFlag = PostPicToYun.getYunFlag();
                    Log.i( "Test", "onClick: "+yunFlag );
                    String picUrl = PostPicToYun.getPicUrl();
                    Log.i( "Test", "onClick: "+picUrl );
                    if(yunFlag != null && yunFlag.equals( "OK" )) {
                        if (picUrl != null || picUrl.length() > 20) {
                            commitChange( picUrl );
                        }
                    }else{
                        Toast.makeText( this, "图片上传失败！", Toast.LENGTH_SHORT ).show();
                        Log.i( "Test", "onClick: 上传失败！");
                    }
                }
                if (statusPic == 0) {
                    commitChange( picDir );
                }
                break;
        }
    }

    private void commitChange(String picUrl) {
        String username = update_username.getText().toString().trim();
        Log.i( TAG, "commitChange: username is "+username );
        String sex = update_sex.getText().toString().trim();
        String phone = update_phone.getText().toString().trim();
        String QQ = update_QQ.getText().toString().trim();
        String weixin = update_weixin.getText().toString().trim();
        String ps = update_ps.getText().toString().trim();

        //从sp中取出token
        SharedPreferences User = getSharedPreferences( "data", Context.MODE_PRIVATE );
        String token = User.getString( "token","" );

        UserVO userVO = new UserVO();
        userVO.setOpType( op_update );
        userVO.setToken( token );
        userVO.setPicDir( picUrl );
        userVO.setUname( username );
        if(sex != null){
            if(sex.contains( "男" )){
                userVO.setSex( 1 );
            }else{
                userVO.setSex( 0 );
            }
        }
        if(TestAndVerify.checkPhone( phone )){
            userVO.setUphone( phone );
        }else {
            Toast.makeText( this, "手机号码格式不对！，请检查", Toast.LENGTH_SHORT ).show();
        }
        if(TestAndVerify.checkQQ( QQ )){
            userVO.setQq( QQ );
        }else{
            Toast.makeText( this, "QQ号格式不对！请检查", Toast.LENGTH_SHORT ).show();
        }
        if(TestAndVerify.checkWeixin( weixin )){
            userVO.setWeixin( weixin );
        }else{
            Toast.makeText( this, "微信号格式不对！请检查", Toast.LENGTH_SHORT ).show();
        }
        if(ps != null && ps.length() >= 100){
            Toast.makeText( this, "个性签名长度过长！", Toast.LENGTH_SHORT ).show();
            return;
        }
        userVO.setPs( ps );
        Gson gson = new Gson();
        String reqJson = gson.toJson( userVO,UserVO.class );
        Log.i( TAG, "commitChange: reqJson is "+reqJson.toString() );
        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( UpdateUserInfo.this, "正在更新用户信息...." );
        progressDialog.show();
        postChanges(reqJson,username,ps,picUrl);
    }

    //提交请求
    private void postChanges(String reqJson, final String username, final String ps,final String picUrl) {
        PostWith.sendPostWithOkhttp( url, reqJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d( "Test", "获取数据失败了" + e.toString() );
                //切换为主线程
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        String errorData = TestAndVerify.judgeError( UpdateUserInfo.this );
                        Toast.makeText( UpdateUserInfo.this, errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d( "Test", "获取数据成功了" );
                //获取后台返回结果
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: 后台返回的数据是:"+responseData );
                Gson gson = new Gson();
                UserCO userCO = gson.fromJson( responseData,UserCO.class );
                int flag = userCO.getFlag();
                if(flag == 200 ){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            dismiss( progressDialog );
                            SharedPreferences.Editor editor = getSharedPreferences( "data", MODE_PRIVATE ).edit();
                            editor.putString( "uname", username );
                            editor.putString( "ps",ps );
                            editor.putString( "picDir",picUrl );
                            editor.commit();
                            Toast.makeText( UpdateUserInfo.this, "修改成功！", Toast.LENGTH_SHORT ).show();
                            AppStr appStr = (AppStr)getApplication();
                            appStr.setUpdateInf( true );
                            Intent intent = new Intent( UpdateUserInfo.this, UserInfor.class );
                            startActivity( intent );
                            finish();
                        }
                    } );
                }else if(flag == 30001){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            dismiss( progressDialog );
                            Toast.makeText( UpdateUserInfo.this, "修改失败！登录信息已过期，请重新登录账户。", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }else{
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            dismiss( progressDialog );
                            Toast.makeText( UpdateUserInfo.this, "修改失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }

    private void changeImage() {
        //底部弹框Dialog
        //获取拍照和手机相册的权利
        List<String> stringList = new ArrayList<>();
        stringList.add( "拍照" );
        stringList.add( "从相册选择" );
        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(UpdateUserInfo.this , stringList );
        optionBottomDialog.setItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //启动相机程序
                        //隐式Intent
                        Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                        imageUri = ImageUtils.getImageUri( UpdateUserInfo.this );
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
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.PNG.toString() );
        intent.putExtra( "return-data", false );//是否将数据保留在Bitmap中返回dataParcelable相应的Bitmap数据，防止造成OOM
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
                    //设置照片存储文件及剪切图片
                    File saveFile = ImageUtils.getTempFile();
                    filePath = ImageUtils.getTempFile();
                    startImageCrop( saveFile,imageUri );
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        imageUri = data.getData(); //获取系统返回的照片的Uri
                        Log.i( "Test", "onActivityResult: uriImage is " +imageUri );
                        //设置照片存储文件及剪切图片
                        File saveFile = ImageUtils.setTempFile( UpdateUserInfo.this );
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
                    //通过FileName拿到图片
                    Bitmap bitmap = BitmapFactory.decodeFile( filePath.toString() );
                    //把裁剪后的图片展示出来
                    update_image.setImageBitmap( bitmap );
                    //置换头像标志
                    statusPic = 1;
                    Toast.makeText( this, "图片即将上传腾讯云！", Toast.LENGTH_SHORT ).show();
                    //图片上传腾讯云
                    PostPicToYun.PostPic( UpdateUserInfo.this,filePath ,"reg");
                }
                break;
            default:
                break;
        }
    }
}
