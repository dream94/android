package com.example.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidinternet.R;

public class CameraTest extends Activity {
	ImageView image;
	Button btn1, btn2;
	
	public static final int NONE = 0;  
    public static final int PHOTOHRAPH = 1;// 拍照  
    public static final int PHOTOZOOM = 2; // 缩放  
    public static final int PHOTORESOULT = 3;// 结果  
    public static final String IMAGE_UNSPECIFIED = "image/*";  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		image = (ImageView) findViewById(R.id.image);
		btn1 = (Button) findViewById(R.id.camera);
		btn1.setOnClickListener(onClickListener);
		btn2 = (Button) findViewById(R.id.photo);
		btn2.setOnClickListener(onClickListener);
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.camera:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));  
                startActivityForResult(intent, PHOTOHRAPH); 
				break;
			case R.id.photo:
				 Intent intent1 = new Intent(Intent.ACTION_PICK, null);  
                 intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);  
                 startActivityForResult(intent1, PHOTOZOOM);  
				break;
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("huang", "requestCode:"+requestCode+", resultCode:"+resultCode);
		 if (resultCode == NONE)  
             return;  
         // 拍照  
         if (requestCode == PHOTOHRAPH) {  
             //设置文件保存路径这里放在跟目录下  
             //File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");  
             //startPhotoZoom(Uri.fromFile(picture)); 
        			 
         }  
           
         if (data == null)  
             return;  
           
         // 读取相册缩放图片  
         if (requestCode == PHOTOZOOM) {
        	 Log.e("huang", "data.getData():"+data.getData().toString());
             startPhotoZoom(data.getData());  
         }  
         // 处理结果  
         if (requestCode == PHOTORESOULT) {  
             Bundle extras = data.getExtras();  
             if (extras != null) {  
                 Bitmap photo = extras.getParcelable("data");  
                 /*ByteArrayOutputStream stream = new ByteArrayOutputStream();  
                 photo.compress(Bitmap.CompressFormat.JPEG, 0, stream);// (0 - 100)压缩文件  
*/                 image.setImageBitmap(photo);  
             }  
         }  
   
     }  
   
     public void startPhotoZoom(Uri uri) {  
         Intent intent = new Intent("com.android.camera.action.CROP");  
         intent.setDataAndType(uri, IMAGE_UNSPECIFIED);  
         intent.putExtra("crop", "true");  
         // aspectX aspectY 是宽高的比例  
         intent.putExtra("aspectX", 1);  
         intent.putExtra("aspectY", 1);  
         // outputX outputY 是裁剪图片宽高  
         intent.putExtra("outputX", 64);  
         intent.putExtra("outputY", 64);  
         intent.putExtra("return-data", true);  
         startActivityForResult(intent, PHOTORESOULT);  
     }  
	
	
}
