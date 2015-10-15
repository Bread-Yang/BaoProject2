package com.mdground.yizhida.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.rota.ScheduleHelper;
import com.mdground.yizhida.adapter.vo.EmployeeSchedule;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.view.TimeRangView;
import com.mdground.yizhida.view.TimeRangView.TimeRangChangeListener;
import com.mdground.yizhida.view.TimeRangWheelView;
import com.mdground.yizhida.view.switchbtn.SwitchButton;

public class SchedulingAdapter extends BaseAdapter implements OnClickListener, TimeRangChangeListener {

	protected LayoutInflater mInflater;
	protected Context mContext;
	private TimeRangWheelView mTimeRangWheelView;
	private List<EmployeeSchedule> schedules;
	private Employee loginEmployee;
	private Date today = new Date();

	ListView mListView;

	private boolean isModify = false;

	protected static class ViewHolder {
		private SwitchButton mSwitchButton;
		private TextView mEmployeeName;
		private TimeRangView[] mTimeRangView;
		private LinearLayout mContenLayoutt;
		private LinearLayout mSchedulingLayout;
	}

	public SchedulingAdapter(Context context, List<EmployeeSchedule> schedules, Employee loginEmployee, TimeRangWheelView mTimeRangWheelView,
			ListView mListView) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.schedules = schedules;
		this.mTimeRangWheelView = mTimeRangWheelView;
		this.loginEmployee = loginEmployee;
		this.mListView = mListView;
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_scheduling, null);
			ViewHolder vholder = new ViewHolder();
			vholder.mSwitchButton = (SwitchButton) convertView.findViewById(R.id.btn_switch);
			vholder.mSwitchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					ViewHolder holder = (ViewHolder) buttonView.getTag(R.id.tag_holder);
					if (holder == null) {
						return;
					}

					int postion = (Integer) buttonView.getTag();
					EmployeeSchedule ems = schedules.get(postion);

					if (ems.isVacation() == isChecked) {
						isModify = true;
					}

					if (isChecked) {
						holder.mSchedulingLayout.setVisibility(View.VISIBLE);
						holder.mContenLayoutt.setVisibility(View.GONE);
						((View) holder.mContenLayoutt.getParent()).setBackgroundResource(R.color.white);
						ems.setVacation(false);
					} else {
						holder.mSchedulingLayout.setVisibility(View.GONE);
						holder.mContenLayoutt.setVisibility(View.VISIBLE);
						((View) holder.mContenLayoutt.getParent()).setBackgroundResource(R.color.shedule_table_passed_item_bg);
						ems.setVacation(true);
						List<Schedule> scheduleList = ems.getSchedules();
						if (scheduleList != null) {
							scheduleList.clear();
						}
					}

					if (postion == 1 || postion == 2) {
						Log.i("hh", "postion = " + postion + " isSelected = " + ems.isVacation());
					}

				}
			});
			vholder.mEmployeeName = (TextView) convertView.findViewById(R.id.tv_name);
			vholder.mTimeRangView = new TimeRangView[3];
			vholder.mTimeRangView[0] = (TimeRangView) convertView.findViewById(R.id.rang_time1);
			vholder.mTimeRangView[1] = (TimeRangView) convertView.findViewById(R.id.rang_time2);
			vholder.mTimeRangView[2] = (TimeRangView) convertView.findViewById(R.id.rang_time3);

			vholder.mTimeRangView[0].setOnClickListener(this);
			vholder.mTimeRangView[1].setOnClickListener(this);
			vholder.mTimeRangView[2].setOnClickListener(this);

			vholder.mTimeRangView[0].setTimeRangChangeListener(this);
			vholder.mTimeRangView[1].setTimeRangChangeListener(this);
			vholder.mTimeRangView[2].setTimeRangChangeListener(this);

			vholder.mContenLayoutt = (LinearLayout) convertView.findViewById(R.id.layout_vacation);
			vholder.mSchedulingLayout = (LinearLayout) convertView.findViewById(R.id.layout_scheduling);
			convertView.setTag(vholder);
			holder = vholder;
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bindData(position, holder);
		return convertView;
	}

	private void bindData(int position, ViewHolder holder) {
		EmployeeSchedule es = schedules.get(position);
		// 清空数据
		holder.mEmployeeName.setText(es.getEmployee().getEmployeeName());

		holder.mSwitchButton.setTag(position);
		holder.mSwitchButton.setTag(R.id.tag_holder, holder);
		if (es.isVacation()) {
			holder.mSchedulingLayout.setVisibility(View.GONE);
			holder.mContenLayoutt.setVisibility(View.VISIBLE);
			((View) holder.mContenLayoutt.getParent()).setBackgroundResource(R.color.shedule_table_passed_item_bg);
			holder.mSwitchButton.setChecked(false);
		} else {
			holder.mSchedulingLayout.setVisibility(View.VISIBLE);
			holder.mContenLayoutt.setVisibility(View.GONE);
			((View) holder.mContenLayoutt.getParent()).setBackgroundResource(R.color.white);
			holder.mSwitchButton.setChecked(true);
		}

		List<Schedule> schedules = es.getSchedules();
		for (int i = 0; i < 3; i++) {
			TimeRangView timeView = holder.mTimeRangView[i];
			timeView.setPostion(position);
			timeView.setItemIndex(i);
			if (schedules == null || i > (schedules.size() - 1)) {
				timeView.reset();
				continue;
			}
			Schedule schedule = schedules.get(i);
			if (schedule.getStatus() == 3) {// 休假不显示
				timeView.reset();
				continue;
			}
			timeView.setBeginTime(DateUtils.minToTimeRange(schedule.getBeginHour()));
			timeView.setEndTime(DateUtils.minToTimeRange(schedule.getEndHour()));
		}
	}

	@Override
	public void onTimeRangChange(TimeRangView view, String beginTime, String endTime) {
		if (loginEmployee == null) {
			return;
		}
		isModify = true;
		int postion = view.getPostion();// 改变行
		int itemIndex = view.getItemIndex();// 某行里的item
		if (postion >= this.schedules.size()) {
			return;
		}

		if (itemIndex >= 3) {
			return;
		}

		EmployeeSchedule es = this.schedules.get(postion);
		List<Schedule> esSchedules = es.getSchedules();
		if (esSchedules == null) {
			esSchedules = new ArrayList<Schedule>(3);
			es.setSchedules(esSchedules);
		}

		Schedule s = null;
		if (esSchedules.size() - 1 < itemIndex) {
			s = new Schedule();
			s.setStatus(1);
			s.setClinicId(loginEmployee.getClinicID());
			s.setEmployeeID(es.getEmployee().getEmployeeID());
			s.setUpdateTime(today);
			s.setWorkDate(es.getDate());
			esSchedules.add(s);
		} else {
			s = esSchedules.get(itemIndex);
		}
		s.setBeginHour(DateUtils.timeToHour(beginTime));
		s.setEndHour(DateUtils.timeToHour(endTime));
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TimeRangView && this.mTimeRangWheelView != null) {
			final TimeRangView rv = (TimeRangView) v;
			rv.setEditing(true);
			this.mTimeRangWheelView.setTimeChangeListener(rv);
			this.mTimeRangWheelView.show();
			rv.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					int postion = rv.getPostion();// 改变行
					mListView.smoothScrollToPositionFromTop(postion, 0);	
				}
			}, 100);
		}
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public boolean isModify() {
		return isModify;
	}

	@Override
	public void onFinishInput(TimeRangView view) {
		if (loginEmployee == null) {
			return;
		}
		int postion = view.getPostion();// 改变行
		int itemIndex = view.getItemIndex();// 某行里的item
		if (postion >= this.schedules.size()) {
			return;
		}

		if (itemIndex >= 3) {
			return;
		}

		EmployeeSchedule es = this.schedules.get(postion);
		List<Schedule> esSchedules = es.getSchedules();
		es.setSchedules(ScheduleHelper.mergeSchedulesToList(esSchedules));
		notifyDataSetChanged();
	}

	@Override
	public void onResetTime(TimeRangView view) {
		isModify = true;
		if (loginEmployee == null) {
			return;
		}
		int postion = view.getPostion();// 改变行
		int itemIndex = view.getItemIndex();// 某行里的item
		if (postion >= this.schedules.size()) {
			return;
		}

		if (itemIndex >= 3) {
			return;
		}

		EmployeeSchedule es = this.schedules.get(postion);
		List<Schedule> esSchedules = es.getSchedules();
		if (esSchedules != null && itemIndex < esSchedules.size()) {
			esSchedules.remove(itemIndex);
			notifyDataSetChanged();
		}
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

}
