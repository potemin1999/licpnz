package org.licpnz.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ilya on 23.11.2016.
 */

public class PreviewImage extends ImageView {

    public PreviewImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec,100);
        //if (true) return;
        final Drawable d = this.getDrawable();
        if (d!=null){
            final int w = MeasureSpec.getSize(widthMeasureSpec);
            final int h = (int) Math.ceil(w*(float)d.getIntrinsicHeight()/d.getIntrinsicWidth());
            if (getLayoutParams().height==0)
                this.setMeasuredDimension(w,0);
            else
                this.setMeasuredDimension(w,h);
        }else{
            super.onMeasure(widthMeasureSpec,0);
        }
    }

}
