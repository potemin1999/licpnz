package org.licpnz.api.internal;

import org.licpnz.api.Api;
import org.licpnz.api.news.NewsApi;

/**
 * Created by Ilya on 05.11.2016.
 */

public class InternalApi extends Api.ApiInstance {

    private NewsApi mNewsApi;
    private long mNewsApiKey;

    public boolean isStarted = false;
    public boolean isStopped = true;

    @Override
    public void start() {
        if (isStarted) return;
        isStarted = true;
        isStarted = false;
        mNewsApi = new NewsApi(this, new Api.ProtectedChannelListener() {
            public long getKey(long key){
                mNewsApiKey = key;
                return Api.getRandomLongKey();
            }
        });
    }

    @Override
    public void stop() {
        if (isStopped) return;
        isStarted = false;
        isStopped = true;
        mNewsApi.stop(mNewsApiKey);
        mNewsApi = null;
    }

    @Override
    public NewsApi getNewsApi() {
        return mNewsApi;
    }
}
