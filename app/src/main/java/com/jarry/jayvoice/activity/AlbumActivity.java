package com.jarry.jayvoice.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.main.fragment.FindFragment;
import com.jarry.jayvoice.bean.Album;
import com.jarry.jayvoice.bean.Singer;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.core.GetDataBusiness.ResSongListHandler;
import com.jarry.jayvoice.util.DisplayUtil;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.util.Utility;
import com.jarry.jayvoice.widget.WhatFallScrollView;

import a.b.c.DynamicSdkManager;

public class AlbumActivity extends BaseActivity{


	
//	private ImageView albumIv,headIv,albumTabIv;
	private ViewPager mViewPager;
	private ImageView albumIv;
	private ArrayList<View> mViews;//用来存放下方滚动的layoutlayout_1,layout_2
	ListView albumListView;
	ListView albumListView2;
	TextView albumNameView;
	TextView albumLanguageView;
	TextView publishTimeView;
	TextView albumDescView;
	private Album album;
	private TabLayout tabLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		iniVariable();
		getData();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("专辑");
		tabLayout = (TabLayout) findViewById(R.id.tablayout);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		albumIv = (ImageView) findViewById(R.id.top_img);
//		albumListView2 = (ListView) findViewById(R.id.album_listview2);
	}
	
	private void iniVariable() {
		// TODO Auto-generated method stub
		mViews = new ArrayList<>();
    	mViews.add(getLayoutInflater().inflate(R.layout.album_tab_layout1, null));
    	mViews.add(getLayoutInflater().inflate(R.layout.album_tab_layout2, null));
    	mViewPager.setAdapter(new MyPagerAdapter());//设置ViewPager的适配器
    	mViewPager.setCurrentItem(0);
		tabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		album = (Album) getIntent().getSerializableExtra("album");
		if(album!=null){
			showAlbumInfo();
		}
//		getAlbumListData();
	}
	
	private void getAlbumListData() {
		// TODO Auto-generated method stub
		System.out.println("getAlbumListData--");
		getDataBusiness.getSongListFromAlbum(new ResSongListHandler() {
			
			@Override
			public void onResponse(List<Song> result) {
				// TODO Auto-generated method stub
				showToast("getAlbumListData---result.size="+result.size());
				System.out.println("getAlbumListData---result.size="+result.size());
				SongAdapter songAdapter = new SongAdapter(result);
				albumListView.setAdapter(songAdapter);
//				albumListView2.setAdapter(songAdapter);
//				Utility.setListViewHeightBasedOnChildren(albumListView,AlbumActivity.this);
			}
		}, album);
	}
	
	private void getAlbumTabData() {
		// TODO Auto-generated method stub
//		if(album==null){			
//			album = (Album) getIntent().getSerializableExtra("album");
//		}
		//tab2内容
		albumNameView.setText(album.getName());
		albumLanguageView.setText(album.getLanguage());
		publishTimeView.setText(album.getPublishTime());
		if(StringUtils.isNotNull(album.getDesc()))
			albumDescView.setText(Html.fromHtml(album.getDesc()));	
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}
	
	private void showAlbumInfo() {
		// TODO Auto-generated method stub
		if(album==null)return;
		if(StringUtils.isNotNull(album.getName()))
			setTitle(album.getName());
		if(album.getBig_image()!=null){
			String imgUrl = album.getBig_image().getFileUrl(this);
			if(StringUtils.isNotNull(imgUrl)){
				mFetcher.loadImage(imgUrl, albumIv);
			}
		}	
//		Singer singer = album.getSinger();
//		if(singer!=null){
//			if(singer.getUserPic()!=null)
//				mFetcher.loadImage(singer.getUserPic().getFileUrl(this), headIv);
//			singerNameTv.setText(singer.getName());
//		}
//		topPublishTv.setText(album.getPublishTime());
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_album;
	}




	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {
			String title = "";
			switch (position) {
				case 0:
					title = getString(R.string.album_tab1);
					break;
				case 1:
					title = getString(R.string.album_tab2);
					break;
			}
			return title;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
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
					albumListView = (ListView) rootView.findViewById(R.id.album_listview);
					getAlbumListData();
					break;
				case 1:
					albumNameView = (TextView) findViewById(R.id.album_name_textview);
					albumLanguageView = (TextView) findViewById(R.id.album_language_textview);
					publishTimeView = (TextView) findViewById(R.id.album_publishtime_textview);
					albumDescView = (TextView) findViewById(R.id.album_desc_textview);
					getAlbumTabData();
					break;
			}
			return rootView;
		}
	}


		class SongAdapter extends BaseAdapter{

		List<Song> songs = new ArrayList<Song>();
		public SongAdapter(List<Song> songs){
			this.songs = songs;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return songs.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return songs.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.song_list_item, null);
			}
			TextView numView = (TextView) convertView.findViewById(R.id.song_listitem_num);
			TextView nameView = (TextView) convertView.findViewById(R.id.song_listitem_name);
			TextView infoView = (TextView) convertView.findViewById(R.id.song_listitem_info);
			final ImageView rightView = (ImageView) convertView.findViewById(R.id.song_listitem_right);
			final View controlView = convertView.findViewById(R.id.song_listitem_controlview);
			View delView = convertView.findViewById(R.id.song_listitem_delview);
			rightView.setVisibility(View.GONE);
			final Song song = (Song) getItem(position);
			numView.setText(StringUtils.getNum(position+1));
			nameView.setText(song.getName());
			infoView.setText(song.getSinger().getName()+"·"+song.getAlbum().getName());
			convertView.setBackgroundResource(R.drawable.list_selector_bg);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					gotoSelectSong(song);					
				}
			});
			return convertView;
		}
		
	}

	private void gotoSelectSong(Song song) {
		// TODO Auto-generated method stub
		application.setIfPlayChange(true);
		Intent intent = new Intent(this,MainActivity.class);
		intent.putExtra("song", song);
		startActivity(intent);
		this.finish();
	}
	

}
