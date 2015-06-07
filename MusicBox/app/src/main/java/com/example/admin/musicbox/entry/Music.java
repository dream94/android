package com.example.admin.musicbox.entry;

import android.util.Log;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 音乐类
 * Created by Admin on 2015/5/25.
 */
public class Music implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String musicName;
    private String singer;
    private String albumName;
    private String albumPath;
    private String albumkey;
    private String musicPath;
    private String time;
    private String savePath;
    private long music_size;
    private boolean isLoaded;

    public Music() {
    }

    public Music(String musicName, String singer, String albumName, String savePath, long music_size) {
        this.musicName = musicName;
        this.singer = singer;
        this.albumName = albumName;
        this.savePath = savePath;
        this.music_size = music_size;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusicName() {
        return changeStr(musicName);
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSinger() {
        return changeStr(singer);
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbumName() {
        return changeStr(albumName);
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public String getAlbumkey() {
        return albumkey;
    }

    public void setAlbumkey(String albumkey) {
        this.albumkey = albumkey;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getMusic_size() {
        return music_size;
    }

    public void setMusic_size(long music_size) {
        this.music_size = music_size;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    private String changeStr(String str){
        String result = null;
        try {
            result = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            if(result.contains("?")){     //如果含有乱码
                result = str;
            }
        } catch (UnsupportedEncodingException e) {
            Log.i("huang", "Music.java changeStr wrong");
        }
        return result;
    }
}
