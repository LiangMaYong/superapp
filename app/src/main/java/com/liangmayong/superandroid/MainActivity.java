package com.liangmayong.superandroid;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.liangmayong.base.BaseActivity;
import com.liangmayong.base.bind.annotations.ColorId;
import com.liangmayong.base.bind.annotations.Layout;
import com.liangmayong.base.bind.annotations.Title;
import com.liangmayong.base.widget.iconfont.Icon;
import com.liangmayong.base.widget.pullrefresh.PullRefreshLayout;
import com.liangmayong.base.widget.pullrefresh.drawables.PictureDrawable;
import com.liangmayong.loading.Loading;
import com.liangmayong.preferences.annotations.PreferenceValue;

@Layout(R.layout.activity_main)
@Title("AndroidBase")
public class MainActivity extends BaseActivity {

    @PreferenceValue("key")
    String app_name;

    @ColorId
    int colorPrimary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getDefualtToolbar().setTitle(app_name);

        setThemeColor(colorPrimary);
        getDefualtToolbar().leftOne().iconToLeft(Icon.icon_back).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDefualtToolbar().leftTwo().iconToRight(Icon.icon_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeColor(0xff336666);
            }
        });
        getDefualtToolbar().leftThree().iconToRight(Icon.icon_message).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeColor(0xff663366);
            }
        });
        getDefualtToolbar().rightOne().iconToRight(Icon.icon_menu).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeColor(0xffff6585);
            }
        });
        getDefualtToolbar().rightTwo().iconToRight(Icon.icon_edit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeColor(0xff3399ff);
            }
        });
        getDefualtToolbar().rightThree().iconToRight(Icon.icon_location).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThemeColor(0xff18a28b);
            }
        });
//        goTo("百度一下", "http://www.baidu.com");

        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull);
        PictureDrawable pictureDrawable = new PictureDrawable(pullRefreshLayout, R.mipmap.loading_bee, R.mipmap.loading_bee1, R.mipmap.loading_bee2, R.mipmap.loading_bee3, R.mipmap.loading_bee4, R.mipmap.loading_bee5, R.mipmap.loading_bee6);
        pullRefreshLayout.setRefreshDrawable(pictureDrawable);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Loading.showLoading(MainActivity.this, "开始加载数据");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefreshLayout.setRefreshing(false);
                        Loading.cancelLoading(MainActivity.this);
                    }
                }, 1000);
            }
        });

    }
}
