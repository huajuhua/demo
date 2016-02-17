package com.example.netsdkdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xm.CONFIG_EncodeAbility;
import com.xm.MyConfig;
import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SDK_CONFIG_ENCODE_SIMPLIIFY;

public class EncodeConfig extends Activity implements OnClickListener {
    private ViewHolder mViewHolder;
    private PopupWindow mresolution_pw;        //????????
    private PopupWindow mfps_pw;            //??????
    private PopupWindow mquality_pw;        //???????
    private SDK_CONFIG_ENCODE_SIMPLIIFY[] mEncodeInfos;    //???????
    private CONFIG_EncodeAbility mEncodeAbility;        //??????????
    private List<Integer> mresolutions;        //???????????
    private NetSdk mNetSdk;
    private long mLoginId;
    private boolean mbaudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encodeconfig);
        initData();
        initLayout();
        getEncodeConfig();
    }

    private void initLayout() {
        mViewHolder = new ViewHolder();
        mViewHolder.audio_rl = (RelativeLayout) findViewById(R.id.audio_rl);
        mViewHolder.audio_rl.setOnClickListener(this);
        mViewHolder.quality_rl = (RelativeLayout) findViewById(R.id.quality_rl);
        mViewHolder.quality_rl.setOnClickListener(this);
        mViewHolder.fps_rl = (RelativeLayout) findViewById(R.id.fps_rl);
        mViewHolder.fps_rl.setOnClickListener(this);
        mViewHolder.resolution_rl = (RelativeLayout) findViewById(R.id.resolution_rl);
        mViewHolder.resolution_rl.setOnClickListener(this);
        mViewHolder.resolution_tv = (TextView) findViewById(R.id.resolution_tv);
        mViewHolder.fps_tv = (TextView) findViewById(R.id.fps_tv);
        mViewHolder.quality_tv = (TextView) findViewById(R.id.quality_tv);
        mViewHolder.audio_tv = (TextView) findViewById(R.id.audio_tv);
        mViewHolder.audio_iv = (ImageView) findViewById(R.id.audio_iv);
        mViewHolder.ok = (Button) findViewById(R.id.ok);
        mViewHolder.ok.setOnClickListener(this);
        mViewHolder.cancel = (Button) findViewById(R.id.cancel);
        mViewHolder.cancel.setOnClickListener(this);
        ListView resolution_lv = new ListView(getApplicationContext());
        ArrayList<HashMap<String, String>> resolution_data = new ArrayList<HashMap<String, String>>();
        int i = 0;
        for (i = 0; i < mresolutions.size(); ++i) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("item", MyConfig.Capture_Size[mresolutions.get(i)]);
            resolution_data.add(map);
        }
        SimpleAdapter adpater = new SimpleAdapter(getApplicationContext(), resolution_data,
                R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
        resolution_lv.setAdapter(adpater);
        //???????
        resolution_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                mEncodeInfos[0].dstMainFmt.vfFormat.iResolution = mresolutions.get(arg2);
                mViewHolder.resolution_tv.setText(MyConfig.Capture_Size[mresolutions.get(arg2)]);
                mresolution_pw.dismiss();
            }
        });
        mresolution_pw = new PopupWindow(resolution_lv, 300, 300);
        mresolution_pw.setBackgroundDrawable(new BitmapDrawable());
        mresolution_pw.setFocusable(true);
        mresolution_pw.setOutsideTouchable(true);
        ArrayList<HashMap<String, String>> fps_data = new ArrayList<HashMap<String, String>>();
        for (i = 0; i < MyConfig.FPS.length; ++i) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("item", MyConfig.FPS[i]);
            fps_data.add(map);
        }
        ListView fps_lv = new ListView(getApplicationContext());
        fps_lv.setFocusable(true);
        adpater = new SimpleAdapter(getApplicationContext(), fps_data,
                R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
        fps_lv.setAdapter(adpater);
        fps_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                mEncodeInfos[0].dstMainFmt.vfFormat.nFPS = arg2 + 1;
                mViewHolder.fps_tv.setText("" + mEncodeInfos[0].dstMainFmt.vfFormat.nFPS);
                mfps_pw.dismiss();
            }
        });
        mfps_pw = new PopupWindow(fps_lv, 300, 300);
        mfps_pw.setBackgroundDrawable(new BitmapDrawable());
        mfps_pw.setFocusable(true);
        mfps_pw.setOutsideTouchable(true);
        ArrayList<HashMap<String, String>> quality_data = new ArrayList<HashMap<String, String>>();
        for (i = 0; i < MyConfig.QUALITY.length; ++i) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("item", MyConfig.QUALITY[i]);
            quality_data.add(map);
        }
        ListView quality_lv = new ListView(getApplicationContext());
        adpater = new SimpleAdapter(getApplicationContext(), quality_data,
                R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
        quality_lv.setAdapter(adpater);
        quality_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                mEncodeInfos[0].dstMainFmt.vfFormat.iQuality = arg2 + 1;
                mViewHolder.quality_tv.setText(MyConfig.QUALITY[arg2]);
                mquality_pw.dismiss();
            }
        });
        mquality_pw = new PopupWindow(quality_lv, 300, 300);
        mquality_pw.setBackgroundDrawable(new BitmapDrawable());
        mquality_pw.setFocusable(true);
        mquality_pw.setOutsideTouchable(true);
    }

    private void initData() {
        Intent it = getIntent();
        mLoginId = it.getLongExtra("LoginId", 0);
        mNetSdk = NetSdk.getInstance();
        if (mLoginId <= 0)
            return;
        mEncodeInfos = new SDK_CONFIG_ENCODE_SIMPLIIFY[32];
        for (int i = 0; i < 32; ++i) {
            mEncodeInfos[i] = new SDK_CONFIG_ENCODE_SIMPLIIFY();
        }
        mEncodeAbility = new CONFIG_EncodeAbility();
        //?????????????
        if (mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_ABILTY_ENCODE, -1, mEncodeAbility, 5000)) {
            System.out.println("ImageSizePerChannel:" + mEncodeAbility.ImageSizePerChannel[0]);
        }
        mresolutions = new ArrayList<Integer>();
        //????????????
        int resolution_mask = mEncodeAbility.ImageSizePerChannel[0];
        int resolution_pos = 0;
        //?????????????????????
        do {
            if ((resolution_mask & 1) == 1) {
                mresolutions.add(resolution_pos);
            }
            resolution_pos++;
        } while ((resolution_mask = (resolution_mask >> 1)) > 0);
    }

    //????????????
    private void getEncodeConfig() {
        if (mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_SYSENCODE_SIMPLIIFY, -1, mEncodeInfos, 5000)) {
            System.out.println("resolution:" + mEncodeInfos[0].dstMainFmt.vfFormat.iResolution);
            if (mEncodeInfos[0].dstMainFmt.vfFormat.iResolution >= 0)
                mViewHolder.resolution_tv.setText("" + MyConfig.Capture_Size[mEncodeInfos[0].dstMainFmt.vfFormat.iResolution]);
            mViewHolder.fps_tv.setText("" + mEncodeInfos[0].dstMainFmt.vfFormat.nFPS);
            mViewHolder.quality_tv.setText("" + MyConfig.QUALITY[mEncodeInfos[0].dstMainFmt.vfFormat.iQuality - 1]);
            mbaudio = mEncodeInfos[0].dstMainFmt.bAudioEnable;
            if (mbaudio)
                mViewHolder.audio_iv.setImageResource(R.drawable.autologin_on);
            else
                mViewHolder.audio_iv.setImageResource(R.drawable.autologin_off);

        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.resolution_rl:
                mresolution_pw.showAsDropDown(mViewHolder.resolution_rl);
                break;
            case R.id.fps_rl:
                mfps_pw.showAsDropDown(mViewHolder.fps_rl);
                break;
            case R.id.quality_rl:
                mquality_pw.showAsDropDown(mViewHolder.quality_rl);
                break;
            case R.id.audio_rl:
                if (mbaudio) {
                    mViewHolder.audio_iv.setImageResource(R.drawable.autologin_off);
                } else {
                    mViewHolder.audio_iv.setImageResource(R.drawable.autologin_on);
                }
                mbaudio = !mbaudio;
                mEncodeInfos[0].dstMainFmt.bAudioEnable = mbaudio;
                break;
            case R.id.ok://????
                if (onSave())
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "???????", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel://???
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (mresolution_pw.isShowing())
            mresolution_pw.dismiss();
        if (mfps_pw.isShowing())
            mfps_pw.dismiss();
        if (mquality_pw.isShowing())
            mquality_pw.dismiss();
        return super.onTouchEvent(event);
    }

    private boolean onSave() {
        boolean bret = mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_SYSENCODE_SIMPLIIFY,
                -1, mEncodeInfos, 1000);
        return bret;
    }

    class ViewHolder {
        RelativeLayout resolution_rl;
        RelativeLayout fps_rl;
        RelativeLayout quality_rl;
        RelativeLayout audio_rl;
        TextView resolution_tv;
        TextView fps_tv;
        TextView quality_tv;
        TextView audio_tv;
        ImageView audio_iv;
        Button ok;
        Button cancel;
    }

}
