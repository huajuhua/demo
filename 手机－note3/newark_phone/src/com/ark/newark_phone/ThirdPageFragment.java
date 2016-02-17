package com.ark.newark_phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.MyConfig;
import com.xm.NetSdk;
import com.xm.video.MySurfaceView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by a1314 on 15/1/11.
 */
public class ThirdPageFragment extends Fragment implements View.OnClickListener {

    private View v = null;
    private ImageView second_light1, second_light2, second_light3, second_door, second_door_invisible, second_window1,
            second_window2, second_window1_s, second_window3, second_window4, second_wind,
            second_wind_invisible,second_escape,second_chuanglian;
    private boolean second_light1_bool = false, second_light2_bool = false, second_light3_bool = false,
            second_door_bool = false, second_window1_bool = false, second_window2_bool = false,
            second_window3_bool = false, second_window4_bool = false;
    private int second_window1_int = 0;
    private int second_window2_int = 1;
    private int second_window3_int = 2;
    private int second_window4_int = 3;
    private int wind_int = 4;
    private Animation hyperspaceJumpAnimation;
    private Animation hyperspaceJumpAnimation1;
    private Animation hyperspaceJumpAnimation2;
    private Animation hyperspaceJumpAnimation3;
    private Animation hyperspaceJumpAnimation_wind;
    private ImageView second_window1_open, second_window1_close, second_window2_open, second_window2_close,
            second_window3_open, second_window3_close, second_window4_open, second_window4_close, second_water, second_water_invisible;
    private int window1_tag = 0;
    private int window2_tag = 0;
    private int window3_tag = 0;
    private int window4_tag = 0;
    private boolean water_bool = false;
    private ImageView second_isemer;
    private boolean second_isemer_bool = false;
    private boolean ishand = false;
    private Dialog dialog;
    private ImageView second_window_invisible;
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";
    private final String ACTION_NAME = "发送广播";
    private Bitmap doorbitmap_on;
    private Bitmap doorbitmap_off;
    private boolean second_door_bool1 = true;
    private boolean wind_bool = false;
    private boolean escape_chuanglian_bool = false;
    private boolean escape_bool = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.secondpagefragment, container,
                false);
        uiti();

        registerBoradcastReceiver();

        doorbitmap_on = readBitMap(this.getActivity(), R.drawable.second_door_on);
        doorbitmap_off = readBitMap(this.getActivity(), R.drawable.second_door_off);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        return v;
    }

    // handler类接收数据
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (G.isnull == 1) {
                updateui();
            }
        }

        ;
    };

    private void uiti() {
        second_light1 = (ImageView) v.findViewById(R.id.second_light1);
        second_light2 = (ImageView) v.findViewById(R.id.second_light2);
        second_light3 = (ImageView) v.findViewById(R.id.second_light3);
        second_door = (ImageView) v.findViewById(R.id.second_door);
        second_window1 = (ImageView) v.findViewById(R.id.second_window1);
        second_window2 = (ImageView) v.findViewById(R.id.second_window2);
        second_window3 = (ImageView) v.findViewById(R.id.second_window3);
        second_window4 = (ImageView) v.findViewById(R.id.second_window4);
        second_window1_s = (ImageView) v.findViewById(R.id.second_window1_s);
        second_wind = (ImageView) v.findViewById(R.id.second_wind);
        second_window1_open = (ImageView) v.findViewById(R.id.second_window1_open);
        second_window2_open = (ImageView) v.findViewById(R.id.second_window2_open);
        second_window3_open = (ImageView) v.findViewById(R.id.second_window3_open);
        second_window4_open = (ImageView) v.findViewById(R.id.second_window4_open);
        second_window1_close = (ImageView) v.findViewById(R.id.second_window1_close);
        second_window2_close = (ImageView) v.findViewById(R.id.second_window2_close);
        second_window3_close = (ImageView) v.findViewById(R.id.second_window3_close);
        second_window4_close = (ImageView) v.findViewById(R.id.second_window4_close);
        second_water = (ImageView) v.findViewById(R.id.second_water);
        second_isemer = (ImageView) v.findViewById(R.id.second_isemer);
        second_window_invisible = (ImageView) v.findViewById(R.id.second_window_invisible);
        second_door_invisible = (ImageView) v.findViewById(R.id.second_door_invisible);
        second_wind_invisible = (ImageView) v.findViewById(R.id.second_wind_invisible);
        second_water_invisible = (ImageView) v.findViewById(R.id.second_water_invisible);
        second_escape = (ImageView) v.findViewById(R.id.second_escape);
        second_chuanglian = (ImageView) v.findViewById(R.id.second_chuanglian);

//        BackgroundHelper.setBackgroundEffect(second_wind,R.drawable.wind_off);
//        BackgroundHelper.setBackgroundEffect(second_water,R.drawable.water_off);
//        BackgroundHelper.setBackgroundEffect(second_escape,R.drawable.escape_img);
//        BackgroundHelper.setBackgroundEffect(second_chuanglian,R.drawable.chuanglian);




        second_light1.setOnClickListener(this);
        second_light2.setOnClickListener(this);
        second_light3.setOnClickListener(this);
        second_door.setOnClickListener(this);
//        second_window1.setOnClickListener(this);
//        second_window2.setOnClickListener(this);
        second_wind.setOnClickListener(this);
        second_window1_open.setOnClickListener(this);
        second_window2_open.setOnClickListener(this);
        second_window3_open.setOnClickListener(this);
        second_window4_open.setOnClickListener(this);
        second_window1_close.setOnClickListener(this);
        second_window2_close.setOnClickListener(this);
        second_window3_close.setOnClickListener(this);
        second_window4_close.setOnClickListener(this);
        second_water.setOnClickListener(this);
        second_isemer.setOnClickListener(this);
        second_escape.setOnClickListener(this);
        second_chuanglian.setOnClickListener(this);

    }

    private void updateui() {

        if (G.buffer != null) {

//            if (G.buffer[28] == 0) {//消毒灯
//                second_light3.setBackgroundResource(R.drawable.second_light_off);
//                second_light3_bool = false;
//            } else if (G.buffer[28] == 1) {
//                second_light3.setBackgroundResource(R.drawable.second_light_on);
//                second_light3_bool = true;
//            }
//            if (G.buffer[29] == 0) {//门
//                second_door.setBackgroundResource(R.drawable.second_door_off);
//                second_door.setImageBitmap(doorbitmap_off);
//                second_door_bool = false;
//            } else if (G.buffer[29] == 1) {
//                second_door.setBackgroundResource(R.drawable.second_door_on);
//                second_door.setImageBitmap(doorbitmap_on);
//                second_door_bool = true;
//            }
//            if (G.buffer[30] == 0) {//通风换气
//                wind_int = 100;
//                second_wind.setBackgroundResource(R.drawable.wind_off);
//                second_wind.clearAnimation();
//                hyperspaceJumpAnimation_wind = null;
//            } else if (G.buffer[30] == 1) {
//                wind_int = 101;
//                second_wind.setBackgroundResource(R.drawable.wind_on);
//                // 加载动画
//                if (hyperspaceJumpAnimation_wind == null) {
//
//                    hyperspaceJumpAnimation_wind = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_wind.startAnimation(hyperspaceJumpAnimation_wind);
//                }
//            }
//            if (G.buffer[31] == 1) {//软件紧急模式
//
//            } else {
//
//            }
//            if (G.buffer[32] == 1) {// 是否充电
//
//            } else {
//
//            }
//            if (G.buffer[33] == 0) {//灯1
//                second_light1.setBackgroundResource(R.drawable.second_light_off);
//                second_light1_bool = false;
//            } else if (G.buffer[33] == 1) {
//                second_light1.setBackgroundResource(R.drawable.second_light_on);
//                second_light1_bool = true;
//            }
//            if (G.buffer[34] == 0) {//灯2
//                second_light2.setBackgroundResource(R.drawable.second_light_off);
//                second_light2_bool = false;
//            } else if (G.buffer[34] == 1) {
//                second_light2.setBackgroundResource(R.drawable.second_light_on);
//                second_light2_bool = true;
//            }
////            Log.d("","-------------------999999999--------"+G.buffer[37]);
//            if (G.buffer[36] == 0) {//窗1
//                second_window1.setBackgroundResource(R.drawable.second_window_off);
//                second_window1.clearAnimation();
//                hyperspaceJumpAnimation = null;
//            } else if (G.buffer[36] == 1) {
//                second_window1.setBackgroundResource(R.drawable.second_window_on);
//                second_window1.clearAnimation();
//                hyperspaceJumpAnimation = null;
//            } else if (G.buffer[36] == 2) {
//                // 加载动画
//                if (hyperspaceJumpAnimation == null) {
//
//                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    if (window1_tag == 100) {
//                        second_window1.setBackgroundResource(R.drawable.second_window_off);
//                    } else if (window1_tag == 101) {
//                        second_window1.setBackgroundResource(R.drawable.second_window_on);
//                    }
//                    second_window1.startAnimation(hyperspaceJumpAnimation);
//                }
//            } else if (G.buffer[36] == 3) {
//                // 加载动画
//                if (hyperspaceJumpAnimation == null) {
//
//                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window1.setBackgroundResource(R.drawable.second_window_on);
//                    second_window1.startAnimation(hyperspaceJumpAnimation);
//                }
//            }
//            if (G.buffer[37] == 0) {//窗2
//                second_window2.setBackgroundResource(R.drawable.second_window_off);
//                second_window2.clearAnimation();
//                hyperspaceJumpAnimation1 = null;
//            } else if (G.buffer[37] == 1) {
//                second_window2.setBackgroundResource(R.drawable.second_window_on);
//                second_window2.clearAnimation();
//                hyperspaceJumpAnimation1 = null;
//            } else if (G.buffer[37] == 2) {
//                // 加载动画
//                Log.d("", "----------5------------------------" + window2_tag);
//                if (hyperspaceJumpAnimation1 == null) {
////
//                    hyperspaceJumpAnimation1 = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window2.setBackgroundResource(R.drawable.second_window_on);
//                    second_window2.startAnimation(hyperspaceJumpAnimation1);
//                }
//
//            } else if (G.buffer[37] == 3) {
//                // 加载动画
//                Log.d("", "----------5------------------------" + window2_tag);
//                if (hyperspaceJumpAnimation1 == null) {
////
//                    hyperspaceJumpAnimation1 = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window2.setBackgroundResource(R.drawable.second_window_off);
//                    second_window2.startAnimation(hyperspaceJumpAnimation1);
//                }
//
//            }
//            if (G.buffer[38] == 0) {//窗3
//                second_window3.setBackgroundResource(R.drawable.second_window_off);
//                second_window3.clearAnimation();
//                hyperspaceJumpAnimation2 = null;
//            } else if (G.buffer[38] == 1) {
//                second_window3.setBackgroundResource(R.drawable.second_window_on);
//                second_window3.clearAnimation();
//                hyperspaceJumpAnimation2 = null;
//            } else if (G.buffer[38] == 2) {
//                // 加载动画
//                if (hyperspaceJumpAnimation2 == null) {
//                    hyperspaceJumpAnimation2 = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window3.setBackgroundResource(R.drawable.second_window_on);
//                    second_window3.startAnimation(hyperspaceJumpAnimation2);
//                }
//
//            } else if (G.buffer[38] == 3) {
//                // 加载动画
//                if (hyperspaceJumpAnimation2 == null) {
//                    hyperspaceJumpAnimation2 = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window3.setBackgroundResource(R.drawable.second_window_off);
//                    second_window3.startAnimation(hyperspaceJumpAnimation2);
//                }
//
//            }
//            if (G.buffer[39] == 0) {//窗4
//                second_window4.setBackgroundResource(R.drawable.second_window_off);
//                second_window4.clearAnimation();
//                hyperspaceJumpAnimation3 = null;
//            } else if (G.buffer[39] == 1) {
//                second_window4.setBackgroundResource(R.drawable.second_window_on);
//                second_window4.clearAnimation();
//                hyperspaceJumpAnimation3 = null;
//            } else if (G.buffer[39] == 2) {
//                // 加载动画
//                if (hyperspaceJumpAnimation3 == null) {
//                    hyperspaceJumpAnimation3 = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window4.setBackgroundResource(R.drawable.second_window_on);
//                    second_window4.startAnimation(hyperspaceJumpAnimation3);
//                }
//
//            } else if (G.buffer[39] == 3) {
//                // 加载动画
//                if (hyperspaceJumpAnimation3 == null) {
//                    hyperspaceJumpAnimation3 = AnimationUtils.loadAnimation(
//                            this.getActivity(), R.anim.loading);
//                    second_window4.setBackgroundResource(R.drawable.second_window_off);
//                    second_window4.startAnimation(hyperspaceJumpAnimation3);
//                }
//
//            }
            if (G.buffer[31] == 1) {// 紧急模式
                second_isemer.setBackgroundResource(R.drawable.top_emer);
                second_isemer_bool = true;
                emerui(true);
                ishand = false;
            } else if (G.buffer[40] == 1) {
                second_isemer.setBackgroundResource(R.drawable.top_emer);
                ishand = true;
                second_isemer_bool = true;
                emerui(true);
            } else if (G.buffer[31] == 0) {
                second_isemer.setBackgroundResource(R.drawable.top_nomal);
                second_isemer_bool = false;
                ishand = false;
                emerui(false);
            } else if (G.buffer[40] == 0) {
                emerui(false);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                    Log.d("", "--------------1111111111111------------------");
                    order_s(18);
                }
                second_isemer.setBackgroundResource(R.drawable.top_nomal);
                second_isemer_bool = false;
                ishand = false;
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.second_light1:
                Log.d("","----------------s--------------"+second_light1_bool);
                if (!second_light1_bool) {
//                    Intent intent = new Intent(getActivity(), LockActivity.class);
//                    getActivity().startActivityForResult(intent,0);
                    order(12, 0, 1);
                    second_light1.setBackgroundResource(R.drawable.second_light_on);
                    second_light1_bool = true;
                } else {
                    order(12, 0, 0);
                    second_light1.setBackgroundResource(R.drawable.second_light_off);
                    second_light1_bool = false;
                }
                break;
            case R.id.second_light2:
                if (!second_light2_bool) {
                    order(12, 1, 1);
                    second_light2.setBackgroundResource(R.drawable.second_light_on);
                    second_light2_bool = true;
                } else {
                    order(12, 1, 0);
                    second_light2.setBackgroundResource(R.drawable.second_light_off);
                    second_light2_bool = false;
                }
                break;
            case R.id.second_light3:
                if (!second_light3_bool) {
                    order(16, 0, 1);
                    second_light3.setBackgroundResource(R.drawable.second_light_on);
                    second_light3_bool = true;
                } else {
                    order(16, 0, 0);
                    second_light3.setBackgroundResource(R.drawable.second_light_off);
                    second_light3_bool = false;
                }
                break;
            case R.id.second_door:
                if (!second_door_bool1) {

//                    Intent intent = new Intent(getActivity(), LockActivity.class);
//                    getActivity().startActivityForResult(intent, 0);
                    second_door_bool1 = true;
                    order(14, 0, 1);
                    second_door.setBackgroundResource(R.drawable.second_door_on);

                } else {
                    second_door_bool1 = false;
                    order(14, 0, 0);
//                    second_door.setImageBitmap(doorbitmap_off);
                    second_door.setBackgroundResource(R.drawable.second_door_off);

                }
                break;
            case R.id.second_window1:

                if (second_window1_int == 1000) {
                    order(13, 0, 1);
                } else if (second_window1_int == 1001) {
                    order(13, 0, 0);
                } else if (second_window1_int == 1002) {
                    order(13, 0, 2);
                }
//                if (!second_window1_bool) {
//                    order(13,0,1);
//                    second_window1.setBackgroundResource(R.drawable.second_window_on);
//                    second_window1_bool = true;
//                } else {
//                    order(13,0,0);
//                    second_window1.setBackgroundResource(R.drawable.second_window_off);
//                    second_window1_bool = false;
//                }
                break;
            case R.id.second_window2:
                if (second_window2_int == 2000) {
                    order(13, 1, 1);
                } else if (second_window2_int == 2001) {
                    order(13, 1, 0);
                } else if (second_window2_int == 2002) {
                    order(13, 1, 2);
                }
                break;
            case R.id.second_window3:
                if (second_window3_int == 3000) {
                    order(13, 2, 1);
                } else if (second_window3_int == 3001) {
                    order(13, 2, 0);
                } else if (second_window3_int == 3002) {
                    order(13, 2, 2);
                }
                break;
            case R.id.second_window4:
                if (second_window4_int == 4000) {
                    order(13, 3, 1);
                } else if (second_window4_int == 4001) {
                    order(13, 3, 0);
                } else if (second_window4_int == 4002) {
                    order(13, 3, 2);
                }
                break;
            case R.id.second_window1_open:
                window1_tag = 101;
                order(13, 0, 1);
                break;
            case R.id.second_window1_close:
                window1_tag = 100;
                order(13, 0, 0);
                break;
            case R.id.second_window2_open:
                window2_tag = 201;
                order(13, 1, 1);
                break;
            case R.id.second_window2_close:
                window2_tag = 200;
                order(13, 1, 0);
                break;
            case R.id.second_window3_open:
                window3_tag = 301;
                order(13, 2, 1);
                break;
            case R.id.second_window3_close:
                window3_tag = 300;
                order(13, 2, 0);
                break;
            case R.id.second_window4_open:
                window4_tag = 401;
                order(13, 3, 1);
                break;
            case R.id.second_window4_close:
                window4_tag = 400;
                order(13, 3, 0);
                break;
            case R.id.second_wind:
                if (wind_bool) {
                    order(15, 0, 0);
                    wind_bool = false;
                } else {
                    order(15, 0, 1);
                    wind_bool = true;
                }
                break;
            case R.id.second_water:
                if (water_bool) {
                    order(19, 0, 0);
                    second_water.setBackgroundResource(R.drawable.water_off);
                    water_bool = false;
                } else {
                    order(19, 0, 1);
                    second_water.setBackgroundResource(R.drawable.water_off);
                    water_bool = true;
                }
                break;
            case R.id.second_isemer:
                if (second_isemer_bool) {
                    if (ishand) {
                        dialog = dialog();
                        dialog.show();
                    } else {
                        order_s(18);//退出紧急模式
                        second_isemer.setBackgroundResource(R.drawable.top_nomal);
                        emerui(false);
                        second_isemer_bool = false;
                        Log.d("", "--------------------1-------------");
                    }

                } else {
                    order_s(17);//进入紧急模式
                    second_isemer.setBackgroundResource(R.drawable.top_emer);
                    emerui(true);
                    second_isemer_bool = true;
                }
                break;
            case R.id.second_escape://逃逸口
                if (escape_bool){
                    order(33, 0, 1);
                    escape_chuanglian_bool = false;
                }else {
                    order(33, 0, 0);
                    escape_bool = true;
                }
                break;
            case R.id.second_chuanglian://窗帘
                if (escape_chuanglian_bool){
                    order(34, 0, 1);
                    escape_chuanglian_bool = false;
                }else {
                    order(34, 0, 0);
                    escape_chuanglian_bool = true;
                }
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
            if (G.socket != null) {
                G.socket.getOutputStream().write(buffer_control);
            }
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

    private void emerui(boolean bool) {
        if (bool) {
            second_window1.setVisibility(View.GONE);
            second_window1_open.setVisibility(View.GONE);
            second_window1_close.setVisibility(View.GONE);
            second_window2.setVisibility(View.GONE);
            second_window2_open.setVisibility(View.GONE);
            second_window2_close.setVisibility(View.GONE);
            second_window3.setVisibility(View.GONE);
            second_window3_open.setVisibility(View.GONE);
            second_window3_close.setVisibility(View.GONE);
            second_window4.setVisibility(View.GONE);
            second_window4_open.setVisibility(View.GONE);
            second_window4_close.setVisibility(View.GONE);
            second_door.setVisibility(View.GONE);
            second_water.setVisibility(View.GONE);
            second_wind.setVisibility(View.GONE);
            second_window_invisible.setVisibility(View.VISIBLE);
            second_water_invisible.setVisibility(View.VISIBLE);
            second_door_invisible.setVisibility(View.VISIBLE);
            second_wind_invisible.setVisibility(View.VISIBLE);
        } else {
            second_window1.setVisibility(View.VISIBLE);
            second_window1_open.setVisibility(View.VISIBLE);
            second_window1_close.setVisibility(View.VISIBLE);
            second_window2.setVisibility(View.VISIBLE);
            second_window2_open.setVisibility(View.VISIBLE);
            second_window2_close.setVisibility(View.VISIBLE);
            second_window3.setVisibility(View.VISIBLE);
            second_window3_open.setVisibility(View.VISIBLE);
            second_window3_close.setVisibility(View.VISIBLE);
            second_window4.setVisibility(View.VISIBLE);
            second_window4_open.setVisibility(View.VISIBLE);
            second_window4_close.setVisibility(View.VISIBLE);
            second_door.setVisibility(View.VISIBLE);
            second_water.setVisibility(View.VISIBLE);
            second_wind.setVisibility(View.VISIBLE);
            second_window_invisible.setVisibility(View.GONE);
            second_water_invisible.setVisibility(View.GONE);
            second_door_invisible.setVisibility(View.GONE);
            second_wind_invisible.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                Log.d("", "--------------6-------------" + wind_int);
                order(14, 0, 1);
                second_door.setImageBitmap(doorbitmap_on);
                second_door_bool = true;

            }
        }

    };

    private Dialog dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage("请手动退出紧急模式")
                .setCancelable(false);
        AlertDialog alert = builder.create();

        return alert;
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        //注册广播
        this.getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    private static short bigByteToShort(byte[] bArr, int start) {
        short m = (short) (((bArr[start + 0] & 0xff) << 8) | (bArr[start + 1] & 0xff));
        return m;
    }


}
