package com.jarry.jayvoice.activity;



import cn.join.android.net.imgcache.ImageFetcher;
import cn.join.android.net.imgcache.SharedImageFetcher;
import cn.jpush.android.api.JPushInterface;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.core.Config;
import com.jarry.jayvoice.core.DataBusiness;
import com.jarry.jayvoice.core.GetDataBusiness;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.interfac.IfdoInterface;
import com.jarry.jayvoice.interfac.activityInterface;
import com.jarry.jayvoice.util.SharedPrefUtil;
import com.jarry.jayvoice.widget.HandyTextView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener,activityInterface{

	public Toolbar toolbar;
	public TextView txtTopBack;
	public TextView txtTitle;
	public TextView txtRight;
	public View titleLayout;
	public boolean ifShowInanim = true;
	public MyApplication application;
	public GetDataBusiness getDataBusiness;
	public DataBusiness dataBusiness;
	public SharedPrefUtil prefUtil;
	public ImageFetcher mFetcher;
	public Tencent mTencent;
	public IWXAPI iwxapi;
	public Dialog ifDoDialog;
	public TextView ifdoConfirm,ifdoCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (ifShowInanim) {
			overridePendingTransition(R.anim.push_left_in,  R.anim.push_left_out);
		}
		if(getLayoutId()>0){
			setContentView(getLayoutId());
		}	
		initData();
		initView();
	}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		titleLayout = findViewById(R.id.viewTitleLayout);
		txtTopBack =(TextView) findViewById(R.id.menu_last_view);
		txtTitle = (TextView) findViewById(R.id.menu_title);
		txtRight = (TextView) findViewById(R.id.menu_right_btn);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null){
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			txtTitle = (TextView) findViewById(R.id.toolbar_title_tv);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private void initData(){
		application = (MyApplication) this.getApplicationContext();
		prefUtil = SharedPrefUtil.getInstance(getApplicationContext());
		mFetcher = SharedImageFetcher.getSharedFetcher(this);
    	mFetcher.setLoadingImage(null);
		getDataBusiness = new GetDataBusiness(this);
		dataBusiness = new DataBusiness(this);
	}
	
	public void setBackText(String text){
		txtTopBack.setText(text);
	}
	
	public void setTitle(String text){
		if (txtTitle != null)
			txtTitle.setText(text);
	}
	
	
	public int getDpSize(int size){
		
		return (int) (size*getResources().getDisplayMetrics().density);
	}
	
	public void gotoActivity(Class goA){
		Intent intent = new Intent();
		intent.setClass(this, goA);
		startActivity(intent);		
	}
	
	public void onBack(View v){
	    onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();	 
	    overridePendingTransition(R.anim.push_main_in,R.anim.push_right_out);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	public synchronized void showToast(String text) {
		// TODO Auto-generated method stub
		View toastRoot = LayoutInflater.from(this).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(this);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
	
	public void regToQQ() {
		// TODO Auto-generated method stub
    	// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    	// 其中APP_ID是分配给第三方应用的appid，类型为String。
    	mTencent = Tencent.createInstance(Config.qq_APP_ID, this.getApplicationContext());
    	 // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
	}
	
    public void regToWx() {
		// TODO Auto-generated method stub
		iwxapi = WXAPIFactory.createWXAPI(this, Config.wx_APP_ID, true);
		iwxapi.registerApp(Config.wx_APP_ID);
	}

	
	public void gotoImage(String url,ImageView imageView) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,CommonImageActivity.class);
		intent.putExtra("url", url);
		int[] location = new int[2];
		imageView.getLocationOnScreen(location);
		intent.putExtra("locationX", location[0]);
		intent.putExtra("locationY", location[1]);

		intent.putExtra("width", imageView.getWidth());
		intent.putExtra("height", imageView.getHeight());
		startActivity(intent);
		overridePendingTransition(0, 0);
	}
	
	public void showIfdoDialogCb(String title,final IfdoInterface ifdoInterface) {
		// TODO Auto-generated method stub
		ifDoDialog = new Dialog(this, R.style.MyDialog);
		ifDoDialog.setContentView(R.layout.if_do_dialog);
		TextView doInfoView = (TextView) ifDoDialog.findViewById(R.id.textView_do_info);
		doInfoView.setText(title);
		ifdoConfirm = (TextView) ifDoDialog.findViewById(R.id.input_ifdo_confirm);
		ifdoCancel = (TextView) ifDoDialog.findViewById(R.id.input_ifdo_cancel);
		ifdoConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ifdoInterface.doConfirm();
			}
		});
		ifdoCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ifdoInterface.doCancel();
			}
		});
		ifDoDialog.show();
	}
    
    public void dismissIfdoDialog() {
		// TODO Auto-generated method stub
    	if(ifDoDialog.isShowing()){
    		ifDoDialog.dismiss();
    	}
	}
    
    public void setEmptyName(String text) {
    	TextView emptyNameView = (TextView) findViewById(R.id.empty_nameTv);
    	emptyNameView.setText(text);  
	}
    
    public void showEmptyView() {
		// TODO Auto-generated method stub
    	View emptyView = findViewById(R.id.empty_view);
    	emptyView.setVisibility(View.VISIBLE); 
	}
    
    public void disEmptyView() {
		// TODO Auto-generated method stub
    	View emptyView = findViewById(R.id.empty_view);
    	emptyView.setVisibility(View.GONE);
	}

}
