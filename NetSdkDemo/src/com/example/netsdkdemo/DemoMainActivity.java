package com.example.netsdkdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.SDK_CONFIG_ENCODE_SIMPLIIFY;
import com.xm.MyConfig.AudioState;
import com.xm.MyConfig.ConnectMode;
import com.xm.MyConfig.RecordState;
import com.xm.MyConfig.SdkConfigType;
import com.xm.MyConfig.SocketStyle;
import com.xm.MyConfig.StreamType;
import com.xm.MyConfig.VoiceIntercomState;
import com.xm.NetSdk;
import com.xm.NetSdk.OnAlarmListener;
import com.xm.NetSdk.OnDisConnectListener;
import com.xm.audio.AudioParam;
import com.xm.audio.VoiceIntercom;
import com.xm.dialog.RadarSearchDevicesDlg;
import com.xm.dialog.RadarSearchDevicesDlg.OnMySearchListener;
import com.xm.dialog.RadarSearchDevicesDlg.onMyCancelListener;
import com.xm.dialog.RadarSearchDevicesDlg.onMyDismissListener;
import com.xm.dialog.RadarSearchDevicesDlg.onMySelectDevListener;
import com.xm.net.NetConfig;
import com.xm.utils.OutputDebug;
import com.xm.utils.ThreadToast;
import com.xm.video.MySurfaceView;
import com.xm.video.MySurfaceView.OnPTZScrollListener;
import com.xm.video.MySurfaceView.OnPTZStopListener;
import com.xm.view.MyDirection;
/**
 * 
 *@project NetSdkDemo 
 *@author huangwanshui
 *@date 2013-11-1 ����05:12:08
 *@describe ����NetSdk�ӿ� Demo ʵ����ʵʱԤ������̨���ơ��Խ���¼���ѯ���ط�
 */
public class DemoMainActivity extends Activity implements OnClickListener{
	private NetSdk mNetSdk = null;
	//private Button stopbtn = null;							//ֹͣ
	private Button voiceIntercombtn = null;					//�����Խ�
	private Button playback = null;							//¼��ط�
	private Button capture = null;							//��ͼ
	private Button audioctrl = null;						//��Ƶ����
	private Button ianalysis = null;						//���ܷ���
	private Button setting = null;							//����
	private Button startRecord = null;						//��ʼ¼��
	private Button stopRecord = null;						//ֹͣ¼��
	private Button ptzctrl;									//��̨����		
	private MyDirection mDirection;							//��̨�ؼ�
	private Button loginButton;								//��¼
	private Button playOrStop;          					//����ֹͣ
	private Button rapid_configBtn;							//��������
	private boolean mbPTZ = false;
	private	long mloginid;
	private long[] mplayhandles = new long[4];
	private VoiceIntercom mVoiceIntercom = null;			//�����Խ�
	private long mlVoiceHandle = 0;							//�Խ����
	private int mbConnectMode = ConnectMode.monitorconn;	//Ĭ���Ǽ������
	private AlertDialog mAudioRecordDlg = null;				//�����Խ���ʾ��
	private ImageView mCaptureIV = null;
	private int mWndSelected = 0;							//ѡ�еĴ���
	private TranslateAnimation mShowAnimation;
	private TranslateAnimation mHideAnimation;
//	private Button mOk = null;								//ȷ��
	private Button mCancel = null;							//ȡ��
	private TextView sum,num[];								//��ͨ������������ʾͨ����
	private TextView mSnTV = null;							//���к�
	private TextView mIpTV = null;							//ip
	private TextView mPortTV = null;						//�˿�
	private TextView mUserNameTV = null;					//�û���
	private TextView mPassWordTV = null;					//����
	private EditText mSnET = null;							//���к�
	private EditText mIpET = null;							//ip
	private EditText mPortET = null;						//�˿�
	private EditText mUserNameET = null;					//�û���
	private EditText mPassWordET = null;					//����
	private Spinner mLoginTypeSP = null;					//��½����:��ͨ��½���Ʒ�ʽ��½
	private WndsHolder mWndsHolder;
	private int mSocketStyle = SocketStyle.TCPSOCKET;
	private Bitmap mCatchPic;								//ץͼͼƬ
	private int miStreamType = StreamType.Extra;			//��ǰ�������ͣ�Ĭ���Ǹ�����
	private DevInfo mDevInfo = new DevInfo();
	private FileOutputStream fos ;
	private int ReviewState[];								//Ԥ��״̬������1ֹͣ0��
	private int Channel[];									//����0 1 2 3Ԥ��ͨ����
	private MyWifiManager mWifiManager;
	private RadarSearchDevicesDlg dlg; 
	private boolean isLogin=false;
	String path = "/sdcard/MyH264.h264";
	File file = new File(path);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
        //initLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    /*
     * ��ʼ��
     */
    private void init() {
    	
    	ReviewState=new int[]{0,0,0,0};
    	Channel=new int[]{-1,-1,-1,-1};
    	mNetSdk.DevInit();
    	mNetSdk = NetSdk.getInstance();
    	mNetSdk.onStopAlarmMsg(false);
    	//��ȡ��������Ƶ����
//    	mNetSdk.setOnH264DataListener(new OnH264DataListener() {
//			
//			@Override
//			public void onData(int id,byte[] pDataBuf, long dwBufSize) {
//				// TODO Auto-generated method stub
//				OutputDebug.OutputDebugLogE("DemoMainActivity", "dwBufSize:"+dwBufSize);
//				try {
//					fos.write(pDataBuf,0,(int)dwBufSize);
//					fos.flush();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
    	/**
    	 * �����ص�
    	 */
    	mNetSdk.setOnAlarmListener(new OnAlarmListener() {
			
			@Override
			public void onAlarm(long loginid, int ichannel, int iEvent, int iStatus,
					int[] time) {
				// TODO Auto-generated method stub
		//		OutputDebug.OutputDebugLogD("Demo", "tttttttttttttttttttttttttt");
			//	new ThreadToast(DemoMainActivity.this, "iEvent:"+iEvent, Toast.LENGTH_SHORT).Show();
			}
		});
    	try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//stopbtn = (Button) findViewById(R.id.stop);
    	voiceIntercombtn = (Button) findViewById(R.id.voiceIntercom);
    	playback = (Button) findViewById(R.id.playback);
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
       // mWndsHolder.vv1 = (MySurfaceView)findViewById(R.id.vv1);
        mWndsHolder.vv1 = (MySurfaceView) findViewById(R.id.vv1);
        mWndsHolder.vv2 = (MySurfaceView) findViewById(R.id.vv2);
        mWndsHolder.vv3 = (MySurfaceView) findViewById(R.id.vv3);
        mWndsHolder.vv4 = (MySurfaceView) findViewById(R.id.vv4);
        mWndsHolder.vv1.init(DemoMainActivity.this, 0);
        mWndsHolder.vv2.init(DemoMainActivity.this, 1);
        mWndsHolder.vv3.init(DemoMainActivity.this, 2);
        mWndsHolder.vv4.init(DemoMainActivity.this, 3);
        mWndsHolder.vv1.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
        mWndsHolder.vv2.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
        mWndsHolder.vv3.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
        mWndsHolder.vv4.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
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
    	loginButton=(Button)findViewById(R.id.login_device);
    	playOrStop=(Button)findViewById(R.id.play_or_stop);
    	ptzctrl=(Button)findViewById(R.id.ptzctrl);
    	rapid_configBtn=(Button)findViewById(R.id.rapid_config);
    	sum=(TextView)findViewById(R.id.sum);
    	num=new TextView[4];
    	num[0]=(TextView)findViewById(R.id.num);
    	num[1]=(TextView)findViewById(R.id.num1);
    	num[2]=(TextView)findViewById(R.id.num2);
    	num[3]=(TextView)findViewById(R.id.num3);
    	mWndsHolder.vv1.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo¼���ļ�"+File.separator+"ͨ��"+mWndSelected);
     	mWndsHolder.vv2.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo¼���ļ�"+File.separator+"ͨ��"+mWndSelected);
     	mWndsHolder.vv3.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo¼���ļ�"+File.separator+"ͨ��"+mWndSelected);
     	mWndsHolder.vv4.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo¼���ļ�"+File.separator+"ͨ��"+mWndSelected);
    	//�����Խ���ʼ��
		AudioParam audioParam = getAudioParam();
		mVoiceIntercom = new VoiceIntercom(myHandler, audioParam,DemoMainActivity.this);
    	mDirection = (MyDirection) findViewById(R.id.direction);
    	mDirection.onInit(new int[] {
    			R.drawable.up_selected,
    			R.drawable.down_selected,
    			R.drawable.left_selected,
    			R.drawable.right_selected,
    			R.drawable.left_up_selected,
    			R.drawable.left_down_selected,
    			R.drawable.right_up_selected,
    			R.drawable.right_down_selected	
    	});
    	ptzctrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isLogin){
		    		Toast.makeText(getApplicationContext(), "���ȵ�¼,�����豸", Toast.LENGTH_SHORT).show();
		    		return ;
		    	}
				Intent intent=new Intent(DemoMainActivity.this,PTZControlActivity.class);
				intent.putExtra("loginid", mloginid);
				startActivity(intent);
			}
		});
    	
    	mWndsHolder.vv1.setWndTouchType(1);//֧�ִ��ڻ�����ʾ��̨���� mWndTouchType;//���ڴ������� 0����������еĲ��Ű�ť��1�����������ʾ��̨����
    	/*
    	 * ��̨��������
    	 */
    	mWndsHolder.vv1.setOnPTZScrollListener(new OnPTZScrollListener() {
			
			@Override
			public void onPTZScroll(int direction) {
				// TODO Auto-generated method stub
				if(direction >= 0 && !mbPTZ) {
					mNetSdk.PTZControl(mloginid, 0, direction,false,4,0,0);
					mDirection.setVisibility(View.VISIBLE);
					mDirection.setDirection(direction);
					mbPTZ = true;
				}
			}
		});
    	/*
    	 *��ֹ̨ͣ����
    	 */
    	mWndsHolder.vv1.setOnPTZStopListener(new OnPTZStopListener() {
			
			@Override
			public void onPTZStop(int direction) {
				// TODO Auto-generated method stub
				mNetSdk.PTZControl(mloginid, 0, direction,true,4,0,0);
				mDirection.setVisibility(View.INVISIBLE);
				mbPTZ = false;
			}
		});
    	/*
    	 * ֹͣ����
    	 */
    /*	stopbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Stop();
			}
		});*/
    	/*
    	 * �Խ�
    	 */
    	voiceIntercombtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
					return ;
				}
				initVoiceIntercom();
			}
		});
    	/*
    	 * ¼��ط�
    	 */
    	playback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!isLogin){
		    		Toast.makeText(getApplicationContext(), "���ȵ�¼,�����豸", Toast.LENGTH_SHORT).show();
		    		return ;
		    	}else{
					//onStopReview();
					//Stop();
					mNetSdk.onStopAlarmMsg(true);
					playOrStop.setText("����");
					onStopChn(4);
					Intent it = new Intent();
					it.putExtra("loginId", mloginid);
					it.putExtra("channelNum", mDevInfo.ChanNum);
					it.setClass(DemoMainActivity.this, PlayBackActivity.class);
					startActivity(it);
				}
			}
		});
    	/*
    	 * ���ؽ�ͼ
    	 */
    	capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
					return ;
				}
				String str = Environment.getExternalStorageDirectory() + "/Capture";
				File picture = null;
				switch (mWndSelected) {
				case 0:
					picture = mWndsHolder.vv1.OnCapture(getApplicationContext(), str);
					Bitmap bitmap = BitmapFactory.decodeFile(str);
					mCaptureIV.setImageBitmap(bitmap);
					mCaptureIV.setVisibility(View.VISIBLE);
					Animation animation = (AnimationSet) AnimationUtils.loadAnimation(DemoMainActivity.this, R.anim.popshow_anim2);
					mCaptureIV.setAnimation(animation);
					animation.start();
					myHandler.sendEmptyMessageDelayed(2, 2000);
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
				if(picture != null) {
					onShowPicture(picture.getAbsoluteFile().toString());
					Toast.makeText(getApplicationContext(), "��ͼ�ɹ�", Toast.LENGTH_SHORT).show();
				}
			}
		});
    	/**
    	 * ��Ƶ����
    	 */
    	audioctrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
					return ;
				}
				//����Խ����ڿ�������ֱ�Ӳ�����Ƶ
				if(mlVoiceHandle != 0)
					return;
				if(getMySurface(mWndSelected).getAudioState() == AudioState.CLOSED) {
					getMySurface(mWndSelected).setAudioCtrl(AudioState.OPENED);
				}
				else {
					getMySurface(mWndSelected).setAudioCtrl(AudioState.CLOSED);
				}
			}
		});
    	/*
    	 * ���ܷ���
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
    	/*
    	 * ����
    	 */
    	setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.putExtra("loginId", mloginid);
				it.setClass(DemoMainActivity.this, SettingActivity.class);
				startActivity(it);
			}
		});
    	/*
    	 * ��ʼ¼��
    	 */
    	startRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
					return ;
				}
				if(getMySurface(mWndSelected).getRecordState() != RecordState.RECORDING){
					getMySurface(mWndSelected).onStartRecord();
				}
			}
		});
    	/*
    	 * ֹͣ¼��
    	 */
    	stopRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
					return ;
				}
				if(getMySurface(mWndSelected).getRecordState() == RecordState.RECORDING){
					getMySurface(mWndSelected).onStopRecord();
				}
			}
		});
    	/*
    	 * ��������
    	 * */
    	rapid_configBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onQAddDev();
			
			}
		});
    }
    /*private void initLogin() {
		//mOk = (Button)findViewById(R.id.btn_add);//����豸����
		//mOk.setOnClickListener(this);
		mCancel = (Button)findViewById(R.id.btn_cancel);
		mCancel.setOnClickListener(this);
		mSnTV = (TextView)findViewById(R.id.txt_serial);
		mIpTV = (TextView)findViewById(R.id.txt_ip);
		mPortTV = (TextView)findViewById(R.id.txt_port);
		mUserNameTV = (TextView)findViewById(R.id.txt_username);
		mPassWordTV = (TextView)findViewById(R.id.txt_password);
		mSnET = (EditText)findViewById(R.id.edt_serial);
		mIpET = (EditText)findViewById(R.id.edt_ip);
		mPortET = (EditText)findViewById(R.id.edt_port);
		mUserNameET = (EditText)findViewById(R.id.edt_username);
		mPassWordET = (EditText)findViewById(R.id.edt_password);
		mLoginTypeSP = (Spinner)findViewById(R.id.sp_logintype);
    }*/
    /**
	 * ��������豸
	 */
	private void onQAddDev() {
		mWifiManager=new MyWifiManager(DemoMainActivity.this);
		//RadarSearchDevicesDlg ͼƬ��Դǰ�����״���أ����������������豸ͼ��
		int[] res = {R.drawable.gplus_search_bg,
				R.drawable.locus_round_click,
				R.drawable.gplus_search_args,
				R.drawable.chn_green,
				R.drawable.chn_red};
		//RadarSearchDevicesDlg �ı���Դ
		int[] textres = {R.string.find,
				R.string.find_dev,
				R.string.password_error3};
		 //��ȡ��ǰ�ֻ�����Ļ�ߡ���
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        float mDensity = dm.density;
        //mDensity�����ִ�С���
		dlg = new RadarSearchDevicesDlg(DemoMainActivity.this,res,textres,mDensity, null);
		//RadarSearchDevicesDlg wifi���ƿ򱳾�
		dlg.setTextViewBackgroundResource(R.drawable.textfield_bg);
		//RadarSearchDevicesDlg ����򱳾�
		dlg.setEditTextBackgroundResource(R.drawable.textfield_bg);
		//RadarSearchDevicesDlg wifi���ͼ��
		dlg.setWifiImageResource(R.drawable.wifi_logo);
		//RadarSearchDevicesDlg �������ͼ��
		dlg.setPasswordImageResource(R.drawable.password_logo);
		//RadarSearchDevicesDlg ��ʧ����
		dlg.setOnDismissListener(new onMyDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
			}
		});
		//RadarSearchDevicesDlg ȡ������
		dlg.setOnMyCancelListener(new onMyCancelListener() {
			
			@Override
			public void onCancel(int arg0) {
			}
		});
		//RadarSearchDevicesDlg �������豸���״��л���ʾ�豸ͼ�꣬���ǵ��ͼ�����
		dlg.setOnMySelectDevListener(new onMySelectDevListener() {
			@Override
			public void onSelectDev(DevInfo devinfo) {
				//DevInfo �豸��Ϣ��
				final DevInfo _devInfo = devinfo;
				_devInfo.Socketstyle = SocketStyle.TCPSOCKET;
				try {
					_devInfo.UserName = "admin".getBytes("GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				_devInfo.PassWord = "";
				_devInfo.wifi_ssid = mWifiManager.getSSID();
				onClearData();
				new Thread(new Runnable() {
					@Override
					public void run() {
						//��¼�豸 @param ���ں�  �豸��Ϣ��  Socket���
						int[] mLoginError = new int[1];		//��½����ֵ
						mloginid = mNetSdk.onLoginDevNat(mWndSelected, _devInfo, mLoginError, mSocketStyle);
						myHandler.sendEmptyMessage(9);
						myHandler.sendEmptyMessage(3);
					}
				}).start();
		
			}
		});
		dlg.setOnMySearchListener(new OnMySearchListener() {
			
			@Override
			public void onMySearch(String ssid, String password) {
				// TODO Auto-generated method stub
			}
		});
		//RadarSearchDevicesDlg ����
		dlg.setTitle(R.string.add_dev);
		//RadarSearchDevicesDlg ����ʾ���롱 ��������
		dlg.setShowPwdText(R.string.show_pwd);
		//������ť ��ʽ��Դ
		int[] srcs = {R.drawable.bg_color6,R.drawable.button6_sel};
		//wifi��Ϣ
		NetConfig netConfig = new NetConfig();
		mNetSdk.GetLocalWifiNetConfig(netConfig,mWifiManager.getDhcpInfo());
		netConfig.mac = mWifiManager.getMacAddress();
		netConfig.linkSpeed = mWifiManager.getLinkSpeed();
		//���� ������ť����
		dlg.setButtonBackgroundResource(srcs);
		dlg.setButtonText("����");
		dlg.setWifiNetConfig(netConfig);
		String password = "";
		//��ʼ��  mWifiManager.getSSID() ��ǰwifi���ƣ�      password Ĭ������
		dlg.setText(mWifiManager.getSSID(),password);
		//����RadarSearchDevicesDlg��wifi��ϢΪ��ǰwifi����Ϣ  ScanResult
		dlg.setWifiResult(mWifiManager.getCurScanResult(mWifiManager.getSSID()));
		dlg.onShow();
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
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		onStopChn(4);
	}

	/*
	 * ��ʼ�������Խ�
	 */
	private void initVoiceIntercom() {
		new Thread(new Runnable() {
			@Override 
			public void run() {
				// TODO Auto-generated method stub
				Message msg = myHandler.obtainMessage(1);
				if(mlVoiceHandle == 0)
					mlVoiceHandle = mNetSdk.StartVoiceComMR(mloginid,0);
				if(mlVoiceHandle != 0) {
					msg.obj = VoiceIntercomState.INIT_S;
					if(mVoiceIntercom.prepare())
						mVoiceIntercom.start(mlVoiceHandle);
				}else {
					msg.obj = VoiceIntercomState.INIT_F;
				}
				if(mbConnectMode == ConnectMode.monitorconn)
					msg.sendToTarget();
				
			}
		}).start();
	}
	/*
     * ���PCM��Ƶ���ݲ���
     */
	public AudioParam getAudioParam()
	{
		AudioParam audioParam = new AudioParam();
    	audioParam.mFrequency = 8000;
    	audioParam.mChannel = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    	audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;
    	return audioParam;
	}
    protected void Stop() {
		mNetSdk.onStopAlarmMsg(true);
		onStopChn(0);
	}
    private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if(mAudioRecordDlg == null) {
					LayoutInflater inflater = LayoutInflater.from(DemoMainActivity.this);
					View view = inflater.inflate(R.layout.voiceintercom, null);
					Button cancelbtn = (Button)view.findViewById(R.id.cancelbtn);
					cancelbtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mAudioRecordDlg.dismiss();
						}
					});
					mAudioRecordDlg = new AlertDialog.Builder(DemoMainActivity.this).create();
					mAudioRecordDlg.setMessage("�����Խ�");
					mAudioRecordDlg.setView(view);
					mAudioRecordDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface arg0) {
							// TODO Auto-generated method stub
							mVoiceIntercom.stop();
							mVoiceIntercom.release();
							if(mlVoiceHandle != 0) {
								mNetSdk.StopVoiceCom(mlVoiceHandle);
								mlVoiceHandle = 0;
							}
						}
					});
				}
				mAudioRecordDlg.show();
				break;
			case 2:
				mCaptureIV.setVisibility(View.GONE);
				break;
			case 3:
				if(mloginid != 0){
					Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_SHORT).show();
					isLogin=true;
					loginButton.setText("ע��");
					sum.setText("����ͨ����"+String.valueOf(mDevInfo.ChanNum));
					
					mNetSdk.setOnDisConnectListener(new OnDisConnectListener() {
						@Override
						public void onDisConnect(int arg0, long arg1, byte[] arg2, long arg3) {
							if(arg1 == mloginid) {
								onStopChn(4);
								mloginid = 0;
								mNetSdk.setReceiveCompleteVData(0, false);
								loginButton.setText("��¼");
								isLogin=false;
							}else {
								onStopChn(arg0);
								ReviewState[arg0] = 0;
								Channel[arg0]=-1;
							}
							playOrStop.setText("����");
						}
					});
					}else{
					Toast.makeText(getApplicationContext(), "��¼ʧ��", Toast.LENGTH_SHORT).show();
					}
				break;
			case 4:
				loginButton.setText("��¼");
				isLogin=false;
				Toast.makeText(getApplicationContext(), "�ɹ��Ͽ��豸����", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				num[mWndSelected].setText("ͨ���ţ�"+String.valueOf(Channel[mWndSelected]));
				playOrStop.setText("ֹͣ");
				break;
			case 6:
				Toast.makeText(getApplicationContext(), "���ȵ�¼", Toast.LENGTH_SHORT).show();
				break;
			case 7:
				num[msg.arg1].setText("ͨ���ţ�");
				break;
			case 8:
				sum.setText("����ͨ��:");
				break;
			case 9:
				dlg.onDismiss();
				break;
			default:
				break;
			}
		}
    	
    };
    private void onLogin(){
    	mDevInfo.Ip = "192.168.1.50";
    	mDevInfo.TCPPort = 34567;//���devInfo.Ip��д��ip����Ҫ������ط���д�˿�
    	mDevInfo.UserName = "admin".getBytes();
    	mDevInfo.PassWord = "";
		/*mDevInfo.Ip = Ip;//���������дip�������к�
    	mDevInfo.TCPPort = Integer.parseInt(Port);//���devInfo.Ip��д��ip����Ҫ������ط���д�˿�
    	mDevInfo.UserName = Name.getBytes();
    	mDevInfo.PassWord = Password;*/
    	mDevInfo.Socketstyle = SocketStyle.TCPSOCKET;//���devInfo.IpΪip��ַ�������Ҫ�ĳ�SocketStyle.TCPSOCKET,��������к���ΪSOCKETNR
    	mSocketStyle = SocketStyle.TCPSOCKET;////���devInfo.IpΪip��ַ�������Ҫ�ĳ�SocketStyle.TCPSOCKET,��������к���ΪSOCKETNR
    	//mWndSelectedΪѡ�еĴ���id
    	int[] mLoginError = new int[1];		//��½����ֵ
    	mloginid = mNetSdk.onLoginDevNat(mWndSelected, mDevInfo,mLoginError, mSocketStyle);
    	
    }
    /*
     * ��½
     */
    public void onLoginDevice(View view){
    	if(view.getId()==R.id.login_device)
    	{
    		if(loginButton.getText().toString().equals("��¼")){
    			LinearLayout layout = new LinearLayout(DemoMainActivity.this);
    			layout.setOrientation(LinearLayout.VERTICAL);
    			final EditText ip_et = new EditText(DemoMainActivity.this);
    			ip_et.setHint("ip��ַ");
    			final EditText port_et = new EditText(DemoMainActivity.this);
    			port_et.setHint("�˿�");
    			final EditText et1 = new EditText(DemoMainActivity.this);
    			et1.setHint("�û���");
    			final EditText et2 = new EditText(DemoMainActivity.this);
    			et2.setHint("����");
    			et2.setSingleLine(true);
    			et2.setInputType(InputType.TYPE_CLASS_TEXT
    					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
    			layout.addView(ip_et, new LayoutParams(LayoutParams.MATCH_PARENT,
    					LayoutParams.WRAP_CONTENT));
    			layout.addView(port_et, new LayoutParams(LayoutParams.MATCH_PARENT,
    					LayoutParams.WRAP_CONTENT));
    			layout.addView(et1, new LayoutParams(LayoutParams.MATCH_PARENT,
    					LayoutParams.WRAP_CONTENT));
    			layout.addView(et2, new LayoutParams(LayoutParams.MATCH_PARENT,
    					LayoutParams.WRAP_CONTENT));
    			new AlertDialog.Builder(DemoMainActivity.this)
    					.setTitle("�����¼��Ϣ")
    					.setView(layout)
    					.setPositiveButton(android.R.string.ok,
    							new DialogInterface.OnClickListener() {
    								@Override
    								public void onClick(DialogInterface arg0, int arg1) {
    									final String Name = et1.getText().toString().trim();
    									final String Password = et2.getText().toString().trim();
    									final String Ip=ip_et.getText().toString().trim();
    									final String Port=port_et.getText().toString().trim();
    									/*if(Name==null||Name.equals(""))
    										return;
    									if(Ip==null||Ip.equals(""))
    										return;
    									if(Port==null||Port.equals(""))
    										return;*/
    									new Thread(new Runnable() {
    										@Override
    										public void run() {
    												int port = 0;
    												//mDevInfo.Ip = "10.10.32.72";//���������дip�������к�
    												//mDevInfo.Ip = "10.10.32.127";//���������дip�������к�
    												onLogin();
    												Message msg=Message.obtain();
    												msg.what=3;
    												myHandler.sendMessageDelayed(msg, 2000);
    											}
    										}).start();
    								}
    							})
    					.setNegativeButton(android.R.string.cancel,
    							new DialogInterface.OnClickListener() {
    								@Override
    								public void onClick(DialogInterface arg0, int arg1) {
    									// TODO Auto-generated method stub
    								}
    							}).create().show();
    		}else{
    			onClearData();
    			for(int i=0;i<ReviewState.length;i++){
    				ReviewState[i]=0;
    			}
    			playOrStop.setText("����");
    			Message msg=Message.obtain();
				msg.what=4;
				myHandler.sendMessageDelayed(msg, 2000);
				
    		}
    	}
    }
    private void onClearData() {	
		onStopChn(4);
		mNetSdk.setReceiveCompleteVData(0, false);
		if (mloginid != 0) {
			mNetSdk.onDevLogout(mloginid); 
			mloginid = 0;
		}
	}
    /*
     * ���ţ�ֹͣ����¼�
     * */
    
    public void onPlayOrStop(View view){
    	if(!isLogin){
    		Toast.makeText(getApplicationContext(), "���ȵ�¼,�����豸", Toast.LENGTH_SHORT).show();
    		return ;
    	}
    	if(playOrStop.getText().toString().equals("����")){
    	new Thread(new Runnable() {
			@Override
		public void run() {
			//mNetSdk.SetupAlarmChan(mloginid);
    		//���ñ����ص�����
    		//mNetSdk.SetAlarmMessageCallBack();
    		//������Ǵ�ͨ����Ҫ����׼������
    		ChnInfo chnInfo = new ChnInfo();
    		//chnInfo.ChannelNo = 0;//ͨ��ID
    		chnInfo.nStream = miStreamType;//0��ʾ��������1��ʾ������
	    	if(mWndSelected >= mDevInfo.ChanNum)
	    		chnInfo.ChannelNo=0;
	    	else 
	    		chnInfo.ChannelNo=mWndSelected;
			int position=0;
    		for(int i:Channel){
    			if(i==chnInfo.ChannelNo){
    				onStopChn(position);
    				Message msg=Message.obtain();
    				msg.what=7;
    				msg.arg1=position;
    				myHandler.sendMessage(msg);
    			}
    			position++;
    		}
    		onReview(chnInfo);
    		getMySurface(mWndSelected).setAudioCtrl(AudioState.OPENED);
			getMySurface(mWndSelected).onPlay();
			mNetSdk.setReceiveCompleteVData(0, true);
    		Message msg=Message.obtain();
			msg.what=5;
			myHandler.sendMessageDelayed(msg, 500);
		}}).start();
    	}else{
    		onStopChn(mWndSelected);
    		ReviewState[mWndSelected]=0;
    		playOrStop.setText("����");
    		num[mWndSelected].setText("ͨ���ţ�");
    	}
    	
    }
    private void onStopChn(int pos) {
    	switch(pos){
		case 0:mWndsHolder.vv1.onStop();
			break;
		case 1:mWndsHolder.vv2.onStop();
			break;
		case 2:mWndsHolder.vv3.onStop();
			break;
		case 3:mWndsHolder.vv4.onStop();
			break;
		case 4:
			mWndsHolder.vv1.onStop();
			mWndsHolder.vv2.onStop();
			mWndsHolder.vv3.onStop();
			mWndsHolder.vv4.onStop();
			break;
		default:
			pos = 0;
			break;
    	}
    	if(pos<4){
    		ReviewState[pos] = 0;
    		Channel[pos] = -1;
    		Message msg=Message.obtain();
    		msg.what=7;
    		msg.arg1=pos;
    		myHandler.sendMessage(msg);
    		if( mplayhandles[pos] > 0) {
        		mNetSdk.onStopRealPlay(mplayhandles[pos]);
        		mplayhandles[pos] = 0;
        	}
    	}else{
    		for(int i= 0;i <= 3;i++){
    			if(ReviewState[i]==1){
	    			ReviewState[i]=0;
	        		Channel[i]=-1;
	        		num[i].setText("ͨ���ţ�");
	        		/*msg.what=7;
	        		msg.arg1=i;
	        		myHandler.sendMessage(msg);*/
        		}
        		if(mplayhandles[i] > 0){
        			mNetSdk.onStopRealPlay(mplayhandles[i]);
        			mplayhandles[i] = 0;
        		}
    		}
    		playOrStop.setText("����");
    		Message msg=Message.obtain();
    		msg.what=8;
    		myHandler.sendMessage(msg);
    	}
    	
    }
	@Override
	public void onClick(View v) {
		
	}
	/*
	 * �����Ƶ���ڵ���Ӧ�¼�
	 */
	private com.xm.video.MySurfaceView.MyOneCallBack myOneCallBack = new com.xm.video.MySurfaceView.MyOneCallBack() {
		@Override
		public void onClick(final int arg0, MotionEvent arg1) {
			System.out.println("ѡ�д��ڣ�"+arg0);
			mWndSelected=arg0;
			if(ReviewState[mWndSelected]==0){
				num[mWndSelected].setText("ͨ���ţ�");
				playOrStop.setText("����");
			}
			else if(ReviewState[mWndSelected]==1){
				num[mWndSelected].setText("ͨ���ţ�"+String.valueOf(Channel[mWndSelected]));
				playOrStop.setText("ֹͣ");
			}
		}
	};
	//��Ļ����������
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "dddddddd", Toast.LENGTH_SHORT).show();
		super.onConfigurationChanged(newConfig);
	}
	//���� ������͹��ܱ�����֧���Ʒ��ʵĻ����ϣ������޷���������
	public void onPushMsg(View v) {
//		mNetSdk.setOnPushMsgListener(new OnPushMsgListener() {
//			
//			@Override
//			public void onPush(int arg0, String arg1, int arg2, int arg3, int arg4,
//					int[] arg5) {
//				// TODO Auto-generated method stub
//				OutputDebug.OutputDebugLogD("demo", "uuid:"+arg1);
//			}
//		});
//		int iret = mNetSdk.AuthClientInit();
//		if(iret > 0) {
//			PeerInfo peerinfo = new PeerInfo();
//			iret = mNetSdk.AuthClientGetPeerInfo("4d3fe5453876c7f7", peerinfo);
//			if(iret > 0) {
//				OutputDebug.OutputDebugLogD("Demo", "pushmsg:"+peerinfo.pmsIP+"port:"+peerinfo.wanPort);
//				mNetSdk.PushLayerInit();
//				iret = mNetSdk.PushLayerLinkDev(peerinfo.pmsIP,9001, "4d3fe5453876c7f7", "0", "admin", "");
//				OutputDebug.OutputDebugLogD("Demo", "iret:"+iret);
//				if(iret >= 0) {
//					OutputDebug.OutputDebugLogD("Demo", "pushmsg ok");
//				}
//			}
//		}
	}
	//͸������
	/*public void onSerial(View v) {
		Intent it = new Intent(DemoMainActivity.this,SerialActivity.class);
		it.putExtra("loginId", mloginid);
		startActivity(it);
	}*/
	//�豸��Զ��ץͼ
	public void onDevCapture(View v) {
		if(!isLogin){
			Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
			return ;
		}
		byte[] buffer = new byte[1024*250];
		int[] pPicLen = new int[1];
		boolean bret = mNetSdk.H264DVRCatchPicInBuffer(mloginid, 0, buffer, 0, pPicLen);
		if(bret) {
			Bitmap pic = BitmapFactory.decodeByteArray(buffer, 0, pPicLen[0]);
			mCaptureIV.setImageBitmap(pic);
			mCaptureIV.setVisibility(View.VISIBLE);
			Animation animation = (AnimationSet) AnimationUtils.loadAnimation(DemoMainActivity.this, R.anim.popshow_anim2);
			mCaptureIV.setAnimation(animation);
			animation.start();
			myHandler.sendEmptyMessageDelayed(2, 2000);
		}
	}
	//�����л�
	public void onChangeStream(View v) {
		if(!isLogin){
			Toast.makeText(getApplicationContext(), "���ȵ�¼�������豸", Toast.LENGTH_SHORT).show();
			return ;
		}
		//mWndsHolder.vv1.onStop();
		if(mplayhandles[mWndSelected] > 0)
			mNetSdk.onStopRealPlay(mplayhandles[mWndSelected]);
		if(miStreamType == StreamType.Extra) {
			miStreamType = StreamType.Main;
			((Button)v).setText("����");
			Toast.makeText(DemoMainActivity.this, "������", Toast.LENGTH_SHORT).show();
		}
		else {
			miStreamType = StreamType.Extra;
			((Button)v).setText("����");
			Toast.makeText(DemoMainActivity.this, "������", Toast.LENGTH_SHORT).show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				//mNetSdk.SetupAlarmChan(mloginid);
	    		//���ñ����ص�����
	    		//mNetSdk.SetAlarmMessageCallBack();
	    		//������Ǵ�ͨ����Ҫ����׼������
	    		ChnInfo chnInfo = new ChnInfo();
	    		//chnInfo.ChannelNo = 0;//ͨ��ID
	    		chnInfo.nStream = miStreamType;//0��ʾ��������1��ʾ������
		    	if(mWndSelected >= mDevInfo.ChanNum)
		    	{
		    		chnInfo.ChannelNo=0;
		    	}
		    	else 
		    		chnInfo.ChannelNo=mWndSelected;
	    		//��ʼ�������������������ã���Ȼ������ʾͼ��
	    		onReview(chnInfo);
	    		if(ReviewState[mWndSelected]==0){
	    			Message msg=Message.obtain();
	    			msg.what=5;
	    			myHandler.sendMessage(msg);
	    		}
			}
		}).start();
		
	}
	private void onReview(ChnInfo chnInfo){
		int position=0;
		for(int i:Channel){
			if(i==chnInfo.ChannelNo){
				Channel[position]=-1;
				ReviewState[position]=0;
				onStopChn(position);
			}
			position++;
		}
		//��ʵʱԤ��
		mplayhandles[mWndSelected] = mNetSdk.onRealPlay(mWndSelected, mloginid, chnInfo);
		//���¾�����Ƶ���ݽ��յĻ�������С��Ĭ����10��
		mNetSdk.SetVBufferCount(30);
		//�������ݻص�
		mNetSdk.setDataCallback(mplayhandles[mWndSelected]);
		switch(mWndSelected){
    		case 0:mWndsHolder.vv1.initData();
    			break;
    		case 1:mWndsHolder.vv2.initData();
    			break;
    		case 2:mWndsHolder.vv3.initData();
    			break;
    		case 3:mWndsHolder.vv4.initData();
    			break;
		}
		Channel[mWndSelected]=chnInfo.ChannelNo;
		ReviewState[mWndSelected]=1;
	}
	private MySurfaceView getMySurface(int position){
		switch(position){
			case 0:return mWndsHolder.vv1;
			case 1:return mWndsHolder.vv2;
			case 2:return mWndsHolder.vv3;
			case 3:return mWndsHolder.vv4;
			default:return null;
		}
	}
	class WndsHolder {
		MySurfaceView vv1;
		MySurfaceView vv2;
		MySurfaceView vv3;
		MySurfaceView vv4;
	}
}
