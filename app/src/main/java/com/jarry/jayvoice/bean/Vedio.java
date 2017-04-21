package com.jarry.jayvoice.bean;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

public class Vedio extends BaseBean{
	
	public String name;
	
	public BmobFile image; 
	
	public String lead;//演员，主持人等
	
	public BmobDate time;
	
	public String director;
	
	public String style;
	
	public String desc;
	
	public VedioType videotype;
	
	public BmobFile file; 
	
	public String fileurl;
	
	public int playnum;
	
	public int typeId;
}
