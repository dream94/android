package com.example.MyGame.coregame.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import com.example.MyGame.coregame.framework.Vibration;

/**
 * Created by Admin on 2015/4/7.
 */
public class AndroidVibration implements Vibration {
    private Vibrator vibrator;

    public AndroidVibration(Context context){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    @Override
    public void vibrate(int time) {
        vibrator.vibrate((long)time);
    }
}
