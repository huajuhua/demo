package com.example.netsdkdemo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 *@project XMFamily 
 *@author huangwanshui	
 *@date 2014-3-8 œ¬ŒÁ02:11:18
 *@describe «∞∂À…Ë±∏…Ë÷√
 */
public class DevSettingActivity extends Activity implements OnClickListener,OnItemClickListener{
	private ViewHolder mViewHolder;
	private long mLoginId;									//…Ë±∏µ«¬Ωæ‰±˙
	private ImageButton mBackib;							//∑µªÿ∞¥≈•
	private ImageView mLogoib;								//Õº±Í∞¥≈•
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dev_setting);
		init();
		initData();
	}
	private void init() {
		mViewHolder = new ViewHolder();
		mViewHolder.datalist = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		//¬ºœÒ≈‰÷√
		map = new HashMap<String, Object>();
		map.put("item","¬ºœÒ…Ë÷√");
		mViewHolder.datalist.add(map);
		//¬Î¡˜≈‰÷√
		map = new HashMap<String, Object>();
		map.put("item", "¬Î¡˜≈‰÷√");
		mViewHolder.datalist.add(map);
		SettingAdapter adapter = new SettingAdapter(getApplicationContext(), mViewHolder.datalist);
		mViewHolder.mListView = (ListView) findViewById(R.id.set_lv);
		mViewHolder.mListView.setAdapter(adapter);
		mViewHolder.mListView.setOnItemClickListener(this);
	} 
	private void initData() {
		Intent it = getIntent();
		mLoginId = it.getLongExtra("LoginId", 0);
	}
	class ViewHolder {
		ListView mListView;
		ArrayList<HashMap<String, Object>> datalist = null;
		TextView mTitle_tv;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
		case 0://¬ºœÒ
			Intent record_it = new Intent(getApplicationContext(),RecordConfig.class);
			record_it.putExtra("LoginId", mLoginId);
			startActivity(record_it);
			break;
		case 1://±‡¬Î
			Intent encode_it = new Intent(getApplicationContext(),EncodeConfig.class);
			encode_it.putExtra("LoginId", mLoginId);
			startActivity(encode_it);
			break;
		case 2://camera…Ë÷√
			Intent camera_it = new Intent(getApplicationContext(),CameraConfig.class);
			camera_it.putExtra("LoginId", mLoginId);
			startActivity(camera_it);
			
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
