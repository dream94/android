package com.example.admin.testfinal;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    RefreshView refreshView;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshView = (RefreshView) findViewById(R.id.refresh_view);
        listview = (ListView) findViewById(R.id.list_view);
        Log.i("huang", "Main listView id:"+listview.getId());
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for(int t=0; t<15; ++t){
            HashMap<String, Object> str = new HashMap<String, Object>();
            str.put("id", t);
            str.put("extra", t+"附带材料");
            list.add(str);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.item, new String[]{"id", "extra"},  new int[]{R.id.text1, R.id.text2});
        listview.setAdapter(simpleAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, position + "", 2000).show();
            }
        });

        refreshView.setPullToRefreshListener(new RefreshView.PullToRefreshListener() {
            @Override
            public void refresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshView.finishRefreshing();
            }
        });
    }


}
