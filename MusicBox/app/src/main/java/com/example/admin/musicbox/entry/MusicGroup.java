package com.example.admin.musicbox.entry;

import java.util.List;

/**
 * 音乐分组类
 * Created by Admin on 2015/5/25.
 */
public class MusicGroup {
    private int id;
    private String title;
    private List<MusicItem> list;
    public MusicGroup(){

    }

    public MusicGroup(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MusicItem> getList() {
        return list;
    }

    public void setList(List<MusicItem> list) {
        this.list = list;
    }
}
