package com.example.tianyi.sensenote.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.example.tianyi.sensenote.R;
import com.example.tianyi.sensenote.adapter.decoration.NoteBookItemDecoration;
import com.example.tianyi.sensenote.viewgroup.SwipeLeftLinearLayout;

public class StickyRecyclerHeadersTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mTapDetector;
    private RecyclerView mRecyclerView;
    private NoteBookItemDecoration mDecor;

    public StickyRecyclerHeadersTouchListener(RecyclerView mRecyclerView, NoteBookItemDecoration mDecor) {
        mTapDetector = new GestureDetectorCompat(mRecyclerView.getContext(), new SingleTapDetector());
        this.mRecyclerView = mRecyclerView;
        this.mDecor = mDecor;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent e) {
        //将事件交给GestureDetector类进行处理，通过onSingleTapUp返回的值，判断是否要拦截事件
        Log.i("touch","itemView intercept");
        processSwipeExit(recyclerView,e);
        boolean tapDetectorResponse = this.mTapDetector.onTouchEvent(e);
        if (tapDetectorResponse) {
            // Don't return false if a single tap is detected
            Log.i("touch","cosumed");
            return true;
        }
        //Log.i("touch","itemView intercept false");
//        //如果是点击在header区域，则拦截事件
//        if (e.getAction() == MotionEvent.ACTION_DOWN) {
//            int position = mDecor.findHeaderPositionUnder((int) e.getX(), (int) e.getY());
//            return position != -1;
//        }
        return false;
    }

    private void processSwipeExit(RecyclerView recyclerView, MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:{
                for(int i = 0; i < recyclerView.getChildCount();i++){
                    if(i == 0) continue;
                    SwipeLeftLinearLayout view = (SwipeLeftLinearLayout) recyclerView.getChildAt(i);
                    if(view.isSwipeViewShown() && !isSelectedView(e,view)) {
                        //Log.i("click","close");
                        view.closeSwipe();
                    }
                    //Log.i("click","view is istance of viewholder:"+ (SwipeLeftLinearLayout.class.isInstance(view)));
                }
                break;
            }
            default:
                break;
        }
    }

    public boolean isSelectedView(MotionEvent e,SwipeLeftLinearLayout view){
        //Log.i("click","view top:" + view.getTop() + " view bootom:" + view.getBottom() + " click y:" + e.getY());
        return (view.getTop() < e.getY()) && (view.getBottom() > e.getY());
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    private class SingleTapDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //根据点击的坐标查找是不是点击在header的区域
            int position = mDecor.findHeaderPositionUnder((int) e.getX(), (int) e.getY());
            Log.i("recyclerView","position:" + position);
            if (position != -1) {
                //如果position不等于-1,则表示点击在header区域，然后在判断是否在header需要响应的区域
                View headerView = mDecor.getHeader(mRecyclerView, position);
                View view1 = headerView.findViewById(R.id.imgView_notebook_addnotebook);
                if (mDecor.findHeaderClickView(mRecyclerView,view1, (int) e.getX(), (int) e.getY())) {
                    //如果在header需要响应的区域，该区域的view模拟点击
                    view1.performClick();
                }
                mRecyclerView.playSoundEffect(SoundEffectConstants.CLICK);
                headerView.onTouchEvent(e);
                return true;
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }
}
