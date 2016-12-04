package org.licpnz.api;

import org.licpnz.api.internal.InternalApi;
import org.licpnz.api.news.NewsApi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by Ilya on 05.11.2016.
 */

public class Api {

    public static PrintStream out;
    public static PrintStream err;
    public static PrintStream log;
    public static final boolean DEBUG = true;

    private static boolean isInitialized = false;
    private static ApiInstance mInstance;

    static{
        out = System.out;
        err = System.err;
        log = err;
    }

    public static final String getHost(){
        return "http://localhost:8080/";
    }

    public static final String getSiteHost() { return "http://licpnz.ru/"; }

    public static void init(){
        if (!isInitialized){
            mInstance = new InternalApi();
            mInstance.start();;
        }
    }

    public static ApiInstance getApi(){
        if (!isInitialized)
            init();
        return mInstance;
    }

    public static final long getRandomLongKey(){
        return (long)(Math.random()*2.0 - 1.0)*9223372036854775807l;
    }






    public static final class InvalidKeyException extends NullPointerException{
        public InvalidKeyException(){
            this("unknown");
        }
        public InvalidKeyException(String s){
            super("invalid key: "+s);
        }
    }


    public static abstract class ApiInstance extends Api{
        public abstract void start();
        public abstract void stop();
        public static final ApiInstance getApi(){return Api.getApi();}
        public abstract NewsApi getNewsApi();
    }

    public static final InputStream request(final String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            return connection.getInputStream();
        }catch (Throwable t){
            Api.err.println(t.toString());
            return null;
        }
    }

    public static final void request(final String url,final DataConnectionReceiver dcr){
        request(url,dcr,2048);
    }

    public static final void request(final String url,final DataConnectionReceiver dcr,final int bufferSize){
        new Thread(){
            public void run(){
                try {
                    dcr.onReceive(new byte[]{},0);
                    BufferedInputStream is = new BufferedInputStream(request(url));
                    out.println("request started");
                    out.println("request opened");
                    byte[] buffer = new byte[2048];
                    for (;;){
                        int r = is.read(buffer,0,buffer.length);
                        if (r<0) break;
                        dcr.onReceive(buffer,1);
                    }
                    out.println("request finishing");
                    dcr.onReceive(new byte[]{},2);
                    is.close();
                    out.println("request finished");
                }catch (Throwable ie){
                    if (DEBUG)
                        dcr.onReceive(new String("exception while api request: "+ie.toString()).getBytes(),2);
                    else
                        err.println("exception while api request: "+ie.toString());
                }
            }
        }.start();
    }

    public interface DataConnectionReceiver{
        public void onReceive(byte[] in,int state);
    }

    public interface ProtectedChannelListener{
        public long getKey(long key);
    }



    public static abstract class ApiMember{

        private final ProtectedChannelListener mPCL;
        private final long mKey;
        private final long mTrustedKey;

        public ApiMember(Api.ApiInstance api,ProtectedChannelListener pcl){
            super();
            mPCL = pcl;
            mKey = Api.getRandomLongKey();
            mTrustedKey = pcl.getKey(mKey);
        }

        protected final long getKey(){
            return mKey;
        }
        protected final long getTrustedKey(){
            return mTrustedKey;
        }
    }

    public static int ERROR_UNKNOWN = 0;
    public static int ERROR_CONNECTION_TIMEOUT = 1;
    public static int ERROR_HOST_UNREACHABLE = 2;
}
