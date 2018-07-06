package com.ourcompany.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.ourcompany.widget.CloseKeyboardOnOutsideContainer;

/**
 * @author xh2009cn
 */
public class InputMethodUtils {


    private static ViewTreeObserver viewTreeObserver;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    /**
     * 监听软键盘弹出/关闭接口
     */
    public interface OnKeyboardEventListener {
        /**
         * 软键盘弹出
         *
         * @param keyboardHeight
         */
        void onSoftKeyboardOpened(int keyboardHeight);

        /**
         * 软键盘关闭
         */
        void onSoftKeyboardClosed();


        boolean isEmotionPanelShowing();

        void hideEmotionPanel();
    }


    private static boolean sIsKeyboardShowing;
    static OnKeyboardEventListener onKeyboardEventListener;
    private static int sKeyBoardHeight = DisplayUtils.getDefaultKeyboardHeight();

    public static boolean isKeyboardShowing() {
        return sIsKeyboardShowing;
    }

    public static void setKeyboardShowing(boolean show) {
        sIsKeyboardShowing = show;
    }

    public static int getKeyBoardHeight() {
        return sKeyBoardHeight;
    }

    public static void setKeyBoardHeight(int keyBoardHeight) {
        sKeyBoardHeight = keyBoardHeight;
    }

    /**
     * 隐藏输入法
     */
    public static void hideKeyboard() {
        View currentFocusView = ((Activity) onKeyboardEventListener).getCurrentFocus();
        if (currentFocusView != null) {
            IBinder token = currentFocusView.getWindowToken();
            if (token != null) {
                InputMethodManager im = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, 0);
            }
        }
    }


    /**
     * 隐藏输入法
     */
    public static void hideKeyboard(  View currentFocusView ) {
        if (currentFocusView != null) {
            IBinder token = currentFocusView.getWindowToken();
            if (token != null) {
                InputMethodManager im = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, 0);
            }
        }
    }

    /**
     * 开关输入法
     *
     * @param currentFocusView 当前焦点view
     */
    public static void toggleSoftInput(View currentFocusView) {
        InputMethodManager imm = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(currentFocusView, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void toggleSoftInputForEt(View currentFocusView) {
        currentFocusView.setFocusableInTouchMode(true);
        currentFocusView.setFocusable(true);
        currentFocusView.requestFocus();
        currentFocusView.findFocus();
        InputMethodManager imm = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(currentFocusView, InputMethodManager.RESULT_SHOWN);

    }

    /**
     * 是否点击软键盘和输入法外面区域
     *
     * @param
     * @param touchY 点击y坐标
     */
    public static boolean isTouchKeyboardOutside(int touchY) {
        View editText = ((Activity) onKeyboardEventListener).getCurrentFocus();
        if (editText == null) {
            return false;
        }
        int[] location = new int[2];
        editText.getLocationOnScreen(location);
        int editY = location[1] - DisplayUtils.getStatusBarHeight();
        int offset = touchY - editY;
        if (offset > 0 && offset < editText.getMeasuredHeight()) {
            return false;
        }
        return true;
    }

    public static void enableCloseKeyboardOnTouchOutside(Activity activity) {
        CloseKeyboardOnOutsideContainer frameLayout = new CloseKeyboardOnOutsideContainer(activity);
        activity.addContentView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    /**
     * 只有当Activity的windowSoftInputMode设置为adjustResize时才有效
     */
    public static void detectKeyboard(final Activity activity, final OnKeyboardEventListener listener) {
        onKeyboardEventListener = listener;
      final   View  activityRootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (activityRootView != null) {
            viewTreeObserver = activityRootView.getViewTreeObserver();
            if (viewTreeObserver != null) {
                onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final Rect r = new Rect();
                        activityRootView.getWindowVisibleDisplayFrame(r);
                        int heightDiff = DisplayUtils.getWindowHeight(activity) - (r.bottom - r.top);
                        boolean show = heightDiff >= sKeyBoardHeight / 2;
                        if (show ^ sIsKeyboardShowing) {
                            sIsKeyboardShowing = show;
                            if (show) {
                                int keyboardHeight = heightDiff - DisplayUtils.getStatusBarHeight();
                                if (keyboardHeight != sKeyBoardHeight) {
                                    sKeyBoardHeight = keyboardHeight;
                                }
                                if (listener != null) {
                                    listener.onSoftKeyboardOpened(keyboardHeight);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onSoftKeyboardClosed();
                                }
                            }
                        }
                    }
                };
                viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
            }
        }
    }

    public static void updateSoftInputMethod(Activity activity, int softInputMode) {
        if (!activity.isFinishing()) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            if (params.softInputMode != softInputMode) {
                params.softInputMode = softInputMode;
                activity.getWindow().setAttributes(params);
            }
        }
    }


    public static boolean isEmotionPanelShowing() {
        return onKeyboardEventListener.isEmotionPanelShowing();
    }

    public static void hideEmotionPanel() {
        if (onKeyboardEventListener != null) {
            onKeyboardEventListener.hideEmotionPanel();
        }
    }

    /**
     * 将布局的监听器给注销
     */
    public static void removeListenser(){
        if(viewTreeObserver!=null && onGlobalLayoutListener!=null&& viewTreeObserver.isAlive()){
            viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
    }

}
