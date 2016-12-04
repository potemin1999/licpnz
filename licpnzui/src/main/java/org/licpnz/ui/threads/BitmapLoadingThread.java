package org.licpnz.ui.threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.licpnz.api.Api;
import org.licpnz.api.internal.ByteList;
import org.licpnz.api.news.New;

import java.io.InputStream;

/**
 * Created by Ilya on 22.11.2016.
 */



public class BitmapLoadingThread extends Thread {

    private class Receiver implements Api.DataConnectionReceiver{
        ByteList bl = new ByteList();

        @Override
        public void onReceive(byte[] in, int state) {
            if (state==2){
                onBitmapLoaded(bl.toArray());
            }else{

            }
        }
    }



    private Bitmap mBitmap;
    private boolean isLoaded = false;
    private InputStream mInputStream;

    public BitmapLoadingThread(String url){
        super();
        try {
            mInputStream = Api.request(url);
        }catch(Throwable t){}
    }

    public Bitmap getBitmap(){
        if (mInputStream==null) return null;
        try {
            mBitmap = BitmapFactory.decodeStream(mInputStream);
            isLoaded = true;
            while (!isLoaded) {
                try {
                    sleep(2);
                } catch (Throwable t) {
                }
            }
            return mBitmap;
        }catch(Throwable t){
            return null;
        }

    }

    public void onBitmapLoaded(byte[] loaded){
        mBitmap = BitmapFactory.decodeStream(mInputStream);
        isLoaded = true;
    }

}
