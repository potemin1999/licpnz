package org.licpnz.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

/**
 * Created by Ilya on 22.12.2016.
 */

public class NewUpdatingView extends FrameLayout {

    ViewGroup mContainer;
    ViewGroup.LayoutParams mParams;
    ProgressBar mPB;
    Animator enterAnim,exitAnim;
    boolean isShowing = false;

    @TargetApi(21)
    public NewUpdatingView(Context c){
        super(c);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPB = new ProgressBar(c);
        setVisibility(View.INVISIBLE);
        addView(mPB, new FrameLayout.LayoutParams(-2,-2, Gravity.CENTER));
    }



    public void setContainer(ViewGroup vg, ViewGroup.LayoutParams lp){
        mContainer = vg;
        mParams = lp;
    }

    public void setAnimator(Animator enterA,Animator exitA){

    }

    public void show(){
        if (isShowing) return;
        isShowing = true;
        if (enterAnim==null){
            enterAnim = ObjectAnimator.ofFloat(this,"translationX",-200,0).setDuration(250);
            enterAnim.setInterpolator(new DecelerateInterpolator());
        }
        if (exitAnim==null){
            exitAnim = ObjectAnimator.ofFloat(this,"translationX",0,-200).setDuration(250);
            enterAnim.setInterpolator(new AccelerateInterpolator());
        }
        mContainer.addView(this,mParams);
        enterAnim.start();
        this.setVisibility(View.VISIBLE);
    }

    public void hide(){
        if (!isShowing) return;
        isShowing = false;
        exitAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                mContainer.removeView(NewUpdatingView.this);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        exitAnim.start();
    }
}
