package org.licpnz.api.news;


import org.json.JSONArray;
import org.licpnz.api.Api;

import java.io.InputStream;
import java.io.ObjectStreamClass;
import java.util.Scanner;

import org.json.JSONObject;
/**
 * Created by Ilya on 05.11.2016.
 */

public class NewsApi extends Api.ApiMember{


    public static final String[][] mRegex = {
            {"&laquo;","\""},//"«"},
            {"&raquo;","\""},//"»"},
            {"&dquo;","\""},
            {"&quot;","\""},
            {"&ndash;","-"},
            {"&nbsp;"," "},
            {"<strong>",""},
            {"</strong>",""}
    };

    public interface AsyncNewsReceiver{
        public void onReceiveNews(New[] receive,int requestId);
    }

    public interface AsyncNewDetailer{
        public void onUpdateNew(New n);
    }

    public NewsApi(Api.ApiInstance api,Api.ProtectedChannelListener pcl){
        super(api,pcl);
    }

    public void start(long tk){
        if (tk!=getTrustedKey()) throw new Api.InvalidKeyException("failed news start verification");
    }

    public void stop(long tk){
        if (tk!=getTrustedKey()) throw new Api.InvalidKeyException("failed news stop verification");
    }

    public void getNewsAsync(final long offset,final long count,final AsyncNewsReceiver anr,final int requestId,final boolean safemode){
        Thread receiver = Thread.currentThread();
        new Thread(){
            public void run(){
                New[] n = getNews(offset,count,0,safemode);
                if (n==null&&safemode) return;
                synchronized (anr){
                    anr.onReceiveNews(n,requestId);
                }
            }
        }.start();
    }

    public New[] getNews(long offset,long count) {
        return getNews(offset,count,0);
    }

    public New[] getNews(long offset,long count,int version){
        return getNews(offset,count,0);
    }

    public New[] getNews(long offset,long count,int version,boolean safemode){
        for (int i = 0;i<count;i++){
            InputStream is = Api.request(Api.getHost()+"api/news.get.php?p="+offset);
            if (is==null) return null;
            Scanner s = new Scanner(is);
            StringBuffer sb = new StringBuffer();
            while (s.hasNext())
                sb.append(s.nextLine()+"\n");

            return parseNews(sb.toString());
        }
        return null;
    }

    public void getAsyncDetails(final New n,final AsyncNewDetailer and){
        new Thread(){
            public void run(){
                getDetails(n);
                and.onUpdateNew(n);
            }
        }.start();
    }

    public void getDetails(final New n) {
        try {
            String url = Api.getHost() + "api/news.get.details.php?url=" + Api.getSiteHost() + n.mT.mRef.substring(1, n.mT.mRef.length());
            Api.out.println("request details : " + url);
            InputStream is = Api.request(url);

            if (is == null) return;
            Scanner s = new Scanner(is);
            StringBuffer sb = new StringBuffer();
            while (s.hasNext())
                sb.append(s.nextLine() + "\n");
            String json = sb.toString();
            updateNew(json, n);
            n.isDetailsRequested = true;
        }catch(Throwable t){}
    }

    public void updateNew(String json,New n){
       // System.out.println("trying to update \n\n\n"+json+"\n\n\n");
        JSONObject root = new JSONObject(json);
        JSONObject njson = root.getJSONObject("new");
        JSONObject message = njson.getJSONObject("message");
        String message_text = message.getString("text");
        n.mM.mDetailedText = regexReplace(message_text);
        JSONArray src = message.getJSONArray("src");
        n.mM.mDetailedSrc = new String[src.length()];
        for (int c = 0; c< src.length() ; c++){
            n.mM.mDetailedSrc[c] = src.getString(c);
        }
    }



    public New[] parseNews(String json){
        JSONObject root = new JSONObject(json);
        JSONArray array = root.getJSONArray("news");
        int l = array.length();
        New[] news = new New[l];
        for (int j = 0;j<l;j++){
            New n = new New();
            JSONObject o = array.getJSONObject(j);
            n.mID = o.getInt("id");
            n.mT.mText = o.getString("title");
            n.mT.mRef = o.getString("href");

            JSONObject m = o.getJSONObject("message");
            n.mM.mText = m.getString("text");
            JSONArray src = m.getJSONArray("src");
            int srcl = src.length();
            System.out.println("src l "+src.length());
            n.mM.mAttached = new String[srcl];
            for (int i = 0; i<srcl;i++){
                n.mM.mAttached[i] = src.getString(i);
            }
            JSONArray refs = m.getJSONArray("refs");
            n.mM.mRefs = new String[refs.length()];
            for (int i = 0; i<refs.length();i++){
                n.mM.mRefs[i] = refs.getJSONObject(i).getString("href");
            }

            JSONObject d = o.getJSONObject("details");
            n.mI.mDate = d.getString("date");
            n.mI.mTime = d.getString("time");

            news[j] = n;
        }
        dumpNews(news);
        return news;
    }

    private void dumpNews(New[] news){
        for (New n : news){
            Api.out.println(n.toString());
        }
    }


    public static String regexReplace(String in){
        for (int i = 0; i<mRegex.length; i++) {
            in = in.replaceAll(mRegex[i][0],mRegex[i][1]);
        }
        return in;
    }

}
