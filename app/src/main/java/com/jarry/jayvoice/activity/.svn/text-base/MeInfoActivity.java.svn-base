package com.jarry.jayvoice.activity;

import java.util.ArrayList;
import java.util.List;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.PhotoSelUtil;
import com.jarry.jayvoice.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MeInfoActivity extends BaseActivity {

	public static final int SET_USER_IMG = 0X0;// 头像上传
	private final int CHANGE_INFO_RESULT = 0X03;
	TextView rightBtnView,unloginView;
	ListView mListView;

	PhotoSelUtil photoSelUtil;
	ImageView imgHeader;
	public static final int UCENTER = 0X1;// 用户中心
	List2Adapter list2Adapter;
	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		photoSelUtil = new PhotoSelUtil();
		getData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Logger.d("MeInfoActivity--onResume");		
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.personal_data));
		mListView = (ListView) findViewById(R.id.me_info_mainlist);
		imgHeader = (ImageView) findViewById(R.id.me_headicon);
		findViewById(R.id.viewHeader).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					photoSelUtil.setGetType(0);
					photoSelUtil.showPhoto(MeInfoActivity.this,"设置头像");
			}
		});
		imgHeader.setOnClickListener(this);
		getDataSource();
		list2Adapter = new List2Adapter(this);
		mListView.setAdapter(list2Adapter);
	}

	@Override
	public void getData() {
		user = application.getUser();	
		if(user!=null)
			showData();
	}

//	@Override
//	public void handleMsg(Message msg) {
//		// TODO Auto-generated method stub
//		super.handleMsg(msg);
//		switch (msg.what) {
//		case UCENTER:
//			
//			break;
//		case SET_USER_IMG:
//			SetImgResult resp = (SetImgResult) msg.obj;
//			if (resp.isSuccess()) {
//				application.setIfuserChange(true);
//				getData();				
//			}else{
//				showToast(resp.getMsg());
//			}
//			break;		
//		}
//	}
	
	@Override
	public void showData() {
		if(StringUtils.isNotNull(user.imgUrl)){
			mFetcher.loadImage(user.imgUrl, imgHeader);
		}
		contentList.clear();
		contentList.add(user.nickName);
		if(user.gender==0){
			contentList.add("男");
		}else{
			contentList.add("女");
		}		
		 
		list2Adapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
								
				}
				
		});
		
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.me_headicon:	
			if(user!=null){
				gotoImage(user.imgUrl, imgHeader);	
			}		
			break;

		}
	};

	List<String> nameList,contentList;
	private void getDataSource() {
		nameList = new ArrayList<String>();
		contentList = new ArrayList<String>();		
		nameList.add("昵称");
		nameList.add("性别");
		contentList.add("");
		contentList.add("");
	}

	class List2Adapter extends BaseAdapter {
		LayoutInflater lin;

		public List2Adapter(Context context) {
			lin = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return nameList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return nameList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (convertview == null) {
				convertview = lin.inflate(R.layout.me_listitem_view_me, null);
			}
			TextView titleView = (TextView) convertview
					.findViewById(R.id.listitem_left_view);
			TextView contentView = (TextView) convertview
					.findViewById(R.id.listitem_right_view);
			ImageView goView = (ImageView) convertview
					.findViewById(R.id.listitem_right_img);
			titleView.setText(nameList.get(position));
			contentView.setText(contentList.get(position));	
			return convertview;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		photoSelUtil.onActivityResult(requestCode, resultCode, data);
		if(requestCode==photoSelUtil.RESIZE_REQUEST_CODE){
			String base64String = photoSelUtil.getImgString();
			if (!TextUtils.isEmpty(base64String)) {
//				SetImgReq req = new SetImgReq();
//				req.img = base64String;
//				mDataBusiness.setUserImg(handler, SET_USER_IMG, req);
			}
		}else if(requestCode == CHANGE_INFO_RESULT&&resultCode==RESULT_OK){
			getData();
		}
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_meinfo;
	}

}
