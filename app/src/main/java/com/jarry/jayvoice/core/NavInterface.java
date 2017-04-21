package com.jarry.jayvoice.core;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;


public interface NavInterface {
	
	public Fragment getFragment();
	
	public int getUID();
	
	public void initView(View v);
	
	public void getData();
	
	public void showData();
}
