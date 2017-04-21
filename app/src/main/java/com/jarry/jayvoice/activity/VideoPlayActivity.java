package com.jarry.jayvoice.activity;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.Vedio;
import com.jarry.jayvoice.core.GetDataBusiness.ResVedioHandler;
import com.jarry.jayvoice.core.MediaControl;
import com.jarry.jayvoice.core.MediaControlListner;
import com.jarry.jayvoice.media.PlayerEngine;
import com.jarry.jayvoice.util.StringUtils;

import a.b.c.DynamicSdkManager;
import a.b.c.listener.IVideoAdListener;
import a.b.c.listener.IVideoAdRequestListener;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class VideoPlayActivity extends BaseActivity implements OnPreparedListener, OnCompletionListener,
        OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener
{

    private final String TAG = "VideoPlayActivity";

    /**
     * 您的ak
     */
    private String AK = "Q5G1K9b0q8Ngv6SsA5VrRzICmf65RcTF";
    //您的sk的前16位
    private String SK = "0PnusRXya2zSiw5U";
    Context mContext;
    TextView mTitle;
    private String mVideoSource = null;
    MediaControl mediaControl;

    private final int EVENT_PLAY = 0;

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;

    /**
     * 播放状态
     */
    private  enum PLAYER_STATUS {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
    }

    private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

    private BVideoView mVV = null;

    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    private final Object SYNC_Playing = new Object();

    private WakeLock mWakeLock = null;
    private static final String POWER_LOCK = "VideoPlayActivity";

    private boolean mIsHwDecode = false;
    private ImageButton fullScreenIb;
    private Vedio mVedio;
    int videoTypeId;
    boolean ifPlayAd;


    private PlayerEngine getPlayerEngine(){
        return MyApplication.getInstance().getPlayerEngineInterface();
    };
    private void doPlay() {
        // TODO Auto-generated method stub
        if(getPlayerEngine().isPlaying()){
            getPlayerEngine().pause();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("VideoPlayActivity","onCreate");
        super.onCreate(savedInstanceState);
        mContext = this;
        mediaControl = new MediaControl(this,mediaControlListner);
        mVedio = (Vedio) getIntent().getSerializableExtra("video");
        mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
        DynamicSdkManager.getInstance(this).initNormalAd();
        // 请求视频广告
        if(mVedio!=null&&mVedio.typeId == 5){
            doHasAdVideo(false);
        }else{
            Log.d("videoPlay","requestVideoAd-start");
            DynamicSdkManager.getInstance(this).requestVideoAd(new IVideoAdRequestListener() {

                @Override
                public void onRequestSucceed() {
                    Log.e("videoPlay", "request success");
                    if(!ifPlayAd)
                        doHasAdVideo(true);
                }

                @Override
                public void onRequestFail(int errorCode) {
                    // 关于错误码的解读：-1为网络连接失败，请检查网络。-2007为无广告，-3312为该设备一天的播放次数已完。
                    Log.e("videoPlay", "request fail,errorCode is " + errorCode);
                    doHasAdVideo(false);
                }
            });
        }

    }

    private void doHasAdVideo(final boolean hasAd) {
        // TODO Auto-generated method stub
        if(mVedio!=null){
            initVideoInfo(mVedio);
            if(hasAd){
                showAdVideo();
            }else{
                doVideo();
                if(mEventHandler!=null)
                    mEventHandler.sendEmptyMessage(EVENT_PLAY);
            }
        }else{
            String videoId = getIntent().getStringExtra("id");
            if(StringUtils.isNotNull(videoId)){
                getDataBusiness.getVideoInfo(videoId, new ResVedioHandler() {

                    @Override
                    public void onResponse(Vedio result) {
                        // TODO Auto-generated method stub
                        mVedio = result;
                        initVideoInfo(mVedio);
                        if(hasAd){
                            showAdVideo();
                        }else{
                            doVideo();
                            if(mEventHandler!=null)
                                mEventHandler.sendEmptyMessage(EVENT_PLAY);
                        }
                    }
                });
            }else{
                goNullResource();
            }
            return;
        }
    }

    private void showAdVideo() {
        // TODO Auto-generated method stub
        Log.d("videoPlay", "showAdVideo--in");
        DynamicSdkManager.getInstance(this).showVideo(this, new IVideoAdListener() {

            @Override
            public void onVideoPlayInterrupt() {
                Log.e("videoPlay", "interrupt");
                ifPlayAd = true;
                VideoPlayActivity.this.finish();
            }

            @Override
            public void onVideoPlayFail() {
                Log.e("videoPlay", "failed");
                ifPlayAd = true;
                VideoPlayActivity.this.finish();
            }

            @Override
            public void onVideoPlayComplete() {
                Log.e("videoPlay", "complete");
                ifPlayAd = true;
                doHasAdVideo(false);
            }

            @Override
            public void onVideoCallback(boolean callback) {
                Log.e("videoPlay", "completeEffect:" + callback);
                ifPlayAd = true;
            }
        });
    }

    private void initVideoInfo(Vedio mVedio) {
        // TODO Auto-generated method stub
        videoTypeId = getIntent().getIntExtra("typeId", 1);
        mediaControl.setTitleName(mVedio.name);
        if(mVedio.file!=null){
            mVideoSource = mVedio.file.getFileUrl(this);
        }else{
            mVideoSource = mVedio.fileurl;
        }
//		switchOrientation(true);
        fullScreenIb.setVisibility(View.GONE);
    }

    private void doVideo() {
        // TODO Auto-generated method stub
        if(StringUtils.isNull(mVideoSource)){
            goNullResource();
            return;
        }
//		if(mVideoSource.endsWith(".mp4")||mVideoSource.endsWith(".MP4")||mVideoSource.endsWith(".3gp")||mVideoSource.endsWith(".3GP")){
//			mIsHwDecode = true;
//		}
        initVideoview();
        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread",
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());
        mediaControl.initSeekLoading();
        doPlay();
    }

    /**
     * 初始化界面
     */
    private void initVideoview() {

        /**
         * 设置ak及sk的前16位
         */
        BVideoView.setAKSK(AK, SK);

        /**
         *获取BVideoView对象
         */
        mVV = (BVideoView) findViewById(R.id.video_view);

        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);
//		mVV.setOnPlayingBufferCacheListener(this);
        Log.d(TAG,"mIsHwDecode="+mIsHwDecode);
        /**
         * 设置解码模式
         */
        mVV.setDecodeMode(mIsHwDecode?BVideoView.DECODE_HW:BVideoView.DECODE_SW);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
            mLastPos = mVV.getCurrentPosition();
            mVV.stopPlayback();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.v(TAG, "onResume");
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }
        /**
         * 发起一次播放任务,当然您不一定要在这发起
         */
        if(mEventHandler!=null)
            mEventHandler.sendEmptyMessage(EVENT_PLAY);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }


    private long mTouchTime;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            mTouchTime = System.currentTimeMillis();
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            long time = System.currentTimeMillis() - mTouchTime;
            if (time < 400) {
//				updateControlBar(!barShow);
                mediaControl.updateTouch();
            }
        }

        return true;
    }



    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        /**
         * 退出后台事件处理线程
         */
        if(mHandlerThread!=null){
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    @Override
    public boolean onInfo(int what, int extra) {
        // TODO Auto-generated method stub
        switch(what){
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     */
    @Override
    public void onPlayingBufferCache(int percent) {
        // TODO Auto-generated method stub
//		percentView.setText("缓冲"+percent+"%");
    }

    /**
     * 播放出错
     */
    @Override
    public boolean onError(int what, int extra) {
        // TODO Auto-generated method stub
        Log.v(TAG, "onError");
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        mediaControl.stopUpdateCupos();
        return true;
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onCompletion");
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        mediaControl.stopUpdateCupos();
        mediaControl.showReStart();
//		this.finish();
    }

    /**
     * 准备播放就绪
     */
    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onPrepared");
        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
//		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
//		mUIHandler.sendEmptyMessage(FADE_IN);
        mediaControl.updateCupos();
        mediaControl.showControlBar();
        mediaControl.onLoaded();
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
        fullScreenIb = (ImageButton) findViewById(R.id.fullscreen);
        fullScreenIb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void showData() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_controllerplaying;
    }

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_PLAY:
                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */
                    if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
                        synchronized (SYNC_Playing) {
                            try {
                                SYNC_Playing.wait();
                                Log.v(TAG, "wait player status to idle");
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.v(TAG, "mVideoSource="+mVideoSource);
                    /**
                     * 设置播放url
                     */
                    mVV.setVideoPath(mVideoSource);

                    /**
                     * 续播，如果需要如此
                     */
                    if (mLastPos > 0) {

                        mVV.seekTo(mLastPos);
                        mLastPos = 0;
                    }

                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    mVV.showCacheInfo(true);

                    /**
                     * 开始播放
                     */
                    mVV.start();

                    mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
                    mediaControl.onLoading();
                    break;
                default:
                    break;
            }
        }
    }


    MediaControlListner mediaControlListner = new MediaControlListner() {

        @Override
        public void seekTo(int second) {
            // TODO Auto-generated method stub
            mVV.seekTo(second);
        }

        @Override
        public void resume() {
            // TODO Auto-generated method stub
            mVV.resume();
        }

        @Override
        public void pause() {
            // TODO Auto-generated method stub
            mVV.pause();
            DynamicSdkManager.getInstance(mContext).showSpot(mContext);
        }

        @Override
        public boolean isPlaying() {
            // TODO Auto-generated method stub
            return mVV.isPlaying();
        }

        @Override
        public BVideoView getVideoView() {
            // TODO Auto-generated method stub
            return mVV;
        }

        @Override
        public int getDuration() {
            // TODO Auto-generated method stub
            return mVV.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            // TODO Auto-generated method stub
            return mVV.getCurrentPosition();
        }

        @Override
        public void restart() {
            // TODO Auto-generated method stub
            doVideo();
            if(mEventHandler!=null)
                mEventHandler.sendEmptyMessage(EVENT_PLAY);
        }

        @Override
        public void exit() {
            // TODO Auto-generated method stub
            VideoPlayActivity.this.finish();
        }
    };

    private void goNullResource() {
        // TODO Auto-generated method stub
        showToast("未找到资源");
        this.finish();
    }

}
