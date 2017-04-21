package com.jarry.jayvoice.activity;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.Qrcode;
import com.jarry.jayvoice.core.GetDataBusiness.ResQrcodeHandler;
import com.jarry.jayvoice.util.ImageUtil;
import com.jarry.jayvoice.util.QQUtil;
import com.jarry.jayvoice.util.WeixinUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class InvitationActivity extends BaseActivity{
	WeixinUtil weixinUtil;
	QQUtil qqUtil;
	String tel;
	View wb_view,qq_view,qzone_view,wx_view,pyq_view;
	ImageView qrcodeIv;
	String appUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		regToWx();
		regToQQ();
		weixinUtil = new WeixinUtil(this, iwxapi);
		qqUtil = new QQUtil(this,mTencent);
		getData();
	}
	
	 @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.invite_friends));		
		wb_view = findViewById(R.id.xlwb_layout);
		wx_view = findViewById(R.id.weixin_layout);
		pyq_view = findViewById(R.id.pyq_layout);
		qq_view = findViewById(R.id.qq_layout);
		qzone_view = findViewById(R.id.qzone_layout);
		qrcodeIv = (ImageView) findViewById(R.id.invitation_qrcode_iv);
		wb_view.setOnClickListener(this);
		wx_view.setOnClickListener(this);
		pyq_view.setOnClickListener(this);
		qq_view.setOnClickListener(this);
		qzone_view.setOnClickListener(this);
		qrcodeIv.setFocusable(true);
		qrcodeIv.requestFocus();
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		getDataBusiness.getQrcode(new ResQrcodeHandler() {
			
			@Override
			public void onResponse(Qrcode result) {
				// TODO Auto-generated method stub
				appUrl = result.url;
				application.setAppUrl(appUrl);
				showData();
			}
		});
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		try {
			bitmap = ImageUtil.Create2DCode(appUrl, this,150);
			qrcodeIv.setImageBitmap(bitmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.xlwb_layout:
			break;
		case R.id.weixin_layout:
			weixinUtil.sendReq(0,"");
			break;
		case R.id.pyq_layout:
			weixinUtil.sendReq(1,"");
			break;
		case R.id.qq_layout:
			qqUtil.share("");
			break;
		case R.id.qzone_layout:
			qqUtil.shareToQQzone();
			break;
		}
	}
	

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_invitation;
	}


}
