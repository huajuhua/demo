package com.example.netsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SDK_RECORDCONFIG;

public class RecordConfig extends Activity {
    private static final String MYLOG = "RecordConfig";
    private NetSdk mNetSdk;
    private ViewHolder mViewHolder;
    private SDK_RECORDCONFIG mRecordConfig;
    private long mLoginId;                                //��½ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordconfig);
        init();
        initLayout();
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
            }
        });
        mViewHolder.recordmode_rg = (RadioGroup) findViewById(R.id.recordmode_rg);
        mViewHolder.manual_rb = (RadioButton) findViewById(R.id.manual_rb);
        mViewHolder.close_rb = (RadioButton) findViewById(R.id.close_rb);
        mViewHolder.regular_cb = (CheckBox) findViewById(R.id.regular);
        mViewHolder.detect_cb = (CheckBox) findViewById(R.id.detect);
        mViewHolder.alarm_cb = (CheckBox) findViewById(R.id.alarm);
        mViewHolder.recordmode_rg.setOnCheckedChangeListener(myOnCheckedChangeLs);
    }

    private void initData() {
        Intent it = getIntent();
        mLoginId = it.getLongExtra("LoginId", 0);
        mRecordConfig = new SDK_RECORDCONFIG();
        if (mLoginId > 0) {
            if (mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD, 0, mRecordConfig, 5000)) {
                mViewHolder.yulu_tv.setText(mRecordConfig.iPreRecord + "Sec");
                mViewHolder.changdu_tv.setText(mRecordConfig.iPacketLength + "Min");
                mViewHolder.yulu_sb.setProgress(mRecordConfig.iPreRecord);
                mViewHolder.changdu_sb.setProgress(mRecordConfig.iPacketLength);
                if (mRecordConfig.iRecordMode == 1) {
                    mViewHolder.manual_rb.setChecked(true);
                } else if (mRecordConfig.iRecordMode == 0) {
                    mViewHolder.close_rb.setChecked(true);
                }

            }
        }
    }

    OnCheckedChangeListener myOnCheckedChangeLs = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            // TODO Auto-generated method stub

        }

    };

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
        CheckBox regular_cb;
        CheckBox detect_cb;
        CheckBox alarm_cb;
    }
}
