package com.example.MyGame.coregame.framework.impl;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.MyGame.coregame.framework.Input.TouchEvent;
import com.example.MyGame.coregame.framework.TouchHandler;
import com.example.MyGame.coregame.framework.impl.Pool.PoolObjectFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/4/7.
 */
public class MutiTouchHandler implements TouchHandler {
    private boolean[] isTouched = new boolean[20];
    private int[] touchX = new int[20];
    private int[] touchY = new int[20];
    private Pool<TouchEvent> touchEventPool;
    private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();

    public MutiTouchHandler(View view) {
        PoolObjectFactory factory = new PoolObjectFactory() {
            @Override
            public Object createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(100, factory);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("huang", "添加Touch事件");
        synchronized (this){
            int action = event.getAction() & MotionEvent.ACTION_MASK;   //ACTION_MASK = 0xff; ACTION_POINTER_ID_MASK = 0xff00
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)>>MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);
            TouchEvent touchEvent;

            switch (action){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int)event.getX(pointerIndex);
                    touchEvent.y = touchY[pointerId] = (int)event.getY(pointerIndex);
                    isTouched[pointerId] = true;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int)event.getX(pointerIndex);
                    touchEvent.y = touchY[pointerId] = (int)event.getY(pointerIndex);
                    isTouched[pointerId] = false;
                    touchEventsBuffer.add(touchEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int pointerCount = event.getPointerCount();
                    for(int t=0; t<pointerCount; ++t){
                        pointerIndex = t;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int)event.getX(pointerIndex);
                        touchEvent.y = touchY[pointerId] = (int)event.getY(pointerIndex);
                        touchEventsBuffer.add(touchEvent);
                    }
            }
            return true;
        }
    }

    @Override
    public boolean isTouchDown() {
        synchronized (this){
            return (getMaxPointer()>=0);
        }
    }

    @Override
    public int getTouchX() {
        synchronized (this){
            int pointer = getMaxPointer();
            if(pointer<0){
                return 0;
            }else{
                return touchX[pointer];
            }
        }
    }

    @Override
    public int getTouchY() {
        synchronized (this){
            int pointer = getMaxPointer();
            if(pointer<0){
                return 0;
            }else{
                return touchY[pointer];
            }
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this){
            int len = touchEvents.size();
            for(int t=0; t<len; ++t){
                touchEventPool.addfreeObject(touchEvents.get(t));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    private int getMaxPointer(){
        for(int t=19; t>=0; --t){
            if(isTouched[t]){
                return t;
            }
        }
        return -1;
    }

}
