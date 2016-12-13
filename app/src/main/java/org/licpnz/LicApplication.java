package org.licpnz;

import android.app.Application;

import org.licpnz.api.Api;

/**
 * Created by Ilya on 12.12.2016.
 */

public class LicApplication extends Application {

    private static String mHost = null;

    public static final String getHost(){
        if (mHost==null){
            return Api.getHost();
        }else{
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
