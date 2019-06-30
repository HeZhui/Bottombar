package com.example.a15927.bottombardemo.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;

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

    public static File setTempFile(Context content) {
        //自定义图片名称
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance( Locale.CHINA)) + ".png";
        Log.i( TAG, " name : "+name );
        //定义图片存放的位置
        tempFile = new File(content.getExternalCacheDir(),name);
        Log.i( TAG, " tempFile : "+tempFile );
        return tempFile;
    }

    public static File getTempFile() {
        return tempFile;
    }

    public static Bitmap Compress (Context content, Bitmap bm) {
        try {
            File tempFile = getTempFile(  );
            FileOutputStream os = new FileOutputStream( tempFile );
            //对图片进行压缩
            bm.compress( Bitmap.CompressFormat.PNG, 100, os );
            os.flush();
            os.close();
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}