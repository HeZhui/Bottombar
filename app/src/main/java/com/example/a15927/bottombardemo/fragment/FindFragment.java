package com.example.a15927.bottombardemo.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.AppStr;
import com.example.a15927.bottombardemo.Utils.TestAndVerify;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.adapter.ShopAdapter;
import com.example.a15927.bottombardemo.dialog.DialogUIUtils;
import com.example.a15927.bottombardemo.findactivity.FindBuy;
import com.example.a15927.bottombardemo.findactivity.FindSale;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserBuy;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.a15927.bottombardemo.dialog.DialogUIUtils.dismiss;

public class FindFragment extends Fragment implements View.OnClickListener {
    private String TAG = "Test";
    //定义控件对象
    private TextView sale_button;
    private TextView buy_button;
    private ImageView image_add;
    private RecyclerView recyclerView;
    private SpringView springView;
    private View netFailedLayout,find_nothing;
    //分页状态
    public int page = 1;
    //当前分页  1------加载，  2-----------刷新
    protected int checkType = 1;
    //每页数目
    public int pageSize = 5;
    //用来标记当前页面处于的状态   1---摊位  2---求购
    private int statue = 1;
    //由于记录上次的state状态
    private int statueLast = 1;
    //查询类型
    private int opTypeBuy = 90004;
    //token
    private String token;
    private boolean login;
    //访问网址
    private String urlBuy = "http://47.105.185.251:8081/Proj31/buy";
    //进度条一
    Dialog progressDialog;
    //进度条二
    //    private LoadingDialog dialog;
    //
    //    private Handler mHandler = new Handler() {
    //        public void dispatchMessage(android.os.Message msg) {
    //            if (dialog != null && dialog.isShowing()) {
    //                dialog.dismiss();
    //            }
    //        };
    //    };

    //发送请求得到的商品信息
    List<ItemGoods> GoodsList = new ArrayList<>();
    //存放（每次分页的商品加载在一起）所有商品信息的数组
    List<ItemGoods> newGoodsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_find, container, false );
        //初始化组件
        init( view );

        //进度框显示方法一
        progressDialog = DialogUIUtils.showLoadingDialog( getActivity(), "正在查询" );
        progressDialog.show();
        //在Fragment中需要依赖的Activity (getActivity()方法需要使用)
        SharedPreferences sp = getActivity().getSharedPreferences( "data", MODE_PRIVATE );
        token = sp.getString( "token", "" );
        login = sp.getBoolean( "login", false );
        Log.i( TAG, "token is  " + token );
        Log.i( TAG, "login is  " + login );
        //获取商品信息
        getData();

        springView.setHeader( new DefaultHeader( getActivity() ) );
        springView.setFooter( new DefaultFooter( getActivity() ) );
        springView.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                checkType = 2;  //刷新
                getData();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                page++;
                checkType = 1;  //加载
                Log.i( TAG, "onRefresh: page is " + page );
                getData();
                springView.onFinishFreshAndLoad();
            }
        } );
        return view;
    }


    //初始化绑定控件
    public void init(View view) {
        sale_button = (TextView) view.findViewById( R.id.sale );
        sale_button.setOnClickListener( this );
        buy_button = (TextView) view.findViewById( R.id.buy );
        buy_button.setOnClickListener( this );
        image_add = (ImageView) view.findViewById( R.id.image_add );
        image_add.setOnClickListener( this );
        //上拉加载下拉刷新
        recyclerView = (RecyclerView) view.findViewById( R.id.recycler_find );
        springView = (SpringView) view.findViewById( R.id.springView );
        netFailedLayout = view.findViewById( R.id.layout_net_failed_find );
        find_nothing = view.findViewById( R.id.find_nothing );
        //设置默认状态 ：摊位
        sale_button.setEnabled( false );
        sale_button.setTextColor( Color.parseColor( "#0895e7" ) );
    }

    @Override
    public void onClick(View view) {
        //清除状态
        sale_button.setEnabled( true );
        sale_button.setTextColor( Color.WHITE );
        buy_button.setEnabled( true );
        buy_button.setTextColor( Color.WHITE );
        switch (view.getId()) {
            case R.id.sale://摊位
                statue = 1;
                //设置所选状态
                sale_button.setEnabled( false );
                sale_button.setTextColor( Color.parseColor( "#0895e7" ) );
                //进度框显示方法一
                progressDialog = DialogUIUtils.showLoadingDialog( getActivity(), "正在查询" );
                progressDialog.show();
                getData();
                break;
            case R.id.buy://求购
                statue = 2;
                //设置所选状态
                buy_button.setEnabled( false );
                buy_button.setTextColor( Color.parseColor( "#0895e7" ) );
                //进度框显示方法一
                progressDialog = DialogUIUtils.showLoadingDialog( getActivity(), "正在查询" );
                progressDialog.show();
                //进度条显示方法二
                //                dialog = new LoadingDialog(getActivity(),R.layout.tips_load);
                //                //点击物理返回键是否可取消dialog
                //                dialog.setCancelable(true);
                //                //点击dialog之外 是否可取消
                //                dialog.setCanceledOnTouchOutside(true);
                //                //显示
                //                dialog.show();

                //在求购界面查询商品
                getData();
                break;
            case R.id.image_add:
                if (statue == 1) {
                    sale_button.setEnabled( false );
                    sale_button.setTextColor( Color.parseColor( "#0895e7" ) );
                } else if (statue == 2) {
                    buy_button.setEnabled( false );
                    buy_button.setTextColor( Color.parseColor( "#0895e7" ) );
                }
                if (login == true) {
                    if (statue == 1) {
                        Intent intent_sale = new Intent( getActivity(), FindSale.class );
                        startActivity( intent_sale );
                    } else {
                        //跳转到用户查询商品的界面
                        Intent intent_buy = new Intent( getActivity(), FindBuy.class );
                        startActivity( intent_buy );
                    }
                } else {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), "登录之后才能进入哦，请先登录账号！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
                break;
            default:
                break;
        }
    }

    public void getData() {
        //当statue的状态发生变化时
        if(statueLast != statue){
            page = 1;
            newGoodsList.clear();
            Log.i( TAG, "getData: 清空后的数组为" +newGoodsList);
        }
        final UserBuy userBuy = new UserBuy();
        userBuy.setOpType( opTypeBuy );
        userBuy.setToken( token );
        userBuy.setPageSize( pageSize );
        userBuy.setPage( page );
        userBuy.setCheckType( checkType );
        userBuy.setCondition( statue );
        Gson gson_buy = new Gson();
        String userBuyJson = gson_buy.toJson( userBuy, UserBuy.class );
        //记录上次的state状态
        statueLast = statue;
        //application全局变量
        AppStr appStr = (AppStr)getActivity().getApplication();
        appStr.setState( false );
        //发送OkHttp请求
        PostWith.sendPostWithOkhttp( urlBuy, userBuyJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i( TAG, "onFailure: " );
                //application全局变量
                AppStr appStr = (AppStr)getActivity().getApplication();
                appStr.setState( true );
                getActivity().runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        //取消进度框一
                        dismiss( progressDialog );
                        //显示布局net_failed
                        recyclerView.setVisibility( View.GONE );
                        netFailedLayout.setVisibility( View.VISIBLE );
                        find_nothing.setVisibility( View.GONE );
                        String errorData = TestAndVerify.judgeError( getActivity() );
                        Toast.makeText( getActivity(), errorData, Toast.LENGTH_SHORT ).show();
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d( "Test", "获取数据成功了" );
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " + responseData );
                //开始解析返回数据
                Gson gson = new Gson();
                //把属性给到对应的对象中
                Goods goods = gson.fromJson( responseData, Goods.class );
                int flag = goods.getFlag();
                Log.i( TAG, "flag " + flag );
                GoodsList = goods.getGoodsList();
                //application全局变量
                AppStr appStr = (AppStr)getActivity().getApplication();
                appStr.setState( true );
                if (flag == 200) {
                    if (GoodsList.size() == 0) {
                        if(page == 1){
                            getActivity().runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    recyclerView.setVisibility( View.GONE );
                                    netFailedLayout.setVisibility( View.GONE );
                                    find_nothing.setVisibility( View.VISIBLE );
                                    Toast.makeText( getActivity(), "暂时没有商品展示哦！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }else{
                            getActivity().runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    //取消进度框一
                                    dismiss( progressDialog );
                                    Toast.makeText( getActivity(), "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
                                }
                            } );
                        }
                    }
                    if (GoodsList.size() <= pageSize && GoodsList.size() != 0) {
                        for (int i = 0; i < GoodsList.size(); i++) {
                            boolean repeat = false;
                            for (int j = 0; j < newGoodsList.size(); j++) {
                                if (newGoodsList.get( j ).getGoodsID().equals( GoodsList.get( i ).getGoodsID() )) {
                                    repeat = true;
                                    break;
                                }
                            }
                            if (repeat == false) {
                                newGoodsList.add( GoodsList.get( i ) );
                            }
                        }
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                dismiss( progressDialog );
                                recyclerView.setVisibility( View.VISIBLE );
                                netFailedLayout.setVisibility( View.GONE );
                                find_nothing.setVisibility( View.GONE );
                                Log.i( TAG, "run: success" );
                                //设置布局方式，这里是线性布局
                                LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
                                recyclerView.setLayoutManager( layoutManager );
                                if (statue == 1) {
                                    Log.i( TAG, "run: "+newGoodsList.toString() );
                                    GoodsAdapter goodsAdapter = new GoodsAdapter( getActivity(), newGoodsList );
                                    recyclerView.setAdapter( goodsAdapter );
                                }
                                if (statue == 2) {
                                    Log.i( TAG, "run: 求购 "+newGoodsList.toString() );
                                    ShopAdapter shopAdapter = new ShopAdapter( getActivity(), newGoodsList );
                                    recyclerView.setAdapter( shopAdapter );
                                }
                                if(checkType == 1){
                                    Toast.makeText( getActivity(), "加载成功！", Toast.LENGTH_SHORT ).show();
                                }else{
                                    Toast.makeText( getActivity(), "刷新成功！", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } );
                    }
                } else if (flag == 30001) {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( getActivity(), "登录信息已失效，请重新登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //取消进度框一
                            dismiss( progressDialog );
                            Toast.makeText( getActivity(), "查询失败！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }
}


