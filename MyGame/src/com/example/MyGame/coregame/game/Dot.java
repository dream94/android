package com.example.MyGame.coregame.game;

/**
 * Created by Admin on 2015/4/7.
 */
public class Dot {
    public enum Type {
        Enemy, Health, Shield;        //敌人，生命， 反护盾
    }
    public Type type;
    public float energy;       //最大值为1.0F
    public static float maxRadius;

    public VectorF speed;
    public VectorF coords;


}
