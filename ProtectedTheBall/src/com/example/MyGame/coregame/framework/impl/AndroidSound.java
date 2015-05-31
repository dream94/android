package com.example.MyGame.coregame.framework.impl;

import android.media.SoundPool;
import com.example.MyGame.coregame.framework.Sound;

/**
 * Created by Admin on 2015/4/6.
 */
public class AndroidSound implements Sound {
    private int soundId;
    private SoundPool soundPool;

    public AndroidSound(int soundId, SoundPool soundPool) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume) {        //播放声音
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {              //卸载声音
        soundPool.unload(soundId);
    }
}
