package com.jarry.jayvoice.base;

import java.lang.reflect.Field;
import java.util.List;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.VideoPlayActivity;
import com.jarry.jayvoice.bean.Recommend;
import com.jarry.jayvoice.activity.main.fragment.BaseFragment;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.widget.CirclePageIndicator;
import com.jarry.jayvoice.widget.FixedSpeedScroller;
import com.jarry.jayvoice.widget.LoopPagerAdapter;

import cn.join.android.ui.widget.JazzyViewPager;
import cn.join.android.ui.widget.JazzyViewPager.TransitionEffect;


import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AdFragment extends BaseFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 广告banner相关
	 */
	public boolean isRun = false;
	public boolean isDown = false;
	public JazzyViewPager topPager;
//	public int currentAdPage;
	public ProgressBar progressBar;
	public HandlerThread adHandlerThread;
	public Handler adHandler;
	public CirclePageIndicator indicator;
	String[] effects;
	public final int AD_DURATION = 10000;
	
	Handler adUiHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				refreshAd();
				break;
			}	
		};
	};
	
	public void initViewPager() {
		// TODO Auto-generated method stub
		effects = mActivity.getResources().getStringArray(R.array.jazzy_effects);
		 try {
				Field mScroller = null;  
				mScroller = ViewPager.class.getDeclaredField("mScroller");  
				mScroller.setAccessible(true);   
				FixedSpeedScroller scroller = new FixedSpeedScroller( topPager.getContext( ) );  
				mScroller.set( topPager, scroller);
			} catch (Exception e) {	
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 topPager.setTransitionEffect(TransitionEffect.Standard);
//		 topPager.setPageMargin(20);
	}
	
	public void initAd() {
		// TODO Auto-generated method stub
		clearAdHandler();
		isRun = true;
		adHandlerThread = new HandlerThread("adUpdate");
		adHandlerThread.start();
		adHandler = new Handler(adHandlerThread.getLooper(), new Handler.Callback() {		
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					adUiHandler.sendEmptyMessage(0);				
					if (isRun && !isDown) {
						changeAdPage();
					}
					break;
				}			
				return true;
			}
		});
		changeAdPage();
		topPager.setOnTouchListener(new View.OnTouchListener() {
			//
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					isRun = false;
					isDown = true;
					adHandler.removeCallbacksAndMessages(null);
				} else if (action == MotionEvent.ACTION_MOVE) {
					isDown = true;
					isRun = false;
					adHandler.removeCallbacksAndMessages(null);
				} else if (action == MotionEvent.ACTION_UP) {
					isRun = true;
					isDown = false;
					adHandler.removeCallbacksAndMessages(null);
					changeAdPage();
				}
				return false;
			}
		});
	}
	
	public void clearAdHandler() {
		// TODO Auto-generated method stub
		isRun = false;
		if(adHandlerThread!=null){
			adHandlerThread.quit();
			adHandlerThread = null;
			adHandler = null;
		}
	}
	
	public void changeAdPage(){
		if(adHandler!=null){
			 adHandler.sendEmptyMessageDelayed(0, AD_DURATION);
		}
			
	}
	
	public void refreshAd(){
		if (topPager!=null) {
			topPager.setCurrentItem(topPager.getCurrentItem() + 1, true);	
		}
	}

	public void showAdProcess() {
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void dismissAdProcess() {
		progressBar.setVisibility(View.GONE);
	}
	
	public class PAdapter extends LoopPagerAdapter implements OnClickListener {
		List<com.jarry.jayvoice.bean.Recommend> recommends;
		public PAdapter(List<com.jarry.jayvoice.bean.Recommend> activitys){
			this.recommends = activitys;
		}
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeView((View) object);
			container.removeView(topPager.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(container.getContext()).inflate(
					R.layout.pager_item_poster, null);
			Recommend activity = recommends.get(position % recommends.size());
			ImageView img = (ImageView) view.findViewById(R.id.img);
			if(activity.image!=null){
				if(StringUtils.isNotNull(activity.image.getFileUrl())){
					mFetcher.loadImage(activity.image.getFileUrl(), img);
				}
			}else{
				if(activity.video!=null){
					mFetcher.loadImage(activity.video.image.getFileUrl(), img);
				}
			}
			
			TextView textView = (TextView) view.findViewById(R.id.name);
			if(activity.video!=null&&StringUtils.isNotNull(activity.video.name))
				textView.setText(activity.video.name);
			container.addView(view, 0);
			view.setTag(activity);
			view.setOnClickListener(this);
			topPager.setObjectForPosition(view, position);
			return view;
		}

		@Override
		public void onClick(View v) {
			Recommend recommend =  (Recommend) v.getTag();
			Intent intent = new Intent(mActivity,VideoPlayActivity.class);
			intent.putExtra("video", recommend.video);
			if(recommend.video!=null)
				intent.putExtra("typeId", recommend.video.typeId);
			startActivity(intent);
		}

		@Override
		public int getRealCount() {
			return recommends.size();
		}

	}


}
