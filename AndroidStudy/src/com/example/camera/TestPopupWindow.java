package com.example.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidinternet.R;
import com.example.gson.TestGsonActivity;
/**
 * 实现Popup弹出窗口并实现调用系统照相机功能
 * @author dream
 *
 */
public class TestPopupWindow extends Activity {
	private SelectPopupWindow popupWindow;
	private ImageView image;
	private Button btn, btn1, btn2;
	private Uri fileUri;     
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		init();
		
	}
	
	private void testClass(){
	}
	
	private void init(){
		image = (ImageView) findViewById(R.id.image);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(onClickListener);
		btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(onClickListener);
		btn2 = (Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(onClickListener);
		
		fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")); //图片存放路径
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn:
				test();
				break;
			case R.id.btn1:
				startActivity(new Intent(TestPopupWindow.this, TestGsonActivity.class));
				break;
			case R.id.takephoto:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, 100);
				break;
			case R.id.selectfromalbum:
				Intent intent1 = new Intent(Intent.ACTION_PICK);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 200);
				break;
			case R.id.btn2:
				EditText text = (EditText)findViewById(R.id.edit);
				String s = text.getText().toString();
				Toast.makeText(TestPopupWindow.this, s, 2000).show();
				break;
				
			}
		}
	};
	
	public void test(){
		popupWindow = new SelectPopupWindow(this, onClickListener);
		popupWindow.showAtLocation(findViewById(R.id.main), 
				Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == RESULT_OK){
			int width = image.getWidth();
			int height = image.getHeight();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileUri.getPath(), opts);
			int w = opts.outWidth;
			int h = opts.outHeight;
			int factor;
			if(w>width && h>height){
				factor = Math.min(w/width, h/height);  //根据ImageView的大小按一定比例缩小图片
			}else {
				factor = 1;
			}
			opts.inSampleSize = factor;
			opts.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath(), opts);
			image.setImageBitmap(bm);
		} else if(requestCode == 200 && resultCode == RESULT_OK){
			int width = image.getWidth();
			int height = image.getHeight();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			Uri uri = data.getData();
			try {
				InputStream in = getContentResolver().openInputStream(uri);
				BitmapFactory.decodeStream(in, null, opts);
				int w = opts.outWidth;
				int h = opts.outHeight;
				int factor;
				if(w>width && h>height){
					factor = Math.min(w/width, h/height);  //根据ImageView的大小按一定比例缩小图片
				}else {
					factor = 1;
				}
				opts.inSampleSize = factor;
				opts.inJustDecodeBounds = false;
				in = getContentResolver().openInputStream(uri);   //需要再次获取，因为前面流已经改变了
				Bitmap bm = BitmapFactory.decodeStream(in, null, opts);
				image.setImageBitmap(bm);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	
	
}
