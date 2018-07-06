package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Administrator on 2017/8/23.
 *
 * 该类
 * 1处理嵌套RecyclerView
 * 2在嵌套RecyclerView 快速滑动时，事件没及时处理
 */

public class SRecyclerView extends RecyclerView {

    private static final String TAG = "SRecyclerView";
    private int mScrollPointerId;
    private int mInitialTouchY;
    private int mInitialTouchX;
    private int mTouchSlop;

    public SRecyclerView(Context context) {
        this(context,null);
    }

    public SRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setScrollingTouchSlop(int slopConstant) {
        super.setScrollingTouchSlop(slopConstant);
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        switch (slopConstant) {
            default:
                Log.w(TAG, "setScrollingTouchSlop(): bad argument constant "
                        + slopConstant + "; using default value");
                // fall-through
            case TOUCH_SLOP_DEFAULT:
                mTouchSlop = vc.getScaledTouchSlop();
                break;

            case TOUCH_SLOP_PAGING:
                mTouchSlop = vc.getScaledPagingTouchSlop();
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        /**
         * 这些代码是从RecyclerView copy 修改的
         */
        if (e ==null){
            return false;
        }
        final int action = MotionEventCompat.getActionMasked(e);
        final int actionIndex = MotionEventCompat.getActionIndex(e);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //这里主要是获取手指按下的位置，然后什么不用做了，
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX =  (int) (e.getX() + 0.5f);
                mInitialTouchY =  (int) (e.getY() + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEventCompat.ACTION_POINTER_DOWN:
                mScrollPointerId = e.getPointerId(0);
                mInitialTouchX =  (int) (e.getX(actionIndex) + 0.5f);
                mInitialTouchY =  (int) (e.getY(actionIndex) + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_MOVE: {
                final int index = e.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    return false;
                }

                final int x = (int) (e.getX(index) + 0.5f);
                final int y = (int) (e.getY(index) + 0.5f);
                if (getScrollState() != SCROLL_STATE_DRAGGING) {
                    final int dx = x - mInitialTouchX;
                    final int dy = y - mInitialTouchY;
                    boolean startScroll = false;
                   /***
                    * getLayoutManager().canScrollVertically() [可以不需要，但是
                    * 更严谨些，扩大范围]|| Math.abs(dx)>Math.abs(dy)
                    *getLayoutManager().canScrollHorizontally()也是
                    */
                    if ((getLayoutManager().canScrollHorizontally() && Math.abs(dx) > mTouchSlop) &&
                            (getLayoutManager().canScrollVertically() || Math.abs(dx)> Math.abs(dy))) {
                        startScroll = true;
                    }
                    if ((getLayoutManager().canScrollVertically() && Math.abs(dy) > mTouchSlop) &&
                            (getLayoutManager().canScrollHorizontally() || Math.abs(dy)> Math.abs(dx)) ) {
                        startScroll = true;
                    }
                   return startScroll && super.onInterceptTouchEvent(e);
                }
            }
            default:
                return super.onInterceptTouchEvent(e);
        }
    }

    /**
     * 这个接口的作用是，不允许父类打断TouchEvent 事件
     * 那么这里只需要空实现就行，这个SRecyclerView类的跟父类RecyclerView相反的功能
     * @param disallowIntercept
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}
