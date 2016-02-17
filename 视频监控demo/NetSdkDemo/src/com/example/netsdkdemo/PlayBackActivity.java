package com.example.netsdkdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.xm.FileData;
import com.xm.FindInfo;
import com.xm.MyConfig;
import com.xm.MyConfig.AudioState;
import com.xm.MyConfig.FileType;
import com.xm.MyConfig.PlayState;
import com.xm.NetSdk;
import com.xm.NetSdk.OnRPlayBackCompletedListener;
import com.xm.audio.AudioParam;
import com.xm.audio.VoiceIntercom;
import com.xm.video.MySurfaceView;

/**
 * @author huangwanshui
 * @project NetSdkDemo
 * @date 2013-11-1 ����05:13:11
 * @describe ¼��ط�
 */
public class PlayBackActivity extends Activity {
    private MySurfaceView mPlayBackVV;
    private MySurfaceView mPlayBackVV2;
    private Button mSearchBtn;
    private Button mStopBtn;
    private Button mPauseBtn;
    private Button mResumeBtn;
    private Button mFPlayBtn;
    private Button mSPlayBtn;
    private Button mStopDlBtn;
    private ListView mFileList;
    private NetSdk mNetSdk;
    private FileData[][] mFileData;
    private long mLoginId;
    private long mlPlayHandle;
    private long mlPlayHandle2;
    private long mlDownLoadHandle;
    private VoiceIntercom mVoiceIntercom = null;//�����Խ�
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback);
        init();
        mFileData = new FileData[2][50];
    }

    private void init() {
        Intent it = getIntent();
        mLoginId = it.getLongExtra("loginId", 0);
        mNetSdk = NetSdk.getInstance();
        /**
         * ���ûطŽ�ȼ���
         */
        mNetSdk.setOnRPlayBackCompletedListener(new OnRPlayBackCompletedListener() {

            @Override
            public void onRPlayBackCompleted(long lPlayHandle, long lTotalSize,
                                             long lDownLoadSize, long dwUser) {
                // TODO Auto-generated method stub
                if (lPlayHandle == mlPlayHandle) {
                    System.out.println("lDownLoadSize��" + lDownLoadSize);
                } else if (lPlayHandle == mlDownLoadHandle) {
                    System.out.println("�ļ��ܴ�СΪ:" + lTotalSize + " Ŀǰ�Ѿ�����:" + lDownLoadSize);
                }
            }
        });
        mPlayBackVV = (MySurfaceView) findViewById(R.id.playback_vv);
        mPlayBackVV2 = (MySurfaceView) findViewById(R.id.playback_vv2);
        mPlayBackVV.init(getApplicationContext(), 0);
        mPlayBackVV.setAudioCtrl(AudioState.CLOSED);
        mPlayBackVV2.init(getApplicationContext(), 1);
        mPlayBackVV2.setAudioCtrl(AudioState.CLOSED);
        //�����Խ���ʼ��
        AudioParam audioParam = getAudioParam();
        mVoiceIntercom = new VoiceIntercom(myHandler, audioParam, PlayBackActivity.this);
        mSearchBtn = (Button) findViewById(R.id.search);
        mStopBtn = (Button) findViewById(R.id.stop);
        mPauseBtn = (Button) findViewById(R.id.pause);
        mFPlayBtn = (Button) findViewById(R.id.kf);
        mSPlayBtn = (Button) findViewById(R.id.mf);
        mStopDlBtn = (Button) findViewById(R.id.stopdl);
        //��ͣ�ط�
        mPauseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mlPlayHandle > 0) {
                    mNetSdk.PlayBackControl(mlPlayHandle, MyConfig.PlayBackAction.SDK_PLAY_BACK_PAUSE, 0);
                    mPlayBackVV.onPause();
                }
            }
        });
        mResumeBtn = (Button) findViewById(R.id.resume);
        /*
		 * �ָ��ط�
		 */
        mResumeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mlPlayHandle > 0) {
                    mPlayBackVV.onPlay();
                    mNetSdk.PlayBackControl(mlPlayHandle, MyConfig.PlayBackAction.SDK_PLAY_BACK_CONTINUE, 0);
                }
            }
        });
        mFileList = (ListView) findViewById(R.id.filelist);
        /**
         * ��ѯ¼��ط��ļ�
         */
        mSearchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ArrayList<HashMap<String, Object>> list = onSearchFile();
                SimpleAdapter adapter = new SimpleAdapter(PlayBackActivity.this, list, R.layout.filelist,
                        new String[]{"filename"}, new int[]{R.id.filelist_tv});
                mFileList.setAdapter(adapter);
            }
        });
		/*
		 * ����¼���ļ�
		 */
        mFileList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
//				FileData filedata = mFileData[0][arg2];
//				System.out.println("filedata:"+filedata.sFileName);
//				filedata.ch = 0;
//				mlPlayHandle = mNetSdk.PlayBackByName(0,mLoginId,filedata,1);
//				if(mlPlayHandle > 0) {
//					Toast.makeText(getApplicationContext(), "�򿪳ɹ�", Toast.LENGTH_SHORT).show();
//					mPlayBackVV.initData();
//				}else 
//					Toast.makeText(getApplicationContext(), "��ʧ��", Toast.LENGTH_SHORT).show();
            }
        });
        mFileList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
//				FileData filedata = mFileData[0][arg2];
//				System.out.println("filedata:"+filedata.sFileName);
//				filedata.ch = 0;
//				mlDownLoadHandle = mNetSdk.GetFileByName(0,mLoginId,filedata,Environment.getExternalStorageDirectory()+"/DemoFile.h264",1);
//				if(mlDownLoadHandle > 0) {
//					Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
//				}else 
//					Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
                FindInfo findInfo = new FindInfo();
                findInfo.szFileName = "55555.h264";
                findInfo.nChannelN0 = 0;
                findInfo.nFileType = FileType.SDK_RECORD_ALL;
                findInfo.startTime.year = 2014;
                findInfo.startTime.month = 1;
                findInfo.startTime.day = 16;
                findInfo.startTime.hour = 9;
                findInfo.startTime.minute = 0;
                findInfo.startTime.second = 0;
                findInfo.endTime.year = 2014;
                findInfo.endTime.month = 1;
                findInfo.endTime.day = 16;
                findInfo.endTime.hour = 10;
                findInfo.endTime.minute = 00;
                findInfo.endTime.second = 0;
                File file = new File(Environment.getExternalStorageDirectory() + "/");
                if (file.exists())
                    Toast.makeText(getApplicationContext(), "�����ɹ�", Toast.LENGTH_SHORT).show();
                mlDownLoadHandle = mNetSdk.GetFileByTime(0, mLoginId, findInfo, file.getAbsolutePath(), 1);
                if (mlDownLoadHandle > 0) {
                    Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
		/*
		 * ֹͣ�ط�
		 */
        mStopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean ret = mNetSdk.StopPlayBack(mlPlayHandle);
                if (ret) {
                    mPlayBackVV.onStop();
                }
            }
        });
        /**
         * ���
         */
        mFPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayBackVV.mplaystatus != PlayState.MPS_FAST) {
                    mPlayBackVV.onPlayBackFast();
                    mNetSdk.PlayBackControl(mlPlayHandle, MyConfig.PlayBackAction.SDK_PLAY_BACK_FAST, 2);
                }
            }
        });
        /**
         * ���
         */
        mSPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				if(mPlayBackVV.mplaystatus != PlayState.MPS_SLOW) {
//					mPlayBackVV.onPlayBackSlow();
//					mNetSdk.PlayBackControl(mlPlayHandle, MyConfig.PlayBackAction.SDK_PLAY_BACK_SLOW, 2);
//				}
                mPlayBackVV.OnCapture(getApplicationContext(), Environment.getExternalStorageDirectory() + "/snapshot");
            }
        });
        mStopDlBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mlDownLoadHandle > 0) {
                    mNetSdk.StopGetFile(mlDownLoadHandle);
                }
            }
        });
    }

    /*
     * ����¼���ļ�
     */
    private ArrayList<HashMap<String, Object>> onSearchFile() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int k = 0; k < 1; ++k) {
            FindInfo findInfo = new FindInfo();
            findInfo.nChannelN0 = k;
            findInfo.nFileType = FileType.SDK_RECORD_ALL;
            findInfo.startTime.year = 2014;
            findInfo.startTime.month = 1;
            findInfo.startTime.day = 21;
            findInfo.startTime.hour = 0;
            findInfo.endTime.year = 2014;
            findInfo.endTime.month = 1;
            findInfo.endTime.day = 21;
            findInfo.endTime.hour = 24;
            for (int i = 0; i < 50; ++i)
                mFileData[k][i] = new FileData();
            System.out.println("mLoginId:" + mLoginId);
            int count = mNetSdk.FindFile(mLoginId, findInfo, mFileData[k], 50, 5000);
            for (int j = 0; j < count; ++j) {
                if (count < 50) {
                    System.out.println("filename:" + mFileData[k][j].sFileName);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("filename", mFileData[k][j].sFileName);
                    list.add(map);
                } else
                    break;
            }
        }
        return list;
    }

    /*
     * ���PCM��Ƶ��ݲ���
     */
    public AudioParam getAudioParam() {
        AudioParam audioParam = new AudioParam();
        audioParam.mFrequency = 8000;
        audioParam.mChannel = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;
        return audioParam;
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }

    };
}
