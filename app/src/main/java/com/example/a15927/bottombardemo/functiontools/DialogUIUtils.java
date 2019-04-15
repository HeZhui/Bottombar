package com.example.a15927.bottombardemo.functiontools;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Administrator on 2019/4/7.
 */

public class DialogUIUtils {
    public static Dialog showLoadingDialog(Context context, String showMes){
        Dialog progressDialog;
        progressDialog = CustomProgressTransDialog.createLoadingDialog(context, showMes);
        return progressDialog;
    }

    public static void dismiss(Dialog dialog){

        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }

}
