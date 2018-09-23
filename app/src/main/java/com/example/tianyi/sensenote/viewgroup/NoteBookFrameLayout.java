package com.example.tianyi.sensenote.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.example.tianyi.sensenote.R;


public class NoteBookFrameLayout extends LinearLayout {

    private int headerHeight = 0;

    private int lastTouchX;
    private int lastTouchY;

    private int lastInterceptX;
    private int lastInterceptY;

    private int scrollHeaderHeight;

    private boolean isHeaderShown;

    private Scroller mScroller;


    public NoteBookFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public NoteBookFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NoteBookFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        headerHeight = getChildAt(0).getHeight();
        Log.i("view","height:"+headerHeight);
        isHeaderShown = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int intercept = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                lastTouchX = x;
                lastTouchY = y;
                lastInterceptX = x;
                lastInterceptY = y;
                intercept = 0;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if(isHeaderShown){
                    intercept = 1;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                intercept = 0;
                lastInterceptY = 0;
                lastInterceptX = 0;
            }
        }

        return intercept == 1;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - lastTouchX;
                int deltaY = y - lastTouchY;
                //Log.i("scroll","cur x:"+ x +" cur Y:"+y);
                scrollBy(0,-deltaY);
                lastTouchX = x;
                lastTouchY = y;
                scrollHeaderHeight += -deltaY;
                if(scrollHeaderHeight > headerHeight){
                    isHeaderShown =false;
                }
                Log.i("tag","scroll:"+ getScrollY());
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
        }

        return true;
    }

    private void smoothScrollTo(int destX,int destY){
        int scrollY = getScrollY();
        int deltaY = destY - scrollY;
        mScroller.startScroll(0,scrollY,0,deltaY,1000);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }



}
