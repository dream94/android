package com.example.MyGame.coregame.framework.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Admin on 2015/4/6.
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable {

    private AndroidGame game;
    private Bitmap frameBuffer;
    private Thread renderThread;
    private SurfaceHolder holder;
    volatile static boolean running = false;

    public AndroidFastRenderView(AndroidGame game, Bitmap frameBuffer) {
        super(game);
        this.game = game;
        this.frameBuffer = frameBuffer;
        holder = getHolder();
        setKeepScreenOn(true);      //保持屏幕常亮
    }

    public void resume(){
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();         //System.nanoTime()只能用来计算时间
        while(running){
            if(!holder.getSurface().isValid()){
                continue;
            }
            float deltatime = (System.nanoTime() - startTime)/1000000000.0F;   //转化为秒级别

            game.getCurrentScreen().update(deltatime);
            game.getCurrentScreen().present(deltatime);    //修改页面内容

            startTime = System.nanoTime();
            Canvas canvas = holder.lockCanvas();           //刷新页面内容
            canvas.drawBitmap(frameBuffer, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        running = false;
        try {
            renderThread.join();
            Log.i("huang","Thread end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
