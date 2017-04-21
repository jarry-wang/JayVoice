package com.jarry.jayvoice.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.core.Config;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

public class WeixinUtil {
	IWXAPI iwxapi;
	Context context;
	String tjTitle,tjContent,pageUrl;
	public WeixinUtil(Context context, IWXAPI iwxapi){
		this.iwxapi = iwxapi;
		this.context = context;
		tjTitle = Config.invitationTitle;
		tjContent = Config.invitationInfo;
		pageUrl = MyApplication.getInstance().getAppUrl();
	}
	
	public void sendReq(int flag,String tjCode) {	//0微信好友，1微信朋友圈
		  // 初始化WXTextObject对象		 
		  WXWebpageObject webpage = new WXWebpageObject();  
		  webpage.webpageUrl = pageUrl;
		  // 用WXTextObject对象初始化一个WXMediaMessage对象
		  WXMediaMessage msg = new WXMediaMessage(webpage);		 
		  msg.title = tjTitle;
		  msg.description = tjContent;
		  Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		  msg.setThumbImage(thumb);
		  // 构造一个Req		 
		  SendMessageToWX.Req req = new SendMessageToWX.Req();	 
		  req.message = msg;	 
		  req.transaction = String.valueOf(System.currentTimeMillis());// 唯一字段，标识一个请求
		  req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
		  iwxapi.sendReq(req);	
	}
	
	public void sendReq(int flag,String title,String content,String url) {	//0微信好友，1微信朋友圈
		  // 初始化WXTextObject对象		 
		  WXWebpageObject webpage = new WXWebpageObject();  
		  webpage.webpageUrl = url;
		  // 用WXTextObject对象初始化一个WXMediaMessage对象
		  WXMediaMessage msg = new WXMediaMessage(webpage);		 
		  msg.title = title;
		  msg.description = content;
		  Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		  msg.setThumbImage(thumb);
		  // 构造一个Req		 
		  SendMessageToWX.Req req = new SendMessageToWX.Req();	 
		  req.message = msg;	 
		  req.transaction = String.valueOf(System.currentTimeMillis());// 唯一字段，标识一个请求
		  req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
		  iwxapi.sendReq(req);		 
	}
	
	public void sendMusicReq(int flag,String title,String content,String url,String imageUrl) {	//0微信好友，1微信朋友圈
		  // 初始化WXTextObject对象	
		  WXMusicObject musicObject = new WXMusicObject();
		  musicObject.musicUrl = pageUrl;
		  musicObject.musicDataUrl = url;
		  WXMediaMessage msg = new WXMediaMessage();
		  msg.mediaObject = musicObject;
		  msg.title = title;
		  msg.description = content;
		  Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		  msg.setThumbImage(thumb);
		  // 构造一个Req		 
		  SendMessageToWX.Req req = new SendMessageToWX.Req();	 
		  req.message = msg;	 
		  req.transaction = String.valueOf(System.currentTimeMillis());// 唯一字段，标识一个请求
		  req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
		  iwxapi.sendReq(req);		 
	}
	
}
