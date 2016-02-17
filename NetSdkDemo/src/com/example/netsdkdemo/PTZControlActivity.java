package com.example.netsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.MyConfig.PTZ_ControlType;
import com.xm.MyConfig.StreamType;
import com.xm.NetSdk;
import com.xm.video.MySurfaceView;
import com.xm.video.MySurfaceView.OnPTZScrollListener;
import com.xm.video.MySurfaceView.OnPTZStopListener;
import com.xm.view.MyDirection;

public class PTZControlActivity extends Activity {

	private NetSdk mNetSdk = null;
	private WnHolder myWndsHolder;
	private int mWndSelected = 0; // 选中的窗口
	private long mLoginId;
	private boolean mbPTZ = false;
	private MyDirection mDirection; // 云台控件
	private Button playOrStop; // 播放停止
	private int ReviewState[]; // 预览状态“播放1停止0”
	private int Channel[]; // 窗口0 1 2 3预览通道号
	private long[] mplayhandles = new long[4];
	private int miStreamType = StreamType.Extra; // 当前码流类型，默认是副码流
	private DevInfo mDevInfo = new DevInfo();
	private boolean hasFocus=false;
	private Button zoom_In,zoom_Out,focus_Far,focus_Near,aperture_Open,aperture_Close;
	private int changeState=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptzcontrol);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		findViewById();
		init();
	}

	@Override
	protected void onStop() {
		super.onStop();
		onStopChn(4);
	}

	private void findViewById() {
		myWndsHolder = new WnHolder();
		myWndsHolder.vvv1 = (MySurfaceView) findViewById(R.id.vvv1);
		myWndsHolder.vvv2 = (MySurfaceView) findViewById(R.id.vvv2);
		myWndsHolder.vvv3 = (MySurfaceView) findViewById(R.id.vvv3);
		myWndsHolder.vvv4 = (MySurfaceView) findViewById(R.id.vvv4);
		playOrStop = (Button) findViewById(R.id.playorstop);
		mDirection = (MyDirection) findViewById(R.id.mdirection);
		zoom_In=(Button)findViewById(R.id.zoom_in);
		zoom_Out=(Button)findViewById(R.id.zoom_out);
		focus_Far=(Button)findViewById(R.id.focus_far);
		focus_Near=(Button)findViewById(R.id.focus_near);
		aperture_Open=(Button)findViewById(R.id.aperture_open);
		aperture_Close=(Button)findViewById(R.id.aperture_close);
	}

	private void init() {
		mNetSdk.DevInit();
		mNetSdk = NetSdk.getInstance();
		Intent intent = getIntent();
		mLoginId = intent.getLongExtra("loginid", 0);
		ReviewState = new int[] { 0, 0, 0, 0 };
		Channel = new int[] { -1, -1, -1, -1 };
		myWndsHolder.vvv1.init(PTZControlActivity.this, 0);
		myWndsHolder.vvv2.init(PTZControlActivity.this, 1);
		myWndsHolder.vvv3.init(PTZControlActivity.this, 2);
		myWndsHolder.vvv4.init(PTZControlActivity.this, 3);
		/*
		 * myWndsHolder.vvv1.initAdd(new int[] { R.drawable.wnd_add_normal,
		 * R.drawable.wnd_add_selected }); myWndsHolder.vvv2.initAdd(new int[] {
		 * R.drawable.wnd_add_normal, R.drawable.wnd_add_selected });
		 * myWndsHolder.vvv3.initAdd(new int[] { R.drawable.wnd_add_normal,
		 * R.drawable.wnd_add_selected }); myWndsHolder.vvv4.initAdd(new int[] {
		 * R.drawable.wnd_add_normal, R.drawable.wnd_add_selected });
		 */
		myWndsHolder.vvv1.setOneCallBack(myOneCallBack);
		myWndsHolder.vvv2.setOneCallBack(myOneCallBack);
		myWndsHolder.vvv3.setOneCallBack(myOneCallBack);
		myWndsHolder.vvv4.setOneCallBack(myOneCallBack);
		myWndsHolder.vvv1.setWndTouchType(1);// 支持窗口滑动表示云台操作
												// mWndTouchType;//窗口触摸类型
												// 0：点击窗口中的播放按钮；1：点击窗口显示云台操作
		myWndsHolder.vvv2.setWndTouchType(1);
		myWndsHolder.vvv3.setWndTouchType(1);
		myWndsHolder.vvv4.setWndTouchType(1);
		mDirection.onInit(new int[] { R.drawable.up_selected,
				R.drawable.down_selected, R.drawable.left_selected,
				R.drawable.right_selected, R.drawable.left_up_selected,
				R.drawable.left_down_selected, R.drawable.right_up_selected,
				R.drawable.right_down_selected });
		initControlListener();
		zoom_In.setOnTouchListener(new myTouchListener());
		zoom_Out.setOnTouchListener(new myTouchListener());
		focus_Far.setOnTouchListener(new myTouchListener());
		focus_Near.setOnTouchListener(new myTouchListener());
		aperture_Open.setOnTouchListener(new myTouchListener());
		aperture_Close.setOnTouchListener(new myTouchListener());
	}
	class myTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (view.getId()) {
			case R.id.zoom_in:
				changeState = PTZ_ControlType.ZOOM_IN;
				break;
			case R.id.zoom_out:
				changeState = PTZ_ControlType.ZOOM_OUT;
				break;
			case R.id.focus_far:
				changeState = PTZ_ControlType.FOCUS_FAR;
				break;
			case R.id.focus_near:
				changeState = PTZ_ControlType.FOCUS_NEAR;
				break;
			case R.id.aperture_open:
				changeState = PTZ_ControlType.IRIS_OPEN;
				break;
			case R.id.aperture_close:
				changeState = PTZ_ControlType.IRIS_CLOSE;
				break;
			default:
				break;
			}
			switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					hasFocus=false;
					break;
				case MotionEvent.ACTION_UP:
					hasFocus=true;
					break;
				default:
					break;
			}
			mNetSdk.PTZControl(mLoginId, Channel[mWndSelected], changeState, hasFocus, 4, 0, 0);
			return false;
		}
		
	}
	private void initControlListener() {
		/*
		 * 云台滑动操作
		 */
		myWndsHolder.vvv1.setOnPTZScrollListener(new OnPTZScrollListener() {
			@Override
			public void onPTZScroll(int direction) {
				// TODO Auto-generated method stub
				if (direction >= 0 && !mbPTZ && mWndSelected == 0
						&& ReviewState[0] == 1) {
					mNetSdk.PTZControl(mLoginId, 0, direction, false, 4, 0, 0);
					mDirection.setVisibility(View.VISIBLE);
					mDirection.setDirection(direction);
					mbPTZ = true;
				}
			}
		});
		/*
		 * 云台停止操作
		 */
		myWndsHolder.vvv1.setOnPTZStopListener(new OnPTZStopListener() {
			@Override
			public void onPTZStop(int direction) {
				// TODO Auto-generated method stub
				if (mWndSelected == 0 && ReviewState[0] == 1) {
					mNetSdk.PTZControl(mLoginId, 0, direction, true, 4, 0, 0);
					mDirection.setVisibility(View.INVISIBLE);
					mbPTZ = false;
				}
			}
		});
		/*
		 * 云台滑动操作
		 */
		myWndsHolder.vvv2.setOnPTZScrollListener(new OnPTZScrollListener() {
			@Override
			public void onPTZScroll(int direction) {
				// TODO Auto-generated method stub
				
				if (direction >= 0 && !mbPTZ && mWndSelected == 1
						&& ReviewState[1] == 1) {
					System.out.println("云台："+"1111111");
					mNetSdk.PTZControl(mLoginId, Channel[mWndSelected], direction, false, 4, 0, 0);
					mDirection.setVisibility(View.VISIBLE);
					mDirection.setDirection(direction);
					mbPTZ = true;
				}
			}
		});
		/*
		 * 云台停止操作
		 */
		myWndsHolder.vvv2.setOnPTZStopListener(new OnPTZStopListener() {
			@Override
			public void onPTZStop(int direction) {
				// TODO Auto-generated method stub
				if (mWndSelected == 1 && ReviewState[1] == 1) {
					mNetSdk.PTZControl(mLoginId, Channel[mWndSelected], direction, true, 4, 0, 0);
					mDirection.setVisibility(View.INVISIBLE);
					mbPTZ = false;
				}
			}
		});
		/*
		 * 云台滑动操作
		 */
		myWndsHolder.vvv3.setOnPTZScrollListener(new OnPTZScrollListener() {
			@Override
			public void onPTZScroll(int direction) {
				// TODO Auto-generated method stub
				if (direction >= 0 && !mbPTZ && mWndSelected == 2
						&& ReviewState[2] == 1) {
					mNetSdk.PTZControl(mLoginId,  Channel[mWndSelected], direction, false, 4, 0, 0);
					mDirection.setVisibility(View.VISIBLE);
					mDirection.setDirection(direction);
					mbPTZ = true;
				}
			}
		});
		/*
		 * 云台停止操作
		 */
		myWndsHolder.vvv3.setOnPTZStopListener(new OnPTZStopListener() {
			@Override
			public void onPTZStop(int direction) {
				// TODO Auto-generated method stub
				if (mWndSelected == 2 && ReviewState[2] == 1) {
					mNetSdk.PTZControl(mLoginId, Channel[mWndSelected], direction, true, 4, 0, 0);
					mDirection.setVisibility(View.INVISIBLE);
					mbPTZ = false;
				}
			}
		});
		/*
		 * 云台滑动操作
		 */
		myWndsHolder.vvv4.setOnPTZScrollListener(new OnPTZScrollListener() {
			@Override
			public void onPTZScroll(int direction) {
				// TODO Auto-generated method stub
				if (direction >= 0 && !mbPTZ && mWndSelected == 3
						&& ReviewState[3] == 1) {
					mNetSdk.PTZControl(mLoginId,  Channel[mWndSelected], direction, false, 4, 0, 0);
					mDirection.setVisibility(View.VISIBLE);
					mDirection.setDirection(direction);
					mbPTZ = true;
				}
			}
		});
		/*
		 * 云台停止操作
		 */
		myWndsHolder.vvv4.setOnPTZStopListener(new OnPTZStopListener() {
			@Override
			public void onPTZStop(int direction) {
				// TODO Auto-generated method stub
				if (mWndSelected == 3 && ReviewState[3] == 1) {
					mNetSdk.PTZControl(mLoginId,  Channel[mWndSelected], direction, true, 4, 0, 0);
					mDirection.setVisibility(View.INVISIBLE);
					mbPTZ = false;
				}
			}
		});
	}
	/*
	 * 播放，停止点击事件
	 */
	public void PlayOrStop(View view) {
		if (playOrStop.getText().toString().equals("播放")) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					//mNetSdk.SetupAlarmChan(mLoginId);
					// 设置报警回调函数
					//mNetSdk.SetAlarmMessageCallBack();
					// 下面就是打开通道需要做的准备工作
					ChnInfo chnInfo = new ChnInfo();
					// chnInfo.ChannelNo = 0;//通道ID
					chnInfo.nStream = miStreamType;// 0表示主码流，1表示副码流
					if (mWndSelected >= mDevInfo.ChanNum)
						chnInfo.ChannelNo = 0;
					else
						chnInfo.ChannelNo = mWndSelected;
					int position = 0;
					for (int i : Channel) {
						if (i == chnInfo.ChannelNo) {
							onStopChn(position);
						}
						position++;
					}
					onReview(chnInfo);
					Message msg = Message.obtain();
					msg.what = 5;
					myHandler.sendMessageDelayed(msg, 500);
				}
			}).start();
		} else {
			onStopChn(mWndSelected);
			ReviewState[mWndSelected] = 0;
			playOrStop.setText("播放");
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 5:
				playOrStop.setText("停止");
				break;
			}
		}
	};

	/*
	 * 打开预览
	 */
	private void onReview(ChnInfo chnInfo) {
		int position = 0;
		for (int i : Channel) {
			if (i == chnInfo.ChannelNo) {
				Channel[position] = -1;
				ReviewState[position] = 0;
				onStopChn(position);
			}
			position++;
		}
		// 打开实时预览
		mplayhandles[mWndSelected] = mNetSdk.onRealPlay(mWndSelected, mLoginId,
				chnInfo);
		// 以下就是视频数据接收的缓冲区大小，默认是10个
		mNetSdk.SetVBufferCount(30);
		// 设置数据回调
		mNetSdk.setDataCallback(mplayhandles[mWndSelected]);
		switch (mWndSelected) {
		case 0:
			myWndsHolder.vvv1.initData();
			break;
		case 1:
			myWndsHolder.vvv2.initData();
			break;
		case 2:
			myWndsHolder.vvv3.initData();
			break;
		case 3:
			myWndsHolder.vvv4.initData();
			break;
		}
		Channel[mWndSelected] = chnInfo.ChannelNo;
		ReviewState[mWndSelected] = 1;
	}

	private void onStopChn(int pos) {
		switch (pos) {
		case 0:
			myWndsHolder.vvv1.onStop();
			break;
		case 1:
			myWndsHolder.vvv2.onStop();
			break;
		case 2:
			myWndsHolder.vvv3.onStop();
			break;
		case 3:
			myWndsHolder.vvv4.onStop();
			break;
		case 4:
			myWndsHolder.vvv1.onStop();
			myWndsHolder.vvv2.onStop();
			myWndsHolder.vvv3.onStop();
			myWndsHolder.vvv4.onStop();
			break;
		default:
			pos = 0;
			break;
		}
		if (pos < 4) {
			ReviewState[pos] = 0;
			Channel[pos] = -1;
			if (mplayhandles[pos] > 0) {
				mNetSdk.onStopRealPlay(mplayhandles[pos]);
				mplayhandles[pos] = 0;
			}
		} else {
			for (int i = 0; i <= 3; i++) {
				ReviewState[i] = 0;
				Channel[i] = -1;
				if (mplayhandles[i] > 0) {
					mNetSdk.onStopRealPlay(mplayhandles[i]);
					mplayhandles[i] = 0;
				}
			}
		}

	}

	/*
	 * 点击视频窗口的响应事件
	 */
	private com.xm.video.MySurfaceView.MyOneCallBack myOneCallBack = new com.xm.video.MySurfaceView.MyOneCallBack() {
		@Override
		public void onClick(final int arg0, MotionEvent arg1) {
			System.out.println("选中窗口："+arg0);
			mWndSelected=arg0;
			if(ReviewState[mWndSelected]==0)
				playOrStop.setText("播放");
			else if(ReviewState[mWndSelected]==1)
				playOrStop.setText("停止");
		}
	};

	class WnHolder {
		MySurfaceView vvv1;
		MySurfaceView vvv2;
		MySurfaceView vvv3;
		MySurfaceView vvv4;
	}
}
