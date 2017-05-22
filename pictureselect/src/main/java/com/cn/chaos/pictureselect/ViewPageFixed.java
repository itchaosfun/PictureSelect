package com.cn.chaos.pictureselect;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by HSAEE on 2017/5/19.
 * 修复viewpager的系统bug,当使用photo进行缩放并进行滑动时 会报java.lang.IllegalArgumentException: pointerIndex out of range
 * 需要重写viewpager的onInterceptTouchEvent()事件,并进行try catch异常抓取
 */

public class ViewPageFixed extends ViewPager {
    public ViewPageFixed(Context context) {
        super(context);
    }
    public ViewPageFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
