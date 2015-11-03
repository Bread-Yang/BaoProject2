package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Patient;

public class PatientListAdapter extends SimpleAdapter<Patient> {

	protected static class ViewHolder extends BaseViewHolder {
		TextView sequenceNum;
		TextView name;
		TextView sex;
		TextView age;
	}

	public PatientListAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	private void initHolder(ViewHolder viewHolder, View convertView) {
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {

	}

	@Override
	protected void initHolder(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		viewHolder.sequenceNum = (TextView) convertView.findViewById(R.id.sequence_number);
		viewHolder.name = (TextView) convertView.findViewById(R.id.name_text);
		viewHolder.sex = (TextView) convertView.findViewById(R.id.sex_text);
		viewHolder.age = (TextView) convertView.findViewById(R.id.age_text);

	}

	@Override
	protected int getViewResource() {
		return R.layout.item_patient;
	}
}
