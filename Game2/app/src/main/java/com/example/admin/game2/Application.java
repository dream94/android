package com.example.admin.game2;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 存放游戏变量
 * Created by Admin on 2015/6/2.
 */
public class Application {
    public static int width;
    public static  int height;
    public static int time = 30;      //游戏总的时间

    public static int[] color = {R.color.red, R.color.green, R.color.blue, R.color.juse, R.color.white};    //颜色种类

    //随机出现的词语
    public static String[] data = {"中国","别墅","增添","秀丽","汁液","解释","土壤","瀑布"};

    public static String createChineseChar() {
        Random random = new Random();
        int hightPos = (176 + Math.abs(random.nextInt(39)));// 获取高位值
        int lowPos = (161 + Math.abs(random.nextInt(93)));// 获取低位值
        byte[] b = new byte[2];              //每个汉字是由两个字节组成
        b[0] = (new Integer(hightPos).byteValue());
        b[1] = (new Integer(lowPos).byteValue());
        String str = null;
        try {
            str = new String(b, "GBk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 转成中文
        return str;
    }
}
