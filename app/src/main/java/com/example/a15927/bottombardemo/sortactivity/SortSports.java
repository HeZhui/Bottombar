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

import com.example.a15927.bottombardemo.Utils.GetListsUtils;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

import java.util.ArrayList;
import java.util.List;

import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class SortSports extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";

    private RecyclerView recycler_sports;
    private View netFailed;
    private ImageView arrow_back0;

    private int QueryType = 1;//代表按照商品类别查询
    private String goodsType = "体育用品";


    private int flag;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private boolean isSuccess;
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sort_sports );

        arrow_back0 = (ImageView) findViewById( R.id.arrow_back0 );
        arrow_back0.setOnClickListener( this );
        recycler_sports = (RecyclerView) findViewById( R.id.recycler_sports );
        netFailed = findViewById( R.id.layout_net_failed6 );

        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( SortSports.this, "正在查询..." );
        progressDialog.show();
        //调用方法获取返回结果
        //goodsList = GetListsUtils.getLists( SortSports.this, goodsType, QueryType );
        GetListsUtils.getLists( SortSports.this, goodsType, QueryType );
        goodsList = GetListsUtils.getGoodsList();
        Log.i( TAG, "onCreate: goodsList is " + goodsList + " " );
        flag = GetListsUtils.getFlag();
        Log.i( TAG, "onCreate: flag is " + flag );
        isSuccess = GetListsUtils.isSuccess();
        Log.i( TAG, "onCreate: isSuccess is " + isSuccess );

        if (isSuccess == true) {
            if (goodsList.size() == 0) {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( SortSports.this, "查询结果为空哦！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            } else {
                if (flag == 200) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "run: 查询成功！" );
                            Toast.makeText( SortSports.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                            //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                            LinearLayoutManager layoutManager = new LinearLayoutManager( SortSports.this );
                            recycler_sports.setLayoutManager( layoutManager );
                            GoodsAdapter adapter = new GoodsAdapter(SortSports.this, goodsList );
                            recycler_sports.setAdapter( adapter );
                        }
                    } );
                } else if (flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( SortSports.this, "登录信息无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( SortSports.this, "查询失败", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        }

        if (isSuccess == false) {
            recycler_sports.setVisibility( View.GONE );
            netFailed.setVisibility( View.VISIBLE );
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    //取消进度框
                    dismiss( progressDialog );
                    Toast.makeText( SortSports.this, "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }

}
