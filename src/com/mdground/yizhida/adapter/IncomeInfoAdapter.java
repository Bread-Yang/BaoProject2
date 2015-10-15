package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;

public class IncomeInfoAdapter extends SimpleAdapter<String> {

	protected static class ViewHolder extends BaseViewHolder {
		TextView tvIncomeType;
		TextView tvTime;
		TextView tvIncome;
	}

	public IncomeInfoAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		// ViewHolder viewHolder = (ViewHolder) holder;

	}

	@Override
	protected void initHolder(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		viewHolder.tvIncomeType = (TextView) convertView.findViewById(R.id.tv_income_type);
		viewHolder.tvIncomeType = (TextView) convertView.findViewById(R.id.tv_time);
		viewHolder.tvIncomeType = (TextView) convertView.findViewById(R.id.tv_income);
	}

	@Override
	protected int getViewResource() {
		return R.layout.item_income_detial;
	}
}
