package com.jarry.jayvoice.core;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.jarry.jayvoice.bean.Album;
import com.jarry.jayvoice.bean.Photo;
import com.jarry.jayvoice.bean.SType;
import com.jarry.jayvoice.bean.Singer;
import com.jarry.jayvoice.bean.Song;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.join.android.util.ToastUtil;

import android.content.Context;

public class DataBusiness {
	Context context;
	String[] jayAlbums = {"哎哟不错哦","十二新作","惊叹号!","跨时代"
						,"魔杰座","我很忙","依然范特西","十一月的肖邦"
						,"七里香","叶惠美","八度空间","范特西","Jay"
						};
	String[] years = {"2014","2012","2011","2010","2008","2007","2006","2005","2004","2003","2002","2001","2000"};
	ToastUtil toastUtil;
	/**
	 * 构造函数
	 * 
	 * @author
	 */
	public DataBusiness(Context getcontext) {
		this.context = getcontext;
		toastUtil = new ToastUtil(context);
	}
	
	public interface ResponseHandlerFind {
		public void onResponse(List<TypeVariable<GenericDeclaration>> result);
	}
	
	public interface ResponseHandlerGet<T> {
		public void onResponse(T result);
	}
	
	/**
	 * 批量添加专辑
	 */
	public void addSomeAlbum(){
		List<BmobObject> albums = new ArrayList<BmobObject>();
		for (int i = 0; i < jayAlbums.length; i++) {
		    Album album = new Album();
		    album.setName(jayAlbums[i]);
		    album.setPublishTime(years[i]);
		    album.setArtistName("周杰伦");
		    album.setNum(10);
		    album.setRating(0.9);
		    albums.add(album);
		}
		new BmobBatch().insertBatch(albums).doBatch(new QueryListListener<BatchResult>() {
			@Override
			public void done(List<BatchResult> list, BmobException e) {
				if (e == null) {
					toastUtil.showToast("批量添加成功");
				}else {
					toastUtil.showToast("批量添加失败:"+e.getMessage());
				}
			}
		});
	}

	/**
	 * 添加单个专辑
	 */
	public void addAlbum(){
		Album album = new Album();
    	album.setName("啊哦哟不错哦");
    	album.setRating(0.9);
    	album.setArtistName("周杰伦");
    	album.save(new SaveListener() {

			@Override
			public void done(Object o, BmobException e) {
				if (e == null) {
					toastUtil.showToast("添加成功");
				}else {
					toastUtil.showToast("添加失败:"+e.getMessage());
				}
			}
    	});
	}
	
	/**
	 * 添加歌曲
	 */
	public void addSong(){
    	Song song = new Song();
    	song.setName("算什么男人");
    	song.setSinger(null);
	}
	
	/**
	 * 添加歌手
	 */
	public void addSinger(){
		Singer singer = new Singer();
    	singer.setName("");
    	singer.setUserPic(null);
    	singer.setGender("");
    	singer.setDescription("");
    	singer.setCountryType("");
	}
	
	/**
	 * 添加分类
	 */
	public void addSomeType(){
		List<BmobObject> sTypes = new ArrayList<BmobObject>();
    	String []strings = {"忧伤","欢快","励志","酷炫","中国风"};
    	for (int i = 0; i < strings.length; i++) {
    		SType sType = new SType();
    	    sType.setTypeName(strings[i]);
    	    sTypes.add(sType);
    	}
		new BmobBatch().insertBatch(sTypes).doBatch(new QueryListListener<BatchResult>() {
			@Override
			public void done(List<BatchResult> list, BmobException e) {
				if (e == null) {
					toastUtil.showToast("批量操作成功");
				}else {
					toastUtil.showToast("批量添加失败:"+e.getMessage());
				}
			}
		});
	}
	
	/**
	 * 添加用户
	 */
	/**
	 * 添加图片
	 */
	public void addSomePic(){
		List<BmobObject> sPics = new ArrayList<BmobObject>();
    	String []strings = {"http://img5.imgtn.bdimg.com/it/u=2908926804,480994655&fm=23&gp=0.jpg",
    			"http://img0.imgtn.bdimg.com/it/u=3203753615,3355949903&fm=23&gp=0.jpg",
    			"http://img1.imgtn.bdimg.com/it/u=3912886271,634420248&fm=23&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=2195871892,1606241027&fm=23&gp=0.jpg",
    			"http://img0.imgtn.bdimg.com/it/u=4239481692,2065325068&fm=11&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=3381281436,931854300&fm=11&gp=0.jpg",
    			"http://img1.imgtn.bdimg.com/it/u=2631695484,2136982049&fm=21&gp=0.jpg",
    			"http://img0.imgtn.bdimg.com/it/u=4151707655,2713588283&fm=11&gp=0.jpg",
    			"http://img4.imgtn.bdimg.com/it/u=4166177135,575146071&fm=11&gp=0.jpg",
    			"http://img5.imgtn.bdimg.com/it/u=3828484765,90386254&fm=23&gp=0.jpg",
    			"http://img1.imgtn.bdimg.com/it/u=1280312745,3700393430&fm=21&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=1346493067,1397877260&fm=11&gp=0.jpg",
    			"http://img4.imgtn.bdimg.com/it/u=1949690491,399242793&fm=23&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=921364150,2863711606&fm=23&gp=0.jpg",
    			"http://img0.imgtn.bdimg.com/it/u=3304123038,626553798&fm=23&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=743519941,2465620472&fm=23&gp=0.jpg",
    			"http://img1.imgtn.bdimg.com/it/u=2146379855,2794649478&fm=11&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=419842901,3121938376&fm=11&gp=0.jpg",  			
    			"http://img3.imgtn.bdimg.com/it/u=633517761,1719248070&fm=21&gp=0.jpg",   			
    			"http://img2.imgtn.bdimg.com/it/u=3880804824,2256639144&fm=21&gp=0.jpg",
    			"http://img1.imgtn.bdimg.com/it/u=1446009823,573253062&fm=21&gp=0.jpg",
    			"http://img4.imgtn.bdimg.com/it/u=3381301006,27507774&fm=23&gp=0.jpg",
    			"http://img4.imgtn.bdimg.com/it/u=937981325,2221474511&fm=23&gp=0.jpg",
    			"http://img1.imgtn.bdimg.com/it/u=539594930,741035411&fm=23&gp=0.jpg",
    			"http://img0.imgtn.bdimg.com/it/u=681231121,1949019233&fm=23&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=2879858209,2579447298&fm=23&gp=0.jpg",
    			"http://img4.imgtn.bdimg.com/it/u=3032924788,3223863286&fm=23&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=1131182504,3932397137&fm=23&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=1367821842,1192073204&fm=11&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=3002101849,2246779067&fm=21&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=96645509,3928017327&fm=21&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=935964783,2968063554&fm=21&gp=0.jpg",
    			"http://img3.imgtn.bdimg.com/it/u=1353775166,526365991&fm=21&gp=0.jpg",
    			"http://img4.imgtn.bdimg.com/it/u=3768933936,2807662682&fm=21&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=3178771607,1977602323&fm=21&gp=0.jpg",
    			"http://img0.imgtn.bdimg.com/it/u=3539127633,396953511&fm=21&gp=0.jpg",
    			"http://img5.imgtn.bdimg.com/it/u=2495613533,1784340234&fm=21&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=2024777882,579692070&fm=21&gp=0.jpg",
    			"http://img2.imgtn.bdimg.com/it/u=141080736,2335558934&fm=21&gp=0.jpg"
    			};
    	for (int i = 0; i < strings.length; i++) {
    		Photo photo = new Photo();
    		photo.url = strings[i];
    		sPics.add(photo);
    	}
		new BmobBatch().insertBatch(sPics).doBatch(new QueryListListener<BatchResult>() {
			@Override
			public void done(List<BatchResult> list, BmobException e) {
				if (e == null) {
					toastUtil.showToast("批量操作成功");
				}else {
					toastUtil.showToast("批量添加失败:"+e.getMessage());
				}
			}
		});
	}
}
