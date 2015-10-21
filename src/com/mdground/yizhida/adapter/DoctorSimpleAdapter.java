package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Doctor;

public class DoctorSimpleAdapter extends SimpleAdapter<Doctor> {

	private int selectedItem = -1;
	
	protected static class ViewHolder extends BaseViewHolder {
		TextView name;
	}

	public DoctorSimpleAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		Doctor doctor = getItem(position);
		if (selectedItem >= 0 && selectedItem == position) {
			convertView.setBackgroundResource(R.color.click_gray);
		}else{
			convertView.setBackgroundResource(R.drawable.selector_bg_listview_item_2);
		}
		viewHolder.name.setText(doctor.getDoctorName());

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
		return R.layout.simple_doctor_item;
	}

	public void setSelectItem(int nCurrentDoctorIndex) {
		this.selectedItem = nCurrentDoctorIndex;
	}

}
