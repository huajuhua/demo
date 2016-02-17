package com.example.netsdkdemo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;
public class MyWifiManager {

	private static final String TAG = "[WifiAdmin]";
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiList = null;
	private List<WifiConfiguration> mWifiConfiguration;
	private WifiLock mWifiLock;
	private DhcpInfo dhcpInfo;
	private ConnectivityManager connManager;
	public MyWifiManager(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}
	//打开wifi
	public boolean openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			//Log.i(TAG, "setWifiEnabled.....");
			mWifiManager.setWifiEnabled(true);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Log.i(TAG, "setWifiEnabled.....end");
		}
		return mWifiManager.isWifiEnabled();
	}
	//关闭wifi
	public void closeWifi() { if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}
	//获取wifi状态
	public int checkState() {
		return mWifiManager.getWifiState();
	}
	//锁定wifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}
	//解锁wifiLock
	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}
	//粗行间wiif锁
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}
	//得到配置好的网络
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}
	//指定配置好的网络进行连接
	public void connectConfiguration(int index) {
		if (index > mWifiConfiguration.size()) {
			return;
			
		}
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}
	//wifi扫描 type:0-搜索所有的wifi热点，1-搜索ipc设备的wifi；2-搜索ipc以外的设备wifi;
	public void startScan(int type) {
		boolean scan = mWifiManager.startScan();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		//Log.i(TAG, "startScan result:" + scan);
		List<ScanResult> wifiList = mWifiManager.getScanResults();
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
		mWifiList = new ArrayList<ScanResult>();
		if (mWifiList != null && wifiList != null) {
//			//Log.i(TAG, "startScan result:" + wifiList.size());
			if(type == 1) {//搜索ipc设备的wifi
				for (ScanResult result : wifiList) {
					if (isXMDeviceWifi(result.SSID)) {
						mWifiList.add(result);
					}
				}
			}else if(type == 2) {//搜索ipc以外的设备wifi
				for (ScanResult result : wifiList) {
					if (!isXMDeviceWifi(result.SSID)) {
						mWifiList.add(result);
					}
				}
			}else {
				mWifiList = (List<ScanResult>) wifiList;
			}
		}
	}
	public ScanResult getCurScanResult(String SSID) {
		boolean scan = mWifiManager.startScan();
//		//Log.i(TAG, "startScan result:" + scan);
		List<ScanResult> wifiList = mWifiManager.getScanResults();
		ScanResult scanResult = null;
		if (wifiList != null && SSID != null) {
			for (ScanResult result : wifiList) {
				if (result.SSID.contains(SSID)) {
					scanResult = result;
					return scanResult;
				}
			}
		}
		return scanResult;
	}
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}
	
	public boolean isWifiConnect() { 
		NetworkInfo ni = connManager.getActiveNetworkInfo();
		return (ni != null && ni.isConnectedOrConnecting());
	 }
	// 查看扫描结果
	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}
	public String getSSID() {
		if(mWifiInfo == null)
			return null;
		String ssid = mWifiInfo.getSSID();
		if(ssid != null && ssid.startsWith("\""))
			ssid = ssid.substring(1, ssid.length() - 1);
		return ssid;
	}
	public DhcpInfo getDhcpInfo() {
		return dhcpInfo = mWifiManager.getDhcpInfo();
	}

	public String getIPAddress() {
		int ipAddress = 0;
		if(mWifiInfo != null)
			ipAddress = mWifiInfo.getIpAddress();
		return (ipAddress == 0) ? null :
			((ipAddress & 0xff) + "." +
		     (ipAddress >>8 & 0xff) + "." +
			 (ipAddress >> 16 & 0xff) + "." +
		     (ipAddress >>24 & 0xff));
	}

	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}
	public int getLinkSpeed() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getLinkSpeed();
	}
	public WifiInfo getWifiInfo() {
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo;
	}
	// 添加一个网络配置并连接 &nbsp;
	public boolean addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean benable = mWifiManager.enableNetwork(wcgID, true);
		 System.out.println("wcgID--" + wcgID);  
		 System.out.println("b--" + benable);  
		return benable;
	}
	public boolean enableNetwork(String ssid) {
		boolean benable = false;
		WifiConfiguration wcg = IsExsits(ssid);
		if(wcg != null)
			benable = addNetwork(wcg);
		return benable;
	}
	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
	public boolean disconnect(){
		boolean benable=mWifiManager.disconnect();
		System.out.println("disconnect--" + benable);  
		return benable;		
	}
	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
//		//Log.i(TAG, "SSID:" + SSID + ",password:" + Password);
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
//			mWifiManager.enableNetwork(tempConfig.networkId, false);
			return tempConfig;
		} else {
			//Log.i(TAG, "IsExsits is null.");
		}

		if (Type == 1) // WIFICIPHER_NOPASS
		{
//			//Log.i(TAG, "Type =1.");
			 config.wepKeys[0] = "\"" + "\"";  
			config.status=WifiConfiguration.Status.ENABLED;
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
//			//Log.i(TAG, "Type =2.");
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";//\"转义字符，代表" 
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{

			//Log.i(TAG, "Type =3.");
			config.preSharedKey = "\"" + Password + "\"";

			config.hiddenSSID = false;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}
	/**
	 * 移除连接过的XM设备wifi热点
	 * @param ssid wifi热点
	 * @return
	 */
	public boolean onRemoveNetWork(String ssid) {
		WifiConfiguration tempConfig = this.IsExsits(ssid);
		if (tempConfig != null && isXMDeviceWifi(ssid)) {
			mWifiManager.removeNetwork(tempConfig.networkId);
			return true;
		} else {
			return false;
		}
	}
	// 查看以前是否已经配置过该SSID &nbsp;
	private WifiConfiguration IsExsits(String SSID) {
		String ssid = "\""+SSID+"\"";
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals(ssid) || existingConfig.SSID.equals(SSID)) {
				return existingConfig;
			}
		}
		return null;
	}
	//获取已经连接上的wifi信息
	public String getConfiguredNetwork() {
		return mWifiManager == null ? null:mWifiManager.getConnectionInfo().getSSID();
	}
	/**
	 * 判断是不是xm设备的wifi热点
	 * @param ssid
	 * @return
	 */
	public static boolean isXMDeviceWifi(String ssid) {
		if(ssid == null)
			 return false;
		if(ssid.startsWith("robot_") || ssid.startsWith("robot_", 1)
				|| ssid.startsWith("Robot_") || ssid.startsWith("Robot_", 1)
				|| ssid.startsWith("card_") || ssid.startsWith("card_",1)
				|| ssid.startsWith("car_") || ssid.startsWith("car_",1)
				|| ssid.startsWith("seye_") || ssid.startsWith("seye_",1)
				|| ssid.startsWith("NVR_") || ssid.startsWith("NVR_",1)
				|| ssid.startsWith("DVR_") || ssid.startsWith("DVR_",1)
				|| ssid.startsWith("beye_") || ssid.startsWith("beye_",1)
				|| ssid.startsWith("IPC_") || ssid.startsWith("IPC_", 1)
				|| ssid.startsWith("Car") || ssid.startsWith("Car", 1)
				|| ssid.startsWith("BOB_") || ssid.startsWith("BOB_",1))
			return true;
		else 
			return false;
	}
}