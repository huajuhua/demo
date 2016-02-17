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
 * TODO ֡���㷨
 * 2014-7-3
 */
public class FPSAlgorithm
{
	private int iMaxEncodePower;//֧�ֵ�����������
	public FPSAlgorithm()
	{
		
	}
	/**
	 * ��������������
	 * @param maxencodepower
	 */
	public void setMaxEncodePower(int maxencodepower) 
	{
		this.iMaxEncodePower = maxencodepower;
	}
	/**
	 * ��ȡ���������֡��
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
	 * ��ȡ���������֡��
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
