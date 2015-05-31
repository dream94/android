package com.example.MyGame.coregame.framework.impl;

import android.view.View;
import com.example.MyGame.coregame.framework.Input;
import com.example.MyGame.coregame.framework.Input.KeyEvent;
import com.example.MyGame.coregame.framework.impl.Pool;
import com.example.MyGame.coregame.framework.impl.Pool.PoolObjectFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/4/7.
 */
public class KeyboardHandler implements View.OnKeyListener {
    private boolean[] pressKeys = new boolean[128];
    private Pool<KeyEvent> keyEventPool;
    private List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
    private List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();

    public KeyboardHandler(View view) {
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
            @Override
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool<KeyEvent>(100, factory);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();           //获取焦点
    }

    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if(event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE){      //多点触碰
            return false;
        }
        synchronized (this){
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if(event.getAction() == android.view.KeyEvent.ACTION_DOWN){
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode>0&&keyCode<127){
                    pressKeys[keyCode] = true;
                }
            }
            if(event.getAction() == android.view.KeyEvent.ACTION_UP){
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode>0&&keyCode<127){
                    pressKeys[keyCode] = false;
                }
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }
    public boolean isPressed(int keyCode){
        if(keyCode<0||keyCode>127){
            return false;
        }
        return pressKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents(){
        synchronized (this){
            int len = keyEvents.size();
            for(int t=0; t<len; ++t){
                keyEventPool.addfreeObject(keyEvents.get(t));
            }
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }


}
