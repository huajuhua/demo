package com.xm;

public class SDK_VIDEO_FORMAT {
	public int		iCompression;			//  ѹ��ģʽ 	
	public int		iResolution;			//  �ֱ��� ����ö��SDK_CAPTURE_SIZE_t
	public int		iBitRateControl;		//  �������� ����ö��SDK_capture_brc_t
	public int		iQuality;				//  �����Ļ��� ����1-6		
	public int		nFPS;					//  ֡��ֵ��NTSC/PAL������,������ʾ����һ֡		
	public int		nBitRate;				//  0-4096k,���б���Ҫ�ɿͻ��˱��棬�豸ֻ����ʵ�ʵ�����ֵ�������±ꡣ
	public int		iGOP;					//  ��������I֮֡��ļ��ʱ�䣬2-12 
}
