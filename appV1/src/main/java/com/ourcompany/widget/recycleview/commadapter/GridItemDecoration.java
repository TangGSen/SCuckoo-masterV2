package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/8/30.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDrawable;

    public GridItemDecoration(Context context, int resDrawableId) {
        mDrawable = ContextCompat.getDrawable(context, resDrawableId);
    }

    /**
     * 这个方法是留出分割线的位置
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int bottom = mDrawable.getIntrinsicHeight();
        int right = mDrawable.getIntrinsicHeight();
        //最后一列，不留
        if (isLastColumn(view, parent)) {

            right = 0;
        }
        //最后一行
        if (isLastRows(view, parent)) {
            bottom = 0;
        }

        outRect.bottom = bottom;
        outRect.right = right;

    }

    private boolean isLastRows(View view, RecyclerView parent) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            //列数
            int spanCount = manager.getSpanCount();
            //行数
            int rowCount = parent.getAdapter().getItemCount() % spanCount == 0 ?
                    parent.getAdapter().getItemCount() / spanCount : (parent.getAdapter().getItemCount() / spanCount) + 1;

            return (position + 1) > (rowCount - 1) * spanCount;
        }
        return false;
    }

    private boolean isLastColumn(View view, RecyclerView parent) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            //列数
            int spanCount = manager.getSpanCount();
            return (position + 1) % spanCount == 0;
        }
        return false;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawvertical(c, parent);

    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            int left = view.getLeft() - layoutParams.leftMargin;
            int right = view.getRight() + mDrawable.getIntrinsicWidth() + layoutParams.rightMargin;
            int top = view.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    private void drawvertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            int left = view.getRight() + layoutParams.rightMargin;
            int right = left + mDrawable.getIntrinsicWidth();
            int top = view.getTop() - layoutParams.topMargin;
            int bottom = view.getBottom() + layoutParams.bottomMargin;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

}
