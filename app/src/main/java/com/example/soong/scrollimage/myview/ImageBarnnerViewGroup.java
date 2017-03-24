package com.example.soong.scrollimage.myview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Soong on 2017/3/24.
 * 自定义ViewGroup必须实现 测量 布局 绘制
 */

public class ImageBarnnerViewGroup extends ViewGroup{
    private int children;
    private int childrenWidth; //子视图宽度
    private int childrenHeight;

    private boolean isClick;
    private boolean isAuto = true;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (++index >= children){
                        index = 0;
                    }
                    scrollTo(childrenWidth*index,0);
                    break;
            }
        }
    };

    private void setAuto(){
        isAuto = true;
    }

    private void shutAuto(){
        isAuto = false;
    }

    private int x; //初始坐标
    private int index;  //图片索引

    private Scroller scroller;

    private ImageListener listener;

    public void setListener(ImageListener listener) {
        this.listener = listener;
    }

    public ImageListener getListener() {
        return listener;
    }

    public ImageBarnnerViewGroup(Context context) {
        super(context);
        init();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        scroller = new Scroller(getContext());

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isAuto){
                    handler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(timerTask,1000,1000);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        children = getChildCount();
        if (children == 0){
            setMeasuredDimension(0,0);
        }else{
            //测量姿势图的宽度和高度
            measureChildren(widthMeasureSpec,heightMeasureSpec);
            View view = getChildAt(0);
            childrenWidth = view.getMeasuredWidth();
            childrenHeight = view.getMeasuredHeight();
            int width = view.getMeasuredWidth()*children;  //总宽度
            /**
             * 通过测量子布局的宽高 来设置ViewGroup的宽 高
             */
            setMeasuredDimension(width,childrenHeight);
        }
    }

    /**
     * 事件拦截 viewGroup截获(返回值为true) 后 调用onTouchEvent事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isAuto = false;
                isClick = true;
                if (scroller.isFinished()){
                    scroller.abortAnimation();
                }
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int distance  = moveX-x;
                scrollBy(-distance,0);
                x = moveX;
                isClick = false;
                break;

            case MotionEvent.ACTION_UP:
                isAuto = true;
                int scrollX = getScrollX();
                index = (scrollX+childrenWidth/2)/childrenWidth;
                if (index<0){
                    index=0;
                }else if(index>children-1){
                    index = children-1;
                }
                if (isClick){
                    listener.ImageTouch(index);
                }else {
                    int dx = index*childrenWidth - scrollX;
                    scroller.startScroll(scrollX,0,dx,0);
                    postInvalidate();
                }

//                scrollTo(index*childrenWidth,0);
                break;
            default:
                break;
        }
        return true;  //通知ViewGroup父容器 viewGroup已处理事件 不在向上传递
    }

    /**
     * 布局
     * @param changed viewGroup布局为止发生改变为true
      @param l 子布局左侧距viewGroup的距离  left top right bottom
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            int leftMargin = 0;
            for (int i = 0; i < children; i++) {
                View view = getChildAt(i);
                view.layout(leftMargin,0,leftMargin+childrenWidth,childrenHeight);
                leftMargin +=childrenWidth;
            }
        }
    }
    
}
