package com.example.admin.testfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新的控件
 * Created by Admin on 2015/5/6.
 */
public class RefreshView extends LinearLayout implements View.OnTouchListener {
    public static final int STATUS_PULL_TO_REFRESH = 0;         //下拉状态
    public static final int STATUS_RELEASE_TO_REFRESH = 1;      //释放立即刷新状态
    public static final int STATUS_REFRESHING = 2;              //正在刷新状态
    public static final int STATUS_REFRESH_FINISHED = 3;         //刷新完成或未刷新状态
    public static final int SCROLL_SPEED = -20;                 //下拉头部回滚的速度
    public static final long ONE_MINUTE = 60 * 1000;              //一分钟时间，用于判断上次的更新时间
    public static final long ONE_HOUR = ONE_MINUTE * 60;          //一个小时的时间
    public static final long ONE_DAY = ONE_HOUR * 24;             //一天的时间
    private static final String UPDATED_AT = "huang";

    private PullToRefreshListener mListener;                      //下次刷新的回调窗口
    /**
     * 用于存储上次更新时间
     */
    private SharedPreferences preferences;
    /**
     * 下拉头的View
     */
    private View header;
    private ListView listView;         //这个布局下的ListView
    private ProgressBar progressBar;
    private ImageView arrow;
    private TextView description;
    private TextView updateAt;
    /**
     * 下拉头的布局参数
     */
    private MarginLayoutParams headerLayoutParams;
    private long lastUpdateTime;                                 //上次更新时间的毫秒值

    private int hideHeaderHeight;       //下拉头的高度
    private int currentStatus = STATUS_REFRESH_FINISHED;                 //当前处于什么状态
    private int lastStatus = currentStatus;                    //记录上一次的状态，避免重复操作

    private float y;
    private int touchSlop;         //在被判定为滚动之前用户手指可以移动的最大值
    private boolean loadOnce;      //是否加载过一次layout，这里onLayout中的初始化秩序加载一次
    private boolean ableToPull;    //当前是否可以下拉，只有ListView滚动到头的时候才允许下拉

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        header = LayoutInflater.from(context).inflate(R.layout.pulltorefresh, null, true);
        progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
        arrow = (ImageView) header.findViewById(R.id.arrow);
        description = (TextView) header.findViewById(R.id.description);
        updateAt = (TextView) header.findViewById(R.id.updated_at);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.i("huang", "touchSlop123:"+touchSlop);
        refreshUpdatedAtValue();
        setOrientation(VERTICAL);
        addView(header, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed && !loadOnce){
            hideHeaderHeight = -header.getHeight();
            Log.i("huang", hideHeaderHeight+"");
            headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
            headerLayoutParams.topMargin = hideHeaderHeight;         //这里就代表header视图位置，改变的话header也会改变位置
            listView = (ListView) getChildAt(1);      //获取布局下的ListView控件
            listView.setOnTouchListener(this);
            loadOnce = true;
            ableToPull = true;
        }
    }


    /**
     * * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
     * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
     * @param event
     */
    private void setIsAbleToPull(MotionEvent event){
        View firstChild = listView.getChildAt(0);
        if(firstChild != null){
            int firstVisiblePos = listView.getFirstVisiblePosition();
            if(firstVisiblePos == 0 && firstChild.getTop()==0){     //已经滑到此时ListView的顶端
                if(!ableToPull){
                    y = event.getRawY();
                }
                ableToPull = true;
            }else{
                if(headerLayoutParams.topMargin != hideHeaderHeight){
                    Log.i("huang", "topMargin:"+headerLayoutParams.topMargin+"");
                    headerLayoutParams.topMargin = hideHeaderHeight;
                    header.setLayoutParams(headerLayoutParams);                   //增强连贯性
                }
                ableToPull = false;
            }
        }else{
            // 如果ListView中没有元素，也应该允许下拉刷新
            ableToPull = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //setIsAbleToPull(event);
        if (ableToPull) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    y = event.getRawY();
                    Log.i("huang", "y:"+y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove = event.getRawY();
                    int distance = (int) (yMove - y);
                    // 如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
                    if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
                        return false;
                    }
                    if (distance < touchSlop) {
                        return false;
                    }
                    if (currentStatus != STATUS_REFRESHING) {
                        if (headerLayoutParams.topMargin > 0) {        //已经完全拉了下来，准备松手释放
                            Log.i("huang", "headerLayoutParams.topMargin > 0");
                            currentStatus = STATUS_RELEASE_TO_REFRESH;
                        } else {
                            currentStatus = STATUS_PULL_TO_REFRESH;     //还没完全拉下来
                        }
                        // 通过偏移下拉头的topMargin值，来实现下拉效果
                        headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
                        header.setLayoutParams(headerLayoutParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                        // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
                        new RefreshingTask().execute();
                    } else if (currentStatus == STATUS_PULL_TO_REFRESH) {
                        // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
                        new HideHeaderTask().execute();
                    }
                    break;
            }
            // 时刻记得更新下拉头中的信息
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH) {
                updateHeaderView();
                // 当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
                listView.setPressed(false);
                listView.setFocusable(false);
                listView.setFocusableInTouchMode(false);
                lastStatus = currentStatus;
                // 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
                return true;
            }
        }
        return false;
    }

    public void setPullToRefreshListener(PullToRefreshListener m){
        mListener = m;
    }

    public void finishRefreshing() {
        currentStatus = STATUS_REFRESH_FINISHED;
        preferences.edit().putLong(UPDATED_AT, System.currentTimeMillis()).commit();
        new HideHeaderTask().execute();
    }
    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    class RefreshingTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= 0) {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            currentStatus = STATUS_REFRESHING;
            publishProgress(0);
            if (mListener != null) {
                mListener.refresh();           //刷新时做出的操作
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            Log.i("huang", "topMargin:"+topMargin[0]);
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }

    }

    /**
     * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏。
     *
     * @author guolin
     */
    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            Log.i("huang", "Hide topMargin:"+topMargin);
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= hideHeaderHeight) {
                    topMargin = hideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
                sleep(10);
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            Log.i("huang", "progress Hide topMargin:"+topMargin[0]);
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
            headerLayoutParams.topMargin = topMargin;
            header.setLayoutParams(headerLayoutParams);
            currentStatus = STATUS_REFRESH_FINISHED;
        }
    }

    /**
     * 更新下拉头中的信息
     */
    private void updateHeaderView(){
        if(lastStatus != currentStatus){
            if(currentStatus == STATUS_PULL_TO_REFRESH){           //下拉就会更新
                description.setText(getResources().getString(R.string.pull_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);             //隐藏进度条
                rotateArrow();                   //旋转箭头方向
            }else if(currentStatus == STATUS_REFRESHING){       //正在刷新
                description.setText(getResources().getString(R.string.refreshing));
                progressBar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }else if(currentStatus == STATUS_RELEASE_TO_REFRESH){          //释放就会刷新
                description.setText(getResources().getString(R.string.release_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            }
            refreshUpdatedAtValue();
        }
    }

    /**
     * 箭头旋转，调转方向
     */
    private void rotateArrow(){
        float pivoX = arrow.getWidth()/2f;       //以箭头宽度的一半为中心
        float pivoY = arrow.getHeight()/2f;
        float fromDegree = 0f;
        float toDegree = 0f;
        if(currentStatus == STATUS_PULL_TO_REFRESH){
            fromDegree = 180f;
            toDegree = 360f;
        }else if(currentStatus == STATUS_RELEASE_TO_REFRESH){
            fromDegree = 0f;
            toDegree = 180f;
            RotateAnimation animation = new RotateAnimation(fromDegree, toDegree, pivoX, pivoY);
            animation.setDuration(200);
            animation.setFillAfter(true);
            arrow.startAnimation(animation);
        }

    }

    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    public void refreshUpdatedAtValue(){
        lastUpdateTime = preferences.getLong(UPDATED_AT, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue = null;
        if(lastUpdateTime == -1){
            updateAtValue = getResources().getString(R.string.not_updated_yet);
        }else if(timePassed<0){
            updateAtValue = getResources().getString(R.string.time_error);
        }else if(timePassed<ONE_MINUTE){        //小于一分钟
            updateAtValue = getResources().getString(R.string.updated_just_now);
        }else if(timePassed<ONE_HOUR){
            timeIntoFormat = timePassed / ONE_MINUTE;
            updateAtValue = String.format(getResources().getString(R.string.updated_at), timeIntoFormat+"分钟");
        }
        updateAt.setText(updateAtValue);
    }
    interface PullToRefreshListener{
        void refresh();
    }
}
