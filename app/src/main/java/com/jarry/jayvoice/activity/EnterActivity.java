package com.jarry.jayvoice.activity;


import cn.join.android.Logger;

import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.core.UserManager;

import android.os.Bundle;

public class EnterActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Logger.d("EnterActivity--in");

		if(!prefUtil.getInstallInfo()){
			prefUtil.saveInstallInfo(true);
		}	
		if(checkLogin()){
			gotoActivity(MainActivity.class);
			this.finish();
		}

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean checkLogin() {
		// TODO Auto-generated method stub
		UserManager userManager = UserManager.getInstance(getApplicationContext());
    	if(userManager.getPrefLogin()){
        	userManager.getLoginInfo();   		    		
        }else{
    		application.setLogin(false);   
    		application.setUser(null);
        }
    	return true;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_enter;
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
