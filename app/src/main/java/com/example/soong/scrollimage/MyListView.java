package com.example.soong.scrollimage;

import android.content.Context;

import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Soong on 2017/3/25.
 */

public class MyListView extends ListView implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private View deleteButton;
    private OnDeleteListener deleteListener;

    private boolean isDeleteShown;
    private int selectedItem;

    private ViewGroup itemLayout;

    public interface OnDeleteListener {
        void onDeleteItem(int index);
    }

    public void setOnDeleteListener(OnDeleteListener listener){
        deleteListener = listener;
    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDeleteShown) {
            itemLayout.removeView(deleteButton);
            deleteButton = null;
            isDeleteShown = false;
            return false;
        } else {
            selectedItem = pointToPosition((int) event.getX(), (int) event.getY());
            return gestureDetector.onTouchEvent(event);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (isDeleteShown) {
            selectedItem = pointToPosition((int) e.getX(), (int) e.getY());
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    /**
     * 初次结实onFling
     * @param e1  手势起点的移动事件
     * @param e2   手势当前的移动事件
     * @param velocityX  每秒X轴方向移动的像素
     * @param velocityY  每秒Y轴方向移动的像素
     * @return
     */

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY) && (e1.getX()-e2.getX())>20) {
            deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.delete_button, null);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    deleteListener.onDeleteItem(selectedItem);
                }
            });
            itemLayout = (ViewGroup) getChildAt(selectedItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(deleteButton, params);
            isDeleteShown = true;
        }
        return false;
    }


}
