package org.licpnz.transitions;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ilya on 06.12.2016.
 */

@TargetApi(21)
public class ElevationTransition extends Transition {
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureValues(TransitionValues transitionValue){
        transitionValue.values.put("elevation",transitionValue.view.getElevation());
        transitionValue.values.put("translationZ",transitionValue.view.getTranslationZ());
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues!=null&&endValues!=null){
            final View endView = endValues.view;
            final float startE = (float) startValues.values.get("elevation");
            final float endE = (float) endValues.values.get("elevation");
            final float startZ = (float) startValues.values.get("translationZ");
            final float endZ = (float) endValues.values.get("translationZ");
            if (startE!=endE|| startZ!=endZ){
                ValueAnimator anim = ValueAnimator.ofFloat(0,1);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float inter = (float) animation.getAnimatedValue();
                        endView.setElevation(startE + (endE - startE)*inter);
                        endView.setTranslationZ(startZ + (endZ - startZ)*inter);
                    }
                });
                return anim;
            }
        }
        return null;
    }
}
