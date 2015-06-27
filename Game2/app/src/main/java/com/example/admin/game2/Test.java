package com.example.admin.game2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 2015/6/3.
 */
public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        TextView textView = (TextView) findViewById(R.id.test);
        textView.setBackgroundColor(getResources().getColor(Application.color[1]));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("huang", 1+"");
            }
        }, 0, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("huang", 2+"");
            }
        }, 0, 1000);
    }
}
