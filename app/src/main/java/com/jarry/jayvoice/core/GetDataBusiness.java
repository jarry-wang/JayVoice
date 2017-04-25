package com.jarry.jayvoice.core;

import java.util.List;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.About;
import com.jarry.jayvoice.bean.Album;
import com.jarry.jayvoice.bean.Apk;
import com.jarry.jayvoice.bean.BaseBean;
import com.jarry.jayvoice.bean.Collect;
import com.jarry.jayvoice.bean.FeedBack;
import com.jarry.jayvoice.bean.Photo;
import com.jarry.jayvoice.bean.Qrcode;
import com.jarry.jayvoice.bean.Recommend;
import com.jarry.jayvoice.bean.SType;
import com.jarry.jayvoice.bean.Singer;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.bean.Vedio;

import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.util.Logger;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.join.android.net.appupdate.AppVersionInfo;
import cn.join.android.net.appupdate.VersionUpdateHelper;
import cn.join.android.util.ToastUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.widget.TextView;

public class GetDataBusiness {

	Context context;
//	String[] jayAlbums = {"哎哟不错哦","十二新作","惊叹号!","跨时代"
//						,"魔杰座","我很忙","依然范特西","十一月的肖邦"
//						,"七里香","叶惠美","八度空间","范特西","Jay"
//						};
//	String[] years = {"2014","2012","2011","2010","2008","2007","2006","2005","2004","2003","2002","2001","2000"};
	/**
	 *  IGNORE_CACHE :只从网络获取数据，且不会将数据缓存在本地，这是默认的缓存策略。

		•CACHE_ONLY :只从缓存读取数据，如果缓存没有数据会导致一个BmobException,可以忽略不处理这个BmobException.
				
		•NETWORK_ONLY :只从网络获取数据，同时会在本地缓存数据。
			
		•NETWORK_ELSE_CACHE:先从网络读取数据，如果没有，再从缓存中获取。		
		
		•CACHE_ELSE_NETWORK:先从缓存读取数据，如果没有，再从网络获取。
				
		•CACHE_THEN_NETWORK:先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。

	 */
	private Dialog loadingDialog;
	private boolean ifShow = true;
	private ToastUtil toastUtil;

	/**
	 * 构造函数
	 * 
	 * @author
	 */
	public GetDataBusiness(Context context) {
		this.context = context;
		toastUtil = new ToastUtil(context);
		initDialog();
	}

	private void initDialog(){
		setStyleTextNormal();
	}

	public void setStyleTextNormal(){
		setStyleText(context.getString(R.string.loading_info));
	}
	
	public void setStyleText(String text) {
		// TODO Auto-generated method stub
		if(loadingDialog!=null)loadingDialog = null;
		loadingDialog = new Dialog(context, R.style.LoadDialogText);
		loadingDialog.setContentView(R.layout.loading_dialog_text);
		loadingDialog.setOnKeyListener(onDialogKeyListener);
		loadingDialog.setCancelable(true);
		TextView textView = (TextView) loadingDialog.findViewById(R.id.loading_text);
		textView.setText(text);
	}

	
	public void showLoading() {
		// TODO Auto-generated method stub
		if(ifShow){
			if (!((Activity) context).isFinishing())loadingDialog.show();
		}
			
	}
	
	public void hideLoading() {
		// TODO Auto-generated method stub
		if(loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
	
	public void setIfShow(boolean ifShow) {
		this.ifShow = ifShow;
	}
	
	public OnKeyListener onDialogKeyListener = new OnKeyListener() {
		@Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            	if(loadingDialog.isShowing())
					loadingDialog.dismiss();
            }
            return false;
        }
    };
	
	public interface ResTypeHandler {
		void onResponse(List<SType> result);
	}
	public interface ResSongListHandler {
		void onResponse(List<Song> result);
	}
	public interface ResAlbumListHandler {
		void onResponse(List<Album> result);
	}
	public interface ResPhotoListHandler {
		void onResponse(List<Photo> result);
	}
	public interface ResUserHandler {
		void onResponse(User result);
		void onError();
	}
	public interface ResCollectionListHandler {
		void onResponse(List<Collect> result);
	}
	public interface ResVideoListHandler {
		void onResponse(List<Vedio> result);
		void onError(int errorCode,String msg);
	}
	public interface ResVedioHandler {
		void onResponse(Vedio result);
	}
	public interface ResRecommendListHandler {
		void onResponse(List<Recommend> result);
	}
	public interface ResCollectionHandler {
		void onResponse(Collect result);
	}
	public interface ResQrcodeHandler {
		void onResponse(Qrcode result);
	}
	
	public interface ResAboutHandler {
		void onResponse(About result);
	}
	
	public interface SaveHandler {
		void onSuccess();
		void onError(int errorCode,String msg);
	}
	
	public interface UpdateHandler {
		void onSuccess();
		void onError(int errorCode,String msg);
	}

	public interface CommonBeanRedHandler {
		void onResponse(BaseBean result);
	}
	
	/**
	 * 获取分类
	 */
	public void getTypeList(final ResTypeHandler handler){
		showLoading();		
		BmobQuery<SType> query = new BmobQuery<SType>();
		query.order("num");
		boolean isCache = query.hasCachedResult(SType.class);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}
		query.findObjects(new FindListener<SType>() {

			@Override
			public void done(List<SType> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}

		});
	}
	
	/**
	 * 获取歌曲
	 */
	public void getSongList(final ResSongListHandler handler){
		showLoading();
		BmobQuery<Song> songQuery = new BmobQuery<Song>();
		songQuery.include("singer.name,album");
		songQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		songQuery.findObjects(new FindListener<Song>() {
			@Override
			public void done(List<Song> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}

		});	
	}

	/**
	 * 获取歌手信息
	 */
	public void getSingerInfo(final CommonBeanRedHandler handler) {
		BmobQuery<Singer> query2 = new BmobQuery<>();
		query2.getObject(Config.jay_id, new QueryListener<Singer>() {

			@Override
			public void done(Singer singer, BmobException e) {
				if (e == null) {
					MyApplication.getInstance().setSinger(singer);
					handler.onResponse(singer);
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}

		});
	}

	/**
	 * 通过类型获取歌曲
	 */
	public void getSongList(final ResSongListHandler handler,SType sType,boolean ifReload){
		showLoading();
		BmobQuery<Song> songQuery = new BmobQuery<Song>();			
		songQuery.include("singer.name,album,sType");
		songQuery.addWhereEqualTo("sType", sType);
		if(ifReload){
			songQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		}else{
			boolean isCache = songQuery.hasCachedResult(Song.class);
			if(isCache){
				songQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
			}else{
				songQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
			}
		}
		songQuery.findObjects(new FindListener<Song>() {
			@Override
			public void done(List<Song> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}

		});	
	}
	
	/**
	 * 通过专辑获取歌曲
	 */
	public void getSongListFromAlbum(final ResSongListHandler handler,Album album){
		showLoading();
		BmobQuery<Song> songQuery = new BmobQuery<>();
		songQuery.include("singer.name,album,sType");
		songQuery.addWhereEqualTo("album", album);
		songQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		songQuery.findObjects(new FindListener<Song>() {
			@Override
			public void done(List<Song> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}
		});	
	}

	/**
	 * 获取专辑
	 */
	public void getAlbumList(final ResAlbumListHandler handler){
		showLoading();
		BmobQuery<Album> albumQuery = new BmobQuery<>();
		albumQuery.order("-sort");
		albumQuery.addWhereEqualTo("singer", MyApplication.getInstance().getSinger());
		albumQuery.include("singer");
		albumQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		albumQuery.findObjects(new FindListener<Album>() {
			@Override
			public void done(List<Album> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}
		});	
	}
	
	/**
	 * 获取图片
	 */
	public void getPhotoList(final ResPhotoListHandler handler){
		showLoading();
		BmobQuery<Photo> photoQuery = new BmobQuery<>();
		photoQuery.order("-createdAt");
		boolean isCache = photoQuery.hasCachedResult(Photo.class);
		if(isCache){
			photoQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
		}else{
			photoQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
		}
		photoQuery.findObjects( new FindListener<Photo>() {
			@Override
			public void done(List<Photo> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}
		});	
	}
	
	/**
	 * 获取视频
	 * orderType 0:按最新  1：按最热
	 */
	public void getVideoList(int typeId,int orderType,final ResVideoListHandler handler){
		showLoading();
		BmobQuery<Vedio> videoQuery = new BmobQuery<>();
		boolean isCache = videoQuery.hasCachedResult(Vedio.class);
		if(isCache){
			videoQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
		}else{
			videoQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
		}
		if(orderType==1){
			videoQuery.order("-time");
		}else{
			videoQuery.order("-playnum");
		}
		videoQuery.addWhereEqualTo("typeId", typeId);
		videoQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		videoQuery.findObjects(new FindListener<Vedio>() {
			@Override
			public void done(List<Vedio> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
				}else {
					handler.onError(e.getErrorCode(),e.getMessage());
				}
				hideLoading();
			}
		});	
	}
	
	public void getVideoInfo(String id , final ResVedioHandler resVedioHandler) {
		// TODO Auto-generated method stub
		showLoading();
		BmobQuery<Vedio> query = new BmobQuery<Vedio>();
		query.getObject(id, new QueryListener<Vedio>() {

			@Override
			public void done(Vedio vedio, BmobException e) {
				if (e == null) {
					resVedioHandler.onResponse(vedio);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}

		});
	}
	
	/**
	 * 获取视频推荐
	 * orderType 0:按最新  1：按最热
	 */
	public void getRecommendList(final ResRecommendListHandler handler){
		showLoading();
		BmobQuery<Recommend> activityQuery = new BmobQuery<Recommend>();		
		activityQuery.include("video");
		activityQuery.order("num");
		activityQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	
		activityQuery.findObjects(new FindListener<Recommend>() {
			@Override
			public void done(List<Recommend> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}
		});	
	}
	
	/**
	 * 获取二维码
	 */
	public void getQrcode(final ResQrcodeHandler handler){
		showLoading();
		BmobQuery<Qrcode> qrcQuery = new BmobQuery<Qrcode>();
		qrcQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		qrcQuery.findObjects(new FindListener<Qrcode>() {
			@Override
			public void done(List<Qrcode> list, BmobException e) {
				if (e == null) {
					if(ListUtil.isNotNull(list)){
						handler.onResponse(list.get(0));
					}
					hideLoading();
				}else {
					if(e.getErrorCode() == 9009){
						Qrcode qrcode = new Qrcode();
						qrcode.url = Config.APP_URL_XIAOMI;
						handler.onResponse(qrcode);
						hideLoading();
					}else{
						doError(e.getErrorCode(),e.getMessage());
					}
				}
			}
		});	
	}
	
	/**
	 * 获取关于
	 */
	public void getAbout(final ResAboutHandler handler){
		showLoading();
		BmobQuery<About> aboutQuery = new BmobQuery<About>();
		aboutQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		aboutQuery.findObjects(new FindListener<About>() {
			@Override
			public void done(List<About> list, BmobException e) {
				if (e == null) {
					if(ListUtil.isNotNull(list)){
						handler.onResponse(list.get(0));
					}
					hideLoading();
				}else {
					doError(e.getErrorCode(),e.getMessage());
				}
			}
		});	
	}

	/**
	 * 获取app信息
	 */
	public void getApkInfo(final VersionUpdateHelper.GetAppVersionCallback callback){
		BmobQuery<Apk> apkBmobQuery = new BmobQuery<>();
		apkBmobQuery.setCachePolicy(CachePolicy.NETWORK_ONLY);
		Logger.d("获取app信息");
		apkBmobQuery.findObjects(new FindListener<Apk>() {
			@Override
			public void done(List<Apk> list, BmobException e) {
				if (e == null) {
					Logger.d("获取app信息:"+list.size());
					if(ListUtil.isNotNull(list)){
						Apk apkInfo = list.get(0);
						AppVersionInfo appVersionInfo = new AppVersionInfo();
						appVersionInfo.versionName = apkInfo.versionName;
						appVersionInfo.versionCode = apkInfo.versionCode;
						appVersionInfo.versionInfo = apkInfo.versionInfo;
						appVersionInfo.filePath = apkInfo.apkUrl;
						appVersionInfo.ifForceUpdate = apkInfo.ifForceUpdate;
						Logger.d("获取app信息:"+appVersionInfo);
						callback.onsuccess(appVersionInfo);
					}
				}else {
					doError(e.getErrorCode(),e.getMessage());
					callback.onError();
				}
			}
		});
	}
	
	/**
	 * 获取歌曲
	 */
	public void getSongList(){
		
	}
	/**
	 * 获取歌手信息
	 */
	public void getSingerList(){
		
	}
	
	/**
	 * 添加用户
	 */
	public void addUser(User user,SaveHandler saveHandler){		
		user.save(new MySaveListener(saveHandler));
	}
	
	/**
	 * 注册用户
	 */
	public void signUpUser(User user,final SaveHandler saveHandler) {
		// TODO Auto-generated method stub
		showLoading();
		user.signUp(new SaveListener<User>() {

			@Override
			public void done(User o, BmobException e) {
				if (e == null) {
					saveHandler.onSuccess();
				}else {
					saveHandler.onError(e.getErrorCode(), e.getMessage());
				}
				hideLoading();
			}
		});
	}
	
	/**
	 * 登录
	 * @param user
	 * @param saveHandler
	 */
	public void doLogin(User user,final SaveHandler saveHandler) {
		// TODO Auto-generated method stub
		showLoading();
		user.login(new SaveListener<User>() {
			@Override
			public void done(User o, BmobException e) {
				if (e == null) {
					saveHandler.onSuccess();
					hideLoading();
				}else {
					doError(e.getErrorCode(),"登录失败:"+e.getMessage());
				}
			}
		});
	}
	
	/**
	 * 获取用户信息
	 * @param username
	 * @param handler
	 */
	public void getUser(String username,final ResUserHandler handler) {
		// TODO Auto-generated method stub
		showLoading();
		BmobQuery<User> query = new BmobQuery<User>();	
		query.addWhereEqualTo("username", username);
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query.findObjects(new FindListener<User>() {

			@Override
			public void done(List<User> list, BmobException e) {
				if (e == null) {
					if(list.size()>0){
						handler.onResponse(list.get(0));
					}
					hideLoading();
				}else {
					handler.onError();
					hideLoading();
				}
			}
		});
	}
	
	/**
	 * 查询用户收藏的歌曲
	 */
	public void searchCollection(User user,final ResCollectionListHandler handler) {
		// TODO Auto-generated method stub
		showLoading();
		BmobQuery<Collect> query = new BmobQuery<Collect>();		
		query.addWhereEqualTo("userId", user.getObjectId());
		query.include("song,song.singer,song.album,song.sType");
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query.findObjects(new FindListener<Collect>() {

			@Override
			public void done(List<Collect> list, BmobException e) {
				if (e == null) {
					handler.onResponse(list);;
					hideLoading();
				}else {
					doError(e.getErrorCode(), e.getMessage());
				}
			}

		});
	}
	
	/**
	 * 获取收藏信息
	 */
	public void getCollection(User user,Song song,final ResCollectionHandler handler) {
		// TODO Auto-generated method stub
		showLoading();
		BmobQuery<Collect> query = new BmobQuery<Collect>();	
		query.addWhereEqualTo("userId", user.getObjectId());
		query.addWhereEqualTo("song", song);
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		query.findObjects(new FindListener<Collect>() {

			@Override
			public void done(List<Collect> list, BmobException e) {
				if (e == null) {
					if (list.size() > 0)
						handler.onResponse(list.get(0));
					hideLoading();
				}else {
					doError(e.getErrorCode(), e.getMessage());
				}
			}

		});
	}
	
	/**
	 * 添加收藏
	 */
	public void addCollection(User user,Song song,SaveHandler saveHandler){
		showLoading();
		Collect collection = new Collect();
		collection.userId = user.getObjectId();
		collection.song = song;
		collection.save(new MySaveListener(saveHandler));
	}
	
	/**
	 * 取消收藏
	 * @author mlfdev1
	 *
	 */
	public void delCollection(Collect collection,UpdateHandler delHandler){
		showLoading();
		collection.delete(new MyUpdateListener(delHandler));
	}
	
	/**
	 * 添加回馈
	 * @param info
	 * @param content
	 * @param saveHandler
	 */
	public void addFeedBack(String info,String content,SaveHandler saveHandler) {
		// TODO Auto-generated method stub
		FeedBack feedBack = new FeedBack();
		feedBack.contact_info = info;
		feedBack.feedback_content = content;

		feedBack.save(new MySaveListener(saveHandler));
	}
	
	class MySaveListener extends SaveListener{
		SaveHandler saveHandler;
		public MySaveListener(SaveHandler saveHandler){
			this.saveHandler = saveHandler;
		}

		@Override
		public void done(Object o, BmobException e) {
			if (e == null) {
				saveHandler.onSuccess();
			}else {
				saveHandler.onError(e.getErrorCode(), e.getMessage());
			}
			hideLoading();
		}

	}
	
	class MyUpdateListener extends UpdateListener{
		UpdateHandler updateHandler;
		public MyUpdateListener(UpdateHandler delHandler){
			this.updateHandler = delHandler;
		}

		@Override
		public void done(BmobException e) {
			if (e == null) {
				updateHandler.onSuccess();
			}else {
				updateHandler.onError(e.getErrorCode(), e.getMessage());
			}
			hideLoading();
		}
	}
	
	private void doError(int code,String msg) {
		// TODO Auto-generated method stub
		if(code==9009){//no cache data
			hideLoading();
		}else{
			toastUtil.showToast(code+":"+msg);
			hideLoading();
		}
		
	}
}
