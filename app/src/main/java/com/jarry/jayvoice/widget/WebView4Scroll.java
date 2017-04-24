package com.jarry.jayvoice.widget;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by wangfj on 2017/4/24.
 */

public class WebView4Scroll extends WebView {

    public interface ScroellListener {
            void canRefrsh(boolean canRefresh);
    }
    private ScroellListener scrollListener;

    public WebView4Scroll(Context context,ScroellListener scrollListener){
        super(context);
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollListener.canRefrsh(this.getScrollY() == 0);
    }
}
