package com.example.a15927.bottombardemo.sortactivity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.MyTools.GetListsUtils;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.findactivity.GoodsAdapter;
import com.example.a15927.bottombardemo.functiontools.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

import java.util.ArrayList;
import java.util.List;

import static com.example.a15927.bottombardemo.functiontools.DialogUIUtils.dismiss;

public class SortMakeup extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "Test";

    private RecyclerView recycler_make_up;
    private View netFailed;
    private ImageView arrow_back1;

    private int QueryType = 1;//代表按照商品类别查询
    private String goodsType = "日常用品";


    private int flag;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private boolean isSuccess;
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sort_makeup );

        arrow_back1 = (ImageView) findViewById( R.id.arrow_back1);
        arrow_back1.setOnClickListener( this );
        recycler_make_up = (RecyclerView)findViewById( R.id.recycler_make_up );
        netFailed = findViewById( R.id.layout_net_failed5 );

        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( SortMakeup.this, "正在查询..." );
        progressDialog.show();
        //调用方法获取返回结果
        goodsList = GetListsUtils.getLists( SortMakeup.this, goodsType, QueryType );
        //goodsList = GetListsUtils.getGoodsList();
        Log.i( TAG, "onCreate: goodsList is " + goodsList + " " );
        flag = GetListsUtils.getFlag();
        Log.i( TAG, "onCreate: flag is " + flag );
        isSuccess = GetListsUtils.isSuccess();
        Log.i( TAG, "onCreate: isSuccess is " + isSuccess );

        if (isSuccess == true) {
            if (goodsList.size() != 0) {
                if (flag == 200) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "run: 查询成功！" );
                            Toast.makeText( SortMakeup.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                            //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                            LinearLayoutManager layoutManager = new LinearLayoutManager( SortMakeup.this );
                            recycler_make_up.setLayoutManager( layoutManager );
                            GoodsAdapter adapter = new GoodsAdapter( goodsList );
                            recycler_make_up.setAdapter( adapter );
                        }
                    } );
                } else if (flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( SortMakeup.this, "登录信息无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( SortMakeup.this, "查询失败", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            } else {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( SortMakeup.this, "查询结果为空哦！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        }

        if (isSuccess == false) {
            recycler_make_up.setVisibility( View.GONE );
            netFailed.setVisibility( View.VISIBLE );
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    //取消进度框
                    dismiss( progressDialog );
                    Toast.makeText( SortMakeup.this, "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    }

    @Override
    public void onClick(View view){
        finish();
    }
}
