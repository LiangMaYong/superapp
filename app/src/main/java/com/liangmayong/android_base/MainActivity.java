package com.liangmayong.android_base;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.liangmayong.base.BaseActivity;
import com.liangmayong.base.widget.iconfont.Icon;
import com.liangmayong.base.widget.pullrefresh.PullRefreshLayout;
import com.liangmayong.base.widget.pullrefresh.drawables.PictureDrawable;
import com.liangmayong.skin.Skin;
import com.liangmayong.viewbinding.annotations.BindLayout;
import com.liangmayong.viewbinding.annotations.BindTitle;
import com.liangmayong.viewbinding.annotations.OnClick;

@BindTitle("AndroidBase")
public class MainActivity extends BaseActivity {

    @OnClick(R.id.sbutton)
    private void btn() {
        goTo("百度一下", "http://www.baidu.com");
    }

    // colors
    private int[] colors = {0xff336666, 0xff663366, 0xff3399ff, 0xff18a28b, 0xffff6585};
    // index
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDefualtToolbar().leftOne().iconToLeft(Icon.icon_back).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final PullRefreshLayout pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pull);
        PictureDrawable pictureDrawable = new PictureDrawable(pullRefreshLayout, R.mipmap.loading_bee, R.mipmap.loading_bee1, R.mipmap.loading_bee2, R.mipmap.loading_bee3, R.mipmap.loading_bee4, R.mipmap.loading_bee5, R.mipmap.loading_bee6);
        pullRefreshLayout.setRefreshDrawable(pictureDrawable);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index++;
                if (index > colors.length - 1) {
                    index = 0;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefreshLayout.setRefreshing(false);
                        Skin.editor().setThemeColor(colors[index], 0xffffffff).commit();
                    }
                }, 0);
            }
        });

    }
}
