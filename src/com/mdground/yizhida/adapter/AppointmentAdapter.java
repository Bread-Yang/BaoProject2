package com.mdground.yizhida.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;

/**
 * 预约信息适配器
 * 
 * @author Vincent
 * 
 */
public class AppointmentAdapter extends SimpleAdapter<AppointmentInfo> {
	private int role;

	public static final int DIAGNOSING = 0;
	public static final int WATTING = 1;
	public static final int FINISH = 2;
	public static final int PASSED = 3;
	public static final int UNKNOW = 4;

	protected static class ViewHolder extends BaseViewHolder {
		private TextView squencesNmeber;
		private TextView patientName;
		private TextView patientSex;
		private TextView patientAge;
		private TextView remark;
		private View basicInfoLayout;
		private TextView operator;
	}

	public AppointmentAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	private void setViewTag(ViewHolder viewHolder, AppointmentInfo appointment) {
		viewHolder.squencesNmeber.setTag(appointment);
		viewHolder.patientName.setTag(appointment);
		viewHolder.patientSex.setTag(appointment);
		viewHolder.remark.setTag(appointment);
		viewHolder.operator.setTag(appointment);
		viewHolder.patientAge.setTag(appointment);
	}

	@Override
	protected void bindData(BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}

		if (getCount() <= position) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		AppointmentInfo appointment = getItem(position);
		if (null != appointment) {
			String pattern = "0000";
			java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
			viewHolder.squencesNmeber.setText(df.format(appointment.getOPNo()));
			viewHolder.patientName.setText(appointment.getPatientName());
			viewHolder.remark.setText(appointment.getRemark());
			setViewTag(viewHolder, appointment);
			long status = appointment.getOPStatus();
			// 就诊中
			if ((status & AppointmentInfo.STATUS_DIAGNOSING) == AppointmentInfo.STATUS_DIAGNOSING) {
				viewHolder.basicInfoLayout.setBackgroundResource(R.drawable.box1_on);
				viewHolder.patientAge.setText("就诊中");
				viewHolder.patientSex.setText("");
				viewHolder.patientAge.setTextColor(Color.WHITE);
				viewHolder.patientName.setTextColor(Color.WHITE);
				viewHolder.squencesNmeber.setTextColor(Color.WHITE);
				viewHolder.remark.setTextColor(Color.WHITE);
			} else {
				viewHolder.basicInfoLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
				viewHolder.patientAge.setText(appointment.getPatientAge());
				viewHolder.patientSex.setText(appointment.getPatientGenderStr());
				viewHolder.patientAge.setTextColor(Color.BLACK);
				viewHolder.patientName.setTextColor(Color.BLACK);
				viewHolder.squencesNmeber.setTextColor(Color.BLACK);
				viewHolder.remark.setTextColor(Color.GRAY);
			}
			// 根据登陆角色，和病人当前状态显示不同的信息
			Employee employee = ((MedicalAppliction) mContext.getApplicationContext()).getLoginEmployee();
			if (employee != null) {
				role = employee.getEmployeeRole();
				if ((role & Employee.DOCTOR) == Employee.DOCTOR) {
					initDoctorOperator(viewHolder, appointment);
				} else if ((role & Employee.NURSE) == Employee.NURSE) {
					initNurseOperator(viewHolder, appointment);
				}
			}
		}

	}

	@Override
	protected void initHolder(BaseViewHolder viewHolder, View convertView) {
		if (viewHolder instanceof ViewHolder) {
			ViewHolder holder = (ViewHolder) viewHolder;
			holder.squencesNmeber = (TextView) convertView.findViewById(R.id.sequence_number);
			holder.patientName = (TextView) convertView.findViewById(R.id.name_text);
			holder.patientSex = (TextView) convertView.findViewById(R.id.sex_text);
			holder.patientAge = (TextView) convertView.findViewById(R.id.age_text);
			holder.remark = (TextView) convertView.findViewById(R.id.remark_text);
			holder.basicInfoLayout = convertView.findViewById(R.id.basic_info_layout);
			holder.operator = (TextView) convertView.findViewById(R.id.operator_name);
		}
	}

	@Override
	protected int getViewResource() {
		return R.layout.item_waitting_patient_list;
	}

	private void initNurseOperator(ViewHolder viewHolder, AppointmentInfo appointment) {
		viewHolder.operator.setTag(appointment);
		// 就诊状态，显示结束
		if ((appointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) == AppointmentInfo.STATUS_DIAGNOSING) {
			viewHolder.operator.setText("");
		} else if ((appointment.getOPStatus() & AppointmentInfo.STATUS_WATTING) == AppointmentInfo.STATUS_WATTING) {
			// 候诊状态，显示分配
			viewHolder.operator.setText(mContext.getResources().getString(R.string.opt_assign));
		} else if ((appointment.getOPStatus() & AppointmentInfo.STATUS_PASSED) == AppointmentInfo.STATUS_PASSED) {// 过号状态
			viewHolder.operator.setText(mContext.getResources().getString(R.string.opt_rebuild));
		}
	}

	private void initDoctorOperator(ViewHolder viewHolder, AppointmentInfo appointment) {
		viewHolder.operator.setTag(appointment);
		// 就诊状态，无操作
		if ((appointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) == AppointmentInfo.STATUS_DIAGNOSING) {
			viewHolder.operator.setText(mContext.getResources().getString(R.string.opt_finish));
		} else if ((appointment.getOPStatus() & AppointmentInfo.STATUS_WATTING) == AppointmentInfo.STATUS_WATTING) {
			// 候诊状态，显示过号
			viewHolder.operator.setText(mContext.getResources().getString(R.string.opt_pass));
		}
	}

	@Override
	public int getItemViewType(int position) {
		if ((getItem(position).getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			return DIAGNOSING;
		} else if ((getItem(position).getOPStatus() & AppointmentInfo.STATUS_WATTING) != 0) {
			return WATTING;
		} else if ((getItem(position).getOPStatus() & AppointmentInfo.STATUS_FINISH) != 0) {
			return FINISH;
		} else if ((getItem(position).getOPStatus() & AppointmentInfo.STATUS_PASSED) != 0) {
			return PASSED;
		} else {
			return UNKNOW;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

}
