package com.houseofapps.heystranger;

import android.app.Application;
import android.app.Service;
import android.util.Log;

/**
 * Created by abc on 23/03/2018.
 */

public class Delete_node extends Application {
    @Override
    public void onCreate() {
        Log.e("Application_lifecycle","onCreate");
        super.onCreate();
    }
    @Override
    public void onTerminate() {
        Log.e("Application_lifecycle","onTerminate");
        super.onTerminate();
    }
}
