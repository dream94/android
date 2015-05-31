package com.example.MyGame.coregame.framework.impl;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.example.MyGame.coregame.framework.Input;
import com.example.MyGame.coregame.framework.Orientation;
import com.example.MyGame.coregame.framework.TouchHandler;

import java.util.List;

/**
 * Created by Admin on 2015/4/7.
 */
public class AndroidInput implements Input{
    private Orientation orientation;
    private KeyboardHandler keyboardHandler;
    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view) {
        orientation = new AndroidOrientitiaoHandler(context);
        keyboardHandler = new KeyboardHandler(view);
        touchHandler = new MutiTouchHandler(view);
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyboardHandler.isPressed(keyCode);
    }

    @Override
    public boolean isTouchDown() {
        return touchHandler.isTouchDown();
    }

    @Override
    public int getTouchX() {
        return touchHandler.getTouchX();
    }

    @Override
    public int getTouchY() {
        return touchHandler.getTouchY();
    }

    @Override
    public float getAzimuth() {
        return orientation.getAzimuth();        //返回的是弧度
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyboardHandler.getKeyEvents();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
