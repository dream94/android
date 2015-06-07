package com.example.admin.musicbox.view;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.musicbox.Main;
import com.example.admin.musicbox.MusicLayIntenface;
import com.example.admin.musicbox.R;
import com.example.admin.musicbox.adapter.LocalMusicAdapter;
import com.example.admin.musicbox.db.MusicGroupDao;
import com.example.admin.musicbox.db.MusicItemDao;
import com.example.admin.musicbox.entry.Music;
import com.example.admin.musicbox.entry.MusicData;
import com.example.admin.musicbox.entry.MusicGroup;
import com.example.admin.musicbox.entry.MusicItem;
import com.example.admin.musicbox.tools.CustomDialog;
import com.example.admin.musicbox.util.StrTime;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Admin on 2015/5/27.
 */
public class LocalLayout extends LinearLayout implements MusicLayIntenface{
    View rootview;
    ListView localistview;
    LayoutInflater inflater;
    Context context;
    MusicItemDao musicItemDao;
    LocalMusicAdapter adapter;
    Handler handler;
    public int num;
    ArrayList<Music> locallist;
    ArrayList<Integer> musicid = new ArrayList<Integer>();;
    MusicItem musicItem;

    public LocalLayout(Context context, Handler handler) {
        super(context);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.handler = handler;
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT)); //设置布局参数
        initView();
        initData();
    }

    @Override
    public void initView() {
        rootview = inflater.inflate(R.layout.musiclist, this, true);
        musicItemDao = new MusicItemDao(context);
        localistview = (ListView) rootview.findViewById(R.id.lvSounds);
    }

    @Override
    public void initData() {
        locallist = (ArrayList<Music>) MusicData.getMultiDatas(context);
        adapter = new LocalMusicAdapter(context, locallist, localistview);
        localistview.setAdapter(adapter);
    }

    private void songItemDialog(){
        String[] menustring = new String[] { "播放此音乐", "添加到分组", "删除此歌曲" , "查看该歌曲详情"};
        ListView menulist = new ListView(context);
        menulist.setCacheColorHint(Color.TRANSPARENT);
        menulist.setDividerHeight(1);
        menulist.setAdapter(new ArrayAdapter<String>(context, R.layout.dialog_menu_item, R.id.text1, menustring));
        menulist.setLayoutParams(new LayoutParams(Main.getScreen(context)[0] / 2, LayoutParams.WRAP_CONTENT));
        final CustomDialog dialog = new CustomDialog.Builder(context).setTitle("您要将文件处理为:").setView(menulist).create();
        dialog.show();
        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                dialog.dismiss();
                Music music = locallist.get(num);
                if (position == 0) {
                    playmusic(num);        //播放音乐
                } else if (position == 1) {
                    AddToGroup(context, music.getId(), false, null);
                } else if (position == 2) {// 从媒体库中删除该音乐的记录
                    LocalLayout.this.context.getContentResolver()
                            .delete(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            music.getId()), null, null);
                    // 从sdcard上删除文件
                    new File(music.getSavePath()).delete();
                    // 更新listView
                    LocalLayout.this.adapter.remove(num);
                    // 从音乐分组列表中移除音乐信息
                    musicItemDao.deleteItemByMusicid(music
                            .getId());
                }else if (position==3){
                    View view1 = inflater.inflate(R.layout.song_detail, null);
                    ((ViewGroup) rootview).removeView(view1);
                    ((TextView) view1.findViewById(R.id.tv_song_title)).setText(music.getMusicName());
                    ((TextView) view1.findViewById(R.id.tv_song_artist)).setText(music.getSinger());
                    ((TextView) view1.findViewById(R.id.tv_song_album)).setText(music.getAlbumName());
                    ((TextView) view1.findViewById(R.id.tv_song_filepath)).setText(music.getSavePath());
                    ((TextView) view1.findViewById(R.id.tv_song_duration)).setText(StrTime.getTime(music.getTime()));
                    //((TextView) view1.findViewById(R.id.tv_song_format)).setText(MainActivity.getSuffix(music.getSavePath()));
                    //((TextView) view1.findViewById(R.id.tv_song_size)).setText(music.getSerialversionuid() + "MB");
                    new CustomDialog.Builder(context).setTitle("歌曲详细信息:").setNeutralButton("yes", null).setView(view1).create().show();
                }

            }
        });
    }

    private void playmusic(int num){
        Intent intent = new Intent("com.music.play");
        intent.putExtra("position", num);
        //context.sendBroadcast(intent);
    }

    private void AddToGroup(final Context context, final int musicId,
                            final boolean ismult, final ArrayList<Integer> ids) {
        // 查询所有的musicgroup
        ArrayList<MusicGroup> groups = (ArrayList<MusicGroup>) new MusicGroupDao(context).getAllGroup();
        // 将所有的musicgrouptitle保存在一个数组中
        final String[] titles = new String[groups.size()];
        for (int i = 0; i < titles.length; i++) {
            titles[i] = groups.get(i).getId() + "、" + groups.get(i).getTitle();
        }
        // 创建单选对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setTitle("请选择组")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(titles, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 获取组id
                                int groupId = Integer.parseInt(titles[which]
                                        .substring(0,
                                                titles[which].indexOf("、")));
                                if (ismult) {
                                    for (Integer id : ids) {
                                        addToGroup(id, groupId);
                                    }
                                } else {
                                    addToGroup(musicId, groupId);
                                }
                                // 关闭对话框
                                dialog.cancel();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    private void addToGroup(final int musicId, int groupId) {
        // 构造musicItem对象
        musicItem = new MusicItem();
        musicItem.setGroupId(groupId);
        musicItem.setMusicId(musicId);
        // 添加数据到musicitem表
        musicItemDao.insert(musicItem);
    }
    @Override
    public void initListener() {
        localistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                num = position;
                songItemDialog();
                return false;
            }
        });
        localistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playmusic(position);
            }
        });
    }

    @Override
    public void Refresh(Object... obj) {

    }
}
