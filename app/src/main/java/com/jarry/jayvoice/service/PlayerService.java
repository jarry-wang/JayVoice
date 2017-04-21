/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jarry.jayvoice.service;



import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.PlaylistEntry;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.media.PlayerEngine;
import com.jarry.jayvoice.media.PlayerEngineImpl;
import com.jarry.jayvoice.media.PlayerEngineListener;
import com.jarry.jayvoice.util.BaseTools;
import com.jarry.jayvoice.util.Logger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Background player
 * 
 * @author Lukasz Wisniewski
 */
public class PlayerService extends Service{
	
	public static final String ACTION_PLAY = "play";
	public static final String ACTION_NEXT = "next";
	public static final String ACTION_PREV = "prev";
	public static final String ACTION_STOP = "stop";
	public static final String ACTION_BIND_LISTENER = "bind_listener";

	private WifiManager mWifiManager;
	private WifiLock mWifiLock;
	private PlayerEngine mPlayerEngine;
	private TelephonyManager mTelephonyManager;
	private PhoneStateListener mPhoneStateListener;
	private NotificationManager mNotificationManager = null;
	private RemoteViews mRemoteViews;
	private static final int PLAYING_NOTIFY_ID = 667667;
//	Notification mNotification;
	Notification notify;
	BroadcastReceiver onClickReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		Logger.i("Player Service onBind");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Logger.i("Player Service onUnbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onCreate()
	{
		Logger.i("Player Service onCreate");
		
		// All necessary Application <-> Service pre-setup goes in here
		
		mPlayerEngine = new PlayerEngineImpl();
		mPlayerEngine.setListener(mLocalEngineListener);

		mTelephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mPhoneStateListener = new PhoneStateListener(){

			@Override
			public void onCallStateChanged(int state, String incomingNumber) 
			{
				Logger.e("onCallStateChanged");
				if (state == TelephonyManager.CALL_STATE_IDLE)
				{
					// resume playback
				} else { 
					if(mPlayerEngine != null){
						mPlayerEngine.pause();
					}
				}
			}

		};
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		mNotificationManager = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );

		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiLock = mWifiManager.createWifiLock(MyApplication.TAG);
		mWifiLock.setReferenceCounted(false);
		
		MyApplication.getInstance().setConcretePlayerEngine(mPlayerEngine);
		mRemoteEngineListener = MyApplication.getInstance().fetchPlayerEngineListener();
		
		onClickReceiver = new BroadcastReceiver() {
			 
	           @Override
	           public void onReceive(Context context, Intent intent) {
	               if (intent.getAction().equals(ACTION_BUTTON)) {
	            	   int id = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
	               	   switch (id) {
						case BUTTON_PALY_ID:
							if(mPlayerEngine.isPlaying()){  
								mPlayerEngine.pause();
					            mRemoteViews.setImageViewResource(R.id.notification_play, R.drawable.ic_bofang);  
					        }else{  
					        	mPlayerEngine.play();
					            mRemoteViews.setImageViewResource(R.id.notification_play, R.drawable.ic_zanting);  
					        } 
							mNotificationManager.notify(PLAYING_NOTIFY_ID, notify); 
							break;	
						case BUTTON_NEXT_ID:
							mPlayerEngine.next();
							break;
						case BUTTON_CANCEL_ID:
							mPlayerEngine.clearPlaylist();
							MyApplication.getInstance().clearData();
							PlayerService.this.stopSelf();
							break;
					}
	               }
	           }
	       };
		   IntentFilter filter = new IntentFilter();
	       filter.addAction(ACTION_BUTTON);
	       registerReceiver(onClickReceiver, filter);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);		
		if(intent == null){
			return;
		}
		
		String action = intent.getAction();
		Logger.i("Player Service onStart - "+action);
		
		if(action.equals(ACTION_STOP)){
			stopSelfResult(startId);
			return;
		}
		
		if(action.equals(ACTION_BIND_LISTENER)){
			mRemoteEngineListener = MyApplication.getInstance().fetchPlayerEngineListener();
			return;
		}
		
		// we need to have up-to-date playlist if any of play,next,prev buttons is pressed
		updatePlaylist();
		
		if(action.equals(ACTION_PLAY)){	
			mPlayerEngine.play();
			return;
		}
		
		if(action.equals(ACTION_NEXT)){	
			mPlayerEngine.next();
			return;
		}
		
		if(action.equals(ACTION_PREV)){	
			mPlayerEngine.prev();
			return;
		}
	}
	
	/**
	 * Fetches a new playlist if its reference address differs from the current one  
	 */
	private void updatePlaylist(){
		if(mPlayerEngine.getPlaylist() != MyApplication.getInstance().fetchPlaylist()){
			mPlayerEngine.openPlaylist(MyApplication.getInstance().fetchPlaylist());
		}
	}
	
	@Override
	public void onDestroy() {
		Logger.i("Player Service onDestroy");
		MyApplication.getInstance().setConcretePlayerEngine(null);
		mRemoteViews = null;
		mPlayerEngine.stop();
		mPlayerEngine = null;
		// unregister listener
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);		
		try {
			if(onClickReceiver!=null)
				unregisterReceiver(onClickReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}		
		super.onDestroy();
		System.exit(0);
	}

	/**
	 * Hint: if necessary this can be extended to ArrayList of listeners in the future, though
	 * I do not expect that it will be necessary
	 */
	private PlayerEngineListener mRemoteEngineListener;

	/**
	 * Sends notification to the status bar + passes other notifications to remote listeners
	 */
	private PlayerEngineListener mLocalEngineListener = new PlayerEngineListener(){

		@Override
		public void onTrackBuffering(int percent) {
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onTrackBuffering(percent);
			}

		}

		@Override
		public void onTrackChanged(PlaylistEntry playlistEntry) {
			displayNotifcation(playlistEntry);
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onTrackChanged(playlistEntry);
			}
			
			// last.fm scrobbler
			boolean lastFm = PreferenceManager.getDefaultSharedPreferences(PlayerService.this).getBoolean("lastfm_scrobble", false);
			if(lastFm){
				scrobblerMetaChanged();
			}
		}

		@Override
		public void onTrackProgress(int seconds) {
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onTrackProgress(seconds);
			}
		}

		@Override
		public void onTrackStop() {
			// allow killing this service
			// NO-OP setForeground(false);
			mWifiLock.release();
			
			mNotificationManager.cancel(PLAYING_NOTIFY_ID);
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onTrackStop();
			}
			changeNotifyPause();
		}

		@Override
		public boolean onTrackStart() {
			// prevent killing this service
			// NO-OP setForeground(true);
			mWifiLock.acquire();
			
			if(mRemoteEngineListener != null){
				if( !mRemoteEngineListener.onTrackStart() )
					return false;
			}

			boolean wifiOnlyMode = PreferenceManager.getDefaultSharedPreferences(PlayerService.this).getBoolean("wifi_only", false);

			// wifi only mode
			if(wifiOnlyMode && !mWifiManager.isWifiEnabled()){
				return false;
			}

			// roaming protection
			boolean roamingProtection = PreferenceManager.getDefaultSharedPreferences(PlayerService.this).getBoolean("roaming_protection", true);
			if(!mWifiManager.isWifiEnabled()){
				if(roamingProtection && mTelephonyManager.isNetworkRoaming())
					return false;
			}
			changeNotifyPlay();
			return true;
		}

		@Override
		public void onTrackPause() {
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onTrackPause();
			}
			changeNotifyPause();
		}

		@Override
		public void onTrackStreamError() {
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onTrackStreamError();
			}
		}

		@Override
		public void onMaxProgress(int max) {
			// TODO Auto-generated method stub
			if(mRemoteEngineListener != null){
				mRemoteEngineListener.onMaxProgress(max);
			}
		}

	};

	/**
	 * This snippet of code scrobbles music to last.fm servers using the official
	 * last.fm client
	 */
	private void scrobblerMetaChanged(){
		PlaylistEntry entry = mPlayerEngine.getPlaylist().getSelectedTrack();
		if(entry != null){
			Intent i = new Intent("fm.last.android.metachanged");
			i.putExtra("artist", entry.getSong().getAlbum().getArtistName());
			i.putExtra("album", entry.getSong().getAlbum().getName());
			i.putExtra("track", entry.getSong().getName());
//			i.putExtra("duration", entry.getSong().getDuration()*1000); // duration in milliseconds
			sendBroadcast(i);
		}
	}

	final String STATUS_BAR_COVER_CLICK_ACTION = "STATUS_BAR_COVER_CLICK_ACTION";
	
	private void displayNotifcation(PlaylistEntry playlistEntry){
		final Song song =  playlistEntry.getSong();		
		new Thread(new Runnable() {			
			
			public void run() {
				showButtonNotify(song);	        
			}
		}).start();
	}

	final String ACTION_BUTTON = "ACTION_BUTTON";
	final String INTENT_BUTTONID_TAG = "INTENT_BUTTONID_TAG";
	final int BUTTON_PALY_ID = 1;
	final int BUTTON_NEXT_ID = 2;
	final int BUTTON_CANCEL_ID = 3;
	/** 
     * 带按钮的通知栏 
     */  
    public void showButtonNotify(Song song){  
        NotificationCompat.Builder mBuilder = new Builder(this);  
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification);  
        mRemoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_launcher);  
        //API3.0 以上的时候显示按钮，否则消失  
        mRemoteViews.setTextViewText(R.id.notification_title, song.getName());  
        mRemoteViews.setTextViewText(R.id.notification_arties, song.getSinger().getName());  
        //如果版本号低于（3。0），那么不显示按钮  
        if(BaseTools.getSystemVersion() <= 9){  
            mRemoteViews.setViewVisibility(R.id.notification_button_view, View.GONE);  
        }else{  
            mRemoteViews.setViewVisibility(R.id.notification_button_view, View.VISIBLE);  
        }  
        //  
        if(mPlayerEngine.isPlaying()){  
            mRemoteViews.setImageViewResource(R.id.notification_play, R.drawable.ic_zanting);  
        }else{  
            mRemoteViews.setImageViewResource(R.id.notification_play, R.drawable.ic_bofang);  
        } 
       
        //点击的事件处理  
        Intent buttonIntent = new Intent(ACTION_BUTTON);   
        /* 播放/暂停  按钮 */  
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);  
        PendingIntent intent_paly = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
        mRemoteViews.setOnClickPendingIntent(R.id.notification_play, intent_paly);  
        /* 下一首 按钮  */  
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);  
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
        mRemoteViews.setOnClickPendingIntent(R.id.notification_next, intent_next);
        /* 取消 按钮  */  
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_CANCEL_ID);  
        PendingIntent intent_cancel = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
        mRemoteViews.setOnClickPendingIntent(R.id.notification_cacel, intent_cancel);
          
        mBuilder.setContent(mRemoteViews)  
                .setContentIntent(getDefalutIntent(PendingIntent.FLAG_UPDATE_CURRENT))  
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示  
                .setTicker("正在播放")  
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级  
//                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher); 
        
        notify = mBuilder.build();  
        notify.flags = Notification.FLAG_NO_CLEAR;  
        mNotificationManager.notify(PLAYING_NOTIFY_ID, notify);  
    }  
    
    private void changeNotifyPlay() {
		// TODO Auto-generated method stub
    	if(mRemoteViews == null || mNotificationManager == null)return;
    	mRemoteViews.setImageViewResource(R.id.notification_play, R.drawable.ic_zanting); 
    	mNotificationManager.notify(PLAYING_NOTIFY_ID, notify);
	}
    
    private void changeNotifyPause() {
		// TODO Auto-generated method stub
    	if(mRemoteViews == null || mNotificationManager == null)return;
    	mRemoteViews.setImageViewResource(R.id.notification_play, R.drawable.ic_bofang); 
    	mNotificationManager.notify(PLAYING_NOTIFY_ID, notify);
	}
    
    public PendingIntent getDefalutIntent(int flags){  
    	Intent intent=new Intent(PlayerService.this, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, flags);  
        return pendingIntent; 
    }  
    
    
}
