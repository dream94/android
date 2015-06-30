package com.example.admin.game1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Activity {
    private int time;
    private int score;
    Timer timer;        //计时器
    Button pause;
    GridView gridView;
    TextView scoretext, timetext;
    Handler handler;
    int red, green, blue;        //三原色的值(0-255)
    int pos;                    //不同颜色所处的位置（相比其他格子）
    int count;
    int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        pause = (Button) findViewById(R.id.pause);
        scoretext = (TextView) findViewById(R.id.score);
        timetext = (TextView) findViewById(R.id.time);
        gridView = (GridView) findViewById(R.id.gridview);
        height = gridView.getHeight();
        init();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    Intent intent = new Intent(Main.this, GameOver.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }else if(msg.what == 1){
                    scoretext.setText("得分:"+score);
                    timetext.setText("时间:"+time);
                }
            }
        };
        gridView.setAdapter(new GridViewAdapter(this));
        gridView.setNumColumns(count);      //设置列数


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == pos) {       //选中目标(颜色不一样的格子)
                    score++;
                    handler.sendEmptyMessage(1);
                    red = (int) (230 * Math.random()) + 25;      //更新
                    green = (int) (230 * Math.random()) + 25;
                    blue = (int) (230 * Math.random()) + 25;
                    count++;
                    if (count > 8) {
                        count = 2;
                    }
                    pos = (int) (Math.random() * count * count);
                    gridView.setNumColumns(count);
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, GamePause.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TimerTask timerTask = new TimerTask() {        //时间任务
            @Override
            public void run() {
                Log.i(Application.TAG, "time");
                if(time>0){
                    time--;
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(0);       //游戏结束
                }
            }

        };
        timer = new Timer();    //防止Activity处于Stop状态后调用onRestart重启，而onCreate在整个Activity生命周期中只调用一次
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 初始化一些值
     */
    private void init(){
        red = Application.red;
        green = Application.green;
        blue = Application.blue;
        score = Application.score;
        time = Application.time;
        SharedPreferences preferences = getSharedPreferences("game1", Context.MODE_PRIVATE);
        count = preferences.getInt("count", 2);    //如果没有存的话，默认为2
        pos = preferences.getInt("pos", -1);     //如果是-1的话，则说明是第一次
        if(pos == -1){
            pos = (int)(Math.random()*count*count);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        storedata();
    }

    /**
     * 游戏界面消失，保存一些数据
     */
    private void storedata(){
        SharedPreferences preferences = getSharedPreferences("game1", Context.MODE_PRIVATE);
        preferences.edit().putInt("count", count).putInt("pos", pos).commit();
        Application.red = red;
        Application.green = green;
        Application.blue = blue;
        Application.score = score;
        Application.time = time;
        Log.i("huang", "store data");
    }

    class GridViewAdapter extends BaseAdapter{
        Context context;
        public GridViewAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return count * count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            TextView textView = new TextView(context);
            int h = (Application.height - 150)/count;
            textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, h));
            if(position == pos){       //不一样颜色的格子
                textView.setBackgroundColor(Color.rgb(red+(int)(Math.random()*20),
                        green+(int)(Math.random()*20), blue+(int)(Math.random()*20)));
            }else {
                textView.setBackgroundColor(Color.rgb(red, green, blue));
            }
            return textView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences("game1", Context.MODE_PRIVATE);       //重新设置初始化数据
        preferences.edit().putInt("count", 2).putInt("pos", -1).commit();
        Application.red = 100;
        Application.green = 100;
        Application.blue = 100;
        Application.score = 0;
        Application.time = 60;
    }
}
