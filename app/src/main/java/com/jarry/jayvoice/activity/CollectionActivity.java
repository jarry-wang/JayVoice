package com.jarry.jayvoice.activity;

import java.util.ArrayList;
import java.util.List;

import com.jarry.jayvoice.activity.main.MainActivity;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.Collect;
import com.jarry.jayvoice.bean.Song;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.core.GetDataBusiness.DelHandler;
import com.jarry.jayvoice.core.GetDataBusiness.ResCollectionHandler;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.interfac.IfdoInterface;
import com.jarry.jayvoice.util.ListUtil;
import com.jarry.jayvoice.util.StringUtils;
import com.jarry.jayvoice.util.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionActivity extends BaseActivity{

	private ImageView headView;
	private TextView nameView;
	private User user;
	private ListView mListView;
	private List<Song> collectionSongs;
	private MyAdapter myAdapter;
	private UserManager userManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userManager = UserManager.getInstance(getApplicationContext());
		getData();
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.my_collection));
		headView = (ImageView) findViewById(R.id.collec_headimg);
		nameView = (TextView) findViewById(R.id.collec_nameview);
		mListView = (ListView) findViewById(R.id.collec_listview);
		collectionSongs = new ArrayList<Song>();
		myAdapter = new MyAdapter();
		mListView.setAdapter(myAdapter);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		user = userManager.getUser();		
		showUserData();
		if(ListUtil.isNotNull(application.getCollectionSongs())){
			collectionSongs = application.getCollectionSongs();
			showData();
		}		
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		if(ListUtil.isNotNull(collectionSongs)){
			myAdapter.notifyDataSetChanged();
			Utility.setListViewHeightBasedOnChildren(mListView,this);
		}
	}
	
	private void showUserData() {
		// TODO Auto-generated method stub
		if(user!=null){
			mFetcher.loadImage(user.imgUrl, headView);
			nameView.setText(user.nickName);
		}
	}
	
	private void delRefeshListview(int position) {
		// TODO Auto-generated method stub
		collectionSongs.remove(position);
		myAdapter.notifyDataSetChanged();
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_collection;
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return collectionSongs.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return collectionSongs.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.song_list_item, null);
			}
			TextView numView = (TextView) convertView.findViewById(R.id.song_listitem_num);
			TextView nameView = (TextView) convertView.findViewById(R.id.song_listitem_name);
			TextView infoView = (TextView) convertView.findViewById(R.id.song_listitem_info);
			final ImageView rightView = (ImageView) convertView.findViewById(R.id.song_listitem_right);
			final View controlView = convertView.findViewById(R.id.song_listitem_controlview);
			View delView = convertView.findViewById(R.id.song_listitem_delview);
			final Song song = (Song) getItem(position);
			numView.setText(StringUtils.getNum(position+1));
			nameView.setText(song.getName());
			infoView.setText(song.getSinger().getName()+"·"+song.getAlbum().getName());
			
			OnClickListener myClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					switch (view.getId()) {
					case R.id.song_listitem_right:
						if(controlView.getVisibility() == View.VISIBLE){
							controlView.setVisibility(View.GONE);							
							rightView.setImageResource(R.drawable.ic_todown);
						}else{
							controlView.setVisibility(View.VISIBLE);			
							rightView.setImageResource(R.drawable.ic_toup);
						}
						break;

					case R.id.song_listitem_delview:
						showIfdoDialogCb("确定删除该收藏吗", new IfdoInterface() {
							
							@Override
							public void doConfirm() {
								// TODO Auto-generated method stub
								dismissIfdoDialog();
								getDataBusiness.getCollection(application.getUser(), song, new ResCollectionHandler() {
									
									@Override
									public void onResponse(Collect result) {
										// TODO Auto-generated method stub
										getDataBusiness.delCollection(result, new DelHandler() {
											
											@Override
											public void onSuccess() {
												// TODO Auto-generated method stub
												delRefeshListview(position);
												userManager.delOneCollection(song);
												application.setIfCollectChange(true);
											}
											
											@Override
											public void onError(int errorCode, String msg) {
												// TODO Auto-generated method stub
												showToast("删除收藏失败:"+msg);
											}
										});
									}
								});
							}
							
							@Override
							public void doCancel() {
								// TODO Auto-generated method stub
								dismissIfdoDialog();
							}
						});
						break;
					}
				}
			};
			rightView.setOnClickListener(myClickListener);
			delView.setOnClickListener(myClickListener);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					gotoSelectSong(song);					
				}
			});
			return convertView;
		}
		
	}
	
	private void gotoSelectSong(Song song) {
		// TODO Auto-generated method stub
		application.setIfPlayChange(true);
		Intent intent = new Intent(this,MainActivity.class);
		intent.putExtra("song", song);
		startActivity(intent);
		this.finish();
	}
	
	
	private void rotationImageView(View view,boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			view.animate().rotation(180);		
		}else{
			view.animate().rotation(-180);		
		}
	}
}
