package com.jarry.jayvoice.widget;

public interface Pullable
{
	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ���ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullDown();

	/**
	 * �ж��Ƿ�����������������Ҫ�������ܿ���ֱ��return false
	 * 
	 * @return true��������������򷵻�false
	 */
	boolean canPullUp();
}
