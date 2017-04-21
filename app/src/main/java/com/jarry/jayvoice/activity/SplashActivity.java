package com.jarry.jayvoice.activity;

import com.jarry.jayvoice.activity.main.MainActivity;

import a.b.c.DynamicSdkManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 展示开屏广告
		DynamicSdkManager.getInstance(this).showSplash(this, MainActivity.class);

	}


	/**
	 * 请务必加上词句，否则进入网页广告后无法进去原sdk
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 10045) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
