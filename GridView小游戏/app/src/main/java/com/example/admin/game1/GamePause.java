package com.example.admin.game1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Admin on 2015/5/31.
 */
public class GamePause extends Activity implements View.OnClickListener{
    Button resume, exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.gamepause);
        resume = (Button) findViewById(R.id.resume);
        exit = (Button) findViewById(R.id.exit1);
        resume.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resume:
                startActivity(new Intent(GamePause.this, Main.class));
                finish();
                break;
            case R.id.exit1:
                finish();
                break;
        }
    }
}
