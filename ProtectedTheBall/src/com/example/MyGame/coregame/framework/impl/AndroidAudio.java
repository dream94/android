package com.example.MyGame.coregame.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import com.example.MyGame.coregame.framework.Audio;
import com.example.MyGame.coregame.framework.Sound;

import java.io.IOException;

/**
 * Created by Admin on 2015/4/6.
 */
public class AndroidAudio implements Audio {
    private AssetManager assetManager;
    private SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        assetManager = activity.getAssets();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Sound newSound(String filename) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new AndroidSound(soundId, soundPool);
        } catch (IOException e) {
            Log.i("huang", "Could not load newSound");
        }
        return null;
    }
}
