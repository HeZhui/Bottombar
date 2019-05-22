package com.example.a15927.bottombardemo.meactivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.MainActivity;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserBO;
import com.example.a15927.bottombardemo.functiontools.UserCO;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.a15927.bottombardemo.functiontools.DialogUIUtils.dismiss;


public class MeLogin extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_arrow;
    private CheckBox cb_mima;
    private EditText in_username, in_password;
    private Button meLogin, register;
    private TextView forgetpassword;
    private String url = "http://47.105.185.251:8081/Proj31/login";//http://192.168.2.134:8081/Proj20/login
    private int opType = 90002;
    private String TAG = "Test";

    //后台返回数据
    public static String responseData;

    //判断是否是登录过
    private boolean login = false;

    //进度条一
    Dialog progressDialog;
    //进度条二
    //private LoadingDialog dialog;

//    //掌握进度条二消失
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
        setContentView( R.layout.me_login );


        back_arrow = (ImageView) findViewById( R.id.arrow_back );
        back_arrow.setOnClickListener( this );

        cb_mima = (CheckBox) findViewById( R.id.cb_mima );
        cb_mima.setOnClickListener( this );

        in_username = (EditText) findViewById( R.id.in_username );
        in_password = (EditText) findViewById( R.id.in_password );

        meLogin = (Button) findViewById( R.id.login_in );
        meLogin.setOnClickListener( this );

        register = (Button) findViewById( R.id.register_in );
        register.setOnClickListener( this );

        forgetpassword = (TextView) findViewById( R.id.forget_to );
        forgetpassword.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.login_in:
                // 登录程序
                Login();
                break;
            case R.id.register_in:
                Intent intent_register = new Intent( MeLogin.this, RegisterIn.class );
                startActivity( intent_register );
                break;
            case R.id.forget_to:
                Intent intent_reset = new Intent( MeLogin.this, ResetPassword.class );
                startActivity( intent_reset );
                break;
            case R.id.cb_mima:

                break;
            default:
                break;

        }

        //        //定义一个保存数据的方法
        //        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE );
        //        if (sp.getBoolean("ISCHECK", false)) {
        //            in_username.setText(sp.getString("USERNAME", ""));
        //            in_password.setText(sp.getString("PASSWORD", ""));
        //            cb_mima.setChecked(true);
        //        }
        //
        //        // checkbox添加监听事件。
        //        cb_mima.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //                if (cb_mima.isChecked()) {
        //                    //sp.edit().putBoolean("ISCHECK", true).commit();
        //                    in_password.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
        //                } else {
        //                    //sp.edit().putBoolean("ISCHECK", false).commit();
        //                    in_password.setTransformationMethod( PasswordTransformationMethod.getInstance());
        //                }
        //            }
        //        });
    }

    //登录
    public void Login() {
        final String username = in_username.getText().toString().trim();
        final String password = in_password.getText().toString().trim();

        //检验是否输入非法字符
        checkName( username );

        //检查数据格式是否正确
        if (TextUtils.isEmpty( username ) | TextUtils.isEmpty( password )) {
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    Toast.makeText( MeLogin.this, "请检查并输入正确的用户名和密码", Toast.LENGTH_SHORT ).show();
                }
            } );

        } else {
            //创建实例对象
            UserBO userBO = new UserBO();
            //设置属性值
            userBO.setOpType( opType );
            userBO.setUname( username );
            userBO.setUpassword( password );
            //封装为json串
            Gson gson = new Gson();
            String userJsonStr = gson.toJson( userBO, UserBO.class );
            Log.i( TAG, "Login: userJsonStr  is " + userJsonStr );

            //进度框显示方法一
            progressDialog = DialogUIUtils.showLoadingDialog( MeLogin.this,"正在登录" );
            progressDialog.show();

            //progressDialog.setCancelable( true );

//            //进度条显示方法二
//            dialog = new LoadingDialog(MeLogin.this,R.layout.tips_load);
//            //点击物理返回键是否可取消dialog
//            dialog.setCancelable(true);
//            //点击dialog之外 是否可取消
//            dialog.setCanceledOnTouchOutside(true);
//            //显示
//            dialog.show();

            //发送请求
            //传递参数
            PostWith.sendPostWithOkhttp( url, userJsonStr, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //这里是子线程
                    Log.d( TAG, "获取数据失败了" + e.toString() );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss(progressDialog);
                            //取消进度条二
                            //mHandler.sendEmptyMessage(1);
                            Toast.makeText( MeLogin.this, "数据错误", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {//回调的方法执行在子线程。
                        Log.d( TAG, "获取数据成功了" );
                        responseData = response.body().string();
                        Log.i( TAG, "onResponse: responseData is " + responseData );
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //json转String
                                //UserCO userCO = new UserCO();
                                Gson g = new Gson();
                                UserCO userCO = g.fromJson( responseData, UserCO.class );
                                //Log.i( "Test", userCO.toString() );
                                int flag = userCO.getFlag();
                                //Log.i( "Test", String.valueOf( flag ) );
                                //String message = userCO.getMessage();
                                //Log.i( "Test", message);
                                String token = userCO.getToken();
                                //由于token为空时,因为log打印的消息不可为空，所以会造成崩溃
                                //Log.i( TAG, token);

                                if (flag == 200) {
                                    //登录成功标志
                                    login = true;
                                    Log.i( TAG, "login_run: login is "+login );
                                    //保存Token
                                    final SharedPreferences.Editor editor = getSharedPreferences( "data", MODE_PRIVATE ).edit();
                                    editor.putString( "uname", username );
                                    editor.putString( "token", token );
                                    //储存登录成功login标志
                                    editor.putBoolean( "login",login );
                                    editor.commit();
                                    runOnUiThread( new Runnable() {
                                        @Override
                                        public void run() {
                                            //取消进度框一
                                            dismiss(progressDialog);
                                            //取消进度条二
                                            //mHandler.sendEmptyMessage(1);

                                            Toast.makeText( MeLogin.this, "登录成功", Toast.LENGTH_SHORT ).show();
                                            Intent intent = new Intent( MeLogin.this, MainActivity.class );
                                            startActivity( intent );
                                        }
                                    } );
                                } else if (flag == 20005) {
                                    runOnUiThread( new Runnable() {
                                        @Override
                                        public void run() {
                                            //取消进度框一
                                            dismiss(progressDialog);
                                            //取消进度条二
                                            //mHandler.sendEmptyMessage(1);
                                            Toast.makeText( MeLogin.this, "用户名或密码不正确，登录失败！", Toast.LENGTH_SHORT ).show();
                                        }
                                    } );
                                } else {
                                    runOnUiThread( new Runnable() {
                                        @Override
                                        public void run() {
                                            //取消进度框一
                                            dismiss(progressDialog);
                                            //取消进度条二
                                            //mHandler.sendEmptyMessage(1);
                                            Toast.makeText( MeLogin.this, "登录失败！", Toast.LENGTH_SHORT ).show();
                                        }
                                    } );
                                }
                            }
                        } );
                    }
                }
            } );
        }
    }

    public void postDataLogin(final String uname, String url, String userJsonStr) {
        //异步请求
        //执行post操作
        //创建OkHttpClient的对象
        OkHttpClient client = new OkHttpClient();

        //数据类型为json格式
        //        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //        RequestBody requestBody = RequestBody.create(JSON, userJsonStr);
        RequestBody requestBody = new FormBody.Builder()
                .add( "reqJson", userJsonStr )
                .build();

        Request request = new Request.Builder()
                .url( url )
                .post( requestBody )
                .build();

        client.newCall( request ).enqueue( new Callback() {//enqueue方法是异步请求
            @Override
            public void onFailure(Call call, IOException e) {
                //这里是子线程
                Log.d( TAG, "获取数据失败了" + e.toString() );
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( MeLogin.this, "数据错误", Toast.LENGTH_SHORT ).show();
                    }
                } );

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d( TAG, "获取数据成功了" );
                    //Log.d("Test","response.code()=="+response.code());

                    final String str = response.body().string();
                    Log.d( TAG, "response.body().string()==" + str );

                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //json转String
                            UserCO userCO = new UserCO();
                            Gson g = new Gson();
                            userCO = g.fromJson( str, UserCO.class );
                            //Log.i( "Test", userCO.toString() );
                            int flag = userCO.getFlag();
                            //Log.i( "Test", String.valueOf( flag ) );
                            //String message = userCO.getMessage();
                            //Log.i( "Test", message);
                            String token = userCO.getToken();
                            //由于token为空时,因为log打印的消息不可为空，所以会造成崩溃
                            //Log.i( TAG, token);

                            if (flag == 200) {
                                //保存Token
                                SharedPreferences.Editor editor = getSharedPreferences( "data", MODE_PRIVATE ).edit();
                                editor.putString( "uname", uname );
                                editor.putString( "token", token );
                                editor.apply();
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText( MeLogin.this, "登录成功", Toast.LENGTH_SHORT ).show();
                                    }
                                } );
                            } else if (flag == 20005) {
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText( MeLogin.this, "用户名或密码不正确，登录失败！", Toast.LENGTH_SHORT ).show();
                                    }
                                } );
                            } else {
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText( MeLogin.this, "当登录失败！", Toast.LENGTH_SHORT ).show();
                                    }
                                } );
                            }
                        }
                    } );
                }
            }
        } );

    }

    private void showResponse(final String msg) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( MeLogin.this, msg, Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    //是否输入非法字符
    public void checkName(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( str );
        if (m.find()) {
            Toast.makeText( MeLogin.this, "禁止输入非法符号！", Toast.LENGTH_LONG ).show();
        }
    }


    //    private String checkDataValid(String account,String pwd){
    //        if(TextUtils.isEmpty(account) | TextUtils.isEmpty(pwd)|account.trim().equals( "" ))
    //            return "用户名或密码不能为空";
    //        if(account.length() != 11 && !account.contains("@"))
    //            return "用户名不是有效的登录账号";
    //        return "";
    //    }

    //    //过滤特殊字符
    //    public static String stringFilter(String str) throws PatternSyntaxException {
    //        String regEx = "[/\\:*?<>|\"\n\t ]";
    //        Pattern p = Pattern.compile(regEx);
    //        Matcher m = p.matcher(str);
    //        return m.replaceAll("").trim();
    //    }


    //    public void TextWatch(final EditText in){
    //
    //      in.addTextChangedListener( new TextWatcher() {
    //                    private int cou = 0;
    //                    int selectionEnd = 0;
    //
    //                    @Override
    //                    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
    //                        cou = before + count;
    //                        String editable = in.getText().toString();
    //                        String str = stringFilter( editable );//过滤特殊字符
    //                        if(!editable.equals( "" )){
    //                            in.setText( str );
    //                        }
    //                        in.setSelection( in.length() );
    //                        cou = in.length();
    //                    }
    //
    //                    @Override
    //                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    //
    //                    }
    //
    //                    @Override
    //                    public void afterTextChanged(Editable editable) {
    //                        if(cou > MaxLength){
    //                            selectionEnd = in.getSelectionEnd();
    //                            editable.delete(MaxLength,selectionEnd);
    //                        }
    //                    }
    //                } );
    //      }


}

