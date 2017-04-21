package com.jarry.jayvoice.activity.main.fragment;

import java.util.ArrayList;
import java.util.List;


import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.AlbumActivity;
import com.jarry.jayvoice.activity.main.interf.MainInterf;
import com.jarry.jayvoice.bean.Album;
import com.jarry.jayvoice.bean.Photo;
import com.jarry.jayvoice.core.GetDataBusiness.ResAlbumListHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResPhotoListHandler;
import com.jarry.jayvoice.util.DisplayUtil;
import com.jarry.jayvoice.util.ImageUtil;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.widget.WhatFallScrollView;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import a.b.c.DynamicSdkManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint({ "CutPasteId", "SetJavaScriptEnabled" })
public class FindFragment extends BaseFragment implements MainInterf.FindChild{
	private View rootView;
	LayoutInflater lin;
	private GridView gridView;
	private View changeAlTypeView;
	private TextView changeAlTypeTv;
	int alType = 0;//0:人物版；1：卡通版
	List<Album> albumList = new ArrayList<Album>();
	AlbumAdapter albumAdapter;

	private ViewPager mViewPager;	//下方的可横向拖动的控件
	private ArrayList<View> mViews;//用来存放下方滚动的layout(layout_1,layout_2,layout_3)
	WebView newsWebView;
	String newsHomeUrl;
	WhatFallScrollView whatFallScrollView;
	private MainInterf.MainView mainView;
	private boolean canRefresh = true;
	private TabLayout mTabLayout;

	public FindFragment(MainInterf.MainView mainView) {
		this.mainView = mainView;
		this.mainView.setFindChild(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		Logger.d("AlbumFragment--onCreateView");
		lin = LayoutInflater.from(getActivity());
		rootView = inflater.inflate(R.layout.frag_find, container,false);					
        return rootView;
	}
	
	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		mTabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
		mViewPager = (ViewPager)view.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(3);
//		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
		iniVariable();
		mTabLayout.setupWithViewPager(mViewPager);
	}


	@Override
	public void getData() {
		// TODO Auto-generated method stub		
	}
	
	private void getAlbumData() {
		// TODO Auto-generated method stub
		mGetDataBusiness.getAlbumList(new ResAlbumListHandler() {
			
			@Override
			public void onResponse(List<Album> result) {
				// TODO Auto-generated method stub				
				if (ListUtil.isNotNull(result)) {
					albumList.clear();
					albumList.addAll(result);
					albumAdapter.notifyDataSetChanged();
					mainView.stopRefresh();
				}
			}
		});
	}
	
	private void getPicData() {
		// TODO Auto-generated method stub
		mGetDataBusiness.getPhotoList(new ResPhotoListHandler() {
			
			@Override
			public void onResponse(List<Photo> result) {
				// TODO Auto-generated method stub
				whatFallScrollView.setPhotos(result);
				whatFallScrollView.postInvalidate();
			}
		});
	}
	
	
	private void iniVariable() {
		// TODO Auto-generated method stub
    	mViews = new ArrayList<>();
    	mViews.add(getActivity().getLayoutInflater().inflate(R.layout.find_album_layout, null));
    	mViews.add(getActivity().getLayoutInflater().inflate(R.layout.find_pic_layout, null));
    	mViews.add(getActivity().getLayoutInflater().inflate(R.layout.find_news_layout, null));
    	mViewPager.setAdapter(new MyPagerAdapter());//设置ViewPager的适配器
    	mViewPager.setCurrentItem(0);
	}

	private void showNewsData() {
		// TODO Auto-generated method stub
		if(application.getSinger()!=null){
			newsHomeUrl = application.getSinger().getNewsUrl();
			newsWebView.loadUrl(newsHomeUrl);
		}
		
	}
	@Override
	public void onResume() {
		Logger.d("AlbumFragment--onResume");
		super.onResume();
		mainView.setEnableRefresh(canRefresh);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.change_album_type_view:
			doChangeAnim();
			if(alType==0){
				albumAdapter.notifyDataSetChanged();
				alType = 1;
				changeAlTypeTv.setText(getString(R.string.change_to_people));
			}else{
				albumAdapter.notifyDataSetChanged();
				alType = 0;
				changeAlTypeTv.setText(getString(R.string.change_to_cartoon));
			}
			break;

		default:
			break;
		}
	}
	
	private void doChangeAnim() {
		// TODO Auto-generated method stub
		ObjectAnimator.
	   	 ofFloat(changeAlTypeView, "rotationX", 0.0F, 360.0F).//  
	   	 setDuration(500).//  
	   	 start();
	}

	@Override
	public void doRefresh() {
		mGetDataBusiness.setIfShow(false);
		getAlbumData();
	}

	class AlbumAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return albumList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return albumList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(view == null){
				view = lin.inflate(R.layout.album_item_layout,null);
			}
			final Album album = (Album) getItem(position);
			final ImageView albumImg = (ImageView) view.findViewById(R.id.album_item_img);
			TextView albumName = (TextView) view.findViewById(R.id.album_item_name);
			TextView albumNum = (TextView) view.findViewById(R.id.album_item_num);
			albumName.setText(album.getName());
			albumNum.setText(album.getNum()+"首");
			if(album.getImage()!=null&&album.getCartoonImg()!=null){
				final String imgUrl = alType==0?album.getImage().getFileUrl(getActivity()):album.getCartoonImg().getFileUrl(getActivity());
				if(StringUtils.isNotNull(imgUrl))
					ImageUtil.setImg(mActivity,albumImg,imgUrl,0);
//					mFetcher.loadImage(imgUrl, albumImg);
				doAnim(albumImg);
			}	
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					ImageUtil.gotoImage(mActivity, imgUrl, albumImg);
					Intent intent = new Intent(mActivity,AlbumActivity.class);
					intent.putExtra("album", album);
					startActivity(intent);
				}
			});
			return view;
		}	
		
		private void doAnim(ImageView albumImg) {
			// TODO Auto-generated method stub
			ObjectAnimator translationY = ObjectAnimator.ofFloat(albumImg,"scaleX",0.92f,1f).setDuration(500);
			ObjectAnimator translationX = ObjectAnimator.ofFloat(albumImg,"scaleY",0.92f,1f).setDuration(500);
			AnimatorSet set = new AnimatorSet();
			set.setDuration(500);
			set.setTarget(albumImg);
			set.playTogether(translationY,translationX);
			set.start();
		}
	}

	
	private class MyPagerAdapter extends PagerAdapter{

		@Override
		public CharSequence getPageTitle(int position) {
			String title = "";
			switch (position) {
				case 0:
					title = getString(R.string.find_tab1);
					break;
				case 1:
					title = getString(R.string.find_tab3);
					break;
				case 2:
					title = getString(R.string.find_tab4);
					break;
			}
			return title;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViews.get(position));
		}


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViews.get(position));
			View rootView = mViews.get(position);
			switch (position) {
				case 0:
					gridView = (GridView) rootView.findViewById(R.id.gridview_album);
					changeAlTypeView = rootView.findViewById(R.id.change_album_type_view);
					changeAlTypeTv = (TextView) rootView.findViewById(R.id.change_album_type_tv);
					albumAdapter = new AlbumAdapter();
					gridView.setAdapter(albumAdapter);
					gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							if (gridView != null && gridView.getChildCount() > 0) {
								// check if the first item of the list is visible
								boolean firstItemVisible = gridView.getFirstVisiblePosition() == 0;
								// check if the top of the first item is visible
								boolean topOfFirstItemVisible = gridView.getChildAt(0).getTop() == 0;
								// enabling or disabling the refresh layout
								canRefresh = firstItemVisible && topOfFirstItemVisible;
							}
							mainView.setEnableRefresh(canRefresh);
						}

						@Override
						public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

						}
					});
					changeAlTypeView.setOnClickListener(FindFragment.this);
					mainView.refreshData();
					break;
				case 1:
					View banner = DynamicSdkManager.getInstance(mActivity).getBanner(mActivity);
					LinearLayout adLayout=(LinearLayout)rootView.findViewById(R.id.adLayout);
					adLayout.removeAllViews();
					if(banner!=null)
						adLayout.addView(banner);
					whatFallScrollView = (WhatFallScrollView) rootView.findViewById(R.id.my_scroll_view);
					getPicData();
					break;
				case 2:
					View banner2 = DynamicSdkManager.getInstance(mActivity).getBanner(mActivity);
					LinearLayout adLayout2=(LinearLayout)rootView.findViewById(R.id.adLayout2);
					adLayout2.removeAllViews();
					if(banner2!=null)
						adLayout2.addView(banner2);
					newsWebView = (WebView) rootView.findViewById(R.id.view_webview);
					newsWebView.getSettings().setJavaScriptEnabled(true);
					newsWebView.setWebViewClient(new WebViewClient(){
						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							System.out.println(url);
							if(url.startsWith("http://www.jay520.com.cn")||url.startsWith("http://m.jay520.com")){
								return true;
							}
							view.loadUrl(url);   //在当前的webview中跳转到新的url
							return true;
						}
					});
					showNewsData();
					break;
			}
			return rootView;
		}


		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}


	@Override
	public Fragment getFragment() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getUID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}
	
	public void onBackPressd(){
		switch (mViewPager.getCurrentItem()) {
		case 0:
		case 1:
			mActivity.doBack();
			break;
		case 2:
			if(newsWebView.getUrl().equals(newsHomeUrl)){
				mActivity.doBack();
			}else{
				newsWebView.goBack();
			}		
			break;
		}
	}

}
