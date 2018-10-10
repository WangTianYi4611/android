package com.example.tianyi.sensenote.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.tianyi.sensenote.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeLeftLinearLayout extends LinearLayout{

    private Context mContext;
    private List<View> mSwipeViews;
    private int mSwipeViewTotalWidth;
    public static int TOUCH_SLOP = 1;
    private VelocityTracker mVelocityTracker;
    private int mDownX;
    private int mDownY;
    private int mlastX;
    private int mlastY;
    //private int scrollX;
    private boolean isSwipeViewShown;
    private Paint mPaint;
    private Scroller mScroller;
//    private GestureDetector mGestureDetector;


    public SwipeLeftLinearLayout(Context context) {
        this(context,null);
    }

    public SwipeLeftLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeLeftLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mSwipeViews = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.green));
        this.setWillNotDraw(false);
        mScroller = new Scroller(context);
//        this.mGestureDetector = new GestureDetector(context,new SwipeOnGestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = 0;
        canvas.save();
        canvas.translate(canvas.getWidth(),0);
        //canvas.drawRect(0,0,100,100,mPaint);
        for(int i = 0; i < mSwipeViews.size();i++){
            View curView = mSwipeViews.get(i);
            Log.i("draw",curView.getWidth()+" " + curView.getHeight());
            drawSwipeView(canvas,curView,width);
            width = curView.getWidth() + width;
        }
        canvas.restore();
    }

    private void drawSwipeView(Canvas canvas,View curView, int width) {
        canvas.save();
        canvas.translate(width,0);
        curView.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int intercept = 0;
        Log.i("touch","onIntercept");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                mDownX = x;
                mDownY = y;
                mlastX = x;
                mlastY = y;
                intercept = 0;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mDownX;
                int deltaY = y - mDownY;
                Log.i("touch","deltaX:"+deltaX + "deltaY:"+deltaY);
                if(Math.abs(deltaX) > Math.abs(deltaY)){
                    intercept = 1;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }
        }
        return (intercept == 1 || isSwipeViewShown);
        //return true;
    }

    private void smoothScrollTo(int destX,int destY){
        int scrollX = getScrollX();
        //int scrollY = getScrollY();
        //int deltaY = destY - scrollY;
        int deltaX = destX - scrollX;
        mScroller.startScroll(scrollX,0,deltaX,0,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        //Log.i("touch","onTouch");
        mVelocityTracker.addMovement(ev);
       // if(mSwipeViews.size() == 0) return true;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                Log.i("touch","action down ");
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mlastX;
                int scrollX = getScrollX();
                //mVelocityTracker.computeCurrentVelocity(1000);
                Log.i("touch","action move scrollX:"+scrollX +"delatX:"+deltaX +" speed:"+mVelocityTracker.getXVelocity());
                if(scrollX >= 0 && scrollX <= mSwipeViewTotalWidth) {
                    scrollX = scrollX + (-deltaX) ;
                    if(scrollX >= mSwipeViewTotalWidth) scrollX = mSwipeViewTotalWidth;
                    else if(scrollX <= 0) scrollX = 0;
                    scrollTo(scrollX, 0);
                    invalidate();
                    isSwipeViewShown = true;
                }
                mlastX = x;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:{
                Log.i("touch","action up x:"+x +" mDownX:"+mDownX +"isSwipeViewShown:"+isSwipeViewShown);
                if(isSwipeViewShown) {
                    if (Math.abs(x - mDownX) < TOUCH_SLOP && isSwipeViewShown) {//onDown事件
                        processOnDown(ev);
                    } else {
                        int deltaX = x - mlastX;
                        //Log.i("touch","ScrollX:"+getScrollX() +" width:" + mSwipeViewTotalWidth);
                        mVelocityTracker.computeCurrentVelocity(1000);
                        //Log.i("touch", "scrollX:" + getScrollX() + " speed:" + mVelocityTracker.getXVelocity());
                        int scrollX = getScrollX();
                        if(mVelocityTracker.getXVelocity() < -500){
                            smoothScrollTo(mSwipeViewTotalWidth, 0);
                        }else if(mVelocityTracker.getXVelocity() > 500){
                            smoothScrollTo(0, 0);
                        }else if ((scrollX > 0 && scrollX < mSwipeViewTotalWidth / 2)) {//关闭
                            smoothScrollTo(0, 0);
                            isSwipeViewShown = false;
                        } else if (scrollX >= mSwipeViewTotalWidth / 2) {
                            smoothScrollTo(mSwipeViewTotalWidth, 0);
                        }
                    }
                }
                break;
            }

        }
        return true;
    }

    private void processOnDown(MotionEvent ev) {
        int start = getWidth();
        for(int i = mSwipeViews.size() - 1 ; i >= 0;i--){
            View swipeView = mSwipeViews.get(i);
            int curWidth = swipeView.getWidth();
            if(start - curWidth < mDownX){
                swipeView.dispatchTouchEvent(ev);
                //swipeView.performClick();
                return;
            }
            start = start - curWidth;
        }
        smoothScrollTo(0,0);
    }



    /**
     * 高和父view一样，宽自定义
     * @param v
     * @param width
     */
    public void addSwipeItem(final View v, final int width){
        mSwipeViews.add(v);
        this.post(new Runnable() {
            @Override
            public void run() {
                int w = View.MeasureSpec.makeMeasureSpec(width,
                        View.MeasureSpec.EXACTLY);
                int h = View.MeasureSpec.makeMeasureSpec(getHeight(),
                        View.MeasureSpec.EXACTLY);
                v.measure(w,h);
                v.layout(0,0,v.getMeasuredWidth(),v.getMeasuredHeight());
                //Log.i("view","son width:"+v.getWidth()+"height:"+v.getHeight());
                mSwipeViewTotalWidth += v.getWidth();
            }
        });
    }

    public void closeSwipe() {
        smoothScrollTo(0, 0);
        isSwipeViewShown = false;
    }

    public void closeSwipeInstantly(){
        scrollTo(0, 0);
        isSwipeViewShown = false;
    }

    public void clearSwipeView(){
        mSwipeViews.clear();
        //requestLayout();
    }


    //    private class SwipeOnGestureListener extends GestureDetector.SimpleOnGestureListener{
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            return super.onSingleTapUp(e);
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//
//
//            return super.onScroll(e1, e2, distanceX, distanceY);
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            return super.onFling(e1, e2, velocityX, velocityY);
//        }
//    }
    public boolean isSwipeViewShown() {
        return isSwipeViewShown;
    }
}
