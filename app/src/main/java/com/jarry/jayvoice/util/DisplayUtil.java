package com.jarry.jayvoice.util;

import android.content.Context;
import android.view.WindowManager;

public class DisplayUtil {
	 public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
     }
	 
	 public static int getWindowHight(Context context) {
			// TODO Auto-generated method stub
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		return height;
	 }
	 
	 public static int getDpSize(int size,Context context){			
			return (int) (size*context.getResources().getDisplayMetrics().density);
	 }
	 
	 public static int getWindowWidth(Context context) {
			// TODO Auto-generated method stub
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	 }
	 public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
	 } 
	 public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
	 }  
	   
	 public static int dip2px(Context context, float dipValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dipValue * scale + 0.5f);  
	 }  

}
