package org.licpnz.ui;

import android.content.Context;

import org.licpnz.api.Api;

/**
 * Created by Ilya on 20.11.2016.
 */

public class Ui {

    static{
        mApi = Api.getApi();
    }

    private static Api.ApiInstance mApi;
    private static Context mContext;
    private static Provider mProvider;

    public static void bind(Context c,Provider p){
        mContext = c;
        mProvider = p;
    }

    public static boolean isImageLoadingEnabled(){
        if (mProvider!=null)
            return mProvider.isImageLoadingEnabled();
        return true;
    }

    private static void checkStateLoss(){
    }

    public static Api.ApiInstance getApi(){
        return mApi;
    }

    public static interface Provider{
        public boolean isImageLoadingEnabled();
    }
}