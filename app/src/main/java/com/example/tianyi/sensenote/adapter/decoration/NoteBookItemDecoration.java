package com.example.tianyi.sensenote.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import com.example.tianyi.sensenote.adapter.NoteBookAdapter;
import com.example.tianyi.sensenote.util.DensityUtils;

public class NoteBookItemDecoration extends RecyclerView.ItemDecoration{

    private final Context mContext;
    private NoteBookAdapter mAdapter;
    private Rect headerRect;
    private View headerView;
    private float mDividerHeight;

    private int marginLeft;

    private Paint mPaint;


    public NoteBookItemDecoration(Context context, NoteBookAdapter mAdapter) {
        this.mAdapter = mAdapter;
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        marginLeft = 20;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int headerHeight = 0;
        if (position != RecyclerView.NO_POSITION && hasHeader(position)) {
            //获取到ItemDecoration所需要的高度
            View header = getHeader(parent, position);
            headerHeight = header.getHeight();
        }
        mDividerHeight = 1;
        outRect.set(0, headerHeight, 0, 1);
    }

    public View getHeader(RecyclerView parent, int position) {
        final int headerId = mAdapter.getHeaderId(position);
        View header = headerView;
        if (header == null) {
            NoteBookAdapter.NoteBookStrickyHeadViewHolder holder = mAdapter.createStrickyHeader(parent);
            header = holder.itemView;
            //绑定数据 暂时用不到
            //mAdapter.onBindHeaderViewHolder(holder, position);
            //测量View并且layout
            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.EXACTLY);
            //根据父View的MeasureSpec和子view自身的LayoutParams以及padding来获取子View的MeasureSpec
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), header.getLayoutParams().height);
            //进行测量
            header.measure(childWidth, childHeight);
            //根据测量后的宽高放置位置
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
            //将创建好的头部view保存在数组中，避免每次重复创建
            headerView = header;
        }
        return header;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        for ( int i = 0; i < childCount; i++ ) {
            View view = parent.getChildAt(i);

            int index = parent.getChildAdapterPosition(view);
            //第一个ItemView不需要绘制
            if ( index == 0 || index == mAdapter.getItemCount() - 1) {
                continue;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();

            float dividerTop = view.getBottom() - mDividerHeight;
            float dividerLeft = parent.getPaddingLeft() + marginLayoutParams.leftMargin;
            float dividerBottom = view.getBottom();
            float dividerRight = parent.getWidth() - parent.getPaddingRight();

            c.drawRect(dividerLeft,dividerTop,dividerRight,dividerBottom,mPaint);
        }
    }

    /**
     * 判断是否有header
     *
     * @param position
     * @return
     */
    private boolean hasHeader(int position) {
        return mAdapter.hasHeader(position);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int count = parent.getChildCount();
        //遍历屏幕上加载的item
        for (int layoutPos = 0; layoutPos < count; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            //获取该item在列表数据中的位置
            final int adapterPos = parent.getChildAdapterPosition(child);
            //只有在最上面一个item或者有header的item才绘制header
            if (adapterPos != RecyclerView.NO_POSITION && ((layoutPos == 0 && adapterPos >= 1 ) || hasHeader(adapterPos))) {
                View header = getHeader(parent, adapterPos);
                c.save();
                //获取绘制header的起始位置(left,top)
                final int left = 0;
                final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                //将画布移动到绘制的位置
                c.translate(left, top);
                //绘制header
                header.draw(c);
                c.restore();
                //保存绘制的header的区域
                headerRect = new Rect(left, top, left + header.getWidth(), top + header.getHeight());
            }
        }
    }

//    @Override
//    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
//        final int count = parent.getChildCount();
//        //遍历屏幕上加载的item
//        for (int layoutPos = 0; layoutPos < count; layoutPos++) {
//            final View child = parent.getChildAt(layoutPos);
//            //获取该item在列表数据中的位置
//            final int adapterPos = parent.getChildAdapterPosition(child);
//            //只有在最上面一个item或者有header的item才绘制header
//            if (adapterPos != RecyclerView.NO_POSITION && (layoutPos == 0 || hasHeader(adapterPos))) {
//                View header = getHeader(parent, adapterPos);
//                c.save();
//                //获取绘制header的起始位置(left,top)
//                final int left = child.getLeft();
//                final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
//                //将画布移动到绘制的位置
//                c.translate(left, top);
//                //绘制header
//                header.draw(c);
//                c.restore();
//                //保存绘制的header的区域
//                headerRect = new Rect(left, top, left + header.getWidth(), top + header.getHeight());
//            }
//        }
//    }

    private int getHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos) {
        int headerHeight = header.getHeight();
        int top = ((int) child.getY()) - headerHeight;
        //在绘制最顶部的header的时候，需要考虑处理两个分组的header交换时候的情况
        if (layoutPos == 0) {
            final int count = parent.getChildCount();
            final int currentId = mAdapter.getHeaderId(adapterPos);
            //从第二个屏幕上线上的第二个item开始遍历
            for (int i = 1; i < count; i++) {
                int nextpos = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (nextpos != RecyclerView.NO_POSITION) {
                    int nextId = mAdapter.getHeaderId(nextpos);
                    //找到下一个不同组的view
                    if (currentId != nextId) {
                        final View next = parent.getChildAt(i);
                        //当不同组的第一个view距离顶部的位置减去两组header的高度，得到offset
                        final int offset = ((int) next.getY()) - (headerHeight + getHeader(parent, nextpos).getHeight());
                        //offset小于0即为两组开始交换，第一个header被挤出界面的距离
                        if (offset < 0) {
                            return offset;
                        } else {
                            break;
                        }
                    }
                }
            }
            top = Math.max(0, top);
        }
        return top;
    }

    public int findHeaderPositionUnder(int x, int y) {
        //遍历屏幕上header的区域，判断点击的位置是否在某个header的区域内
        if(headerRect.contains(x,y)){
            return 1;
        }
        return -1;
    }


    public boolean findHeaderClickView(RecyclerView parent,View view, int x, int y) {
        if (view == null) return false;
        NoteBookAdapter adapter = (NoteBookAdapter)parent.getAdapter();
        if (headerRect.contains(x, y)) {
            Rect vRect = new Rect();
            // 需要响应点击事件的区域在屏幕上的坐标
            View topView = parent.getChildAt(0);
            int topViewPostion = parent.getChildAdapterPosition(topView);
            int top = view.getTop();
            if(topViewPostion == 0 && adapter.getHeaderView() != null){
                top += topView.getBottom();
            }
            vRect.set(vRect.left + view.getLeft(), vRect.top + top, vRect.left + view.getLeft() + view.getWidth(), vRect.top + top + view.getHeight());
            return vRect.contains(x, y);
        }
        return false;
    }
}
