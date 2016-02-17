package com.xm;

public class SDK_VIDEOCOLOR {
	public SDK_TIMESECTION			tsTimeSection;		/// 时间段
	public SDK_VIDEOCOLOR_PARAM	dstColor;			/// 颜色定义
	public int					iEnable;
	public SDK_VIDEOCOLOR() {
		tsTimeSection = new SDK_TIMESECTION();
		dstColor = new SDK_VIDEOCOLOR_PARAM();
	}
}
