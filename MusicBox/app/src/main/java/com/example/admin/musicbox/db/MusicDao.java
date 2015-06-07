package com.example.admin.musicbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.admin.musicbox.entry.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 装载音乐表的增删改查操作类   (DBOpenHelper.LOADEDMUSIC_TABLE)
 * Created by Admin on 2015/5/25.
 */
public class MusicDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public MusicDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    public void close(){
        if(db!=null&&db.isOpen()){
            db.close();
            db = null;
        }
    }
    /**
     * 添加音乐
     * @param music
     * @return
     */
    public long insert(Music music){
        long rowId = -1;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", music.getId());
        values.put("musicname", music.getMusicName());
        values.put("singer", music.getSinger());
        values.put("_data", music.getSavePath());
        rowId = db.insert(DBOpenHelper.LOADEDMUSIC_TABLE, null, values);
        db.close();
        return rowId;
    }

    /**
     * 更新音乐
     * @param music
     * @return
     */
    public int update(Music music){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("musicname", music.getMusicName());
        values.put("singer", music.getSinger());
        count = db.update(DBOpenHelper.LOADEDMUSIC_TABLE, values, "_id=" + music.getId(), null);
        db.close();
        return count;
    }

    /**
     * 根据id删除表中的记录
     * @param id
     * @return
     */
    public int delete(int id){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        count = db.delete(DBOpenHelper.LOADEDMUSIC_TABLE, "_id=" + id, null);
        db.close();
        return count;
    }

    /**
     * 返回装载音乐表中所有记录的游标
     * @return
     */
    public Cursor query(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.LOADEDMUSIC_TABLE, null);
        return cursor;
    }

    /**
     * 获得音乐数目
     * @return
     */
    public int getCount(){
        int count = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DBOpenHelper.LOADEDMUSIC_TABLE, null);
        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 扫描音乐目录，更新音乐表中的音乐（一些不存在的音乐文件就删去记录）
     */
    public void scanDir(){
        Cursor cursor = query();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String path = cursor.getString(cursor.getColumnIndex("_data"));
            if(!new File(path).exists()){        //如果该音乐文件不存在的话,则更新表中记录
                delete(id);
            }
        }
        cursor.close();
        close();
        return ;
    }

    /**
     * 判断这个id的音乐是否存在
     * @param id
     * @return
     */
    public boolean isExist(int id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.LOADEDMUSIC_TABLE + " where _id=" + id, null);
        if(cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    /**
     * 获取该页面下的音乐list
     * @return
     */
    public List<Music> getAllMusic(){
        List<Music> list = new ArrayList<Music>();
        Cursor cursor = query();
        while(cursor.moveToNext()){
            Music music = new Music();
            music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            music.setMusicName(cursor.getString(cursor.getColumnIndex("musicname")));
            music.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
            music.setSavePath(cursor.getString(cursor.getColumnIndex("_data")));
            list.add(music);
        }
        cursor.close();
        close();
        return list;
    }

    /**
     * 返回当前页的音乐list
     * @param currentPage  当前页面
     * @param pageSize  页面所能装载的音乐数目
     * @return
     */
    public List<Music> getPageMusic(int currentPage, int pageSize){
        List<Music> list = new ArrayList<Music>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.LOADEDMUSIC_TABLE + " limit ?, ?",
                new String[]{String.valueOf((currentPage-1)*pageSize), String.valueOf(pageSize)});//表示从?（后面的一个）开始后面?条数据
        while(cursor.moveToNext()){
            Music music = new Music();
            music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            music.setMusicName(cursor.getString(cursor.getColumnIndex("musicname")));
            music.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
            music.setSavePath(cursor.getString(cursor.getColumnIndex("_data")));
            list.add(music);
        }
        cursor.close();
        close();
        return list;
    }
}
