package com.jarry.jayvoice.widget;


import com.jarry.jayvoice.R;
import com.jarry.jayvoice.util.QQUtil;
import com.jarry.jayvoice.util.WeixinUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.LinearLayout.LayoutParams;

public class RecommendComponent implements OnClickListener{

	protected PopupWindow recommendWindow;
	private int mWidthPixels;
	Context context;
	private IWXAPI iwxapi;
	private Tencent mTencent;
	WeixinUtil weixinUtil;
	QQUtil qqUtil;
	View qq_view,wx_view,pyq_view;
	String tjCode;
	String title,content,url,imageUrl;
	
	public RecommendComponent(Context context,IWXAPI iwxapi,Tencent mTencent) {
		this.context = context;
		this.iwxapi = iwxapi;
		this.mTencent = mTencent;
		weixinUtil = new WeixinUtil(context, iwxapi);
		qqUtil = new QQUtil(context,mTencent);
		View view = initRecommendComponent();
		recommendWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT );
		recommendWindow.setFocusable(true);
		recommendWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.default_gray));
		recommendWindow.setIgnoreCheekPress();
		recommendWindow.setAnimationStyle(R.style.recommondPopupAnimation);
		recommendWindow.setOnDismissListener(new OnDismissListener() {			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public int getDpSize(int size){
		return (int) (size*context.getResources().getDisplayMetrics().density);
	}

	private View initRecommendComponent() {
		mWidthPixels = context.getResources().getDisplayMetrics().widthPixels;
		LayoutInflater mflayout = LayoutInflater.from(context);
		View view = mflayout.inflate(R.layout.share_music_layout, null);
		wx_view = view.findViewById(R.id.weixin_layout);
		pyq_view = view.findViewById(R.id.pyq_layout);
		qq_view = view.findViewById(R.id.qq_layout);
		wx_view.setOnClickListener(this);
		pyq_view.setOnClickListener(this);
		qq_view.setOnClickListener(this);
		return view;
	}

	public void setMsg(String title,String content,String url,String imageUrl) {
		// TODO Auto-generated method stub
		this.title = title;
		this.content = content;
		this.url = url;
		this.imageUrl = imageUrl;
	}
	

	public PopupWindow getPopupWindow() {
		return recommendWindow;
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.weixin_layout:
			weixinUtil.sendMusicReq(0, title,content, url, imageUrl);	
			break;
		case R.id.pyq_layout:
			weixinUtil.sendMusicReq(1, title,content, url, imageUrl);		
			break;
		case R.id.qq_layout:
			qqUtil.onAudioShare(title,content, url, imageUrl);
			break;
		}
	}	
	
}
