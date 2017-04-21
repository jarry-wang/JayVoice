package com.jarry.jayvoice.activity;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.About;
import com.jarry.jayvoice.core.GetDataBusiness.ResAboutHandler;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends BaseActivity{

	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getData();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.about));
		textView = (TextView) findViewById(R.id.about_textview);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		getDataBusiness.getAbout(new ResAboutHandler() {
			
			@Override
			public void onResponse(About result) {
				// TODO Auto-generated method stub
				textView.setText(Html.fromHtml(result.content));
			}
		});
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_about;
	}

}
