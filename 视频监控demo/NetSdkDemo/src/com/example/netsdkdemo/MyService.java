package com.example.netsdkdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.FindInfo;
import com.xm.MyConfig.AudioState;
import com.xm.MyConfig.DevStatus;
import com.xm.MyConfig.FileType;
import com.xm.MyConfig.SocketStyle;
import com.xm.video.MySurfaceView;
import com.xm.video.MyVideoData;
import com.xm.NetSdk;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class MyService extends Service {
    private NetSdk mNetSdk;
    private MyVideoData mVideoData;

    public MyService() {
        mNetSdk = NetSdk.getInstance();
        DevInfo devInfo = new DevInfo();
        devInfo.Ip = "10.10.34.70";
        devInfo.TCPPort = 34567;
        try {
            devInfo.UserName = "admin".getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        devInfo.PassWord = "";
        long mloginid = mNetSdk.onLoginDev(0, devInfo, null, SocketStyle.TCPSOCKET);
        //¼��
//    	if(mloginid > 0) {
//    		FindInfo findInfo = new FindInfo();
//			findInfo.szFileName = "55555.h264";
//			findInfo.nChannelN0 = 0;
//			findInfo.nFileType = FileType.SDK_RECORD_ALL;
//			findInfo.startTime.year = 2014;
//			findInfo.startTime.month = 1;
//			findInfo.startTime.day = 21;
//			findInfo.startTime.hour = 2;
//			findInfo.startTime.minute = 0;
//			findInfo.startTime.second = 0;
//			findInfo.endTime.year = 2014;
//			findInfo.endTime.month = 1;
//			findInfo.endTime.day = 21;
//			findInfo.endTime.hour = 2;
//			findInfo.endTime.minute = 10;
//			findInfo.endTime.second = 0;
//			File file = new File(Environment.getExternalStorageDirectory() + "/");
//			if(file.exists())
//				System.out.println("�����ɹ�");
//			long mlDownLoadHandle = mNetSdk.GetFileByTime(0,mloginid,findInfo, file.getAbsolutePath(),1);
//			if(mlDownLoadHandle > 0) {
//				System.out.println("��������");
//			}else 
//				System.out.println("����ʧ��");
//			}
        //��ͼ
        mVideoData = new MyVideoData(MyService.this, 0);
        if (mloginid > 0) {
            /** ͨ����Ϣ
             * 	public int devStatus = DevStatus.ChnNotOpen;//��δ��ͨ��
             public long Playhandle = 0;//���ž��
             public int ChannelNo = 0;//ͨ����
             public int DeviceNo;//�豸��
             public String ChnName;//ͨ����
             public String Ip;//ip��ַ
             public int WndNo;//��Ӧ�Ĵ��ں�
             public int nStream = 1;	//0��ʾ��������Ϊ1��ʾ������
             public int nMode = 0;		//0��TCP��ʽ,1��UDP��ʽ,2���ಥ��ʽ,3 - RTP��ʽ��4-����Ƶ�ֿ�(TCP)
             public int nComType = 0;	//ֻ����ϱ���ͨ����Ч, ��ϱ���ͨ����ƴͼģʽ
             */
            ChnInfo chnInfo = new ChnInfo();
            chnInfo.ChannelNo = 0;//��һͨ��
            /**
             * id ����ID
             * loginid ��½ID
             * lpChnInfo ͨ����Ϣ
             */
            long playhandle = mNetSdk.onRealPlay(0, mloginid, chnInfo);
            if (playhandle > 0) {
                mNetSdk.setDataCallback(playhandle);
                mVideoData.initData();//��ʼ�����
                mHandler.sendEmptyMessageDelayed(0, 5000);
            }
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = mVideoData.getFrameBitmap();
                    File file = null;
                    file = new File(Environment.getExternalStorageDirectory() + "/");
                    File[] files = file.listFiles();
                    if (files != null) {
                        int item = files.length + 1;
                        File imagefile = new File(file, "capture" + item + ".jpg");
                        try {
                            String ret;
                            imagefile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(imagefile);
                            bitmap.compress(CompressFormat.JPEG, 50, fos);
                            fos.flush();
                            fos.close();

                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
