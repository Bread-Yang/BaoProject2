package com.mdground.yizhida.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.view.CircleImageView;

/**
 * 选择医生Adapter
 * 
 * @author Administrator
 * @param <T>
 * 
 */
public class SelectDoctorRoomListAdapter extends SimpleAdapter<Doctor> {

	protected static class ViewHolder extends BaseViewHolder {
		private CircleImageView doctorIcon;
		private TextView doctorName;
		private TextView doctorType;
		private TextView patientCount;
		private ImageView selectedIcon;
		private LinearLayout layout;
	}

	public SelectDoctorRoomListAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		Doctor doctor = getItem(position);
		viewHolder.doctorName.setText(doctor.getDoctorName());
		viewHolder.doctorType.setText(doctor.getEMRType());
		viewHolder.patientCount.setText(String.valueOf(doctor.getWaittingCount()));
		if (doctor.isSelected()) {
			viewHolder.selectedIcon.setVisibility(View.VISIBLE);
			viewHolder.layout.setVisibility(View.GONE);
		} else {
			viewHolder.selectedIcon.setVisibility(View.GONE);
			viewHolder.layout.setVisibility(View.VISIBLE);
		}

		if (doctor.getGender() == 1) {// 1代表男
			viewHolder.doctorIcon.setImageResource(R.drawable.head_man);
		} else {
			viewHolder.doctorIcon.setImageResource(R.drawable.head_lady);
		}
		viewHolder.doctorIcon.loadImage(doctor.getPhotoSIDURL());
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
		viewHolder.selectedIcon = (ImageView) convertView.findViewById(R.id.selcted_icon);
		viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);

	}

	@Override
	protected int getViewResource() {
		return R.layout.item_select_doctor;
	}

}
