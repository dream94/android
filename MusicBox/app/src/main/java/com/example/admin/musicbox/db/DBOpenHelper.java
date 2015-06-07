package com.example.admin.musicbox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 2015/5/25.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "music.db";
    public static final int VERSION  =1;
    public static final String LOADEDMUSIC_TABLE = "loadedmusic";
    public static final String MUSICGROUP_TABLE = "musicgroup";
    public static final String MUSICITEM_TABLE = "musicitem";

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + LOADEDMUSIC_TABLE       //创建装载音乐表
                +"(" +
                "_id integer primary key," +
                "musicname text not null," +
                "singer text ," +
                "_data text not null" +
                ")"
        );
        db.execSQL("create table if not exists " + MUSICGROUP_TABLE      //创建音乐组表
                +"(" +
                "_id integer primary key autoincrement," +
                "title text not null"  +
                ")");
        db.execSQL("create table if not exists " + MUSICITEM_TABLE       //创建音乐组中的音乐表
                +"(" +
                "_id integer primary key autoincrement," +
                "musicid integer not null," +
                "groupid integer references " +
                MUSICGROUP_TABLE +
                "(_id))" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
