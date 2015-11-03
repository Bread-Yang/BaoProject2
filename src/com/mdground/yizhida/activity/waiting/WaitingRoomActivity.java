package com.mdground.yizhida.activity.waiting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.extras.PullToRefreshSwipeMenuListView;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenu;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuCreator;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuItem;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.appointment.PatientAppointmentActivity;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.searchpatient.SearchPatientActivity;
import com.mdground.yizhida.adapter.AppointmentAdapter;
import com.mdground.yizhida.adapter.AppointmentAdapter2;
import com.mdground.yizhida.adapter.DoctorSimpleAdapter;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.screen.ScreenManager;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.RadioView;
import com.mdground.yizhida.view.RadioView.SelectListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这是护士端看到的医生候诊的页面
 * 
 * @author Administrator
 * 
 */
public class WaitingRoomActivity extends BaseActivity implements WaitingRoomView, OnClickListener, SelectListener,
		OnRefreshListener<SwipeMenuListView>, OnItemClickListener, OnMenuItemClickListener {

	private static final int ACTION_REAPPOINT = 1;
	// private static final int ACTION_ASSIGN = 2;
	private static final int ACTION_CALL = 3;
	private static final int ACTION_PASSED = 4;

	private RadioView radioView;
	private ListView doctorListView;
	private ImageView IvBack;
	private TextView TvdoctorName;
	private PopupWindow mPopupWindow;
	private View mPopuView;
	private ImageView IvSearch;
	private TextView TvNoBody;
	private RelativeLayout emptyPromptLayout;

	private AppointmentAdapter2 appointmentListAdapter;
	private DoctorSimpleAdapter doctorSimapleAdapter;
	private ArrayList<AppointmentInfo> appointments = new ArrayList<AppointmentInfo>();
	private ArrayList<Doctor> doctors = new ArrayList<Doctor>();
	private Doctor currentDoctor;
	private int nCurrentDoctorIndex;

	private PullToRefreshSwipeMenuListView pullToRefreshSwipeListView;

	private WaitingRoomPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_room);
		findView();
		initMemberData();
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		onSelect(radioView.getCurrentSelect());
	}

	@Override
	public void findView() {
		IvBack = (ImageView) this.findViewById(R.id.back);
		IvSearch = (ImageView) findViewById(R.id.search);
		radioView = (RadioView) this.findViewById(R.id.radio_group);
		TvdoctorName = (TextView) this.findViewById(R.id.doctor_room_name);
		mPopuView = LayoutInflater.from(this).inflate(R.layout.layout_popoupwindow, null);
		doctorListView = (ListView) mPopuView.findViewById(R.id.doctor_listview);
		TvNoBody = (TextView) findViewById(R.id.no_body_text);
		TvNoBody.setText(R.string.query_ing);
		emptyPromptLayout = (RelativeLayout) findViewById(R.id.empty_prompt);

		pullToRefreshSwipeListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.pull_swipeListView);
	}

	@Override
	public void initView() {
		pullToRefreshSwipeListView.setAdapter(appointmentListAdapter);
		pullToRefreshSwipeListView.getRefreshableView().setMenuCreator(initSwipeMenuCreator());
		TvdoctorName.setText(Tools.getFormat(WaitingRoomActivity.this, R.string.doctor_waiting_title,
				currentDoctor.getDoctorName()));
	}

	private SwipeMenuCreator initSwipeMenuCreator() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				if (menu.getMenuItems().size() == 0) {
					int type = menu.getViewType();
					SwipeMenuItem optItem = null;// 操作按钮
					SwipeMenuItem callItem = null;// 叫号按钮
					switch (type) {
					case AppointmentAdapter.PASSED:
						optItem = new SwipeMenuItem(WaitingRoomActivity.this.getApplicationContext());
						optItem.setTitle("重新排队");
						optItem.setWidth(Tools.dip2px(WaitingRoomActivity.this, 120));
						optItem.setAction(ACTION_REAPPOINT);
						optItem.setVisable(true);
						break;
					case AppointmentAdapter.WATTING:
						optItem = new SwipeMenuItem(WaitingRoomActivity.this.getApplicationContext());
						optItem.setTitle("过号");
						optItem.setWidth(Tools.dip2px(WaitingRoomActivity.this, 120));
						optItem.setAction(ACTION_PASSED);
						optItem.setVisable(true);

						callItem = createCallMenuItem();
						break;
					default:
						break;
					}

					if (callItem != null) {
						menu.addMenuItem(callItem);
					}

					if (optItem != null) {
						optItem.setBackground(R.drawable.selector_bg_button_black_dent);
						optItem.setTitleSize(18);
						optItem.setTitleColor(Color.WHITE);
						menu.addMenuItem(optItem);
					}

				} else {
					SwipeMenuItem item = menu.getMenuItemBayAction(ACTION_CALL);
					if (item == null) {
						return;
					}
					if (ScreenManager.getInstance().isConnected()) {
						item.setVisable(true);
					} else {
						item.setVisable(false);
					}
				}
			}

		};

		return creator;
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		if (intent != null) {
			// 采用传对象的方式
			currentDoctor = intent.getParcelableExtra(MemberConstant.DOCTOR);
			doctors = intent.getParcelableArrayListExtra(MemberConstant.DOCTOR_LIST);
		}
		appointmentListAdapter = new AppointmentAdapter2(this, appointments);

		presenter = new WaitingRoomPresenterImpl(this);
		initPopuWindow();
	}

	private void initPopuWindow() {
		doctorSimapleAdapter = new DoctorSimpleAdapter(this);
		doctorSimapleAdapter.setDataList(doctors);
		doctorListView.setAdapter(doctorSimapleAdapter);

		for (int i = 0; i < doctors.size(); i++) {
			if (currentDoctor.getDoctorID() == doctors.get(i).getDoctorID()) {
				nCurrentDoctorIndex = i;
				break;
			}
		}

		doctorSimapleAdapter.setSelectItem(nCurrentDoctorIndex);
		doctorListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentDoctor = doctors.get(position);
				nCurrentDoctorIndex = position;
				changeListData(radioView.getCurrentSelect());
				TvdoctorName.setText(Tools.getFormat(WaitingRoomActivity.this, R.string.doctor_waiting_title,
						doctors.get(position).getDoctorName()));
				showPopuWindow();
			}
		});
	}

	private void showPopuWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mPopuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setOutsideTouchable(false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					Drawable closeDrawable = getResources().getDrawable(R.drawable.down);
					closeDrawable.setBounds(0, 0, closeDrawable.getMinimumWidth(), closeDrawable.getMinimumHeight());
					TvdoctorName.setCompoundDrawables(null, null, closeDrawable, null);
				}
			});

		}

		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return;
		} else {
			Drawable openDrawable = getResources().getDrawable(R.drawable.up);
			openDrawable.setBounds(0, 0, openDrawable.getMinimumWidth(), openDrawable.getMinimumHeight());
			TvdoctorName.setCompoundDrawables(null, null, openDrawable, null);
		}
		int width = TvdoctorName.getMeasuredWidth();
		int popuViewWidth = Tools.dip2px(this, 200);
		doctorListView.setSelection(nCurrentDoctorIndex);
		doctorSimapleAdapter.setSelectItem(nCurrentDoctorIndex);
		mPopupWindow.showAsDropDown(TvdoctorName, -(Math.abs((popuViewWidth - width) / 2)), 0);// 30为让popupwind右移一点
	}

	@Override
	public void setListener() {
		TvdoctorName.setOnClickListener(this);
		IvSearch.setOnClickListener(this);
		IvBack.setOnClickListener(this);
		radioView.setSelectListener(this);
		pullToRefreshSwipeListView.getRefreshableView().setOnItemClickListener(this);
		pullToRefreshSwipeListView.getRefreshableView().setOnMenuItemClickListener(this);
		pullToRefreshSwipeListView.setOnRefreshListener(this);
	}

	/**
	 * 创建叫号按钮
	 * 
	 * @return
	 */
	private SwipeMenuItem createCallMenuItem() {
		SwipeMenuItem callItem = null;

		callItem = new SwipeMenuItem(getApplicationContext());
		callItem.setBackground(R.drawable.selector_bg_button_blue_dent);
		callItem.setWidth(Tools.dip2px(this, 120));
		callItem.setTitle("叫号");
		callItem.setTitleSize(18);
		callItem.setAction(ACTION_CALL);
		callItem.setTitleColor(Color.WHITE);
		if (!ScreenManager.getInstance().isConnected()) {
			callItem.setVisable(false);
		} else {
			callItem.setVisable(true);
		}

		return callItem;
	}

	@Override
	public void onSelect(int i) {
		switch (i) {
		case RadioView.WATTING:
			changeListData(RadioView.WATTING);
			break;
		case RadioView.PASSED:
			changeListData(RadioView.PASSED);
			break;
		case RadioView.AREADY_DIAGNOSIS:
			changeListData(RadioView.AREADY_DIAGNOSIS);
			break;
		}

	}

	public void changeListData(int type) {
		appointments.clear();
		int doctorId = currentDoctor.getDoctorID();
		switch (type) {
		case RadioView.WATTING:
			presenter.getAppointmentInfoListByDoctor(AppointmentInfo.STATUS_WATTING, doctorId);
			break;
		case RadioView.PASSED:
			presenter.getAppointmentInfoListByDoctor(AppointmentInfo.STATUS_PASSED, doctorId);
			break;
		case RadioView.AREADY_DIAGNOSIS:
			presenter.getAppointmentInfoListByDoctor(AppointmentInfo.STATUS_FINISH, doctorId);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			Intent intent = new Intent(this, SearchPatientActivity.class);
			startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
			break;
		case R.id.back:
			onBackPressed();
			break;
		case R.id.doctor_room_name:
			showPopuWindow();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE
				&& resultCode == MemberConstant.APPIONTMENT_RESOULT_CODE) {
			setResult(resultCode, data);
			finish();
		} else if (requestCode == MemberConstant.APPIONTMENT_ASSIGN_REQUEST_CODE
				&& resultCode == MemberConstant.APPIONTMENT_RESOULT_ASSIGN) {
			if (appointments.size() == 1) {
				finish();
			}
		} else if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE
				&& resultCode == MemberConstant.APPIONTMENT_NEXT_RESOULT_CODE) {
			AppointmentInfo appointmentInfo = data.getParcelableExtra(MemberConstant.APPOINTMENT);
			nextAppiontment(appointmentInfo.getOPID(),
					data.getBooleanExtra(MemberConstant.APPOINTMENT_CALL_NEXT, false));
		}
	}

	/**
	 * 显示下一个
	 */
	public void nextAppiontment(int appiontmentId, boolean isCallNext) {
		if (appiontmentId <= 0) {
			return;
		}
		AppointmentInfo nextAppointment = null;
		// 解决第一次重复显示同一个预约
		for (int i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getOPID() == appiontmentId) {
				appointments.remove(appointments.get(i));
				break;
			}
		}

		int offset = -1;
		int index = -1;
		int tmpOffset = -1;
		if (appointments.size() > 0) {
			for (int i = 0; i < appointments.size(); i++) {
				AppointmentInfo appointment = appointments.get(i);
				if (appointment == null || appointment.getType() == AppointmentInfo.GROUP
						|| appointment.getType() == AppointmentInfo.EMPTY
						|| DateUtils.compareToCurrentPeriod(appointment.getOPDatePeriod()) == 1) {
					continue;
				}
				if (appointment.isEmergency()) {   // 如果是急诊,立刻返回
					index = i;
					break;
				} else {
					tmpOffset = appointment.getOPID() - appiontmentId;
					if (offset == -1 && tmpOffset > 0) {
						offset = tmpOffset;
						index = i;
					} else {
						if (tmpOffset > 0 && tmpOffset < offset) {
							offset = tmpOffset;
							index = i;
						}
					}
				}
			}

			if (index == -1) {
				for (int i = 0; i < appointments.size(); i++) {
					AppointmentInfo appointment = appointments.get(i);
					if (appointment == null || appointment.getType() == AppointmentInfo.GROUP
							|| appointment.getType() == AppointmentInfo.EMPTY
							|| DateUtils.compareToCurrentPeriod(appointment.getOPDatePeriod()) == 1) {
						continue;
					}
					nextAppointment = appointment;
					index = i;
					break;
				}
			} else {
				nextAppointment = appointments.get(index);
			}
		} else {
			return;
		}
		if (nextAppointment == null) {
			return;
		}
		
		ArrayList<AppointmentInfo> tempList = new ArrayList<AppointmentInfo>();
		
		int tempIndex = index;
		for (int i = 0; i < appointments.size(); i++) {
			AppointmentInfo item = appointments.get(i);
			if (item.getType() == AppointmentInfo.GROUP || item.getType() == AppointmentInfo.EMPTY) {
				if (index > i) {
					L.e(this, "position--");
					tempIndex--;
				}
				continue;
			}
			tempList.add(item);
		}
		
		// appointments.remove(nextAppiontment);
		appointments.get(index).setDoctorName(currentDoctor.getEmployeeName());
		nextAppointment.setDoctorName(currentDoctor.getEmployeeName());
		Intent intent = new Intent(this, PatientAppointmentActivity.class);
		intent.putExtra(MemberConstant.APPOINTMENT, nextAppointment);
		intent.putParcelableArrayListExtra(MemberConstant.APPOINTMENT_LIST, tempList);
		intent.putExtra(MemberConstant.APPOINTMENT_LIST_INDEX, tempIndex);
		
		if (isCallNext) {
			presenter.callPatient(nextAppointment, currentDoctor.getEmployeeName());
		}
		
		startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
	}

	@Override
	public void onRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
		changeListData(radioView.getCurrentSelect());
	}

	@Override
	public void refresh(int status, List<AppointmentInfo> appointmentInfos) {
		this.appointments.clear();
		if (appointmentInfos == null || appointmentInfos.size() == 0) {
			this.pullToRefreshSwipeListView.setEmptyView(emptyPromptLayout);
			TvNoBody.setText(Tools.getFormat(this, R.string.no_body, radioView.getSelectText()));
			return;
		}
		for (AppointmentInfo item : appointmentInfos) {
			item.setDoctorName(currentDoctor.getEmployeeName());
		}
		this.appointments.addAll(appointmentInfos);

		appointmentListAdapter.notifyDataSetChanged();
	}

	@Override
	public void refreshComplete() {
		pullToRefreshSwipeListView.onRefreshComplete();
		appointmentListAdapter.notifyDataSetChanged();
	}

	@Override
	public void updateStatusComplete() {
		changeListData(radioView.getCurrentSelect());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 跳转到病人详情页
		position -= 1;
		AppointmentInfo appointment = appointments.get(position);
		if (appointment.getType() == AppointmentInfo.GROUP || appointment.getType() == appointment.EMPTY) {
			return;
		}

		ArrayList<AppointmentInfo> tempList = new ArrayList<AppointmentInfo>();

		int index = position;
		for (int i = 0; i < appointments.size(); i++) {
			AppointmentInfo item = appointments.get(i);
			if (item.getType() == AppointmentInfo.GROUP || item.getType() == AppointmentInfo.EMPTY) {
				if (position > i) {
					L.e(this, "position--");
					index--;
				}
				continue;
			}
			tempList.add(item);
		}

		tempList.get(index).setDoctorName(currentDoctor.getEmployeeName());

		Intent intent = new Intent(WaitingRoomActivity.this, PatientAppointmentActivity.class);
		appointment.setOPEMR(currentDoctor.getEMRType());
		intent.putParcelableArrayListExtra(MemberConstant.APPOINTMENT_LIST, tempList);
		intent.putExtra(MemberConstant.APPOINTMENT_LIST_INDEX, index);
		startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		SwipeMenuItem item = menu.getMenuItem(index);
		AppointmentInfo appointment = appointments.get(position);
		if (appointment.getType() == AppointmentInfo.GROUP) {
			return true;
		}
		switch (item.getAction()) {
		case ACTION_CALL:
			presenter.callPatient(appointments.get(position), currentDoctor.getEmployeeName());
			break;
		case ACTION_PASSED:
			// 候诊状态，显示分配
			// Intent intent = new Intent(WaitingRoomActivity.this,
			// DoctorSelectListActivity.class);
			// intent.putExtra(MemberConstant.APPOINTMENT, appointment);
			// startActivityForResult(intent,
			// MemberConstant.APPIONTMENT_ASSIGN_REQUEST_CODE);

			if (position > appointments.size()) {
				return false;
			}
			final AppointmentInfo appointmentInfo = appointments.get(position);
			// if ((System.currentTimeMillis() -
			// appintmentInfo.getCreateTime().getTime()) < (long) 30 * (long) 60
			// * (long) 1000) {
			// Toast.makeText(getActivity(), appintmentInfo.getTaStr() +
			// "预约才不到半小时,请手下留情", Toast.LENGTH_SHORT).show();
			// return false;
			// }

			Calendar c = Calendar.getInstance();
			int currentHour = c.get(Calendar.HOUR_OF_DAY); // 当前时间

			currentHour = currentHour / 2 + 1;

			if (appointmentInfo.getOPDatePeriod() > currentHour) {
				Toast.makeText(this, R.string.current_time_no_appointment_time, Toast.LENGTH_SHORT).show();
				return false;
			}

			final AlertDialog myDialog = new AlertDialog.Builder(this).setMessage("确定过号？")
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							presenter.updateAppointment(appointmentInfo, AppointmentInfo.STATUS_PASSED);
						}
					}).setNeutralButton("取消", null).create();
			myDialog.show();

			break;
		case ACTION_REAPPOINT:
			// 重新排队
			appointment.setOPEMR(currentDoctor.getEMRType());
			presenter.updateAppointment(appointment, AppointmentInfo.STATUS_REAPPOINT);
			break;
		}
		return false;
	}

}
