package com.listview;

import android.content.Context;
import android.drm.DrmStore.Action;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.androidinternet.R;

public class SlideBar extends View{
	TextView center;
	SectionIndexer indexer;
	ListView list;
	char[] letter;
	Bitmap bm;
	private int color = 0xff858c94;
	
	public SlideBar(Context ctx){
		super(ctx);
		init();
	}
	public SlideBar(Context ctx, AttributeSet attr){
		super(ctx, attr);
		init();
	}
	public SlideBar(Context ctx, AttributeSet attr, int defStyle){
		super(ctx, attr, defStyle);
		init();
	}
	
	private void init(){
		letter = new char[27];
		letter[0] = '!';
		for(int t=1; t<=26; ++t){
			letter[t] = (char) ('A'+t-1);
		}
		bm = BitmapFactory.decodeResource(getResources(), R.drawable.search_bar_icon_normal);
	}
	
	// 设置中间的提示窗
	public void setCenterText(TextView text){
		center = text;
	}
	
	public void setListView(ListView listView){
		list = listView;
		
		HeaderViewListAdapter ad = (HeaderViewListAdapter) listView.getAdapter();
		MyAdapter adater = (MyAdapter) ad.getWrappedAdapter();
		indexer = (SectionIndexer) adater;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();
		int h1 = h/letter.length;
		Log.e("huang", "w:"+w+" h:"+h);
		
		Paint paint = new Paint();
		paint.setTextSize(12);
		paint.setStyle(Style.FILL);		
		paint.setTextAlign(Align.CENTER);
		paint.setColor(color);
		for(int t=0; t<27; ++t){
			if(t==0){
				canvas.drawBitmap(bm, w/2-20, h1*t+h1/2-10, paint);
			} else {
				String temp = String.valueOf(letter[t]);
				canvas.drawText(temp, w/2-8, h1/2+h1*t, paint);
			}
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int y = (int) event.getY();
		int h = getMeasuredHeight()/letter.length;
		int num = y/h;
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			center.setVisibility(VISIBLE);
			setBackgroundResource(R.drawable.scrollbar_bg);
			if(num == 0){
				center.setTextSize(15);
				center.setText("搜索");
			} else {
				center.setTextSize(18);
				center.setText(letter[num]+"");
			}
			break;
		case MotionEvent.ACTION_UP:
			center.setVisibility(INVISIBLE);
			setBackgroundColor(Color.parseColor("#00ffffff"));
			if(num == 0){
				list.setSelection(0);
			} else {
				int pos = indexer.getSectionForPosition(letter[num]);
				if(pos != -1){
					list.setSelection(pos);
				}
			}
			break;
		}
		
		return true;
		
		
		
	}
}
