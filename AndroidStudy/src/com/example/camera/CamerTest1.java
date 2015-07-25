package com.example.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidinternet.R;

public class CamerTest1 extends Activity {
	ImageView image;
	Button btn1, btn2;
	private Uri fileUri;
	public static final String IMAGE_UNSPECIFIED = "image/*";  
	public static final int MEDIA_TYPE_IMAGE = 1;
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
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Log.e("huang", "fileUri: "+fileUri.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, 100); 
				break;
			case R.id.photo:
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent1, 200);  
				break;
			}
		}
	};
	
	private static Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
    	File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
    	return file;
        /*File mediaStorageDir = null;
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        
        try
        {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MyCameraApp");

            Log.e("huang", "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("huang", "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.e("huang",
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile = null;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        return mediaFile;*/
    }

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 200 && resultCode == RESULT_OK){
			 Log.e("huang", "RESULT_OK");
			 if (data != null)
             {
                
                 // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
                 // Image captured and saved to fileUri specified in the
                 // Intent
                 Toast.makeText(this, "Image saved to:\n" + data.getData(),
                         Toast.LENGTH_LONG).show();
                 Uri uri = data.getData();
                 try {
					InputStream in = getContentResolver().openInputStream(uri);
					int width = image.getWidth();
					int height = image.getHeight();
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(in, null, option);
					int w = option.outWidth;
					int h = option.outHeight;
					Log.e("huang", "width:"+width+" height:"+height+" w:"+w+", h:"+h);
					int scaleFactor = Math.min(width/w, height/h);
					if(scaleFactor<1){
						scaleFactor = 1;
					}
					option.inSampleSize = 1;
					option.inJustDecodeBounds = false;
					option.inPurgeable = true;
					in = getContentResolver().openInputStream(uri);
					Bitmap bitmap = BitmapFactory.decodeStream(in, null, option);
					image.setImageBitmap(bitmap);
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 
                 
             }
             else
             {
                 Log.e("huang",
                         "data IS null, file saved on target position.");
                 // If there is no thumbnail image data, the image
                 // will have been stored in the target output URI.

                 // Resize the full image to fit in out image view.
                 int width = image.getWidth();
                 int height = image.getHeight();
                 Log.e("huang", "width:"+width+" ,height:"+height);
                 BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

                 factoryOptions.inJustDecodeBounds = true;
                 BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
                 
                 int imageWidth = factoryOptions.outWidth;
                 int imageHeight = factoryOptions.outHeight;

                 // Determine how much to scale down the image
                 int scaleFactor = Math.min(imageWidth / width, imageHeight
                         / height);
                 Log.e("huang", "scaleFactor:"+scaleFactor);
                 // Decode the image file into a Bitmap sized to fill the
                 // View
                 factoryOptions.inJustDecodeBounds = false;
                 factoryOptions.inSampleSize = scaleFactor;
                 factoryOptions.inPurgeable = true;

                 Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                         factoryOptions);

                 image.setImageBitmap(bitmap);
             } 
         }
	
		
	}
	
	
	
	/**
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == RESULT_OK){
			int width = headimage.getWidth();
			int height = headimage.getHeight();
			if(fileUri != null){
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(fileUri.getPath(), opts);   //file:///      file协议
				int w = opts.outWidth;
				int h = opts.outHeight;
				int factor;
				if(w>width && h>height){                
					factor = Math.min(w/width, h/height);       //按照一定比例缩小
				} else {
					factor = 1;
				}
				opts.inJustDecodeBounds = false;
				opts.inPurgeable = true;
				Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath(), opts);
				//headimage.setImageBitmap(bm);
				File file = new File(fileUri.getPath());
				if(file != null){
					Log.e("take photo", "file length:"+file.length()+" file path:"+fileUri.getPath());
				} else {
					Log.e("take photo", "file null");
				}
				uploadHeadImg(file);
			}
			
		} else if(requestCode == 200 && resultCode == RESULT_OK){
			Uri uri = data.getData();
			int width = headimage.getWidth();
			int height = headimage.getHeight();
			try {
				InputStream in = getContentResolver().openInputStream(uri);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, opts);
				int w = opts.outWidth;
				int h = opts.outHeight;
				int factor;
				if(w>width && h>height){                
					factor = Math.min(w/width, h/height);       //按照一定比例缩小
				} else {
					factor = 1;
				}
				opts.inJustDecodeBounds = false;
				opts.inPurgeable = true;
				in = getContentResolver().openInputStream(uri);        //需要在获取一次，前面的流已经改变了
				
				File file = new File(fileUri.getPath());
				FileOutputStream out = new FileOutputStream(file);
				byte []b = new byte[1024];
				int len;
				try {
					while((len=in.read(b))!=-1){
						out.write(b, 0, len);
					}
					out.flush();
					out.close();
					if(file != null){
						Log.e("take photo", "file length:"+file.length()+" file path:"+fileUri.getPath());
					} else {
						Log.e("take photo", "file null");
					}
					uploadHeadImg(file);	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//Bitmap bm = BitmapFactory.decodeStream(in, null, opts);
				//headimage.setImageBitmap(bm);
				//uploadHeadImg(in);
				
				//File file = new File(fileUri.getPath())
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		popupWindow_photo.dismiss();
	}
	 */
	
}	
