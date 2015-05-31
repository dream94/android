package com.example.MyGame.coregame.game;

import android.graphics.PointF;

/**
 * Created by Admin on 2015/4/7.
 */
public class VectorF extends PointF {
    public VectorF(float x, float y) {
        super(x, y);
    }
    public VectorF add(VectorF vectorF){
        return new VectorF(x+vectorF.x, y+vectorF.y);
    }
    public VectorF sub(VectorF vectorF){
        return new VectorF(x-vectorF.x, y-vectorF.y);
    }
    public VectorF mutiply(float k){
        return new VectorF(x*k, y*k);
    }
    public VectorF divide(float k){
        return mutiply(1.0F/k);
    }

    public void addThis(VectorF vectorF){
        x += vectorF.x;
        y += vectorF.y;
    }
    public void subThis(VectorF vectorF){
        x -= vectorF.x;
        y -= vectorF.y;
    }
    public void multiplyThis(float k){
        x *= k;
        y *= k;
    }
    public void divideThis(float k){
        multiplyThis(1.0F/k);
    }

}
