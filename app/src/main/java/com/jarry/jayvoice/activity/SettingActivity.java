package com.jarry.jayvoice.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.join.android.ui.widget.MergeAdapter;
import cn.join.android.util.AppUtil;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.core.Config;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.interfac.IfdoInterface;

public class SettingActivity extends BaseActivity{
	
	private final int LOGIN_OUT = 0x01;
	private LinearLayout rightLayout;
	MergeAdapter adapter;
	ListView mListView;
	TextView disloginView;
	boolean ifReveive;
	private UserManager userManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		regToQQ();
		getData();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		userManager = UserManager.getInstance(getApplicationContext());
		setTitle(getString(R.string.setting));
		mListView = (ListView) findViewById(R.id.setting_mainlist);
		adapter = new MergeAdapter();
		View line1 = LayoutInflater.from(this).inflate(R.layout.common_three_line, null);
		adapter.addView(line1);
		String[] str1 = {getString(R.string.feedback)};
		adapter.addAdapter(new FuncAdapter(str1,null));
		View line2 = LayoutInflater.from(this).inflate(R.layout.common_three_line, null);
		adapter.addView(line2);
		String[] str2 = {getString(R.string.detect_updates),getString(R.string.about_us)};
		String[] values2 = {"当前版本:"+ AppUtil.getVersionName(this),""};
		adapter.addAdapter(new FuncAdapter(str2,values2));	
		if(userManager.isLogin()){
			View bottomView = LayoutInflater.from(this).inflate(R.layout.me_info_bottom, null);
			disloginView = (TextView) bottomView.findViewById(R.id.dislogin_commit_tv);
			disloginView.setOnClickListener(this);
			adapter.addView(bottomView);
		}	
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				switch (position) {
				case 1:
					gotoActivity(FeedBackActivity.class);
					break;
				case 3:
					doUpdata();
					break;
				case 4:
					gotoActivity(AboutActivity.class);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}
	
	private void doUpdata() {
		// TODO Auto-generated method stub
		/** 开始调用自动更新函数 **/
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.dislogin_commit_tv:
			showEditDialog();
			break;
		}
	}

	protected void showEditDialog() {
		showIfdoDialogCb("确定退出登录", new IfdoInterface() {
			
			@Override
			public void doConfirm() {
				// TODO Auto-generated method stub
				doLoginOut();
				ifDoDialog.dismiss();							
			}
			
			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				ifDoDialog.dismiss();
			}
		});
	}
	
	private void doLoginOut() {
		// TODO Auto-generated method stub
		String loginType = userManager.getLoginType();
		if(loginType.equals(Config.LOGINTYPE_WEIXIN)){
			
		}else{
			BmobUser.logOut(this);   //清除缓存用户对象
			if(mTencent!=null)
				mTencent.logout(getApplicationContext());
			userManager.loginOut();
			SettingActivity.this.finish();
		}
	}
	
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_setting;
	}
	
	class FuncAdapter extends BaseAdapter{
		String[] names;
		String[] values;
		public FuncAdapter(String[] names,String[] values){
			this.names = names;
			this.values = values;
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
				convertView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.me_listitem_view, null);				
			}
			
			TextView nameView = (TextView) convertView.findViewById(R.id.listitem_left_view);
			TextView valueView = (TextView) convertView.findViewById(R.id.listitem_right_view);
			nameView.setText((CharSequence) getItem(position));
			if(values!=null){
				valueView.setText(values[position]);
			}
			
			convertView.setBackgroundResource(R.drawable.list_selector_bg);

			return convertView;
		}

	}

//	FeedbackAgent fb;
//    private void setUpUmengFeedback() {
//        fb = new FeedbackAgent(this);
//        // check if the app developer has replied to the feedback or not.
//        fb.sync();
//        fb.setWelcomeInfo("您好，我是产品经理小浪，欢迎您反馈使用产品的感受和建议");
//        fb.openAudioFeedback();
//        fb.closeFeedbackPush();
////      fb.openFeedbackPush();
////      PushAgent.getInstance(this).enable();
//
//        //fb.setWelcomeInfo();
//        //  fb.setWelcomeInfo("Welcome to use umeng feedback app");
////        FeedbackPush.getInstance(this).init(true);
////        PushAgent.getInstance(this).setPushIntentServiceClass(MyPushIntentService.class);
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean result = fb.updateUserInfo();
//            }
//        }).start();
//    }

}
