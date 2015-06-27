package com.example.admin.game2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 第二关游戏
 * 游戏简介:在一个7*7的方格画出匹配的词语(序号为0~48)
 * Created by Admin on 2015/6/2.
 */
public class Game2 extends Activity implements View.OnClickListener{
    private TextView []text = new TextView[4];
    private Game2SurfaceView surfaceView;
    private TextView title, timetext;
    LinearLayout content, finalframe;
    Button replay, exit;
    Timer timer;
    Handler handler;
    int time;
    int []textId = {R.id.text1, R.id.text2, R.id.text3, R.id.text4};    //文本资源
    public static ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getString();      //获得四个词语
        setContentView(R.layout.game2);
        time = Application.time;
        surfaceView = (Game2SurfaceView) findViewById(R.id.surfaceView);
        finalframe = (LinearLayout) findViewById(R.id.finalframe1);
        content = (LinearLayout) findViewById(R.id.content1);
        replay = (Button) findViewById(R.id.replay);
        exit = (Button) findViewById(R.id.exit);
        title = (TextView) findViewById(R.id.Gametitle);
        title.setText("第二关");
        replay.setOnClickListener(this);
        exit.setOnClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        timetext.setText(time+"");
                        break;
                    case 2:      //游戏结束
                        timer.cancel();
                        timer = null;
                        surfaceView.check = false;
                        Application.time = 60;
                        finalframe.setVisibility(View.VISIBLE);
                        content.setVisibility(View.GONE);
                        break;
                    case 3:
                        timer.cancel();
                        timer = null;
                        startActivity(new Intent(Game2.this, Rate.class));
                        finish();
                }
            }
        };
        init();
        Log.i("huang", "Game2:"+this.toString());
    }

    /**
     * 初始化
     */
    private void init(){

        for(int t=0; t<4; ++t){
            text[t] = (TextView) findViewById(textId[t]);
            text[t].setText(list.get(t));
        }
        timetext = (TextView) findViewById(R.id.time);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(time>0){
                    handler.sendEmptyMessage(1);
                    if(!surfaceView.check){
                        handler.sendEmptyMessage(3);      //成功晋级
                    }
                }else{
                    handler.sendEmptyMessage(2);
                }
                time--;
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    //获得四个词语
    private void getString(){
        list = new ArrayList<String>();
        ArrayList temp = new ArrayList();
        int len = Application.data.length;
        int t=0;
        while(true){
            if(t==4){
                break;
            }
            int k = (int)(Math.random()*len);
            if(!temp.contains(k)){
                list.add(Application.data[k]);
                temp.add(k);
                t++;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.replay:
                Application.time = 60;
                startActivity(new Intent(Game2.this, Game1.class));
                finish();
                break;
            case R.id.exit:
                new AlertDialog.Builder(Game2.this).setTitle("确定退出吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
    }
    public void retry(){
        startActivity(new Intent(Game2.this, Game2.class));
        finish();
    }
}
