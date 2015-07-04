package com.example.admin.game1;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 全局变量
 * Created by Admin on 2015/5/31.
 */
public class Application {
    public static final String TAG = "huang";         //标记
    public static int width;    //屏幕宽度
    public static int height;   //屏幕高度
    public static int red = 100;      //用来保存数据
    public static int green = 100;
    public static int blue = 100;
    public static int score = 0;
    public static int time = 60;
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void clearActivity(){
        for(int t=0; t<activities.size(); ++t){
            Activity temp = activities.get(t);
            temp.finish();
        }
    }
 }
