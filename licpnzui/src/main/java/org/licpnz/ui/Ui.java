package org.licpnz.ui;

import org.licpnz.api.Api;

/**
 * Created by Ilya on 20.11.2016.
 */

public class Ui {

    static{
        mApi = Api.getApi();
    }

    private static Api.ApiInstance mApi;

    public static Api.ApiInstance getApi(){
        return mApi;
    }
}