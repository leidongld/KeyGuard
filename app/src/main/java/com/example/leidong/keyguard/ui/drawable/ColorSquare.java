package com.example.leidong.keyguard.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by leidong on 2017/10/15
 */

public class ColorSquare extends Drawable{
    private String color;

    public ColorSquare(String color) {
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.parseColor(color));
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override
    public int getIntrinsicWidth() {
        return 20;
    }

    @Override
    public int getIntrinsicHeight() {
        return 20;
    }
}
