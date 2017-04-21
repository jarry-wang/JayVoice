package com.jarry.jayvoice.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.Album;
import com.jarry.jayvoice.bean.Singer;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.core.GetDataBusiness.ResSongListHandler;
import com.jarry.jayvoice.util.DisplayUtil;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.util.Utility;

public class AlbumActivity extends BaseActivity implements OnCheckedChangeListener{

	
	private RadioButton[] mRadioButtons = new RadioButton[2];
	
	private ImageView albumIv,headIv,albumTabIv;
//	private ViewPager mViewPager;
	private RelativeLayout infoView;
	private ArrayList<View> mViews;//用来存放下方滚动的layoutlayout_1,layout_2
	ListView albumListView;
	TextView albumNameView,albumLanguageView,publishTimeView,albumDescView;
	TextView singerNameTv,topPublishTv;
	private Album album; 
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
		typeTabWidth = DisplayUtil.getWindowWidth(this)/2;
		mRadioGroup = (RadioGroup)findViewById(R.id.album_type_radioGroup);
		mRadioButtons[0] = (RadioButton)findViewById(R.id.album_type_btn1);
		mRadioButtons[1] = (RadioButton)findViewById(R.id.album_type_btn2);	
		headIv = (ImageView) findViewById(R.id.album_headimg);
		singerNameTv = (TextView) findViewById(R.id.album_top_nameview);
		topPublishTv = (TextView) findViewById(R.id.album_top_timeview);
		albumIv = (ImageView) findViewById(R.id.iv);
		mTypeImageView = (ImageView)findViewById(R.id.album_type_bottomimg);	
		mRadioGroup.setOnCheckedChangeListener(this);
		infoView = (RelativeLayout) findViewById(R.id.album_info_view);
		albumListView = (ListView) findViewById(R.id.album_listview);
		albumNameView = (TextView) findViewById(R.id.album_name_textview);
		albumLanguageView = (TextView) findViewById(R.id.album_language_textview);
		publishTimeView = (TextView) findViewById(R.id.album_publishtime_textview);
		albumDescView = (TextView) findViewById(R.id.album_desc_textview);
		albumTabIv = (ImageView) findViewById(R.id.album_imageview);
		albumTabIv.setVisibility(View.GONE);
		doTypeTextSize(mRadioButtons);
	}
	
	private void iniVariable() {
		// TODO Auto-generated method stub
		mViews = new ArrayList<View>();
    	mViews.add(findViewById(R.id.album_info_view1));
    	mViews.add(findViewById(R.id.album_info_view2));
//    	mViewPager.setAdapter(new MyPagerAdapter());//设置ViewPager的适配器
    	mRadioButtons[0].setChecked(true);
//    	mViewPager.setCurrentItem(0);
    	mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
    	switchInfoContent(0);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		album = (Album) getIntent().getSerializableExtra("album");
		if(album!=null){
			showAlbumInfo();
			getAlbumListData();
			getAlbumTabData();
		}		
	}
	
	private void getAlbumListData() {
		// TODO Auto-generated method stub
		getDataBusiness.getSongListFromAlbum(new ResSongListHandler() {
			
			@Override
			public void onResponse(List<Song> result) {
				// TODO Auto-generated method stub
				SongAdapter songAdapter = new SongAdapter(result);
				albumListView.setAdapter(songAdapter);
				Utility.setListViewHeightBasedOnChildren(albumListView,AlbumActivity.this);
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
		Singer singer = album.getSinger();
		if(singer!=null){
			if(singer.getUserPic()!=null)
				mFetcher.loadImage(singer.getUserPic().getFileUrl(this), headIv);
			singerNameTv.setText(singer.getName());			
		}
		topPublishTv.setText(album.getPublishTime());	
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_album;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub
		int position = 0;
		if (checkedId == R.id.album_type_btn1) {
			position = 0;
			doImgAnimation(0);			
		}else if (checkedId == R.id.album_type_btn2) {
			position = 1;
			doImgAnimation(typeTabWidth);			
		}
		switchInfoContent(position);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();//更新当前蓝色横条距离左边的距离
	}

	private void switchInfoContent(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			mViews.get(0).setVisibility(View.VISIBLE);
			mViews.get(1).setVisibility(View.GONE);
			break;
		case 1:
			mViews.get(1).setVisibility(View.VISIBLE);
			mViews.get(0).setVisibility(View.GONE);
			break;
		}
		
	}
	
	private float getCurrentCheckedRadioLeft() {
		// TODO Auto-generated method stub
		switch (mRadioGroup.getCheckedRadioButtonId()) {
		case R.id.album_type_btn1:
			return 0;
		case R.id.album_type_btn2:
			return typeTabWidth;
		}		
		return 0f;
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
