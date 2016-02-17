package com.ark.newark_phone;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends FragmentActivity implements CategoryView.OnCategoryItemSelectedListener {
    /**
     * Called when the activity is first created.
     */
    /**
     * 页面list *
     */
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    /**
     * 页面title list *
     */
    List<String> titleList = new ArrayList<String>();

    private ViewPager viewPager;// 页卡内容
    private CategoryView categoryView = null;
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";
    private final String ACTION_NAME = "发送广播";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InitViewPager();

        SharedPreferences preferences = getSharedPreferences(LOCK, MODE_WORLD_READABLE);

        String lockPattenString = preferences.getString(LOCK_KEY, null);
        Log.d("", "-----------------1-----------=====" + lockPattenString);

        if (lockPattenString == null) {
            Intent intent = new Intent(this, LockSetupActivity.class);
            startActivity(intent);

        }
    }


    private void InitViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vPager);
        viewPager.setAdapter(new ArkAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);

        categoryView = (CategoryView) this.findViewById(R.id.categoryView);
        List<Integer> title = new ArrayList<Integer>();
        title.add(R.drawable.first_logo_on);
        title.add(R.drawable.second_logo_off);
//        title.add(R.drawable.third_logo_off);

        List<Long> value = new ArrayList<Long>();
        value.add(1L);
        value.add(2L);
//        value.add(3L);

        categoryView.setTitleDataSource(title);
        categoryView.setValueDataSource(value);

        categoryView.createUI();
        categoryView.setViewPager(viewPager);

        categoryView.setOnCategoryItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent mIntent = new Intent(ACTION_NAME);
        //发送广播
        sendBroadcast(mIntent);
    }


    public static class ArkAdapter extends FragmentPagerAdapter {

//		private List<Fragment> fragmentList;
//		private List<String> titleList;

        public ArkAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                FirstPageFragment vfragment = new FirstPageFragment();
                return vfragment;
            } else if (position == 1) {
                ThirdPageFragment vfragment2 = new ThirdPageFragment();
//                SecondPageFragment vfragment1 = new SecondPageFragment();
                return vfragment2;
            } else {
//                SecondPageFragment vfragment1 = new SecondPageFragment();
                ThirdPageFragment vfragment2 = new ThirdPageFragment();
                return vfragment2;

            }

        }
    }

    @Override
    public void onCategoryItemSelected(int selectedPosition, Long selectedValue, Integer selectedTitle) {

        if (selectedPosition == 0) {
            categoryView.findViewWithTag("0" + "a").setBackgroundResource(R.drawable.first_logo_on);
            categoryView.findViewWithTag("1" + "a").setBackgroundResource(R.drawable.second_logo_off);
//            categoryView.findViewWithTag("2"+"a").setBackgroundResource(R.drawable.third_logo_off);
        } else if (selectedPosition == 1) {
            categoryView.findViewWithTag("0" + "a").setBackgroundResource(R.drawable.first_logo_off);
            categoryView.findViewWithTag("1" + "a").setBackgroundResource(R.drawable.second_logo_on);
//            categoryView.findViewWithTag("2"+"a").setBackgroundResource(R.drawable.third_logo_off);
        } else if (selectedPosition == 2) {
            categoryView.findViewWithTag("0" + "a").setBackgroundResource(R.drawable.first_logo_off);
            categoryView.findViewWithTag("1" + "a").setBackgroundResource(R.drawable.second_logo_off);
            categoryView.findViewWithTag("2" + "a").setBackgroundResource(R.drawable.third_logo_on);
        }


    }
}
