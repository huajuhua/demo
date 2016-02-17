/**
 * NetSdkDemo
 * SerialActivity.java
 * Administrator
 * TODO
 * 2014-10-30
 */
package com.example.netsdkdemo;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xm.NetSdk;
import com.xm.NetSdk.OnTransComLisenter;
import com.xm.TransComChannel;
import com.xm.utils.OutputDebug;

/**
 * NetSdkDemo
 * SerialActivity.java
 * @author huangwanshui
 * TODO
 * 2014-10-30
 */
public class SerialActivity extends Activity {
	private EditText mReadEt;
	private EditText mWriteEt;
	private NetSdk mNetSdk;
	private long mLoginId;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serial);
		initLayout();
		initData();
	}
	private void initLayout() {
		mReadEt = (EditText) findViewById(R.id.read_et);
		mWriteEt = (EditText) findViewById(R.id.write_et);
	}
	private void initData() {
		mNetSdk = NetSdk.getInstance();
		Intent it = getIntent();
		mLoginId = it.getLongExtra("loginId", 0);
		mNetSdk.setOnTransComLisenter(new OnTransComLisenter() {
			
			@Override
			public void onTransCom(long lLoginID, long lTransComType, byte[] pBuffer,
					long dwBufSize, long dwUser) {
				// TODO Auto-generated method stub
//				Toast.makeText(SerialActivity.this, "loginid:"+lLoginID, Toast.LENGTH_SHORT).show();
				String str = null;
				try {
					str = new String(pBuffer,"GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				OutputDebug.OutputDebugLogD("SerialActivity", "buf:"+str);
			}
		});
	}
	public void onSend(View v) {
		byte[] buf = mWriteEt.getText().toString().getBytes();
		boolean bret = mNetSdk.SerialWrite(mLoginId, 0, buf, buf.length);
		if(bret) {
			Toast.makeText(SerialActivity.this, "send ok", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(SerialActivity.this, "send failed", Toast.LENGTH_SHORT).show();
		}
	}
	public void onRev(View v) {
		byte[] buf = new byte[1024];
		int[] readlen = new int[1];
		boolean bret = mNetSdk.SerialRead(mLoginId, 0, buf, buf.length,readlen);
		if(bret) {
			mReadEt.setText(new String(buf));
			Toast.makeText(SerialActivity.this, "rev ok", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(SerialActivity.this, "rev failed", Toast.LENGTH_SHORT).show();
		}
	}
	public void onOpen(View v) {
		TransComChannel tcc = new TransComChannel();
		tcc.baudrate = 8;
		tcc.databits = 1;
		tcc.stopbits = 0;
		tcc.parity = 9600;
		boolean bret = mNetSdk.OpenTransComChannel(mLoginId, tcc, 0);
		OutputDebug.OutputDebugLogD("SerialActivity", "bret:"+bret);
	}
}
