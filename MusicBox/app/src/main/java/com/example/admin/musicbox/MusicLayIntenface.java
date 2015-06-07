package com.example.admin.musicbox;

/**
 * 音频播放器的自定义接口
 * Created by Admin on 2015/5/27.
 */
public interface MusicLayIntenface {
    public void initView();//View界面更新

    public void initData();//数据加载

    public void initListener();//监听器加载

    public void Refresh(Object... obj);//刷新方法
}
