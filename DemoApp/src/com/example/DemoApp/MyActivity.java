package com.example.DemoApp;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private KeyguardManager km;
    private KeyguardManager.KeyguardLock kl;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        wakeAndUnlock(false);
    }

    private void wakeAndUnlock(boolean b)
    {

        //获取电源管理器对象
        pm=(PowerManager) getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        //得到键盘锁管理器对象
        km= (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");
        if(b)
        {

            //点亮屏幕
            wl.acquire();


            //解锁
            kl.disableKeyguard();
        }
        else
        {
            //锁屏
            kl.reenableKeyguard();

            //释放wakeLock，关灯
            wl.release();
        }

    }


    public static void printDeviceInf(String tag){
        StringBuilder sb = new StringBuilder();
        sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
        sb.append("BOARD ").append(android.os.Build.BOARD).append("\n");
        sb.append("BOOTLOADER ").append(android.os.Build.BOOTLOADER).append("\n");
        sb.append("BRAND ").append(android.os.Build.BRAND).append("\n");
        sb.append("CPU_ABI ").append(android.os.Build.CPU_ABI).append("\n");
        sb.append("CPU_ABI2 ").append(android.os.Build.CPU_ABI2).append("\n");
        sb.append("DEVICE ").append(android.os.Build.DEVICE).append("\n");
        sb.append("DISPLAY ").append(android.os.Build.DISPLAY).append("\n");
        sb.append("FINGERPRINT ").append(android.os.Build.FINGERPRINT).append("\n");
        sb.append("HARDWARE ").append(android.os.Build.HARDWARE).append("\n");
        sb.append("HOST ").append(android.os.Build.HOST).append("\n");
        sb.append("ID ").append(android.os.Build.ID).append("\n");
        sb.append("MANUFACTURER ").append(android.os.Build.MANUFACTURER).append("\n");
        sb.append("MODEL ").append(android.os.Build.MODEL).append("\n");
        sb.append("PRODUCT ").append(android.os.Build.PRODUCT).append("\n");
        sb.append("RADIO ").append(android.os.Build.RADIO).append("\n");
        sb.append("SERIAL ").append(android.os.Build.SERIAL).append("\n");
        sb.append("TAGS ").append(android.os.Build.TAGS).append("\n");
        sb.append("TIME ").append(android.os.Build.TIME).append("\n");
        sb.append("TYPE ").append(android.os.Build.TYPE).append("\n");
        sb.append("USER ").append(android.os.Build.USER).append("\n");
        Log.i(tag, sb.toString());
    }
}
