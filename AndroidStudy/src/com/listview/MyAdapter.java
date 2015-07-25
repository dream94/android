package com.listview;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.androidinternet.R;

public class MyAdapter extends BaseAdapter implements SectionIndexer{
	private List<Data> list;
	private LayoutInflater inflater;
	
	public MyAdapter(Context ctx, List<Data> list) {
		this.list = list;
		inflater = LayoutInflater.from(ctx);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.listview_item, null);
			holder = new ViewHolder();
			holder.kind = (TextView) convertView.findViewById(R.id.kind);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position > 0){
			if(list.get(position).getKind()==(list.get(position-1).getKind())){
				holder.kind.setVisibility(View.GONE);
			}else {
				holder.kind.setVisibility(View.VISIBLE);
			}
		} else {
			holder.kind.setVisibility(View.VISIBLE);
		}
		holder.kind.setText(list.get(position).getKind()+"");
		holder.title.setText(list.get(position).getTitle());
		holder.image.setImageResource(list.get(position).getImage());
		return convertView;
	}
	
	class ViewHolder{
		ImageView image;
		TextView kind;
		TextView title;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {   //传过来和字母比较
		// TODO Auto-generated method stub
		int len = list.size();
		for(int t=0; t<len; ++t){
			if(position == list.get(t).getKind()){
				return t+1;
			}
		}
		return -1;
	}
	
	
	
	
}
