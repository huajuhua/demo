package com.example.netsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SDK_RECORDCONFIG;

public class RecordConfig extends Activity implements OnClickListener{
	private static final String MYLOG = "RecordConfig";
	private NetSdk mNetSdk;
	private ViewHolder mViewHolder;
	private SDK_RECORDCONFIG mRecordConfig;
	private long mLoginId;								//登陆ID
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordconfig);
		init();
		initLayout() ;
		initData();
	}
	private void init() {
		mNetSdk = NetSdk.getInstance();
	}
	private void initLayout() {
		mViewHolder = new ViewHolder();
		mViewHolder.yulu_tv = (TextView) findViewById(R.id.yulu_tv);
		mViewHolder.changdu_tv = (TextView) findViewById(R.id.changdu_tv);
		mViewHolder.yulu_sb = (SeekBar) findViewById(R.id.yulu_sb);
		mViewHolder.yulu_sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {	
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub		
			}	
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub		
			}	
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				mViewHolder.yulu_tv.setText(arg1 + "Sec");
				mRecordConfig.iPreRecord=arg1;
			}
		});
		mViewHolder.changdu_sb = (SeekBar) findViewById(R.id.changdu_sb);
		mViewHolder.changdu_sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {	
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub			
			}		
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				mViewHolder.changdu_tv.setText(arg1 + "Min");
				mRecordConfig.iPacketLength=arg1;
			}
		});
		mViewHolder.recordmode_rg = (RadioGroup) findViewById(R.id.recordmode_rg);
		mViewHolder.manual_rb = (RadioButton) findViewById(R.id.manual_rb);
		mViewHolder.close_rb = (RadioButton) findViewById(R.id.close_rb);
		mViewHolder.none_rb = (RadioButton) findViewById(R.id.none_rb);
		mViewHolder.regular_cb = (CheckBox) findViewById(R.id.regular);
		mViewHolder.detect_cb = (CheckBox) findViewById(R.id.detect);
		mViewHolder.alarm_cb = (CheckBox) findViewById(R.id.alarm);
		mViewHolder.recordmode_rg.setOnCheckedChangeListener(myOnCheckedChangeLs);
		mViewHolder.ok_btn = (Button) findViewById(R.id.ok);
		mViewHolder.ok_btn.setOnClickListener(this);
		mViewHolder.cancel_btn = (Button) findViewById(R.id.cancel);
		mViewHolder.cancel_btn.setOnClickListener(this);
	} 
	private void initData() {
		Intent it = getIntent();
		mLoginId = it.getLongExtra("LoginId", 0);
		mRecordConfig = new SDK_RECORDCONFIG();
		if(mLoginId != 0) {
			if(mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD, 0, mRecordConfig, 5000)) {
				mViewHolder.yulu_tv.setText(mRecordConfig.iPreRecord + "Sec");
				mViewHolder.changdu_tv.setText(mRecordConfig.iPacketLength + "Min");
				mViewHolder.yulu_sb.setProgress(mRecordConfig.iPreRecord);
				mViewHolder.changdu_sb.setProgress(mRecordConfig.iPacketLength);
				if(mRecordConfig.iRecordMode == 1) {
					mViewHolder.manual_rb.setChecked(true);
				}else if(mRecordConfig.iRecordMode == 0) {
					mViewHolder.close_rb.setChecked(true);
				}
					
			}
		}
	}
	OnCheckedChangeListener myOnCheckedChangeLs = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			if(arg1==mViewHolder.manual_rb.getId()){
				mRecordConfig.iRecordMode=1;
			}
			else if(arg1==mViewHolder.close_rb.getId())
				mRecordConfig.iRecordMode=0;
			else if(arg1==mViewHolder.none_rb.getId())
				mRecordConfig.iRecordMode=2;
				
		}
		
	};
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.ok://保存
			if(onSave())
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
			break;
		case R.id.cancel://取消
			finish();
			break;
		default:
			break;
		}
	
	}
	private boolean onSave() {
		boolean bret = mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD,
				0, mRecordConfig, 1000);
		return bret;
	}
	class ViewHolder {
		TextView yulu_tv;
		TextView changdu_tv;
		SeekBar yulu_sb;
		SeekBar changdu_sb;
		RadioGroup recordmode_rg;
		RadioButton manual_rb;
		RadioButton close_rb;
		RadioButton none_rb ;
		CheckBox regular_cb;
		CheckBox detect_cb;
		CheckBox alarm_cb;
		Button ok_btn;
		Button cancel_btn;
	}
	
}
