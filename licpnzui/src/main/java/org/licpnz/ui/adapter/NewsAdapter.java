package org.licpnz.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.licpnz.api.Api;
import org.licpnz.api.news.New;
import org.licpnz.api.news.NewsApi;
import org.licpnz.ui.R;
import org.licpnz.ui.Ui;
import org.licpnz.ui.drawable.RoundRectDrawable;
import org.licpnz.ui.threads.NewPreviewLoadingThread;
import org.licpnz.ui.widget.PreviewImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya on 20.11.2016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>
        implements NewsApi.AsyncNewsReceiver,View.OnClickListener,Serializable {

    public interface Callback{
        public void onError(int err);
        public void onNewAction(New n,NewsHolder nh,int action);
    }

    public static final int ACTION_REQUEST_SHOW_DETAILS=4;
    public static final int ERROR_NEWS_NOT_RECEIVED=8;

    @Override
    public void onClick(View v) {
        New n = findNewById(v.getId());
        if (n!=null){
            mCallback.onNewAction(n,findHolderByNew(n), ACTION_REQUEST_SHOW_DETAILS);
        }else{
            Api.out.println("action click for null New");
        }
    }

    private final Context mContext;
    private final List<New> mList;
    private final NewsApi mNewsApi;
    private final Callback mCallback;
    private int lastLoadingPosition=-2;
    private int lastLoadedPage=0;

    public final New findNewById(int id){
        for (int i = 0;i<mList.size();i++){
            if (id==mList.get(i).mID) return mList.get(i);
        }
        return null;
    }

    public NewsHolder findHolderByNew(New n){
        //for (int i = 0;i<mList.size();i++){
            //if (mList.get(i).mObjects.containsKey("holder"))
             //   if (n==mList.get(i).mObjects.get("holder"))
                //    return (NewsHolder) mList.get(i).mObjects.get("holder");
       // }
        if (n.mObjects.containsKey("holder")){
            return (NewsHolder) n.mObjects.get("holder");
        }else {
            return null;
        }
    }


    public final String[][] mRegex = {
            {"&laquo;","\""},//"«"},
            {"&raquo;","\""},//"»"},
            {"&dquo;","\""},
            {"&quot;","\""},
            {"&ndash;","–"},
            {"&nbsp;"," "}
    };

    private Handler h;


    public NewsAdapter(Context c,Callback callback){
        super();
        mContext = c;
        mCallback = callback;
        mList = new ArrayList<New>();
        mNewsApi = Ui.getApi().getNewsApi();
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what>-1) {
                    notifyItemInserted(msg.what);
                }else{
                    notifyDataSetChanged();
                }
            }
        };
        requestNews(0);
        lastLoadedPage++;
    }

    public void requestNews(int page) {
        mNewsApi.getNewsAsync(0+page,1,this,page+1,false);
    }

    public void insertNews(New[] news){
        for (New n : news){
            transformNew(n);
            onInsertNew(n);
            mList.add(mList.size(),n);
            final int what = mList.size() - 1;
            h.post(new Runnable(){
                @Override
                public void run() {
                    if (what>-1) {
                        notifyItemInserted(what);
                    }else{
                        notifyDataSetChanged();
                    }
                }
            });
           // h.sendEmptyMessage(mList.size()-1);
        }
        //h.sendEmptyMessage(-111);
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsHolder nh = new NewsHolder(mContext,(ViewGroup) ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)) .inflate(R.layout.new_layout,null));
        return nh;
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.setNew(mList.get(position));
        if (position>lastLoadedPage*10-4){
            lastLoadedPage++;
            requestNews(lastLoadedPage);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onReceiveNews(New[] receive, int requestId) {
        if (receive==null) {
            mCallback.onError(ERROR_NEWS_NOT_RECEIVED);
            return;
        }
        System.out.println("received "+receive.length+" news");
        insertNews(receive);
    }

    public void onInsertNew(final New n){
        if (n.mM.mAttached.length>0)
            new Thread(){
                public void run(){
                    for ( String s : n.mM.mAttached){
                        if (s.endsWith(".jpg")){
                            n.mObjects.put(s,new String("loading"));
                        }
                    }
                }
            }.start();
    }

    public void transformNew(final New n){
        if (n.mT.mText!=null)
            n.mT.mText = regexReplace(n.mT.mText);
        if (n.mM.mText!=null)
            n.mM.mText = regexReplace(n.mM.mText);
    }

    public String regexReplace(String in){
        for (int i = 0; i<mRegex.length; i++) {
            in = in.replaceAll(mRegex[i][0],mRegex[i][1]);
        }
        return in;
    }



    public class NewsHolder extends RecyclerView.ViewHolder implements Serializable{

        public final ViewGroup mContainer;
        public final ViewGroup mNewLayout;
        public final View mNewTransitionBackground;
        public final View mTitlebarLayout;
        final LayoutInflater mInflater;
        public final TextView mTitleTextView;
        public final TextView mContentTextView;
        final TextView mIdTextView;
        public final TextView mPreviewSrcTextView;
        public final PreviewImage mImageView;
        public New mNew;
        boolean isInUpdate;

        public NewsHolder(Context c,View v){
            super(v);
            mContainer = (ViewGroup) itemView;

            RecyclerView.LayoutParams llll = new RecyclerView.LayoutParams(-1,-2);
            llll.setMargins(10,10,10,10);
            mContainer.setLayoutParams(llll);

            mInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            mNewLayout = (ViewGroup) mContainer.findViewById(R.id.new_layout); // (ViewGroup) mInflater.inflate(R.layout.new_layout,null);
            mNewTransitionBackground = mContainer.findViewById(R.id.new_background_transition);
            mTitlebarLayout = mNewLayout.findViewById(R.id.new_title_container);

            mNewLayout.setClickable(true);
            mNewLayout.setFocusable(true);
            mNewLayout.setEnabled(true);
            mNewLayout.setOnClickListener(NewsAdapter.this);




            mTitleTextView = (TextView) mNewLayout.findViewById(R.id.new_title_textview);
            mContentTextView = (TextView) mNewLayout.findViewById(R.id.new_content_textview);
            mIdTextView = (TextView) mNewLayout.findViewById(R.id.new_debug_id_textview);
            mPreviewSrcTextView = (TextView) mNewLayout.findViewById(R.id.new_debug_previewsrc_textview);
            mImageView = (PreviewImage)  mNewLayout.findViewById(R.id.new_src_imageview);

            //mImageView.setOnClickListener(NewsAdapter.this);
           // mContentTextView.setOnClickListener(NewsAdapter.this);
           // mTitleTextView.setOnClickListener(NewsAdapter.this);
            //mContainer.addView(mNewLayout,-1,-1);

            nullTransitionName();
        }

        @TargetApi(21)
        public void nullTransitionName(){
            mTitleTextView.setTransitionName(null);
            mContentTextView.setTransitionName(null);
            mNewTransitionBackground.setTransitionName(null);
            mTitlebarLayout.setTransitionName(null);
            mImageView.setTransitionName(null);

            //mNewTransitionBackground.setBackground(new RoundRectDrawable(mNewTransitionBackground.getContext().getResources().getDimensionPixelSize(R.dimen.cardview_default_radius)));
        }

        public void setImage(Bitmap b){
            if (b!=null)
            mPreviewSrcTextView.setText(b.toString());
            mImageView.setImageBitmap(b);

            mImageView.getLayoutParams().height = -2;
            //float w = mImageView.getWidth();
            //if (w!=0)
               // mImageView.getLayoutParams().height = b.getHeight()*(  (int)(w/b.getWidth())  );

            mImageView.requestLayout();
        }

        public void setUpdateButton(){

        }

        public String checkRequestForPreview(New n){
            System.out.println("  new " + n.mID+"  checks preview");
            if (n.mM.mAttached.length==0)
                return null;
            for (String s : n.mM.mAttached){
                System.out.println("     check string "+s);
                if (s.endsWith(".jpg")){
                    //if (n.mObjects.containsKey("preview bitmap loading"))
                      //  return null;
                   // else
                    return s;
                }
                if (s.endsWith(".png")){
                    return s;
                }
            }
            return null;
        }


        public void setNew(New n) {
            mNew = n;
            mNew.mObjects.put("holder",this);
            mNewLayout.setId(mNew.mID);
           // mTitleTextView.setId(mNew.mID);
           // mContentTextView.setId(mNew.mID);
           // mImageView.setId(mNew.mID);
            mTitleTextView.setText(n.mT.mText);
            mIdTextView.setText(""+n.mID);
            if (n.mM.mText.equalsIgnoreCase(""))
                mContentTextView.getLayoutParams().height = 0;
            else
                mContentTextView.getLayoutParams().height = -2;
            mContentTextView.requestLayout();
            mContentTextView.setText(n.mM.mText);
            String s = checkRequestForPreview(mNew);
            if (s!=null){

                if (n.mObjects.containsKey("preview bitmap loading")){
                    if ((((int) n.mObjects.get("preview bitmap loading")) == 100)) {
                        final Bitmap b = (Bitmap) n.mObjects.get("preview bitmap");
                        mImageView.getLayoutParams().height = -2;
                        setImage(b);
                    }else{
                        mImageView.getLayoutParams().height = 0;
                       // mImageView.setImageDrawable(getColorDrawable(Color.BLACK));
                    }
                }else{
                    mImageView.getLayoutParams().height = 0;
                    //mImageView.setImageDrawable(getColorDrawable(Color.BLACK));
                    new NewPreviewLoadingThread(n, this, s).start();
                    if (!n.mObjects.containsKey("preview bitmap loading")) {
                        n.mObjects.put("preview bitmap loading", 0);
                    }
                }
                mImageView.setVisibility(View.VISIBLE);
            }else{
                mImageView.getLayoutParams().height = 0;
                mImageView.setVisibility(View.INVISIBLE);
            }
            mImageView.requestLayout();

        }
    }

    private static Drawable getColorDrawable(final int color){
        return new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawColor(color);
            }
            @Override
            public void setAlpha(int alpha) {}
            @Override
            public void setColorFilter(ColorFilter colorFilter) {}
            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        };
    }
}
