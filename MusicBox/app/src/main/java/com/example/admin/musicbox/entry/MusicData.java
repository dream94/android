package com.example.admin.musicbox.entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/5/26.
 */
public class MusicData {
    /**
     * 获取musics下所有音乐的名字
     * @param musics
     * @return
     */
    public static String[] getAllMusicName(List<Music> musics){
        int len = musics.size();
        String[] names = new String[len];
        for(int t=0; t<len; ++t){
            names[t] = musics.get(t).getMusicName();
        }
        return names;
    }

    public static List<Music> getMultiDatas(Context context){
        List<Music> musics = new ArrayList<Music>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        int column;
        for(cursor.moveToFirst(); cursor.isAfterLast()!=false; cursor.moveToNext()){
            Music music = new Music();
            music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            music.setMusicName(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            music.setSavePath(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
            music.setAlbumName(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
            music.setSinger(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
            music.setTime(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);
            music.setAlbumkey(cursor.getString(column));
            musics.add(music);
        }
        cursor.close();
        return musics;
    }

    public static String gealbumby(Context context, String albumkey) {
        // 取得歌曲对应的专辑Key 这里由于专辑图片太占内存 就不在播放列表上显示了
        String[] argArr = { albumkey };
        Cursor albumCursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?", argArr, null);
        if (null != albumCursor && albumCursor.getCount() > 0) {
            albumCursor.moveToFirst();
            int albumArtIndex = albumCursor
                    .getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
            String musicAlbumArtPath = albumCursor.getString(albumArtIndex);
            if (null != musicAlbumArtPath) {
                return musicAlbumArtPath;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    /**
     * 获取指定id的音乐信息
     *
     * @param context
     * @param id
     *            音乐id
     * @return music
     */
    public static Music getMusicbyid(Context context, String id) {
        // 循环找出所有的歌曲和信息
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "_id=?",
                new String[] { id }, null);
        int column;
        Music music = new Music();
        // 遍历游标内容
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToNext();
            // 取得音乐的名字
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            music.setMusicName(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            music.setSavePath(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
            music.setAlbumName(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
            music.setSinger(cursor.getString(column));
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
            music.setTime(cursor.getString(column));
            // 取得歌曲对应的专辑Key 这里由于专辑图片太占内存 就不在播放列表上显示了
            column = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);
            String albumkey = cursor.getString(column);
            music.setAlbumPath(gealbumby(context, albumkey));
        }
        cursor.close();
        return music;
    }
    public static ArrayList<Music> getMultiDataBycurrsor(Context context, Cursor musicCursor) {
        ArrayList<Music> musics = new ArrayList<Music>();
        // 循环找出所有的歌曲和信息
        int musicColumnIndex;
        // 遍历游标内容
        if (null != musicCursor && musicCursor.getCount() > 0) {
            for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor
                    .moveToNext()) {
                Music music = new Music();
                // 取得音乐的名字
                music.setId(musicCursor.getInt(musicCursor
                        .getColumnIndex("_id")));
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
                music.setMusicName(musicCursor.getString(musicColumnIndex));
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                music.setSavePath(musicCursor.getString(musicColumnIndex));
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
                music.setAlbumName(musicCursor.getString(musicColumnIndex));
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
                music.setSinger(musicCursor.getString(musicColumnIndex));
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
                music.setTime(musicCursor.getString(musicColumnIndex));
                // 取得歌曲对应的专辑Key 这里由于专辑图片太占内存 就不在播放列表上显示了
                musicColumnIndex = musicCursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_KEY);
                String[] argArr = { musicCursor.getString(musicColumnIndex) };
                ContentResolver albumResolver = context.getContentResolver();
                Cursor albumCursor = albumResolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Audio.AudioColumns.ALBUM_KEY + " = ?",
                        argArr, null);
                if (null != albumCursor && albumCursor.getCount() > 0) {
                    albumCursor.moveToFirst();
                    int albumArtIndex = albumCursor
                            .getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART);
                    if (null != albumCursor.getString(albumArtIndex)) {
                        music.setAlbumPath(albumCursor.getString(albumArtIndex));
                    } else {
                        music.setAlbumPath(null);
                    }
                } else {
                    music.setAlbumPath(null);
                }
                albumCursor.close();
                musics.add(music);
            }
            musicCursor.close();
        }
        return musics;
    }


}
