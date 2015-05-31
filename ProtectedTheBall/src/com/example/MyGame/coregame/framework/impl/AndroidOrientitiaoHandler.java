package com.example.MyGame.coregame.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.MyGame.coregame.framework.Orientation;

/**
 * Created by Admin on 2015/4/7.
 */
public class AndroidOrientitiaoHandler implements Orientation, SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private float azimuth = 0.0F;    //方向角的弧度

    public AndroidOrientitiaoHandler(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);   //参数为加速度传感器
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);  //第三个参数是灵敏度
    }

    @Override
    public float getAzimuth() {
        return azimuth;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        azimuth = (float) (Math.atan2((double) event.values[1], (double) event.values[0]) / Math.PI  * 180);   //计算出来弧度(y,x),同时转换为角度
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
