package com.example.androidinternet;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GetData extends Thread {
	Handler handler;
	String url;
	public GetData(Handler handler, String url) {
		this.handler = handler;
		this.url = url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		HttpGet httpRequest = new HttpGet(url);// 建立http get联机
		HttpResponse httpResponse;
		String result = null;
		try {
		
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			    result = EntityUtils.toString(httpResponse.getEntity(), "GBK");  //获取相应的字符串
			 try {  
			        JSONTokener jsonParser = new JSONTokener(result);  
			        JSONObject data = (JSONObject) jsonParser.nextValue();  
			        
			        JSONArray dataArray = data.getJSONArray("data");  
			        /*for(int t=0; t<dataArray.length(); ++t){
			        	JSONObject obj = (JSONObject) dataArray.get(t);
			        	String s1 = (String) obj.get("data");
			        	String s2 = (String) obj.get("content");
			        	String s3 = obj.getString("img");
			        	Log.e("huang", s1+" "+s2+" "+s3);
			        }*/		        
			        JSONObject obj = (JSONObject) dataArray.get(0);
		        	String s1 = (String) obj.get("data");
		        	String s2 = (String) obj.get("content");
		        	String s3 = obj.getString("img");
		        	
		        	//获取图片
		        	HttpGet get = new HttpGet(s3);
		        	HttpResponse res = new DefaultHttpClient().execute(get);
		        	InputStream in = res.getEntity().getContent();
		        	Bitmap img = BitmapFactory.decodeStream(in);
		        	
		        	Message msg = new Message();
		        	Bundle bundle = new Bundle();
		        	bundle.putString("s1", s1);
		        	bundle.putString("s2", s2);
		        	msg.obj = img;
		        	msg.setData(bundle);
		        	handler.sendMessage(msg);
		        	
			    } catch (JSONException ex) {  
			        // 异常处理代码  
			    }  
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 发出http请求		
	}
}
