package com.ourcompany.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.ourcompany.utils.InputMethodUtils;

/**
 * 点击软键盘区域以外自动关闭软键盘
 * @author xh2009cn
 */
public class CloseKeyboardOnOutsideContainer extends FrameLayout {

    /**
     * @param context context
     */
    public CloseKeyboardOnOutsideContainer(Context context) {
        this(context, null);
    }

    /**
     * @param context context
     * @param attrs attrs
     */
    public CloseKeyboardOnOutsideContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public CloseKeyboardOnOutsideContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean isKeyboardShowing = InputMethodUtils.isKeyboardShowing();
        boolean isEmotionPanelShowing = InputMethodUtils.isEmotionPanelShowing();
        if ((isKeyboardShowing || isEmotionPanelShowing) && event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchY = (int) (event.getY());
            if (InputMethodUtils.isTouchKeyboardOutside(touchY)) {
                if (isKeyboardShowing) {
                    InputMethodUtils.hideKeyboard();
                }
                if (isEmotionPanelShowing) {
                    InputMethodUtils.hideEmotionPanel();
                }
            }
        }
        return super.onTouchEvent(event);
    }


}
