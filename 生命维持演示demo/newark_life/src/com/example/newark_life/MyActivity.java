package com.example.newark_life;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Handler;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private TextView battay_text, oxy_text, co2_text, wendu_text, shidu_text;
    private Typeface face;
    private ImageView tongfeng_img, deng_img, chuang_img, meng_img;
    private boolean tongfeng_bool = false;
    private boolean deng_bool = false;
    private boolean chuang_bool = false;
    private boolean men_bool = false;
    private PopupWindow pop_chuang, pop_deng;
    private ImageView vedio1_img, vedio2_img, vedio3_img, vedio4_img;
    private ImageView deng1, deng2, deng3, deng4, deng5, deng6;
    private ImageView chuang1, chuang2, chuang3, chuang4, chuang5, chuang6, chuang7, chuang8;
    private ImageView vedio1_fill, vedio2_fill, vedio3_fill, vedio4_fill;
    private boolean deng1_bool = false;
    private boolean deng2_bool = false;
    private boolean deng3_bool = false;
    private boolean deng4_bool = false;
    private boolean deng5_bool = false;
    private boolean deng6_bool = false;
    private boolean chuang1_bool = false;
    private boolean chuang2_bool = false;
    private boolean chuang3_bool = false;
    private boolean chuang4_bool = false;
    private boolean chuang5_bool = false;
    private boolean chuang6_bool = false;
    private boolean chuang7_bool = false;
    private boolean chuang8_bool = false;
    private boolean vedio1_bool = false;
    private boolean vedio2_bool = false;
    private boolean vedio3_bool = false;
    private boolean vedio4_bool = false;
    private Socket socket2 = null;//视频socket
    private Socket socket3 = null;//数据socket
    private int tag;
    public android.os.Handler handler;
    public android.os.Handler handler1;
    private int video_tag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        uiti();
        pop_chuang = uitipop_chuang(R.layout.chuang_pop, pop_chuang, 400, 600);
        pop_deng = uitipop_deng(R.layout.deng_pop, pop_deng, 400, 520);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket2 = new Socket("192.168.1.202", 8888);//视频
                    socket3 = new Socket("192.168.1.202", 9999);//数据
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        capture("", "");
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data();
                    }
                }).start();
            }
        }).start();

        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (video_tag == 0) {
                    if (msg.what == 0) {
                        vedio2_img.setImageBitmap((Bitmap) msg.obj);
                        vedio2_fill.setImageBitmap((Bitmap) msg.obj);
                        vedio1_fill.setImageBitmap((Bitmap) msg.obj);
//                        vedio1_img.setImageBitmap((Bitmap)msg.obj);
//
                    } else if (msg.what == 1) {
                        vedio1_img.setImageBitmap((Bitmap) msg.obj);
                        vedio1_fill.setImageBitmap((Bitmap) msg.obj);
//                        vedio2_img.setImageBitmap((Bitmap)msg.obj);
//                        vedio2_fill.setImageBitmap((Bitmap)msg.obj);
                    } else if (msg.what == 2) {
                        vedio2_img.setImageBitmap((Bitmap) msg.obj);
                        vedio2_fill.setImageBitmap((Bitmap) msg.obj);
//                        vedio3_img.setImageBitmap((Bitmap)msg.obj);
//                        vedio3_fill.setImageBitmap((Bitmap)msg.obj);
                    } else if (msg.what == 3) {
                        vedio2_img.setImageBitmap((Bitmap) msg.obj);
                        vedio2_fill.setImageBitmap((Bitmap) msg.obj);
//                        vedio4_img.setImageBitmap((Bitmap)msg.obj);
//                        vedio4_fill.setImageBitmap((Bitmap)msg.obj);
                    }
                }

            }
        };

        handler1 = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 16) {
                    oxy_text.setText((String) msg.obj);
                } else if (msg.what == 17) {
                    Double s = Double.valueOf(msg.obj.toString()) / 10;
                    if (s > 2) {
                        co2_text.setText(2.00 + "");
                    } else {
                        if (s.toString().length() >= 4) {
                            co2_text.setText(s.toString().substring(0, 4));
                        } else if (s.toString().length() == 3) {
                            co2_text.setText(s.toString() + "0");
                        } else {
                            co2_text.setText(s.toString());
                        }

                    }

                } else if (msg.what == 18) {
                    wendu_text.setText((String) msg.obj);
                } else if (msg.what == 19) {
                    shidu_text.setText((String) msg.obj);
                }
            }
        };

    }

    private void uiti() {
        face = Typeface.createFromAsset(this.getResources().getAssets(), "Transformers.ttf");
        battay_text = (TextView) findViewById(R.id.battay_text);
        oxy_text = (TextView) findViewById(R.id.oxy_text);
        co2_text = (TextView) findViewById(R.id.co2_text);
        wendu_text = (TextView) findViewById(R.id.wendu_text);
        shidu_text = (TextView) findViewById(R.id.shidu_text);
        tongfeng_img = (ImageView) findViewById(R.id.tongfeng_img);
        deng_img = (ImageView) findViewById(R.id.deng_img);
        chuang_img = (ImageView) findViewById(R.id.chuang_img);
        meng_img = (ImageView) findViewById(R.id.men_img);
        vedio1_img = (ImageView) findViewById(R.id.vedio1_img);
        vedio2_img = (ImageView) findViewById(R.id.vedio2_img);
        vedio3_img = (ImageView) findViewById(R.id.vedio3_img);
        vedio4_img = (ImageView) findViewById(R.id.vedio4_img);

        vedio1_fill = (ImageView) findViewById(R.id.vedio1_fill);
        vedio2_fill = (ImageView) findViewById(R.id.vedio2_fill);
        vedio3_fill = (ImageView) findViewById(R.id.vedio3_fill);
        vedio4_fill = (ImageView) findViewById(R.id.vedio4_fill);


        vedio1_img.setOnClickListener(this);
        vedio2_img.setOnClickListener(this);
        vedio3_img.setOnClickListener(this);
        vedio4_img.setOnClickListener(this);

        vedio1_fill.setOnClickListener(this);
        vedio2_fill.setOnClickListener(this);
        vedio3_fill.setOnClickListener(this);
        vedio4_fill.setOnClickListener(this);

        tongfeng_img.setOnClickListener(this);
        deng_img.setOnClickListener(this);
        chuang_img.setOnClickListener(this);
        meng_img.setOnClickListener(this);

        battay_text.setTypeface(face);
        oxy_text.setTypeface(face);
        co2_text.setTypeface(face);
        wendu_text.setTypeface(face);
        shidu_text.setTypeface(face);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tongfeng_img:
                if (!tongfeng_bool) {
                    tongfeng_img.setBackgroundResource(R.drawable.tongfeng_on);
                    tongfeng_bool = true;
                } else {
                    tongfeng_img.setBackgroundResource(R.drawable.tongfeng_off);
                    tongfeng_bool = false;
                }
                break;
            case R.id.deng_img:
                if (pop_chuang.isShowing()) {
                    pop_chuang.dismiss();
                    chuang_img.setBackgroundResource(R.drawable.chuang_off);
                    chuang_bool = false;
                }
                if (!deng_bool) {
                    deng_img.setBackgroundResource(R.drawable.deng_on);
                    deng_bool = true;
                    pop_deng.showAtLocation(view, Gravity.BOTTOM, 430, 180);
                } else {
                    deng_img.setBackgroundResource(R.drawable.deng_off);
                    deng_bool = false;
                    pop_deng.dismiss();
                }
                break;
            case R.id.chuang_img:
                if (pop_deng.isShowing()) {
                    pop_deng.dismiss();
                    deng_img.setBackgroundResource(R.drawable.deng_off);
                    deng_bool = false;
                }
                if (!chuang_bool) {
                    chuang_img.setBackgroundResource(R.drawable.chuang_on);
                    chuang_bool = true;
                    pop_chuang.showAtLocation(view, Gravity.BOTTOM, 430, 180);
                } else {
                    chuang_img.setBackgroundResource(R.drawable.chuang_off);
                    chuang_bool = false;
                    pop_chuang.dismiss();
                }
                break;
            case R.id.men_img:
                if (!men_bool) {
                    meng_img.setBackgroundResource(R.drawable.meng_on);
                    men_bool = true;
                } else {
                    meng_img.setBackgroundResource(R.drawable.meng_off);
                    men_bool = false;
                }
                break;
            //视频放大缩小
            case R.id.vedio1_img:
                if (!vedio1_bool) {
                    vedio1_fill.setVisibility(View.VISIBLE);
                    vedio1_bool = true;
                }
                break;
            case R.id.vedio1_fill:
                if (vedio1_bool) {
                    vedio1_fill.setVisibility(View.GONE);
                    vedio1_bool = false;
                }
                break;
            case R.id.vedio2_img:
                if (!vedio2_bool) {
                    vedio2_fill.setVisibility(View.VISIBLE);
                    vedio2_bool = true;
                }
                break;
            case R.id.vedio2_fill:
                if (vedio2_bool) {
                    vedio2_fill.setVisibility(View.GONE);
                    vedio2_bool = false;
                }
                break;
            case R.id.vedio3_img:
                if (!vedio3_bool) {
                    vedio3_fill.setVisibility(View.VISIBLE);
                    vedio3_bool = true;
                }
                break;
            case R.id.vedio3_fill:
                if (vedio3_bool) {
                    vedio3_fill.setVisibility(View.GONE);
                    vedio3_bool = false;
                }
                break;
            case R.id.vedio4_img:
                if (!vedio4_bool) {
                    vedio4_fill.setVisibility(View.VISIBLE);
                    vedio4_bool = true;
                }
                break;
            case R.id.vedio4_fill:
                if (vedio4_bool) {
                    vedio4_fill.setVisibility(View.GONE);
                    vedio4_bool = false;
                }
                break;

            //灯泡控制
            case R.id.deng1:
                if (!deng1_bool) {
                    deng1.setBackgroundResource(R.drawable.deng_pop_on);
                    deng1_bool = true;
                } else {
                    deng1.setBackgroundResource(R.drawable.deng_pop_off);
                    deng1_bool = false;
                }
                break;
            case R.id.deng2:
                if (!deng2_bool) {
                    deng2.setBackgroundResource(R.drawable.deng_pop_on);
                    deng2_bool = true;
                } else {
                    deng2.setBackgroundResource(R.drawable.deng_pop_off);
                    deng2_bool = false;
                }
                break;
            case R.id.deng3:
                if (!deng3_bool) {
                    deng3.setBackgroundResource(R.drawable.deng_pop_on);
                    deng3_bool = true;
                } else {
                    deng3.setBackgroundResource(R.drawable.deng_pop_off);
                    deng3_bool = false;
                }
                break;
            case R.id.deng4:
                if (!deng4_bool) {
                    deng4.setBackgroundResource(R.drawable.deng_pop_on);
                    deng4_bool = true;
                } else {
                    deng4.setBackgroundResource(R.drawable.deng_pop_off);
                    deng4_bool = false;
                }
                break;
            case R.id.deng5:
                if (!deng5_bool) {
                    deng5.setBackgroundResource(R.drawable.deng_pop_on);
                    deng5_bool = true;
                } else {
                    deng5.setBackgroundResource(R.drawable.deng_pop_off);
                    deng5_bool = false;
                }
                break;
            case R.id.deng6:
                if (!deng6_bool) {
                    deng6.setBackgroundResource(R.drawable.deng_pop_on);
                    deng6_bool = true;
                } else {
                    deng6.setBackgroundResource(R.drawable.deng_pop_off);
                    deng6_bool = false;
                }
                break;
            //窗控制
            case R.id.chuang1:
                if (!chuang1_bool) {
                    chuang1.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang1_bool = true;
                } else {
                    chuang1.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang1_bool = false;
                }
                break;
            case R.id.chuang2:
                if (!chuang2_bool) {
                    chuang2.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang2_bool = true;
                } else {
                    chuang2.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang2_bool = false;
                }
                break;
            case R.id.chuang3:
                if (!chuang3_bool) {
                    chuang3.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang3_bool = true;
                } else {
                    chuang3.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang3_bool = false;
                }
                break;
            case R.id.chuang4:
                if (!chuang4_bool) {
                    chuang4.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang4_bool = true;
                } else {
                    chuang4.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang4_bool = false;
                }
                break;
            case R.id.chuang5:
                if (!chuang5_bool) {
                    chuang5.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang5_bool = true;
                } else {
                    chuang5.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang5_bool = false;
                }
                break;
            case R.id.chuang6:
                if (!chuang6_bool) {
                    chuang6.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang6_bool = true;
                } else {
                    chuang6.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang6_bool = false;
                }
                break;
            case R.id.chuang7:
                if (!chuang7_bool) {
                    chuang7.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang7_bool = true;
                } else {
                    chuang7.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang7_bool = false;
                }
                break;
            case R.id.chuang8:
                if (!chuang8_bool) {
                    chuang8.setBackgroundResource(R.drawable.chuang_pop_on);
                    chuang8_bool = true;
                } else {
                    chuang8.setBackgroundResource(R.drawable.chuang_pop_off);
                    chuang8_bool = false;
                }
                break;
        }
    }

    //窗弹出框
    private PopupWindow uitipop_chuang(int layout, PopupWindow pop, int width, int height) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(layout, null);
        pop = new PopupWindow(view1, width, height, false);
        chuang1 = (ImageView) view1.findViewById(R.id.chuang1);
        chuang2 = (ImageView) view1.findViewById(R.id.chuang2);
        chuang3 = (ImageView) view1.findViewById(R.id.chuang3);
        chuang4 = (ImageView) view1.findViewById(R.id.chuang4);
        chuang5 = (ImageView) view1.findViewById(R.id.chuang5);
        chuang6 = (ImageView) view1.findViewById(R.id.chuang6);
        chuang7 = (ImageView) view1.findViewById(R.id.chuang7);
        chuang8 = (ImageView) view1.findViewById(R.id.chuang8);

        chuang1.setOnClickListener(this);
        chuang2.setOnClickListener(this);
        chuang3.setOnClickListener(this);
        chuang4.setOnClickListener(this);
        chuang5.setOnClickListener(this);
        chuang6.setOnClickListener(this);
        chuang7.setOnClickListener(this);
        chuang8.setOnClickListener(this);
//        pop.setFocusable(true);
//        pop.setOutsideTouchable(true);
//        pop.setBackgroundDrawable(new BitmapDrawable());
        return pop;
    }

    //灯弹出框
    private PopupWindow uitipop_deng(int layout, PopupWindow pop, int width, int height) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(layout, null);
        pop = new PopupWindow(view1, width, height, false);
        deng1 = (ImageView) view1.findViewById(R.id.deng1);
        deng2 = (ImageView) view1.findViewById(R.id.deng2);
        deng3 = (ImageView) view1.findViewById(R.id.deng3);
        deng4 = (ImageView) view1.findViewById(R.id.deng4);
        deng5 = (ImageView) view1.findViewById(R.id.deng5);
        deng6 = (ImageView) view1.findViewById(R.id.deng6);
        deng1.setOnClickListener(this);
        deng2.setOnClickListener(this);
        deng3.setOnClickListener(this);
        deng4.setOnClickListener(this);
        deng5.setOnClickListener(this);
        deng6.setOnClickListener(this);
//        pop.setFocusable(true);
//        pop.setOutsideTouchable(true);
//        pop.setBackgroundDrawable(new BitmapDrawable());
        return pop;
    }

    public void data() {
        try {
            Log.d("", "----------9999999999-------------");
            byte[] bufferdata = new byte[1024];
            InputStream basedata = socket3.getInputStream();

            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                int readleng = basedata.read(bufferdata);
                Log.d("", "--------------d---------------" + bufferdata[0] + "\n" + bufferdata[1] + "\n" + bufferdata[2] +
                        "\n" + bufferdata[3] + "\n" + bufferdata[4] + "\n" + bufferdata[5] + "\n" + bufferdata[6] +
                        "\n" + bufferdata[7] + "\n" + bufferdata[8] + "\n" + bufferdata[9] + "\n" + bufferdata[10] +
                        "\n" + bufferdata[11]);
                Log.d("", "---------2----------------" + getTemp(bufferdata[10], bufferdata[11]));
                if (bufferdata[9] == 16) {//氧气
                    Message msg = new Message();
                    msg.what = bufferdata[9];
                    msg.obj = getTemp(bufferdata[10], bufferdata[11]);
                    handler1.sendMessage(msg);
                } else if (bufferdata[9] == 17) {//二氧化碳
                    Message msg = new Message();
                    msg.what = bufferdata[9];
                    msg.obj = getTemp(bufferdata[10], bufferdata[11]);
                    handler1.sendMessage(msg);
                } else if (bufferdata[9] == 18) {//温度
                    Message msg = new Message();
                    msg.what = bufferdata[9];
                    msg.obj = getTemp(bufferdata[10], bufferdata[11]);
                    handler1.sendMessage(msg);
                } else if (bufferdata[9] == 19) {//湿度
                    Message msg = new Message();
                    msg.what = bufferdata[9];
                    msg.obj = getTemp(bufferdata[10], bufferdata[11]);
                    handler1.sendMessage(msg);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ;

    public boolean capture(String host, String port) {

        try {
//			socket = new Socket(host, Integer.valueOf(port));

            //obtain the bitmap
            byte[] buffer_control = new byte[18];
            buffer_control[0] = (byte) 0xeb;
            buffer_control[1] = (byte) 0x90;
            buffer_control[2] = (byte) 0x12;
            buffer_control[3] = (byte) 0x34;
            buffer_control[4] = (byte) 0x01;
            buffer_control[5] = (byte) 0x00;
            buffer_control[6] = (byte) 0x00;
            buffer_control[7] = (byte) 0x00;
            buffer_control[8] = (byte) 0x04;
            buffer_control[9] = (byte) 0x80;


            socket3.getOutputStream().write(buffer_control);

            InputStream in = socket2.getInputStream();
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                int count = 400000;//1450054;//31000;
                int m = 0;
                int a = 0;
                int b = 0;
                int c = 0;
                int d = 0;

                byte[] buffer = new byte[count];
                byte[] buffer1 = new byte[1024];
                byte[] buffer2 = new byte[2048];
                int index = 0;
                int readlen = in.read(buffer1);
                int i1;
                for (i1 = 0; i1 < buffer1.length - 3; i1++) {
                    if (buffer1[i1] == -21 && buffer1[i1 + 1] == -112 && buffer1[i1 + 2] == 18 && buffer1[i1 + 3] == 52) {
                        m = i1;
                        break;
                    }
                }
                if (i1 == buffer1.length - 3) {
                    continue;
                }
                System.arraycopy(buffer1, m + 4, buffer2, 0, readlen - m - 4);
                int readlen1 = in.read(buffer1);
                System.arraycopy(buffer1, 0, buffer2, readlen - m - 4, readlen1);
                if (buffer2[0] >= 0) {
                    a = buffer2[0];
                } else {
                    a = buffer2[0] + 256;
                }
                if (buffer2[1] >= 0) {
                    b = buffer2[1];
                } else {
                    b = buffer2[1] + 256;
                }
                if (buffer2[2] >= 0) {
                    c = buffer2[2];
                } else {
                    c = buffer2[2] + 256;
                }
                if (buffer2[3] >= 0) {
                    d = buffer2[3];
                } else {
                    d = buffer2[3];
                }
                tag = d;
//                Log.d("", "-------------------66666666666666-----------------------" + d);
                int bytelength = a + 256 * b + 256 * 256 * c;
//                Log.d("", "-------------m--------======"+m+"\n"+bytelength);
//		        int begin = 8;
                System.arraycopy(buffer2, 4, buffer, 0, readlen + readlen1 - m - 8);
                index += readlen + readlen1 - m - 8;

                int nowlenght = bytelength - readlen - readlen1 + m + 8;

                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    readlen = in.read(buffer1);
                    nowlenght = nowlenght - readlen;
                    if (nowlenght >= 0) {
                        System.arraycopy(buffer1, 0, buffer, index, readlen);
                        index += readlen;
                    } else {
                        System.arraycopy(buffer1, 0, buffer, index, readlen);
                        break;
                    }
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, count);
//				int a1 = bitmap.getByteCount();
//				Log.d("", count+"-------------t-----------------"+a1);

                Message msg = new Message();
                msg.what = tag;
                msg.obj = bitmap;
                handler.sendMessage(msg);
//                handler1.sendMessage(msg);
            }
        } catch (RuntimeException e) {
            Log.i("", "Failed to obtain image over network", e);
            return false;
        } catch (IOException e) {
            Log.i("", "Failed to obtain image over network", e);
            return false;
        } finally {
//            try {
//                socket2.close();
//            } catch (IOException e) {
//				/* ignore */
//            }
        }
        return true;
    }

    private static int vtolh(byte[] bArr) {
        int n = 0;
        for (int i = 0; i < bArr.length && i < 4; i++) {
            int left = i * 8;
            n += (bArr[i] << left);
        }
        return n;
    }

    private static String getTemp(byte heightByte, byte lowerByte) {
        float realTemp;
        short temp;

        temp = (short) (((heightByte & 0xff) << 8) | (lowerByte & 0xff));
        realTemp = (float) (temp * 0.1);

        return String.valueOf(realTemp);
    }

}
