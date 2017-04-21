package com.jarry.jayvoice.widget;

import com.jarry.jayvoice.util.DisplayUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView{

	Context context;
	int mScreenWidth;
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mScreenWidth = DisplayUtil.getWindowWidth(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		int length = (mScreenWidth - DisplayUtil.getDpSize(40, context))/3;
		getLayoutParams().width = length;
		getLayoutParams().height = length;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
