package com.jarry.jayvoice.core;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.VideoPlayActivity;
import com.jarry.jayvoice.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MediaControl implements OnClickListener{
	
	private final String TAG = "MediaControl";
	private TextView vedioTitleTv;
	private SeekBar mProgress = null;
	private View mController = null;
	private ImageButton mPlaybtn = null;
	private TextView mDuration = null;
	private TextView mCurrPostion = null;
	VideoPlayActivity mActivity;
	private int seekcount = 0;
	private View seekLoadingContainerView,retryView,retryControlView,overExitView;
	
	private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
	private static final int FADE_OUT = 2;
	private static final int FADE_IN = 3;
	private static final int SHOW_RETRY = 4;
	private static final int HIDE_RETRY = 5;
	private static final int sDefaultTimeout = 5000;
	private boolean barShow = true;
	MediaControlListner mMediaControlListner;
	boolean ifPause;
	
	public MediaControl(Context context,MediaControlListner mediaControlListner){
		mActivity = (VideoPlayActivity) context;
		this.mMediaControlListner = mediaControlListner;
		vedioTitleTv = (TextView) mActivity.findViewById(R.id.video_title);
		mPlaybtn = (ImageButton)mActivity.findViewById(R.id.control_pause);	
		mController = mActivity.findViewById(R.id.controlbar);
		seekLoadingContainerView = mActivity.findViewById(R.id.seek_loading_bg);
		mProgress = (SeekBar)mActivity.findViewById(R.id.control_seek);
		mDuration = (TextView)mActivity.findViewById(R.id.time_total);
		mCurrPostion = (TextView)mActivity.findViewById(R.id.time_current);
		retryView = mActivity.findViewById(R.id.view_restart);
		retryControlView = mActivity.findViewById(R.id.go_retry);
		overExitView = mActivity.findViewById(R.id.go_exit);
		mPlaybtn.setOnClickListener(this);
		retryControlView.setOnClickListener(this);
		overExitView.setOnClickListener(this);
		registerCallbackForControl();
	}
	
	public void setTitleName(String text){
		if(StringUtils.isNotNull(text)){
			vedioTitleTv.setText(text);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.control_pause:
			if (mMediaControlListner.isPlaying()) {
				mPlaybtn.setImageResource(R.drawable.ic_media_play);
				/**
				 * 暂停播放
				 */
				mMediaControlListner.pause();
				ifPause = true;
				mUIHandler.removeMessages(FADE_OUT);
			} else {
				mPlaybtn.setImageResource(R.drawable.ic_media_pause);
				/**
				 * 继续播放
				 */
				mMediaControlListner.resume();	
				ifPause = false;
				hideControlDelay();
			}			
			break;
		case R.id.go_retry:
			mUIHandler.sendEmptyMessage(HIDE_RETRY);
			mMediaControlListner.restart();
			break;
		case R.id.go_exit:
			mMediaControlListner.exit();
			break;
		default:
			break;
		}
	}
	/**
	 * 为控件注册回调处理函数
	 */
	private void registerCallbackForControl(){
		
		OnSeekBarChangeListener osbc1 = new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				//Log.v(TAG, "progress: " + progress);
				updateTextViewWithTimeFormat(mCurrPostion, progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				/**
				 * SeekBar开始seek时停止更新
				 */
				mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int iseekPos = seekBar.getProgress();
				/**
				 * SeekBark完成seek时执行seekTo操作并更新界面
				 *
				 */
				mMediaControlListner.seekTo(iseekPos);
				Log.v(TAG, "seek to " + iseekPos);
				mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
			}
		};
		mProgress.setOnSeekBarChangeListener(osbc1);
	}
	
	public void stopUpdateCupos(){
		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
	}
	
	public void updateCupos() {
		// TODO Auto-generated method stub
		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
	}
	
	public void showControlBar() {
		// TODO Auto-generated method stub
		mUIHandler.sendEmptyMessage(FADE_IN);
	}
	
	public void hideControlBar() {
		// TODO Auto-generated method stub
		mUIHandler.sendEmptyMessage(FADE_OUT);
	}
	
	public void showReStart(){
		mUIHandler.sendEmptyMessage(SHOW_RETRY);
	}
	
	public void hideReStart(){
		mUIHandler.sendEmptyMessage(HIDE_RETRY);
	}
	
	public void updateTouch() {
		// TODO Auto-generated method stub
		updateControlBar(!barShow);
	}
		
	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 更新进度及时间
			 */
			case UI_EVENT_UPDATE_CURRPOSITION:
				int currPosition = mMediaControlListner.getCurrentPosition();
				int duration = mMediaControlListner.getDuration();
				updateTextViewWithTimeFormat(mCurrPostion, currPosition);
				updateTextViewWithTimeFormat(mDuration, duration);				
				mProgress.setMax(duration);				
				mProgress.setProgress(currPosition);
				
				mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
				break;
			case FADE_OUT:
				updateControlBar(false);
				break;
			case FADE_IN:
				updateControlBar(true);
				break;
			case SHOW_RETRY:
				retryView.setVisibility(View.VISIBLE);
				break;
			case HIDE_RETRY:
				retryView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};
	
	public void updateControlBar(boolean show) {

		if (show) {
			mController.setVisibility(View.VISIBLE);
			if(mMediaControlListner.isPlaying()){
				hideControlDelay();
			}			
		} else {
			mController.setVisibility(View.INVISIBLE);
		}
		barShow = show;
	}
	
	private void hideControlDelay() {
		// TODO Auto-generated method stub
		Message msg = mUIHandler.obtainMessage(FADE_OUT);
		mUIHandler.removeMessages(FADE_OUT);
		mUIHandler.sendMessageDelayed(msg, sDefaultTimeout);
	}
	
	private void updateTextViewWithTimeFormat(TextView view, int second){
		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;
		String strTemp = null;
		if (0 != hh) {
			strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
		} else {
			strTemp = String.format("%02d:%02d", mm, ss);
		}
		view.setText(strTemp);
	}
	
	
	public void showLoading() {
		if (null != seekLoadingContainerView) {
			if (seekLoadingContainerView.getVisibility() == View.GONE) {
				seekLoadingContainerView.setVisibility(View.VISIBLE);
				seekcount = 0;
				seekHandler.sendEmptyMessageDelayed(0, 50);

			}
			if (null != mMediaControlListner.getVideoView()
					&& mMediaControlListner.getCurrentPosition() > 1000) {
				seekendHandler.sendEmptyMessageDelayed(0, 50);
				seekLoadingContainerView.setBackgroundResource(0);
			} else {
				seekLoadingContainerView
						.setBackgroundResource(R.drawable.bg_play);
			}
		}
	}

	public void hideLoading() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (null != seekLoadingContainerView) {
					seekLoadingContainerView.setVisibility(View.GONE);
					playLoadingBar.setProgress(0);
				}
				if (null != seekHandler)
					seekHandler.removeCallbacksAndMessages(null);
			}
		});
	}

	private Handler seekHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (seekcount > 95) {
				seekcount = 0;
			}
			seekcount++;
			playLoadingBar.setProgress(seekcount);
			Thread temp = new Thread(new Runnable() {

				@Override
				public void run() {
					seekHandler.sendEmptyMessageDelayed(0, 50);
				}
			});
			temp.run();
		}

	};

	private SeekBar playLoadingBar;
	public void initSeekLoading() {
		if (null == seekLoadingContainerView)
			return;
		seekLoadingContainerView.setVisibility(View.GONE);
		playLoadingBar = (SeekBar) seekLoadingContainerView
				.findViewById(R.id.loading_seekbar);
		if (null != playLoadingBar)
			playLoadingBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							if (fromUser) {
								//Track.setTrackPlayLoading(false);
								return;
							} else {
								seekBar.setProgress(progress);
							}

						}
					});
	}
	
	private Handler seekendHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (seekcount < 100) {
				seekcount++;
				playLoadingBar.setProgress(seekcount);
				Thread temp = new Thread(new Runnable() {

					@Override
					public void run() {
						seekHandler.sendEmptyMessageDelayed(0, 10);
					}
				});
				temp.run();
			}

		}

	};
	
	public void onLoaded() {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideLoading();
			}
		});
	}

	public void onLoading() {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showLoading();
			}
		});
	}
}
