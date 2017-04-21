package com.jarry.jayvoice.activity;

import java.util.ArrayList;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.widget.SmoothImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import cn.join.android.net.imgcache.ImageFetcher;
import cn.join.android.net.imgcache.SharedImageFetcher;


public class CommonImageActivity extends Activity{

//	SmoothImageView smoothImageView; 
	private String url;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	SmoothImageView imageView = null;
	public ImageFetcher mFetcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFetcher = SharedImageFetcher.getSharedFetcher(this);
		url = getIntent().getStringExtra("url");
		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);
		imageView = new SmoothImageView(this);
		imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		imageView.transformIn();
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);			
		setContentView(imageView);
		if(StringUtils.isNotNull(url)){
			mFetcher.loadImage(url, imageView);
		}else{
			imageView.setImageResource(R.drawable.userpic);
		}
		imageView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				doOut();
				return false;
			}
		});
	}

	@Override
	public void onBackPressed() {
		doOut();
	}
	
	private void doOut() {
		// TODO Auto-generated method stub
		imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		imageView.transformOut();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}


}
