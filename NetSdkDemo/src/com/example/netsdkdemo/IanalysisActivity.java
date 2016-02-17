package com.example.netsdkdemo;

import com.xm.view.RuleArea;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

public class IanalysisActivity extends Activity{
	private RuleArea mRuleArea;
	private Bitmap mBitMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ianalysis);
		init();
	}
	private void init() {
		Resources res = getResources();
		mBitMap = BitmapFactory.decodeResource(res, R.drawable.point);
		mRuleArea = (RuleArea) findViewById(R.id.rulearea);
		mRuleArea.setPointImageResource(mBitMap);
	}
}
