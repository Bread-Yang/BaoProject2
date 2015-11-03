package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;

public class IncomeCategoryAdapter extends SimpleAdapter<String> {

	protected static class ViewHolder extends BaseViewHolder {
		TextView name;
	}

	public IncomeCategoryAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		String category = getItem(position);
		viewHolder.name.setText(category);
	}

	@Override
	protected void initHolder(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		viewHolder.name = (TextView) convertView.findViewById(R.id.doctor_name);
	}

	@Override
	protected int getViewResource() {
		return R.layout.item_simple_doctor;
	}

}
