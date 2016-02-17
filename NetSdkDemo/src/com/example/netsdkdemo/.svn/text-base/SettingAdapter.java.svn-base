package com.example.netsdkdemo;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter{
	private ArrayList<HashMap<String, Object>> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	public SettingAdapter(Context context,ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null != mList ? mList.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.setlist, null);
			holder = new ViewHolder();
			holder.itmetv = (TextView) convertView.findViewById(R.id.item);
			holder.contenttv = (TextView) convertView.findViewById(R.id.content);
			holder.promptiv = (ImageView) convertView.findViewById(R.id.prompt);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		if(position == (mList.size() - 1)) {
			holder.contenttv.setVisibility(View.VISIBLE);
			holder.promptiv.setVisibility(View.GONE);
			//holder.contenttv.setText(mList.get(position).get("content").toString());
		}else {
			holder.promptiv.setVisibility(View.VISIBLE);
			holder.contenttv.setVisibility(View.GONE);
		}
		holder.itmetv.setText(mList.get(position).get("item").toString());
		
		return convertView;
	}
	class ViewHolder {
		TextView itmetv;
		TextView contenttv;
		ImageView promptiv;
	}
}
