package com.jarry.jayvoice;

import java.util.List;

import a.b.c.CommonManager;
import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;
import com.jarry.jayvoice.bean.Playlist;
import com.jarry.jayvoice.bean.SType;
import com.jarry.jayvoice.bean.Singer;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.core.Config;
import com.jarry.jayvoice.media.PlayerEngine;
import com.jarry.jayvoice.media.PlayerEngineListener;
import com.jarry.jayvoice.service.PlayerService;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.SharedPrefUtil;
import com.jarry.jayvoice.util.StringUtils;

import a.b.c.DynamicSdkManager;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class MyApplication extends Application{

	public static String TAG = "jayvoice";
	private Singer singer;

	/**
	 * Singleton pattern
	 */
	private static MyApplication instance;
	
	/**
	 * Image cache, one for all activities and orientations
	 */
//	private ImageCache mImageCache;
	
	/**
	 * Web request cache, one for all activities and orientations
	 */
//	private RequestCache mRequestCache;

	/**
	 * Service player engine
	 */
	private PlayerEngine mServicePlayerEngine;
	
	/**
	 * Intent player engine
	 */
	private PlayerEngine mIntentPlayerEngine;

	/**
	 * Player engine listener
	 */
	private PlayerEngineListener mPlayerEngineListener;
	
	/**
	 * Download interface
	 */
//	private DownloadInterface mDownloadInterface;
	
	/**
	 * Stored in Application instance in case we destory Player service
	 */
	private Playlist mPlaylist;
	
	/**
	 * Stores info about this session's finished downloads
	 */
//	private ArrayList<DownloadJob> mCompletedDownloads;
	
	/**
	 * Stores info about this session's downloads that are queued
	 */
//	private ArrayList<DownloadJob> mQueuedDownloads;
	public boolean isLogin = false;
	
	private boolean ifUserInfoChange;
	
	private boolean ifCollectChange;
	
	private boolean ifPlayChange;
	
	SharedPrefUtil prefUtil;
	
	private User user; 
	
	private String userId; 
	
	private String appUrl;
	
	private List<Song> collectionSongs;

	
	public static MyApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Logger.d("MyApplication--oncreate");
		instance = this;
		prefUtil = SharedPrefUtil.getInstance(this);
		//有米广告
		CommonManager.getInstance(this).init("04ae730e20cffd38", "7b68518189d403e9", false);
		//bmob初始化
		Bmob.initialize(this, "9a74543266e5a754c4de1d129c51cf6e");

		DynamicSdkManager.getInstance(this).loadInDate("2015-06-06");
		try {
			// appId和APPsecret设置在manifest上时调用此方法
			DynamicSdkManager.onCreate(this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
	}

	public void setConcretePlayerEngine(PlayerEngine playerEngine) {
		this.mServicePlayerEngine = playerEngine;
	}
	public PlayerEngine getPlayerEngineInterface() {
		// request service bind
		if(mIntentPlayerEngine == null){
			mIntentPlayerEngine = new IntentPlayerEngine();
		}
		return mIntentPlayerEngine;
	}
	
	public void setPlayerEngineListener(PlayerEngineListener l){
		getPlayerEngineInterface().setListener(l);
	}
	public PlayerEngineListener fetchPlayerEngineListener(){
		return mPlayerEngineListener;
	}
	
	public Playlist fetchPlaylist(){
		return mPlaylist;
	}
	
	public String getVersion(){
		String version = "0.0.0";
		
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return version;
	}
	
	private class IntentPlayerEngine implements PlayerEngine{

		@Override
		public Playlist getPlaylist() {
			return mPlaylist;
		}

		@Override
		public boolean isPlaying() {
			if(mServicePlayerEngine == null){
				// service does not exist thus no playback possible
				return false;
			} else {
				return mServicePlayerEngine.isPlaying();
			}
		}

		@Override
		public void next() {
			if(mServicePlayerEngine != null){
				playlistCheck();
				mServicePlayerEngine.next();
			} else {
				startAction(PlayerService.ACTION_NEXT);
			}
		}

		@Override
		public void openPlaylist(Playlist playlist) {
			mPlaylist = playlist;
			if(mServicePlayerEngine != null){
				mServicePlayerEngine.openPlaylist(playlist);
			}
		}

		@Override
		public void pause() {
			if(mServicePlayerEngine != null){
				mServicePlayerEngine.pause();
			}
		}

		@Override
		public void play() {
			Logger.i("IntentPlayerEngine--play--mServicePlayerEngine="+mServicePlayerEngine);
			if(mServicePlayerEngine != null){
				playlistCheck();
				mServicePlayerEngine.play();
			} else {
				startAction(PlayerService.ACTION_PLAY);
			}
		}

		@Override
		public void prev() {
			if(mServicePlayerEngine != null){
				playlistCheck();
				mServicePlayerEngine.prev();
			} else {
				startAction(PlayerService.ACTION_PREV);
			}
		}

		@Override
		public void setListener(PlayerEngineListener playerEngineListener) {
			mPlayerEngineListener = playerEngineListener;
			// we do not want to set this listener if Service
			// is not up and a new listener is null
			if(mServicePlayerEngine != null || mPlayerEngineListener != null){
				startAction(PlayerService.ACTION_BIND_LISTENER);
			}
		}

		@Override
		public void skipTo(int index) {
			if(mServicePlayerEngine != null){
				mServicePlayerEngine.skipTo(index);
			}
		}

		@Override
		public void stop() {
			startAction(PlayerService.ACTION_STOP);
			//stopService(new Intent(JamendoApplication.this, PlayerService.class));
		}
		
		private void startAction(String action){
			Intent intent = new Intent(MyApplication.this, PlayerService.class);
			intent.setAction(action);
			startService(intent);
		}
		
		/**
		 * This is required if Player Service was binded but playlist was not passed from
		 * Application to Service and one of buttons: play, next, prev is pressed
		 */
		private void playlistCheck(){
			if(mServicePlayerEngine != null){
				if(mServicePlayerEngine.getPlaylist() == null && mPlaylist != null){
					mServicePlayerEngine.openPlaylist(mPlaylist);
				}
			}
		}

		@Override
		public void seekTo(int position) {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				mServicePlayerEngine.seekTo(position);
			} 
		}

		@Override
		public int getCurrentPosition() {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				return mServicePlayerEngine.getCurrentPosition();
			} 
			return 0;
		}

		@Override
		public int getDuration() {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				return mServicePlayerEngine.getDuration();
			}
			return 0;
		}

		@Override
		public void setPlayTypes(List<SType> sTypes) {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				mServicePlayerEngine.setPlayTypes(sTypes);
			}
			
		}

		@Override
		public List<SType> getPlayTypes() {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				return mServicePlayerEngine.getPlayTypes();
			}
			return null;
		}

		@Override
		public void clearPlaylist() {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				mServicePlayerEngine.clearPlaylist();
			}
		}

		@Override
		public void playSong(Song song) {
			// TODO Auto-generated method stub
			if(mServicePlayerEngine != null){				
				mServicePlayerEngine.playSong(song);
			}
		}
		
	}
	public Singer getSinger() {
		return singer;
	}

	public void setSinger(Singer singer) {
		this.singer = singer;
	}
	
	public void clearData() {
		// TODO Auto-generated method stub
		mPlaylist = null;
		mIntentPlayerEngine= null;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean isIfUserInfoChange() {
		return ifUserInfoChange;
	}

	public void setIfUserInfoChange(boolean ifUserInfoChange) {
		this.ifUserInfoChange = ifUserInfoChange;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getAppName() {
		// TODO Auto-generated method stub
		return getString(R.string.app_name);
	}

	public String getAppUrl() {
		if(StringUtils.isNull(appUrl)){
			return Config.APP_URL_XIAOMI;
		}
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public List<Song> getCollectionSongs() {
		return collectionSongs;
	}

	public void setCollectionSongs(List<Song> collectionSongs) {
		this.collectionSongs = collectionSongs;
	}

	public boolean isIfCollectChange() {
		return ifCollectChange;
	}

	public void setIfCollectChange(boolean ifCollectChange) {
		this.ifCollectChange = ifCollectChange;
	}

	public boolean isIfPlayChange() {
		return ifPlayChange;
	}

	public void setIfPlayChange(boolean ifPlayChange) {
		this.ifPlayChange = ifPlayChange;
	}

	
	
	
}
