package com.jarry.jayvoice.activity.main.fragment;


import java.lang.ref.SoftReference;

import cn.join.android.net.imgcache.ImageFetcher;
import cn.join.android.net.imgcache.SharedImageFetcher;
import cn.join.android.ui.widget.HandyTextView;
import cn.join.android.util.ToastUtil;

import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.LoginActivity;
import com.jarry.jayvoice.core.Config;
import com.jarry.jayvoice.core.DataBusiness;
import com.jarry.jayvoice.core.GetDataBusiness;
import com.jarry.jayvoice.core.NavInterface;
import com.jarry.jayvoice.util.SharedPrefUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements OnClickListener,NavInterface{

	private SoftReference<View> soft_view;
	public boolean create_every_time=false;
	public View anim_view;
	public ImageFetcher mFetcher;
	public DataBusiness mDataBusiness;
	public GetDataBusiness mGetDataBusiness;
	SharedPrefUtil prefUtil;
	public MainActivity mActivity;
	MyApplication application;
	public Tencent mTencent;
	public IWXAPI iwxapi;
	public ToastUtil toastUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = (MainActivity) this.getActivity();
		mFetcher = SharedImageFetcher.getSharedFetcher(mActivity);
		application = (MyApplication) getActivity().getApplicationContext();
		prefUtil = SharedPrefUtil.getInstance(getActivity().getApplicationContext());
		mDataBusiness = new DataBusiness(getActivity());
		mGetDataBusiness = new GetDataBusiness(getActivity());
		toastUtil = new ToastUtil(mActivity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=null;
		if(!create_every_time&&soft_view!=null&&soft_view.get()!=null&&soft_view.get().getParent()==null){
			v= soft_view.get();
		}else{
			v = onCreateView(inflater,container);
			if (v != null) {
				anim_view = v.findViewById(R.id.anim_view);
				v.setFocusable(false);
			}
			soft_view=new SoftReference<View>(v);
		}	
		initView(v);
		getData();
		return v;
	}
	public abstract View onCreateView(LayoutInflater inflater, ViewGroup container);
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void showToast(String text) {
		// TODO Auto-generated method stub
		View toastRoot = LayoutInflater.from(getActivity()).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(getActivity());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen"); 
	}

	public void gotoActvity(Class activity){
		Intent intent = new Intent();
		intent.setClass(getActivity(), activity);
		startActivity(intent);
	}
	
	public void gotoActvity(Class activity,boolean ifNeedLogin){
		Intent intent = new Intent(mActivity,activity);
		if (ifNeedLogin) {
			if(application.isLogin()){
				startActivity(intent);
			}else{
				gotoActvity(LoginActivity.class);
			}
		}else {
			startActivity(intent);
		}
	}
	
	public void regToQQ() {
		// TODO Auto-generated method stub
    	mTencent = Tencent.createInstance(Config.qq_APP_ID, mActivity.getApplicationContext());
	}
	public void regToWx() {
		// TODO Auto-generated method stub
		iwxapi = WXAPIFactory.createWXAPI(mActivity, Config.wx_APP_ID, true);
		iwxapi.registerApp(Config.wx_APP_ID);
	}
}
