/**
 * NetSdkDemo
 * DevConfig.java
 * Administrator
 * TODO
 * 2014-12-9
 */
package com.example.netsdkdemo.devsetting;

import com.xm.MyConfig.SdkConfigType;
import com.xm.H264DecoderIF;
import com.xm.NetSdk;
import com.xm.javaclass.SDK_NatConfig;
import com.xm.utils.OutputDebug;

import android.app.Activity;

/**
 * NetSdkDemo
 * DevConfig.java
 * @author huangwanshui
 * TODO
 * 2014-12-9
 */
public class DevConfig {
	private NetSdk mNetSdk;
	private long mLoginId;
	public DevConfig(long loginid) {
		mNetSdk = NetSdk.getInstance();
		this.mLoginId = loginid;
	}
	public byte[] getConfig(int commd,int chnid,int bufsize) {
		boolean bret = false;
		if(bufsize <= 0)
			return null;
		byte[] buf = new byte[bufsize];
		bret = mNetSdk.H264DVRGetDevConfig2(mLoginId, commd, chnid,buf,5000);
		if(bret)
			return buf;
		else {
			OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
			return null;
		}
	}
	public boolean setConfig(int commd,int chnid,byte[] config) {
		boolean bret = false;
		bret = mNetSdk.H264DVRSetDevConfig2(mLoginId, commd, chnid, config, 5000);
		if(!bret) {
			OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
		}
		return bret;
	}
}
