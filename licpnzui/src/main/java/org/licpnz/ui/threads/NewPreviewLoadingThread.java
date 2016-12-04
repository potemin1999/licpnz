package org.licpnz.ui.threads;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import org.licpnz.api.Api;
import org.licpnz.api.news.New;
import org.licpnz.ui.adapter.NewsAdapter;

/**
 * Created by Ilya on 22.11.2016.
 */

public class NewPreviewLoadingThread extends Thread {

    public final New mNew;
    public final NewsAdapter.NewsHolder mHolder;
    public final Handler mUiHandler;
    public final String mNotFullUrl;

    public NewPreviewLoadingThread(New n, NewsAdapter.NewsHolder h,String url){
        super();
        mNew = n;
        mHolder = h;
        if (url.startsWith("/"))
            mNotFullUrl = url.substring(1,url.length());
        else
            mNotFullUrl = url;
        mUiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mNew.mObjects.put("preview bitmap loading",100);
                mNew.mObjects.put("preview bitmap",(Bitmap)msg.obj);
                if (mHolder.mNew == mNew)
                    mHolder.setImage((Bitmap)mNew.mObjects.get("preview bitmap"));
            }
        };
    }

    @Override
    public void run() {
        Message m = new Message();
        System.out.println("new preview thread started on new "+mNew.mID);
        BitmapLoadingThread blt = new BitmapLoadingThread(Api.getSiteHost()+mNotFullUrl);
        blt.start();
        m.obj = blt.getBitmap();
        if (m.obj!=null) {
            System.out.println("preview bitmap downloaded for new " + mNew.mID);
            mUiHandler.sendMessage(m);
        }else{
            System.out.println("preview bitmap download failed for new " + mNew.mID);
        }
    }
}
