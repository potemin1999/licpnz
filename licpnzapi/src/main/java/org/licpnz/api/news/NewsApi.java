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

    public interface AsyncNewsReceiver{
        public void onReceiveNews(New[] receive,int requestId);
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

    public void getNewsAsync(final long offset,final long count,final AsyncNewsReceiver anr,final int requestId){
        Thread receiver = Thread.currentThread();
        new Thread(){
            public void run(){
                New[] n = getNews(offset,count);
                synchronized (anr){
                    anr.onReceiveNews(n,requestId);
                }
            }
        }.start();
    }

    public New[] getNews(long offset,long count){
        return getNews(offset,count,0);
    }

    public New[] getNews(long offset,long count,int version){
        for (int i = 0;i<count;i++){
            InputStream is = Api.request(Api.getHost()+"api/news.get.php?p="+offset);
            if (is==null) throw new NullPointerException("is null");
            Scanner s = new Scanner(is);
            StringBuffer sb = new StringBuffer();
            while (s.hasNext())
                sb.append(s.nextLine()+"\n");

            return parseNews(sb.toString());
        }
        return null;
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

}
