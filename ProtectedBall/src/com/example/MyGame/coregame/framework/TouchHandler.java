package com.example.MyGame.coregame.framework;

import android.view.View;
import com.example.MyGame.coregame.framework.Input.TouchEvent;
import java.util.List;

/**
 * Created by Admin on 2015/4/7.
 */
public interface TouchHandler extends View.OnTouchListener {
    public boolean isTouchDown();
    public int getTouchX();
    public int getTouchY();
    public List<TouchEvent> getTouchEvents();
}
