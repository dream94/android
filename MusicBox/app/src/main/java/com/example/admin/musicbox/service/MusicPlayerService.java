package com.example.admin.musicbox.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.TelephonyManager;

/**
 * 音乐播放器服务类
 * Created by Admin on 2015/5/26.
 */
public class MusicPlayerService extends Service {
    private MediaPlayer mediaPlayer;




    //手机电话状态的广播监听
    public class PhoneStateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            boolean isPause = false;
            switch(telephonyManager.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:       //有来电
                case TelephonyManager.CALL_STATE_OFFHOOK:       //通话中
                    if(mediaPlayer != null && mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        isPause = true;
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:      //通话结束
                    if(mediaPlayer != null && isPause){
                        isPause = false;
                        mediaPlayer.start();
                    }
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void test(){

    }
}
