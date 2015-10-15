package com.mdground.yizhida.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SimpleAdapter<T> extends BaseAdapter {
	private static final String TAG = SimpleAdapter.class.getSimpleName();
	
	protected List<T> dataList;
	protected LayoutInflater mInflater;
	protected Context mContext;
	private Class<? extends BaseViewHolder> holderClass;

	public SimpleAdapter(Context context, Class<? extends BaseViewHolder> holderClass) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.holderClass = holderClass;
	}

	public void setDataList(List<T> t) {
		this.dataList = t;
	}

	public void clearDataList() {
		if (null != dataList) {
			this.dataList.clear();
		}

	}

	@Override
	public int getCount() {
		if (null != dataList) {
			return dataList.size();
		}
		return 0;
	}

	@Override
	public T getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseViewHolder viewHolder = null;
		if (null == convertView ) {
			if (holderClass == null) {
				Log.e(TAG, "holderClass is null");
				return convertView;
			}
			try {
				viewHolder = holderClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			convertView = mInflater.inflate(getViewResource(), null);
			initHolder(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (BaseViewHolder) convertView.getTag();
		}

		bindData(viewHolder, position, convertView);
		return convertView;
	}

	protected abstract  void bindData(BaseViewHolder holder, int position, View convertView);

	protected abstract void initHolder(BaseViewHolder holder, View convertView);

	protected abstract int getViewResource();

	// viewHolder父类
	protected static class BaseViewHolder {

	}

}
