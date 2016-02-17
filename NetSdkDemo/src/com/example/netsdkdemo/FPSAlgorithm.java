/**
 * XMFamily
 * FPSAlgorithm.java
 * Administrator
 * TODO
 * 2014-7-3
 */
package com.example.netsdkdemo;

/**
 * XMFamily
 * FPSAlgorithm.java
 * @author huangwanshui
 * TODO 帧率算法
 * 2014-7-3
 */
public class FPSAlgorithm
{
	private int iMaxEncodePower;//支持的最大编码能力
	public FPSAlgorithm()
	{
		
	}
	/**
	 * 设置最大编码能力
	 * @param maxencodepower
	 */
	public void setMaxEncodePower(int maxencodepower) 
	{
		this.iMaxEncodePower = maxencodepower;
	}
	/**
	 * 获取副码流最大帧率
	 * @param main_stream
	 * @param main_fps
	 * @param sub_stream
	 * @return
	 */
	public int getSubMaxFPS(int main_stream,int main_fps,int sub_stream)
	{
		float fps;
		fps = (float)((float)iMaxEncodePower - (float)(main_stream * main_fps)) / (float)sub_stream;
		return (int) fps;
	}
	/**
	 * 获取主码流最大帧率
	 * @param sub_stream
	 * @param sub_fps
	 * @param main_stream
	 * @return
	 */
	public int getMainMaxFPS(int sub_stream,int sub_fps,int main_stream)
	{
		float fps;
		fps = (float)((float)iMaxEncodePower - (float)(sub_stream * sub_fps)) / (float)main_stream;
		return (int) fps;
	}
}
