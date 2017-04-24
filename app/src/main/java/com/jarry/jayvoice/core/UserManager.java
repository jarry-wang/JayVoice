package com.jarry.jayvoice.core;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import cn.bmob.v3.BmobUser;
import cn.join.android.util.ToastUtil;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.bean.Collect;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.bean.UserQq;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.util.SharedPrefUtil;
import android.content.Context;

public class UserManager {

	Context context;
	MyApplication application;
	SharedPrefUtil prefUtil;
	ToastUtil toastUtil;

	private static UserManager userManager;

	public static UserManager getInstance(Context context){
		if (userManager == null) {
			synchronized (UserManager.class) {
				if (userManager == null) {
					userManager = new UserManager(context);
				}
			}
		}
		return userManager;
	}

	private UserManager(Context context){
		this.context = context;
		application = (MyApplication) context.getApplicationContext();
		prefUtil = SharedPrefUtil.getInstance(context);
		toastUtil = new ToastUtil(context);
	}
	
	public boolean isLogin(){
		return application.isLogin();
	}
	
	public boolean getPrefLogin() {
		// TODO Auto-generated method stub
		return prefUtil.getLogin();
	}
	
	public void saveLoginData(User user,String loginType) {
		// TODO Auto-generated method stub
		application.setUser(user);
		application.setUserId(user.getUsername());
		application.setLogin(true);		
		prefUtil.saveUserInfo(user);
		prefUtil.setLogin(true);
		prefUtil.setLoginType(loginType);
	}
	
	public void getLoginInfo() {
		// TODO Auto-generated method stub
    		// TODO Auto-generated method stub	
		application.setLogin(true);		
		User user = BmobUser.getCurrentUser(User.class);
		if(user == null){
			application.setUser(prefUtil.getUserInfo());
		}else{
			application.setUser(user);
		}		
	}
	
	public String getLoginType() {
		// TODO Auto-generated method stub
		return prefUtil.getLoginType();
	}
	
	public void loginOut() {
		// TODO Auto-generated method stub
		application.setLogin(false);
		prefUtil.setLogin(false);
		prefUtil.clearUserInfo();
	}

	public UserQq getUser(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		UserQq userQq = new UserQq();
		try {
			userQq.figureurl_qq_2 = jsonObject.getString("figureurl_qq_2");
			userQq.nickname = jsonObject.getString("nickname");
			userQq.gender = jsonObject.getString("gender");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userQq;
	}
	
	public User getUser() {
		// TODO Auto-generated method stub
		return application.getUser();
	}

	public void setUser(User user) {
		// TODO Auto-generated method stub
		application.setUser(user);
	}

	public interface RefreshUserinfoCallback {
		void succuss();
	}

	public void refreshUserInfo(Context context,final RefreshUserinfoCallback callback) {
		GetDataBusiness getDataBusiness =new GetDataBusiness(context);
		getDataBusiness.getUser(application.getUserId(), new GetDataBusiness.ResUserHandler() {

			@Override
			public void onResponse(User result) {
				// TODO Auto-generated method stub
				setUser(result);
				callback.succuss();
			}

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				toastUtil.showToast("获取用户信息失败");
			}
		});
	}
	
	/**
	 * 保存收藏在本地
	 */
	public void saveConnection(List<Collect> connections) {
		// TODO Auto-generated method stub
		List<Song> collectionSongs = new ArrayList<Song>();
		for (int i = 0; i < connections.size(); i++) {
			collectionSongs.add(connections.get(i).song);
		}
		application.setCollectionSongs(collectionSongs);
	}
	
	public boolean ifNullLocalCollection(){
		if(ListUtil.isNotNull(application.getCollectionSongs())){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 查询某首歌是否在收藏里
	 */
	public boolean ifInCollection(Song song) {
		// TODO Auto-generated method stub
		List<Song> collections = application.getCollectionSongs();
		if(ListUtil.isNotNull(collections)){			
			for (Song song2 : collections) {
				if(song2.getObjectId().equals(song.getObjectId())){
					return true;
				}
			}
		}else{			
		}
		return false;
	}
	
	/**
	 * 去掉某个收藏
	 */
	public boolean delOneCollection(Song song) {
		// TODO Auto-generated method stub
		List<Song> collectionSongs = application.getCollectionSongs();
		if(ListUtil.isNotNull(collectionSongs)){
			for (int i = 0; i < collectionSongs.size(); i++) {
				Song song2 = collectionSongs.get(i);
				if(song2.getObjectId().equals(song.getObjectId())){
					collectionSongs.remove(song2);
					application.setCollectionSongs(collectionSongs);
				}
			}
		}else{			
		}
		return false;
	}
	
	/**
	 * 增加收藏
	 */
	public boolean addOneCollection(Song song) {
		// TODO Auto-generated method stub
		List<Song> collectionSongs = application.getCollectionSongs();
		if(ListUtil.isNotNull(collectionSongs)){
			collectionSongs.add(song);
		}else{	
			collectionSongs = new ArrayList<Song>();
			collectionSongs.add(song);
		}
		application.setCollectionSongs(collectionSongs);
		return false;
	}
}
