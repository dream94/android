package com.example.camera;


import com.example.androidinternet.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;


public class SelectPopupWindow extends PopupWindow {
	Button takePhoto;
	Button selectFromAlbum;
	Button cancel;
	View menuView;
	public SelectPopupWindow(Context ctx, OnClickListener onClickListener) {
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menuView = inflater.inflate(R.layout.modify_head, null);
		takePhoto = (Button) menuView.findViewById(R.id.takephoto);
		takePhoto.setOnClickListener(onClickListener);
		selectFromAlbum = (Button) menuView.findViewById(R.id.selectfromalbum);
		selectFromAlbum.setOnClickListener(onClickListener);		
		cancel = (Button) menuView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		setContentView(menuView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);   //弹出窗体后可点击
		
		//setAnimationStyle(R.style.AnimBottom);   //设置动画
		menuView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = menuView.findViewById(R.id.top).getTop();
				Log.e("huang", " top height:"+height+" y:"+event.getY());
				int y = (int) event.getY();
				if(y<height){
					dismiss();
				}
				return true;
			}
		});
	}
}
