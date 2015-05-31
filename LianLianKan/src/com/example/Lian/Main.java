package com.example.Lian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements View.OnClickListener{
    public static final String Tag = "huang";
    Button start, exit, resume;
    TextView title;

    //游戏的4种状态
    public static final int STATE_GAME = 1;
    public static final int STATE_WIN = 2;
    public static final int STATE_LOSE = 3;
    public static final int STATE_PAUSE = 4;

    //用来表示游戏现在处于什么状态
    public static int state = STATE_GAME;

    public static int iconCount = 8;           //设置图片为8中
    Bitmap icons[] = new Bitmap[iconCount];
    static Controller controller = null;


    public static int screenWidth;
    public static int screenHeight;

    public Bitmap[] getIcons() {
        return icons;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);        //使d获得设备相关信息
        screenWidth = d.widthPixels;
        screenHeight = d.heightPixels;
        loadMenu();                                //根据游戏不同状态显示不同的菜单项
    }

    public void loadMenu(){
        if(controller == null){
            controller = new Controller();
            Log.i(Tag, "controller is null");
        }
        loadIcons();
        setContentView(R.layout.main);
        start = (Button) findViewById(R.id.start);
        exit = (Button) findViewById(R.id.exit);
        title = (TextView) findViewById(R.id.title);
        start.setOnClickListener(this);
        exit.setOnClickListener(this);
        if(state == STATE_WIN){
            title.setText("恭喜过关");
            start.setText("下一关");
        }
        if(state == STATE_LOSE){
            title.setText("闯关失败");
            start.setText("重试");
        }
        if(state == STATE_PAUSE){           //游戏处于暂停状态时显示
            title.setText("新的游戏");
            resume = (Button) findViewById(R.id.resume);
            resume.setVisibility(View.VISIBLE);
            resume.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == start){
            state = STATE_GAME;
            setContentView(R.layout.game);
            if(start.getText().equals("下一关")||start.getText().equals("重试")){
                controller.startGame(Main.this, 1);
            }else{
                controller.startGame(this, 0);        //开始
            }
        }else if( v == resume){              //继续
            state = STATE_GAME;
            setContentView(R.layout.game);
            controller.resume(this);
        }else if(v == exit){
           showExit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(state == STATE_GAME){          //处于游戏状态下才生效
           switch (keyCode){
               case KeyEvent.KEYCODE_DPAD_CENTER:          //?
                   Log.i(Tag, "KEYCODE_DPAD_CENTER");
                   break;
               case KeyEvent.KEYCODE_DPAD_LEFT:          //?
                   Log.i(Tag, "KEYCODE_DPAD_LEFT");
                   break;
               case KeyEvent.KEYCODE_BACK:                  //返回键
                   showExit();
                   break;
           }
       }
        return false;
    }

    public void showExit(){
        new AlertDialog.Builder(this).setTitle("提醒").setMessage("确定退出吗?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(state == STATE_GAME){
            state = STATE_PAUSE;
            controller.pause();
        }
    }

    //创建菜单项，手机第3个键
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
        menu.add(1, 1, 1, "暂停");
        menu.add(1, 2, 2, "帮助");
        menu.add(1, 3, 3, "退出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case 1:
                pause();
                break;
            case 2:
                controller.autoErase();            //帮助功能，自动清除
                break;
            case 3:
                showExit();
                break;
        }
        return true;
    }
    public void pause(){
        //controller.pause();
        state = STATE_PAUSE;
        loadMenu();
    }

    public void setState(int sta){
        if(sta == STATE_LOSE||sta == STATE_WIN){
            loadMenu();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(Tag, "onConfig change");
    }

    public void loadIcons(){
        Resources r = getResources();
        icons[0] = ((BitmapDrawable)r.getDrawable(R.drawable.i1)).getBitmap();
        icons[1] = ((BitmapDrawable)r.getDrawable(R.drawable.i2)).getBitmap();
        icons[2] = ((BitmapDrawable)r.getDrawable(R.drawable.i3)).getBitmap();
        icons[3] = ((BitmapDrawable)r.getDrawable(R.drawable.i4)).getBitmap();
        icons[4] = ((BitmapDrawable)r.getDrawable(R.drawable.i5)).getBitmap();
        icons[5] = ((BitmapDrawable)r.getDrawable(R.drawable.i6)).getBitmap();
        icons[6] = ((BitmapDrawable)r.getDrawable(R.drawable.i7)).getBitmap();
        icons[7] = ((BitmapDrawable)r.getDrawable(R.drawable.i8)).getBitmap();/*
        icons[8] = ((BitmapDrawable)r.getDrawable(R.drawable.i9)).getBitmap();
        icons[9] = ((BitmapDrawable)r.getDrawable(R.drawable.i10)).getBitmap();
        icons[10] = ((BitmapDrawable)r.getDrawable(R.drawable.i11)).getBitmap();
        icons[11] = ((BitmapDrawable)r.getDrawable(R.drawable.i12)).getBitmap();
        icons[12] = ((BitmapDrawable)r.getDrawable(R.drawable.i13)).getBitmap();
        icons[13] = ((BitmapDrawable)r.getDrawable(R.drawable.i14)).getBitmap();
        icons[14] = ((BitmapDrawable)r.getDrawable(R.drawable.i15)).getBitmap();
        icons[15] = ((BitmapDrawable)r.getDrawable(R.drawable.i16)).getBitmap();
        icons[16] = ((BitmapDrawable)r.getDrawable(R.drawable.i17)).getBitmap();
        icons[17] = ((BitmapDrawable)r.getDrawable(R.drawable.i18)).getBitmap();
        icons[18] = ((BitmapDrawable)r.getDrawable(R.drawable.i19)).getBitmap();
        icons[19] = ((BitmapDrawable)r.getDrawable(R.drawable.i20)).getBitmap();
        icons[20] = ((BitmapDrawable)r.getDrawable(R.drawable.i21)).getBitmap();
        icons[21] = ((BitmapDrawable)r.getDrawable(R.drawable.i22)).getBitmap();
        icons[22] = ((BitmapDrawable)r.getDrawable(R.drawable.i23)).getBitmap();
        icons[23] = ((BitmapDrawable)r.getDrawable(R.drawable.i24)).getBitmap();*/
    }
}
