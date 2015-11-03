package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.view.CircleImageView;

public class DoctorRoomListAdapter extends SimpleAdapter<Doctor> {

	protected static class ViewHolder extends BaseViewHolder {
		private CircleImageView doctorIcon;
		private TextView doctorName;
		private TextView doctorType;
		private TextView patientCount;
	}

	public DoctorRoomListAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		Doctor doctor = (Doctor) getItem(position);
		viewHolder.doctorName.setText(doctor.getDoctorName());
		viewHolder.doctorType.setText(doctor.getEMRType());
		viewHolder.patientCount.setText(String.valueOf(doctor.getWaittingCount()));
		if (doctor.getGender() == 1) {// 1代表男
			viewHolder.doctorIcon.setImageResource(R.drawable.head_man);
		} else {
			viewHolder.doctorIcon.setImageResource(R.drawable.head_lady);
		}
		viewHolder.doctorIcon.loadImage(doctor.getPhotoSIDURL(), position);
	}

	@Override
	protected void initHolder(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		viewHolder.doctorIcon = (CircleImageView) convertView.findViewById(R.id.doctor_icon);
		viewHolder.doctorName = (TextView) convertView.findViewById(R.id.doctor_name);
		viewHolder.doctorType = (TextView) convertView.findViewById(R.id.doctor_type);
		viewHolder.patientCount = (TextView) convertView.findViewById(R.id.patient_count);
	}

	@Override
	protected int getViewResource() {
		return R.layout.item_doctor_list;
	}

}
