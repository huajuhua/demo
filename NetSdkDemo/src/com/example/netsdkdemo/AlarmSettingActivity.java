/**
 * NetSdkDemo
 * AlarmSettingActivity.java
 * Administrator
 * TODO
 * 2014-10-15
 */
package com.example.netsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SDK_AllAlarmIn;
import com.xm.SDK_AllAlarmOut;

/**
 * NetSdkDemo
 * AlarmSettingActivity.java
 * @author huangwanshui
 * TODO
 * 2014-10-15
 */
public class AlarmSettingActivity extends Activity {
	private NetSdk mNetSdk;
	private long mLoginId;
	private SDK_AllAlarmIn mAllAlarmIn; 
	private SDK_AllAlarmOut mAllAlarmOut;
	private boolean mbOpen;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmset);
		initData();
	}
	private void initData() {
		Intent it = getIntent();
		mLoginId = it.getLongExtra("LoginId", 0);
		mNetSdk = NetSdk.getInstance();
		mAllAlarmIn = new SDK_AllAlarmIn();
		mAllAlarmOut = new SDK_AllAlarmOut();
	}
	public void onInSwitch(View v) {
		boolean bret = mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_ALARM_IN, -1,mAllAlarmIn, 5000);
		System.out.println("alarminconfig:"+bret+":"+mAllAlarmIn.alarmInParam[0].bEnable);
		mAllAlarmIn.alarmInParam[0].bEnable = !mAllAlarmIn.alarmInParam[0].bEnable;
		if(mAllAlarmIn.alarmInParam[0].bEnable)
			((Button)v).setText("报警输入开");
		else
			((Button)v).setText("报警输入关");
		boolean bret2 = mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_ALARM_IN, -1,
				mAllAlarmIn, 5000);
		System.out.println("bret2:"+mNetSdk.GetLastError());
	}
	public void onOutSwitch(View v) {
		boolean bret = mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_ALARM_OUT, -1,mAllAlarmOut, 5000);
		System.out.println("mAllAlarmOut:"+bret+":"+mAllAlarmOut.alarmOutParam[0].AlarmOutType);
		mAllAlarmOut.alarmOutParam[0].AlarmOutType = 1;
		boolean bret2 = mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_ALARM_OUT, -1,
				mAllAlarmOut, 5000);
//		System.out.println("bret2:"+mNetSdk.GetLastError());
	}
}
