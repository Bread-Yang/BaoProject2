package com.mdground.yizhida.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.util.Tools;

/**
 * 挂号成功提框框
 * 
 * @author Vincent
 * 
 */
public class AppointmentDialog extends Dialog implements android.view.View.OnClickListener {
	private TextView tvDoctorName;
	private TextView tvRole;
	private TextView tvAppointmentNo;// 预约号
	private TextView tvWaitingCount;// 前面等待
	private TextView tvSure;
	
	public AppointmentDialog(Context context) {
		this(context, R.style.appointmentDialog);
	}
	
	public AppointmentDialog(Context context, int theme) {
		super(context, theme);
	}
	
	protected AppointmentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LinearLayout.inflate(getContext(), R.layout.dialog_appointment_success_, null);
		tvDoctorName = (TextView) view.findViewById(R.id.DoctorName);
		tvRole = (TextView) view.findViewById(R.id.role);
		tvAppointmentNo = (TextView) view.findViewById(R.id.AppointmentNo);
		tvWaitingCount = (TextView) view.findViewById(R.id.WaitingCount);
		tvSure = (TextView) view.findViewById(R.id.sure);
		tvSure.setOnClickListener(this);
		setContentView(view);

		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(false);
	}

	public void show(AppointmentInfo appointmentInfo) {
		if (appointmentInfo == null) {
			return;
		}
		show();
		tvDoctorName.setText(appointmentInfo.getDoctorName());
		tvRole.setText(appointmentInfo.getOPEMR()+"候诊号");
		tvAppointmentNo.setText(String.valueOf(appointmentInfo.getOPNo()));
		tvWaitingCount.setText(Tools.getFormat(getContext(), R.string.dialog_regist_wait_people, appointmentInfo.getWaitingCount()));
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}

}
