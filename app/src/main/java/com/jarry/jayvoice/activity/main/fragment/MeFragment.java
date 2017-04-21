package com.jarry.jayvoice.activity.main.fragment;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.CollectionActivity;
import com.jarry.jayvoice.activity.InvitationActivity;
import com.jarry.jayvoice.activity.LoginActivity;
import com.jarry.jayvoice.activity.MeInfoActivity;
import com.jarry.jayvoice.activity.SettingActivity;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.core.GetDataBusiness.ResUserHandler;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.util.StringUtils;

import cn.join.android.Logger;
import cn.join.android.ui.widget.MergeAdapter;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MeFragment extends BaseFragment{
	View root;
	ListView mListView;
	MergeAdapter adapter;
	String[] login_itemNames1 = {"收藏歌曲"},login_itemNames2 = {"邀请好友"},login_itemNames3 = {"设置"};
	private boolean ifLogin = false;
	TextView goLoginView;
	ImageView headView;
	TextView unameView;
	User user;
	private ImageView mHeadImg;
	private UserManager userManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		Logger.d("MeFragment--onCreateView");
		root = inflater.inflate(R.layout.frag_me, container, false);
		ifLogin = application.isLogin();
		userManager = UserManager.getInstance(getActivity());
		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Logger.d("MeFragment--onResume");
		super.onResume();
    	if(ifLogin == application.isLogin()){
			if(ifLogin&&application.isIfUserInfoChange()){
				getLoginData();
				application.setIfUserInfoChange(false);
			}			
		}else{//登录状态改变
			ifLogin = application.isLogin();
			initView(root);
		}
	}

	@Override
	public void initView(View v) {
		// TODO Auto-generated method stub
		initMeView(v, ifLogin);
		if(ifLogin){
			getLoginData();
		}	
	}
	
	@Override
	public void getData() {
		
	}
	
	private void initMeView(View v,boolean ifLogin) {
		// TODO Auto-generated method stub
		adapter = new MergeAdapter();	
		mListView = (ListView) v.findViewById(R.id.me_listview);
		View topView = null;
		if(ifLogin){
			topView = LayoutInflater.from(mActivity).inflate(R.layout.me_login_topview, null);
			headView = (ImageView) topView.findViewById(R.id.me_login_top_headimg);
			unameView = (TextView) topView.findViewById(R.id.me_login_top_nameTv);
			adapter.addView(topView);
			topView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					gotoActvity(MeInfoActivity.class);
				}
			});
			headView.setOnClickListener(this);
		}else{
			topView = LayoutInflater.from(mActivity).inflate(R.layout.me_nologin_topview, null);
			goLoginView = (TextView) topView.findViewById(R.id.me_nologin_top_toloin_tv);
			goLoginView.setOnClickListener(this);
			adapter.addView(topView);
		}
		
		adapter.addAdapter(new FuncAdapter(login_itemNames1));
		View line2 = LayoutInflater.from(mActivity).inflate(R.layout.common_three_line_large, null);
		adapter.addView(line2);
		adapter.addAdapter(new FuncAdapter(login_itemNames2));
		View line3 = LayoutInflater.from(mActivity).inflate(R.layout.common_three_line_large, null);
		adapter.addView(line3);
		adapter.addAdapter(new FuncAdapter(login_itemNames3));
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				switch (position) {
				case 1:
					gotoActvity(CollectionActivity.class, true);
					break;
				case 3:
					gotoActvity(InvitationActivity.class);
					break;
				case 5:
					gotoActvity(SettingActivity.class);
					break;
				}
			}
		});
	}
	
	private void getLoginData() {
		user = userManager.getUser();
		if(user==null){
			userManager.refreshUserInfo(getActivity(), new UserManager.RefreshUserinfoCallback() {
				@Override
				public void succuss() {
					user = userManager.getUser();
					showData();
				}
			});
		}else{
			showData();
		}
		
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		unameView.setText(user.nickName);
		if(StringUtils.isNotNull(user.imgUrl)){
			mFetcher.loadImage(user.imgUrl, headView);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.me_nologin_top_toloin_tv:
			if(!ifLogin){
				Intent intent = new Intent(mActivity,LoginActivity.class);
				intent.putExtra("fromAc", mActivity.getClass().getSimpleName());
				startActivity(intent);
			}
			
			break;
		case R.id.me_login_top_headimg:
			gotoActvity(MeInfoActivity.class);
			break;
		}
	}
	
	class FuncAdapter extends BaseAdapter{
		String[] names;
		public FuncAdapter(String[] names){
			this.names = names;
		}
		@Override
		public int getCount() {
			return names.length;
		}
		@Override
		public Object getItem(int arg0) {
			return names[arg0];
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			if(convertView==null){
				convertView = LayoutInflater.from(mActivity).inflate(R.layout.me_listitem_view, null);				
			}
			
			TextView nameView = (TextView) convertView.findViewById(R.id.listitem_left_view);
			nameView.setText((CharSequence) getItem(position));
			convertView.setBackgroundResource(R.drawable.list_selector_bg);

			return convertView;
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


}
