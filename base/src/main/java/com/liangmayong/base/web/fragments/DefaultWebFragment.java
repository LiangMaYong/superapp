package com.liangmayong.base.web.fragments;

import android.annotation.SuppressLint;

import com.liangmayong.base.sub.BaseSubWebFragment;
import com.liangmayong.base.sub.webkit.BaseWebView;
import com.liangmayong.base.sub.webkit.BaseWebWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiangMaYong on 2016/10/27.
 */
@SuppressLint("ValidFragment")
public class DefaultWebFragment extends BaseSubWebFragment {


    public DefaultWebFragment(String title, String url) {
        super(title, url);
    }

    public DefaultWebFragment(String title, String url, boolean moreEnabled) {
        super(title, url, moreEnabled);
    }

    // HEADERS
    private static final Map<String, String> HEADERS = new HashMap<String, String>();
    // WIDGETS
    private static final List<BaseWebWidget> WIDGETS = new ArrayList<BaseWebWidget>();

    @Override
    protected void initWebView(BaseWebView rootView) {
        super.initWebView(rootView);
    }

    /**
     * addWebWidget
     *
     * @param scheme    scheme
     * @param webWidget webWidget
     */
    public static void addWebWidget(String scheme, BaseWebWidget webWidget) {
        removeWebWidget(scheme);
        if (webWidget != null) {
            webWidget.setSchemeName(scheme);
            WIDGETS.add(webWidget);
        }
    }

    /**
     * removeWebWidget
     *
     * @param scheme scheme
     */
    public static void removeWebWidget(String scheme) {
        for (int i = 0; i < WIDGETS.size(); i++) {
            if (WIDGETS.get(i).getSchemeName().equals(scheme)) {
                WIDGETS.remove(i);
            }
        }
    }

    /**
     * addHeader
     *
     * @param key   key
     * @param value value
     */
    public static void addHeader(String key, String value) {
        if (value == null) {
            if (HEADERS.containsKey(key)) {
                HEADERS.remove(key);
            }
        } else {
            HEADERS.put(key, value);
        }
    }

    /**
     * addHeaders
     *
     * @param headers headers
     */
    public static void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            HEADERS.putAll(headers);
        }
    }

    /**
     * clearHeaders
     */
    public static void clearHeaders() {
        HEADERS.clear();
    }

    @Override
    protected Map<String, String> getHeaders() {
        return HEADERS;
    }

    @Override
    protected List<BaseWebWidget> getWidgets() {
        return WIDGETS;
    }

}