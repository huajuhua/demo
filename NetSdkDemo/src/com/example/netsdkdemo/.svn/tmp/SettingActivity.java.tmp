package com.example.netsdkdemo;

import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.netsdkdemo.devsetting.DevConfigActivity;
import com.xm.MyConfig;
import com.xm.MyConfig.NetKeyBoardState;
import com.xm.MyConfig.NetKeyBoardValue;
import com.xm.MyConfig.SDK_CAPTURE_SIZE_t;
import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SDK_CONFIG_ENCODE_SIMPLIIFY;
import com.xm.SDK_NetDHCPConfig;
import com.xm.SDK_RECORDCONFIG;
import com.xm.SDK_VIDEOCOLOR;
import com.xm.dialog.ModifyPwdDlg;
import com.xm.dialog.ModifyPwdDlg.OnMyClickListener;
/**
 * 
 *@project NetSdkDemo 
 *@author huangwanshui
 *@date 2014-1-1 下午01:51:35
 *@describe Setting
 */
public class SettingActivity extends Activity implements OnClickListener,OnSeekBarChangeListener{
	private long mLoginId;
	private NetSdk mNetSdk;
	private Button btn1;
	private Button btn2;
	private Button split4;
	private Button split1;
	private Button OK;
	private SeekBar BrightnessSB;
	private SeekBar ContrastSB;
	private SeekBar SaturationSB;
	private SeekBar HueSB;
	private Button getEncode;
	private Button getRecordTime;
	private Button setRecordTime;
	private TextView RecordTimeInfo;
	private SDK_VIDEOCOLOR[] videocolors;
	private SDK_CONFIG_ENCODE_SIMPLIIFY[] encodeInfos;
	private SDK_RECORDCONFIG recordConfig;
	private TextView encodeInfo;
	private Button setEncode;
	private Button getDHCP;
	private TextView DHCPInfo;
	private SDK_NetDHCPConfig[] netDHCPCfg;
	private float mDensity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		videocolors = new SDK_VIDEOCOLOR[2];
		encodeInfos = new SDK_CONFIG_ENCODE_SIMPLIIFY[32];
		netDHCPCfg = new SDK_NetDHCPConfig[4];
		recordConfig = new SDK_RECORDCONFIG();
		DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        mDensity = dm.density;
		initLayout();
		initData();
	}
	/*
	 * 初始化数据
	 */
	private void initData() {
		Intent it = getIntent();
		mLoginId = it.getLongExtra("loginId", 0);
		mNetSdk = NetSdk.getInstance();
		int i = 0;
		for(i = 0; i < 2; ++i) {
			videocolors[i] = new SDK_VIDEOCOLOR();
		}
		for(i = 0 ; i < 32; ++i) {
			encodeInfos[i] = new SDK_CONFIG_ENCODE_SIMPLIIFY();
		}
		for(i = 0 ; i < 4; ++i) {
			netDHCPCfg[i] = new SDK_NetDHCPConfig();
		}
		if(mNetSdk.H264DVRGetDevConfig(mLoginId, 84, 0,videocolors,1000)) {
			BrightnessSB.setProgress(videocolors[0].dstColor.nBrightness);
			ContrastSB.setProgress(videocolors[0].dstColor.nContrast);
			HueSB.setProgress(videocolors[0].dstColor.nHue);
			SaturationSB.setProgress(videocolors[0].dstColor.nSaturation);
		}
		
	}
	private void initLayout() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		split4 = (Button) findViewById(R.id.split4);
		split1 = (Button) findViewById(R.id.split1);
		BrightnessSB = (SeekBar) findViewById(R.id.nBrightness);
		BrightnessSB.setOnSeekBarChangeListener(this);
		ContrastSB = (SeekBar) findViewById(R.id.nContrast);
		ContrastSB.setOnSeekBarChangeListener(this);
		HueSB = (SeekBar) findViewById(R.id.nHue);
		HueSB.setOnSeekBarChangeListener(this);
		SaturationSB = (SeekBar) findViewById(R.id.nSaturation);
		SaturationSB.setOnSeekBarChangeListener(this);
		OK = (Button) findViewById(R.id.Ok);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		split1.setOnClickListener(this);
		split4.setOnClickListener(this);
		OK.setOnClickListener(this);
		getEncode = (Button) findViewById(R.id.getEncode);
		getEncode.setOnClickListener(this);
		encodeInfo = (TextView)findViewById(R.id.encodeinfo);
		setEncode = (Button) findViewById(R.id.setEncode);
		setEncode.setOnClickListener(this);
		getDHCP = (Button) findViewById(R.id.getDHCP);
		getDHCP.setOnClickListener(this);
		DHCPInfo = (TextView) findViewById(R.id.DHCPinfo);
		getRecordTime = (Button) findViewById(R.id.getRecordTime);
		getRecordTime.setOnClickListener(this);
		RecordTimeInfo = (TextView) findViewById(R.id.RecordTime);
		setRecordTime = (Button) findViewById(R.id.setrecordtime);
		setRecordTime.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn1:
			mNetSdk.H264DVRClickKey(mLoginId, NetKeyBoardValue.SDK_NET_KEY_1, NetKeyBoardState.SDK_NET_KEYBOARD_KEYDOWN);
			break;
		case R.id.btn2:
			mNetSdk.H264DVRClickKey(mLoginId, NetKeyBoardValue.SDK_NET_KEY_2, NetKeyBoardState.SDK_NET_KEYBOARD_KEYDOWN);
			break;
		case R.id.split4:
			mNetSdk.H264DVRClickKey(mLoginId, NetKeyBoardValue.SDK_NET_KEY_SPLIT4, NetKeyBoardState.SDK_NET_KEYBOARD_KEYDOWN);
			break;
		case R.id.split1:
			mNetSdk.H264DVRClickKey(mLoginId, NetKeyBoardValue.SDK_NET_KEY_SPLIT1, NetKeyBoardState.SDK_NET_KEYBOARD_KEYDOWN);
			break;
		case R.id.Ok:
			videocolors[0].dstColor.nBrightness = BrightnessSB.getProgress();
			if(mNetSdk.H264DVRSetDevConfig(mLoginId, 84, 0,videocolors,1000)) {
				System.out.println("ok");
			}
			break;
		case R.id.getEncode:
			if(mNetSdk.H264DVRGetDevConfig(mLoginId, 68,-1, encodeInfos, 1000)) {
				StringBuffer sb = new StringBuffer();
				sb.append("分辨率:"+MyConfig.Capture_Size[encodeInfos[0].dstMainFmt.vfFormat.iResolution]);
				sb.append("帧率:"+encodeInfos[0].dstMainFmt.vfFormat.nFPS);
				encodeInfo.setText(sb.toString());
			}
			break;
		case R.id.setEncode:
			if(encodeInfos[0] == null)
				break;
			encodeInfos[0].dstMainFmt.vfFormat.nFPS = 20;
			if(mNetSdk.H264DVRSetDevConfig(mLoginId, 68,-1, encodeInfos, 1000)) {
				System.out.println("SetEncode OK");
			}
			break;
		case R.id.getDHCP:
			if(mNetSdk.H264DVRGetDevConfig(mLoginId, 37, -1, netDHCPCfg, 1000)) {
				String str = new String(netDHCPCfg[0].ifName);
				DHCPInfo.setText(str);
			}
			break;
		case R.id.getRecordTime:
			if(recordConfig == null)
				break;
			if(mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD, 0, recordConfig, 5000)) {
				RecordTimeInfo.setText("time:"+recordConfig.tsSchedule[0][0].startHour);
				System.out.println("dddddd:"+recordConfig.iRecordMode);
			}
			break;
		case R.id.setrecordtime:
			if(recordConfig == null)
				break;
			recordConfig.iPacketLength = 2;
			recordConfig.iRecordMode = 2;
			/*星期天从0开始，1维数组表示星期，二维数组表示时间段*/
			recordConfig.tsSchedule[0][0].startHour = 12;
			if(mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD, 0, recordConfig, 5000)) {
				Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
			}else
				Toast.makeText(SettingActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
	public void onRecordSet(View v) {
		Intent record_it = new Intent(SettingActivity.this,RecordConfig.class);
		record_it.putExtra("LoginId", mLoginId);
		startActivity(record_it);
	}
	public void onEncodeSet(View v) {
		Intent encode_it = new Intent(SettingActivity.this,EncodeConfig.class);
		encode_it.putExtra("LoginId", mLoginId);
		startActivity(encode_it);	
	}
	public void onCameraSet(View v) {
		Intent camera_it = new Intent(SettingActivity.this,CameraConfig.class);
		camera_it.putExtra("LoginId", mLoginId);
		startActivity(camera_it);	
	}
	public void onAlarmSet(View v) {
		Intent alarmset_it = new Intent(SettingActivity.this,AlarmSettingActivity.class);
		alarmset_it.putExtra("LoginId", mLoginId);
		startActivity(alarmset_it);
	}
	public void onNatConfig(View v) {
		Intent devconfig_it = new Intent(SettingActivity.this,DevConfigActivity.class);
		devconfig_it.putExtra("LoginId", mLoginId);
		startActivity(devconfig_it);
	}
	public void onChangePWD(View v) {
		byte[] pwd = new byte[32];
		boolean bret = mNetSdk.getDevPwd(mLoginId, SdkConfigType.E_SDK_CONFIG_USER,-1,pwd,1000);
		final ModifyPwdDlg dlg = new ModifyPwdDlg(SettingActivity.this,mLoginId,
				pwd,null,mDensity,
				new String[]{"old_password",
				"new_password",
				"sure_password",
				"new_pwd_error",
				"old_pwd_error",
				"modify_pwd"});
		dlg.setNegativeButton(new OnMyClickListener() {
			
			@Override
			public void onClick(View v, String result) {
				// TODO Auto-generated method stub
				dlg.onDismiss();
			}
		});
		dlg.setPositiveButton(new OnMyClickListener() {
			
			@Override
			public void onClick(View v, String result) {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, result, Toast.LENGTH_LONG).show();
				dlg.onDismiss();
			}
		});
		dlg.onShow();
	}
}	
