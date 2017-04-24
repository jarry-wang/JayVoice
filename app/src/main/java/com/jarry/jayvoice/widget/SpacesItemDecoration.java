package com.jarry.jayvoice.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by mlfdev1 on 2017/4/24.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    StaggeredGridLayoutManager layoutManager;

    public SpacesItemDecoration(int space, StaggeredGridLayoutManager layoutManager) {
        this.space=space;
        this.layoutManager = layoutManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = layoutParams.getSpanIndex();
        outRect.bottom=space;
        if(parent.getChildAdapterPosition(view) < layoutManager.getSpanCount()){
            outRect.top=space;
        }
        if (spanIndex == 0){
            outRect.left=space;
            outRect.right=space/2;
        }else {
            outRect.left=space/2;
            outRect.right=space;
        }
    }
}