package com.example.a15927.bottombardemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a15927.bottombardemo.R;

/**
 * Created by Administrator on 2019/4/7.
 */

public class CustomProgressTransDialog {
    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate( R.layout.custom_dialog_progress_trans_layout, null);
        LinearLayout layout = (LinearLayout) v.findViewById( R.id.dialog_view );
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        //物理键返回
        //若为true，点击ProgressDialog以外的区域的时候ProgressDialog就会关闭
        loadingDialog.setCancelable( true );
        loadingDialog.setCanceledOnTouchOutside( false );
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }
}
