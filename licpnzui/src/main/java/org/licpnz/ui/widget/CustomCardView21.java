package org.licpnz.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import org.licpnz.ui.drawable.RoundRectDrawable;

/**
 * Created by Ilya on 10.12.2016.
 */
@TargetApi(21)
public class CustomCardView21 extends ImageView {

    RoundRectDrawable mRRD;

    public CustomCardView21(Context c,android.util.AttributeSet as){
        super(c,as);
        mRRD = new RoundRectDrawable(2);
        setBackground(mRRD);
    }

   /* @Override
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                mRRD.getOutline(outline);
            }
        };
    }*/
}
