package com.ark.newark_phone;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.MyConfig;
import com.xm.NetSdk;
import com.xm.video.MySurfaceView;

/**
 * Created by a1314 on 15/1/11.
 */
public class SecondPageFragment extends Fragment {

    private View v = null;
    private long mloginid;
    private long[] mplayhandle;
    private NetSdk mNetSdk = null;
    private WndsHolder mWndsHolder;
    private int mSocketStyle = MyConfig.SocketStyle.TCPSOCKET;
    private int mWndSelected = 0;
    private MediaPlayer mp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.thirdpagefragment, container,
                false);
//        init();
        return v;
    }

    /*
     * 初始化
     */
    private void init() {
        mNetSdk = NetSdk.getInstance();
        mp = MediaPlayer.create(SecondPageFragment.this.getActivity(), R.raw.jingbao);
        /**
         * 报警回调
         */
        mNetSdk.setOnAlarmListener(new NetSdk.OnAlarmListener() {

            @Override
            public void onAlarm(long loginid, int ichannel, int iEvent, int iStatus,
                                int[] time) {
                // TODO Auto-generated method stub
                System.out.println("报警");
                if (!mp.isPlaying()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mp.start();
                        }
                    }).start();

                }

            }
        });
        mWndsHolder = new WndsHolder();
        mWndsHolder.vv1 = (MySurfaceView) v.findViewById(R.id.vv1);
        mWndsHolder.vv1.init(this.getActivity(), 0);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                DevInfo devInfo = new DevInfo();
                devInfo.Ip = "192.168.1.50";
                devInfo.TCPPort = 34567;
                devInfo.UserName = "admin".getBytes();
                devInfo.PassWord = "";
                mloginid = mNetSdk.onLoginDev(mWndSelected, devInfo, null, mSocketStyle);
                mplayhandle = new long[devInfo.ChanNum];
                mNetSdk.SetupAlarmChan(mloginid);
                mNetSdk.SetAlarmMessageCallBack();
                ChnInfo chnInfo = new ChnInfo();
                chnInfo.ChannelNo = 0;
                long playhandle = mNetSdk.onRealPlay(0, mloginid, chnInfo);
                mWndsHolder.vv1.onPlay();
                mNetSdk.setDataCallback(playhandle);
                mNetSdk.setReceiveCompleteVData(0, true);
            }
        }).start();
    }

    class WndsHolder {
        MySurfaceView vv1;
    }
}
