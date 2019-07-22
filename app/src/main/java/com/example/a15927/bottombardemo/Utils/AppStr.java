package com.example.a15927.bottombardemo.Utils;

import android.app.Application;
import android.util.Log;

/**
 * Created by Administrator on 2019/7/16.
 */

public class AppStr extends Application{
    private boolean state = false;
    private boolean updateInf = false;

    public boolean isState() {
        Log.i( "Test", "isState: "+state );
        return state;
    }

    public void setState(boolean state) {
        Log.i( "Test", "isState: "+state );
        this.state = state;
    }

    public boolean isUpdateInf() {
        Log.i( "Test", "isUpdateInf: " +updateInf);
        return updateInf;
    }

    public void setUpdateInf(boolean updateInf) {
        Log.i( "Test", "isUpdateInf: " +updateInf);
        this.updateInf = updateInf;
    }
}
