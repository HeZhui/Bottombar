package com.example.a15927.bottombardemo.functiontools;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;

public class LoadingDialog extends AlertDialog {

    private TextView tips_loading_msg;
    private int layoutResId;
    private String message = null;


    public LoadingDialog(@NonNull Context context, @StyleRes int layoutResId) {
        super( context, layoutResId );
        this.layoutResId = layoutResId;
        message = "加载中... ";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( layoutResId );
        tips_loading_msg = (TextView) findViewById( R.id.tips_loading_msg);
        tips_loading_msg.setText(this.message);
    }
}
