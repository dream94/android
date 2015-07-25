package com.example.androidinternet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
	Handler handler;
	TextView text, text1;
	ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);
		img = (ImageView) findViewById(R.id.img);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle b = msg.getData();
				Log.e("huang", b.getString("s1")+" "+b.getString("s2"));
				text.setText(b.getString("s1"));
				text1.setText(b.getString("s2"));
				img.setImageBitmap((Bitmap) msg.obj);
			}
		};
		new GetData(handler, "http://7xiwtn.com1.z0.glb.clouddn.com/data.jason.txt").start();
		//new PostData("http://hebapi.xingguangju.com/HongEBao/User/GetSMSRegisterCode").start();
	}
	

}
