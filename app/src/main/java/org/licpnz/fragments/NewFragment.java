package org.licpnz.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.TransformationMethod;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.licpnz.R;
import org.licpnz.activities.MainActivity;
import org.licpnz.api.Api;
import org.licpnz.api.news.New;
import org.licpnz.api.news.NewsApi;
import org.licpnz.transitions.ElevationTransition;
import org.licpnz.transitions.NewDetailsTransition;
import org.licpnz.ui.adapter.NewsAdapter;
import org.licpnz.ui.drawable.HalfRoundRectDrawable;
import org.licpnz.ui.threads.BitmapLoadingThread;
import org.licpnz.ui.widget.NewUpdatingView;

/**
 * Created by Ilya on 05.12.2016.
 */

public class NewFragment extends Fragment implements NewsApi.AsyncNewDetailer {

    FrameLayout mContainer;
    LinearLayout mContent;
    TextView mTitleTextView;
    TextView mContentTextView;
    ImageView mPreviewImage;
    New mNew;
    Bitmap mPreview;
    NewsAdapter.NewsHolder mRequester;
    Scene mScene;
    NewUpdatingView mUpdatingView;

    Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.obj instanceof Bitmap) {
                    ImageView iv = new ImageView(getActivity());
                    mContent.addView(iv);
                    iv.setImageBitmap((Bitmap) msg.obj);
                }
            }catch(Throwable t){}
        }
    };

    public void connectSrc(){
        if (mNew.mM.mDetailedSrc!=null)
            for (String s : mNew.mM.mDetailedSrc){
                if (s.endsWith(".jpg")||s.endsWith(".png"))
                    if (mNew.mObjects.containsKey("preview href")&&!((String)mNew.mObjects.get("preview href")).equalsIgnoreCase(s)){
                        if (!mNew.mObjects.containsKey("image "+s)||((int)mNew.mObjects.get("image "+s))!=100){
                            loadImage(s);
                        }else if(mNew.mObjects.containsKey("image "+s)&&((int)mNew.mObjects.get("image "+s))==100){
                            Message m = new Message();
                            m.obj = (Bitmap) mNew.mObjects.get("bitmap "+s);
                            h.sendMessage(m);
                        }
                    }
            }
    }

    @TargetApi(21)
    @Override
    public void onUpdateNew(final New n) {
        h.postDelayed( new Runnable() {
            public void run() {
                TransitionManager.beginDelayedTransition(mContent);
                if (n.mM.mDetailedText != null)
                    mContentTextView.setText(n.mM.mDetailedText);
                connectSrc();
                mUpdatingView.hide();
            }
        },0);
    }

    @TargetApi(21)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = (FrameLayout) getActivity().getLayoutInflater().inflate(org.licpnz.ui.R.layout.new_detailed_layout,null);
        mScene = new Scene(mContainer);
        mContent = (LinearLayout) mContainer.findViewById(R.id.new_details_container);
        mTitleTextView = (TextView) mContainer.findViewById(org.licpnz.ui.R.id.new_detailed_title_text_view);
        mContentTextView = (TextView) mContainer.findViewById(org.licpnz.ui.R.id.new_detailed_content_text_view);
        mPreviewImage = (ImageView) mContainer.findViewById(R.id.new_preview_detailed_image);
        mUpdatingView = new NewUpdatingView(getActivity());
        mUpdatingView.setBackground(new HalfRoundRectDrawable(true, Color.WHITE));
        mUpdatingView.setElevation(40);
        mUpdatingView.setTranslationZ(-20);

        FrameLayout.LayoutParams ullp = new FrameLayout.LayoutParams(200,100, Gravity.BOTTOM | Gravity.LEFT);
        ullp.bottomMargin=200;
        mUpdatingView.setContainer(mContainer,ullp);



        if (savedInstanceState==null)
            savedInstanceState = getArguments();
        //if (savedInstanceState==null)
        mNew = (New) savedInstanceState.getSerializable("new");
        mRequester = (NewsAdapter.NewsHolder) savedInstanceState.getSerializable("holder");
        getArguments().clear();
        mTitleTextView.setText(mNew.mT.mText);
        mContentTextView.setText(mNew.mM.mText);
        mPreview = null;
        if (mNew.mObjects.containsKey("preview bitmap loading")){
            if ((((int) mNew.mObjects.get("preview bitmap loading")) == 100)) {
                mPreview = (Bitmap) mNew.mObjects.get("preview bitmap");
            }
        }
        if (mPreview!=null){
            mPreviewImage.setImageBitmap(mPreview);
        }
        if (!mNew.isDetailsRequested) {
            mUpdatingView.show();
            Api.getApi().getNewsApi().getAsyncDetails(mNew, this);
        }else
        if (mNew.mM.mDetailedText != null)
            mContentTextView.setText(mNew.mM.mDetailedText);
        connectSrc();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("new",mNew);
        //outState.putSerializable("holder",mRequester);
        super.onSaveInstanceState(outState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContainer;
    }



    public void loadImage(final String url0){
        new Thread(){
            public void run(){
                String url = url0;
                mNew.mObjects.put("image "+url0,0);
                if (url.startsWith("/"))
                    url = url.substring(1,url.length());
                else
                    url = url;
                BitmapLoadingThread blt = null;
                if (url.startsWith("http"))
                    blt = new BitmapLoadingThread(url);
                else
                    blt = new BitmapLoadingThread(Api.getSiteHost()+url);
                blt.start();
                Bitmap b = blt.getBitmap();
                if (b==null) return;
                mNew.mObjects.put("bitmap "+url0,b);
                Message m = new Message();
                m.obj = b;
                h.sendMessage(m);
                mNew.mObjects.put("image "+url0,100);
            }
        }.start();
    }



















    public static void newInstance(NewsListFragment nlf, New n, NewsAdapter.NewsHolder nh){
        NewFragment nf = new NewFragment();
        Bundle b = new Bundle();
        b.putSerializable("new",n);
        b.putSerializable("holder",nh);
        nf.setRetainInstance(false);
        nf.setArguments(b);
        //Toast.makeText(nlf.getActivity(),"holder id "+nh.mNewLayout.getId(),Toast.LENGTH_SHORT).show();
        FragmentTransaction ft = nlf.getFragmentManager().beginTransaction();
        ft.replace(org.licpnz.R.id.main_fragment_container,nf, MainActivity.mNewsDetailsFragmentTag);
        ft.addToBackStack(MainActivity.mNewsDetailsFragmentTag);
        if (true){
            prepareTransition(nlf,n,nh,ft,nf);
        }else{
            prepareAnimation(nlf,n,nh,ft,nf);
        }
        ft.commit();
    }

    @TargetApi(21)
    public void onDestroy(){
        try{
            mRequester.mNewTransitionBackground.setElevation(0);
            mRequester.mNewLayout.setTranslationZ(0);
        }catch(Throwable t) {}
        super.onDestroy();
    }

    @TargetApi(21)
    public static void prepareTransition(NewsListFragment nlf, New n,final NewsAdapter.NewsHolder nh,FragmentTransaction ft,NewFragment nf){

        nh.mNewTransitionBackground.setElevation(nlf.getResources().getDimensionPixelSize(R.dimen.cardview_default_elevation));
        nh.mNewLayout.setTranslationZ(nh.mNewTransitionBackground.getElevation());

        nh.mTitleTextView.setTransitionName(nlf.getResources().getString(org.licpnz.ui.R.string.new_title_transition_name));
        nh.mContentTextView.setTransitionName(nlf.getResources().getString(org.licpnz.ui.R.string.new_content_transition_name));
        nh.mNewTransitionBackground.setTransitionName(nlf.getResources().getString(org.licpnz.ui.R.string.new_background_transition_name));
        nh.mTitlebarLayout.setTransitionName(nlf.getResources().getString(org.licpnz.ui.R.string.new_titlebar_background_transition_name));
        nh.mImageView.setTransitionName(nlf.getResources().getString(org.licpnz.ui.R.string.new_preview_transition_name));

        ft.addSharedElement(nh.mTitleTextView,nlf.getResources().getString(org.licpnz.ui.R.string.new_title_transition_name));
        ft.addSharedElement(nh.mContentTextView,nlf.getResources().getString(org.licpnz.ui.R.string.new_content_transition_name));
        ft.addSharedElement(nh.mNewTransitionBackground,nlf.getResources().getString(org.licpnz.ui.R.string.new_background_transition_name));
        ft.addSharedElement(nh.mTitlebarLayout,nlf.getResources().getString(org.licpnz.ui.R.string.new_titlebar_background_transition_name));
        ft.addSharedElement(nh.mImageView,nlf.getResources().getString(org.licpnz.ui.R.string.new_preview_transition_name));

        NewDetailsTransition mEnter = new NewDetailsTransition();//nlf.getResources().getDimensionPixelSize(R.dimen.cardview_default_elevation), ElevationTransition.ELEVATION_AUTO);
        NewDetailsTransition mReturn = new NewDetailsTransition();
        mReturn.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                //nh.mTitleTextView.setVisibility(View.VISIBLE);
                //nh.mContentTextView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onTransitionEnd(Transition transition) {
                nh.mTitleTextView.setTransitionName(null);
                nh.mContentTextView.setTransitionName(null);
                nh.mNewTransitionBackground.setTransitionName(null);
                nh.mTitlebarLayout.setTransitionName(null);
                nh.mImageView.setTransitionName(null);
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });
        nf.setSharedElementEnterTransition(mEnter);
        nf.setSharedElementReturnTransition(mReturn);
        //nh.mTitleTextView.setVisibility(View.INVISIBLE);
        //nh.mContentTextView.setVisibility(View.INVISIBLE);
        //nf.setExitTransition(new NewDetailsTransition());
        /*Fade f = new Fade();
        f.setMode(Fade.MODE_IN);
        Fade fO = new Fade();
        fO.setMode(Fade.MODE_OUT);*/
        Explode e = (Explode) new android.transition.Explode()
                .excludeTarget(nh.mContainer,false);
                //.excludeChildren(nh.mTitleTextView,true)
                //.excludeChildren(nh.mContentTextView,true);
                //.excludeTarget(nlf.getResources().getString(org.licpnz.ui.R.string.new_title_transition_name),true)
               // .excludeTarget(nlf.getResources().getString(org.licpnz.ui.R.string.new_content_transition_name),true);
        /*TransitionSet tsExit = new TransitionSet();
        tsExit.setOrdering(tsExit.ORDERING_TOGETHER);*/
        //tsExit.addTransition(e);
        //tsExit.addTransition(f);
       // nf.setEnterTransition(fO);

        //nlf.setExitTransition(e);
        nlf.setExitTransition(new Fade());
        /*nlf.setExitTransition(new android.transition.Explode()
                .excludeTarget(nlf.getResources().getString(org.licpnz.ui.R.string.new_title_transition_name),true)
                .excludeTarget(nlf.getResources().getString(org.licpnz.ui.R.string.new_content_transition_name),true));*/
    }

    public static void prepareAnimation(NewsListFragment nlf, New n, NewsAdapter.NewsHolder nh,FragmentTransaction ft,NewFragment nf){

    }

}
