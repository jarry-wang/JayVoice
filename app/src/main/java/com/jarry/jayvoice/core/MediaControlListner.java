package com.jarry.jayvoice.core;

import com.baidu.cyberplayer.core.BVideoView;

public interface MediaControlListner {

	BVideoView getVideoView();
	
	int getCurrentPosition();
	
	int getDuration();
	
	boolean isPlaying();
	
	void pause();
	
	void resume();
	
	void seekTo(int position);
	
	void restart();
	
	void exit();
}
