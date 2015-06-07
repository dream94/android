package com.example.admin.musicbox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.admin.musicbox.view.FavoriteLayout;
import com.example.admin.musicbox.view.LocalLayout;
import com.example.admin.musicbox.view.NetLayout;

import java.util.ArrayList;
import java.util.List;


public class Main extends Activity {
    private Context context;
    LayoutInflater inflater;
    ViewPager viewPager;
    List<View> views;
    ImageView album;
    ImageButton btnlocal, btnfav, btnnet, btnprevious, btnnext, btnplay;

    //三个界面
    LocalLayout localLayout;
    FavoriteLayout favoriteLayout;
    NetLayout netLayout;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        inflater = LayoutInflater.from(this);
        initViews();
    }
    private void initViews(){
        /* 中间部分view page视图容器 */
        viewPager = (ViewPager) findViewById(R.id.center_body_view_pager);
        views = new ArrayList<View>();

        localLayout = new LocalLayout(context, handler);
        /*favoriteLayout = new FavoriteLayout(context);
        netLayout = new NetLayout(context);


        views.add(favoriteLayout);
        views.add(netLayout);*/

        views.add(localLayout);

		/* 顶部的三个导航按钮 */

        btnlocal = (ImageButton) findViewById(R.id.imagemusic);
        btnfav = (ImageButton) findViewById(R.id.imagelove);
        btnnet = (ImageButton) findViewById(R.id.imagenet);
		/* 底部现实专辑图片 */
        album = (ImageView) findViewById(R.id.list_show_album);
		/* 下面三个是控制按钮 */
        btnprevious = (ImageButton) findViewById(R.id.btnPrevious_player);
        btnplay = (ImageButton) findViewById(R.id.btnPlay_player);
        btnnext = (ImageButton) findViewById(R.id.btnNext_player);
        getScreen(this);
    }

    public static int[] getScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        Log.i("huang", "density:"+outMetrics.density);
        return new int[] { (int) (outMetrics.widthPixels),
                (int) (outMetrics.heightPixels) };
        NetWorkI
    }
}
