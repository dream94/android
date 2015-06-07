package com.example.admin.musicbox.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Admin on 2015/5/29.
 */
public class MyPageAdapter extends PagerAdapter {
    List<View> list;
    public MyPageAdapter(List<View> views) {
        list = views;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try{
            container.addView(list.get(position));
        } catch (Exception e){
            e.printStackTrace();
        }
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position>0){
            container.removeView(list.get(position));
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
