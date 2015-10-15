package com.mdground.yizhida.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.extras.pinnedsectionlistview.PinnedSectionListView.PinnedSectionListAdapter;
import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.AppointmentInfo;

/**
 * 预约信息适配器
 * 
 * @author Vincent
 * 
 */
public class AppointmentAdapter2 extends BaseAdapter implements PinnedSectionListAdapter {

	String pattern = "00";
	java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);

	protected LayoutInflater mInflater;
	protected Context mContext;

	private List<AppointmentInfo> appointmentList = new ArrayList<AppointmentInfo>();

	protected static class BaseHolder {
	}

	protected static class ViewHolder extends BaseHolder {
		private TextView squencesNmeber;
		private TextView patientName;
		private TextView patientSex;
		private TextView patientAge;
		private TextView remark;
		private View basicInfoLayout;
		private TextView operator;
	}

	protected static class GroupViewHolder extends BaseHolder {
		private TextView groupName;
	}

	public AppointmentAdapter2(Context context, List<AppointmentInfo> appointmentList) {
		this.mInflater = LayoutInflater.from(context);
		this.appointmentList = appointmentList;
		this.mContext = context;
	}

	@Override
	public int getViewTypeCount() {
		return 6;
	}

	@Override
	public int getCount() {
		return appointmentList == null ? 0 : appointmentList.size();
	}

	@Override
	public AppointmentInfo getItem(int position) {
		return appointmentList == null ? null : appointmentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case AppointmentInfo.GROUP:
				convertView = mInflater.inflate(R.layout.waitting_patient_group_item, null);
				holder = createGroupHolder(convertView);
				convertView.setTag(holder);
				break;
			default:
				convertView = mInflater.inflate(R.layout.waitting_patient_list_item, null);
				holder = createItemHolder(convertView);
				convertView.setTag(holder);
				break;
			}
		} else {
			holder = (BaseHolder) convertView.getTag();
		}

		switch (type) {
		case AppointmentInfo.GROUP:
			bindGroupData(position, holder);
			break;

		default:
			bindItemData(position, holder);
			break;
		}

		return convertView;
	}

	private void bindGroupData(int position, BaseHolder holder) {
		if (!(holder instanceof GroupViewHolder)) {
			return;
		}
		
		GroupViewHolder groupHolder = (GroupViewHolder) holder;
		AppointmentInfo appointmentInfo = getItem(position);
		// if (appointmentInfo.getType() == AppointmentInfo.GROUP) {
		int datePeriod = appointmentInfo.getOPDatePeriod();
		String timeRange = calcTime(datePeriod);
		groupHolder.groupName.setText(timeRange);
		// }
	}

	// 计算时间区间
	private String calcTime(int datePeriod) {
		StringBuffer sb = new StringBuffer();
		sb.append(df.format((datePeriod - 1) * 2));
		sb.append(":00");
		sb.append("~");
		sb.append(df.format((datePeriod) * 2));
		sb.append(":00");
		return sb.toString();
	}

	private void bindItemData(int position, BaseHolder holder) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}

		if (getCount() <= position) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;

		AppointmentInfo appointment = getItem(position);
		if (null != appointment) {
			String pattern = "0000.";
			java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
			viewHolder.squencesNmeber.setText(df.format(appointment.getOPNo()));
			viewHolder.patientName.setText(appointment.getPatientName());
			viewHolder.remark.setText(appointment.getRemark());
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
		}

	}

	private BaseHolder createGroupHolder(View convertView) {
		GroupViewHolder viewHolder = new GroupViewHolder();
		viewHolder.groupName = (TextView) convertView.findViewById(R.id.tv_group_name);
		return viewHolder;
	}

	private BaseHolder createItemHolder(View convertView) {
		ViewHolder itemHolder = new ViewHolder();
		itemHolder.squencesNmeber = (TextView) convertView.findViewById(R.id.sequence_number);
		itemHolder.patientName = (TextView) convertView.findViewById(R.id.name_text);
		itemHolder.patientSex = (TextView) convertView.findViewById(R.id.sex_text);
		itemHolder.patientAge = (TextView) convertView.findViewById(R.id.age_text);
		itemHolder.remark = (TextView) convertView.findViewById(R.id.remark_text);
		itemHolder.basicInfoLayout = convertView.findViewById(R.id.basic_info_layout);
		itemHolder.operator = (TextView) convertView.findViewById(R.id.operator_name);
		return itemHolder;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == AppointmentInfo.GROUP;
	}

	@Override
	public int getItemViewType(int position) {
		return appointmentList.get(position).getType();
	}

}