package org.licpnz;

import android.app.Application;
import android.content.SharedPreferences;

import org.licpnz.api.Api;
import org.licpnz.ui.Ui;

/**
 * Created by Ilya on 12.12.2016.
 */

public final class LicApplication extends Application implements Api.IApiSettingsProvider,Ui.Provider {

    private static String mHost = null;
    private static String mSiteHost = null;
    private static boolean isImageLoadingEnabled=true;
    private SharedPreferences mPreferences;

    private static LicApplication mInstance;

    public static LicApplication getInstance(){
        return mInstance;
    }

    public boolean getImageLoadingEnabled(){
        return isImageLoadingEnabled;
    }

    public String getHost(){
        return mHost;
    }

    @Override
    public String getSiteHost() {
        return mSiteHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Ui.bind(this,this);
        mPreferences = getSharedPreferences("api",MODE_PRIVATE);
        mHost = mPreferences.getString("mHost",null);
        mSiteHost = mPreferences.getString("mSiteHost",null);
        isImageLoadingEnabled = mPreferences.getBoolean("mImageLoading",true);
        Api.init();
        Api.setSettingsProvider(this);

        //
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setSiteHost(String sh){
        if (sh==null) {
            mSiteHost = mPreferences.getString("mSiteHost","");
            sh = mSiteHost;
        }
        mPreferences.edit().putString("mSiteHost",sh).commit();
        mSiteHost = sh;
    }

    public void setIsImageLoadingEnabled(boolean is){
        mPreferences.edit().putBoolean("mImageLoading",true);
        isImageLoadingEnabled = is;
    }

    public void setHost(String h){
        if (h==null){
            mHost = mPreferences.getString("mHost","");
            h = mHost;
        }
        mPreferences.edit().putString("mHost",h).commit();
        mHost = h;
    }

    @Override
    public boolean isImageLoadingEnabled() {
        return isImageLoadingEnabled;
    }
}
