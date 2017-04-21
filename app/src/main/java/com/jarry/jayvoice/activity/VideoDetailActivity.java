package com.jarry.jayvoice.activity;

import a.b.c.DynamicSdkManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.Vedio;
import com.jarry.jayvoice.util.StringUtils;

public class VideoDetailActivity extends BaseActivity{

	private View infoView;
	private ImageView infoImg;
	private TextView infoNameTv,infoLeadTv,infoTimeTv,infofromTv,infoDescTv,infoDirectorTv,infoLeadTv2,infoStyleTv,infoPlayTv;
	private Vedio mVedio; 
	int videoTypeId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getData();
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("视频详情");
		infoView = findViewById(R.id.video_info_layout);
		infoImg = (ImageView) findViewById(R.id.video_info_image);
		infoNameTv = (TextView) findViewById(R.id.video_info_name);
		infoLeadTv = (TextView) findViewById(R.id.video_info_lead);
		infoTimeTv = (TextView) findViewById(R.id.video_info_time);
		infofromTv = (TextView) findViewById(R.id.video_info_from);
		infoDescTv = (TextView) findViewById(R.id.video_info_desc);
		infoDirectorTv = (TextView) findViewById(R.id.video_info_director);
		infoLeadTv2 = (TextView) findViewById(R.id.video_info_lead2);
		infoStyleTv = (TextView) findViewById(R.id.video_info_style);
		infoPlayTv = (TextView) findViewById(R.id.video_info_start);
		infoPlayTv.setOnClickListener(this);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		mVedio = (Vedio) getIntent().getSerializableExtra("video");
		videoTypeId = getIntent().getIntExtra("typeId", 1);
		if(mVedio!=null)
			showData();
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		infoView.setVisibility(View.VISIBLE);
		if(mVedio.image!=null)
			mFetcher.loadImage(mVedio.image.getFileUrl(this), infoImg);
		infoNameTv.setText(mVedio.name);
		infoLeadTv.setText("主演："+mVedio.lead);
		if(mVedio.time!=null)
			infoTimeTv.setText("年代："+mVedio.time.getDate().split("-")[0]);
		infoDirectorTv.setText("导演："+mVedio.director);
		infoLeadTv2.setText("主演："+mVedio.lead);
		infoStyleTv.setText("类型："+mVedio.style);
		if(StringUtils.isNotNull(mVedio.desc))
			infoDescTv.setText(Html.fromHtml(mVedio.desc));
		View banner = DynamicSdkManager.getInstance(this).getBanner(this);
	    LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);		
	    if(banner!=null)
	    	adLayout.addView(banner);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.video_info_start:
			Intent intent = new Intent(this,VideoPlayActivity.class);
			intent.putExtra("video", mVedio);
			intent.putExtra("typeId", videoTypeId);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_video_detail;
	}

}
