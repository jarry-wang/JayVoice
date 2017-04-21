package com.jarry.jayvoice.util;

import java.util.List;

public class ListUtil {

	public static boolean isNotNull(List list){
		if(list!=null&&(!list.isEmpty())&&list.size()>0){
			return true;
		}
		return false;
	}
}
