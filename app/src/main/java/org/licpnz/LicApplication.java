package org.licpnz;

import android.app.Application;
import android.content.SharedPreferences;

import org.licpnz.api.Api;

/**
 * Created by Ilya on 12.12.2016.
 */

public final class LicApplication extends Application implements Api.IApiSettingsProvider {

    private static String mHost = null;
    private static String mSiteHost = null;
    private SharedPreferences mPreferences;

    private static LicApplication mInstance;

    public static LicApplication getInstance(){
        return mInstance;
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
        mPreferences = getSharedPreferences("api",MODE_PRIVATE);
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

    public void setHost(String h){
        if (h==null){
            mHost = mPreferences.getString("mHost","");
            h = mHost;
        }
        mPreferences.edit().putString("mHost",h).commit();
        mHost = h;
    }
}
