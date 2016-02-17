/**
 * NetSdkDemo
 * NatConfig.java
 * Administrator
 * TODO
 * 2014-12-9
 */
package com.example.netsdkdemo.devsetting;
import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.basic.G;
import com.example.netsdkdemo.R;
import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SearchDeviceInfo;
import com.xm.javaclass.SDK_ALARM_INPUTCONFIG_ALL;
import com.xm.javaclass.SDK_AlarmOutConfigAll;
import com.xm.javaclass.SDK_CONFIG_NET_COMMON;
import com.xm.javaclass.SDK_CONFIG_NET_COMMON_V3;
import com.xm.javaclass.SDK_DVR_WORKSTATE;
import com.xm.javaclass.SDK_EventHandler;
import com.xm.javaclass.SDK_LogList;
import com.xm.javaclass.SDK_LogSearchCondition;
import com.xm.javaclass.SDK_NatConfig;
import com.xm.javaclass.SDK_NetDDNSConfigALL;
import com.xm.javaclass.SDK_NetDHCPConfigAll;
import com.xm.javaclass.SDK_NetDNSConfig;
import com.xm.javaclass.SDK_NetUPNPConfig;
import com.xm.javaclass.SDK_NetWifiConfig;
import com.xm.javaclass.SDK_NetWifiDevice;
import com.xm.javaclass.SDK_NetWifiDeviceAll;
import com.xm.javaclass.SDK_WifiStatusInfo;
import com.xm.utils.DataUtils;
import com.xm.utils.OutputDebug;


/**
 * NetSdkDemo
 * NatConfig.java
 * @author huangwanshui
 * TODO
 * 2014-12-9
 */
public class DevConfigActivity extends Activity {
	private ViewHolder mViewHolder;
	private DevConfig mDevConfig;
	private SDK_NatConfig mConfig;
	private SDK_NetDHCPConfigAll mConfig2;
	private SDK_DVR_WORKSTATE mConfig3;
	private SDK_NetDDNSConfigALL mConfig4;
	private SDK_NetUPNPConfig mConfig5;
	private SDK_ALARM_INPUTCONFIG_ALL mConfig6;
	private SDK_AlarmOutConfigAll mConfig7;
	private SDK_LogList mConfig8;
	private SDK_LogSearchCondition mConfig9;
	private SDK_NetWifiDeviceAll mConfig10;
	private SDK_NetWifiConfig mConfig11;
	private SDK_WifiStatusInfo mConfig12;
	private SDK_NetDNSConfig mConfig13;
	private SDK_CONFIG_NET_COMMON mConfig14;
	private int mConfigType = 0;
	private NetSdk mNetSdk;
	private long mLoginid;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_config);
		initLayout();
		initData();
	}
	private void initLayout() {
		mViewHolder = new ViewHolder();
		mViewHolder.getConfigTv = (TextView) findViewById(R.id.getConfig);
		mViewHolder.setConfigEt = (EditText) findViewById(R.id.setConfig);
		mViewHolder.select_sp = (Spinner) findViewById(R.id.select_config);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DevConfigActivity.this, R.array.config_labels,
                android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mViewHolder.select_sp.setAdapter(adapter);
		mViewHolder.select_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mConfigType = arg2;
				onGetConfig();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void initData() {
		mLoginid = getIntent().getLongExtra("LoginId", 0);
		mNetSdk = NetSdk.getInstance();
		mConfigType = 1;
		onGetConfig();
	}
	private void onGetConfig() {
		SDK_EventHandler handler22=  new SDK_EventHandler();
		OutputDebug.OutputDebugLogD("Devconfig", "SDK_EventHandlerddddd:"+com.basic.G.Sizeof(handler22));
		
		if(mLoginid != 0) {
			byte[] buf;
			switch (mConfigType) {
			case 0:
				mDevConfig = new DevConfig(mLoginid);
				mConfig = new SDK_NatConfig();
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NAT, 0, com.basic.G.Sizeof(mConfig));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig, buf);
					mViewHolder.getConfigTv.setText("bEnable:"+mConfig.st_0_bEnable+"\n"
					+"nMTU:"+mConfig.st_3_nMTU+"\n"
					+"serverAddr:"+new String(mConfig.st_4_serverAddr)+"\n"
					+"serverPort:"+mConfig.st_5_serverPort);
				}
				break;
			case 1:
				mDevConfig = new DevConfig(mLoginid);
				mConfig2 = new SDK_NetDHCPConfigAll();
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NET_DHCP, -1, com.basic.G.Sizeof(mConfig2));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig2, buf);
					mViewHolder.getConfigTv.setText("bEnable:"+mConfig2.st_0_vNetDHCPConfig[0].st_0_bEnable+"\n"
					+"ifName:"+new String(mConfig2.st_0_vNetDHCPConfig[0].st_1_ifName));
				}
				break;
			case 2:
				mDevConfig = new DevConfig(mLoginid);
				mConfig4 = new SDK_NetDDNSConfigALL();
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NET_DDNS, -1, com.basic.G.Sizeof(mConfig4));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig4, buf);
					mViewHolder.getConfigTv.setText("bEnable:"+mConfig4.st_0_ddnsConfig[0].st_0_Enable);
				}
				break;
			case 3:
				mDevConfig = new DevConfig(mLoginid); 
				mConfig5 = new SDK_NetUPNPConfig();
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NET_UPNP, -1, com.basic.G.Sizeof(mConfig5));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig5, buf);
					mViewHolder.getConfigTv.setText("st_3_iHTTPPort:"+mConfig5.st_3_iHTTPPort);
				}
				break;
//			case 4:
//				mConfig3 = new SDK_DVR_WORKSTATE();
//				byte[] _buf = new byte[com.basic.G.Sizeof(mConfig3)];
//				boolean bret = mNetSdk.GetDVRWorkState(mLoginid,_buf);
//				if(bret) {
//					com.basic.G.BytesToObj(mConfig3, _buf);
//					mViewHolder.getConfigTv.setText("ChnState:"+mConfig3.st_0_vChnState[0].st_1_iBitrate);
//				}else {
//					OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
//				}
//				break;
			case 4:
				mConfig6 = new SDK_ALARM_INPUTCONFIG_ALL();
				mDevConfig = new DevConfig(mLoginid);
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_ALARM_IN, -1, com.basic.G.Sizeof(mConfig6));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig6, buf);
					mViewHolder.getConfigTv.setText("st_0_bEnable:"+mConfig6.st_0_vAlarmConfigAll[0]
							.st_3_hEvent.st_09_schedule.st_0_tsSchedule[0][0].st_1_startHour);
				}
				break;
			case 5:
				mConfig7 = new SDK_AlarmOutConfigAll();
				mDevConfig = new DevConfig(mLoginid);
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_ALARM_OUT, -1, com.basic.G.Sizeof(mConfig7));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig7, buf);
					mViewHolder.getConfigTv.setText("st_0_nAlarmOutType:"+mConfig7.st_0_vAlarmOutConfigAll[0].st_0_nAlarmOutType);
				}
				break;
			case 6:
				mConfig9 = new SDK_LogSearchCondition();
				mConfig9.st_0_nType = 0;
				mConfig9.st_1_iLogPosition = 0;
				mConfig9.st_2_stBeginTime.st_0_year = 2014;
				mConfig9.st_2_stBeginTime.st_1_month = 12;
				mConfig9.st_2_stBeginTime.st_2_day = 11;
				mConfig9.st_2_stBeginTime.st_4_hour = 00;
				mConfig9.st_2_stBeginTime.st_5_minute = 00;
				mConfig9.st_2_stBeginTime.st_6_second = 00;
				mConfig9.st_3_stEndTime.st_0_year = 2014;
				mConfig9.st_3_stEndTime.st_1_month = 12;
				mConfig9.st_3_stEndTime.st_2_day = 11;
				mConfig9.st_3_stEndTime.st_4_hour = 23;
				mConfig9.st_3_stEndTime.st_5_minute = 00;
				mConfig9.st_3_stEndTime.st_6_second = 00;
				mConfig8 = new SDK_LogList();
				int len = com.basic.G.Sizeof(mConfig8);
				OutputDebug.OutputDebugLogD("DevConfig", "len:"+len);
				byte[] loglist = new byte[com.basic.G.Sizeof(mConfig8)];
				boolean bret = mNetSdk.FindDVRLog(mLoginid, com.basic.G.ObjToBytes(mConfig9), loglist,2000);
				OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
				if(bret) {
					com.basic.G.BytesToObj(mConfig8, loglist);
					mViewHolder.getConfigTv.setText("st_2_sData:"+new String(mConfig8.st_1_Logs[0].st_2_sData));
				}else {
					OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
				}
				break;
			case 7:
				mConfig2.st_0_vNetDHCPConfig[0].st_0_bEnable = false;
				bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_NET_DHCP, -1, com.basic.G.ObjToBytes(mConfig2));
				if(bret) {
					OutputDebug.OutputDebugLogD("DevConfig", "OK");
				}else 
					OutputDebug.OutputDebugLogD("DevConfig", "failed");
				onSearchDev();
				break;
			case 8:
				mConfig10 = new SDK_NetWifiDeviceAll();
				mDevConfig = new DevConfig(mLoginid);
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NET_WIFI_AP_LIST, -1, com.basic.G.Sizeof(mConfig10));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig10, buf);
					mConfig11 = new SDK_NetWifiConfig();
					OutputDebug.OutputDebugLogD("DevConfig", "size:"+com.basic.G.Sizeof(mConfig11));
					buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NET_WIFI, -1, com.basic.G.Sizeof(mConfig11));
					if(buf != null) {
						com.basic.G.BytesToObj(mConfig11, buf);
						for(int i = 0 ; i < mConfig10.st_0_nDevNumber; ++i) {
							SDK_NetWifiDevice device = mConfig10.st_1_vNetWifiDeviceAll[i];
							OutputDebug.OutputDebugLogD("DevConfig", "ssid:"+G.ToString(device.st_0_sSSID));
							if(G.ToString(device.st_0_sSSID).equals("Netcore")) {
								mConfig11.st_00_bEnable = true;
								mConfig11.st_03_nChannel = 0;
								mConfig11.st_04_sNetType = device.st_3_sNetType;
								mConfig11.st_05_sEncrypType = device.st_4_sEncrypType;
								mConfig11.st_06_sAuth = device.st_5_sAuth;
							//	mConfig11.st_08_sKeys//����
								G.SetValue(mConfig11.st_01_sSSID, device.st_0_sSSID);
								com.basic.G.SetValue(mConfig11.st_08_sKeys, "");
								bret = mNetSdk.H264DVRSetDevConfig2(mLoginid, SdkConfigType.E_SDK_CONFIG_NET_WIFI,-1, com.basic.G.ObjToBytes(mConfig11), 3000);
								if(bret) {
									if(bret) {
										OutputDebug.OutputDebugLogD("DevConfig", "OK");
									}else 
										OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
								}
							}
						}	
					}
				}
				break;
			case 9:
				mConfig12 = new SDK_WifiStatusInfo();
				mDevConfig = new DevConfig(mLoginid);
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_WIFI_STATUS, -1, com.basic.G.Sizeof(mConfig12));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig12, buf);
					mViewHolder.getConfigTv.setText("st_0_connectStatus:"+mConfig12.st_0_connectStatus);
				}
				break;
			case 10:
				mConfig13 = new SDK_NetDNSConfig();
				mDevConfig = new DevConfig(mLoginid);
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_NET_DNS, -1, com.basic.G.Sizeof(mConfig13));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig13, buf);
					mViewHolder.getConfigTv.setText("DNS):"+mConfig13.st_0_PrimaryDNS.getIp());
				}
				break;
			case 11:
				mConfig14 = new SDK_CONFIG_NET_COMMON();
				mDevConfig = new DevConfig(mLoginid);
				buf = mDevConfig.getConfig(SdkConfigType.E_SDK_CONFIG_SYSNET, -1, com.basic.G.Sizeof(mConfig14));
				if(buf != null) {
					com.basic.G.BytesToObj(mConfig14, buf);
					mViewHolder.getConfigTv.setText("IP):"+mConfig14.st_01_HostIP.getIp());
				}
				break;
			default:
				break;
			}
			
		}
	}
	private void onSetConfig(String str) {
		boolean bret = false;
		switch (mConfigType) {
		case 0:
			if(Integer.parseInt(DataUtils.getCenterStr2(str, 0, 1)) == 0)
				mConfig.st_0_bEnable = false;
			else
				mConfig.st_0_bEnable = true;
			mConfig.st_3_nMTU = Integer.parseInt(DataUtils.getCenterStr2(str, 1, 2));
			com.basic.G.SetValue(mConfig.st_4_serverAddr, DataUtils.getCenterStr2(str, 2, 3).getBytes());
			mConfig.st_5_serverPort = Integer.parseInt(DataUtils.getCenterStr2(str, 3, 4));
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_NAT, -1, com.basic.G.ObjToBytes(mConfig));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		case 1:
			if(DataUtils.getCenterStr2(str, 0, 1).equals("1"))
				mConfig2.st_0_vNetDHCPConfig[0].st_0_bEnable = true;
			else
				mConfig2.st_0_vNetDHCPConfig[0].st_0_bEnable = false;
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_NET_DHCP, -1, com.basic.G.ObjToBytes(mConfig2));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		case 2:
			if(DataUtils.getCenterStr2(str, 0, 1).equals("1"))
				mConfig4.st_0_ddnsConfig[0].st_0_Enable = true;
			else
				mConfig4.st_0_ddnsConfig[0].st_0_Enable = false;
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_NET_DDNS, -1, com.basic.G.ObjToBytes(mConfig4));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		case 3:
			mConfig5.st_3_iHTTPPort = Integer.parseInt(DataUtils.getCenterStr2(str, 0, 1));
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_NET_UPNP, -1, com.basic.G.ObjToBytes(mConfig5));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
//		case 4:
//			
//			break;
		case 4:
			mConfig6.st_0_vAlarmConfigAll[0].st_0_bEnable = Integer.parseInt(DataUtils.getCenterStr2(str, 0, 1)) == 1;
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_ALARM_IN, -1, com.basic.G.ObjToBytes(mConfig6));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		case 5:
			mConfig7.st_0_vAlarmOutConfigAll[0].st_0_nAlarmOutType = Integer.parseInt(DataUtils.getCenterStr2(str, 0, 1));
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_ALARM_OUT, -1, com.basic.G.ObjToBytes(mConfig7));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		case 10:
			mConfig13.st_0_PrimaryDNS.setIp((byte)192, (byte)168, (byte)0, (byte)1);
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_NET_DNS, -1, com.basic.G.ObjToBytes(mConfig13));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		case 11:
			mConfig14.st_01_HostIP.setIp((byte)10, (byte)10, (byte)34, (byte)36);
			bret = mDevConfig.setConfig(SdkConfigType.E_SDK_CONFIG_SYSNET, -1, com.basic.G.ObjToBytes(mConfig14));
			if(bret) {
				OutputDebug.OutputDebugLogD("DevConfig", "OK");
			}else 
				OutputDebug.OutputDebugLogD("DevConfig", "failed");
			break;
		default:
			break;
		}
	}
	//�����������豸
	private boolean onSearchDev() {
		SearchDeviceInfo[] deviceInfos = new SearchDeviceInfo[100];
		ArrayList<SearchDeviceInfo> searchdevlist = new ArrayList<SearchDeviceInfo>();
		int count = 0;
		count = mNetSdk.SearchDevice(deviceInfos, 100, 8000, true);
		if(count > 0) {
			int i=0;		
			for(i = 0; i < count; ++i) {
				System.out.println("MacAddress:"+deviceInfos[i].HostIP);
				if(deviceInfos[i].HostIP.equals("192.168.1.9"))
				{
					SDK_CONFIG_NET_COMMON_V3 config = new SDK_CONFIG_NET_COMMON_V3();
					com.basic.G.SetValue(config.st_00_HostName, deviceInfos[i].HostName);
					com.basic.G.SetValue(config.st_14_sMac, deviceInfos[i].sMac);
					com.basic.G.SetValue(config.st_15_UserName, "admin");
					com.basic.G.SetValue(config.st_16_Password, "");
					com.basic.G.SetValue(config.st_17_LocalMac, "12:44:55:66:77:88");
					config.st_18_nPasswordType = 1;
					config.st_01_HostIP.st_0_ip[0] = (byte)192;
					config.st_01_HostIP.st_0_ip[1] = (byte)168;
					config.st_01_HostIP.st_0_ip[2] = (byte)1;
					config.st_01_HostIP.st_0_ip[3] = (byte)2;
					
					config.st_03_Gateway.st_0_ip[0] = (byte)192;
					config.st_03_Gateway.st_0_ip[1] = (byte)168;
					config.st_03_Gateway.st_0_ip[2] = (byte)1;
					config.st_03_Gateway.st_0_ip[3] = (byte)1;
					
					config.st_02_Submask.st_0_ip[0] = (byte)255;
					config.st_02_Submask.st_0_ip[1] = (byte)255;
					config.st_02_Submask.st_0_ip[2] = (byte)255;
					config.st_02_Submask.st_0_ip[3] = (byte)0;
					
					config.st_04_HttpPort = deviceInfos[i].HttpPort;
					config.st_05_TCPPort = deviceInfos[i].TCPPort;
					config.st_06_SSLPort = deviceInfos[i].SSLPort;
					config.st_07_UDPPort = deviceInfos[i].UDPPort;
					config.st_08_MaxConn = deviceInfos[i].MaxConn;
					config.st_09_MonMode = deviceInfos[i].MonMode;
					config.st_10_MaxBps = deviceInfos[i].MaxBps;
					config.st_11_TransferPlan = deviceInfos[i].TransferPlan;
					config.st_12_bUseHSDownLoad = deviceInfos[i].bUseHSDownLoad;
					mNetSdk.SetConfigOverNet(SdkConfigType.E_SDK_CONFIG_SYSNET, -1,com.basic.G.ObjToBytes(config),5000);
					OutputDebug.OutputDebugLogD("DevConfig", "error:"+mNetSdk.GetLastError());
				}
			}
			return true;
		}
		return false;
	}
	public void onSave(View v) {
		String str = mViewHolder.setConfigEt.getText().toString();
		onSetConfig(str);
		
	}
	class ViewHolder {
		TextView getConfigTv;
		EditText setConfigEt;
		Spinner select_sp;
	}
}