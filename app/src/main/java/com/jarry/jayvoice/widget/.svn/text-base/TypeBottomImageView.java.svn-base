package com.jarry.jayvoice.widget;

import com.jarry.jayvoice.util.DisplayUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TypeBottomImageView extends ImageView{

	Context context;
	int mScreenWidth;
	public TypeBottomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mScreenWidth = DisplayUtil.getWindowWidth(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		int length = (mScreenWidth)/3;
		getLayoutParams().width = length;
		getLayoutParams().height = DisplayUtil.getDpSize(2, context);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
