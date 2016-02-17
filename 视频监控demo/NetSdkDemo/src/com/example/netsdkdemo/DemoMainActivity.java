package com.example.netsdkdemo;

import java.io.File;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.MyConfig.AudioState;
import com.xm.MyConfig.ConnectMode;
import com.xm.MyConfig.PlayState;
import com.xm.MyConfig.RecordState;
import com.xm.MyConfig.SocketStyle;
import com.xm.MyConfig.VoiceIntercomState;
import com.xm.NetSdk;
import com.xm.NetSdk.OnAlarmListener;
import com.xm.audio.AudioParam;
import com.xm.audio.VoiceIntercom;
import com.xm.video.MySurfaceView;
import com.xm.video.MySurfaceView.OnPTZScrollListener;
import com.xm.video.MySurfaceView.OnPTZStopListener;
import com.xm.view.MyDirection;
import com.xm.view.MyWindowViews;
import com.xm.view.MyWindowViews.MyAddListener;
import com.xm.view.MyWindowViews.MyOneCallBack;

/**
 * @author huangwanshui
 * @project NetSdkDemo
 * @date 2013-11-1 ????05:12:08
 * @describe ????NetSdk??? Demo ???????????????????????????????????
 */
public class DemoMainActivity extends Activity implements OnClickListener {
    private NetSdk mNetSdk = null;
    private Button playbtn = null;//????
    private Button stopbtn = null;//??
    private Button voiceIntercombtn = null;//???????
    private Button playback = null;//?????
    private Button capture = null;//???
    private Button audioctrl = null;//???????
    private Button ianalysis = null;//???????
    private Button setting = null;//????
    private Button startRecord = null;//??????
    private Button stopRecord = null;//?????
    private MyDirection mDirection;//??????
    private Button startservice = null;//????????
    private boolean mbPTZ = false;
    private long mloginid;
    private long[] mplayhandle;
    private VoiceIntercom mVoiceIntercom = null;//???????
    private long mlVoiceHandle = 0;                                                //??????
    private int mbConnectMode = ConnectMode.monitorconn;                        //???????????
    private AlertDialog mAudioRecordDlg = null;                                    //????????????
    private ImageView mCaptureIV = null;
    private int mWndSelected = 0;
    private RelativeLayout mAddDevLayout;//????璞竘ayout
    private TranslateAnimation mShowAnimation;
    private TranslateAnimation mHideAnimation;
    private Button mOk = null;//???
    private Button mCancel = null;//???
    private TextView mSnTV = null;//???泻?
    private TextView mIpTV = null;//ip
    private TextView mPortTV = null;//???
    private TextView mUserNameTV = null;//?????
    private TextView mPassWordTV = null;//????
    private EditText mSnET = null;//???泻?
    private EditText mIpET = null;//ip
    private EditText mPortET = null;//???
    private EditText mUserNameET = null;//?????
    private EditText mPassWordET = null;//????
    private Spinner mLoginTypeSP = null;//???????:???????????????
    private WndsHolder mWndsHolder;
    private int mSocketStyle = SocketStyle.TCPSOCKET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        init();
        initLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /*
     * ?????
     */
    private void init() {
        mNetSdk = NetSdk.getInstance();
        /**
         * ???????
         */
        mNetSdk.setOnAlarmListener(new OnAlarmListener() {

            @Override
            public void onAlarm(long loginid, int ichannel, int iEvent, int iStatus,
                                int[] time) {
                // TODO Auto-generated method stub
                System.out.println("---------------------报警------------");
            }
        });


        playbtn = (Button) findViewById(R.id.play);
        stopbtn = (Button) findViewById(R.id.stop);
        voiceIntercombtn = (Button) findViewById(R.id.voiceIntercom);
        playback = (Button) findViewById(R.id.playback);
        mAddDevLayout = (RelativeLayout) findViewById(R.id.login_set);
        mShowAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        mHideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mShowAnimation.setDuration(500);
        mHideAnimation.setDuration(500);
        mWndsHolder = new WndsHolder();
        mWndsHolder.vv1 = (MySurfaceView) findViewById(R.id.vv1);
        mWndsHolder.vv2 = (MySurfaceView) findViewById(R.id.vv2);
        mWndsHolder.vv3 = (MySurfaceView) findViewById(R.id.vv3);
        mWndsHolder.vv4 = (MySurfaceView) findViewById(R.id.vv4);

        mWndsHolder.vv1.init(DemoMainActivity.this, 0);
        mWndsHolder.vv2.init(DemoMainActivity.this, 1);
        mWndsHolder.vv3.init(DemoMainActivity.this, 2);
        mWndsHolder.vv4.init(DemoMainActivity.this, 3);

        mWndsHolder.vv1.initAdd(new int[]{R.drawable.wnd_add_normal, R.drawable.wnd_add_selected});
        mWndsHolder.vv2.initAdd(new int[]{R.drawable.wnd_add_normal, R.drawable.wnd_add_selected});
        mWndsHolder.vv3.initAdd(new int[]{R.drawable.wnd_add_normal, R.drawable.wnd_add_selected});
        mWndsHolder.vv4.initAdd(new int[]{R.drawable.wnd_add_normal, R.drawable.wnd_add_selected});

        mWndsHolder.vv1.setOneCallBack(myOneCallBack);
        mWndsHolder.vv2.setOneCallBack(myOneCallBack);
        mWndsHolder.vv3.setOneCallBack(myOneCallBack);
        mWndsHolder.vv4.setOneCallBack(myOneCallBack);
        capture = (Button) findViewById(R.id.capture);
        mCaptureIV = (ImageView) findViewById(R.id.catpureiv);
        audioctrl = (Button) findViewById(R.id.audioctrl);
        ianalysis = (Button) findViewById(R.id.ianalysis);
        setting = (Button) findViewById(R.id.setting);
        startRecord = (Button) findViewById(R.id.startrecord);
        stopRecord = (Button) findViewById(R.id.stoprecord);
        startservice = (Button) findViewById(R.id.startservice);
        mWndsHolder.vv1.initRecord(Environment.getExternalStorageDirectory() + "");


        final DevInfo devInfo = new DevInfo();
        devInfo.Ip = "192.168.1.50";
        devInfo.TCPPort = 34567;
        try {
            devInfo.UserName = "admin".getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        devInfo.PassWord = "";
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Stop();
                mloginid = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                mplayhandle = new long[devInfo.ChanNum];
                mNetSdk.SetupAlarmChan(mloginid);
                mNetSdk.SetAlarmMessageCallBack();
                ChnInfo chnInfo = new ChnInfo();
                chnInfo.ChannelNo = 0;
                chnInfo.nStream = 1;//0??????????1????????
                long playhandle = mNetSdk.onRealPlay(0, mloginid, chnInfo);
                mNetSdk.setDataCallback(playhandle);
                mWndsHolder.vv1.initData();
                mWndsHolder.vv1.setAudioCtrl(AudioState.CLOSED);
            }
        }).start();
        if (isAddDevShowing())
            setAddDevShow(false);
        //????????????
        AudioParam audioParam = getAudioParam();
        mVoiceIntercom = new VoiceIntercom(myHandler, audioParam, DemoMainActivity.this);
        mDirection = (MyDirection) findViewById(R.id.direction);
        mDirection.onInit(new int[]{
                R.drawable.up_selected,
                R.drawable.down_selected,
                R.drawable.left_selected,
                R.drawable.right_selected,
                R.drawable.left_up_selected,
                R.drawable.left_down_selected,
                R.drawable.right_up_selected,
                R.drawable.right_down_selected
        });
        mWndsHolder.vv1.setOnPTZScrollListener(new OnPTZScrollListener() {

            @Override
            public void onPTZScroll(int direction) {
                // TODO Auto-generated method stub
                if (direction >= 0 && !mbPTZ) {
                    mNetSdk.PTZControl(mloginid, 0, direction, false, 4, 0, 0);
                    mDirection.setVisibility(View.VISIBLE);
                    mDirection.setDirection(direction);
                    mbPTZ = true;
                }
            }
        });
        mWndsHolder.vv1.setOnPTZStopListener(new OnPTZStopListener() {

            @Override
            public void onPTZStop(int direction) {
                // TODO Auto-generated method stub
                mNetSdk.PTZControl(mloginid, 0, direction, true, 4, 0, 0);
                mDirection.setVisibility(View.INVISIBLE);
                mbPTZ = false;
            }
        });
        playbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//				    	DevInfo devInfo = new DevInfo();
//				    	devInfo.Ip = "10.10.33.74";
//				    	devInfo.TCPPort = 34567;
//				    	devInfo.UserName = "admin";
//				    	devInfo.PassWord = "9";
//				    	mloginid = mNetSdk.onLoginDev(mWndSelected, devInfo, null, SocketStyle.TCPSOCKET);
//				    	
//				    	if(mloginid > 0) {
//				    		mNetSdk.SetupAlarmChan(mloginid);
//				    		mNetSdk.SetAlarmMessageCallBack();
//				    		ChnInfo chnInfo = new ChnInfo();
//				    		chnInfo.ChannelNo = 0;
//				    		mplayhandle = mNetSdk.onRealPlay(mWndSelected, mloginid, chnInfo);
//				    		if(mplayhandle > 0) {
//				    			mNetSdk.setDataCallback(mplayhandle);
//				    			switch (mWndSelected) {
//								case 0:
//									mWndsHolder.vv1.initData();
//									mWndsHolder.vv1.setAudioCtrl(AudioState.CLOSED);
//									break;
//								case 1:
//									mWndsHolder.vv2.initData();
//									break;
//								case 2:
//									mWndsHolder.vv3.initData();
//									break;
//								case 3:
//									mWndsHolder.vv4.initData();
//									break;
//								default:
//									break;
//								}
//				    		}
////				    	}
                    }
                }).start();
            }
        });
        /*
    	 * ??????
    	 */
        stopbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Stop();
            }
        });
    	/*
    	 * ???
    	 */
        voiceIntercombtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initVoiceIntercom();
            }
        });
    	/*
    	 * ?????
    	 */
        playback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent();
                it.putExtra("loginId", mloginid);
                it.setClass(DemoMainActivity.this, PlayBackActivity.class);
                startActivity(it);
            }
        });
    	/*
    	 * ???
    	 */
        capture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String str = Environment.getExternalStorageDirectory() + "/Capture";
                File picture = null;
                switch (mWndSelected) {
                    case 0:
                        picture = mWndsHolder.vv1.OnCapture(getApplicationContext(), str);
                        break;
                    case 1:
                        picture = mWndsHolder.vv2.OnCapture(getApplicationContext(), str);
                        break;
                    case 2:
                        picture = mWndsHolder.vv3.OnCapture(getApplicationContext(), str);
                        break;
                    case 3:
                        picture = mWndsHolder.vv4.OnCapture(getApplicationContext(), str);
                        break;
                    default:
                        break;
                }
                if (picture != null) {
                    onShowPicture(picture.getAbsoluteFile().toString());
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * ???????
         */
        audioctrl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

//				if(!vv.views[mWndSelected].getAudioOpening())
//					vv.views[mWndSelected].setAudioCtrl(true);
//				else
//					vv.views[mWndSelected].setAudioCtrl(false);
            }
        });
    	/*
    	 * ???????
    	 */
        ianalysis.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent();
                it.setClass(DemoMainActivity.this, IanalysisActivity.class);
                startActivity(it);
            }
        });
        setting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent();
                it.putExtra("LoginId", mloginid);
                it.setClass(DemoMainActivity.this, DevSettingActivity.class);
                startActivity(it);
            }
        });
        startRecord.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                File videoFile = mWndsHolder.vv1.onStartRecord();
                if (videoFile != null) {
                    Toast.makeText(DemoMainActivity.this, "??????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        stopRecord.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mWndsHolder.vv1.onStopRecord();
            }
        });
        startservice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent(DemoMainActivity.this, MyService.class);
                startService(it);
            }
        });
    }

    public boolean isAddDevShowing() {
        if (mAddDevLayout.getVisibility() == View.VISIBLE)
            return true;
        else
            return false;
    }

    private void initLogin() {
        /**
         * ????璞�????
         */
        mOk = (Button) findViewById(R.id.btn_add);
        mOk.setOnClickListener(this);
        mCancel = (Button) findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(this);
        mSnTV = (TextView) findViewById(R.id.txt_serial);
        mIpTV = (TextView) findViewById(R.id.txt_ip);
        mPortTV = (TextView) findViewById(R.id.txt_port);
        mUserNameTV = (TextView) findViewById(R.id.txt_username);
        mPassWordTV = (TextView) findViewById(R.id.txt_password);
        mSnET = (EditText) findViewById(R.id.edt_serial);
        mIpET = (EditText) findViewById(R.id.edt_ip);
        mPortET = (EditText) findViewById(R.id.edt_port);
        mUserNameET = (EditText) findViewById(R.id.edt_username);
        mPassWordET = (EditText) findViewById(R.id.edt_password);
        mLoginTypeSP = (Spinner) findViewById(R.id.sp_logintype);
    }

    public void setAddDevShow(boolean bShow) {
        if (bShow) {
            mAddDevLayout.setVisibility(View.VISIBLE);
            mAddDevLayout.startAnimation(mShowAnimation);
        } else {
            mAddDevLayout.setVisibility(View.GONE);
            mAddDevLayout.startAnimation(mHideAnimation);
        }
    }

    private void onShowPicture(String filename) {
        Bitmap bitmap = BitmapFactory.decodeFile(filename);
        mCaptureIV.setImageBitmap(bitmap);
        mCaptureIV.setVisibility(View.VISIBLE);
        Animation animation = (AnimationSet) AnimationUtils.loadAnimation(DemoMainActivity.this, R.anim.popshow_anim2);
        mCaptureIV.setAnimation(animation);
        animation.start();
        myHandler.sendEmptyMessageDelayed(2, 2000);
    }

    /*
	 * ????????????
	 */
    private void initVoiceIntercom() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = myHandler.obtainMessage(1);
                if (mlVoiceHandle == 0)
                    mlVoiceHandle = mNetSdk.StartVoiceComMR(mloginid, 0);
                if (mlVoiceHandle > 0) {
                    msg.obj = VoiceIntercomState.INIT_S;
                    if (mVoiceIntercom.prepare())
                        mVoiceIntercom.start(mlVoiceHandle);
                } else {
                    msg.obj = VoiceIntercomState.INIT_F;
                }
                if (mbConnectMode == ConnectMode.monitorconn)
                    msg.sendToTarget();
            }
        }).start();
    }

    /*
     * ???PCM?????????
     */
    public AudioParam getAudioParam() {
        AudioParam audioParam = new AudioParam();
        audioParam.mFrequency = 8000;
        audioParam.mChannel = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;
        return audioParam;
    }

    protected void Stop() {
        mNetSdk.onStopAlarmMsg(true);
        mWndsHolder.vv1.onStop();
        if (mplayhandle != null) {
            if (mplayhandle.length > 0)
                mNetSdk.onStopRealPlay(mplayhandle[0]);
        }
    }

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mAudioRecordDlg == null) {
                        LayoutInflater inflater = LayoutInflater.from(DemoMainActivity.this);
                        View view = inflater.inflate(R.layout.voiceintercom, null);
                        Button cancelbtn = (Button) view.findViewById(R.id.cancelbtn);
                        cancelbtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                mAudioRecordDlg.dismiss();
                            }
                        });
                        mAudioRecordDlg = new AlertDialog.Builder(DemoMainActivity.this).create();
                        mAudioRecordDlg.setMessage("???????");
                        mAudioRecordDlg.setView(view);
                        mAudioRecordDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface arg0) {
                                // TODO Auto-generated method stub
                                mVoiceIntercom.stop();
                                mVoiceIntercom.release();
                            }
                        });
                    }

                    mAudioRecordDlg.show();
                    break;
                case 2:
                    mCaptureIV.setVisibility(View.GONE);
                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_add://????璞�
                final DevInfo devInfo = new DevInfo();
                int port = 0;
                port = Integer.parseInt(mPortET.getText().toString());
                devInfo.Ip = mIpET.getText().toString();
                devInfo.TCPPort = port;
                try {
                    devInfo.UserName = mUserNameET.getText().toString().getBytes("GBK");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                devInfo.PassWord = mPassWordET.getText().toString();
                Log.d("", "-------1-------" + devInfo.Ip + "\n" + devInfo.TCPPort + "\n" + devInfo.UserName + "\n" + devInfo.PassWord);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//				    	DevInfo devInfo = new DevInfo();
//				    	devInfo.Ip = "10.10.33.74";
//				    	devInfo.TCPPort = 34567;
//				    	devInfo.UserName = "admin";
//				    	devInfo.PassWord = "9";
                        Stop();
                        mloginid = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                        mplayhandle = new long[devInfo.ChanNum];
                        mNetSdk.SetupAlarmChan(mloginid);
                        mNetSdk.SetAlarmMessageCallBack();
                        ChnInfo chnInfo = new ChnInfo();
                        chnInfo.ChannelNo = 0;
                        chnInfo.nStream = 1;//0??????????1????????
                        long playhandle = mNetSdk.onRealPlay(0, mloginid, chnInfo);
                        mNetSdk.setDataCallback(playhandle);
                        mWndsHolder.vv1.initData();
                        mWndsHolder.vv1.setAudioCtrl(AudioState.CLOSED);
                    }
                }).start();
//			if(isAddDevShowing())
//				setAddDevShow(false);
                break;
            case R.id.btn_cancel:
                if (isAddDevShowing())
                    setAddDevShow(false);
                break;
            default:
                break;
        }
    }

    private com.xm.video.MySurfaceView.MyOneCallBack myOneCallBack = new com.xm.video.MySurfaceView.MyOneCallBack() {

        @Override
        public void onClick(int arg0, MotionEvent arg1) {
            // TODO Auto-generated method stub
            if (!isAddDevShowing())
                setAddDevShow(true);
            mWndSelected = arg0;
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "dddddddd", Toast.LENGTH_SHORT).show();
        super.onConfigurationChanged(newConfig);
    }

    private com.xm.video.MySurfaceView.MyAddListener myAddLs = new com.xm.video.MySurfaceView.MyAddListener() {

        @Override
        public void onAdd(int arg0) {
            // TODO Auto-generated method stub
            if (!isAddDevShowing())
                setAddDevShow(true);
            mWndSelected = arg0;
        }
    };

    class WndsHolder {
        MySurfaceView vv1;
        MySurfaceView vv2;
        MySurfaceView vv3;
        MySurfaceView vv4;
    }
}
