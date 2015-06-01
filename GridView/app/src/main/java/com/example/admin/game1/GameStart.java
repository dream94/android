package com.example.admin.game1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Admin on 2015/5/31.
 */
public class GameStart extends Activity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.gamestart);
        btn = (Button) findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameStart.this, Main.class));
                finish();
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Application.width = metrics.widthPixels;
        Application.height = metrics.heightPixels;
        Log.i(Application.TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Application.TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Application.TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Application.TAG, "onStop");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                Log.i(Application.TAG, "back");
                showdialog();
                break;
            case KeyEvent.KEYCODE_HOME:
                Log.i(Application.TAG, "home");      //KEYCODE_HOME没有效果监测不到Home键
        }
        return false;
    }

    private void showdialog(){
        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).
                setTitle("你确定要退出吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

}
