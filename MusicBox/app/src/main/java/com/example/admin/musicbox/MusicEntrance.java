package com.example.admin.musicbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 音乐盒的入口Activity，播放一个动画然后就进入音乐盒主界面
 * Created by Admin on 2015/5/24.
 */
public class MusicEntrance extends Activity {
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("service", Context.MODE_PRIVATE);
        if(sp.getBoolean("openbefore", false)){         //曾经打开过
            startActivity(new Intent(MusicEntrance.this, Main.class));
            finish();
        }else{            //第一次打开
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置全屏
            setContentView(R.layout.entrance);
            image = (ImageView) findViewById(R.id.entrance_image);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo);
            image.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    try{
                        Thread.sleep(2000);
                    } catch (Exception e){

                    }
                    startActivity(new Intent(MusicEntrance.this, Main.class));
                    finish();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
