package com.xm;
/**
 * ����������
 *@project NetSdk 
 *@author Administrator
 *@date 2014-3-29 ����02:42:16
 *@describe TODO
 */
public class CONFIG_EncodeAbility {
	public int iMaxEncodePower;		///< ֧�ֵ�����������
	public int iChannelMaxSetSync;		///< ÿ��ͨ���ֱ����Ƿ���Ҫͬ�� 0-��ͬ��, 1 -ͬ��
	public int nMaxPowerPerChannel[];		///< ÿ��ͨ��֧�ֵ���߱�������
	public int ImageSizePerChannel[];		///< ÿ��ͨ��֧�ֵ�ͼ��ֱ���
	public CONFIG_EncodeAbility() {
		nMaxPowerPerChannel = new int[MyConfig.NET_MAX_CHANNUM];
		ImageSizePerChannel = new int[MyConfig.NET_MAX_CHANNUM];
	}
}
