package com.example.tianyi.sensenote.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TagLayout extends ViewGroup{

    public TagLayout(Context context) {
        this(context,null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minimumHeight = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int leftWidth = 0;
        for(int i = 0; i < getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,lp.leftMargin + lp.rightMargin,lp.width);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,lp.topMargin + lp.bottomMargin,lp.height);
            child.measure(childWidthSpec,childHeightSpec);
            //Log.i("sensenote","child " + i + " measure width :" + child.getMeasuredWidth() + "  measure height :" + child.getMeasuredHeight());
            if(minimumHeight == 0){ //第一行
                minimumHeight = child.getMeasuredHeight() + lp.topMargin + + lp.bottomMargin;
            }else if(leftWidth + child.getMeasuredWidth() + lp.topMargin + + lp.bottomMargin > width){ //该行空间不足
                minimumHeight = minimumHeight + child.getMeasuredHeight() + lp.topMargin + + lp.bottomMargin;
                leftWidth = width;
            }
            leftWidth = leftWidth + child.getMeasuredWidth() + lp.topMargin + + lp.bottomMargin ;
        }
        setMeasuredDimension(resolveSize(MeasureSpec.getSize(widthMeasureSpec),widthMeasureSpec),resolveSize(minimumHeight,heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Log.i("sensenote","width:" + getMeasuredWidth() + " height: "+ getMeasuredHeight());
        //Log.i("sensenote","left:"+left + " right:"+ right + " top:" + top +"bottom:" + bottom);
        int curLeft = 0, curTop = 0,curBottom = 0;
        int width = getMeasuredWidth();
        for(int i = 0; i < getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth =  child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if(curLeft + childWidth + lp.leftMargin + lp.rightMargin > width){
                curLeft = 0;
                curTop = curBottom;
            }
            setChildFrame(child,curLeft+lp.leftMargin,curTop + lp.topMargin,childWidth,childHeight);
            curLeft = curLeft + lp.leftMargin + childWidth + lp.rightMargin;
            curBottom = curTop + lp.topMargin + childHeight + lp.bottomMargin;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }
}
