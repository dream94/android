package com.example.gson;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * GSON解析JSON数据
 * @author dream
 *
 */
public class TestGsonActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*
		//解析josn数据转换成相应对象
		String jsonData = "{id:1, name=\"zhangsan\", sex=\"男\"}";
		Person p = new Gson().fromJson(jsonData, Person.class);   //
		TextView text = new TextView(this);
		text.setText(p.getId()+" "+ p.getName()+ "　"+p.getSex());
		*/
		
		//解析json数据转换成list
		String jsonData = "[{id:1, name=\"zhangsan\", sex=\"男\"}," +
				"{id:2, name=\"lisi\", sex=\"男\"}]";

		List<Person> list = new Gson().fromJson(jsonData,
				new TypeToken<List<Person>>(){}.getType());
		StringBuilder sd = new StringBuilder();
		for(int t=0; t<list.size(); ++t){
			sd.append(list.get(t));
		}
		TextView text = new TextView(this);
		text.setText(sd.toString());
		setContentView(text);
	}
}
