package com.example.a15927.bottombardemo.findactivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.functiontools.ItemGoods;

import java.util.List;

/**
 * Created by Administrator on 2019/4/4.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{
    private List<ItemGoods> mgoodsList;

    //内部类ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goods_img;
        TextView goodsID;
        TextView goodsName;
        TextView goods_price;
        TextView goods_quality;
        TextView goods_unit;

        //获取布局中的实例，  这里的view是指RecyclerView的子项的最外层布局
        public ViewHolder(View view) {
            super( view );
            goods_img = (ImageView)view.findViewById( R.id.goods_image );
            goodsID = (TextView) view.findViewById( R.id.goodsID );
            goodsName = (TextView)view.findViewById( R.id.goods_name );
            goods_price = (TextView)view.findViewById( R.id.price );
            goods_quality = (TextView)view.findViewById( R.id.quality );
            goods_unit = (TextView)view.findViewById( R.id.unit );
        }
    }

    //构造函数
    //这个方法用于把眼展示的数据源传进来，并赋值给一份全局变量mgoodsList,后续的操作都基于这个数据源
    public GoodsAdapter(List<ItemGoods> goodsList) {
        mgoodsList = goodsList;
    }

    //由于继承自RecyclerView.Adapter，需要重写下面的三个方法
    @Override
    //此方法用于创建ViewHolder的实例
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //动态加载布局 ，   首先将goods_item布局加载进来，
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.goods_item,parent,false );
        //再创建ViewHolder的实例，并将加载的布局传入到构造函数中，
        ViewHolder holder = new ViewHolder( view );
        //最后将ViewHolder的实例返回
        return holder;
    }

    @Override
    //此方法适用于对RecyclerView子项的数据进行赋值的，会在每个子项被滚到屏幕内的时候执行
    public void onBindViewHolder(ViewHolder holder, int position) {
        //通过position参数得到当前的子项实例
        ItemGoods goods = mgoodsList.get( position );
        //设置数据
        byte[] img = goods.getGoodsImg();
        if(img.length == 0){
            holder.goods_img.setImageResource( R.drawable.kimg );
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray( img,0,img.length,null );
            holder.goods_img.setImageBitmap( bitmap );
        }
        holder.goodsID.setText( goods.getGoodsID() );
        holder.goodsName.setText( goods.getGoodsName() );
        holder.goods_price.setText( ""+goods.getPrice() );
        holder.goods_quality.setText( ""+ goods.getQuality() );
        holder.goods_unit.setText( goods.getUnit() );
    }

    @Override
    //得到RecyclerView子项的数目
    public int getItemCount() {
        return mgoodsList.size();
    }

}
