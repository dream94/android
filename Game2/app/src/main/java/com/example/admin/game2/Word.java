package com.example.admin.game2;

import android.graphics.Point;

/**
 * 中文词语
 * Created by Admin on 2015/6/5.
 */
public class Word {
    public Point start;       //开始位置
    public Point end;         //结束位置
    private String content;  //词语内容
    public boolean check;  //是否已经划出

    public Word(Point start, Point end, String content, boolean check) {
        this.start = start;
        this.end = end;
        this.content = content;
        this.check = check;
    }


}
