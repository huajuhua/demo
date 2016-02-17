package com.ark.newark_vision1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ark.component.*;
import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.MyConfig;
import com.xm.NetSdk;
import com.xm.video.MySurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MyActivity extends Activity implements SlideBar.OnTriggerListener, View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private MySurfaceView vv1, vv2, vv3, vv4, vv1_full, vv2_full, vv3_full, vv4_full;
    private TextView oxy_txt, wendu_txt, shidu_txt, qiya_txt, yangqi_day;
    private OxyView oxyvalue1;
    private ImageView window1, window2, window3, window4, light1, light2, light3, light4, wind, door;
    private Typeface face;
    private boolean window1_bool = false, window2_bool = false,
            window3_bool = false, window4_bool = false, light1_bool = false,
            light2_bool = false, light3_bool = false, light4_bool = false, wind_bool = false, door_bool = false, emer_bool = false, emer_bool_soft = false, power = false;
    private GradientView gradientView;
    private long mloginid1 = 0;
    private long[] mplayhandle1;
    private long mloginid2 = 0;
    private long[] mplayhandle2;
    private long mloginid3 = 0;
    private long[] mplayhandle3;
    private long mloginid4 = 0;
    private long[] mplayhandle4;
    private NetSdk mNetSdk = null;
    private WndsHolder mWndsHolder;
    private int mSocketStyle = MyConfig.SocketStyle.TCPSOCKET;
    private int mWndSelected = 0;
    private android.os.Handler datahandler;
    private Socket datasocket = null;
    private WaterView water_shengyu;
    private ImageView emer_img;
    private boolean emer_img_bool = false;
    private ImageView center_img;
    private Animation animation;
    private ImageView battary_on;
    private boolean vv1_bool = false, vv2_bool = false, vv3_bool = false, vv4_bool = false,
            vv1_full_bool = false, vv2_full_bool = false, vv3_full_bool = false, vv4_full_bool = false;
    private ImageView video_img1, video_img2, video_img3, video_img4;
    private Dialog dialog;
    private Battary battary1;
    private ImageView window1_open, window1_close, window2_open, window2_close,
            window3_open, window3_close, window4_open, window4_close;
    private ImageView window1_invisible, window2_invisible, window3_invisible, window4_invisible,
            water_invisible, wind_invisible, door_invisible;
    private Animation hyperspaceJumpAnimation_window1;
    private Animation hyperspaceJumpAnimation_window2;
    private Animation hyperspaceJumpAnimation_window3;
    private Animation hyperspaceJumpAnimation_window4;
    private Animation hyperspaceJumpAnimation_wind;
    private int window1_tag = 0;
    private int window2_tag = 1;
    private int window3_tag = 2;
    private int window4_tag = 3;
    private int socket_tag = 9000;
    private Thread mythread;
    private Handler sokecthandler;
    private ImageView water;
    private boolean water_bool = false;
    private boolean isconnect;
    private boolean video1_bool;
    private boolean video2_bool;
    private boolean video3_bool;
    private boolean video4_bool;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        uiti();

        SlideBar slideToUnLock = (SlideBar) this.findViewById(R.id.slideBar);
        gradientView = (GradientView) this.findViewById(R.id.gradientView);
        slideToUnLock.setOnTriggerListener(this);

        init();//摄像头

        datahandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what != 0) {
                    updateui(msg.getData().getByteArray("databuffer"));
                }
            }

        };

        sokecthandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isconnect = startPing("192.168.1.134");
                if (isconnect) {
                    new Thread(runnable).start();
                }
            }
        };

        if (datasocket == null) {
            new Thread(runnable).start();
        } else {
            order_query();
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            {
                {
                    {
                        try {
//                            Log.d("","-----------------1111----------");
                            datasocket = null;
                            datasocket = new Socket("192.168.1.134", 7777);//数据
                            order_query();
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                isconnect = startPing("192.168.1.134");
                                if (datasocket != null) {
                                    if (isconnect) {
                                        answer();
                                    }

                                }

                            }
                        } catch (IOException e) {
                            sokecthandler.sendEmptyMessage(0);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

//    Thread MyThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            {
//                try {
//                    datasocket = new Socket("192.168.1.134", 7777);//数据
//                    boolean status = InetAddress.getByName("192.168.1.134").isReachable(3000);
//                    Log.d("","-----------00000-------------"+status);
//                    order_query();
//                    for (int i=0;i<Integer.MAX_VALUE;i++){
//                        if (datasocket != null){
//                            answer();
//                            Log.d("","-----------123-------------");
//                        }else {
//                            Log.d("","-----------456-------------");
//                            datasocket = new Socket("192.168.1.134", 7777);
//                            if (datasocket != null){
//                                order_query();
//                                answer();
//                            }
//                        }
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    });

    //查询

    public void order_query() {
        byte[] buffer_control = new byte[12];
        buffer_control[0] = (byte) 1;
        buffer_control[1] = (byte) 0;
        buffer_control[2] = (byte) 0;
        buffer_control[3] = (byte) 0;
        buffer_control[4] = (byte) 0;
        buffer_control[5] = (byte) 10;
        buffer_control[6] = (byte) 0;
        buffer_control[7] = (byte) 0;
        buffer_control[8] = (byte) 0;
        buffer_control[9] = (byte) 0;
        buffer_control[10] = (byte) 0;
        buffer_control[11] = (byte) 0;


        try {
            datasocket.getOutputStream().write(buffer_control);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回结果
    public void answer() {
        byte[] bufferdata = new byte[1024];
        byte[] bufferheart = new byte[12];
        try {
            InputStream basedata = datasocket.getInputStream();
            int readleng = basedata.read(bufferdata);
//            Log.d("", "-------------9-----================" + readleng);
            if (readleng == -1) {
                Message msg = new Message();
                msg.what = 0;
                sokecthandler.sendMessage(msg);
            } else {
                if (readleng < 13 && readleng != -1) {
                    System.arraycopy(bufferdata, 0, bufferheart, 0, readleng);
                    if (bufferheart[5] == 1) {
                        order_heart(bufferheart);
                    }
                } else {
                    for (int i = 0; i < readleng; i++) {
//                    Log.d("", "-------------444---===============" + bufferdata[i]);
                    }
                    G.buffer = null;
                    G.buffer = bufferdata;
                    G.socket = null;
                    G.socket = datasocket;
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("databuffer", bufferdata);
                    msg.setData(bundle);
                    datahandler.sendMessage(msg);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回心跳数据
    public void order_heart(byte[] buffer) {
        try {

            datasocket.getOutputStream().write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateui(byte[] buffer) {
        String oxy = buffer[12] + "." + buffer[13];
//        Log.d("","----------------------------------3-----------------------"+buffer[32]);
        String oxyshengyu = buffer[44] + "." + buffer[45];
//        Log.d("","----------------------------------3-----------------------"+oxyshengyu);
        String buff17 = buffer[17] + "";
        if (buff17.length() > 1) {
            buff17 = buff17.substring(0, 1);
        }
        String wendu = buffer[16] + "." + buff17;
        String buff19 = buffer[19] + "";
        if (buff19.length() > 1) {
            buff19 = buff19.substring(0, 1);
        }
        String shidu = buffer[18] + "." + buff19;
        String dianci = "" + buffer[21];
        String buff23 = buffer[23] + "";
        if (buff23.length() > 1) {
            buff23 = buff23.substring(0, 1);
        }
        String shuiwei = buffer[22] + "." + buff23;

        int qiya = bigByteToInt(buffer, 24) / 100;
        if (oxy.length() > 4) {
            oxy_txt.setText(oxy.substring(0, 4));
        }
        wendu_txt.setText(wendu);
        shidu_txt.setText(shidu);
        qiya_txt.setText(qiya + "");
        water_shengyu.setwaterValue(Double.valueOf(shuiwei));
        battary1.setbattaryValue(Integer.valueOf(dianci));
        oxyvalue1.setoxyValue(Double.valueOf(oxyshengyu));
//        yangqi_day.setText(oxyshengyu);

//        Log.d("", buffer[22]+"-----"+buff23+"-------------9-----================"+shuiwei );
        if (buffer[28] == 1) {//消毒灯
            light3_bool = true;
            light3.setBackgroundResource(R.drawable.light3_on);
        } else if (buffer[28] == 0) {
            light3_bool = false;
            light3.setBackgroundResource(R.drawable.light3_off);
        }
        if (buffer[29] == 1) {//门
            door.setBackgroundResource(R.drawable.door_on);
            door_bool = true;
        } else {
            door.setBackgroundResource(R.drawable.door_off);
            door_bool = false;
        }
        if (buffer[30] == 1) {//通风换气
            wind_bool = true;
            wind.setBackgroundResource(R.drawable.wind_on);
            if (hyperspaceJumpAnimation_wind == null) {
                hyperspaceJumpAnimation_wind = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                wind.startAnimation(hyperspaceJumpAnimation_wind);
            }
        } else {
            wind_bool = false;
            wind.setBackgroundResource(R.drawable.wind_off);
            wind.clearAnimation();
            hyperspaceJumpAnimation_wind = null;
        }
        if (buffer[31] == 1) {//软件紧急模式

            emer_bool_soft = true;
        } else {
            emer_bool_soft = false;
        }
        if (buffer[32] == 1) {// 充电
            power = false;
            battary_on.setVisibility(View.INVISIBLE);
        } else {
            power = true;
            battary_on.setVisibility(View.VISIBLE);

        }
        if (buffer[33] == 1) {//灯1
            light1_bool = true;
            light1.setBackgroundResource(R.drawable.light1_on);
        } else {
            light1_bool = false;
            light1.setBackgroundResource(R.drawable.light1_off);
        }
//        if (buffer[34] == 1) {
//            light2_bool = true;
//            light2.setBackgroundResource(R.drawable.light2_on);
//        } else {
//            light2_bool = false;
//            light2.setBackgroundResource(R.drawable.light2_off);
//        }
        if (buffer[35] == 1) {
            light4_bool = true;
        } else {
            light4_bool = false;
        }
//        Log.d("","------------9------------------------"+buffer[36]);
        if (buffer[36] == 1) {
            window1.setBackgroundResource(R.drawable.window_on);
            window1.clearAnimation();
            hyperspaceJumpAnimation_window1 = null;
        } else if (buffer[36] == 0) {
            window1.setBackgroundResource(R.drawable.window_off);
            window1.clearAnimation();
            hyperspaceJumpAnimation_window1 = null;
        } else if (buffer[36] == 2) {//正在开
            // 加载动画
            if (hyperspaceJumpAnimation_window1 == null) {

                hyperspaceJumpAnimation_window1 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window1.setBackgroundResource(R.drawable.window_on);
                window1.startAnimation(hyperspaceJumpAnimation_window1);
            }
        } else if (buffer[36] == 3) {
            // 加载动画
            if (hyperspaceJumpAnimation_window1 == null) {

                hyperspaceJumpAnimation_window1 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window1.setBackgroundResource(R.drawable.window_off);
                window1.startAnimation(hyperspaceJumpAnimation_window1);
            }
        }
        if (buffer[37] == 1) {
            window2.setBackgroundResource(R.drawable.window_on);
            window2.clearAnimation();
            hyperspaceJumpAnimation_window2 = null;
        } else if (buffer[37] == 0) {
            window2.setBackgroundResource(R.drawable.window_off);
            window2.clearAnimation();
            hyperspaceJumpAnimation_window2 = null;
        } else if (buffer[37] == 2) {
            // 加载动画
            if (hyperspaceJumpAnimation_window2 == null) {

                hyperspaceJumpAnimation_window2 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window2.setBackgroundResource(R.drawable.window_on);
                window2.startAnimation(hyperspaceJumpAnimation_window2);
            }
        } else if (buffer[37] == 3) {
            // 加载动画
            if (hyperspaceJumpAnimation_window2 == null) {

                hyperspaceJumpAnimation_window2 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window2.setBackgroundResource(R.drawable.window_off);
                window2.startAnimation(hyperspaceJumpAnimation_window2);
            }
        }
        if (buffer[38] == 1) {
            window3.setBackgroundResource(R.drawable.window_on);
            window3.clearAnimation();
            hyperspaceJumpAnimation_window3 = null;
        } else if (buffer[38] == 0) {
            window3.setBackgroundResource(R.drawable.window_off);
            window3.clearAnimation();
            hyperspaceJumpAnimation_window3 = null;
        } else if (buffer[38] == 2) {
            // 加载动画
            if (hyperspaceJumpAnimation_window3 == null) {

                hyperspaceJumpAnimation_window3 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window3.setBackgroundResource(R.drawable.window_on);
                window3.startAnimation(hyperspaceJumpAnimation_window3);
            }
        } else if (buffer[38] == 3) {
            // 加载动画
            if (hyperspaceJumpAnimation_window3 == null) {

                hyperspaceJumpAnimation_window3 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window3.setBackgroundResource(R.drawable.window_off);
                window3.startAnimation(hyperspaceJumpAnimation_window3);
            }
        }
//        Log.d("","----------------------------------3-----------------------"+buffer[39]);
        if (buffer[39] == 1) {
            window4.setBackgroundResource(R.drawable.window_on);
            window4.clearAnimation();
            hyperspaceJumpAnimation_window4 = null;
        } else if (buffer[39] == 0) {
            window4.setBackgroundResource(R.drawable.window_off);
            window4.clearAnimation();
            hyperspaceJumpAnimation_window4 = null;
        } else if (buffer[39] == 2) {
            // 加载动画
            if (hyperspaceJumpAnimation_window4 == null) {

                hyperspaceJumpAnimation_window4 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window4.setBackgroundResource(R.drawable.window_on);
                window4.startAnimation(hyperspaceJumpAnimation_window4);
            }
        } else if (buffer[39] == 3) {
            // 加载动画
            if (hyperspaceJumpAnimation_window4 == null) {

                hyperspaceJumpAnimation_window4 = AnimationUtils.loadAnimation(
                        this, R.anim.loading);
                window4.setBackgroundResource(R.drawable.window_off);
                window4.startAnimation(hyperspaceJumpAnimation_window4);
            }
        }
        if (buffer[40] == 1) { //手动紧急模式
            emer_bool = true;
        } else {
            emer_bool = false;
        }
        if (emer_bool) {
            emer_img.setBackgroundResource(R.drawable.top_emer);
            emer_img_bool = true;
            emerui(true);
            if (animation == null) {
                center_img.setVisibility(View.VISIBLE);
                animation = new AlphaAnimation(0.1f, 1.0f);
                animation.setDuration(1000);
                animation.setRepeatCount(Integer.MAX_VALUE);
                animation.setRepeatMode(Animation.REVERSE);
                center_img.startAnimation(animation);
            }

        } else {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
                order_s(18);
            }

//            order_s(18);
//            center_img.setVisibility(View.INVISIBLE);
//            center_img.clearAnimation();
//            emer_img.setBackgroundResource(R.drawable.top_nomal);
//            emer_img_bool = false;

            if (emer_bool_soft) {
                emer_img.setBackgroundResource(R.drawable.top_emer);
                emer_img_bool = true;
                emerui(true);
                if (animation == null) {
                    center_img.setVisibility(View.VISIBLE);
                    animation = new AlphaAnimation(0.1f, 1.0f);
                    animation.setDuration(1000);
                    animation.setRepeatCount(Integer.MAX_VALUE);
                    animation.setRepeatMode(Animation.REVERSE);
                    center_img.startAnimation(animation);
                }
            } else {
                emer_img.setBackgroundResource(R.drawable.top_nomal);
                emer_img_bool = false;
                emerui(false);
                center_img.clearAnimation();
                animation = null;
                center_img.setVisibility(View.INVISIBLE);
            }

        }
    }

    private boolean startPing(String ip) {
        Log.e("Ping", "startPing...");
        boolean success = false;
        Process p = null;

        try {
            p = Runtime.getRuntime().exec("ping -c 1 -i 0.2 -W 1 " + ip);
            int status = p.waitFor();
            if (status == 0) {
                success = true;
            } else {
                success = false;
            }
        } catch (IOException e) {
            success = false;
        } catch (InterruptedException e) {
            success = false;
        } finally {
            p.destroy();
        }

        return success;
    }


    /*
     * 视频监控
     */
    private void init() {
        mNetSdk = NetSdk.getInstance();

//        mp = MediaPlayer.create(SecondPageFragment.this.getActivity(), R.raw.jingbao);
        /**
         * 报警回调
         */
        mNetSdk.setOnDisConnectListener(new NetSdk.OnDisConnectListener() {
            @Override
            public void onDisConnect(int i, long l, byte[] bytes, long l2) {
                if (mloginid1 == l) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                video1_bool = startPing("192.168.1.50");
                                if (video1_bool) {
                                    break;
                                }
                            }
                            if (video1_bool) {
                                Stop(mWndsHolder.vv1, mplayhandle1);
                                DevInfo devInfo = new DevInfo();
                                devInfo.Ip = "192.168.1.50";
                                devInfo.TCPPort = 34567;
                                devInfo.UserName = "admin".getBytes();
                                devInfo.PassWord = "";
                                mloginid1 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                                mplayhandle1 = new long[devInfo.ChanNum];
                                mNetSdk.SetupAlarmChan(mloginid1);
                                mNetSdk.SetAlarmMessageCallBack();
                                ChnInfo chnInfo = new ChnInfo();
                                chnInfo.ChannelNo = 0;
                                long playhandle = mNetSdk.onRealPlay(0, mloginid1, chnInfo);

                                mWndsHolder.vv1.onPlay();
                                mNetSdk.setDataCallback(playhandle);
                                mNetSdk.setReceiveCompleteVData(0, true);

                            }

                        }
                    }).start();
                } else if (mloginid2 == l) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                video2_bool = startPing("192.168.1.51");
                                if (video2_bool) {
                                    break;
                                }
                            }
                            if (video2_bool) {
                                Stop(mWndsHolder.vv2, mplayhandle2);
                                DevInfo devInfo = new DevInfo();
                                devInfo.Ip = "192.168.1.51";
                                devInfo.TCPPort = 34567;
                                devInfo.UserName = "admin".getBytes();
                                devInfo.PassWord = "";
                                mloginid2 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                                mplayhandle2 = new long[devInfo.ChanNum];
                                mNetSdk.SetupAlarmChan(mloginid2);
                                mNetSdk.SetAlarmMessageCallBack();
                                ChnInfo chnInfo = new ChnInfo();
                                chnInfo.ChannelNo = 0;
                                long playhandle = mNetSdk.onRealPlay(1, mloginid2, chnInfo);
                                mWndsHolder.vv2.onPlay();
                                mNetSdk.setDataCallback(playhandle);
                                mNetSdk.setReceiveCompleteVData(0, true);
                            }

                        }
                    }).start();

                } else if (mloginid3 == l) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                video3_bool = startPing("192.168.1.52");
                                if (video3_bool) {
                                    break;
                                }
                            }
                            if (video3_bool) {
                                Stop(mWndsHolder.vv3, mplayhandle3);
                                DevInfo devInfo = new DevInfo();
                                devInfo.Ip = "192.168.1.52";
                                devInfo.TCPPort = 34567;
                                devInfo.UserName = "admin".getBytes();
                                devInfo.PassWord = "";
                                mloginid3 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                                mplayhandle3 = new long[devInfo.ChanNum];
                                mNetSdk.SetupAlarmChan(mloginid3);
                                mNetSdk.SetAlarmMessageCallBack();
                                ChnInfo chnInfo = new ChnInfo();
                                chnInfo.ChannelNo = 0;
                                long playhandle = mNetSdk.onRealPlay(2, mloginid3, chnInfo);
                                mWndsHolder.vv3.onPlay();
                                mNetSdk.setDataCallback(playhandle);
                                mNetSdk.setReceiveCompleteVData(0, true);
                            }

                        }
                    }).start();

                } else if (mloginid4 == l) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                video4_bool = startPing("192.168.1.53");
                                if (video4_bool) {
                                    break;
                                }
                            }
                            if (video4_bool) {
                                Stop(mWndsHolder.vv4, mplayhandle4);
                                // TODO Auto-generated method stub
                                DevInfo devInfo = new DevInfo();
                                devInfo.Ip = "192.168.1.53";
                                devInfo.TCPPort = 34567;
                                devInfo.UserName = "admin".getBytes();
                                devInfo.PassWord = "";
                                mloginid4 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                                mplayhandle4 = new long[devInfo.ChanNum];
                                mNetSdk.SetupAlarmChan(mloginid4);
                                mNetSdk.SetAlarmMessageCallBack();
                                ChnInfo chnInfo = new ChnInfo();
                                chnInfo.ChannelNo = 0;
                                long playhandle = mNetSdk.onRealPlay(3, mloginid4, chnInfo);
                                mWndsHolder.vv4.onPlay();
                                mNetSdk.setDataCallback(playhandle);
                                mNetSdk.setReceiveCompleteVData(0, true);
                            }

                        }
                    }).start();
                }
            }
        });

        mNetSdk.setOnAlarmListener(new NetSdk.OnAlarmListener() {

            @Override
            public void onAlarm(long loginid, int ichannel, int iEvent, int iStatus,
                                int[] time) {
                // TODO Auto-generated method stub
                System.out.println("报警");
//                if (!mp.isPlaying()){
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mp.start();
//                        }
//                    }).start();
//
//                }

            }
        });
        mWndsHolder.vv1 = (MySurfaceView) this.findViewById(R.id.vv1);
        mWndsHolder.vv2 = (MySurfaceView) this.findViewById(R.id.vv2);
        mWndsHolder.vv3 = (MySurfaceView) this.findViewById(R.id.vv3);
        mWndsHolder.vv4 = (MySurfaceView) this.findViewById(R.id.vv4);

        mWndsHolder.vv1.init(this, 0);
        mWndsHolder.vv2.init(this, 1);
        mWndsHolder.vv3.init(this, 2);
        mWndsHolder.vv4.init(this, 3);

        mWndsHolder.vv1.setOnClickListener(this);
        mWndsHolder.vv2.setOnClickListener(this);
        mWndsHolder.vv3.setOnClickListener(this);
        mWndsHolder.vv4.setOnClickListener(this);


        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    video1_bool = startPing("192.168.1.50");
                    if (video1_bool) {
                        break;
                    }
                }
                if (video1_bool) {
                    Stop(mWndsHolder.vv1, mplayhandle1);
                    DevInfo devInfo = new DevInfo();
                    devInfo.Ip = "192.168.1.50";
                    devInfo.TCPPort = 34567;
                    devInfo.UserName = "admin".getBytes();
                    devInfo.PassWord = "";
                    mloginid1 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                    Log.d("", "----------------------------------111111--------------" + mloginid1);
                    mplayhandle1 = new long[devInfo.ChanNum];
                    mNetSdk.SetupAlarmChan(mloginid1);
                    mNetSdk.SetAlarmMessageCallBack();
                    ChnInfo chnInfo = new ChnInfo();
                    chnInfo.ChannelNo = 0;
                    long playhandle = mNetSdk.onRealPlay(0, mloginid1, chnInfo);

                    mWndsHolder.vv1.onPlay();
                    mNetSdk.setDataCallback(playhandle);
                    mNetSdk.setReceiveCompleteVData(0, true);

                }

            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    video2_bool = startPing("192.168.1.51");
                    if (video2_bool) {
                        break;
                    }
                }
                if (video2_bool) {
                    Stop(mWndsHolder.vv2, mplayhandle2);
                    DevInfo devInfo = new DevInfo();
                    devInfo.Ip = "192.168.1.51";
                    devInfo.TCPPort = 34567;
                    devInfo.UserName = "admin".getBytes();
                    devInfo.PassWord = "";
                    mloginid2 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                    Log.d("", "----------------------------------234--------------" + mloginid2);
                    mplayhandle2 = new long[devInfo.ChanNum];
                    mNetSdk.SetupAlarmChan(mloginid2);
                    mNetSdk.SetAlarmMessageCallBack();
                    ChnInfo chnInfo = new ChnInfo();
                    chnInfo.ChannelNo = 0;
                    long playhandle = mNetSdk.onRealPlay(1, mloginid2, chnInfo);
                    mWndsHolder.vv2.onPlay();
                    mNetSdk.setDataCallback(playhandle);
                    mNetSdk.setReceiveCompleteVData(0, true);
                }

            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    video3_bool = startPing("192.168.1.52");
                    if (video3_bool) {
                        break;
                    }
                }
                if (video3_bool) {
                    Stop(mWndsHolder.vv3, mplayhandle3);
                    DevInfo devInfo = new DevInfo();
                    devInfo.Ip = "192.168.1.52";
                    devInfo.TCPPort = 34567;
                    devInfo.UserName = "admin".getBytes();
                    devInfo.PassWord = "";
                    mloginid3 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                    mplayhandle3 = new long[devInfo.ChanNum];
                    mNetSdk.SetupAlarmChan(mloginid3);
                    mNetSdk.SetAlarmMessageCallBack();
                    ChnInfo chnInfo = new ChnInfo();
                    chnInfo.ChannelNo = 0;
                    long playhandle = mNetSdk.onRealPlay(2, mloginid3, chnInfo);
                    mWndsHolder.vv3.onPlay();
                    mNetSdk.setDataCallback(playhandle);
                    mNetSdk.setReceiveCompleteVData(0, true);
                }

            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    video4_bool = startPing("192.168.1.53");
                    if (video4_bool) {
                        break;
                    }
                }
                if (video4_bool) {
                    Stop(mWndsHolder.vv4, mplayhandle4);
                    // TODO Auto-generated method stub
                    DevInfo devInfo = new DevInfo();
                    devInfo.Ip = "192.168.1.53";
                    devInfo.TCPPort = 34567;
                    devInfo.UserName = "admin".getBytes();
                    devInfo.PassWord = "";
                    mloginid4 = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                    mplayhandle4 = new long[devInfo.ChanNum];
                    mNetSdk.SetupAlarmChan(mloginid4);
                    mNetSdk.SetAlarmMessageCallBack();
                    ChnInfo chnInfo = new ChnInfo();
                    chnInfo.ChannelNo = 0;
                    long playhandle = mNetSdk.onRealPlay(3, mloginid4, chnInfo);
                    mWndsHolder.vv4.onPlay();
                    mNetSdk.setDataCallback(playhandle);
                    mNetSdk.setReceiveCompleteVData(0, true);
                }

            }
        }).start();
    }

    private void Stop(MySurfaceView view, long[] mplayhandle) {
        mNetSdk.onStopAlarmMsg(true);
        view.onStop();
        if (mplayhandle != null) {
            if (mplayhandle.length > 0)
                mNetSdk.onStopRealPlay(mplayhandle[0]);
        }
    }

    private void uiti() {
        vv1 = (MySurfaceView) findViewById(R.id.vv1);
        vv2 = (MySurfaceView) findViewById(R.id.vv2);
        vv3 = (MySurfaceView) findViewById(R.id.vv3);
        vv4 = (MySurfaceView) findViewById(R.id.vv4);
        vv1_full = (MySurfaceView) findViewById(R.id.vv1_full);
        vv2_full = (MySurfaceView) findViewById(R.id.vv2_full);
        vv3_full = (MySurfaceView) findViewById(R.id.vv3_full);
        vv4_full = (MySurfaceView) findViewById(R.id.vv4_full);

        mWndsHolder = new WndsHolder();

        window1 = (ImageView) findViewById(R.id.window1);
        window2 = (ImageView) findViewById(R.id.window2);
        window3 = (ImageView) findViewById(R.id.window3);
        window4 = (ImageView) findViewById(R.id.window4);
        light1 = (ImageView) findViewById(R.id.light1);
        light2 = (ImageView) findViewById(R.id.light2);
        light3 = (ImageView) findViewById(R.id.light3);
        light4 = (ImageView) findViewById(R.id.light4);
        wind = (ImageView) findViewById(R.id.wind);
        door = (ImageView) findViewById(R.id.door);
        emer_img = (ImageView) findViewById(R.id.emer_img);
        center_img = (ImageView) findViewById(R.id.center_img);
        battary_on = (ImageView) findViewById(R.id.battary_on);
        video_img1 = (ImageView) findViewById(R.id.video_img1);
        video_img2 = (ImageView) findViewById(R.id.video_img2);
        video_img3 = (ImageView) findViewById(R.id.video_img3);
        video_img4 = (ImageView) findViewById(R.id.video_img4);
        battary1 = (Battary) findViewById(R.id.battary1);
        window1_open = (ImageView) findViewById(R.id.window1_open);
        window1_close = (ImageView) findViewById(R.id.window1_close);
        window2_open = (ImageView) findViewById(R.id.window2_open);
        window2_close = (ImageView) findViewById(R.id.window2_close);
        window3_open = (ImageView) findViewById(R.id.window3_open);
        window3_close = (ImageView) findViewById(R.id.window3_close);
        window4_open = (ImageView) findViewById(R.id.window4_open);
        window4_close = (ImageView) findViewById(R.id.window4_close);
        window1_invisible = (ImageView) findViewById(R.id.window1_invisible);
        window2_invisible = (ImageView) findViewById(R.id.window2_invisible);
        window3_invisible = (ImageView) findViewById(R.id.window3_invisible);
        window4_invisible = (ImageView) findViewById(R.id.window4_invisible);
        water = (ImageView) findViewById(R.id.water);
        water_invisible = (ImageView) findViewById(R.id.water_invisible);
        wind_invisible = (ImageView) findViewById(R.id.wind_invisible);
        door_invisible = (ImageView) findViewById(R.id.door_invisible);


//        window1.setOnClickListener(this);
//        window2.setOnClickListener(this);
//        window3.setOnClickListener(this);
//        window4.setOnClickListener(this);
        window1_open.setOnClickListener(this);
        window1_close.setOnClickListener(this);
        window2_open.setOnClickListener(this);
        window2_close.setOnClickListener(this);
        window3_open.setOnClickListener(this);
        window3_close.setOnClickListener(this);
        window4_open.setOnClickListener(this);
        window4_close.setOnClickListener(this);
        light1.setOnClickListener(this);
        light2.setOnClickListener(this);
        light3.setOnClickListener(this);
        light4.setOnClickListener(this);
        water.setOnClickListener(this);
        wind.setOnClickListener(this);
        door.setOnClickListener(this);
        emer_img.setOnClickListener(this);


        oxy_txt = (TextView) findViewById(R.id.oxy_txt);
        wendu_txt = (TextView) findViewById(R.id.wendu_txt);
        shidu_txt = (TextView) findViewById(R.id.shidu_txt);
        qiya_txt = (TextView) findViewById(R.id.qiya_txt);
        yangqi_day = (TextView) findViewById(R.id.yangqi_day);
        oxyvalue1 = (OxyView) findViewById(R.id.oxyvalue1);
        water_shengyu = (WaterView) findViewById(R.id.water_shengyu);

        face = Typeface.createFromAsset(this.getResources().getAssets(), "Transformers.ttf");

        oxy_txt.setTypeface(face);
        wendu_txt.setTypeface(face);
        shidu_txt.setTypeface(face);
        qiya_txt.setTypeface(face);
        yangqi_day.setTypeface(face);

    }

    //紧急模式下不可用按钮
    private void emerui(boolean bool) {
        if (bool) {
            window1_invisible.setVisibility(View.VISIBLE);
            window2_invisible.setVisibility(View.VISIBLE);
            window3_invisible.setVisibility(View.VISIBLE);
            window4_invisible.setVisibility(View.VISIBLE);
            water_invisible.setVisibility(View.VISIBLE);
            wind_invisible.setVisibility(View.VISIBLE);
            door_invisible.setVisibility(View.VISIBLE);

            window1_open.setVisibility(View.GONE);
            window2_open.setVisibility(View.GONE);
            window3_open.setVisibility(View.GONE);
            window4_open.setVisibility(View.GONE);
            window1_close.setVisibility(View.GONE);
            window2_close.setVisibility(View.GONE);
            window3_close.setVisibility(View.GONE);
            window4_close.setVisibility(View.GONE);
            water.setVisibility(View.GONE);
            wind.setVisibility(View.GONE);
            door.setVisibility(View.GONE);
        } else {
            window1_invisible.setVisibility(View.GONE);
            window2_invisible.setVisibility(View.GONE);
            window3_invisible.setVisibility(View.GONE);
            window4_invisible.setVisibility(View.GONE);
            water_invisible.setVisibility(View.GONE);
            wind_invisible.setVisibility(View.GONE);
            door_invisible.setVisibility(View.GONE);

            window1_open.setVisibility(View.VISIBLE);
            window2_open.setVisibility(View.VISIBLE);
            window3_open.setVisibility(View.VISIBLE);
            window4_open.setVisibility(View.VISIBLE);
            window1_close.setVisibility(View.VISIBLE);
            window2_close.setVisibility(View.VISIBLE);
            window3_close.setVisibility(View.VISIBLE);
            window4_close.setVisibility(View.VISIBLE);
            water.setVisibility(View.VISIBLE);
            wind.setVisibility(View.VISIBLE);
            door.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrigger() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.window1_open:
                window1_tag = 101;
                order(13, 0, 1);
                break;
            case R.id.window1_close:
                window1_tag = 100;
                order(13, 0, 0);
                break;
            case R.id.window2_open:
                window2_tag = 201;
                order(13, 1, 1);
                break;
            case R.id.window2_close:
                window2_tag = 200;
                order(13, 1, 0);
                break;
            case R.id.window3_open:
                window3_tag = 301;
                order(13, 2, 1);
                break;
            case R.id.window3_close:
                window3_tag = 300;
                order(13, 2, 0);
                break;
            case R.id.window4_open:
                window4_tag = 401;
                order(13, 3, 1);
                break;
            case R.id.window4_close:
                window4_tag = 400;
                order(13, 3, 0);
                break;

            case R.id.light1:
                if (light1_bool) {
                    order(12, 0, 0);
                    light1.setBackgroundResource(R.drawable.light1_off);
                    light1_bool = false;
                } else {
                    order(12, 0, 1);
                    light1.setBackgroundResource(R.drawable.light1_on);
                    light1_bool = true;
                }
                break;
            case R.id.light2:
                if (light2_bool) {
                    order(12, 1, 0);
                    light2.setBackgroundResource(R.drawable.light2_off);
                    light2_bool = false;
                } else {
                    order(12, 1, 1);
                    light2.setBackgroundResource(R.drawable.light2_on);
                    light2_bool = true;
                }
                break;
            case R.id.light3:
                if (light3_bool) {
                    order(16, 0, 0);
                    light3.setBackgroundResource(R.drawable.light3_off);
                    light3_bool = false;
                } else {
                    order(16, 0, 1);
                    light3.setBackgroundResource(R.drawable.light3_on);
                    light3_bool = true;
                }
                break;
            case R.id.light4:
//                if (light4_bool){
//                    order(16,0,0);
//                    light4.setBackgroundResource(R.drawable.light_off);
//                    light4_bool = false;
//                }else{
//                    order(16,0,1);
//                    light4.setBackgroundResource(R.drawable.light_on);
//                    light4_bool = true;
//                }
                break;
            case R.id.wind:
                if (wind_bool) {
                    order(15, 0, 0);
                    wind.setBackgroundResource(R.drawable.wind_off);
                    wind_bool = false;
                } else {
                    order(15, 0, 1);
                    wind.setBackgroundResource(R.drawable.wind_on);
                    wind_bool = true;
                }
                break;
            case R.id.water:
                if (water_bool) {
                    order(19, 0, 0);
                    water.setBackgroundResource(R.drawable.water_off);
                    water_bool = false;
                } else {
                    order(19, 0, 1);
                    water.setBackgroundResource(R.drawable.water_on);
                    water_bool = true;
                }
                break;
            case R.id.door:
                if (door_bool) {
                    order(14, 0, 0);
                    door.setBackgroundResource(R.drawable.door_off);
                    door_bool = false;
                } else {
                    order(14, 0, 1);
                    door.setBackgroundResource(R.drawable.door_on);
                    door_bool = true;
                }
                break;
            case R.id.emer_img:
                emerui(true);
                if (emer_img_bool) {
                    if (emer_bool) {
                        Log.d("", "----------------------------------3-----------------------");
                        dialog = dialog();
                        dialog.show();

                    } else if (emer_bool_soft) {
                        Log.d("", "----------------------------------4-----------------------");
                        order_s(18);
                        center_img.setVisibility(View.INVISIBLE);
                        center_img.clearAnimation();
                        emer_img.setBackgroundResource(R.drawable.top_nomal);
                        animation = null;

                    }

                } else {

                    Log.d("", "----------------------------------6-----------------------");
                    order_s(17);
                    center_img.setVisibility(View.VISIBLE);
                    animation = new AlphaAnimation(0.1f, 1.0f);
                    animation.setDuration(1000);
                    animation.setRepeatCount(Integer.MAX_VALUE);
                    animation.setRepeatMode(Animation.REVERSE);
                    center_img.startAnimation(animation);

                    emer_img.setBackgroundResource(R.drawable.top_emer);

                }
                break;
            case R.id.vv1:
                if (vv1_bool) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(380, 303);
                    params.setMargins(85, 210, 0, 0);
                    mWndsHolder.vv1.setLayoutParams(params);
                    vv1_bool = false;
                    hide(vv1_bool, 1);
                } else {
                    mWndsHolder.vv1.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    vv1_bool = true;
                    hide(vv1_bool, 1);
                }
                break;
            case R.id.vv2:
                if (vv2_bool) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 95);
                    params.setMargins(485, 210, 0, 0);
                    mWndsHolder.vv2.setLayoutParams(params);
                    vv2_bool = false;
                    hide(vv2_bool, 2);
                } else {
                    mWndsHolder.vv2.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    vv2_bool = true;
                    hide(vv2_bool, 2);
                }
                break;
            case R.id.vv3:
                if (vv3_bool) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 95);
                    params.setMargins(485, 315, 0, 0);
                    mWndsHolder.vv3.setLayoutParams(params);
                    vv3_bool = false;
                    hide(vv3_bool, 3);
                } else {
                    mWndsHolder.vv3.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    vv3_bool = true;
                    hide(vv3_bool, 3);
                }
                break;
            case R.id.vv4:
                if (vv4_bool) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 95);
                    params.setMargins(485, 418, 0, 0);
                    mWndsHolder.vv4.setLayoutParams(params);
                    vv4_bool = false;
                    hide(vv4_bool, 4);
                } else {
                    mWndsHolder.vv4.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    vv4_bool = true;
                    hide(vv4_bool, 4);
                }
                break;
            default:

                break;
        }
    }

    //多组控制
    public void order(int order, int position, int isopen) {
        byte[] buffer_control = new byte[14];
        buffer_control[0] = (byte) 1;
        buffer_control[1] = (byte) 0;
        buffer_control[2] = (byte) 0;
        buffer_control[3] = (byte) 0;
        buffer_control[4] = (byte) 0;
        buffer_control[5] = (byte) order;
        buffer_control[6] = (byte) 0;
        buffer_control[7] = (byte) 2;
        buffer_control[8] = (byte) 0;
        buffer_control[9] = (byte) 0;
        buffer_control[10] = (byte) 0;
        buffer_control[11] = (byte) 0;

        buffer_control[12] = (byte) position;//第七颗灯,0xff开所有灯
        buffer_control[13] = (byte) isopen;//1:开灯，0:关灯


        try {
            if (G.socket != null)
                G.socket.getOutputStream().write(buffer_control);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //单组控制
    public void order_s(int order) {
        byte[] buffer_control = new byte[12];
        buffer_control[0] = (byte) 1;
        buffer_control[1] = (byte) 0;
        buffer_control[2] = (byte) 0;
        buffer_control[3] = (byte) 0;
        buffer_control[4] = (byte) 0;
        buffer_control[5] = (byte) order;
        buffer_control[6] = (byte) 0;
        buffer_control[7] = (byte) 0;
        buffer_control[8] = (byte) 0;
        buffer_control[9] = (byte) 0;
        buffer_control[10] = (byte) 0;
        buffer_control[11] = (byte) 0;


        try {
            if (G.socket != null) {
                G.socket.getOutputStream().write(buffer_control);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int bigByteToInt(byte[] bArr, int start) {
        int m = ((bArr[start + 0] & 0xff) << 24) | ((bArr[start + 1] & 0xff) << 16) | ((bArr[start + 2] & 0xff) << 8) | (bArr[start + 3] & 0xff);
        return m;
    }

    //隐藏相框
    private void hide(boolean bool, int m) {

        if (bool) {
            video_img1.setVisibility(View.GONE);
            video_img2.setVisibility(View.GONE);
            video_img3.setVisibility(View.GONE);
            video_img4.setVisibility(View.GONE);
            if (m == 2) {
                vv1.setVisibility(View.INVISIBLE);
                vv3.setVisibility(View.INVISIBLE);
                vv4.setVisibility(View.INVISIBLE);
            } else if (m == 1) {
                vv2.setVisibility(View.INVISIBLE);
                vv3.setVisibility(View.INVISIBLE);
                vv4.setVisibility(View.INVISIBLE);
            } else if (m == 3) {
                vv2.setVisibility(View.INVISIBLE);
                vv1.setVisibility(View.INVISIBLE);
                vv4.setVisibility(View.INVISIBLE);
            } else if (m == 4) {
                vv2.setVisibility(View.INVISIBLE);
                vv1.setVisibility(View.INVISIBLE);
                vv3.setVisibility(View.INVISIBLE);
            }
        } else {
            if (m == 2) {
                vv1.setVisibility(View.VISIBLE);
                vv3.setVisibility(View.VISIBLE);
                vv4.setVisibility(View.VISIBLE);
            } else if (m == 1) {
                vv2.setVisibility(View.VISIBLE);
                vv3.setVisibility(View.VISIBLE);
                vv4.setVisibility(View.VISIBLE);
            } else if (m == 3) {
                vv2.setVisibility(View.VISIBLE);
                vv1.setVisibility(View.VISIBLE);
                vv4.setVisibility(View.VISIBLE);
            } else if (m == 4) {
                vv2.setVisibility(View.VISIBLE);
                vv3.setVisibility(View.VISIBLE);
                vv1.setVisibility(View.VISIBLE);
            }
            video_img1.setVisibility(View.VISIBLE);
            video_img2.setVisibility(View.VISIBLE);
            video_img3.setVisibility(View.VISIBLE);
            video_img4.setVisibility(View.VISIBLE);
        }

    }

    class WndsHolder {
        MySurfaceView vv1;
        MySurfaceView vv2;
        MySurfaceView vv3;
        MySurfaceView vv4;
        MySurfaceView vv1_full;
        MySurfaceView vv2_full;
        MySurfaceView vv3_full;
        MySurfaceView vv4_full;
    }

    private Dialog dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请手动退出紧急模式")
                .setCancelable(false);
        AlertDialog alert = builder.create();

        return alert;
    }
}
