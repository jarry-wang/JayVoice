package com.jarry.jayvoice.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jarry.jayvoice.bean.User;

/**
 * 存储用户信息
 * @author ASRock
 *
 */
public class SharedPrefUtil {
	Context context;
	private final String userPref = "userInfo";
	private final String userPrefId = "userId";
	private final String userPrefToken = "userPrefToken";
	private final String userPrefName = "userPrefName";
	private final String userPrefPic = "userPrefPic";
	private final String ifFirstInstall = "ifInstall";
	private final String ifLogin = "ifLogin";
	private final String loginType = "loginType";
	
	private final String storePref = "communityInfo";
	private final String cityName = "cityName";
	private final String storeId = "storeId";
	private final String storeName = "storeName";
	
	private final String setPref = "cityName";
	private final String ifReceiveNotice = "ifReceiveNotice";

	private static SharedPrefUtil sharedPrefUtil;

	public static SharedPrefUtil getInstance(Context context){
		if (sharedPrefUtil == null) {
			synchronized (SharedPrefUtil.class) {
				if (sharedPrefUtil == null) {
					sharedPrefUtil = new SharedPrefUtil(context);
				}
			}
		}
		return sharedPrefUtil;
	}

	private SharedPrefUtil(Context context){
		this.context = context;   
	}
	public void saveUserInfo(User user){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(userPrefId, user.getUsername());
		editor.putString(userPrefToken, user.accessToken);
		editor.putString(userPrefName, user.nickName);
		editor.putString(userPrefPic, user.imgUrl);
		editor.apply();//提交修改
	}

	public User getUserInfo(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);
		User user = new User();
		user.setUsername(sharedPreferences.getString(userPrefId, ""));
		user.nickName = sharedPreferences.getString(userPrefName, "");
		user.imgUrl = sharedPreferences.getString(userPrefPic, "");
		user.accessToken =  sharedPreferences.getString(userPrefToken, "");
		return user;
	}
	
	public void setLogin(boolean flag){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(ifLogin, flag);
		editor.apply();//提交修改
	}
	
	public void setLoginType(String type){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(loginType, type);    
		editor.apply();//提交修改
	}
	
	public String getLoginType(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);     
        String type = sharedPreferences.getString(loginType, "");
        return type;
	}
	
	public void clearUserInfo(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(userPrefId, "");
		editor.putString(userPrefToken, "");
		editor.putString(loginType, "");
		editor.putString(userPrefName, "");
		editor.putString(userPrefPic, "");
		editor.apply();//提交修改
	}
	
	public boolean getLogin(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);     
		boolean login = sharedPreferences.getBoolean(ifLogin, false);
        return login;
	}
	
	public String getUserId(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);     
        String userId = sharedPreferences.getString(userPrefId, "");
        Logger.d("userId:"+userId);
        return userId;
	}
	
	
	public String getUserTokenKey(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);     
        String userTokenKey = sharedPreferences.getString(userPrefToken, "");
        Logger.d("userTokenKey:"+userTokenKey);
        return userTokenKey;
	}
	
	@SuppressLint("CommitPrefEdits") 
	public void saveInstallInfo(boolean flag){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(ifFirstInstall, flag);
		editor.apply();//提交修改
	}
	
	public boolean getInstallInfo(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(userPref, Context.MODE_PRIVATE);     
        boolean ifInstall = sharedPreferences.getBoolean(ifFirstInstall, false);
        return ifInstall;
	}
	
	
	
	public boolean setIfReceiveNotice(boolean flag){
		SharedPreferences sharedPreferences = context.getSharedPreferences(setPref, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(ifReceiveNotice, flag);
		return editor.commit();
	}
	
	public boolean getIfReceiveNotice(){
		SharedPreferences sharedPreferences = context.getSharedPreferences(setPref, Context.MODE_PRIVATE);     
		boolean ifReceive = sharedPreferences.getBoolean(ifReceiveNotice, true);
        return ifReceive;
	}
}
