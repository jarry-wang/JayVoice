package com.jarry.jayvoice.base;

import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jarry.jayvoice.activity.BaseActivity;

public abstract class TopTypeActivity extends BaseActivity{

	public RadioGroup mRadioGroup;
	public float typeTabWidth;
	public float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
	public ImageView mTypeImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public void doTypeTextSize(final RadioButton[] mRadioButtons) {
		// TODO Auto-generated method stub
		for(int i = 0;i<mRadioButtons.length;i++){
			final int position = i;
			mRadioButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean flag) {
					// TODO Auto-generated method stub
					if(flag){
						mRadioButtons[position].setTextSize(18);
					}else{
						mRadioButtons[position].setTextSize(17);
					}
				}
			});
		}
	}
	
	public void doImgAnimation(float toX) {
		// TODO Auto-generated method stub
		if(toX==mCurrentCheckedRadioLeft)return;
		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;
		_TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, toX, 0f, 0f);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(true);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		mTypeImageView.startAnimation(_AnimationSet);
	}
}
