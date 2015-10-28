package com.mdground.yizhida.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.handmark.pulltorefresh.library.extras.pinnedsectionlistview.PinnedSectionListView.PinnedSectionListAdapter;
import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.AppointmentInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		private RelativeLayout rlt_group;
		private TextView tv_group_name;
		private TextView tv_group_num;
		private TextView squencesNmeber;
		private ImageView iv_booking_icon;
		private TextView patientName;
		private TextView patientSex;
		private TextView patientAge;
		private TextView remark;
		private View basicInfoLayout;
		private TextView operator;
	}

	protected static class GroupViewHolder extends BaseHolder {
		private RelativeLayout rlt_group;
		private TextView tv_group_name;
		private TextView tv_group_num;
	}
	
	protected static class EmptyViewHolder extends BaseHolder {
		private TextView tv_tips;
	}

	public AppointmentAdapter2(Context context, List<AppointmentInfo> appointmentList) {
		this.mInflater = LayoutInflater.from(context);
		this.appointmentList = appointmentList;
		this.mContext = context;
	}

	@Override
	public int getViewTypeCount() {
		return 7;
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
			// convertView =
			// mInflater.inflate(R.layout.item_waitting_patient_list, null);
			// holder = createItemHolder(convertView);
			// convertView.setTag(holder);
			switch (type) {
			case AppointmentInfo.GROUP:
				convertView = mInflater.inflate(R.layout.item_waitting_patient_group, null);
				holder = createGroupHolder(convertView);
				convertView.setTag(holder);
				break;
			case AppointmentInfo.EMPTY:
				convertView = mInflater.inflate(R.layout.item_waitting_patient_empty, null);
				holder = createEmptyHolder(convertView);
				convertView.setTag(holder);
				break;
			default:
				convertView = mInflater.inflate(R.layout.item_waitting_patient_list, null);
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
			
			Calendar c = Calendar.getInstance();
			int currentHour = c.get(Calendar.HOUR_OF_DAY); // 当前时间

			currentHour = currentHour / 2 + 1;
			
			if (currentHour == getItem(position).getOPDatePeriod()) {
				((GroupViewHolder)holder).rlt_group.setBackgroundColor(Color.parseColor("#E8F6FF"));
			} else {
				((GroupViewHolder)holder).rlt_group.setBackgroundColor(Color.parseColor("#EAEBEC"));
			}
			break;
		case AppointmentInfo.EMPTY:
			switch (getItem(position).getOPStatus()) {
			
			case AppointmentInfo.STATUS_WATTING:
				((EmptyViewHolder)holder).tv_tips.setText(R.string.current_time_nobody_waiting);
				break;
			case AppointmentInfo.STATUS_PASSED:
				((EmptyViewHolder)holder).tv_tips.setText(R.string.current_time_nobody_pass);
				break;
			case AppointmentInfo.STATUS_FINISH:
				((EmptyViewHolder)holder).tv_tips.setText(R.string.current_time_nobody_finish);
				break;
			}
			break;
		default:
			bindItemData(position, holder);
			break;
		}

		// RelativeLayout rlt_group = ((ViewHolder)holder).rlt_group;
		// TextView tv_group_name = ((ViewHolder)holder).tv_group_name;
		// TextView tv_group_num = ((ViewHolder)holder).tv_group_num;
		//
		// if (type != AppointmentInfo.GROUP) {
		// rlt_group.setVisibility(View.GONE);
		// } else {
		// rlt_group.setVisibility(View.VISIBLE);
		// tv_group_name.setTypeface(MedicalConstant.NotoSans_Regular);
		//
		// int datePeriod = appointmentList.get(position).getOPDatePeriod();
		// String timeRange = calcTime(datePeriod);
		// tv_group_name.setText(timeRange);
		//
		// String group_num =
		// mContext.getResources().getString(R.string.group_num);
		// group_num = String.format(group_num,
		// appointmentList.get(position).getGroup_num());
		// tv_group_num.setText(group_num);
		// }
		//
		// bindItemData(position, holder);

		return convertView;
	}

	private void bindGroupData(int position, BaseHolder holder) {
		if (!(holder instanceof GroupViewHolder)) {
			return;
		}

		GroupViewHolder groupHolder = (GroupViewHolder) holder;
		AppointmentInfo appointmentInfo = getItem(position);
		int datePeriod = appointmentInfo.getOPDatePeriod();
		String timeRange = calcTime(datePeriod);
		groupHolder.tv_group_name.setText(timeRange);

		groupHolder.tv_group_name.setTypeface(MedicalConstant.NotoSans_Regular);

		String group_num = mContext.getResources().getString(R.string.group_num);
		group_num = String.format(group_num, appointmentList.get(position).getGroup_num());
		groupHolder.tv_group_num.setText(group_num);
	}

	// 计算时间区间
	private String calcTime(int datePeriod) {
		if (datePeriod == 0) {
			return mContext.getString(R.string.emergency);
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(df.format((datePeriod - 1) * 2));
			sb.append(":00");
			sb.append("~");
			sb.append(df.format((datePeriod) * 2));
			sb.append(":00");
			return sb.toString();
		}
	}
	
	private void bindEmptyData(int position, BaseHolder holder) {
		if (!(holder instanceof GroupViewHolder)) {
			return;
		}

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
			viewHolder.squencesNmeber.setTypeface(MedicalConstant.NotoSans_Regular);
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
			if (appointment.getOPType() == 1) { // 是微信预约
				viewHolder.iv_booking_icon.setVisibility(View.VISIBLE);
				
				if ((appointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
					viewHolder.iv_booking_icon.setImageResource(R.drawable.booking2);
				} else {
					viewHolder.iv_booking_icon.setImageResource(R.drawable.booking);
				}
				
			} else {
				viewHolder.iv_booking_icon.setVisibility(View.INVISIBLE);
			}
			if (appointment.isEmergency()) {
				viewHolder.iv_booking_icon.setVisibility(View.VISIBLE);
				
				if ((appointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
					viewHolder.iv_booking_icon.setImageResource(R.drawable.emergency2);
				} else {	
					viewHolder.iv_booking_icon.setImageResource(R.drawable.emergency);
				}
			}
		}

	}

	private BaseHolder createGroupHolder(View convertView) {
		GroupViewHolder viewHolder = new GroupViewHolder();
		viewHolder.rlt_group = (RelativeLayout) convertView.findViewById(R.id.rlt_group);
		viewHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
		viewHolder.tv_group_num = (TextView) convertView.findViewById(R.id.tv_group_num);
		return viewHolder;
	}
	
	private BaseHolder createEmptyHolder(View convertView) {
		EmptyViewHolder viewHolder = new EmptyViewHolder();
		viewHolder.tv_tips = (TextView) convertView.findViewById(R.id.tv_tips);
		return viewHolder;
	}

	private BaseHolder createItemHolder(View convertView) {
		ViewHolder itemHolder = new ViewHolder();
		itemHolder.rlt_group = (RelativeLayout) convertView.findViewById(R.id.rlt_group);
		itemHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
		itemHolder.tv_group_num = (TextView) convertView.findViewById(R.id.tv_group_num);
		itemHolder.squencesNmeber = (TextView) convertView.findViewById(R.id.sequence_number);
		itemHolder.iv_booking_icon = (ImageView) convertView.findViewById(R.id.iv_booking_icon);
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
