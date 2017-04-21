package com.jarry.jayvoice.activity;

import java.util.List;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.Photo;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.widget.CirclePageIndicator;
import com.jarry.jayvoice.widget.HackyViewPager;

import uk.co.senab.photoview.PhotoView;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;


public class PictrueViewpagerActivity extends BaseActivity implements OnPageChangeListener{

	private static final String ISLOCKED_ARG = "isLocked";
	private final String POSITION = "position";
	private ViewPager mViewPager;
	private MenuItem menuLockItem;
	private TextView pageNumView;
	List<Photo> imgs;
	int position;
	@SuppressWarnings("unchecked")
	int pageSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mViewPager = (HackyViewPager) findViewById(R.id.pictrue_view_pager);
		pageNumView = (TextView) findViewById(R.id.pictrue_pagenum);
		position = getIntent().getIntExtra(POSITION, 0);
		imgs = (List<Photo>) getIntent().getSerializableExtra("imgs");
		if(ListUtil.isNotNull(imgs)){
			mViewPager.setAdapter(new SamplePagerAdapter());
			pageSize = imgs.size();
		}		
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		mViewPager.setCurrentItem(position);
		changePageNum(position);
		mViewPager.setOnPageChangeListener(this);		
	}
	
	
	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			mFetcher.loadImage(imgs.get(position).url, photoView);
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

    private void toggleViewPagerScrolling() {
    	if (isViewPagerActive()) {
    		((HackyViewPager) mViewPager).toggleLock();
    	}
    }
    
    private void changePageNum(int num) {
		// TODO Auto-generated method stub
    	String numText = num+1+"/"+pageSize;
    	pageNumView.setText(numText);
	}

    private boolean isViewPagerActive() {
    	return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }
    
    
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
    	}
		super.onSaveInstanceState(outState);
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_pictrue_viewpager;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		changePageNum(arg0);
	}
}
