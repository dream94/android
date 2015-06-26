package com.example.admin.game2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/6/6.
 */
public class Game2SurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private float width, height;        //SurfaceView的宽度和高度
    private float w, h;                  //平分成小格的宽度和高度
    public boolean check = true;
    private MyThread thread;
    private Paint paint;
    private static boolean [][]map = new boolean[7][7];  //表示是否有字,第一维表示y轴，第二维表示x轴;
    private static String [][]words = new String[7][7];
    private List<Word> listword = new ArrayList<Word>();
    ArrayList<String> list;
    Canvas canvas;
    int startX = -1, startY = -1, endX = -1, endY = -1;         //开始位置，结束位置
    boolean isPress = false;
    public Game2SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        setKeepScreenOn(true);
        paint = new Paint();
        paint.setAntiAlias(true);       //消除锯齿
        setFocusable(true);
        setFocusableInTouchMode(true);
        setZOrderOnTop(true);           //设置surfaceView背景透明,包括下面那句
        holder.setFormat(PixelFormat.TRANSLUCENT);
        for(int j=0; j<7; ++j){
            for(int t=0; t<7; ++t){
                map[j][t] = false;
            }
        }
        list = Game2.list;
        createWord();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        width = holder.getSurfaceFrame().width();
        height = holder.getSurfaceFrame().height();
        w = width/7;
        h = height/7;
        /*Log.i("huang", "height:" + height + " , width:" + width);
        Log.i("huang", "other height:" + getHeight());
        Log.i("huang", "h:" + h + " , w:" + w);
        */
        thread = new MyThread();
        thread.start();
    }

    private void draw(){
        try{
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);      //清屏，防止缓存内容闪烁
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(1f);
            //划横线和竖线
            for(int t=0; t<8; ++t){
                canvas.drawLine(0f, t*h, width, t*h, paint);
                canvas.drawLine(t*w, 0f, t*w, height, paint);
            }
            //把字写上去
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(h-20);
            canvas.drawText(words[0][0],w/2, h/2+(h-25)/2, paint);
            for(int j=0; j<7; ++j){
                for(int t=0; t<7; ++t){
                    canvas.drawText(words[j][t], w/2+t*w, h/2+(h-25)/2+j*h, paint);
                }
            }
            paint.setARGB(100,20,161,13);//设置透明度和颜色
            paint.setStrokeWidth(h-20);
            //画上点击的字
            if(startX != -1){
                if(endX == -1){          //只点了一个点
                    canvas.drawCircle(w/2+startX*w, h/2+startY*h, (w-20)/2, paint);
                }else{           //起点和终点都是圆，中间是线
                    canvas.drawCircle(w/2+startX*w, h/2+startY*h, (w-20)/2, paint);
                    canvas.drawLine(w / 2 + startX * w, h / 2 + startY * h, w / 2 + endX * w, h / 2 + endY * h, paint);
                    canvas.drawCircle(w / 2 + endX * w, h / 2 + endY * h, (w - 20) / 2, paint);
                }
            }

            //检查上面画出来的字是否是文本中的字
            if(!isPress){        //松手时刻或者没按过
                if(startX != -1){       //松手时刻
                    for(int t=0; t<listword.size(); ++t){
                        Word word = listword.get(t);
                        int x1 = word.start.x;
                        int y1 = word.start.y;
                        int x2 = word.end.x;
                        int y2 = word.end.y;
                        if(x1 == startX && y1 == startY && x2 == endX && y2 == endY){
                            word.check = true;
                            break;
                        }
                    }
                    startX = startY = -1;
                    endX = endY = -1;
                }
            }
            //画出已经选对的字
            for(int t=0, g=0; t<listword.size(); ++t){
                Word word = listword.get(t);
                if(word.check){
                    int x1 = word.start.x;
                    int y1 = word.start.y;
                    int x2 = word.end.x;
                    int y2 = word.end.y;
                    canvas.drawCircle(w / 2 + x1 * w, h / 2 + y1 * h, (w - 20) / 2, paint);
                    canvas.drawLine(w / 2 + x1 * w, h / 2 + y1 * h, w / 2 + x2 * w, h / 2 + y2 * h, paint);
                    canvas.drawCircle(w / 2 + x2 * w, h / 2 + y2 * h, (w - 20) / 2, paint);
                    g++;
                }
                if(g == 4){
                    check = false;          //答对所有,线程就结束了
                }
            }



        } catch (Exception e){

        } finally {
            if(canvas != null){
                holder.unlockCanvasAndPost(canvas);
            }
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int pos = (int)x/(int)w+(int)y/(int)h*7;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = pos%7;
                startY = pos/7;
                isPress = true;
                break;
            case MotionEvent.ACTION_MOVE:
                endX = pos%7;
                endY = pos/7;
                break;
            case MotionEvent.ACTION_UP:
                isPress = false;
        }
        return true;
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            while(check){
                draw();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 生成数据
     */
    private void createWord(){
        for(int t=0; t<list.size(); ++t){
            int pos = (int)(Math.random()*49);
            int x = pos%7;
            int y = pos/7;
            listword.add(WordType(list.get(t), new Point(x, y)));
        }
        for(int j=0; j<7; ++j){
            for(int t=0; t<7; ++t){
                if(!map[j][t]){
                    map[j][t] = true;
                    words[j][t] = Application.createChineseChar();
                }
            }
        }
    }

    //产生不同类型的词语（有向左构成的、有向右构成的、有向上构成的、有向下构成的、有向左上方、右上方、左下方、右下方）
    public static Word WordType(String s, Point start){    //start表示开始位置
        int r, len=s.length();
        int x = start.x, y=start.y;
        boolean flag= false;
        Point end = null;
        while(true){
            if(flag){         //已经产生了字符串摆放的类型
                break;
            }
            r= (int)(Math.random()*8);
            switch (r){
                case 0:
                    if(x+1>=len&&!isPut(x, y, x-len+1, y, s)){
                        end = new Point(x-len+1, y);
                        flag = true;
                    }
                    break;
                case 1:
                    if(x+len-1<7&&!isPut(x, y, x+len-1, y, s)){
                        end = new Point(x+len-1, y);
                        flag = true;
                    }
                    break;
                case 2:
                    if(y+1>=len&&!isPut(x, y, x, y-len+1, s)){
                        end = new Point(x, y-len+1);
                        flag = true;
                    }
                    break;
                case 3:
                    if(y+len-1<7&&!isPut(x, y, x, y+len-1, s)){
                        end = new Point(x, y+len-1);
                        flag = true;
                    }
                    break;
                case 4:
                    if(x+1>=len&&y+1>=len&&!isPut(x, y, x-len+1, y-len+1, s)){
                        end = new Point(x-len+1, y-len+1);
                        flag = true;
                    }
                    break;
                case 5:
                    if(x+len-1<7&&y+len-1<7&&!isPut(x, y, x+len-1, y+len-1, s)){
                        end = new Point(x+len-1, y+len-1);
                        flag = true;
                    }
                    break;
                case 6:
                    if(x+len-1<7&&y+1>=len&&!isPut(x, y, x+len-1, y-len+1, s)){
                        end = new Point(x+len-1, y-len+1);
                        flag = true;
                    }
                    break;
                case 7:
                    if(x+1>=len&&y+len-1<7&&!isPut(x, y, x-len+1, y+len-1, s)){
                        end = new Point(x-len+1, y+len-1);
                        flag = true;
                    }
                    break;
            }
        }
        Log.i("point", "("+start.x+","+start.y+") "+ "("+end.x+","+end.y+")");
        return new Word(start, end, s, false);
    }

    public static boolean isPut(int x1, int y1, int x2, int y2, String s){
        if(x1==x2){            //表示竖直方向
            int minY, maxY;
            if(y1>y2){
                s = reverse(s);
                minY = y2;
                maxY = y1;
            }else{
                minY = y1;
                maxY = y2;
            }
            for(int t=minY; t<=maxY; ++t){
                if(map[t][x1]){
                    return true;
                }
            }
            for(int t=minY, g=0; t<=maxY; ++t, ++g){
                map[t][x1] = true;
                words[t][x1] = String.valueOf(s.charAt(g));
            }
            return false;
        }else if(y1==y2){      //表示水平方向
            int minX, maxX;
            if(x1>x2){
                s = reverse(s);
                minX = x2;
                maxX = x1;
            }else{
                minX = x1;
                maxX = x2;
            }
            for(int t=minX; t<=maxX; ++t){
                if(map[y1][t]){
                    return true;
                }
            }
            for(int t=minX, g=0; t<=maxX; ++t, ++g){
                map[y1][t] = true;
                words[y1][t] = String.valueOf(s.charAt(g));
            }
            return false;
        }else{              //表示斜的方向
            int k = -(y1-y2)/(x1-x2);      //斜率,第四象限，y轴是负的
            int minX, maxX, y;
            if(x1>x2){
                s = reverse(s);
                minX = x2;
                maxX = x1;
                y = y2;
            }else{
                minX = x1;
                maxX = x2;
                y = y1;
            }
            for(int t=minX, g=0; t<=maxX; ++t, ++g){
                if(map[y-k*g][t]){
                    return true;
                }
            }
            for(int t=minX, g=0; t<=maxX; ++t, ++g){
                map[y-k*g][t] = true;
                words[y-k*g][t] = String.valueOf(s.charAt(g));
            }
            return false;
        }
    }

    /**
     * 倒序
     * @param s
     * @return
     */
    public static String reverse(String s){
        StringBuilder sd = new StringBuilder(s);
        return sd.reverse().toString();
    }

}
