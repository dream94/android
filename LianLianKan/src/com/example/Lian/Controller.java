package com.example.Lian;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.*;

public class Controller {
    public final static int countX = 10;
    public final static int countY = 12;

    private final static int IconWidth = Main.screenWidth / (countX-1);      //     /9
    private final static int IconHeight = Main.screenHeight / (countY+1);   //     /13

    private Main app;
    private GameView view;

    private static int map[][];
    //保存连接的起始点和转折点
    private List<Point> path = new ArrayList<Point>();
    public RefreshHandler handler = new RefreshHandler();
    public GameView.OnItemClickListener onItemClickListener;

    public boolean isAddTime = false;
    public final static long totalTime = 300;
    public static long remainTime;
    private int help;           //帮助
    private int level;          //关卡
    private List<Point>  plist;
    int n=0;
    Timer timer;
    public void startGame(Main app, int n){
        generateMap();                 //产生地图
        if(n==0){                     //新的开始
            help = 5;
            level = 1;
        }else{
            help += 2;        //每过一关，奖励2次帮助
            level++;
        }
        remainTime = totalTime;          //这里可以修改
        resume(app);
    }
    public void resume(Main app){
        Log.i("huang", remainTime+" leave");
        this.app = app;
        isAddTime = true;          //继续增加时间
        view = new GameView(app);
        view.setMap();
        view.setIcons(app.getIcons());
        LinearLayout parent = (LinearLayout) app.findViewById(R.id.layout);
        parent.addView(view);
        onItemClickListener = new ItemClickListener();
        view.setOnItemClickListener(onItemClickListener);
        timer = new Timer();
        Log.i("huang", "isAddTime:"+isAddTime);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(isAddTime){
                    n++;
                    if(n%5==0){
                        remainTime -= 1;
                        n=0;
                        Message msg = new Message();
                        msg.what = RefreshHandler.UPDATE_TXT;
                        handler.sendMessage(msg);
                    }
                }
            }
        };
        timer.schedule(task, 0, 200);        //200毫秒为一个周期*/
    }
    //处理GameView中产生的触屏事件
    class ItemClickListener implements GameView.OnItemClickListener{
        @Override
        public void onClick(Point p) {
            Log.i("huang", "onClick");
            List<Point> selected = view.getSelected();
            if(selected.size()==1){              //之前已经选了一个点
                Point p1 = selected.get(0);
                Log.i("huang", "pointA:"+p1.x+","+p1.y+" pointB:"+p.x+","+p.y);
                if(map[p1.x][p1.y]==map[p.x][p.y]&&link(p1.x, p1.y, p.x, p.y)){
                    selected.add(p);
                    view.drawline(path);
                    handler.sleep(500);
                }else{
                    selected.clear();
                    selected.add(p);
                    view.invalidate();
                }
            }else{                      //第一次选择
                selected.add(p);
                view.invalidate();
            }
        }
    }
    class RefreshHandler extends Handler{
        public static final int UPDATE_TXT = 0;         //更新文本
         public static final int UPDATE_PATH = 2;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TXT:
                    TextView t = (TextView) app.findViewById(R.id.time);
                    if(t != null){
                        t.setText("     剩余时间：" + remainTime + "秒  帮助：" + help + "次" + "    第" + level + "关");
                        view.invalidate();      //其实是调用view的OnDraw方法
                        if(remainTime<=0){
                            timer.cancel();
                            //画进度条
                            app.setState(Main.STATE_LOSE);
                        }
                    }else{
                        Log.i("huang", "TestView is null");
                    }
                    break;
                case UPDATE_PATH:
                    view.clearPath();
                    if(win()){
                        timer.cancel();
                        app.setState(Main.STATE_WIN);
                    }else if(die()){
                        shuffle();
                    }
            }
        }

        public void sleep(long delay){       // 延时刷新视图
            this.removeMessages(0);     //移走更新文本
            Message msg = new Message();
            msg.what = UPDATE_PATH;
            sendMessageDelayed(msg, delay);
        }
    }
    private boolean win(){
        for(int i=1; i<countX-1; ++i){
            for(int j=1; j<countY-1; ++j){
                if(map[i][j]!=0){
                    return false;
                }
            }
        }
        return true;
    }

    private void generateMap(){
        map = new int[countX][countY];            //注意这里要与手机坐标轴不同，等下要做处理  数组是10行12列
        int x = 1;
        for(int i=1; i<countX-1; ++i){             // 数组是8行10列
            for(int j=1; j<countY -1; j+=2){
                map[i][j] = x;
                map[i][j+1] = x;
                x++;
                if(x==Main.iconCount+1){
                    x = 1;
                }
            }
        }
        shuffle();          //随机打乱
    }

    private void shuffle(){             //随机打乱
        Random r = new Random();
        plist = new ArrayList<Point>();        //用来保存数据的坐标
        for(int i=1; i<countX-1; ++i){
            for(int j=1; j<countY-1; j++){
                if(map[i][j]!=0) {
                    Point p = new Point(i, j);              //注意这里坐标
                    plist.add(p);
                }
            }
        }
        int size = plist.size(), k;
        for(int t=0; t<size; ++t){
            Point p1 = plist.get(t);
            k = r.nextInt(size);
            Point p2 = plist.get(k);
            int temp = map[p1.x][p1.y];
            map[p1.x][p1.y] = map[p2.x][p2.y];
            map[p2.x][p2.y] = temp;
        }
        plist.clear();
        if(die()){                   //随机产生的图无解
            shuffle();
        }
    }

    private boolean die() {           //判断此图还有没有解,无解返回true
        for (int y = 1; y < countY - 1; ++y) {
            for (int x = 1; x < countX - 1; ++x) {
                if (map[x][y] != 0) {
                    for (int j = y; j < countY - 1; ++j) {
                        for (int t = x + 1; t < countX - 1; ++t) {
                            if (map[t][j] == map[x][y] && link(t, j, x, y)) {
                                return false;            //表示有解
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean link(int x1, int y1, int x2, int y2) {        //判断是否可连，不判断相连图片是否相同
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        path.clear();
        if (linkDirect(x1, y1, x2, y2)) {         //判断是否直接相连
            path.add(new Point(x1, y1));
            path.add(new Point(x2, y2));
            Log.i("huang", "linkDirect");
            return true;
        } else if (x1 < x2) {
            if (expandXR(x1, x2, y1) && linkDirect(x2, y1, x2, y2)) {      //一个折点
                path.add(new Point(x1, y1));
                path.add(new Point(x2, y1));
                path.add(new Point(x2, y2));
                Log.i("huang", "expandXR(x1, x2, y1) && linkDirect(x2, y1, x2, y2)");
                return true;
            }else if(expandXL(x2, x1, y2) && linkDirect(x1,y1,x1,y2)){      //一个折点
                path.add(new Point(x2,y2));
                path.add(new Point(x1,y2));
                path.add(new Point(x1,y1));
                Log.i("huang", "expandXL(x2, x1, y2) && linkDirect(x1,y1,x1,y2)");
                return true;
            }
        }else if(x1 > x2){
            if (expandXR(x2, x1, y2) && linkDirect(x1,y2,x1,y1)) {         //一个折点
                path.add(new Point(x2, y2));
                path.add(new Point(x1, y2));
                path.add(new Point(x1, y1));
                Log.i("huang", "expandXR(x2, x1, y2) && linkDirect(x1,y2,x1,y1)");
                return true;
            }else if(expandXL(x1, x2, y1) && linkDirect(x2,y1,x2,y2)){     //一个折点
                path.add(new Point(x1,y1));
                path.add(new Point(x2,y1));
                path.add(new Point(x2,y2));
                Log.i("huang", "expandXL(x1, x2, y1) && linkDirect(x2,y1,x2,y2)");
                return true;
            }
        }
        List<Point> p1 = new ArrayList<Point>();
        List<Point> p2 = new ArrayList<Point>();
        p1.clear();
        p2.clear();
        expandX(x1, y1, p1);       //扩展两个点的x轴上可用的点
        expandX(x2, y2, p2);
        for(Point p : p1){
            for(Point p3 : p2){
                if(p.x==p3.x&&linkDirect(p.x, p.y, p3.x, p3.y)){
                    path.add(new Point(x1,y1));
                    path.add(new Point(p.x, y1));
                    path.add(new Point(p.x, y2));
                    path.add(new Point(x2, y2));
                    Log.i("huang", " expandX(x1, y1, p1); ");
                    return true;
                }
            }
        }
        p1.clear();
        p2.clear();
        expandY(x1, y1, p1);
        expandY(x2, y2, p2);
        for(Point p : p1){
            for(Point p3 : p2){
                if(p.y==p3.y&&linkDirect(p.x, p.y, p3.x, p3.y)){
                    path.add(new Point(x1, y1));
                    path.add(new Point(x1, p.y));
                    path.add(new Point(x2, p.y));
                    path.add(new Point(x2, y2));
                    Log.i("huang", " expandY(x1, y1, p1);");
                    return true;
                }
            }
        }
        return false;
    }
    private boolean expandXR(int x1, int x2, int y1){
        for(int x=x1+1; x<=x2; ++x){
            if(map[x][y1]!=0){
                return false;
            }
        }
        return true;
    }
    private boolean expandXL(int x1,int x2, int y1){
        for(int x=x1-1; x>=x2; --x){
            if(map[x][y1]!=0){
                return false;
            }
        }
        return true;
    }
    private void expandX(int x1, int y, List<Point> list){
        for(int x=x1-1; x>=0; --x){              //扩展，直到边界
            if(map[x][y]==0){
                list.add(new Point(x,y));
            }else break;
        }
        for(int x=x1+1; x<countX; ++x){
            if(map[x][y]==0){
                list.add(new Point(x,y));
            }else break;
        }
    }
    private void expandY(int x, int y1, List<Point> list){
        for(int y=y1-1; y>=0; --y){
            if(map[x][y]==0){
                list.add(new Point(x,y));
            }else break;
        }
        for(int y=y1+1; y<countY; ++y){
            if(map[x][y]==0){
                list.add(new Point(x,y));
            }else break;
        }
    }

    private boolean linkDirect(int x1, int y1, int x2, int y2){      //判断是否直接可怜，无需转折点
        if(x1==x2){
            int ymin = Math.min(y1, y2);
            int ymax = Math.max(y1, y2);
            for(int y=ymin+1; y<ymax; ++y){
                if(map[x1][y]!=0){
                    return false;
                }
            }
            return true;
        }
        if(y1==y2){
            int xmin = Math.min(x1, x2);
            int xmax = Math.max(x1, x2);
            for(int x=xmin+1; x<xmax; ++x){
                if(map[x][y1]!=0){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    public void pause(){
        isAddTime = false;
        timer.cancel();
    }
    //帮助功能的实现
    public void autoErase(){
        if(help>0){
            help--;
            List<Point> selected = view.getSelected();
            Point p1 = path.get(0);
            Point p2 = path.get(path.size()-1);
            selected.add(p1);
            selected.add(p2);
            view.drawline(path);
            handler.sleep(500);
        }

    }
    public static int[][] getMap() {
        return map;
    }

    public List<Point> getPath() {
        return path;
    }

    public static int getIconHeight() {
        return IconHeight;
    }

    public static int getIconWidth() {
        return IconWidth;
    }
}

