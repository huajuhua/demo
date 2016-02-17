package com.example.netsdkdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.xm.CONFIG_EncodeAbility;
import com.xm.MyConfig;
import com.xm.MyConfig.SDK_CAPTURE_SIZE;
import com.xm.MyConfig.SDK_CAPTURE_SIZE_t;
import com.xm.SDK_CONFIG_NET_COMMON;
import com.xm.SDK_CONFIG_NORMAL;
import com.xm.MyConfig.SdkConfigType;
import com.xm.NetSdk;
import com.xm.SDK_CONFIG_ENCODE_SIMPLIIFY;

public class EncodeConfig extends Activity implements OnClickListener{
	private ViewHolder mViewHolder;
	private PopupWindow mresolution_pw;					//�ֱ���ѡ��
	private PopupWindow mfps_pw;						//֡��ѡ��
	private PopupWindow mquality_pw;					//����ѡ��
	private PopupWindow ex_mresolution_pw;              //�������ֱ���ѡ��
	private PopupWindow ex_mfps_pw;						//������֡��ѡ��
	private PopupWindow ex_mquality_pw;					//����������ѡ��
	private PopupWindow mdisplay_pw;
	private SDK_CONFIG_ENCODE_SIMPLIIFY[] mEncodeInfos;
	private CONFIG_EncodeAbility mEncodeAbility;		//����������
	private SDK_CONFIG_NET_COMMON config_net;				
	private ArrayList<HashMap<String, String>> display_data;
	private List<Integer> mresolutions;					//���֧�ֵ��������ֱ���
	private List<Integer> ex_mresolutions;
	private SDK_CONFIG_NORMAL config_normal;             //����
	private int mMain_MaxFPS;							 //���������֡��
	private int mSub_MaxFPS;							 //���������֡��
	private NetSdk mNetSdk;
	private long mLoginId;
	private boolean mbaudio;
	private ExecutorService mThreads;	
	private Handler mHandler;
	private AlertDialog mWaitDlg;						//�ȴ�
	private int miStandard;								//��ʽ�� P�� 25 N�� 30
	private ListView resolution_lv;
	private ArrayList<HashMap<String, String>> resolution_data;
	private ListView ex_resolution_lv;
	private ArrayList<HashMap<String, String>> ex_resolution_data;
	private ListView fps_lv;
	private ArrayList<HashMap<String, String>> fps_data;
	private FPSAlgorithm fpsAlgorithm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.encodeconfig);
		init();
		getEncodeConfig();
		initData();
		initLayout();
		mHandler = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0://
					mWaitDlg.dismiss();
					break;
				default:
					break;
				}
			}
		};
	}
	//��ʼ��
	private void init() {
		Intent it = getIntent();
		mLoginId = it.getLongExtra("LoginId", 0);
		System.out.println("mLoginId :"+mLoginId+"");
		mNetSdk = NetSdk.getInstance();
		mThreads = Executors.newCachedThreadPool();
		if(mLoginId == 0)
			return;
	}
	//��ʼ������
	private void initLayout() {
		mViewHolder = new ViewHolder();
		//������
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
		//������
		mViewHolder.ex_audio_rl = (RelativeLayout) findViewById(R.id.ex_audio_rl);
		mViewHolder.ex_audio_rl.setOnClickListener(this);
		mViewHolder.ex_quality_rl = (RelativeLayout) findViewById(R.id.ex_quality_rl);
		mViewHolder.ex_quality_rl.setOnClickListener(this);
		mViewHolder.ex_fps_rl = (RelativeLayout) findViewById(R.id.ex_fps_rl);
		mViewHolder.ex_fps_rl.setOnClickListener(this);
		mViewHolder.ex_resolution_rl = (RelativeLayout) findViewById(R.id.ex_resolution_rl);
		mViewHolder.ex_resolution_rl.setOnClickListener(this);
		mViewHolder.ex_resolution_tv= (TextView) findViewById(R.id.ex_resolution_tv);
		mViewHolder.ex_fps_tv = (TextView) findViewById(R.id.ex_fps_tv);
		mViewHolder.ex_quality_tv = (TextView) findViewById(R.id.ex_quality_tv);
		mViewHolder.ex_audio_tv = (TextView) findViewById(R.id.ex_audio_tv);
		mViewHolder.ex_audio_iv = (ImageView) findViewById(R.id.ex_audio_iv);
		mViewHolder.ok = (Button) findViewById(R.id.ok);
		mViewHolder.ok.setOnClickListener(this);
		mViewHolder.cancel = (Button) findViewById(R.id.cancel);
		mViewHolder.cancel.setOnClickListener(this);
		//��ʾ
		mViewHolder.display_rl=(RelativeLayout) findViewById(R.id.display);
		mViewHolder.display_tv=(TextView) findViewById(R.id.display_tv);
		mViewHolder.display_rl.setOnClickListener(this);
		resolution_lv = new ListView(getApplicationContext());
		resolution_data = new ArrayList<HashMap<String,String>>();
		int i = 0;
		for(i = 0 ; i < mresolutions.size(); ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.Capture_Size[mresolutions.get(i)]);
			resolution_data.add(map);
		}
		SimpleAdapter adpater = new SimpleAdapter(getApplicationContext(), resolution_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		resolution_lv.setAdapter(adpater);
		//�������ֱ���ѡ���¼�����
		resolution_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mEncodeInfos[0].dstMainFmt.vfFormat.iResolution = mresolutions.get(arg2);
				mViewHolder.resolution_tv.setText(MyConfig.Capture_Size[mresolutions.get(arg2)]);
				//�ı丱�����ֱ���ѡ��
				onChangeMainInfo();
				mresolution_pw.dismiss();
			}
		});
		mresolution_pw = new PopupWindow(resolution_lv,300,300);
		mresolution_pw.setBackgroundDrawable(new BitmapDrawable());   
		mresolution_pw.setFocusable(true);
		mresolution_pw.setOutsideTouchable(true);
		fps_data = new ArrayList<HashMap<String,String>>();
		//P��
		if(config_normal.iVideoFormat==0)
		{
			mMain_MaxFPS = 25;
			mSub_MaxFPS = 25;
			miStandard = 0;
		}//N��
		else if(config_normal.iVideoFormat==1)
		{
			mMain_MaxFPS = 30;
			mSub_MaxFPS = 30;
			miStandard = 1;
		}
		//������֡��
		fpsAlgorithm = new FPSAlgorithm();
		fpsAlgorithm.setMaxEncodePower(mEncodeAbility.nMaxPowerPerChannel[0]);
		//��ȡ���������֡��
		int max_fps = fpsAlgorithm.getMainMaxFPS(MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution],
				mEncodeInfos[0].dstExtraFmt.vfFormat.nFPS,
				MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstMainFmt.vfFormat.iResolution]);
		mMain_MaxFPS = max_fps > mMain_MaxFPS ? mMain_MaxFPS : max_fps;
		for(i = 0 ; i <mMain_MaxFPS; ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.FPS[i]);
			fps_data.add(map);
		}
		fps_lv = new ListView(getApplicationContext());
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
				mViewHolder.fps_tv.setText(""+mEncodeInfos[0].dstMainFmt.vfFormat.nFPS);
				mfps_pw.dismiss();
			}
		});
		mfps_pw = new PopupWindow(fps_lv,300,300);
		mfps_pw.setBackgroundDrawable(new BitmapDrawable());   
		mfps_pw.setFocusable(true);
		mfps_pw.setOutsideTouchable(true);
		ArrayList<HashMap<String, String>> quality_data = new ArrayList<HashMap<String,String>>();
		for(i = 0 ; i < MyConfig.QUALITY.length; ++i) {
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
		mquality_pw = new PopupWindow(quality_lv,300,300);
		mquality_pw.setBackgroundDrawable(new BitmapDrawable());   
		mquality_pw.setFocusable(true);
		mquality_pw.setOutsideTouchable(true);
		//��������������
		ex_resolution_lv = new ListView(getApplicationContext());
		ex_resolution_data = new ArrayList<HashMap<String,String>>();
		int j= 0;
		System.out.print("ex_mre"+ex_mresolutions.size());
		for(j = 0 ; j <ex_mresolutions.size(); ++j) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.Capture_Size[ex_mresolutions.get(j)]);
			ex_resolution_data.add(map);
		}
		SimpleAdapter ex_adpater = new SimpleAdapter(getApplicationContext(), ex_resolution_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		ex_resolution_lv.setAdapter(ex_adpater);
		ex_resolution_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution =ex_mresolutions.get(arg2);
				mViewHolder.ex_resolution_tv.setText(MyConfig.Capture_Size[ex_mresolutions.get(arg2)]);
				ex_mresolution_pw.dismiss();
			}
		});
		ex_mresolution_pw = new PopupWindow();
		ex_mresolution_pw.setContentView(ex_resolution_lv);
		ex_mresolution_pw.setWidth(300);
		ex_mresolution_pw.setHeight(300);
		ex_mresolution_pw.setBackgroundDrawable(new BitmapDrawable());   
		ex_mresolution_pw.setFocusable(true);
		ex_mresolution_pw.setOutsideTouchable(true);
		//֡��
		fpsAlgorithm.setMaxEncodePower(mEncodeAbility.nMaxPowerPerChannel[0]);
		//��ȡ���������֡��
		max_fps = fpsAlgorithm.getSubMaxFPS(MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstMainFmt.vfFormat.iResolution],
				mEncodeInfos[0].dstMainFmt.vfFormat.nFPS,
				MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution]);
		mSub_MaxFPS = max_fps > mSub_MaxFPS ? mSub_MaxFPS : max_fps;
		ArrayList<HashMap<String, String>> ex_fps_data = new ArrayList<HashMap<String,String>>();
		for(i = 0 ; i < mSub_MaxFPS; ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.FPS[i]);
			ex_fps_data.add(map);
		}
		ListView ex_fps_lv = new ListView(getApplicationContext());
		ex_fps_lv.setFocusable(true);
		ex_adpater = new SimpleAdapter(getApplicationContext(),ex_fps_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		ex_fps_lv.setAdapter(ex_adpater);
		ex_fps_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mEncodeInfos[0].dstExtraFmt.vfFormat.nFPS = arg2 + 1;
				mViewHolder.ex_fps_tv.setText(""+mEncodeInfos[0].dstExtraFmt.vfFormat.nFPS);
				ex_mfps_pw.dismiss();
			}
		});
		ex_mfps_pw = new PopupWindow(ex_fps_lv,300,300);
		ex_mfps_pw.setBackgroundDrawable(new BitmapDrawable());   
		ex_mfps_pw.setFocusable(true);
		ex_mfps_pw.setOutsideTouchable(true);
		ArrayList<HashMap<String, String>> ex_quality_data = new ArrayList<HashMap<String,String>>();
		for(i = 0 ; i < MyConfig.QUALITY.length; ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.QUALITY[i]);
			ex_quality_data.add(map);
		}
		ListView ex_quality_lv = new ListView(getApplicationContext());
		ex_adpater = new SimpleAdapter(getApplicationContext(), ex_quality_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		ex_quality_lv.setAdapter(ex_adpater);
		ex_quality_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mEncodeInfos[0].dstExtraFmt.vfFormat.iQuality = arg2 + 1;
				mViewHolder.ex_quality_tv.setText(MyConfig.QUALITY[arg2]);
				ex_mquality_pw.dismiss();
			}
		});
		ex_mquality_pw = new PopupWindow(ex_quality_lv,300,300);
		ex_mquality_pw.setBackgroundDrawable(new BitmapDrawable());   
		ex_mquality_pw.setFocusable(true);
		ex_mquality_pw.setOutsideTouchable(true);
		//��ʾ����
		display_data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("item", "����Ӧ");
		display_data.add(map);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("item", "��������");
		display_data.add(map2);
		HashMap<String, String> map3 = new HashMap<String, String>();
		map3.put("item", "��������");
		display_data.add(map3);
		HashMap<String, String> map4 = new HashMap<String, String>();
		map4.put("item", "��������");
		display_data.add(map4);
		ListView display_lv = new ListView(getApplicationContext());
		adpater = new SimpleAdapter(getApplicationContext(), display_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		display_lv.setAdapter(adpater);
		display_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				config_net.TransferPlan = arg2;
				System.out.println("TransferPlan:"+config_net.TransferPlan);
				mViewHolder.display_tv.setText(display_data.get(arg2).get("item"));
				mdisplay_pw.dismiss();
			}
		});
		mdisplay_pw = new PopupWindow(display_lv,300,300);
		mdisplay_pw.setBackgroundDrawable(new BitmapDrawable());   
		mdisplay_pw.setFocusable(true);
		mdisplay_pw.setOutsideTouchable(true);
		
		System.out.println("resolution:"+mEncodeInfos[0].dstMainFmt.vfFormat.iResolution);
		//����������
		if(mEncodeInfos[0].dstMainFmt.vfFormat.iResolution >= 0)
			mViewHolder.resolution_tv.setText(""+MyConfig.Capture_Size[mEncodeInfos[0].dstMainFmt.vfFormat.iResolution]);
		mViewHolder.fps_tv.setText(""+mEncodeInfos[0].dstMainFmt.vfFormat.nFPS);
		if(mEncodeInfos[0].dstMainFmt.vfFormat.iQuality-1<0)
			return ;
		mViewHolder.quality_tv.setText(""+MyConfig.QUALITY[mEncodeInfos[0].dstMainFmt.vfFormat.iQuality - 1]);
		mbaudio = mEncodeInfos[0].dstMainFmt.bAudioEnable;
		if(mbaudio)
			mViewHolder.audio_iv.setImageResource(R.drawable.autologin_on);
		else
			mViewHolder.audio_iv.setImageResource(R.drawable.autologin_off);
		//����������
		if(mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution >= 0)
			mViewHolder.ex_resolution_tv.setText(""+MyConfig.Capture_Size[mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution]);
		mViewHolder.ex_fps_tv.setText(""+mEncodeInfos[0].dstExtraFmt.vfFormat.nFPS);
		if(mEncodeInfos[0].dstExtraFmt.vfFormat.iQuality-1<0)
			return ;
		else
			mViewHolder.ex_quality_tv.setText(""+MyConfig.QUALITY[mEncodeInfos[0].dstExtraFmt.vfFormat.iQuality-1]);
		mbaudio = mEncodeInfos[0].dstExtraFmt.bAudioEnable;
		if(mbaudio)
			mViewHolder.ex_audio_iv.setImageResource(R.drawable.autologin_on);
		else
			mViewHolder.ex_audio_iv.setImageResource(R.drawable.autologin_off);
		//mViewHolder.display_tv.setText(display_data.get(config_net.TransferPlan).get("item"));		
	}
	private void initData() {
		config_normal=new SDK_CONFIG_NORMAL();
		if(mNetSdk.H264DVRGetDevConfig(mLoginId,SdkConfigType.E_SDK_CONFIG_SYSNORMAL,-1, config_normal, 5000)){
			System.out.println("iVideoFormat:"+config_normal.iVideoFormat);
		}
		mEncodeAbility = new CONFIG_EncodeAbility();
		//��ȡ����������
		if(mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_ABILTY_ENCODE,-1, mEncodeAbility, 5000)) {
			System.out.println("ImageSizePerChannel:"+mEncodeAbility.ImageSizePerChannel[0]);
			System.out.println("ExImageSizePerChannel:"+mEncodeAbility.ExImageSizePerChannel[0]);
		}
		//��ȡ�ֱ�������
		mresolutions = new ArrayList<Integer>();
		//��ȡ�������ķֱ�������
		ex_mresolutions = new ArrayList<Integer>();
		//��ȡ�ֱ�������
		int resolution_mask = mEncodeAbility.ImageSizePerChannel[0];
		int resolution_pos = 0;
		//���������ȡ֧����Щ�ֱ���
		do {
			if((resolution_mask & 1) == 1) {
				mresolutions.add(resolution_pos);
			}	
			resolution_pos++;
		}while((resolution_mask = (resolution_mask >> 1)) > 0);
		//��ȡ�������ķֱ�������
		System.out.println("iResolution:"+mEncodeInfos[0].dstMainFmt.vfFormat.iResolution);
		int ex_resolution_mask = mEncodeAbility.ExImageSizePerChannelEx[0][mEncodeInfos[0].dstMainFmt.vfFormat.iResolution];//
		System.out.println("ex_resolution_mask:"+ex_resolution_mask);
		int ex_resolution_pos = 0;
		//���������ȡ֧����Щ�ֱ���
		do {
			if((ex_resolution_mask & 1) == 1) {
				ex_mresolutions.add(ex_resolution_pos);
			}	
			ex_resolution_pos++;
		}while((ex_resolution_mask = (ex_resolution_mask >> 1)) > 0);
	}
	//��ȡ�豸������Ϣ
	private void getEncodeConfig() {
		mEncodeInfos = new SDK_CONFIG_ENCODE_SIMPLIIFY[32];
		for(int i = 0 ; i < 32; ++i) {
			mEncodeInfos[i] = new SDK_CONFIG_ENCODE_SIMPLIIFY();
		}
		mNetSdk.H264DVRGetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_SYSENCODE_SIMPLIIFY,-1, mEncodeInfos, 5000);
	}
	//�ı���������Ϣ
	private void onChangeMainInfo() {
		//��ȡ�������ķֱ�������
		int ex_resolution_mask = mEncodeAbility.ExImageSizePerChannelEx[0][mEncodeInfos[0].dstMainFmt.vfFormat.iResolution];//mEncodeAbility.ExImageSizePerChannel[0];
		int ex_resolution_pos = 0;
		ex_mresolutions.clear();
		//���������ȡ֧����Щ�ֱ���
		do {
			if((ex_resolution_mask & 1) == 1) {
				ex_mresolutions.add(ex_resolution_pos);
			}	
			ex_resolution_pos++;
		}while((ex_resolution_mask = (ex_resolution_mask >> 1)) > 0);
		//��������������
		int i = 0;
		System.out.print("ex_mre"+ex_mresolutions.size());
		ex_resolution_data.clear();
		for(i = 0 ; i <ex_mresolutions.size(); ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.Capture_Size[ex_mresolutions.get(i)]);
			ex_resolution_data.add(map);
		}
		SimpleAdapter ex_adpater = new SimpleAdapter(getApplicationContext(), ex_resolution_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		ex_resolution_lv.setAdapter(ex_adpater);
		
		//������֡��
		fpsAlgorithm.setMaxEncodePower(mEncodeAbility.nMaxPowerPerChannel[0]);
		//��ȡ���������֡��
		int max_fps = fpsAlgorithm.getMainMaxFPS(MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution],
				mEncodeInfos[0].dstExtraFmt.vfFormat.nFPS,
				MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstMainFmt.vfFormat.iResolution]);
		if(miStandard == 0)
			mMain_MaxFPS = max_fps > 25 ? 25 : max_fps;
		else
			mMain_MaxFPS = max_fps > 30 ? 30 : max_fps;
		fps_data.clear();
		for(i = 0 ; i <mMain_MaxFPS; ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.FPS[i]);
			fps_data.add(map);
		}
		SimpleAdapter adpater = new SimpleAdapter(getApplicationContext(), fps_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		fps_lv.setAdapter(adpater);
		mEncodeInfos[0].dstMainFmt.vfFormat.nFPS = 
				mEncodeInfos[0].dstMainFmt.vfFormat.nFPS > mMain_MaxFPS ? mMain_MaxFPS : mEncodeInfos[0].dstMainFmt.vfFormat.nFPS;
		mViewHolder.fps_tv.setText(""+mEncodeInfos[0].dstMainFmt.vfFormat.nFPS);
	}
	private void onChangeSubInfo() {
		//������֡��
		fpsAlgorithm.setMaxEncodePower(mEncodeAbility.nMaxPowerPerChannel[0]);
		//��ȡ���������֡��
		fpsAlgorithm.setMaxEncodePower(mEncodeAbility.nMaxPowerPerChannel[0]);
		//��ȡ���������֡��
		int max_fps = fpsAlgorithm.getSubMaxFPS(MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstMainFmt.vfFormat.iResolution],
				mEncodeInfos[0].dstMainFmt.vfFormat.nFPS,
				MyConfig.P_CPATURE_SIZE[mEncodeInfos[0].dstExtraFmt.vfFormat.iResolution]);
		mSub_MaxFPS = max_fps > mSub_MaxFPS ? mSub_MaxFPS : max_fps;
		ArrayList<HashMap<String, String>> ex_fps_data = new ArrayList<HashMap<String,String>>();
		for(int i = 0 ; i < mSub_MaxFPS; ++i) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("item", MyConfig.FPS[i]);
			ex_fps_data.add(map);
		}
		SimpleAdapter ex_adpater = new SimpleAdapter(getApplicationContext(), fps_data,
				R.layout.simplelist, new String[]{"item"}, new int[]{R.id.item});
		fps_lv.setAdapter(ex_adpater);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.resolution_rl://�ֱ���
			mresolution_pw.showAsDropDown(mViewHolder.resolution_rl);
			break;
		case R.id.fps_rl://֡��
			mfps_pw.showAsDropDown(mViewHolder.fps_rl);
			break;
		case R.id.quality_rl://����
			mquality_pw.showAsDropDown(mViewHolder.quality_rl);
			break;
		case R.id.audio_rl://��Ƶ
			if(mbaudio) {
				mViewHolder.audio_iv.setImageResource(R.drawable.autologin_off);
			}
			else {
				mViewHolder.audio_iv.setImageResource(R.drawable.autologin_on);
			}
			mbaudio = !mbaudio;
			mEncodeInfos[0].dstMainFmt.bAudioEnable = mbaudio;
			break;
		case R.id.ex_resolution_rl://�������ֱ���
			ex_mresolution_pw.showAsDropDown(mViewHolder.ex_resolution_rl);
			break;
		case R.id.ex_fps_rl://������֡��
			ex_mfps_pw.showAsDropDown(mViewHolder.ex_fps_rl);
			break;
		case R.id.ex_quality_rl://����������
			ex_mquality_pw.showAsDropDown(mViewHolder.ex_quality_rl);
			break;
		case R.id.ex_audio_rl://��������Ƶ
			if(mbaudio) {
				mViewHolder.ex_audio_iv.setImageResource(R.drawable.autologin_off);
			}
			else {
				mViewHolder.ex_audio_iv.setImageResource(R.drawable.autologin_on);
			}
			mbaudio = !mbaudio;
			mEncodeInfos[0].dstExtraFmt.bAudioEnable = mbaudio;
			break;
		case R.id.display:
			mdisplay_pw.showAsDropDown(mViewHolder.display_rl);
			break;
		case R.id.ok://����
			if(onSave())
				Toast.makeText(getApplicationContext(), "����ɹ�", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
			break;
		case R.id.cancel://ȡ��
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(mresolution_pw.isShowing())
			mresolution_pw.dismiss();
		if(mfps_pw.isShowing())
			mfps_pw.dismiss();
		if(mquality_pw.isShowing())
			mquality_pw.dismiss();
		return super.onTouchEvent(event);
	}
	private boolean onSave() {
		boolean bret = mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_SYSENCODE_SIMPLIIFY,
				-1, mEncodeInfos, 1000);
	//	boolean net_bret=mNetSdk.H264DVRSetDevConfig(mLoginId, SdkConfigType.E_SDK_CONFIG_SYSNET, -1,config_net,2000);
	
		return bret;
		
	}
	class ViewHolder {
		//������
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
		//������
		RelativeLayout ex_resolution_rl;
		RelativeLayout ex_fps_rl;
		RelativeLayout ex_quality_rl;
		RelativeLayout ex_audio_rl;
		TextView ex_resolution_tv;
		TextView ex_fps_tv;
		TextView ex_quality_tv;
		TextView ex_audio_tv;
		ImageView ex_audio_iv;
		//��ʾ
		RelativeLayout display_rl;
		TextView display_tv;
	}
	
}
