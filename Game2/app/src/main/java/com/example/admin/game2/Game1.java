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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 第一关游戏
 * 游戏简介:记住出现的颜色顺序
 * Created by Admin on 2015/6/2.
 */
public class Game1 extends Activity {
    TextView title, timetext, colorbox, countnum;
    LinearLayout select_challenge, play, finalframe, content;
    Timer timer, timer1;
    Button btn1, btn2, btn3, btn4, challenge, see, next, exit;
    int time;      //剩余时间
    ArrayList result;    //存放颜色出现的顺序结果
    Handler handler;
    int count = 0, total = 5;       //表示一共5种颜色出现
    int pass = 0;
    int temp;         //temp随机产生的数字
    boolean check = true;
	int check11 = 1;

    public int state = 0;        //当前所处的状态
    //游戏状态
    public static final int GAME_WATCH = 0;      //观看状态
    public static final int GAME_SELECT = 1;     //选择挑战界面
    public static final int GAME_NEXT = 2;       //选择下一关界面或者挑战失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game1);
        title = (TextView) findViewById(R.id.Gametitle);
        title.setText("第一关");

        timetext = (TextView) findViewById(R.id.time);
        colorbox = (TextView) findViewById(R.id.colorbox);
        countnum = (TextView) findViewById(R.id.countnum);
        btn1 = (Button) findViewById(R.id.color1);
        btn2 = (Button) findViewById(R.id.color2);
        btn3 = (Button) findViewById(R.id.color3);
        btn4 = (Button) findViewById(R.id.color4);
        btn1.setOnClickListener(new ButtonOnClickListener());
        btn2.setOnClickListener(new ButtonOnClickListener());
        btn3.setOnClickListener(new ButtonOnClickListener());
        btn4.setOnClickListener(new ButtonOnClickListener());
        challenge = (Button) findViewById(R.id.challenge);
        challenge.setOnClickListener(new ButtonOnClickListener());
        see = (Button) findViewById(R.id.see);
        see.setOnClickListener(new ButtonOnClickListener());
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new ButtonOnClickListener());
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new ButtonOnClickListener());
        next.setText("next");       //默认为next
        //界面
        select_challenge = (LinearLayout) findViewById(R.id.select_challenge);   //选择挑战界面
        play = (LinearLayout) findViewById(R.id.play);   //挑战开始界面
        finalframe = (LinearLayout) findViewById(R.id.finalframe);
        content = (LinearLayout) findViewById(R.id.content);

        time = Application.time;
        result = new ArrayList();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:              //更新时间和颜色以及出现的颜色数目
                        timetext.setText(time + "");
                        countnum.setText(pass+"");
                        colorbox.setBackgroundColor(getResources().getColor(Application.color[temp]));
                        break;
                    case 1:          //打开选择界面
                        pass = 0;
                        content.setVisibility(View.GONE);
                        select_challenge.setVisibility(View.VISIBLE);      //选择项界面设置为可见
                        play.setVisibility(View.GONE);
                        finalframe.setVisibility(View.GONE);
                        colorbox.setVisibility(View.GONE);
                        timetext.setText(time + "");
                        countnum.setText("1");
                        break;
                    case 2:           //更新时间
                        timetext.setText(time + "");
                        break;
                    case 3:
                        finalframe.setVisibility(View.VISIBLE);
                        content.setVisibility(View.GONE);
                        play.setVisibility(View.GONE);
                        select_challenge.setVisibility(View.GONE);
                        break;
                }
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                switch (state){
                    case GAME_WATCH:
                        if(count < total){
                            temp = (int)(Math.random()*4);       //出现颜色的序号
                            result.add(temp);
                            count++;
                            pass++;
                            handler.sendEmptyMessage(0);
                        }else{
                            handler.sendEmptyMessage(1);
                            state = GAME_SELECT;     //跳到选择界面
                        }
                        break;
                    case GAME_SELECT:
                        handler.sendEmptyMessage(2);
                        break;
                    case GAME_NEXT:
                        handler.sendEmptyMessage(3);
                        timer.cancel();
                        timer = null;
                }
                if(time>0){
                    time--;
                }else {          //时间到了
                    state = GAME_NEXT;
                    next.setText("replay");
                }
            }
        };

        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    class ButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int select = -1;       //如果选择是颜色按钮的话则会被赋值
            switch (v.getId()){
                case R.id.challenge:
                    challengeStart();
                    break;
                case R.id.see:
                    resee();
                    break;
                case R.id.color1:
                    select = 0;
                    break;
                case R.id.color2:
                    select = 1;
                    break;
                case R.id.color3:
                    select = 2;
                    break;
                case R.id.color4:
                    select = 3;
                    break;
                case R.id.next:
                    if(next.getText().equals("next")){
                        Application.time = time;
                        startActivity(new Intent(Game1.this, Game2.class));
                        finish();
                    }else{
                        Application.time = 60;
                        startActivity(new Intent(Game1.this, Game1.class));
                        finish();
                    }
                    break;
                case R.id.exit:
                    showexitDialog();
                    break;
            }
            if(select != -1){       //说明点击了颜色按钮
                if(select != result.get(pass)){
                    Toast.makeText(Game1.this, "颜色错了", 2000).show();
                    handler.sendEmptyMessage(1);    //重新回到选择界面
                }else{
                    if(pass == count-1){
                        state = GAME_NEXT;
                    }
                    Toast.makeText(Game1.this, "答对了", 1000).show();
                    countnum.setText((pass+1) + "");
                    pass++;
                }
            }
        }
    }

    private void challengeStart(){         //开始挑战
        content.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        select_challenge.setVisibility(View.GONE);
        finalframe.setVisibility(View.GONE);
        pass = 0;
    }

    private void resee(){
        content.setVisibility(View.VISIBLE);
        select_challenge.setVisibility(View.GONE);
        finalframe.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        colorbox.setVisibility(View.VISIBLE);
        pass = 0;
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pass < count) {        //count为5
                    temp = (int) result.get(pass);
                    pass++;
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                    timer1.cancel();
                }
            }
        }, 0, 1000);
    }

    private void showexitDialog(){
        new AlertDialog.Builder(Game1.this).setTitle("确定退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if(timer!=null){
            timer.cancel();
        }*/
    }
}
