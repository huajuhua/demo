package com.xm;
/**
 * 编码能力集
 *@project NetSdk 
 *@author Administrator
 *@date 2014-3-29 下午02:42:16
 *@describe TODO
 */
public class CONFIG_EncodeAbility {
	public int iMaxEncodePower;		///< 支持的最大编码能力
	public int iChannelMaxSetSync;		///< 每个通道分辨率是否需要同步 0-不同步, 1 -同步
	public int nMaxPowerPerChannel[];		///< 每个通道支持的最高编码能力
	public int ImageSizePerChannel[];		///< 每个通道支持的图像分辨率
	public CONFIG_EncodeAbility() {
		nMaxPowerPerChannel = new int[MyConfig.NET_MAX_CHANNUM];
		ImageSizePerChannel = new int[MyConfig.NET_MAX_CHANNUM];
	}
}
