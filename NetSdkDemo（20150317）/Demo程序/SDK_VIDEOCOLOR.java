package com.xm;

public class SDK_VIDEOCOLOR {
	public SDK_TIMESECTION			tsTimeSection;		/// ʱ���
	public SDK_VIDEOCOLOR_PARAM	dstColor;			/// ��ɫ����
	public int					iEnable;
	public SDK_VIDEOCOLOR() {
		tsTimeSection = new SDK_TIMESECTION();
		dstColor = new SDK_VIDEOCOLOR_PARAM();
	}
}
