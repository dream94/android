package com.example.admin.game2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import net.youmi.android.AdManager;
import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

/**
 * 游戏初始化界面（菜单选项+有米广告）
 */
public class Main extends Activity implements View.OnClickListener{
    Button start, help, more;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        init();     //UI组件初始化以及Application一些游戏变量

        //有米广告
        AdManager.getInstance(this).init("45c3d9b38e626d72", "3411c3b340bb4e8c", false); // 初始化接口，应用启动的时候调用
        AdManager.getInstance(this).setUserDataCollect(true);       //开启用户数据统计服务
        SpotManager.getInstance(this).loadSpotAds();
        //初始化积分墙
        OffersManager.getInstance(this).onAppLaunch();
        // 插屏出现动画效果，0:ANIM_NONE为无动画，1:ANIM_SIMPLE为简单动画效果，2:ANIM_ADVANCE为高级动画效果
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIM_ADVANCE);
        // 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图。
        SpotManager.getInstance(this).setSpotOrientation(
                SpotManager.ORIENTATION_PORTRAIT);

        // 展示插播广告，可以不调用loadSpot独立使用
        SpotManager.getInstance(Main.this).showSpotAds(
                Main.this, new SpotDialogListener() {
                    @Override
                    public void onShowSuccess() {
                        Log.i("YoumiAdDemo", "展示成功");
                    }

                    @Override
                    public void onShowFailed() {
                        Log.i("YoumiAdDemo", "展示失败");
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.i("YoumiAdDemo", "展示关闭");
                    }
                });
    }
    private void init(){
        start = (Button) findViewById(R.id.start);
        help = (Button) findViewById(R.id.help);
        more = (Button) findViewById(R.id.more);
        start.setOnClickListener(this);
        help.setOnClickListener(this);
        more.setOnClickListener(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Application.width = metrics.widthPixels;
        Application.height = metrics.heightPixels;

        Application.time = 60;    //游戏总的时间

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                startActivity(new Intent(Main.this, Game1.class));
                finish();
                break;
            case R.id.help:
                startActivity(new Intent(Main.this, Help.class));
                finish();
                break;
            case R.id.more:
                Log.i("huang", "Onclicke");
                OffersManager.getInstance(this).showOffersWall(
                        new Interface_ActivityListener() {
                            /**
                             * 但积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
                             */
                            @Override
                            public void onActivityDestroy(Context context) {
                                Toast.makeText(context, "全屏积分墙退出了", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    @Override
    public void onBackPressed() {   //照样返回
        super.onBackPressed();
        Log.i("huang", "onBackPressed()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i("huang", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(this).onDestroy();     //回收资源
        OffersManager.getInstance(this).onAppExit();   //回收资源
    }
}
