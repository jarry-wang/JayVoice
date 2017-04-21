package com.jarry.jayvoice.activity.main.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.LoginActivity;
import com.jarry.jayvoice.activity.main.interf.MainInterf;
import com.jarry.jayvoice.bean.Album;
import com.jarry.jayvoice.bean.BaseBean;
import com.jarry.jayvoice.bean.Collect;
import com.jarry.jayvoice.bean.Playlist;
import com.jarry.jayvoice.bean.PlaylistEntry;
import com.jarry.jayvoice.bean.Qrcode;
import com.jarry.jayvoice.bean.SType;
import com.jarry.jayvoice.bean.Singer;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.core.Config;
import com.jarry.jayvoice.core.GetDataBusiness;
import com.jarry.jayvoice.core.GetDataBusiness.DelHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResCollectionHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResCollectionListHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResQrcodeHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResSongListHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResTypeHandler;
import com.jarry.jayvoice.core.GetDataBusiness.SaveHandler;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.media.PlayerEngine;
import com.jarry.jayvoice.media.PlayerEngineListener;
import com.jarry.jayvoice.util.DisplayUtil;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.QQUtil;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.util.WeixinUtil;
import com.jarry.jayvoice.widget.LyricView;
import com.jarry.jayvoice.widget.RecommendComponent;
import com.jarry.jayvoice.widget.weel.OnWheelScrollListener;
import com.jarry.jayvoice.widget.weel.WheelView;
import com.jarry.jayvoice.widget.weel.dapter.AbstractWheelTextAdapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayMainFragment extends BaseFragment implements MainInterf.PlayMainChild{
	
	protected final int SEEK_MESSAGE = 0x1001;
	protected final int UPDATA_LRC = 0x1002;
	protected final int UPDATA_TYPE = 0x1003;
	private UserManager userManager;
	private RecommendComponent recommendWxComponent;
	protected PopupWindow recommondWindow;
	public View shadowView;

	private ImageView picView;
	private ImageView picPlayView;
	private TypeAdapter typeAdapter;
	private WheelView tyWheelView;
	private TextView selectChannelView;
	private boolean scrolling = false;
	private Animation operatingAnim;
	private Button paly_likeButton;
	private ImageView paly_PlayButton,paly_NextButton;
	private int play_state, pesition, duration;
	private int mode = 3;
	private int CurrentTime = 0;
	private TextView time1View,time2View,songNameView,singerView;
	private SeekBar seekBar;
	private List<SType> sTypes = new ArrayList<SType>();
	private List<Song> songs = new ArrayList<Song>();
	private Song currentSong;
	boolean currentSongIfCol;//当前歌曲是否收藏
	boolean isLive;
	private Playlist mPlaylist;
	private Album mCurrentAlbum;
	protected static final int SEEK_DELAYED = 500;
	private int seek_position;
	private LyricView lyricView;
	private int INTERVAL=45;//歌词每行的间隔  
	private HandlerThread lyricHandlerThread;
	private Handler lyricHandler;
	private View parentView;
	//专辑信息页
	private SType mCurrentType;
	private View rootView;  
	boolean hasSearchLrc = false;//是否加载显示过歌词
	private int currentAngle;
	private boolean ifLogin = false;
	private QQUtil qqUtil;
	private WeixinUtil weixinUtil;
	private MainInterf.MainView mainView;

	public PlayMainFragment(MainInterf.MainView mainView) {
		this.mainView = mainView;
		this.mainView.setPlayMainChild(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container) {
		// TODO Auto-generated method stub
		Logger.d("PlayMainFragment--onCreateView");
		rootView = inflater.inflate(R.layout.frag_home, container,false);
		isLive = true;
		userManager = UserManager.getInstance(getActivity());
		MyApplication.getInstance().setPlayerEngineListener(mPlayerEngineListener);
		regToQQ();
		regToWx();
		qqUtil = new QQUtil(mActivity, mTencent);
		weixinUtil = new WeixinUtil(mActivity, iwxapi);
		initAnim();
        return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mainView.setEnableRefresh(true);
		Logger.d("PlayMainFragment--onResume");
		// register UI listener
		if(ifLogin == application.isLogin()){			
		}else{//登录状态改变,获取用户收藏
			ifLogin = application.isLogin();
			getUserCollection();
		}
		initLyricHander();
		if(!hasSearchLrc){
			showLrc();
		}
		//收藏歌曲发生变化
		if(currentSong!=null&&application.isIfCollectChange()){
			showCollection(currentSong);
			application.setIfCollectChange(false);
		}
    	if(application.isIfPlayChange()){
    		Song song = (Song) mActivity.getIntent().getSerializableExtra("song");
    		playSong(song);
    		application.setIfPlayChange(false);   		
    	}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.d("PlayMainFragment--onDestroyView");
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		parentView = view.findViewById(R.id.anim_view);
		shadowView = view.findViewById(R.id.webview_shadow);
		recommendWxComponent = new RecommendComponent(mActivity, iwxapi,mTencent);
		recommondWindow = recommendWxComponent.getPopupWindow();
		recommondWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				disShadow();
			}
		});
		INTERVAL = DisplayUtil.getDpSize(10, getActivity());

		lyricView = (LyricView) view.findViewById(R.id.mylrc);
		songNameView = (TextView) view.findViewById(R.id.textView_play_name);
		singerView = (TextView) view.findViewById(R.id.textView_play_author);
		picView = (ImageView) view.findViewById(R.id.roundImage_song_pic);
		picPlayView = (ImageView) view.findViewById(R.id.song_pic_playbtn);
		selectChannelView = (TextView) view.findViewById(R.id.textView_play_select_type);
		paly_likeButton = (Button) view.findViewById(R.id.main_playing_button_like);
		paly_PlayButton = (ImageView) view.findViewById(R.id.main_playing_button_play);
		paly_NextButton = (ImageView) view.findViewById(R.id.main_playing_button_next);
		tyWheelView = (WheelView) view.findViewById(R.id.type_gallery);
		time1View = (TextView) view.findViewById(R.id.time1);
		time2View = (TextView) view.findViewById(R.id.time2);
		seekBar = (SeekBar) view.findViewById(R.id.seekbar);
//				typeAdapter = new TypeAdapter(getActivity());
//				tyWheelView.setViewAdapter(typeAdapter);
		tyWheelView.addScrollingListener( new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
//		                updataSelectChannel(tyWheelView.getCurrentItem());
				Message message = new Message();
				message.what = UPDATA_TYPE;
				message.arg1 = tyWheelView.getCurrentItem();
				mHandler.sendMessage(message);
			}
		});
		picPlayView.setOnClickListener(PlayMainFragment.this);
		paly_likeButton.setOnClickListener(PlayMainFragment.this);
		paly_PlayButton.setOnClickListener(PlayMainFragment.this);
		paly_NextButton.setOnClickListener(PlayMainFragment.this);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser&&duration>0){
					doSeek(progress);
				}
			}
		});
		typeAdapter = new TypeAdapter(getActivity());
		tyWheelView.setViewAdapter(typeAdapter);
	}

	private void initLyricHander() {
		// TODO Auto-generated method stub
		lyricHandlerThread = new HandlerThread("lyricThread");
		lyricHandlerThread.start();
		lyricHandler = new Handler(lyricHandlerThread.getLooper(), new Handler.Callback() {
			@Override
			public boolean handleMessage(Message arg0) {
				if (getPlayerEngine().isPlaying()) {
					lyricView.setOffsetY(lyricView.getOffsetY() - lyricView.SpeedLrc());
					lyricView.SelectIndex(getPlayerEngine().getCurrentPosition());
					mHandler.post(mUpdateResults);
				}
				controlLyric();
				return true;
			}
		});
		controlLyric();
		if(getPlayerEngine().isPlaying()&&lyricView!=null){
			lyricView.setOffsetY(220 - lyricView.SelectIndex(getPlayerEngine().getCurrentPosition())
					* (lyricView.getSIZEWORD() + INTERVAL-1));
		}
	}

	private void initAnim() {
		// TODO Auto-generated method stub
		operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.cicle_tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
	}


	@Override
	public void getData() {
		// TODO Auto-generated method stub
		if (mainView != null) {
			mainView.refreshData();
		}
	}

	
	private void getPlayInfo() {
		// TODO Auto-generated method stub
		if(getPlayerEngine() != null){
			if(getPlayerEngine().getPlaylist() == null || getPlayerEngine().getPlayTypes() == null){
				getTypeData();
				return;
			}			
			PlaylistEntry playlistEntry = getPlayerEngine().getPlaylist().getSelectedTrack();
			Song song = playlistEntry.getSong();
			mPlayerEngineListener.onTrackChanged(playlistEntry);
			mPlayerEngineListener.onMaxProgress(getPlayerEngine().getDuration());
			sTypes = getPlayerEngine().getPlayTypes();
			SType currentType = song.getsType();
			for (int i = 0; i < sTypes.size(); i++) {
				SType sType = sTypes.get(i);
				if(sType.getObjectId().equals(currentType.getObjectId())){
					tyWheelView.setCurrentItem(i, false);
					selectChannelView.setText(sType.getTypeName()+"调调");
					break;
				}					
			}						
			showSongData(song,true);
			showTypeData();
			mainView.stopRefresh();
		}else{
			getTypeData();
		}
	}

	
	/**
	 * 获取歌曲分类
	 */
	private void getTypeData() {
		// TODO Auto-generated method stub
		mGetDataBusiness.getTypeList(new ResTypeHandler() {
			@Override
			public void onResponse(List<SType> result) {
			// TODO Auto-generated method stub
			if(ListUtil.isNotNull(result)){
				sTypes = result;
				getPlayerEngine().setPlayTypes(sTypes);
				showTypeData();
				SType type0 = sTypes.get(0);
				getSongs(type0,false);
				tyWheelView.setCurrentItem(0);
				mainView.stopRefresh();
			}
			}
		});
	}
	
	
	Playlist playlist;
	/**
	 * 获取歌曲列表
	 * @param sType 分类
	 */
	private void getSongs(final SType sType,boolean ifReload){
		Logger.i("PlayerActivity.getSongs-"+sType.getTypeName());
		if(sType == mCurrentType && !ifReload){
			return;
		}
		mCurrentType = sType;
		playlist = new Playlist();
		mGetDataBusiness.getSongList(new ResSongListHandler() {
			
			@Override
			public void onResponse(List<Song> result) {
				songs = result;
	        	if(ListUtil.isNotNull(songs)){
	        		BmobFile song0File = songs.get(0).getSong();
	        		if(song0File == null){
	        			getSongs(sType, true);
	        			return;
	        		}else{
	        			showSongData(songs.get(0),true);
	        		}
	        		
	        	}
	        	playlist.addTracks(songs);
	        	loadPlaylist(playlist);
			}
		},sType,ifReload);
	}
	
	private void getSinger() {
		// TODO Auto-generated method stub
		mGetDataBusiness.getSingerInfo(new GetDataBusiness.CommonBeanRedHandler() {
			@Override
			public void onResponse(BaseBean result) {
				notifySingerInfo((Singer) result);
			}
		});
	}
	
	/**
	 * 切换分类
	 * @param positon
	 */
	private void updataSelectChannel(int positon) {
		// TODO Auto-generated method stub
		SType sType = sTypes.get(positon);
		if(positon<sTypes.size())
			selectChannelView.setText(sType.getTypeName()+"调调");
		getSongs(sType,false);
	}
	
	private int getTypeNum(SType sType) {
		// TODO Auto-generated method stub
		for (int i = 0; i < sTypes.size(); i++) {
			if(sTypes.get(i).getObjectId().equals(sType.getObjectId())){
				return i;
			}
		}
		return 0;
	}

	/**
	 * 获取用户收藏
	 */
	private void getUserCollection() {
		// TODO Auto-generated method stub
		mGetDataBusiness.searchCollection(application.getUser(), new ResCollectionListHandler() {

			@Override
			public void onResponse(List<Collect> result) {
				// TODO Auto-generated method stub
				Logger.d("wangfj-getUserCollection-result.size="+result.size());
				userManager.saveConnection(result);
				if(currentSong!=null){
					showCollection(currentSong);
				}
			}
		});
	}

	public void playSong(Song song) {
		// TODO Auto-generated method stub
		if(!song.getObjectId().equals(currentSong.getObjectId())){
			getPlayerEngine().playSong(song);
		}
	}

	/**
	 * 显示收藏
	 */
	private void showCollection(Song song) {
		// TODO Auto-generated method stub
		if(userManager.ifInCollection(song)){
			paly_likeButton.setBackgroundResource(R.drawable.mini_like_button_pressed);
			currentSongIfCol = true;
		}else{
			paly_likeButton.setBackgroundResource(R.drawable.mini_like_button_unpressed);
			currentSongIfCol = false;
		}
	}

	private void showCollection(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			paly_likeButton.setBackgroundResource(R.drawable.mini_like_button_pressed);
			currentSongIfCol = true;
		}else{
			paly_likeButton.setBackgroundResource(R.drawable.mini_like_button_unpressed);
			currentSongIfCol = false;
		}
	}

	/**
	 * 显示分类信息
	 */
	private void showTypeData() {
		// TODO Auto-generated method stub
		typeAdapter = new TypeAdapter(getActivity());
		tyWheelView.setViewAdapter(typeAdapter);
	}
	
	/**
	 * 显示歌曲信息
	 * @param song
	 * @param ifReload 是否重新显示数据
	 */
	private void showSongData(final Song song,boolean ifReload) {
		// TODO Auto-generated method stub
		if(ifReload){
			currentSong = song;
			songNameView.setText(song.getName());
			if(song.getSinger()!=null)
				singerView.setText("演唱："+song.getSinger().getName());
			showCollection(song);
			if(song.getAlbum()!=null)
				notifyAlbumInfo(song.getAlbum());
		}	
		Logger.i("PlayerActivity.getSongs-showSongData-showLrc");
		showLrc();	
	}

	/**
	 * 显示歌词
	 */
	private void showLrc() {
		// TODO Auto-generated method stub
		if(currentSong==null)return;
		if(currentSong.getLyric()!=null){
			if(lyricHandler!=null){
				lyricHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						SerchLrc(currentSong.getLyric());
					}
				});
			}			
		}else{
			LyricView.readParse(""); 
			mHandler.sendEmptyMessage(UPDATA_LRC);
		}	
	}
	
	public void SerchLrc(BmobFile lyric) {
		hasSearchLrc = true;
		if(lyric.getFileUrl(getActivity())!=null){
			LyricView.readParse(lyric.getFileUrl(getActivity())); 
			mHandler.sendEmptyMessage(UPDATA_LRC);
		}           
    }  
	
	private void loadPlaylist(Playlist playlist){
		if(playlist == null)
			return;		
		mPlaylist = playlist;
		if(getPlayerEngine().getPlaylist()==null){
			getPlayerEngine().openPlaylist(mPlaylist);
		}else{
			if(mPlaylist != getPlayerEngine().getPlaylist()){
				//getPlayerEngine().stop();
				getPlayerEngine().openPlaylist(mPlaylist);
				getPlayerEngine().play();
			}
		}
		
	}
	
	
	protected Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg == null) return;
			switch (msg.what) {
			case SEEK_MESSAGE:
				getPlayerEngine().seekTo(seek_position);
				mHandler.removeMessages(SEEK_MESSAGE);
				break;
			case UPDATA_LRC:
				lyricView.SetTextSize();
				lyricView.postInvalidate();
				break;
			case UPDATA_TYPE:
				int newValue = msg.arg1;
				updataSelectChannel(newValue);
				break;
			}
		};
	};
	
	private void doSeek(int progress) {
		// TODO Auto-generated method stub
		mHandler.removeMessages(SEEK_MESSAGE);		
		seek_position = progress;
		if (seek_position >= duration) seek_position = duration;
		mHandler.sendEmptyMessageDelayed(SEEK_MESSAGE, SEEK_DELAYED);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.song_pic_playbtn:
			doPlay();
			break;
		case R.id.main_playing_button_play:
			doPlay();
			break;
		case R.id.main_playing_button_next:
			getPlayerEngine().next();
			break;
		case R.id.main_playing_button_like:
			if(application.isLogin()){
				if(currentSongIfCol){
					mGetDataBusiness.getCollection(application.getUser(), currentSong, new ResCollectionHandler() {
						
						@Override
						public void onResponse(Collect result) {
							// TODO Auto-generated method stub
							mGetDataBusiness.delCollection(result, new DelHandler() {
								
								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									showToast("取消收藏成功");
									userManager.delOneCollection(currentSong);
									showCollection(false);
								}
								
								@Override
								public void onError(int errorCode, String msg) {
									// TODO Auto-generated method stub
									showToast("取消收藏失败:"+msg);
								}
							});
						}
					});
					
				}else{
					mGetDataBusiness.addCollection(application.getUser(), currentSong, new SaveHandler() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							showToast("收藏成功");
							userManager.addOneCollection(currentSong);
							showCollection(true);
						}
						
						@Override
						public void onError(int errorCode, String msg) {
							// TODO Auto-generated method stub
							showToast("收藏失败:"+msg);
						}
					});
				};
			}else{
				gotoActvity(LoginActivity.class);
			}
			break;
		}
	}

	@Override
	public void doShare() {
		if(currentSong!=null){
			String content = "";
			String title = "";
			if(currentSong.getSinger()!=null){
				title = currentSong.getName();
				content = currentSong.getSinger().getName()+"\n(奔跑吧小浪奔跑吧音乐)";
			}
			if(currentSong.getSong()!=null){
				if (!recommondWindow.isShowing()) {
					showShadow();
					recommendWxComponent.setMsg(title, content, currentSong.getSong().getFileUrl(mActivity),currentSong.getAlbum().getImage().getFileUrl(mActivity));
					recommondWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			}
		}
	}

	@Override
	public void doRefresh() {
		BmobQuery.clearAllCachedResults(mActivity);
//		rotationImageView();
//		getTypeData();
		mGetDataBusiness.setIfShow(false);
		getSinger();
		getQrcodeData();
		getPlayInfo();
	}
	
	/**
	 * 更新歌手信息
	 * @param singer
	 */
	public void notifySingerInfo(Singer singer) {
		// TODO Auto-generated method stub
		if(singer.getUserPic()!=null){
			if(StringUtils.isNotNull(singer.getUserPic().getFileUrl(getActivity()))){
//                    ImageUtil.setImg(mActivity,topLeftIv,singer.getUserPic().getFileUrl(getActivity()),R.drawable.userpic);
//					mActivity.setToolbarLeftImg(singer.getUserPic().getFileUrl(getActivity()));
			}
		}
		
	}
	
	/**
	 * 更新专辑信息
	 * @param album
	 */
	public void notifyAlbumInfo(Album album) {
		// TODO Auto-generated method stub
		if(album.getImage()!=null){
			mFetcher.loadImage(album.getImage().getFileUrl(getActivity()), picView);
		}
	}

	
	private void doPlay() {
		// TODO Auto-generated method stub
		if(getPlayerEngine().isPlaying()){
			getPlayerEngine().pause();
		} else {
			getPlayerEngine().play(); 
		}	
	}
	
	/**
	 * PlayerEngineListener implementation, manipulates UI
	 */
	private PlayerEngineListener mPlayerEngineListener = new PlayerEngineListener(){

		@Override
		public void onTrackChanged(PlaylistEntry playlistEntry) {
//			new LicenseTask(playlistEntry.getAlbum(), mCurrentAlbum);
			Logger.d("onTrackChanged");
			if(currentSong!=null){				
				showSongData(playlistEntry.getSong(),!currentSong.getObjectId().equals(playlistEntry.getSong().getObjectId()));
			}
			mCurrentAlbum = playlistEntry.getSong().getAlbum();
			time1View.setText(GetFormatTime(0).toString());
			time2View.setText(GetFormatTime(0).toString());
			seekBar.setProgress(0);
			seekBar.setSecondaryProgress(0);
			if(getPlayerEngine() != null){
				if(getPlayerEngine().isPlaying()){
					paly_PlayButton.setBackgroundResource(R.drawable.btnpause_bg);
				} else {
					paly_PlayButton.setBackgroundResource(R.drawable.btnpaly_bg);
				}
			}
		}

		@Override
		public void onTrackProgress(int seconds) {
			time1View.setText(GetFormatTime(seconds).toString());
			seekBar.setProgress(seconds);
		}

		@Override
		public void onTrackBuffering(int percent) {
			int secondaryProgress = (int) (((float)percent/100)*seekBar.getMax());
			seekBar.setSecondaryProgress(secondaryProgress);
		}

		@Override
		public void onTrackStop() {
			paly_PlayButton.setBackgroundResource(R.drawable.btnpaly_bg);
			picPlayView.setBackgroundResource(R.drawable.autio_identicy_play_bg);
			picView.clearAnimation();
		}

		@Override
		public boolean onTrackStart() {
			if (operatingAnim != null) {  
				picView.startAnimation(operatingAnim);  
			} 
			paly_PlayButton.setBackgroundResource(R.drawable.btnpause_bg);
			picPlayView.setBackgroundResource(R.drawable.new_pause_button_bg);
			return true;
		}

		@Override
		public void onTrackPause() {
			paly_PlayButton.setBackgroundResource(R.drawable.btnpaly_bg);
			picPlayView.setBackgroundResource(R.drawable.autio_identicy_play_bg);
			picView.clearAnimation();
		}

		@Override
		public void onTrackStreamError() {
//			Toast.makeText(PlayerActivity.this, R.string.stream_error, Toast.LENGTH_LONG).show();
			showToast("播放出错");
		}

		@Override
		public void onMaxProgress(int max) {
			// TODO Auto-generated method stub
			duration = max;
			time2View.setText(GetFormatTime(duration).toString());
			seekBar.setMax(max);		
		}

	};
	
	private static String GetFormatTime(int time) {
		SimpleDateFormat sim = new SimpleDateFormat("mm:ss");
		return sim.format(time);
	}


	class TypeAdapter extends AbstractWheelTextAdapter{
		
		public TypeAdapter(Context context) {
			// TODO Auto-generated constructor stub
			super(context, R.layout.type_item_layout, NO_RESOURCE);
		}
		@Override
		public int getItemsCount() {
			// TODO Auto-generated method stub
			return sTypes.size();
		}
		@Override
		protected CharSequence getItemText(int index) {
			// TODO Auto-generated method stub
			return sTypes.get(index).getObjectId();
		}
		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = super.getItem(index, convertView, parent);           
			TextView nameView = (TextView) view.findViewById(R.id.textView_type);
			nameView.setText(sTypes.get(index).getTypeName());
            return view;
		}
		
	}

	private PlayerEngine getPlayerEngine(){
		return MyApplication.getInstance().getPlayerEngineInterface();
	}
	
	private void controlLyric() {
		// TODO Auto-generated method stub
		if(lyricHandler!=null)
			lyricHandler.sendEmptyMessageDelayed(0, 200);
	}
	
	public void onPause() {
		super.onPause();
		Logger.d("PlayMainFragment--onPause");
		if(lyricHandlerThread!=null){
			lyricHandlerThread.quit();
			lyricHandlerThread = null;
			lyricHandler = null;
		}	
	};
	
	public void onDestroy() {
		isLive = false;
		MyApplication.getInstance().setPlayerEngineListener(null);
		super.onDestroy();
	};
   
    Runnable mUpdateResults = new Runnable() {  
        public void run() {  
            lyricView.invalidate(); // 更新视图  
        }  
    };  


	@Override
	public Fragment getFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUID() {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}

	private void rotationImageView() {
		// TODO Auto-generated method stub
		currentAngle = currentAngle+180;
//		topSxIv.animate().rotation(currentAngle);
	}
	
	private void getQrcodeData() {
		// TODO Auto-generated method stub
		mGetDataBusiness.getQrcode(new ResQrcodeHandler() {
			
			@Override
			public void onResponse(Qrcode result) {
				// TODO Auto-generated method stub
				String appUrl = result.url;
				application.setAppUrl(appUrl);
				qqUtil.setPageUrl(appUrl);
			}
		});
	}

	public void showShadow() {
		// TODO Auto-generated method stub
		shadowView.setVisibility(View.VISIBLE);
		Animation shadowInAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.shadow_in);
		shadowView.startAnimation(shadowInAnimation);
	}

	public void disShadow() {
		// TODO Auto-generated method stub
		Animation shadowInAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.shadow_out);
		shadowView.startAnimation(shadowInAnimation);

	}
}
