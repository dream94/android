package com.example.MyGame.coregame.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.example.MyGame.coregame.R;
import com.example.MyGame.coregame.framework.*;

import java.io.FileInputStream;

/**
 * Created by Admin on 2015/4/6.
 */
public abstract class AndroidGame extends Activity implements Game{
    private AndroidFastRenderView renderView;
    private Audio audio;
    private Vibration vibration;
    private FileIO fileIO;
    private Input input;
    private Graphics graphics;
    private Screen screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        //没有标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //没有电源等显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);    //设置竖屏显示

        Bitmap frameBuffer = Bitmap.createBitmap(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(), Bitmap.Config.RGB_565);
        renderView = new AndroidFastRenderView(this, frameBuffer);
        audio = new AndroidAudio(this);
        vibration = new AndroidVibration(this);
        //fileIO = new AndroidFileIO(getAssets());     
        input = new AndroidInput(this, renderView);
        graphics = new AndroidGraphics(frameBuffer);
        screen = getStartScreen();

        setContentView(renderView);
        Log.i("huang", "onCreate()");
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public Vibration getVibration() {
        return vibration;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public void setScreen(Screen screen) {
        if(screen == null){
            throw new IllegalArgumentException("Screen is null");
        }
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    protected void onResume() {
        Log.i("huang", "onResume()");
        super.onResume();
        renderView.resume();
    }

    @Override
    protected void onPause() {
        Log.i("huang", "onPause()");
        super.onPause();
        renderView.pause();
        screen.pause();
        if(isFinishing()){
            Log.i("huang", "screen finish");
            screen.dispose();
        }
    }
}
