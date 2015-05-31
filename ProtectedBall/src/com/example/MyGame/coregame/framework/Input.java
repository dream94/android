package com.example.MyGame.coregame.framework;

import java.util.List;

/**
 * Created by Admin on 2015/4/7.
 */
public interface Input {
    public static class KeyEvent{
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;

        public int type;
        public int keyCode;
        public char keyChar;

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            if(type == KEY_DOWN){
                str.append("key_down, ");
            }else{
                str.append("key_up, ");
            }
            str.append(keyCode+","+keyChar);
            return str.toString();
        }
    }

    public static class TouchEvent{
        public static final int TOUCH_UP = 0;
        public static final int TOUCH_DOWN = 1;
        public static final int TOUCH_DRAGGED = 2;

        public int type;
        public int x, y;
        public int pointer;

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            if(type == TOUCH_DOWN){
                str.append("touch_down, ");
            }else if(type == TOUCH_UP){
                str.append("touch_up, ");
            }else{
                str.append("touch_dragged, ");
            }
            str.append(pointer+","+x+","+y);
            return str.toString();
        }
    }

    public boolean isKeyPressed(int keyCode);
    public boolean isTouchDown();
    public int getTouchX();
    public int getTouchY();
    public float getAzimuth();         //得到方位角

    public List<KeyEvent> getKeyEvents();
    public List<TouchEvent> getTouchEvents();
}
