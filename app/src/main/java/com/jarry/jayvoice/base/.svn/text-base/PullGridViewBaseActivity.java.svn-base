package com.jarry.jayvoice.base;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.BaseActivity;
import com.jarry.jayvoice.widget.PullToRefreshLayout;
import com.jarry.jayvoice.widget.PullToRefreshLayout.OnRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;


public abstract class PullGridViewBaseActivity extends BaseActivity{
	
	public PullToRefreshLayout refreshLayout;
	public GridView mGridView;
	//分页公共参数
    public int size;
    public int page;
    public int currentPage = 1;
    public int offset;
    public int pagesize = 10;
    Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		refreshLayout = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		refreshLayout.setOnRefreshListener(new MyListener());
	}
	
	public void getPage() {
		page = size%pagesize==0?size/pagesize:size/pagesize+1;
		if(currentPage<page){
			refreshLayout.setPullLoadEnable(true);
		}else{
			refreshLayout.setPullLoadEnable(false);
		}
    }
    
    public void stopRefresh(final PullToRefreshLayout pullToRefreshLayout){
    	mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoad(pullToRefreshLayout);
			}
		}, 200);
    }
    
    public void stopLoadMore() {
		// TODO Auto-generated method stub
    	new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// 千万别忘了告诉控件加载完毕了哦！
				refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 200);
	}
    
	public void refreshData() {
		// TODO Auto-generated method stub
		currentPage = 1;
		offset = 0;
		size = 0;
		page = 0;
		refeshData();
	}
	
	public void onLoad(PullToRefreshLayout pullToRefreshLayout) {
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	}
	
	public abstract void refeshData();
	
	public void getData(int offset) {
		// TODO Auto-generated method stub
		
	}
	class MyListener implements OnRefreshListener
	{

		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 下拉刷新操作
//			mDataBusiness.setIfShow(false);
			refreshData();
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 加载操作
//			if(currentPage<=page){
//				mDataBusiness.setIfShow(false);
//				offset = (currentPage)*pagesize;
//				getData(offset);
//			}
			currentPage++;
		}

	}
}
