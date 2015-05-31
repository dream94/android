package com.example.MyGame.coregame.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.example.MyGame.coregame.framework.Graphics;

/**
 * Created by Admin on 2015/4/7.
 */
public class AndroidGraphics implements Graphics {
    private Bitmap bitmap;
    private Canvas canvas;

    public AndroidGraphics(Bitmap bitmap) {
        this.bitmap = bitmap;
        canvas = new Canvas(bitmap);
    }

    @Override
    public int getWidth() {
        return canvas.getWidth();
    }

    @Override
    public int getHeight() {
        return canvas.getHeight();
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }
}
