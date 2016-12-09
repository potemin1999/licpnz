package org.licpnz.transitions;

import android.annotation.TargetApi;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.TransitionValues;

/**
 * Created by Ilya on 05.12.2016.
 */

@TargetApi(21)
public class NewDetailsTransition extends TransitionSet{

    public NewDetailsTransition(){
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeImageTransform());
        //addTransition(new AutoTransition());
        addTransition(new ChangeBounds());
        //addTransition(new ChangeClipBounds());
        addTransition(new ChangeTransform());
        addTransition(new ElevationTransition());
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
    }
}
