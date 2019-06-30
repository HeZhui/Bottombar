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

public class SortElectric extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Test";

    private RecyclerView recycler_electric;
    private View netFailed;
    private ImageView arrow_back2;

    private int QueryType = 1;//代表按照商品类别查询
    private String goodsType = "电器";


    private int flag,statue;
    private List<ItemGoods> goodsList = new ArrayList<>();
    private boolean isSuccess;
    //进度条一
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sort_electric );

        arrow_back2 = (ImageView) findViewById( R.id.arrow_back2 );
        arrow_back2.setOnClickListener( this );
        recycler_electric = (RecyclerView) findViewById( R.id.recycler_electric );
        netFailed = findViewById( R.id.layout_net_failed4 );


        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( SortElectric.this, "正在查询..." );
        progressDialog.show();
        //调用方法获取返回结果
        //goodsList = GetListsUtils.getLists( SortElectric.this, goodsType, QueryType );
        GetListsUtils.getLists( SortElectric.this, goodsType, QueryType );

        for (int i = 0;i<100000;i++){
            ;
        }

        goodsList = GetListsUtils.getGoodsList();
        Log.i( TAG, "onCreate: goodsList is " + goodsList + " " );
        flag = GetListsUtils.getFlag();
        Log.i( TAG, "onCreate: flag is " + flag );
        isSuccess = GetListsUtils.isSuccess();
        Log.i( TAG, "onCreate: isSuccess is " + isSuccess );
        statue = GetListsUtils.getStatus();
        Log.i( TAG, "onCreate: statue is "+statue );

        if (isSuccess == true) {
            if (goodsList.size() != 0) {
                if (flag == 200) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Log.i( TAG, "run: 查询成功！" );
                            Toast.makeText( SortElectric.this, "查询成功！", Toast.LENGTH_SHORT ).show();
                            //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                            LinearLayoutManager layoutManager = new LinearLayoutManager( SortElectric.this );
                            recycler_electric.setLayoutManager( layoutManager );
                            GoodsAdapter adapter = new GoodsAdapter( SortElectric.this,goodsList );
                            recycler_electric.setAdapter( adapter );
                        }
                    } );
                } else if (flag == 30001) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( SortElectric.this, "登录信息无效，请重新登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( SortElectric.this, "查询失败", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            } else {
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( SortElectric.this, "查询结果为空哦！", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        }

        if (isSuccess == false) {
            recycler_electric.setVisibility( View.GONE );
            netFailed.setVisibility( View.VISIBLE );
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    //取消进度框
                    dismiss( progressDialog );
                    Toast.makeText( SortElectric.this, "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
