package com.mdground.yizhida.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.extras.fixedheadtable.TableFixHeaders;
import com.handmark.pulltorefresh.library.extras.fixedheadtable.adapter.BaseTableAdapter;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.rota.ScheduleHelper;
import com.mdground.yizhida.activity.schedule.SchedulingActivity;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.ScheduleDao;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.util.Tools;

/**
 * 排班表数据
 * 
 * @author Vincent
 * 
 */
public class ScheduleTableAdapter extends BaseTableAdapter {
	private static final int DATE = 0;
	private static final int BODY = 1;
	private static final int ROW = 2;

	private List<Date> dates;
	private List<Employee> employees;
	private Context context;
	private ScheduleDao mScheduleDao;
	private TextView tvMounth;
	private TableFixHeaders mFixHeaders;

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int row = (Integer) v.getTag(R.id.tag_row);
			int column = (Integer) v.getTag(R.id.tag_column);
			if (row > 0 && column > 0) {
				Date date = dates.get(row - 1);
				Employee employee = employees.get(column - 1);
				Intent intent = new Intent();
				intent.setClass(context, SchedulingActivity.class);
				intent.putExtra(MemberConstant.EMPLOYEE_ID, employee.getEmployeeID());
				intent.putExtra(MemberConstant.SCHEDULE_DATE, date.getTime());
				((Activity) context).startActivityForResult(intent, MemberConstant.SCHEDULE_REQUESTCODE);
			}
		}
	};

	private class ViewHolder {
		View contentView;
	}

	private class ViewHolderDate extends ViewHolder {
		TextView tvDate;
		TextView tvToday;
		TextView tvWeek;
		TextView tvDay;
	}

	private class ViewHolderBody extends ViewHolder {
		TextView tvTime;
		ImageView imgAdd;
		View line;
	}

	public ScheduleTableAdapter(Context context, List<Employee> employees, List<Date> dates, TextView tvMounth) {
		this.dates = dates;
		this.employees = employees;
		this.context = context;
		mScheduleDao = ScheduleDao.getInstance(context);
		this.tvMounth = tvMounth;
	}

	@Override
	public int getRowCount() {
		return dates == null ? 0 : dates.size() + 1;
	}

	@Override
	public int getColumnCount() {
		return employees == null ? 0 : employees.size() + 1;
	}

	@Override
	public View getView(int row, int column, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Log.i("table", "row = " + row + " column = " + column);
		if (mFixHeaders == null && parent instanceof TableFixHeaders) {
			mFixHeaders = (TableFixHeaders) parent;
		}

		Date date = null;
		if (mFixHeaders != null) {
			int firstRow = mFixHeaders.getFirstRow() - 1;
			if (firstRow == -1) {
				firstRow = 0;
			}
			date = dates.get(firstRow);
			if (date != null) {
				tvMounth.setText(DateUtils.getMounth(date) + "月");
			}

			Log.i("table", "first row = " + mFixHeaders.getFirstRow());
		}

		int type = getItemViewType(row, column);
		if (convertView == null) {
			switch (type) {
			case DATE:
				convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_date, null);
				ViewHolderDate dateHolder = new ViewHolderDate();
				dateHolder.tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
				dateHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				dateHolder.tvToday = (TextView) convertView.findViewById(R.id.tv_today);
				dateHolder.tvDay = (TextView) convertView.findViewById(R.id.tv_day);
				holder = dateHolder;
				convertView.setTag(dateHolder);
				break;
			case BODY:
				convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_body, null);
				ViewHolderBody bodyHolder = new ViewHolderBody();
				bodyHolder.imgAdd = (ImageView) convertView.findViewById(R.id.img_add);
				bodyHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_schedule);
				bodyHolder.line = convertView.findViewById(R.id.line);
				holder = bodyHolder;
				convertView.setTag(bodyHolder);
				break;
			default:
				convertView = new View(context);
				holder = new ViewHolder();
				convertView.setTag(holder);
				break;
			}
			holder.contentView = convertView;
			convertView.setOnClickListener(mOnClickListener);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bindData(row, column, holder, type);
		return convertView;
	}

	// 绑定数据
	private void bindData(int row, int column, ViewHolder holder, int type) {
		switch (type) {
		case BODY:
			ViewHolderBody bodyHolder = (ViewHolderBody) holder;
			bindBody(row, column, bodyHolder);
			break;
		case DATE:
			ViewHolderDate dateHolder = (ViewHolderDate) holder;
			bindDate(row, column, dateHolder);
		default:
			break;
		}
	}

	private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	java.text.DecimalFormat df = new java.text.DecimalFormat("00");

	// 绑定日期
	private void bindDate(int row, int column, ViewHolderDate dateHolder) {
		if (row > 0 && row < dates.size()) {
			Date date = dates.get(row - 1);
			dateHolder.tvDate.setText(DateUtils.getMounth(date) + "月");
			dateHolder.tvDay.setText(DateUtils.getDay(date));
			dateHolder.tvWeek.setText(DateUtils.getWeekDay(date));
			setBackground(DATE, date, dateHolder);
			if (DateUtils.getDateString(date, mSdf).equals(DateUtils.getDateString(new Date(), mSdf))) {
				dateHolder.tvToday.setVisibility(View.VISIBLE);
			} else {
				dateHolder.tvToday.setVisibility(View.GONE);
			}

			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int today = c.get(Calendar.DAY_OF_MONTH);
			if (today == 1) {
				dateHolder.tvDate.setVisibility(View.VISIBLE);
			} else {
				dateHolder.tvDate.setVisibility(View.GONE);
			}
		}
	}

	// 绑定数据
	private void bindBody(int row, int column, ViewHolderBody bodyHolder) {
		if (row > 0 && column > 0) {
			Date date = dates.get(row - 1);
			Employee employee = employees.get(column - 1);

			bodyHolder.imgAdd.setVisibility(View.VISIBLE);
			bodyHolder.tvTime.setVisibility(View.VISIBLE);

			List<Schedule> schedules = mScheduleDao.getSchedules(date, employee.getEmployeeID());
			setBackground(BODY, date, bodyHolder);

			if (schedules == null || schedules.size() == 0) {
				bodyHolder.tvTime.setVisibility(View.GONE);
				if (date.before(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000))) {
					bodyHolder.imgAdd.setVisibility(View.GONE);
				} else {
					bodyHolder.imgAdd.setVisibility(View.VISIBLE);
				}
				return;
			}

			boolean isVacation = true;
			for (int j = 0; j < schedules.size(); j++) {
				Schedule s = schedules.get(j);
				if (s.getStatus() == 3) {// 休假
					continue;
				} else {
					isVacation = false;
				}
			}

			if (isVacation) {
				bodyHolder.tvTime.setText("休息");
			} else {
				bodyHolder.tvTime.setText(ScheduleHelper.mergeSchedulesToStr(schedules));
			}
			bodyHolder.imgAdd.setVisibility(View.GONE);
		}
	}

	private void setBackground(int type, Date date, ViewHolder holder) {
		Date today = new Date();
		if (DateUtils.getDateString(today, mSdf).equals(DateUtils.getDateString(date, mSdf))) {
			holder.contentView.setBackgroundResource(R.drawable.box11);
			holder.contentView.setClickable(false);
			if (holder instanceof ViewHolderBody) {
				((ViewHolderBody) holder).line.setVisibility(View.GONE);
			}

		} else if (date.before(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000))) {
			holder.contentView.setBackgroundResource(R.color.shedule_table_passed_item_bg);
			holder.contentView.setClickable(false);
			if (DateUtils.getDateString(new Date(today.getTime() - 1 * 24 * 60 * 60 * 1000), mSdf).equals(DateUtils.getDateString(date, mSdf))) {
				if (holder instanceof ViewHolderBody) {
					((ViewHolderBody) holder).line.setVisibility(View.GONE);
				}
			} else if (holder instanceof ViewHolderBody) {
				((ViewHolderBody) holder).line.setVisibility(View.VISIBLE);
			}
		} else {
			holder.contentView.setBackgroundResource(R.color.white);
			holder.contentView.setClickable(true);
			if (holder instanceof ViewHolderBody) {
				((ViewHolderBody) holder).line.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public int getWidth(int column) {
		if (column == -1) {
			return Tools.dip2px(context, 102);
		}

		if (column > 0) {
			return Tools.dip2px(context, 133);
		}

		return 0;
	}

	@Override
	public int getHeight(int row) {
		if (row == -1) {
			return 0;
		}

		if (row > 0) {
			return Tools.dip2px(context, 90);
		}

		return 0;
	}

	@Override
	public int getItemViewType(int row, int column) {
		if (row == -1) {
			return ROW;
		}

		if (column == -1) {
			return DATE;
		}

		if (row > 0 && column > 0) {
			return BODY;
		}

		return ROW;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}
}
