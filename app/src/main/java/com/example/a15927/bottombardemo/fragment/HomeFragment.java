package com.example.a15927.bottombardemo.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.Utils.AppStr;
import com.example.a15927.bottombardemo.adapter.GoodsAdapter;
import com.example.a15927.bottombardemo.functiontools.Goods;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;
import com.example.a15927.bottombardemo.functiontools.PostWith;
import com.example.a15927.bottombardemo.functiontools.UserBuy;
import com.example.a15927.bottombardemo.homeactivity.HomeSearch;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements OnBannerListener {
    private String TAG = "Test";
    //轮播图
    private Banner banner;
    private ArrayList<String> list_path;
//    private ArrayList<String> list_title;
    private ImageView search_button;
    private RecyclerView recycler_home;
    private List<ItemGoods> goodsList = new ArrayList<>(  );
    private List<ItemGoods> moreGoodsList = new ArrayList<>(  );
    private int opTypebuy  = 90004;
    private  String urlbuy = "http://47.105.185.251:8081/Proj31/buy";
    private SpringView springView_home;
    private View netFailedLayout;
    //分页状态
    public int page = 1;
    //当前分页  1------加载，  2-----------刷新
    protected int checkType = 1;
    //每页数目
    public int pageSize = 5;
    private String token;
    private int statue = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //在Fragment中需要依赖的Activity (getActivity()方法需要使用)
        SharedPreferences sp = getActivity().getSharedPreferences( "data",MODE_PRIVATE );
        String uname = sp.getString( "uname","" );
        token = sp.getString( "token","" );
        Log.i( "Test", "uname is  " +uname);
        Log.i( "Test", "token is  " +token );

        banner = (Banner) view.findViewById(R.id.banner);
        netFailedLayout = view.findViewById( R.id.layout_net_failed );
        recycler_home = (RecyclerView) view.findViewById( R.id.recycler_home );
        springView_home = (SpringView) view.findViewById( R.id.springView_home );
        search_button = (ImageView)view.findViewById(R.id.image_search) ;
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入搜索界面
                switch (view.getId()){
                    case R.id.image_search :
                        Intent intent_search = new Intent(getActivity(), HomeSearch.class);
                        startActivity(intent_search);
                        break;
                    default:
                        break;
                }

            }
        });
        initGoods();
        springView_home.setHeader( new DefaultHeader( getActivity() ) );
        springView_home.setFooter( new DefaultFooter( getActivity() ) );
        springView_home.setListener( new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                checkType = 2;
                initGoods();
                springView_home.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                page++;
                checkType = 1;
                Log.i( TAG, "onRefresh: page is " + page );
                initGoods();
                springView_home.onFinishFreshAndLoad();
            }
        } );
        initView();
        return view;
    }

    private void initGoods() {
        UserBuy userBuy = new UserBuy();
        userBuy.setOpType( opTypebuy );
        userBuy.setToken( token );
        userBuy.setPageSize( pageSize );
        userBuy.setPage( page );
        userBuy.setCheckType( checkType );
        userBuy.setCondition( statue );
        Gson gson_buy = new Gson();
        String userBuyJson = gson_buy.toJson( userBuy,UserBuy.class );
        //application全局变量
        AppStr appStr = (AppStr)getActivity().getApplication();
        appStr.setState( false );
        //发送Post
        PostWith.sendPostWithOkhttp( urlbuy, userBuyJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i( TAG, "onFailure: " );
                AppStr appStr = (AppStr)getActivity().getApplication();
                appStr.setState( true );
                getActivity().runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        recycler_home.setVisibility( View.GONE );
                        Toast.makeText( getActivity(), "当前网络不给力哦！", Toast.LENGTH_SHORT ).show();
                        netFailedLayout.setVisibility( View.VISIBLE );
                    }
                } );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Test","获取数据成功了");
                String responseData = response.body().string();
                Log.i( TAG, "onResponse: " +responseData);
                //开始解析返回数据
                Log.i( TAG, "开始解析数据" );
                Gson gson = new Gson();
                //把属性给到对应的对象中
                Goods goods = gson.fromJson( responseData,Goods.class );
                Log.i( TAG, "解析数据完毕" );
                int flag = goods.getFlag();
                Log.i( TAG, "flag " +flag);
                goodsList = goods.getGoodsList();
                AppStr appStr = (AppStr)getActivity().getApplication();
                appStr.setState( true );
                //flag判断
                if (flag == 200) {
                    if(goodsList.size() == 0){
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                //取消进度框一
                                //dismiss( progressDialog );
                                Toast.makeText( getActivity(), "没有更多的内容了！", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }
                    if(goodsList.size() <= pageSize && goodsList.size() != 0){
                        for (int i = 0; i < goodsList.size(); i++) {
                            boolean repeat = false;
                            for (int j = 0; j < moreGoodsList.size(); j++) {
                                if (moreGoodsList.get( j ).getGoodsID().equals( goodsList.get( i ).getGoodsID() )) {
                                    repeat = true;
                                    break;
                                }
                            }
                            if (repeat == false) {
                                moreGoodsList.add( goodsList.get( i ) );
                            }
                        }
                        //切换到主线程
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                Log.i( "Test", "run: 查询成功" );
                                Toast.makeText( getActivity(), "查询成功", Toast.LENGTH_SHORT ).show();
                                //LinearLayoutManager指定了recyclerView的布局方式，这里是线性布局
                                LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity() );
                                recycler_home.setLayoutManager( layoutManager );
                                GoodsAdapter adapter = new GoodsAdapter( getActivity(), moreGoodsList );
                                recycler_home.setAdapter( adapter );
                            }
                        } );
                    }
                } else if (flag == 30001) {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), "登录信息已失效,请再次登录！", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getActivity(), "出现错误，查询失败", Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
            }
        } );
    }


    private void initView() {
        //放图片地址的集合
        list_path = new ArrayList<>();
//        //放标题的集合
//        list_title = new ArrayList<>();

        list_path.add("https://pic-001-1259665619.cos.ap-chengdu.myqcloud.com/picDemo/20190719_032500.png");//http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg
        list_path.add("https://pic-001-1259665619.cos.ap-chengdu.myqcloud.com/picDemo/20190719_031014.png");//http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg
        list_path.add("https://pic-001-1259665619.cos.ap-chengdu.myqcloud.com/picDemo/20190719_030733.png");//http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
//        list_title.add("");
//        list_title.add("");
//        list_title.add("");
//        list_title.add("");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
//        //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
//                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
//        Log.i("tag", "你点了第"+position+"张轮播图");
    }

}