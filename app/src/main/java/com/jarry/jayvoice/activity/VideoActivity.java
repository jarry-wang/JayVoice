package com.jarry.jayvoice.activity;

import java.util.ArrayList;
import java.util.List;


import com.jarry.jayvoice.R;
import com.jarry.jayvoice.base.PullGridViewBaseActivity;
import com.jarry.jayvoice.bean.Vedio;
import com.jarry.jayvoice.core.GetDataBusiness.ResVideoListHandler;
import com.jarry.jayvoice.util.DisplayUtil;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;



public class VideoActivity extends PullGridViewBaseActivity implements TabLayout.OnTabSelectedListener{
	
	MyAdapter adapter;
	List<Vedio> videos = new ArrayList<Vedio>();
	private String category;
	int videoTypeId;
	String typeTitle;
	int typeTab;
	List<Vedio> hotVedios,newVedios;
	private TabLayout tabLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		typeTab = 0;
		getData();
	}
	
	@Override
	public void initView() {
		tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
		mGridView = (GridView) findViewById(R.id.content_view);
		adapter = new MyAdapter(this);
		mGridView.setAdapter(adapter);
		setEmptyName("暂无资源");
		mFetcher.setLoadingImage(R.color.white);
		TabLayout.Tab tab1 = tabLayout.newTab().setText(getString(R.string.video_tab1));
		tabLayout.addTab(tab1);
		TabLayout.Tab tab2 = tabLayout.newTab().setText(getString(R.string.video_tab2));
		tabLayout.addTab(tab2);
		tabLayout.setOnTabSelectedListener(this);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub	
		videoTypeId = getIntent().getIntExtra("typeId", 1);
		typeTitle =  getIntent().getStringExtra("title");
		getVideoType(typeTab);
		setTitle(typeTitle);
	}
	
	private void getVideoType(final int orderType) {
		// TODO Auto-generated method stub
		switch (orderType) {
		case 0://最热
			if(ListUtil.isNotNull(hotVedios)){
				size = hotVedios.size();
				videos = hotVedios;
				showData();
				return;
			}
			break;

		case 1://最新
			if(ListUtil.isNotNull(newVedios)){
				size = newVedios.size();
				videos = newVedios;
				showData();
				return;
			}
			break;
		}
		getDataBusiness.getVideoList(videoTypeId, orderType, new ResVideoListHandler() {
			
			@Override
			public void onResponse(List<Vedio> result) {
				// TODO Auto-generated method stub
				if(result!=null){
					switch (orderType) {
					case 0:
						hotVedios = result;
						break;
					case 1:
						newVedios = result;
						break;
					}
					size = result.size();
					videos = result;
					showData();
				}	
				stopRefresh(refreshLayout);
				stopLoadMore();
			}

			@Override
			public void onError(int errorCode, String msg) {
				// TODO Auto-generated method stub
				if(errorCode == 9009){
					showEmptyView();
				}else{
					showToast(errorCode+":"+msg);
				}	
				stopRefresh(refreshLayout);
				stopLoadMore();
			}
		});
	}
	
	@Override
	public void getData(int offset) {
		// TODO Auto-generated method stub
		super.getData(offset);
		getVideoType(typeTab);
	}
	
	@Override
	public void refeshData() {
		// TODO Auto-generated method stub
		switch (typeTab) {
		case 0:
			hotVedios = null;
			break;
		case 1:
			newVedios = null;
			break;
		}
		getData(offset);
	}	

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		getPage();
		adapter.notifyDataSetChanged();
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_video;
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);		
	}

	@Override
	public void onTabSelected(TabLayout.Tab tab) {
		Logger.d("onTabSelected---position="+tab.getPosition());
		int position = tab.getPosition();
		typeTab = position;
		getVideoType(typeTab);
	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) {

	}

	@Override
	public void onTabReselected(TabLayout.Tab tab) {

	}

	class MyAdapter extends BaseAdapter {
		Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return videos.size();
		}

		@Override
		public Object getItem(int position) {
			return videos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold viewHold = null;
			if(convertView==null){
				viewHold = new ViewHold();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.video_item_view, null);
				viewHold.imageView = (ImageView) convertView.findViewById(R.id.video_item_img);
				viewHold.nameView = (TextView) convertView.findViewById(R.id.video_item_nameTv);
				viewHold.timeView = (TextView) convertView.findViewById(R.id.video_item_timeTv);
				viewHold.playView = (ImageView) convertView.findViewById(R.id.video_playbtn);
				convertView.setTag(viewHold);
			}else{
				viewHold = (ViewHold) convertView.getTag();
			}
			final Vedio itemInfo = (Vedio) getItem(position);		
			int pWidth = mGridView.getWidth()-DisplayUtil.getDpSize((int) getResources().getDimension(R.dimen.edge_padding_size_m), VideoActivity.this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            		LayoutParams.FILL_PARENT, (int) ((pWidth / 2)*1.2));
            viewHold.imageView.setLayoutParams(params);
            if(itemInfo.image!=null&&StringUtils.isNotNull(itemInfo.image.getFileUrl())){
            	mFetcher.loadImage(itemInfo.image.getFileUrl(), viewHold.imageView);
            }
            viewHold.nameView.setText(itemInfo.name);
            if(itemInfo.time!=null)
            	viewHold.timeView.setText(itemInfo.time.getDate().split(" ")[0]);
            OnClickListener mClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					if(videoTypeId==2){
						intent.setClass(VideoActivity.this,VideoDetailActivity.class);
					}else{
						intent.setClass(VideoActivity.this,VideoPlayActivity.class);
					}					
					intent.putExtra("video", itemInfo);
					intent.putExtra("typeId", videoTypeId);
					startActivity(intent);
				}
			};
			viewHold.playView.setOnClickListener(mClickListener);
            convertView.setOnClickListener(mClickListener);
			return convertView;
		}

	}

	
	class ViewHold{
		ImageView imageView;
		TextView nameView;
		TextView timeView;
		ImageView playView;
	}



	
	
}
