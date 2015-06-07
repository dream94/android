package com.example.admin.musicbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.admin.musicbox.entry.MusicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 对音乐分组类中的音乐进行增删改查操作
 * Created by Admin on 2015/5/25.
 */
public class MusicItemDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public MusicItemDao(Context context) {
        helper = new DBOpenHelper(context);
    }
    private void close(){
        if(db != null && db.isOpen()){
            db.close();
            db = null;
        }
    }
    public long insert(MusicItem item){
        long rowId = -1;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("musicid", item.getMusicId());
        values.put("groupid", item.getGroupId());
        rowId = db.insert(DBOpenHelper.MUSICITEM_TABLE, null, values);
        db.close();
        return rowId;
    }
    public int update(MusicItem item){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("musicid", item.getMusicId());
        values.put("groupid", item.getGroupId());
        count = db.update(DBOpenHelper.MUSICGROUP_TABLE, values, "_id=" + item.getId(), null);
        db.close();
        return count;
    }
    public int delete(int id){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        count = db.delete(DBOpenHelper.MUSICITEM_TABLE, "_id=" + id, null);
        db.close();
        return count;
    }

    public int deleteItemByMusicid(int musicId){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        count = db.delete(helper.MUSICITEM_TABLE, "musicid=" + musicId, null);
        db.close();
        return count;
    }

    public int deleteItemByGroupid(int groupId){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        count = db.delete(helper.MUSICITEM_TABLE, "groupid=" + groupId, null);
        db.close();
        return count;
    }

    /**
     * 获得一个组内的所有音乐item
     * @param groupId
     * @return
     */
    public List<MusicItem> getGroupItems(int groupId){
        List<MusicItem> list = new ArrayList<MusicItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.MUSICITEM_TABLE + " where groupid=" + groupId, null);
        while(cursor.moveToNext()){
            MusicItem item = new MusicItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            item.setMusicId(cursor.getInt(cursor.getColumnIndex("musicid")));
            item.setGroupId(groupId);
            list.add(item);
        }
        cursor.close();
        db.close();
        return list;
    }
    public boolean exists(int groupId, int musicId) {
        boolean ischeck = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + helper.MUSICITEM_TABLE
                        + " where groupid=? and musicid=?", new String[] {
                        String.valueOf(groupId), String.valueOf(musicId) });
        if (cursor.moveToNext()) {
            ischeck = true;
        }
        cursor.close();
        db.close();
        return ischeck;
    }
}
