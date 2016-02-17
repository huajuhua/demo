package com.example.netsdkdemo;

import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

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

/**
 * @author huangwanshui
 * @project NetSdkDemo
 * @date 2014-1-1 ����01:51:35
 * @describe Setting
 */
public class SettingActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        videocolors = new SDK_VIDEOCOLOR[2];
        encodeInfos = new SDK_CONFIG_ENCODE_SIMPLIIFY[32];
        netDHCPCfg = new SDK_NetDHCPConfig[4];
        recordConfig = new SDK_RECORDCONFIG();
        initLayout();
        initData();
    }

    /*
     * ��ʼ�����
     */
    private void initData() {
        Intent it = getIntent();
        mLoginId = it.getLongExtra("loginId", 0);
        mNetSdk = NetSdk.getInstance();
        int i = 0;
        for (i = 0; i < 2; ++i) {
            videocolors[i] = new SDK_VIDEOCOLOR();
        }
        for (i = 0; i < 32; ++i) {
            encodeInfos[i] = new SDK_CONFIG_ENCODE_SIMPLIIFY();
        }
        for (i = 0; i < 4; ++i) {
            netDHCPCfg[i] = new SDK_NetDHCPConfig();
        }
        if (mNetSdk.H264DVRGetDevConfig(mLoginId, 84, 0, videocolors, 1000)) {
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
        encodeInfo = (TextView) findViewById(R.id.encodeinfo);
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
                if (mNetSdk.H264DVRSetDevConfig(mLoginId, 84, 0, videocolors, 1000)) {
                    System.out.println("ok");
                }
                break;
            case R.id.getEncode:
                if (mNetSdk.H264DVRGetDevConfig(mLoginId, 68, -1, encodeInfos, 1000)) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("�ֱ���:" + MyConfig.Capture_Size[encodeInfos[0].dstMainFmt.vfFormat.iResolution]);
                    sb.append("֡��:" + encodeInfos[0].dstMainFmt.vfFormat.nFPS);
                    encodeInfo.setText(sb.toString());
                }
                break;
            case R.id.setEncode:
                if (encodeInfos[0] == null)
                    break;
                encodeInfos[0].dstMainFmt.vfFormat.nFPS = 20;
                if (mNetSdk.H264DVRSetDevConfig(mLoginId, 68, -1, encodeInfos, 1000)) {
                    System.out.println("SetEncode OK");
                }
                break;
            case R.id.getDHCP:
                if (mNetSdk.H264DVRGetDevConfig(mLoginId, 37, -1, netDHCPCfg, 1000)) {
                    String str = new String(netDHCPCfg[0].ifName);
                    DHCPInfo.setText(str);
                }
                break;
            case R.id.getRecordTime:
                if (recordConfig == null)
                    break;
                if (mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD, 0, recordConfig, 5000)) {
                    RecordTimeInfo.setText("time:" + recordConfig.tsSchedule[0][0].startHour);
                    System.out.println("dddddd:" + recordConfig.iRecordMode);
                }
                break;
            case R.id.setrecordtime:
                if (recordConfig == null)
                    break;
                recordConfig.iPacketLength = 2;
                recordConfig.iRecordMode = 2;
            /*�������0��ʼ��1ά�����ʾ���ڣ���ά�����ʾʱ���*/
                recordConfig.tsSchedule[0][0].startHour = 12;
                if (mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_RECORD, 0, recordConfig, 5000)) {
                    Toast.makeText(SettingActivity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(SettingActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
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
}	
