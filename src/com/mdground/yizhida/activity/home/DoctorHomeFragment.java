package com.mdground.yizhida.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.extras.PullToRefreshSwipeMenuListView;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenu;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuCreator;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuItem;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.extras.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.appointment.PatientAppointmentActivity;
import com.mdground.yizhida.activity.base.BaseFragment;
import com.mdground.yizhida.activity.searchpatient.SearchPatientActivity;
import com.mdground.yizhida.adapter.AppointmentAdapter;
import com.mdground.yizhida.adapter.AppointmentAdapter2;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.RadioView;
import com.mdground.yizhida.view.RadioView.SelectListener;

public class DoctorHomeFragment extends BaseFragment implements SelectListener, OnClickListener, DoctorHomeView,
		OnRefreshListener<SwipeMenuListView>, OnMenuItemClickListener, OnItemClickListener {

	private static final int ACTION_CALL = 1;
	private static final int ACTION_FINISH = 2;
	private static final int ACTION_PASSED = 3;

	private View mainView;

	private ImageView IvMainSearch;
	private ImageView IvOpenLeft;
	private RadioView radioView;
	private RelativeLayout emptyPromptLayout;
	private TextView TvNoBody;
	private PullToRefreshSwipeMenuListView pullToRefreshSwipeMenuListView;
	private ArrayList<AppointmentInfo> appointments = new ArrayList<AppointmentInfo>();

	private AppointmentAdapter2 mAdapter;

	private int doctorId = 0;

	private DoctorHomePresenter presenter;
	private Employee employee;

	List<Integer> openItemIndex = new ArrayList<Integer>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_doctor_waiting_room, null);
		findView();
		initData();
		initView();
		setListener();
		return mainView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void findView() {
		IvMainSearch = (ImageView) mainView.findViewById(R.id.main_search);
		IvOpenLeft = (ImageView) mainView.findViewById(R.id.open_left);
		radioView = (RadioView) mainView.findViewById(R.id.radio_group);
		emptyPromptLayout = (RelativeLayout) mainView.findViewById(R.id.empty_prompt);
		TvNoBody = (TextView) mainView.findViewById(R.id.no_body_text);
		TvNoBody.setText(R.string.query_ing);
		pullToRefreshSwipeMenuListView = (PullToRefreshSwipeMenuListView) mainView.findViewById(R.id.pull_swipeListView);
	}

	private void initView() {
		pullToRefreshSwipeMenuListView.setAdapter(mAdapter);
		SwipeMenuCreator creator = initSwipeMenuCreator();
		pullToRefreshSwipeMenuListView.getRefreshableView().setMenuCreator(creator);
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
					case AppointmentAdapter.DIAGNOSING:
						optItem = new SwipeMenuItem(getActivity().getApplicationContext());
						optItem.setTitle("结束");
						optItem.setAction(ACTION_FINISH);
						optItem.setVisable(true);
						break;
					case AppointmentAdapter.WATTING:
						optItem = new SwipeMenuItem(getActivity().getApplicationContext());
						optItem.setTitle("过号");
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
						optItem.setBackground(R.color.bg_item_opt);
						optItem.setWidth(Tools.dip2px(getActivity(), 120));
						optItem.setTitleSize(18);
						optItem.setTitleColor(Color.WHITE);
						menu.addMenuItem(optItem);
					}

				} else {
					SwipeMenuItem item = menu.getMenuItemBayAction(ACTION_CALL);
					if (item == null) {
						return;
					}
					if (presenter.isConnectScreen()) {
						item.setVisable(true);
					} else {
						item.setVisable(false);
					}
				}

			}

		};

		return creator;
	}

	/**
	 * 创建叫号按钮
	 * 
	 * @return
	 */
	private SwipeMenuItem createCallMenuItem() {
		SwipeMenuItem callItem = null;

		callItem = new SwipeMenuItem(getActivity().getApplicationContext());
		callItem.setBackground(R.drawable.box10);
		callItem.setWidth(Tools.dip2px(getActivity(), 120));
		callItem.setTitle("叫号");
		callItem.setTitleSize(18);
		callItem.setAction(ACTION_CALL);
		callItem.setTitleColor(Color.WHITE);
		if (!presenter.isConnectScreen()) {
			callItem.setVisable(false);
		} else {
			callItem.setVisable(true);
		}

		return callItem;
	}

	/** 点击RadioGroup 的radio时选择调用 **/
	@Override
	public void onSelect(int i) {
		appointments.clear();
		switch (i) {
		case RadioView.WATTING:
			// 获取候诊中和已经在就诊中的预约
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

	private void setListener() {
		IvMainSearch.setOnClickListener(this);
		IvOpenLeft.setOnClickListener(this);
		radioView.setSelectListener(this);

		pullToRefreshSwipeMenuListView.setOnRefreshListener(this);
		pullToRefreshSwipeMenuListView.getRefreshableView().setOnMenuItemClickListener(this);
		pullToRefreshSwipeMenuListView.getRefreshableView().setOnItemClickListener(this);
	}

	/** 初始化数据 **/
	private void initData() {
		MedicalAppliction appliction = (MedicalAppliction) getActivity().getApplication();
		employee = appliction.getLoginEmployee();
		if (employee != null) {
			doctorId = employee.getEmployeeID();
		}

		if (presenter == null) {
			presenter = new DoctorHomePresenterImpl(this);
		}
		mAdapter = new AppointmentAdapter2(getActivity(), appointments);
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshAppiontmentList();
	}

	@Override
	public void onResume() {
		super.onResume();
		presenter.addScreenCallBack();
	}

	@Override
	public void onPause() {
		super.onPause();
		presenter.removeScreenCallBack();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		presenter.removeScreenCallBack();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_search:
			Intent intent = new Intent(getActivity(), SearchPatientActivity.class);
			getActivity().startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
			break;
		case R.id.open_left:
			((MainActivity) getActivity()).mainHandler.sendEmptyMessage(MemberConstant.OPEN_LEFT_MENU);
			break;
		}

	}

	/**
	 * 显示下一个
	 */
	public void nextAppiontment(int appiontmentId) {
		if (appiontmentId <= 0) {
			return;
		}
		AppointmentInfo nextAppiontment = null;
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
				if (appointment == null || appointment.getType() == AppointmentInfo.GROUP) {
					continue;
				}
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
			
			if (index == -1) {
				for (int i = 0; i < appointments.size(); i++) {
					AppointmentInfo appointment = appointments.get(i);
					if (appointment == null || appointment.getType() == AppointmentInfo.GROUP) {
						continue;
					}
					nextAppiontment = appointment;
					break;
				}
			} else {
				nextAppiontment = appointments.get(index);
			}
		} else {
			return;
		}
		if (nextAppiontment == null) {
			return;
		}
		appointments.remove(nextAppiontment);
		nextAppiontment.setDoctorName(employee.getEmployeeName());
		Intent intent = new Intent(getActivity(), PatientAppointmentActivity.class);
		intent.putExtra(MemberConstant.APPOINTMENT, nextAppiontment);
		presenter.callPatient(nextAppiontment, employee.getEmployeeName());
		getActivity().startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
	}

	/**
	 * 刷新列表
	 */
	public void refreshAppiontmentList() {
		onSelect(radioView.getCurrentSelect());
	}

	@Override
	public void refresh(int status, final List<AppointmentInfo> appointmentInfos) {
		this.appointments.clear();
		if (appointmentInfos == null || appointmentInfos.size() == 0) {
			pullToRefreshSwipeMenuListView.setEmptyView(emptyPromptLayout);
			TvNoBody.setText(Tools.getFormat(getActivity(), R.string.no_body, radioView.getSelectText()));
			return;
		}
		appointments.addAll(appointmentInfos);

		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void refreshComplete() {
		pullToRefreshSwipeMenuListView.onRefreshComplete();
	}

	@Override
	public void onRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
		onSelect(radioView.getCurrentSelect());
	}

	@Override
	public void updateComplete() {
		onSelect(radioView.getCurrentSelect());
	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		SwipeMenuItem item = menu.getMenuItem(index);
		switch (item.getAction()) {
		case ACTION_CALL:
			presenter.callPatient(appointments.get(position), employee.getEmployeeName());
			break;
		case ACTION_FINISH:
			presenter.updateAppointment(appointments.get(position), AppointmentInfo.STATUS_FINISH);
			break;
		case ACTION_PASSED:
			if (position > appointments.size()) {
				return false;
			}
			final AppointmentInfo appintmentInfo = appointments.get(position);
			// if ((System.currentTimeMillis() -
			// appintmentInfo.getCreateTime().getTime()) < (long) 30 * (long) 60
			// * (long) 1000) {
			// Toast.makeText(getActivity(), appintmentInfo.getTaStr() +
			// "预约才不到半小时,请手下留情", Toast.LENGTH_SHORT).show();
			// return false;
			// }

			final AlertDialog myDialog = new AlertDialog.Builder(getActivity()).setMessage("确定过号？")
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							presenter.updateAppointment(appintmentInfo, AppointmentInfo.STATUS_PASSED);
						}
					}).setNeutralButton("取消", null).create();
			myDialog.show();

			break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 跳转到病人详情页
		AppointmentInfo appointment = appointments.get(position - 1);
		if (appointment.getType() == AppointmentInfo.GROUP) {
			return;
		}
		appointment.setDoctorName(employee.getEmployeeName());
		Intent intent = new Intent(getActivity(), PatientAppointmentActivity.class);
		intent.putParcelableArrayListExtra(MemberConstant.APPOINTMENT_LIST, appointments);
		intent.putExtra(MemberConstant.APPOINTMENT_LIST_INDEX, position - 1);
		getActivity().startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);

	}

	@Override
	public void refreshView() {
		mAdapter.notifyDataSetChanged();
	}

}
