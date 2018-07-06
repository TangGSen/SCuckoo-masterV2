package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ourcompany.R;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/2 21:42
 * Des    :
 */

public class SimpleDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;     //分割线Drawable
    private int mDividerHeight;  //分割线高度

    /**
     * 使用line_divider中定义好的颜色
     *
     * @param context
     * @param dividerHeight 分割线高度
     */
    public SimpleDecoration(Context context, int dividerHeight) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.recycle_line_divider);
        mDividerHeight = dividerHeight;
    }

    /**
     * @param context
     * @param divider       分割线Drawable
     * @param dividerHeight 分割线高度
     */
    public SimpleDecoration(Context context, int divider, int dividerHeight) {
        if (divider > 0) {
            mDivider = ContextCompat.getDrawable(context, divider);
        } else {
            mDivider = ContextCompat.getDrawable(context, R.drawable.recycle_line_divider);
        }
        mDividerHeight = dividerHeight;
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDividerHeight);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
