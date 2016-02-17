package com.xm;

public class SDK_VIDEO_FORMAT {
	public int		iCompression;			//  压缩模式 	
	public int		iResolution;			//  分辨率 参照枚举SDK_CAPTURE_SIZE_t
	public int		iBitRateControl;		//  码流控制 参照枚举SDK_capture_brc_t
	public int		iQuality;				//  码流的画质 档次1-6		
	public int		nFPS;					//  帧率值，NTSC/PAL不区分,负数表示多秒一帧		
	public int		nBitRate;				//  0-4096k,该列表主要由客户端保存，设备只接收实际的码流值而不是下标。
	public int		iGOP;					//  描述两个I帧之间的间隔时间，2-12 
}
