package com.example.admin.musicbox.entry;

/**
 * 音乐组中的音乐（相当于中介，连接音乐类和音乐分组类）
 * Created by Admin on 2015/5/25.
 */
public class MusicItem {
    private int id;        //序号id
    private int musicId;  //音乐的id
    private int groupId;  //音乐的id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMusicId() {
        return musicId;
    }
    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }
    public int getGroupId() {
        return groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
