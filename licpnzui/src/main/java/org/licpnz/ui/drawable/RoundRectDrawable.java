package org.licpnz.ui.drawable;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Ilya on 10.12.2016.
 */

public class RoundRectDrawable extends Drawable {

    final float mRadius;
    final Paint mPaint;

    public RoundRectDrawable(int radius){
        super();
        mRadius = radius;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    public RoundRectDrawable(){
        this(1);
    }


    @Override
    @TargetApi(21)
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(0,0,canvas.getWidth(),canvas.getHeight(),mRadius,mRadius,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    @TargetApi(21)
    public void getOutline(Outline outline) {
        outline.setRoundRect(0,0,getIntrinsicWidth(),getIntrinsicHeight(),mRadius);
    }
}
