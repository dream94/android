package com.example.Lian;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/4/28.
 */
public class GameView extends View {
    private List<Point> selected = new ArrayList<Point>();
    private int iconWidth = Controller.getIconWidth();
    private int iconHeight = Controller.getIconHeight();
    private int map[][] = null;
    private Bitmap icons[];
    private List<Point> path;
    private int offX, offY;

    private OnItemClickListener onItemClickListener;
    public GameView(Context context) {
        super(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<Point> getSelected() {
        return selected;
    }

    public void setMap(){
        this.map = Controller.getMap();
    }

    public void setIcons(Bitmap[] icons) {
        this.icons = icons;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed){
            offX = (right-left-iconWidth*8)>>1;
            offY = (bottom-top-iconHeight*10)/3*2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paintline = new Paint();
        paintline.setColor(Color.BLUE);
        int column = map.length;            //10列
        int row = map[0].length;
        //画进度条
        float rate = (float)Controller.remainTime/Controller.totalTime;
        Paint paintprogress = new Paint();
        paintprogress.setStrokeWidth(14);
        paintprogress.setColor(Color.argb(200, (int)(255*(1-rate)), (int)(255*rate), 0));
        canvas.drawLine(offX, (float)(offY*0.5), (float)(offX+8*iconWidth*rate), (float)(offY*0.5), paintprogress);

        //画网格状(1-2)
        for(int t=1; t<column; ++t){      //先画竖条(1)
            canvas.drawLine(offX+(t-1)*iconWidth, offY, offX+(t-1)*iconWidth, offY+10*iconHeight, paintline);
        }
        for(int t=1; t<row; ++t){          //画横条(2)
            canvas.drawLine(offX, offY+(t-1)*iconHeight, offX+8*iconWidth, offY+(t-1)*iconHeight, paintline);
        }

        //画图片
        for(int j=1; j<row-1; ++j){
            for(int t=1; t<column-1; ++t){
                if(map[t][j]!=0){
                    Bitmap bitmap = icons[map[t][j]-1];
                    canvas.drawBitmap(bitmap, null, new Rect(offX+(t-1)*iconWidth, offY+(j-1)*iconHeight,
                            offX+t*iconWidth, offY+j*iconHeight), null);
                }
            }
        }

        //刷新被选择的图片
        Paint paintImage = new Paint();
        paintImage.setColor(Color.RED);
        paintImage.setStyle(Paint.Style.STROKE);
        paintImage.setStrokeWidth(4);
        for(Point p:selected){
            canvas.drawRect(offX+(p.x-1)*iconWidth, offY+(p.y-1)*iconHeight, offX+p.x*iconWidth, offY+p.y*iconHeight, paintImage);
        }

        //画两张图片相连的线
        if(path != null && path.size()>=2){          //路径中至少要有2个点，才能表示相连
            Paint imageLine = new Paint();
            imageLine.setColor(Color.BLUE);
            imageLine.setStrokeWidth(5);
            imageLine.setStyle(Paint.Style.STROKE);
            for(int t=0; t<path.size()-1; ++t){
                Point p1 = path.get(t);
                Point p2 = path.get(t+1);
                canvas.drawLine(offX+(p1.x-1)*iconWidth+iconWidth/2, offY+(p1.y-1)*iconHeight+iconHeight/2,
                        offX+(p2.x-1)*iconWidth+iconWidth/2, offY+(p2.y-1)*iconHeight+iconHeight/2, imageLine);         //相连两个图片的中心
            }
            Point p1 = path.get(0);
            Point p2 = path.get(path.size()-1);
            map[p1.x][p1.y]=0;
            map[p2.x][p2.y]=0;
            selected.clear();
        }

    }

    public void clearPath(){
        path = null;
        this.invalidate();
    }

    public void drawline(List<Point> path1){                      //对选中的两个点进行相连
        path = path1;
        invalidate();
    }

    interface OnItemClickListener{               //自定义监听器
        public void onClick(Point point);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {                     //触屏事件处理，注意这里的触屏是针对这个view而言的，所以得到的坐标也是相当于这个view
        if (event.getAction() != MotionEvent.ACTION_DOWN) {               //刚触摸的时刻
            return false;
        }
        int x = (int) event.getX();          //获得的x坐标和y坐标都是这个view内的坐标，无需做处理
        int y = (int) event.getY();
        if (x >= offX && y >= offY) {
            int j = (int) ((x - offX) * 1.0 / iconWidth);
            int t = (int) ((y - offY) * 1.0 / iconHeight);
            if(map[j+1][t+1]!=0){
                Log.i("huang", "map["+(j+1)+"]["+(t+1)+"]");
                onItemClickListener.onClick(new Point(j+1, t+1));           //触发Controller事件，来处理触屏事件
            }
        }
        return true;
    }
}
