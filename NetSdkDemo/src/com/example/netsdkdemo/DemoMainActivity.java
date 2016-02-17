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
 *@date 2013-11-1 下午05:12:08
 *@describe 调用NetSdk接口 Demo 实现了实时预览、云台控制、对讲和录像查询及回放
 */
public class DemoMainActivity extends Activity implements OnClickListener{
	private NetSdk mNetSdk = null;
	//private Button stopbtn = null;							//停止
	private Button voiceIntercombtn = null;					//语音对讲
	private Button playback = null;							//录像回放
	private Button capture = null;							//截图
	private Button audioctrl = null;						//音频控制
	private Button ianalysis = null;						//智能分析
	private Button setting = null;							//设置
	private Button startRecord = null;						//开始录像
	private Button stopRecord = null;						//停止录像
	private Button ptzctrl;									//云台控制		
	private MyDirection mDirection;							//云台控件
	private Button loginButton;								//登录
	private Button playOrStop;          					//播放停止
	private Button rapid_configBtn;							//快速配置
	private boolean mbPTZ = false;
	private	long mloginid;
	private long[] mplayhandles = new long[4];
	private VoiceIntercom mVoiceIntercom = null;			//语音对讲
	private long mlVoiceHandle = 0;							//对讲句柄
	private int mbConnectMode = ConnectMode.monitorconn;	//默认是监控连接
	private AlertDialog mAudioRecordDlg = null;				//语音对讲提示框
	private ImageView mCaptureIV = null;
	private int mWndSelected = 0;							//选中的窗口
	private TranslateAnimation mShowAnimation;
	private TranslateAnimation mHideAnimation;
//	private Button mOk = null;								//确定
	private Button mCancel = null;							//取消
	private TextView sum,num[];								//总通道数，窗口显示通道号
	private TextView mSnTV = null;							//序列号
	private TextView mIpTV = null;							//ip
	private TextView mPortTV = null;						//端口
	private TextView mUserNameTV = null;					//用户名
	private TextView mPassWordTV = null;					//密码
	private EditText mSnET = null;							//序列号
	private EditText mIpET = null;							//ip
	private EditText mPortET = null;						//端口
	private EditText mUserNameET = null;					//用户名
	private EditText mPassWordET = null;					//密码
	private Spinner mLoginTypeSP = null;					//登陆类型:普通登陆、云方式登陆
	private WndsHolder mWndsHolder;
	private int mSocketStyle = SocketStyle.TCPSOCKET;
	private Bitmap mCatchPic;								//抓图图片
	private int miStreamType = StreamType.Extra;			//当前码流类型，默认是副码流
	private DevInfo mDevInfo = new DevInfo();
	private FileOutputStream fos ;
	private int ReviewState[];								//预览状态“播放1停止0”
	private int Channel[];									//窗口0 1 2 3预览通道号
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
     * 初始化
     */
    private void init() {
    	
    	ReviewState=new int[]{0,0,0,0};
    	Channel=new int[]{-1,-1,-1,-1};
    	mNetSdk.DevInit();
    	mNetSdk = NetSdk.getInstance();
    	mNetSdk.onStopAlarmMsg(false);
    	//获取完整的视频数据
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
    	 * 报警回调
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
    	mWndsHolder.vv1.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo录像文件"+File.separator+"通道"+mWndSelected);
     	mWndsHolder.vv2.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo录像文件"+File.separator+"通道"+mWndSelected);
     	mWndsHolder.vv3.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo录像文件"+File.separator+"通道"+mWndSelected);
     	mWndsHolder.vv4.initRecord(Environment.getExternalStorageDirectory()+File.separator+"demo录像文件"+File.separator+"通道"+mWndSelected);
    	//语音对讲初始化
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
		    		Toast.makeText(getApplicationContext(), "请先登录,连接设备", Toast.LENGTH_SHORT).show();
		    		return ;
		    	}
				Intent intent=new Intent(DemoMainActivity.this,PTZControlActivity.class);
				intent.putExtra("loginid", mloginid);
				startActivity(intent);
			}
		});
    	
    	mWndsHolder.vv1.setWndTouchType(1);//支持窗口滑动表示云台操作 mWndTouchType;//窗口触摸类型 0：点击窗口中的播放按钮；1：点击窗口显示云台操作
    	/*
    	 * 云台滑动操作
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
    	 *云台停止操作
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
    	 * 停止播放
    	 */
    /*	stopbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Stop();
			}
		});*/
    	/*
    	 * 对讲
    	 */
    	voiceIntercombtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
					return ;
				}
				initVoiceIntercom();
			}
		});
    	/*
    	 * 录像回放
    	 */
    	playback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!isLogin){
		    		Toast.makeText(getApplicationContext(), "请先登录,连接设备", Toast.LENGTH_SHORT).show();
		    		return ;
		    	}else{
					//onStopReview();
					//Stop();
					mNetSdk.onStopAlarmMsg(true);
					playOrStop.setText("播放");
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
    	 * 本地截图
    	 */
    	capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getApplicationContext(), "截图成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
    	/**
    	 * 音频控制
    	 */
    	audioctrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
					return ;
				}
				//如果对讲正在开启，则直接不管音频
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
    	 * 智能分析
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
    	 * 设置
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
    	 * 开始录像
    	 */
    	startRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
					return ;
				}
				if(getMySurface(mWndSelected).getRecordState() != RecordState.RECORDING){
					getMySurface(mWndSelected).onStartRecord();
				}
			}
		});
    	/*
    	 * 停止录像
    	 */
    	stopRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isLogin){
					Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
					return ;
				}
				if(getMySurface(mWndSelected).getRecordState() == RecordState.RECORDING){
					getMySurface(mWndSelected).onStopRecord();
				}
			}
		});
    	/*
    	 * 快速配置
    	 * */
    	rapid_configBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onQAddDev();
			
			}
		});
    }
    /*private void initLogin() {
		//mOk = (Button)findViewById(R.id.btn_add);//添加设备界面
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
	 * 快速添加设备
	 */
	private void onQAddDev() {
		mWifiManager=new MyWifiManager(DemoMainActivity.this);
		//RadarSearchDevicesDlg 图片资源前三个雷达相关，后两个检索到的设备图标
		int[] res = {R.drawable.gplus_search_bg,
				R.drawable.locus_round_click,
				R.drawable.gplus_search_args,
				R.drawable.chn_green,
				R.drawable.chn_red};
		//RadarSearchDevicesDlg 文本资源
		int[] textres = {R.string.find,
				R.string.find_dev,
				R.string.password_error3};
		 //获取当前手机的屏幕高、宽
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        float mDensity = dm.density;
        //mDensity与文字大小相关
		dlg = new RadarSearchDevicesDlg(DemoMainActivity.this,res,textres,mDensity, null);
		//RadarSearchDevicesDlg wifi名称框背景
		dlg.setTextViewBackgroundResource(R.drawable.textfield_bg);
		//RadarSearchDevicesDlg 密码框背景
		dlg.setEditTextBackgroundResource(R.drawable.textfield_bg);
		//RadarSearchDevicesDlg wifi左边图标
		dlg.setWifiImageResource(R.drawable.wifi_logo);
		//RadarSearchDevicesDlg 密码左边图标
		dlg.setPasswordImageResource(R.drawable.password_logo);
		//RadarSearchDevicesDlg 消失监听
		dlg.setOnDismissListener(new onMyDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
			}
		});
		//RadarSearchDevicesDlg 取消监听
		dlg.setOnMyCancelListener(new onMyCancelListener() {
			
			@Override
			public void onCancel(int arg0) {
			}
		});
		//RadarSearchDevicesDlg 搜索到设备，雷达中会显示设备图标，这是点击图标监听
		dlg.setOnMySelectDevListener(new onMySelectDevListener() {
			@Override
			public void onSelectDev(DevInfo devinfo) {
				//DevInfo 设备信息类
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
						//登录设备 @param 窗口号  设备信息类  Socket类别
						int[] mLoginError = new int[1];		//登陆错误值
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
		//RadarSearchDevicesDlg 标题
		dlg.setTitle(R.string.add_dev);
		//RadarSearchDevicesDlg “显示密码” 文字设置
		dlg.setShowPwdText(R.string.show_pwd);
		//搜索按钮 样式资源
		int[] srcs = {R.drawable.bg_color6,R.drawable.button6_sel};
		//wifi信息
		NetConfig netConfig = new NetConfig();
		mNetSdk.GetLocalWifiNetConfig(netConfig,mWifiManager.getDhcpInfo());
		netConfig.mac = mWifiManager.getMacAddress();
		netConfig.linkSpeed = mWifiManager.getLinkSpeed();
		//设置 搜索按钮背景
		dlg.setButtonBackgroundResource(srcs);
		dlg.setButtonText("搜索");
		dlg.setWifiNetConfig(netConfig);
		String password = "";
		//初始化  mWifiManager.getSSID() 当前wifi名称，      password 默认密码
		dlg.setText(mWifiManager.getSSID(),password);
		//设置RadarSearchDevicesDlg中wifi信息为当前wifi的信息  ScanResult
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
	 * 初始化语音对讲
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
     * 获得PCM音频数据参数
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
					mAudioRecordDlg.setMessage("语音对讲");
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
					Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
					isLogin=true;
					loginButton.setText("注销");
					sum.setText("共有通道："+String.valueOf(mDevInfo.ChanNum));
					
					mNetSdk.setOnDisConnectListener(new OnDisConnectListener() {
						@Override
						public void onDisConnect(int arg0, long arg1, byte[] arg2, long arg3) {
							if(arg1 == mloginid) {
								onStopChn(4);
								mloginid = 0;
								mNetSdk.setReceiveCompleteVData(0, false);
								loginButton.setText("登录");
								isLogin=false;
							}else {
								onStopChn(arg0);
								ReviewState[arg0] = 0;
								Channel[arg0]=-1;
							}
							playOrStop.setText("播放");
						}
					});
					}else{
					Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
					}
				break;
			case 4:
				loginButton.setText("登录");
				isLogin=false;
				Toast.makeText(getApplicationContext(), "成功断开设备连接", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				num[mWndSelected].setText("通道号："+String.valueOf(Channel[mWndSelected]));
				playOrStop.setText("停止");
				break;
			case 6:
				Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
				break;
			case 7:
				num[msg.arg1].setText("通道号：");
				break;
			case 8:
				sum.setText("共有通道:");
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
    	mDevInfo.TCPPort = 34567;//如果devInfo.Ip填写了ip则需要在这个地方填写端口
    	mDevInfo.UserName = "admin".getBytes();
    	mDevInfo.PassWord = "";
		/*mDevInfo.Ip = Ip;//这里可以填写ip或者序列号
    	mDevInfo.TCPPort = Integer.parseInt(Port);//如果devInfo.Ip填写了ip则需要在这个地方填写端口
    	mDevInfo.UserName = Name.getBytes();
    	mDevInfo.PassWord = Password;*/
    	mDevInfo.Socketstyle = SocketStyle.TCPSOCKET;//如果devInfo.Ip为ip地址则这边需要改成SocketStyle.TCPSOCKET,如果是序列号则为SOCKETNR
    	mSocketStyle = SocketStyle.TCPSOCKET;////如果devInfo.Ip为ip地址则这边需要改成SocketStyle.TCPSOCKET,如果是序列号则为SOCKETNR
    	//mWndSelected为选中的窗口id
    	int[] mLoginError = new int[1];		//登陆错误值
    	mloginid = mNetSdk.onLoginDevNat(mWndSelected, mDevInfo,mLoginError, mSocketStyle);
    	
    }
    /*
     * 登陆
     */
    public void onLoginDevice(View view){
    	if(view.getId()==R.id.login_device)
    	{
    		if(loginButton.getText().toString().equals("登录")){
    			LinearLayout layout = new LinearLayout(DemoMainActivity.this);
    			layout.setOrientation(LinearLayout.VERTICAL);
    			final EditText ip_et = new EditText(DemoMainActivity.this);
    			ip_et.setHint("ip地址");
    			final EditText port_et = new EditText(DemoMainActivity.this);
    			port_et.setHint("端口");
    			final EditText et1 = new EditText(DemoMainActivity.this);
    			et1.setHint("用户名");
    			final EditText et2 = new EditText(DemoMainActivity.this);
    			et2.setHint("密码");
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
    					.setTitle("输入登录信息")
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
    												//mDevInfo.Ip = "10.10.32.72";//这里可以填写ip或者序列号
    												//mDevInfo.Ip = "10.10.32.127";//这里可以填写ip或者序列号
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
    			playOrStop.setText("播放");
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
     * 播放，停止点击事件
     * */
    
    public void onPlayOrStop(View view){
    	if(!isLogin){
    		Toast.makeText(getApplicationContext(), "请先登录,连接设备", Toast.LENGTH_SHORT).show();
    		return ;
    	}
    	if(playOrStop.getText().toString().equals("播放")){
    	new Thread(new Runnable() {
			@Override
		public void run() {
			//mNetSdk.SetupAlarmChan(mloginid);
    		//设置报警回调函数
    		//mNetSdk.SetAlarmMessageCallBack();
    		//下面就是打开通道需要做的准备工作
    		ChnInfo chnInfo = new ChnInfo();
    		//chnInfo.ChannelNo = 0;//通道ID
    		chnInfo.nStream = miStreamType;//0表示主码流，1表示副码流
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
    		playOrStop.setText("播放");
    		num[mWndSelected].setText("通道号：");
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
	        		num[i].setText("通道号：");
	        		/*msg.what=7;
	        		msg.arg1=i;
	        		myHandler.sendMessage(msg);*/
        		}
        		if(mplayhandles[i] > 0){
        			mNetSdk.onStopRealPlay(mplayhandles[i]);
        			mplayhandles[i] = 0;
        		}
    		}
    		playOrStop.setText("播放");
    		Message msg=Message.obtain();
    		msg.what=8;
    		myHandler.sendMessage(msg);
    	}
    	
    }
	@Override
	public void onClick(View v) {
		
	}
	/*
	 * 点击视频窗口的响应事件
	 */
	private com.xm.video.MySurfaceView.MyOneCallBack myOneCallBack = new com.xm.video.MySurfaceView.MyOneCallBack() {
		@Override
		public void onClick(final int arg0, MotionEvent arg1) {
			System.out.println("选中窗口："+arg0);
			mWndSelected=arg0;
			if(ReviewState[mWndSelected]==0){
				num[mWndSelected].setText("通道号：");
				playOrStop.setText("播放");
			}
			else if(ReviewState[mWndSelected]==1){
				num[mWndSelected].setText("通道号："+String.valueOf(Channel[mWndSelected]));
				playOrStop.setText("停止");
			}
		}
	};
	//屏幕横竖屏监听
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "dddddddd", Toast.LENGTH_SHORT).show();
		super.onConfigurationChanged(newConfig);
	}
	//推送 这个推送功能必须在支持云访问的基础上，否则无法正常调用
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
	//透明串口
	/*public void onSerial(View v) {
		Intent it = new Intent(DemoMainActivity.this,SerialActivity.class);
		it.putExtra("loginId", mloginid);
		startActivity(it);
	}*/
	//设备端远程抓图
	public void onDevCapture(View v) {
		if(!isLogin){
			Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
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
	//码流切换
	public void onChangeStream(View v) {
		if(!isLogin){
			Toast.makeText(getApplicationContext(), "请先登录，连接设备", Toast.LENGTH_SHORT).show();
			return ;
		}
		//mWndsHolder.vv1.onStop();
		if(mplayhandles[mWndSelected] > 0)
			mNetSdk.onStopRealPlay(mplayhandles[mWndSelected]);
		if(miStreamType == StreamType.Extra) {
			miStreamType = StreamType.Main;
			((Button)v).setText("高清");
			Toast.makeText(DemoMainActivity.this, "主码流", Toast.LENGTH_SHORT).show();
		}
		else {
			miStreamType = StreamType.Extra;
			((Button)v).setText("流畅");
			Toast.makeText(DemoMainActivity.this, "副码流", Toast.LENGTH_SHORT).show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				//mNetSdk.SetupAlarmChan(mloginid);
	    		//设置报警回调函数
	    		//mNetSdk.SetAlarmMessageCallBack();
	    		//下面就是打开通道需要做的准备工作
	    		ChnInfo chnInfo = new ChnInfo();
	    		//chnInfo.ChannelNo = 0;//通道ID
	    		chnInfo.nStream = miStreamType;//0表示主码流，1表示副码流
		    	if(mWndSelected >= mDevInfo.ChanNum)
		    	{
		    		chnInfo.ChannelNo=0;
		    	}
		    	else 
		    		chnInfo.ChannelNo=mWndSelected;
	    		//初始化播放器，这个必须调用，不然不会显示图像
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
		//打开实时预览
		mplayhandles[mWndSelected] = mNetSdk.onRealPlay(mWndSelected, mloginid, chnInfo);
		//以下就是视频数据接收的缓冲区大小，默认是10个
		mNetSdk.SetVBufferCount(30);
		//设置数据回调
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
