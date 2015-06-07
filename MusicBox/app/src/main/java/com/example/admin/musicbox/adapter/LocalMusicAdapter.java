package com.example.admin.musicbox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.musicbox.R;
import com.example.admin.musicbox.entry.Music;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 本地音乐适配器
 * Created by Admin on 2015/5/27.
 */
public class LocalMusicAdapter extends BaseAdapter {
    private ArrayList<Music> musics;
    private LayoutInflater inflater;
    int nowposition = -1;// 当前播放到那首歌
    int nowStus = 0;// 播放状态
    ListView listView;
    boolean ismultchose = false;
    HashMap<Integer, Music> choseinfo = new HashMap<Integer, Music>();
    public Bitmap publicbitmap;

    public LocalMusicAdapter(Context context, ArrayList<Music>musics, ListView listView){
        inflater = LayoutInflater.from(context);
        this.musics = musics;
        this.listView = listView;
        publicbitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_bg_s);
    }

    //添加一条记录
    public void addItem(Music music){
        if(music != null){
            musics.add(music);
            notifyDataSetChanged();
        }
    }
    public void refreshNowplay(int position, int status) {
        nowposition = position;
        nowStus = status;
        notifyDataSetChanged();
    }
    /**
     * 返回集合中的全部数据
     */
    public ArrayList<Music> getMusics() {
        return this.musics;
    }

    /**
     * 从集合中移除一个item，并更新ui
     *
     * @param position
     *            待移除的item的position
     */
    public void remove(int position) {
        musics.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * 向集合中追加一组数据，并更新listview
     *
     * @param musics
     */
    public void addItems(ArrayList<Music> musics) {
        if (musics != null) {
            this.musics.addAll(musics);
            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return musics.get(position).getId();         //返回选中的音乐id
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.play_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivAlbum = (ImageView) convertView.findViewById(R.id.ivAlbum);
            viewHolder.tvMusicName = (TextView) convertView.findViewById(R.id.tvMusicName);
            viewHolder.tvSinger = (TextView) convertView.findViewById(R.id.tvSinger);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Music music = musics.get(position);
        viewHolder.tvMusicName.setText(music.getMusicName());
        viewHolder.tvSinger.setText(music.getSinger());
        viewHolder.tvTime.setText(music.getTime());
        viewHolder.ivAlbum.setImageBitmap(publicbitmap);
        return convertView;
    }

    class ViewHolder {
        ImageView ivAlbum;
        TextView tvMusicName;
        TextView tvSinger;
        TextView tvTime;
    }
}
