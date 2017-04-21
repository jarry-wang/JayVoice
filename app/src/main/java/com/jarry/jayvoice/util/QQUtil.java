package com.jarry.jayvoice.util;


import java.util.ArrayList;

import org.json.JSONObject;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.core.Config;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;


public class QQUtil {
	Context context;
	Tencent mTencent;
	String tjTitle,tjContent,pageUrl;
	public QQUtil(Context context,Tencent tencent){
		this.context = context;
		this.mTencent = tencent;
		tjTitle = Config.invitationTitle;
		tjContent = Config.invitationInfo;
		pageUrl = MyApplication.getInstance().getAppUrl();
	}
	public void share(String tjCode){
	    Bundle params = new Bundle();
	    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, tjTitle);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, tjContent);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  pageUrl);
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,Config.LOGO_URL);
//	    params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用222222");
//	    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");		
	    mTencent.shareToQQ((Activity) context, params, new BaseUiListener());
	 } 
	
	public void doLogin(IUiListener listener) {
		// TODO Auto-generated method stub
		MyApplication myApplication = (MyApplication) context.getApplicationContext();
		if(myApplication.isLogin()){
			mTencent.logout(context);
		}
		if (!mTencent.isSessionValid()){
			mTencent.login((Activity) context, "get_user_info,get_simple_userinfo", listener);
		}
	}
	
	public void share(String title,String content,String page){
	    Bundle params = new Bundle();
	    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  page);		
	    mTencent.shareToQQ((Activity) context, params, new BaseUiListener());

	 } 
	
	public void shareToQQzone() {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, tjTitle);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  tjContent);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  pageUrl);
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(Config.LOGO_URL);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);  
		mTencent.shareToQzone((Activity)context, params, new BaseUiListener());
	}
	
	public void onAudioShare(String title,String content,String url,String picUrl) {
	    final Bundle params = new Bundle();
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  pageUrl);
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, picUrl);
	    params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, url);		
	    mTencent.shareToQQ((Activity)context, params, new BaseUiListener());
	}
	
//	public void onAudioShareQzone(String content,String url,String picUrl) {
//		final Bundle params = new Bundle();
//		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_);
//		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
//		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  content);
//		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  page);
//		ArrayList<String> imageUrls = new ArrayList<String>();
//		imageUrls.add(Config.LOGO_URL);
//		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
//		params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);  
//		mTencent.shareToQzone((Activity)context, params, new BaseUiListener());
//	}
	
	
	public void shareToQQzone(String title,String content,String page) {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  content);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  page);
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(Config.LOGO_URL);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);  
		mTencent.shareToQzone((Activity)context, params, new BaseUiListener());
	}


	
	public void sendReq(String content) {
		// TODO Auto-generated method stub
		Bundle params = new Bundle();
	    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE,context.getString(R.string.app_name)+"APP"+"邻居分享");
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  pageUrl);		
	    mTencent.shareToQQ((Activity) context, params, new BaseUiListener());
	}
	
	public void sendNewsReq(String title,String content,String page) {
		// TODO Auto-generated method stub
		Bundle params = new Bundle();
	    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE,context.getString(R.string.app_name)+"APP"+"-"+title+"推荐");
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  page+"?isbrowser=yes");		
	    mTencent.shareToQQ((Activity) context, params, new BaseUiListener());
	}
	
	
	private class BaseUiListener implements IUiListener {
		@Override
		public void onComplete(Object response) {
		//V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
			doComplete((JSONObject)response);
		}
		protected void doComplete(JSONObject values) {
			//分享成功
			Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onError(UiError e) {
			//在这里处理错误信息
			if(e.errorMessage == null){
				Toast.makeText(context, "没有安装QQ", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "分享失败"+e.errorMessage, Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		public void onCancel() {
			//分享被取消
			Toast.makeText(context, "分享取消", 2000).show();
		}
	}


	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	
}
