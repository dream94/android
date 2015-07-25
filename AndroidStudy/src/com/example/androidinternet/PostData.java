package com.example.androidinternet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class PostData extends Thread {
	String url;
	public PostData(String url) {
		this.url = url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("mobileNo", "13826040804"));
		//post.addHeader("platform", "android");
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				String result = EntityUtils.toString(response.getEntity());
				Log.e("huang", result);
				
				/*JSONTokener token = new JSONTokener(result);
				JSONObject obj = (JSONObject) token.nextValue();
				JSONObject obj1 = obj.getJSONObject("result");
				boolean check = obj1.getBoolean("IsSuccess");
				Log.e("huang", "isSuccess:"+check);*/
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("huang", "error");
			e.printStackTrace();
			
		}
		
	}
}
