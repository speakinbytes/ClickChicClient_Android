package com.speakinbytes.base.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;


/**
 * Created by bmjuan on 22/04/14.
 */
public class AppBase extends Application {
    private static AppBase mInstance;
    private static Context mAppContext;

    /**
     * Log or request TAG
     */
    public static final String TAG = "APP_BASE";



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public synchronized static AppBase getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }


}
