package com.mdground.yizhida.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.adapter.vo.EmployeeSchedule;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.view.CustomCheckBox;

/**
 * 紧急休假列表
 * 
 * @author Vincent
 * 
 */
public class VacationAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.CHINA);

	List<EmployeeSchedule> schedules;
	private String pattern = "00";
	java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);

	protected static class ViewHolder {
		private CustomCheckBox cbDate;
		private LinearLayout mRadioButtons;
		private TextView tvWeek;
		List<CustomCheckBox> customCheckBoxs;
		public TextView tvDate;
	}

	public VacationAdapter(Context context, List<EmployeeSchedule> schedules) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.schedules = schedules;
	}

	@Override
	public int getCount() {
		return schedules == null ? 0 : schedules.size();
	}

	@Override
	public Object getItem(int position) {
		return schedules == null ? null : schedules.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_vacation, null);
			initHolder(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bindData(viewHolder, position, convertView);
		return convertView;
	}

	private void bindData(ViewHolder viewHolder, int position, View convertView) {
		EmployeeSchedule ems = schedules.get(position);
		if (ems == null) {
			return;
		}
		List<Schedule> itemSchedules = ems.getSchedules();
		Collections.sort(itemSchedules, new Comparator<Schedule>() {

			@Override
			public int compare(Schedule lhs, Schedule rhs) {
				return lhs.compareTo(rhs);
			}
		});
		viewHolder.mRadioButtons.removeAllViews();
		viewHolder.customCheckBoxs.clear();
		for (int i = 0; i < itemSchedules.size(); i++) {
			Schedule s = itemSchedules.get(i);
			CustomCheckBox rb = createRadioButton(s, viewHolder);
			viewHolder.mRadioButtons.addView(rb);
		}
		viewHolder.tvDate.setText(DateUtils.getDateString(ems.getDate(), sdf));
		// 是否是休假
		if (isVacation(ems.getSchedules())) {
			viewHolder.cbDate.setEnable(true);
		}
		viewHolder.cbDate.setTag(viewHolder);
		viewHolder.cbDate.setTag(R.id.tag_schedule, ems);

		viewHolder.tvWeek.setText(DateUtils.getWeekDay(ems.getDate()));
	}

	/**
	 * 创建一个radioButton
	 * 
	 * @param s
	 * @param viewHolder
	 * @return
	 */
	@SuppressLint("NewApi")
	private CustomCheckBox createRadioButton(Schedule s, ViewHolder viewHolder) {
		CustomCheckBox rb = new CustomCheckBox(mContext);
		StringBuffer sb = new StringBuffer();
		sb.append(df.format(s.getBeginHour() / 60) + ":" + df.format(s.getBeginHour() % 60));
		sb.append("~");
		sb.append(df.format(s.getEndHour() / 60) + ":" + df.format(s.getEndHour() % 60));
		sb.append("\n");
		rb.setText(sb.toString());

		Date today = new Date();
		Date endDate = new Date(s.getWorkDate().getTime() + s.getEndHour() * 60 * 1000);

		if (s.getStatus() == 3 || endDate.before(today)) {
			s.setSelected(true);
			rb.setEnable(true);
		} else {
			rb.setSelected(s.isSelected());
			rb.setOnSelectedChangeCallBack(new CustomCheckBox.OnSelectedChangeCallBack() {

				@Override
				public void onSelectedChange(View view, boolean isSelected) {
					Schedule schedule = (Schedule) view.getTag();
					schedule.setSelected(isSelected);

					ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_holder);
					boolean isAllSelected = true;
					List<CustomCheckBox> customCheckBoxs = viewHolder.customCheckBoxs;
					for (int i = 0; i < customCheckBoxs.size(); i++) {
						CustomCheckBox checkBox = customCheckBoxs.get(i);
						if (!checkBox.isSelected()) {
							isAllSelected = false;
							break;
						}
					}
					viewHolder.cbDate.setSelected(isAllSelected);
				}
			});
			//只添加
			viewHolder.customCheckBoxs.add(rb);
		}
		rb.setTag(s);
		rb.setTag(R.id.tag_holder, viewHolder);
		return rb;
	}

	private void initHolder(ViewHolder viewHolder, View convertView) {
		viewHolder.cbDate = (CustomCheckBox) convertView.findViewById(R.id.radio_date);
		viewHolder.customCheckBoxs = new ArrayList<CustomCheckBox>();
		viewHolder.cbDate.setOnSelectedChangeCallBack(new CustomCheckBox.OnSelectedChangeCallBack() {

			@Override
			public void onSelectedChange(View view, boolean isSelected) {
				ViewHolder holder = (ViewHolder) view.getTag();
				EmployeeSchedule ems = (EmployeeSchedule) view.getTag(R.id.tag_schedule);

				// 更新显示控件
				List<CustomCheckBox> customCheckBoxs = holder.customCheckBoxs;
				for (int i = 0; i < customCheckBoxs.size(); i++) {
					CustomCheckBox checkBox = customCheckBoxs.get(i);
					checkBox.setSelected(isSelected);
				}

				// 更新数据
				List<Schedule> schedules = ems.getSchedules();
				if (schedules == null) {
					return;
				}
				for (int i = 0; i < schedules.size(); i++) {
					Schedule schedule = schedules.get(i);
					schedule.setSelected(isSelected);
				}
			}
		});
		viewHolder.mRadioButtons = (LinearLayout) convertView.findViewById(R.id.layout_radioButtons);
		viewHolder.tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
		viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
	}

	private boolean isVacation(List<Schedule> schedules) {
		if (schedules == null || schedules.size() == 0) {
			return false;
		}

		boolean isVacation = true;
		Date today = new Date();
		for (int i = 0; i < schedules.size(); i++) {
			Schedule s = schedules.get(i);
			Date endDate = new Date(s.getWorkDate().getTime() + s.getEndHour() * 60 * 1000);
			if (endDate.after(today) && s.getStatus() == 1) {
				isVacation = false;
				break;
			}
		}

		return isVacation;
	}

}
