package com.jarry.jayvoice.widget;

import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView{
	private final String TAG = "SlidingMenu";
	int mScreenWidth;
	private int mMenuRightPadding = 80;
	boolean isOpen;
	private int mMenuWidth;  
	private int mHalfMenuWidth;  
	private boolean once;
	ViewGroup mMenu;
	ViewGroup mContent;
	MainActivity mainActivity;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mScreenWidth = getWindowWidth(context);
		mainActivity = (MainActivity) context;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onMeasure-once="+once);
		/** 
		* ��ʾ������һ����� 
		*/  
		if (!once){  
			LinearLayout wrapper = (LinearLayout) getChildAt(0);  
			mMenu = (ViewGroup) wrapper.getChildAt(0);  
			mContent = (ViewGroup) wrapper.getChildAt(1);  
			// dp to px  
			mMenuRightPadding = (int) TypedValue.applyDimension(  
			TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, mContent  
			.getResources().getDisplayMetrics());  
			
			mMenuWidth = mScreenWidth - mMenuRightPadding; 
			mMenuWidth = mScreenWidth/3;
			mHalfMenuWidth = mMenuWidth / 3;  
			mMenu.getLayoutParams().width = mMenuWidth;  
			mContent.getLayoutParams().width = mScreenWidth;  
		
		}  
		Log.d(TAG, "onMeasure-mMenuWidth="+mMenuWidth);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLayout-changed="+changed+";mMenuWidth="+mMenuWidth+";l="+l);
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo(mMenuWidth, 0);  
			once = true;  
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(mainActivity.getCheckId() == R.id.query_type_home){
			return super.onInterceptTouchEvent(ev); 
		}else{
			return false;
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(mainActivity.getCheckId() == R.id.query_type_home){
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_UP:
				int scroolX = this.getScrollX();
				if(scroolX>mHalfMenuWidth){
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false; 
				}else{
					this.smoothScrollTo(0, 0);
					isOpen = true; 
				}
				return true;
			default:
				break;
			}
			return super.onTouchEvent(ev); 
		}else{
			return false;
		}
		
	}
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onScrollChanged-");
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;  
		float leftScale = 1 - 0.3f * scale;  
		float rightScale = 0.8f + scale * 0.2f;  
		  
		ViewHelper.setScaleX(mMenu, leftScale);  
		ViewHelper.setScaleY(mMenu, leftScale);  
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));  
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.6f);  
		
		ViewHelper.setPivotX(mContent, 0);  
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);  
		ViewHelper.setScaleX(mContent, rightScale);  
		ViewHelper.setScaleY(mContent, rightScale);  

	}

	public int getWindowWidth(Context context) {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}
	
	public int getWindowHight(Context context) {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		return height;
	}
    public void openMenu()  
    {  
    	Log.d(TAG, "openMenu-isOpen="+isOpen);
        if (isOpen)  
            return;  
        this.smoothScrollTo(0, 0);  
        isOpen = true;  
    }  

    public void closeMenu()  
    {  
    	Log.d(TAG, "closeMenu-isOpen="+isOpen);
        if (isOpen)  
        {  
            this.smoothScrollTo(mMenuWidth, 0);  
            isOpen = false;  
        }  
    } 
    
    public void closeMenuQz()  
    {    
    	Log.d(TAG, "closeMenuQz-mMenuWidth="+mMenuWidth);
    	if(mMenuWidth>0){
    		this.smoothScrollTo(mMenuWidth, 0); 
            isOpen = false;
    	}
            
    } 

    public void toggle()  
    {  
    	Log.d(TAG, "toggle-isOpen="+isOpen);
        if (isOpen)  
        {  
            closeMenu();  
        } else  
        {  
            openMenu(); 
        }  
    }

	public boolean isOpen() {
		return isOpen;
	}  

	
}
