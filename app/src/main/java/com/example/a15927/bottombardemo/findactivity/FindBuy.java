package com.example.a15927.bottombardemo.findactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.UserBuy;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FindBuy extends AppCompatActivity implements View.OnClickListener{

  //private EditText name_buy,phone_buy,description_buy;
    private TextView commit_buy,buying;
    private ImageView back_buy;
    private int opType = 90004;
    private  String url = "http://localhost:8081/Proj31/buy";//http://118.89.217.225:8080/Proj20/buy

    ListView lv_showGoods;
    List<ItemGoods> Goodslist = new ArrayList<ItemGoods>();

    private  int count = 5;
    private  int page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.find_buy );
        //ListView初始化
        lv_showGoods = (ListView) findViewById( R.id.lv_showGoods );

        //初始化并设置监听
        buying = (TextView) findViewById( R.id.buying );
        buying.setOnClickListener( this );
//        name_buy = (EditText)findViewById( R.id.name_buy );
//        phone_buy = (EditText)findViewById( R.id.phone_buy );
//        description_buy = (EditText)findViewById( R.id.description_buy );
        commit_buy = (TextView)findViewById( R.id.commit_buy );
        commit_buy.setOnClickListener( this );
        back_buy = (ImageView)findViewById( R.id.back_buy );
        back_buy.setOnClickListener( this );


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_buy:
                //返回上一级
                finish();
                break;
            case R.id.buying:
                finish();
                break;
            case R.id.commit_buy:
                require();
                break;
        }
    }

    public  void require(){
        SharedPreferences sp = getSharedPreferences( "data",MODE_PRIVATE );
        String uname = sp.getString( "uname","" );
        String token = sp.getString( "token","" );
        Log.i( "Test", "uname is  " +uname);
        Log.i( "Test", "token is  " +token );
        final UserBuy userBuy = new UserBuy();
        userBuy.setOpType( opType );
        userBuy.setToken( token );
        userBuy.setPageSize( count );
        page = 1;
        userBuy.setPage( page );
        Gson gson_buy = new Gson();
        String userBuyJson = gson_buy.toJson( userBuy,UserBuy.class );
        //发送Post
        //创建OkHttpClient对象。
        OkHttpClient client = new OkHttpClient();
        //请求体
        RequestBody requestBody = new FormBody.Builder()
                .add( "reqJson", userBuyJson)
                .build();
        //发送请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody )
                .build();
        //异步执行，获取返回结果
        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Test","获取数据失败了"+e.toString());
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( FindBuy.this,"网络不给力！",Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Test","获取数据成功了");
                //获取后台返回结果
                final String responseData = response.body().string();
                Log.i( "Test",responseData );
                int flag = 0;
                //解析Json数据
                Log.i( "Test", "开始解析" );
                Gson gson = new Gson();
                Goods goods = new Goods(  );
                goods = gson.fromJson( responseData,Goods.class );
                Log.i( "Test", "解析完毕" );
                flag = goods.getFlag();
                Log.i( "Test", "flag: " +flag);
                Log.i( "Test",goods.toString() );
//                List lv = goods.getGoodsList();
//                Log.i( "Test", "List lv: "+lv );
                Goodslist = goods.getGoodsList();
                Log.i( "Test", "cmGoodsList: "+Goodslist );

                //成功获取返回消息
                if(flag == 200){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Log.i( "Test", "run: 查询成功" );
                            Toast.makeText( FindBuy.this,"查询成功",Toast.LENGTH_SHORT ).show();
                            ArrayAdapter<ItemGoods> adapter = new GoodsAdapter( FindBuy.this,R.layout.goods_item,Goodslist );
                            lv_showGoods.setAdapter( adapter );
                        }
                    } );
                }
                else if(flag == 30001){
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( FindBuy.this,"登录信息已失效,请重新登录",Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
                else{
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( FindBuy.this,"出现错误！,登录失败" ,Toast.LENGTH_SHORT).show();
                        }
                    } );
                }
            }
        } );
    }

    public class GoodsAdapter extends ArrayAdapter<ItemGoods>{
            private int resourceId;
            public GoodsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ItemGoods> objects) {
                super( context, resource, objects );
                this.resourceId = resource;
            }


            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ItemGoods goods  = getItem( position );//获取当前的List的元素
                View view = LayoutInflater.from( getContext() ).inflate( resourceId,parent,false );
                //属性控件对应
                ImageView goods_image  = view.findViewById( R.id.goods_image );
                TextView goods_ID = view.findViewById( R.id.goodsID );
                TextView goods_name = view.findViewById( R.id.goods_name );
                TextView goods_price = view.findViewById( R.id.price );
                TextView goods_quality = view.findViewById( R.id.quality );
                TextView goods_unit = view.findViewById( R.id.unit );

                goods_ID.setText( goods.getGoodsID() );
                goods_name.setText( goods.getGoodsName() );
                goods_price.setText( ""+ goods.getPrice() );
                goods_quality.setText( ""+goods.getQuality() );
                goods_unit.setText( goods.getUnit());


                byte[] img = goods.getGoodsImg();
                Bitmap bitmap = BitmapFactory.decodeByteArray( img,0,img.length,null );
                goods_image.setImageBitmap( bitmap );
                return view;

            }
        }
}
