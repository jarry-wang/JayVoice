package com.jarry.jayvoice.activity.main.fragment;

import java.util.List;

import cn.join.android.Logger;
import cn.join.android.ui.widget.JazzyViewPager;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.VideoActivity;
import com.jarry.jayvoice.activity.main.interf.MainInterf;
import com.jarry.jayvoice.base.AdFragment;
import com.jarry.jayvoice.bean.Recommend;
import com.jarry.jayvoice.core.GetDataBusiness.ResRecommendListHandler;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.widget.CirclePageIndicator;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SeeFragment extends AdFragment implements MainInterf.SeeChild{
	View root;
	View part1View,part2View,part3View,part4View,part5View;
	TextView part1NameTv,part2NameTv,part3NameTv,part4NameTv,part5NameTv;
	List<Recommend> activities;
	private MainInterf.MainView mainView;

	public SeeFragment(){
		Logger.d("SeeFragment--SeeFragment()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		Logger.d("SeeFragment--onCreateView");
		if (mainView == null) {
			mainView = mActivity;
			mainView.setSeeChild(this);
		}
		root = inflater.inflate(R.layout.frag_see, container, false);				
		return root;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Logger.d("SeeFragment--onResume");
		mainView.setEnableRefresh(true);
		super.onResume();
		if(ListUtil.isNotNull(activities)){
			initAd();
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Logger.d("SeeFragment--onPause");
		super.onPause();
		clearAdHandler();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.d("SeeFragment--onDetach");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.d("SeeFragment--onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.d("SeeFragment--onDestroy");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		Logger.d("SeeFragment--onHiddenChanged-hidden="+hidden);
		if(hidden){
			clearAdHandler();
		}else{
			if(ListUtil.isNotNull(activities)){
				initAd();
			}
		}
	}
	
	@Override
	public void initView(View v) {
		// TODO Auto-generated method stub
		topPager = (JazzyViewPager) v.findViewById(R.id.pager);
		indicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		progressBar = (ProgressBar) v.findViewById(R.id.home_pager_progress);
		part1View = v.findViewById(R.id.see_part1_view);
		part2View = v.findViewById(R.id.see_part2_view);
		part3View = v.findViewById(R.id.see_part3_view);
		part4View = v.findViewById(R.id.see_part4_view);
		part5View = v.findViewById(R.id.see_part5_view);
		part1NameTv = (TextView) v.findViewById(R.id.see_part1_name_tv);
		part2NameTv = (TextView) v.findViewById(R.id.see_part2_name_tv);
		part3NameTv = (TextView) v.findViewById(R.id.see_part3_name_tv);
		part4NameTv = (TextView) v.findViewById(R.id.see_part4_name_tv);
		part5NameTv = (TextView) v.findViewById(R.id.see_part5_name_tv);
		part1View.setOnClickListener(this);
		part2View.setOnClickListener(this);
		part3View.setOnClickListener(this);
		part4View.setOnClickListener(this);
		part5View.setOnClickListener(this);
		initViewPager();
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		mainView.refreshData();
		doRefresh();
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		topPager.setAdapter(new PAdapter(activities));
		indicator.setViewPager(topPager);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		Intent intent = new Intent(mActivity,VideoActivity.class); 
		int typeId = 1;
		String title = "";
		switch (view.getId()) {
		case R.id.see_part1_view:
			typeId = 1;
			title = part1NameTv.getText().toString();
			break;
		case R.id.see_part2_view:
			typeId = 2;
			title = part2NameTv.getText().toString();
			break;
		case R.id.see_part3_view:
			typeId = 3;
			title = part3NameTv.getText().toString();
			break;
		case R.id.see_part4_view:
			typeId = 4;
			title = part4NameTv.getText().toString();
			break;
		case R.id.see_part5_view:
			typeId = 5;
			title = part5NameTv.getText().toString();
			break;
		}
		intent.putExtra("typeId", typeId);
		intent.putExtra("title", title);
		startActivity(intent);
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
	public void doRefresh() {
		mGetDataBusiness.setIfShow(false);
		mGetDataBusiness.getRecommendList(new ResRecommendListHandler() {

			@Override
			public void onResponse(List<Recommend> result) {
				// TODO Auto-generated method stub
				activities = result;
				if(ListUtil.isNotNull(activities)){
					showData();
					mainView.stopRefresh();
				}
			}
		});
	}
}
