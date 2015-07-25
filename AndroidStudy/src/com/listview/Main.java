package com.listview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidinternet.R;

public class Main extends Activity {
	private ListView listView;
	private List<Data> list;
	MyAdapter adapter;
	TextView center_text;
	SlideBar slideBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		init();
	}
	
	private void init(){
		listView = (ListView) findViewById(R.id.listview);
		center_text = (TextView) findViewById(R.id.center_text);
		slideBar = (SlideBar) findViewById(R.id.slidebar);
		slideBar.setCenterText(center_text);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View headerView = inflater.inflate(R.layout.search_header, null);
		listView.addHeaderView(headerView);
		initData();
		adapter = new MyAdapter(this, list);
		listView.setAdapter(adapter);
		slideBar.setListView(listView);
		
	}
	
	private void initData(){
		list = new ArrayList<Data>();
		for(int t=0; t<4; ++t){
			list.add(new Data('A', "title "+t, R.drawable.ic_launcher));
		}
		for(int t=0; t<3; ++t){
			list.add(new Data('C', "title "+t, R.drawable.ic_launcher));
		}
		for(int t=0; t<2; ++t){
			list.add(new Data('R', "title "+t, R.drawable.ic_launcher));
		}
		for(int t=0; t<4; ++t){
			list.add(new Data('Z', "title "+t, R.drawable.ic_launcher));
		}
	}
	
}
