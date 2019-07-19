package com.example.a15927.bottombardemo.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2019/6/6.
 */

public class ImageUtils {
    private static String TAG = "Test";
    public static File tempFile;
    private static String name;

    public static String getName() {
        return name;
    }

    public static Bitmap uri2Bitmap(Context mContext, Uri uri) {
        InputStream in = null;
        try {
            in = mContext.getContentResolver().openInputStream(uri);
            //从输入流中获取到图片
            Bitmap bm = BitmapFactory.decodeStream(in);
            in.close();
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取图片的URI
    public static Uri getImageUri(Context content) {
        File file = setTempFile( content );
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
            Uri imageUri = FileProvider.getUriForFile( content, "com.example.a15927.bottombardemo.fileprovider", file );
            return imageUri;
        }else{
            //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
            Uri imageUri = Uri.fromFile( file );
            return imageUri;
        }
    }

    //为图片设置一个存储地址
    public static File setTempFile(Context content) {
        //自定义图片名称
        name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance( Locale.CHINA)) + ".png";
        Log.i( TAG, " name : "+name );
        //定义图片存放的位置
        tempFile = new File(content.getExternalCacheDir(),name);
        Log.i( TAG, " tempFile : "+tempFile );
        return tempFile;
    }

    //获取该图片的存储地址
    public static File getTempFile() {
        return tempFile;
    }

    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 60) { // 循环判断如果压缩后图片是否大于10kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            Log.i( "Test", "compressImage: "+options );
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 150f;// 这里设置高度为800f
        float ww = 150f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    //对bitmap图片进行压缩
    public static Bitmap Compress (Context content, Bitmap bm) {
        try {
            File tempFile = getTempFile(  );
            FileOutputStream os = new FileOutputStream( tempFile );
            //对图片进行压缩
            bm.compress( Bitmap.CompressFormat.PNG, 100, os );//100不压缩，表示压缩率为0
            os.flush();
            os.close();
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}