package com.liangmayong.base.widget.themeview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.liangmayong.base.support.theme.ThemeManager;
import com.liangmayong.base.support.theme.ThemeType;
import com.liangmayong.base.support.theme.listener.OnThemeListener;
import com.liangmayong.base.widget.themeview.interfaces.ThemeViewHandler;
import com.liangmayong.base.widget.themeview.interfaces.ThemeViewHandlerInterface;

/**
 * Created by LiangMaYong on 2017/2/14.
 */
public class ThemeView extends View implements ThemeViewHandlerInterface {

    private ThemeViewHandler handler = null;

    public ThemeView(Context context) {
        super(context);
        init(null);
    }

    public ThemeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ThemeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ThemeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        handler = new ThemeViewHandler(this, new ThemeViewHandler.OnThemeColorListener() {
            @Override
            public void onColor(int color, int textColor) {
                setBackgroundColor(color);
            }
        });
        handler.initAttributeSet(attrs);
        if (isInEditMode()) {
            setBackgroundColor(handler.getThemeColor());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) return;
        ThemeManager.registerThemeListener(handler);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isInEditMode()) return;
        ThemeManager.unregisterThemeListener(handler);
    }

    /**
     * setThemeListener
     *
     * @param themeListener themeListener
     */
    public void setThemeListener(OnThemeListener themeListener) {
        handler.setThemeListener(themeListener);
    }

    /**
     * setThemeType
     *
     * @param themeType themeType
     */
    public void setThemeType(ThemeType themeType) {
        handler.setThemeType(themeType);
    }

    public ThemeType getThemeType() {
        return handler.getThemeType();
    }

    @Override
    public void setThemeColor(int themeColor, int themeTextColor) {
        handler.setThemeColor(themeColor, themeTextColor);
    }

    @Override
    public int getThemeColor() {
        return handler.getThemeColor();
    }

    @Override
    public int getThemeTextColor() {
        return handler.getThemeTextColor();
    }
}
