package com.jarry.jayvoice.bean;


import cn.bmob.v3.datatype.BmobFile;

public class Song extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Singer singer;//歌手
	private BmobFile song;//歌曲文件
	private BmobFile lyric;//歌词
	private Album album;//专辑
	private SType sType;//分类
	private String TypeLabel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public Singer getSinger() {
		return singer;
	}
	public void setSinger(Singer singer) {
		this.singer = singer;
	}
	public BmobFile getSong() {
		return song;
	}
	public void setSong(BmobFile song) {
		this.song = song;
	}
	public BmobFile getLyric() {
		return lyric;
	}
	public void setLyric(BmobFile lyric) {
		this.lyric = lyric;
	}

	public String getTypeLabel() {
		return TypeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		TypeLabel = typeLabel;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public SType getsType() {
		return sType;
	}
	public void setsType(SType sType) {
		this.sType = sType;
	}
	
	
}
