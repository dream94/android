package com.example.admin.musicbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.admin.musicbox.entry.MusicGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 对音乐分组表的增删改查操作
 * Created by Admin on 2015/5/25.
 */
public class MusicGroupDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public MusicGroupDao(Context context){
        helper = new DBOpenHelper(context);
    }
    private void close(){
        if(db != null && db.isOpen()){
            db.close();
            db = null;
        }
    }

    /**
     * 增加新分组
     * @param group
     * @return
     */
    public long insert(MusicGroup group){
        long rowId = -1;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", group.getTitle());
        rowId = db.insert(DBOpenHelper.MUSICGROUP_TABLE, null, values);
        db.close();
        return rowId;
    }

    /**
     * 修改分组名称
     * @param group
     * @return
     */
    public int update(MusicGroup group){
        int count = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", group.getTitle());
        count = db.update(DBOpenHelper.MUSICGROUP_TABLE, values, "_id=" + group.getId(), null);
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
        count = db.delete(DBOpenHelper.MUSICGROUP_TABLE, "_id=" + id, null);
        db.close();
        return count;
    }

    public List<MusicGroup> getAllGroup(){
        List<MusicGroup> list = new ArrayList<MusicGroup>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.MUSICGROUP_TABLE, null);
        while(cursor.moveToNext()){
            MusicGroup group = new MusicGroup();
            group.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            group.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            list.add(group);
        }
        cursor.close();
        db.close();
        return list;
    }

    public int getCount(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBOpenHelper.MUSICGROUP_TABLE, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
