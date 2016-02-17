package com.xm;

public class SDK_VIDEOCOLOR_PARAM {
	public int	nBrightness;		///< 亮度	0-100
	public int	nContrast;			///< 对比度	0-100
	public int	nSaturation;		///< 饱和度	0-100
	public int	nHue;				///< 色度	0-100
	public int	mGain;				///< 增益	0-100 第７位置1表示自动增益		
	public int	mWhitebalance;		///< 自动白电平控制，bit7置位表示开启自动控制.0x0,0x1,0x2分别代表低,中,高等级
	public int nAcutance;          ///< 锐度   0-15
}
