package com.ark.newark_phone;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by a1314 on 15/1/11.
 */
public class FirstPageFragment extends Fragment {

    private View v = null;
    private TextView battay_text, oxy_text, co2_text, wendu_text, shidu_text, qiya_text;
    private Typeface face;
    private Socket datasocket = null;
    private android.os.Handler datahandler;
    private OxyView oxy01, oxy02, oxy03;
    private Battary battary1;
    private WaterView waterview;
    private Handler sokecthandler;
    private byte[] bufferdata;
    private byte[] bufferheart;
    private int readleng;
    private boolean isconnect;
    private long timecha;
    private long lasttime = 0;
    private boolean isconnectnow = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.firstpagefragment, container,
                false);
        uiti();

//        datahandler = new android.os.Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                updateui(msg.getData().getByteArray("databuffer"));
//
//            }
//        };
//
//        if (datasocket == null){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        datasocket = new Socket("192.168.1.134", 7777);//数据
//                        order_query();
//                        for (int i=0;i<Integer.MAX_VALUE;i++){
//                            answer();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }else {
//            order_query();
//        }

        datahandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what != 0) {
                    if (G.isnull == 1) {
                        updateui(msg.getData().getByteArray("databuffer"));
                    }
                }
            }

        };

        sokecthandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isconnect = startPing("192.168.1.134");
                if (isconnect) {
//                    new Thread(runnable).start();
                }else {
                    Toast.makeText(FirstPageFragment.this.getActivity(),"network disconnect",Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 2){
                    Toast.makeText(FirstPageFragment.this.getActivity(),"network disconnect",Toast.LENGTH_SHORT).show();
                }

            }
        };

        if (datasocket == null) {
            new Thread(runnable).start();
        } else {
            order_query();
        }


        return v;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            {
                {
//                    {
//                        try {
////                            Log.d("","-----------------1111----------");
//                            datasocket = null;
//
//                            try {
//                                Thread.sleep(3000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            datasocket = new Socket("192.168.1.134", 7777);//数据
//
//                            order_query();
//                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
//                                isconnect = startPing("192.168.1.134");
//                                if (datasocket != null) {
//                                    if (isconnect) {
//                                        answer();
//                                    }
//
//                                }
//
//                            }
//                        } catch (IOException e) {
//                            sokecthandler.sendEmptyMessage(0);
//                            e.printStackTrace();
//                        }
//                    }
                    newconnect();
                }
            }
        }
    };

    private void newconnect(){

        try {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            datasocket = null;
            datasocket = new Socket("192.168.1.134", 7777);//数据
//                                login(datasocket);
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                isconnect = startPing("192.168.1.134");
                if (datasocket != null) {
                    if (isconnect) {
                        if (lasttime != 0){
                            timecha = System.currentTimeMillis() - lasttime;
                            Log.d("","----------------------timecha--------------"+timecha);
                            if (isconnectnow){
                                if (timecha < 3000){
                                    isconnectnow = false;
                                }
                            }else {
                                if (timecha > 3000){
                                    isconnectnow = true;
                                    datasocket = null;
                                    Message msg = new Message();
                                    msg.what = 2;
                                    sokecthandler.sendMessage(msg);
                                    newconnect();
                                }else {
                                    isconnectnow = false;
                                }

                            }

                        }
                        answer();

                    }

                }
            }

        } catch (IOException e) {
            isconnectnow = true;
            datasocket = null;
            Message msg = new Message();
            msg.what = 2;
            sokecthandler.sendMessage(msg);
            newconnect();
            e.printStackTrace();
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


    @Override
    public void onStop() {
        super.onStop();
    }

    private void uiti() {
        face = Typeface.createFromAsset(this.getResources().getAssets(), "Transformers.ttf");
        oxy_text = (TextView) v.findViewById(R.id.oxy_text);
        co2_text = (TextView) v.findViewById(R.id.co2_text);
        wendu_text = (TextView) v.findViewById(R.id.wendu_text);
        shidu_text = (TextView) v.findViewById(R.id.shidu_text);
        qiya_text = (TextView) v.findViewById(R.id.qiya_text);
        oxy01 = (OxyView) v.findViewById(R.id.oxy01);
        oxy02 = (OxyView) v.findViewById(R.id.oxy02);
        oxy03 = (OxyView) v.findViewById(R.id.oxy03);
        oxy01.setOxyValue(Double.valueOf(100));
        oxy02.setOxyValue(Double.valueOf(100));
        oxy03.setOxyValue(Double.valueOf(40));
        battary1 = (Battary) v.findViewById(R.id.battary);
        waterview = (WaterView) v.findViewById(R.id.waterview);

        oxy_text.setTypeface(face);
        co2_text.setTypeface(face);
        wendu_text.setTypeface(face);
        shidu_text.setTypeface(face);
        qiya_text.setTypeface(face);

    }

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
        bufferdata = new byte[1024];
        bufferheart = new byte[12];
        try {


            InputStream basedata = datasocket.getInputStream();

            readleng = basedata.read(bufferdata);

            if (readleng == -1) {
                Message msg = new Message();
                msg.what = 0;
                sokecthandler.sendMessage(msg);
                G.isnull = 0;
            } else {
                if (readleng < 13 && readleng != -1) {
                    System.arraycopy(bufferdata, 0, bufferheart, 0, readleng);
                    if (bufferheart[5] == 1) {
                        order_heart(bufferheart);
                        lasttime =  System.currentTimeMillis();
                    }
                    G.isnull = 0;
                } else {
//                    for (int i=0;i<readleng;i++){
//                        Log.d("","----------------------8---------------------"+bufferdata[8]);
//                    }
                    G.buffer = null;
                    G.buffer = bufferdata;
                    G.socket = null;
                    G.socket = datasocket;
                    G.isnull = 1;
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
        String co2 = buffer[14] + "." + buffer[15];
        String wendu = buffer[16] + "." + buffer[17];
        String shidu = buffer[18] + "." + buffer[19];
        int qiya = bigByteToShort(buffer, 24);
        String dianci = "" + buffer[21];
        String buff23 = buffer[23] + "";
        String oxyshengyu = buffer[44] + "." + buffer[45];
        if (buff23.length() > 1) {
            buff23 = buff23.substring(0, 1);
        }
        String shuiwei = buffer[22] + "." + buff23;
        if (oxy.length() >= 4) {
            oxy_text.setText(oxy.substring(0, 4) + "%");
        }
        if (co2.length() >= 4 && buffer[14] != -1) {
            co2_text.setText(co2.substring(0, 4) + "%");
        }
        if (wendu.length() >= 4 && buffer[16] != -1) {
            wendu_text.setText(wendu.substring(0, 4));
        }
        if (shidu.length() >= 4 && buffer[18] != -1) {
            shidu_text.setText(shidu.substring(0, 4));
        }
        if (qiya != 0) {
            qiya_text.setText(qiya + "");
        }
        waterview.setwaterValue(Double.valueOf(shuiwei));
        if (Integer.valueOf(dianci) != 0) {
            battary1.setbattaryValue(Integer.valueOf(dianci));
        }
        oxy03.setOxyValue(Double.valueOf(oxyshengyu));

    }

    private static int bigByteToInt(byte[] bArr, int start) {
        int m = ((bArr[start + 0] & 0xff) << 24) | ((bArr[start + 1] & 0xff) << 16) | ((bArr[start + 2] & 0xff) << 8) | (bArr[start + 3] & 0xff);
        return m;
    }

    private static short bigByteToShort(byte[] bArr, int start) {
        short m = (short) (((bArr[start + 0] & 0xff) << 8) | (bArr[start + 1] & 0xff));
        return m;
    }
}
