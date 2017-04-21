package com.jarry.jayvoice.util;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	public static int setListViewHeightBasedOnChildren(ListView listView,Context context) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return 0;  
        }  

        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
       
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + DisplayUtil.getDpSize(50, context);
        params.height = height;  
        listView.setLayoutParams(params);  
        return height;
    }  
	
	public static int setViewPagerHeightBasedOnChildren(ViewPager viewPager,Context context,int pageNum) {  
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null || viewPager == null) {  
            // pre-condition  
            return 0;  
        }  
        int totalHeight = 0;  
        View pagerItem = viewPager.getChildAt(pageNum);        
        pagerItem.measure(0, 0);  
        totalHeight += pagerItem.getMeasuredHeight(); 
        
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();  
        int height = totalHeight;
        params.height = height;  
        viewPager.setLayoutParams(params);  
        return height;
    }  

}
