package com.example.admin.game1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Admin on 2015/5/31.
 */
public class GameOver extends Activity{
    Button replay, exit;
    TextView result, maxscore;
    int max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.gameover);
        replay = (Button) findViewById(R.id.replay);
        exit = (Button) findViewById(R.id.exit2);
        result = (TextView) findViewById(R.id.result);
        maxscore = (TextView) findViewById(R.id.maxscore);
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        result.setText("得分:"+score);

        SharedPreferences preferences = getSharedPreferences("game1", Context.MODE_PRIVATE);
        max = preferences.getInt("maxscore", 0);
        if(score>max){
            max = score;
        }
        maxscore.setText("最高分:"+max);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOver.this, Main.class));
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("game1", Context.MODE_PRIVATE);       //重新设置初始化数据
        preferences.edit().putInt("count", 2).putInt("pos", -1).putInt("maxscore", max).commit();
        Application.red = 100;
        Application.green = 100;
        Application.blue = 100;
        Application.score = 0;
        Application.time = 60;
    }
}
