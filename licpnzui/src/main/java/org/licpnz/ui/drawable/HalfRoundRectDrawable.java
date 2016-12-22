package org.licpnz.ui.drawable;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by Ilya on 22.12.2016.
 */

public class HalfRoundRectDrawable extends Drawable {

    boolean isRight = true;
    int color = Color.WHITE;
    Path path;
    Paint p;

    public HalfRoundRectDrawable(boolean isright,int color){
        super();
        isRight = isright;
        this.color = color;
        p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        p.setShadowLayer(4,1,1,Color.argb(100,100,100,100));
        path = new Path();
    }

    @TargetApi(21)
    @Override
    public void getOutline(Outline outline) {
        //int i = 0; i = 1/i;
        //outline.setRect(0,0,333,55);//(int)(getIntrinsicWidth()-getIntrinsicHeight()*0.5f),getIntrinsicHeight());
        //outline.setOval(50,50,50,100);//getIntrinsicWidth()-getIntrinsicHeight(),0,getIntrinsicWidth(),getIntrinsicHeight());
    }

    @TargetApi(21)
    @Override
    public void draw(Canvas canvas) {
        if (isRight){
            int b = 5;
            path.addArc(canvas.getWidth() - canvas.getHeight()+b,0+b,canvas.getWidth()-b,canvas.getHeight()-b,270,180);
            path.addRect(0,0+b,canvas.getWidth() - (int)(0.5f*canvas.getHeight())+b,canvas.getHeight()-b, Path.Direction.CW);
            canvas.drawPath(path,p);
            //canvas.drawCircle(canvas.getWidth()-canvas.getHeight()*0.5f,canvas.getHeight()*0.5f,canvas.getHeight()*0.5f,p);
            //canvas.drawRect(new RectF(0,0,canvas.getWidth()-canvas.getHeight()*0.5f,canvas.getHeight()),p);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
