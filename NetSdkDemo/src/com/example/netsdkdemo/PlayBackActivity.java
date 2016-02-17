package com.example.netsdkdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xm.FileData;
import com.xm.FindInfo;
import com.xm.MyConfig;
import com.xm.MyConfig.AudioState;
import com.xm.MyConfig.FileType;
import com.xm.MyConfig.PlayBackAction;
import com.xm.MyConfig.PlayState;
import com.xm.NetSdk;
import com.xm.NetSdk.OnRPlayBackCompletedListener;
import com.xm.audio.AudioParam;
import com.xm.audio.VoiceIntercom;
import com.xm.video.MySurfaceView;
import com.xm.video.MySurfaceView.OnPlayBackPosListener;
/**
 * 
 *@project NetSdkDemo 
 *@author huangwanshui
 *@date 2013-11-1 下午05:13:11
 *@describe 录像回放
 */
public class PlayBackActivity extends Activity{
	private MySurfaceView mPlayBackVV;
	private Button mFileSearchBtn;
	private Button mTimeSearchBtn;
	private Button mStopBtn;
	private Button mPauseBtn;
	private Button mResumeBtn;
	private Button mFPlayBtn;
	private Button mSPlayBtn;
	private Button mStopDlBtn,mCloseListBtn;
	private ListView mFileList;
	private NetSdk mNetSdk;
	private long mLoginId;
	private long mlPlayHandle[]=new long[4];
	private boolean mbSeek;
	private long mlDownLoadHandle;
	private VoiceIntercom mVoiceIntercom = null;//语音对讲
	private List<FileData> mFileDataList;
	private int mCount = 0;
	private long mProgress;
	private long mPercentage = 0;							//百分比
	private SeekBar timeSeek;
	private int[] mStartTime=new int[4],mEndTime=new int[4],mTotalTime=new int[4];
	private RelativeLayout myListPart;
	private TextView time_tv;
	private WndHolder mWndsHolder;
	private int ReviewState[];								//预览状态“播放1停止0”
	private int Channel[];									//窗口0 1 2 3预览通道号
	private int channelNum=1;								//通道总数
	private int mWndSelected = 0;							//选中的窗口
	private int mPlayState;
	class WndHolder {
		MySurfaceView v1;
		MySurfaceView v2;
		MySurfaceView v3;
		MySurfaceView v4;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playback);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/*关闭长亮*/
		/*if (wakeLock != null) {
			wakeLock.release();
			}*/
		init();
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		onStopChn(4);
	}
	private void init() {
		mFileDataList = new ArrayList<FileData>();
		ReviewState=new int[]{0,0,0,0};
    	Channel=new int[]{-1,-1,-1,-1};
		mNetSdk.DevInit();
		Intent it = getIntent();
			mLoginId = it.getLongExtra("loginId", 0);
			channelNum=it.getIntExtra("channelNum", 1);
			mWndsHolder=new WndHolder();
			mWndsHolder.v1 = (MySurfaceView) findViewById(R.id.v1);
	        mWndsHolder.v2 = (MySurfaceView) findViewById(R.id.v2);
	        mWndsHolder.v3 = (MySurfaceView) findViewById(R.id.v3);
	        mWndsHolder.v4 = (MySurfaceView) findViewById(R.id.v4);
	        mWndsHolder.v1.init(PlayBackActivity.this, 0);
	        mWndsHolder.v2.init(PlayBackActivity.this, 1);
	        mWndsHolder.v3.init(PlayBackActivity.this, 2);
	        mWndsHolder.v4.init(PlayBackActivity.this, 3);
	        mWndsHolder.v1.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
	        mWndsHolder.v2.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
	        mWndsHolder.v3.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
	        mWndsHolder.v4.initAdd(new int[] {R.drawable.wnd_add_normal,R.drawable.wnd_add_selected});
	        mWndsHolder.v1.setOneCallBack(myOneCallBack);
	        mWndsHolder.v2.setOneCallBack(myOneCallBack);
	        mWndsHolder.v3.setOneCallBack(myOneCallBack);
	        mWndsHolder.v4.setOneCallBack(myOneCallBack);
		 	mWndsHolder.v1.setAudioCtrl(AudioState.OPENED);
			mWndsHolder.v2.setAudioCtrl(AudioState.OPENED);
			mWndsHolder.v3.setAudioCtrl(AudioState.OPENED);
			mWndsHolder.v4.setAudioCtrl(AudioState.OPENED);
			timeSeek=(SeekBar)findViewById(R.id.time_seek);
			time_tv=(TextView)findViewById(R.id.time_tv);
			mNetSdk = NetSdk.getInstance();
			/**
			 * 进度条
			 * */
			timeSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					if(mTotalTime[mWndSelected] > 0 && mbSeek) {
						mPercentage = (mProgress * 100 / mTotalTime[mWndSelected]);
						mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], PlayBackAction.SDK_PLAY_BACK_SEEK_PERCENT,mPercentage);
						mbSeek = false;
					}
					//mWndsHolder.v1.onPlay();
					//mNetSdk.PlayBackControl(mlPlayHandle[0], PlayBackAction.SDK_PLAY_BACK_CONTINUE,0);
					getMySurface(mWndSelected).onPlay();
					//mWndHolder.mPlayBackVideoView.onPlay();
					mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], PlayBackAction.SDK_PLAY_BACK_CONTINUE,0);
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
					mNetSdk.PlayBackControl(mlPlayHandle[0], PlayBackAction.SDK_PLAY_BACK_PAUSE,0);
					getMySurface(mWndSelected).onPause();
					//	mWndHolder.mPlayBackVideoView.onPause();
					mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], PlayBackAction.SDK_PLAY_BACK_PAUSE,0);
				}
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
					// TODO Auto-generated method stub
					mProgress = progress;
					if(fromUser && mTotalTime[mWndSelected] > 0)
						mbSeek = true;
				}
			});
		/**
		 * 设置回放进度监听
		 */
		mNetSdk.setOnRPlayBackCompletedListener(new OnRPlayBackCompletedListener() {
			@Override
			public void onRPlayBackCompleted(long lPlayHandle, long lTotalSize,
					long lDownLoadSize, long dwUser) {
				// TODO Auto-generated method stub
				if(lPlayHandle == mlPlayHandle[mWndSelected]) {
					System.out.println("lDownLoadSize："+lDownLoadSize);
				}else if(lPlayHandle == mlDownLoadHandle) {
					System.out.println("文件总大小为:"+lTotalSize+" 目前已经下载:"+lDownLoadSize);
				}
			}
		});
		//mPlayBackVV = (MySurfaceView) findViewById(R.id.playback_vv);
		//mPlayBackVV.init(getApplicationContext(), 0);
		//mPlayBackVV.setAudioCtrl(AudioState.OPENED);
		

		//语音对讲初始化
		AudioParam audioParam = getAudioParam();
		mVoiceIntercom = new VoiceIntercom(myHandler, audioParam,PlayBackActivity.this);
		mFileSearchBtn = (Button) findViewById(R.id.searchbyfile);
		mTimeSearchBtn = (Button)findViewById(R.id.searchbytime);
		mStopBtn = (Button) findViewById(R.id.stop);
		mPauseBtn = (Button) findViewById(R.id.pause);
		mFPlayBtn = (Button) findViewById(R.id.kf);
		mSPlayBtn = (Button) findViewById(R.id.mf);
		mStopDlBtn = (Button) findViewById(R.id.stopdl);
		myListPart=(RelativeLayout) findViewById(R.id.list_part);
		mCloseListBtn=(Button)findViewById(R.id.close_list);
		//暂停回放
		mPauseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mlPlayHandle[mWndSelected] > 0) {
					mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], MyConfig.PlayBackAction.SDK_PLAY_BACK_PAUSE, 0);
					getMySurface(mWndSelected).onPause();
				}
			}
		});
		
		mCloseListBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myListPart.setVisibility(View.INVISIBLE);
			}
		});

		mResumeBtn = (Button) findViewById(R.id.resume);
		/*
		 * 恢复回放
		 */
		mResumeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mlPlayHandle[mWndSelected] > 0) {
					getMySurface(mWndSelected).onPlay();
					mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], MyConfig.PlayBackAction.SDK_PLAY_BACK_CONTINUE, 0);
				}
			}
		});
		mFileList = (ListView) findViewById(R.id.filelist);
		/**
		 * 查询录像回放文件_文件查找
		 */
		mFileSearchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myListPart.setVisibility(View.VISIBLE);
				ArrayList<HashMap<String,Object>> list = onSearchFile();
				SimpleAdapter adapter = new SimpleAdapter(PlayBackActivity.this, list, R.layout.filelist,
						new String[]{"filename"},new int[]{R.id.filelist_tv});
				mFileList.setAdapter(adapter);
			}
		});
		/**
		 * 查询录像回放文件_时间查找
		 */
		mTimeSearchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout layout = new LinearLayout(PlayBackActivity.this);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.setBackgroundColor(getResources()
						.getColor(R.color.white));
				Calendar calendar=Calendar.getInstance(TimeZone.getDefault());  
				//final TimePickerDialog mTimePicker=new TimePickerDialog(PlayBackActivity.this,otsl,hourOfDay,minute,true);
				//mTimePicker.show();
			    final TimePicker mTimePicker=new TimePicker(PlayBackActivity.this);
				final DatePicker mDatePicker=new DatePicker(PlayBackActivity.this);
				mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
				
				mTimePicker.setIs24HourView(true);
				mDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
				int version = Integer.valueOf(android.os.Build.VERSION.SDK);
				if(version>=11)
					mDatePicker.setCalendarViewShown(false);
				layout.addView(mDatePicker,new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				layout.addView(mTimePicker,new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				new AlertDialog.Builder(PlayBackActivity.this)
				.setTitle("请选择开始时间：")
				.setView(layout)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
								final TimeObject startTime=new TimeObject(mTimePicker.getCurrentMinute(),mTimePicker.getCurrentHour(),
										mDatePicker.getDayOfMonth(),mDatePicker.getMonth()+1,mDatePicker.getYear());
								setEndTime(startTime);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
							}
						}).create().show();
			}
		});
		
		/*
		 * 播放录像文件
		 */
		mFileList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				FileData filedata = mFileDataList.get(arg2);
				System.out.println("filedata:"+filedata.sFileName);
				//filedata.ch = 0;
				if(mWndSelected>=channelNum)
		    		filedata.ch=0;
		    	else 
		    		filedata.ch=mWndSelected;
				int position=0;
		    	for(int i:Channel){
		    		if(i==filedata.ch){
		    			onStopChn(position);
		    		}
		    		position++;
		    	}
				mlPlayHandle[mWndSelected] = mNetSdk.PlayBackByName(mWndSelected,mLoginId,filedata,1);
				
				if(mlPlayHandle[mWndSelected] > 0) {
					Toast.makeText(getApplicationContext(), "打开成功", Toast.LENGTH_SHORT).show();
					getMySurface(mWndSelected).initData();
					ReviewState[mWndSelected]=1;
					Channel[mWndSelected]=filedata.ch;		
					mStartTime[mWndSelected] = filedata.stBeginTime.hour * 3600 +
							filedata.stBeginTime.minute * 60 +
							filedata.stBeginTime.second;
					mEndTime[mWndSelected] = filedata.stEndTime.hour * 3600 +
							filedata.stEndTime.minute * 60 +
							filedata.stEndTime.second;
					mTotalTime[mWndSelected] = (mEndTime[mWndSelected] - mStartTime[mWndSelected]);
					timeSeek.setMax(mTotalTime[mWndSelected]);
					timeSeek.setProgress(0);
					getMySurface(mWndSelected).setOnPlayBackPosListener(onPlayBackPosLs);
					System.out.println("录像长度:"+mTotalTime[mWndSelected]+" "+mStartTime[mWndSelected]+" "+mEndTime[mWndSelected]);
					myListPart.setVisibility(View.INVISIBLE);
				}else 
					Toast.makeText(getApplicationContext(), "打开失败",Toast.LENGTH_SHORT).show();
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
//					Toast.makeText(getApplicationContext(), "正在下载", Toast.LENGTH_SHORT).show();
//				}else 
//					Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
				FindInfo findInfo = new FindInfo();
				findInfo.szFileName = "";
				findInfo.nChannelN0 = 0;
				findInfo.nFileType = FileType.SDK_RECORD_ALL;
				findInfo.startTime.year = 2014;
				findInfo.startTime.month = 10;
				findInfo.startTime.day = 29;
				findInfo.startTime.hour = 9;
				findInfo.startTime.minute = 0;
				findInfo.startTime.second = 0;
				findInfo.endTime.year = 2014;
				findInfo.endTime.month = 10;
				findInfo.endTime.day = 29;
				findInfo.endTime.hour = 10;
				findInfo.endTime.minute = 0;
				findInfo.endTime.second = 0;
				File file = new File(Environment.getExternalStorageDirectory() + "/");
				if(file.exists())
					Toast.makeText(getApplicationContext(), "创建成功", Toast.LENGTH_SHORT).show();
				mlDownLoadHandle = mNetSdk.GetFileByTime(0,mLoginId,findInfo, file.getAbsolutePath(),1);
				if(mlDownLoadHandle != 0) {
					Toast.makeText(getApplicationContext(), "正在下载", Toast.LENGTH_SHORT).show();
				}else 
					Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		/*
		 * 停止回放
		 */
		mStopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					onStopChn(mWndSelected);
				
			}
		});
		/**
		 * 快放
		 */
		mFPlayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getMySurface(mWndSelected).mplaystatus != PlayState.MPS_FAST) {
					getMySurface(mWndSelected).onPlayBackFast();
					mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], MyConfig.PlayBackAction.SDK_PLAY_BACK_FAST, 2);
				}
			}
		});
		/**
		 * 慢放
		 */
		mSPlayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getMySurface(mWndSelected).mplaystatus != PlayState.MPS_SLOW) {
					getMySurface(mWndSelected).onPlayBackSlow();
					mNetSdk.PlayBackControl(mlPlayHandle[mWndSelected], MyConfig.PlayBackAction.SDK_PLAY_BACK_SLOW, 2);
				}
				mPlayBackVV.OnCapture(getApplicationContext(), Environment.getExternalStorageDirectory()+"/snapshot");
			}
		});
		mStopDlBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mlDownLoadHandle != 0) {
					mNetSdk.StopGetFile(mlDownLoadHandle);
				}
			}
		});
	}
	private void setEndTime(final TimeObject startTime){
		LinearLayout layout = new LinearLayout(PlayBackActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(getResources().getColor(R.color.white));
	    final TimePicker mTimePicker=new TimePicker(PlayBackActivity.this);
		final DatePicker mDatePicker=new DatePicker(PlayBackActivity.this);
		mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
		mTimePicker.setIs24HourView(true);
		mDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		final TextView tv=new TextView(PlayBackActivity.this);
		String time= String.format("%02d:%02d",startTime.getHour(),startTime.getMinute());
		String date=String.valueOf(startTime.getYear())+"年"+String.format("%02d",startTime.getMonth())+"月"+
				String.format("%02d",startTime.getDay())+"日";
		tv.setText("开始时间:"+date+"  "+time);
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		if(version>=11)
			mDatePicker.setCalendarViewShown(false);
		layout.addView(tv,new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		layout.addView(mDatePicker,new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		layout.addView(mTimePicker,new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		new AlertDialog.Builder(PlayBackActivity.this)
		.setTitle("请选择结束时间：")
		.setView(layout)
		.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
						TimeObject endTime=new TimeObject(mTimePicker.getCurrentMinute(),mTimePicker.getCurrentHour(),
								mDatePicker.getDayOfMonth(),mDatePicker.getMonth()+1,mDatePicker.getYear());
						System.out.println("时间:"+endTime.getMinute()+"  "+startTime.getMinute());
						onTimeSearch(startTime,endTime);
					}
				})
		.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
					}
				}).create().show();
	}
	
	private void onTimeSearch(TimeObject startTime,TimeObject endTime){
		//mFileDataList = new ArrayList<FileData>();
		FindInfo findInfo = new FindInfo();
		findInfo.startTime.year = startTime.getYear();
		findInfo.startTime.month = startTime.getMonth();
		findInfo.startTime.day = startTime.getDay();
		findInfo.startTime.hour = startTime.getHour();
		findInfo.startTime.minute = startTime.getMinute();
		findInfo.startTime.second = 0;
		findInfo.endTime.year = endTime.getYear();
		findInfo.endTime.month = endTime.getMonth();
		findInfo.endTime.day = endTime.getDay();
		findInfo.endTime.hour = endTime.getHour();
		findInfo.endTime.minute = endTime.getMinute();
		findInfo.endTime.second = 0;
		findInfo.nChannelN0 = 0;
		findInfo.nFileType = 0;
		findInfo.szFileName = "";
		if(mWndSelected>=channelNum)
			findInfo.nChannelN0=0;
    	else 
    		findInfo.nChannelN0=mWndSelected;
		int position=0;
    	for(int i:Channel){
    		if(i==findInfo.nChannelN0){
    			onStopChn(position);
    		}
    		position++;
    	}
		mlPlayHandle[mWndSelected] = mNetSdk.PlayBackByTime(mWndSelected, mLoginId, findInfo,1,1);
		if(mlPlayHandle[mWndSelected] > 0 && compareTime(startTime,endTime)) {
			mPlayState = PlayState.MPS_PLAYING;
			getMySurface(mWndSelected).initData();
			ReviewState[mWndSelected]=1;
			Channel[mWndSelected]=findInfo.nChannelN0;
			Toast.makeText(getApplicationContext(), "打开成功",Toast.LENGTH_SHORT).show();
			mStartTime[mWndSelected] = findInfo.startTime.hour * 3600 +
					findInfo.startTime.minute * 60 +
					findInfo.startTime.second;
			mEndTime[mWndSelected] = findInfo.endTime.hour * 3600 +
					findInfo.endTime.minute * 60 +
					findInfo.endTime.second;
			mTotalTime[mWndSelected] = (mEndTime[mWndSelected] - mStartTime[mWndSelected]);
			timeSeek.setMax(mTotalTime[mWndSelected]);
			timeSeek.setProgress(0);
			getMySurface(mWndSelected).setOnPlayBackPosListener(onPlayBackPosLs);
			System.out.println("录像长度:"+mTotalTime[mWndSelected]+" "+mStartTime[mWndSelected]+" "+mEndTime[mWndSelected]);
			myListPart.setVisibility(View.INVISIBLE);
		}else {
			Toast.makeText(getApplicationContext(), "打开失败，请检查输入时间是否正确...",Toast.LENGTH_SHORT).show();
		}
	}
	public boolean compareTime(TimeObject startTime,TimeObject endTime){
		if(startTime.getYear()>endTime.getYear())
			return false;
		if(startTime.getMonth()>endTime.getMonth())
			return false;
		if(startTime.getDay()>endTime.getDay())
			return false;
		if(startTime.getHour()>endTime.getHour())
			return false;
		if(startTime.getMinute()>endTime.getMinute())
			return false;
		return true;
	}
	/*
	 * 搜索录像文件
	 */
	private ArrayList<HashMap<String,Object>> onSearchFile() {
		mFileDataList.clear();
		int count = 0;
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		FindInfo findInfo = new FindInfo();
		Date date = new Date(System.currentTimeMillis());
		findInfo.nChannelN0 = 0;
		findInfo.nFileType = FileType.SDK_RECORD_ALL;
		findInfo.startTime.year = date.getYear() + 1900;
		findInfo.startTime.month = date.getMonth() + 1;
		findInfo.startTime.day = date.getDate();
		findInfo.startTime.hour = 0;
		findInfo.endTime.year = date.getYear() + 1900;
		findInfo.endTime.month = date.getMonth() + 1;
		findInfo.endTime.day = date.getDate();
		findInfo.endTime.hour = 24;
		do{
			FileData[] filedata = new FileData[64];
			for(int i = 0 ; i < 64; ++i)
				filedata[i] = new FileData();
			count = mNetSdk.FindFile(mLoginId,findInfo, filedata,256, 5000);
			if(count == 64) {
				findInfo.startTime.year = filedata[63].stEndTime.year;
				findInfo.startTime.month = filedata[63].stEndTime.month;
				findInfo.startTime.day = filedata[63].stEndTime.day;
				findInfo.startTime.hour = filedata[63].stEndTime.hour;
				findInfo.startTime.minute = filedata[63].stEndTime.minute;
				findInfo.startTime.second = filedata[63].stEndTime.second;
			}
			for(int j = 0 ; j < count; ++j) {
				if(count <= 64) {
					String starttime = String.format("%02d.%02d.%02d", filedata[j].stBeginTime.hour,
									filedata[j].stBeginTime.minute,
									filedata[j].stBeginTime.second);
					mFileDataList.add(filedata[j]);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("filename",starttime);
					map.put("filetype",filedata[j].filetype);
					list.add(map);
				}else
					break;
			}
		}while(count == 64);
		return list;
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

	private MySurfaceView getMySurface(int position){
		switch(position){
			case 0:return mWndsHolder.v1;
			case 1:return mWndsHolder.v2;
			case 2:return mWndsHolder.v3;
			case 3:return mWndsHolder.v4;
			default:return null;
		}
	}
	private void onStopChn(int pos) {
    	switch(pos){
		case 0:mWndsHolder.v1.onStop();
			break;
		case 1:mWndsHolder.v2.onStop();
			break;
		case 2:mWndsHolder.v3.onStop();
			break;
		case 3:mWndsHolder.v4.onStop();
			break;
		case 4:
			mWndsHolder.v1.onStop();
			mWndsHolder.v2.onStop();
			mWndsHolder.v3.onStop();
			mWndsHolder.v4.onStop();
			break;
		default:
			pos = 0;
			break;
    	}
    	if(pos<4){
    		if(mPlayState>0)
    			mPlayState=0;
    		ReviewState[pos] = 0;
    		Channel[pos] = -1;
    		time_tv.setText("00:00:00");
    		timeSeek.setProgress(0);
    		if( mlPlayHandle[pos] > 0) {
        		mNetSdk.StopPlayBack(mlPlayHandle[pos]);
        		mlPlayHandle[pos] = 0;
        	}
    	}else{
    		for(int i= 0;i <= 3;i++){
    			ReviewState[i]=0;
        		Channel[i]=-1;
        		if(mlPlayHandle[i] > 0){
        			mNetSdk.StopPlayBack(mlPlayHandle[i]);
        			mlPlayHandle[i] = 0;
        		}
    		}
    		timeSeek.setProgress(0);
    		time_tv.setText("00:00:00");
    	}
    	
    }
	private Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				time_tv.setText(msg.obj.toString());
				break;
			case 2:
				time_tv.setText("00:00:00");
				break;
			default:
				break;
			}
		}
	};
	private OnPlayBackPosListener onPlayBackPosLs = new OnPlayBackPosListener() {
		@Override
		public void onPlayBackPos(int pos) {
			if(ReviewState[mWndSelected]==1){
				if(pos < 0)
					return;
				if(pos > mEndTime[mWndSelected])
					pos -= 3600;
				int mCurTime = pos - mStartTime[mWndSelected];
				timeSeek.setProgress(mCurTime);
				int hour=pos/3600;
				int minute= (pos % 3600) / 60;
				int second=(pos % 3600) % 60;
				String time = String.format("%02d:%02d:%02d", hour,minute,second);
				Message msg=Message.obtain();
				msg.what=1;
				msg.obj=time;
				myHandler.sendMessage(msg);
			}
			else{
				timeSeek.setProgress(0);
				Message msg=Message.obtain();
				msg.what=2;
				myHandler.sendMessage(msg);
			}
		}
	};
	
	private com.xm.video.MySurfaceView.MyOneCallBack myOneCallBack = new com.xm.video.MySurfaceView.MyOneCallBack() {
		@Override
		public void onClick(final int arg0, MotionEvent arg1) {
			mWndSelected=arg0;
			if(ReviewState[arg0]==1){
				timeSeek.setVisibility(View.VISIBLE);
			}else{ 
				timeSeek.setVisibility(View.INVISIBLE);
			}
		}
		
	};
}
