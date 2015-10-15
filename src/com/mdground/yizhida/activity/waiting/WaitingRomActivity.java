package com.mdground.yizhida.activity.waiting;

import java.util.ArrayList;
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
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.RadioView;
import com.mdground.yizhida.view.RadioView.SelectListener;

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

/**
 * 这是护士端看到的医生候诊的页面
 * 
 * @author Administrator
 * 
 */
public class WaitingRomActivity extends BaseActivity implements WaitingRomView, OnClickListener, SelectListener,
		OnRefreshListener<SwipeMenuListView>, OnItemClickListener, OnMenuItemClickListener {

	private static final int ACTION_REAPPOINT = 1;
	private static final int ACTION_ASSIGN = 2;

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

	private WaitingRomPresenter presenter;

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
		mPopuView = LayoutInflater.from(this).inflate(R.layout.popoupwindow_layout, null);
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
		TvdoctorName.setText(
				Tools.getFormat(WaitingRomActivity.this, R.string.doctor_waiting_title, currentDoctor.getDoctorName()));
	}

	private SwipeMenuCreator initSwipeMenuCreator() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				if (menu.getMenuItems().size() == 0) {
					int type = menu.getViewType();
					SwipeMenuItem optItem = null;// 操作按钮
					switch (type) {
					case AppointmentAdapter.PASSED:
						optItem = new SwipeMenuItem(WaitingRomActivity.this.getApplicationContext());
						optItem.setTitle("重新排队");
						optItem.setWidth(Tools.dip2px(WaitingRomActivity.this, 120));
						optItem.setAction(ACTION_REAPPOINT);
						optItem.setVisable(true);
						break;
					case AppointmentAdapter.WATTING:
						optItem = new SwipeMenuItem(WaitingRomActivity.this.getApplicationContext());
						optItem.setTitle("分配");
						optItem.setWidth(Tools.dip2px(WaitingRomActivity.this, 120));
						optItem.setAction(ACTION_ASSIGN);
						optItem.setVisable(true);
						break;
					default:
						break;
					}

					if (optItem != null) {
						optItem.setBackground(R.color.bg_item_opt);
						optItem.setTitleSize(18);
						optItem.setTitleColor(Color.WHITE);
						menu.addMenuItem(optItem);
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

		presenter = new WaitingRomPresenterImpl(this);
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
				TvdoctorName.setText(Tools.getFormat(WaitingRomActivity.this, R.string.doctor_waiting_title,
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
			nextAppiontment(appointmentInfo.getOPID());
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

		Intent intent = new Intent();
		intent.setClass(this, PatientAppointmentActivity.class);
		intent.putExtra(MemberConstant.APPOINTMENT, nextAppiontment);

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
		AppointmentInfo appointment = appointments.get(position - 1);
		if (appointment.getType() == AppointmentInfo.GROUP) {
			return;
		}
		Intent intent = new Intent(WaitingRomActivity.this, PatientAppointmentActivity.class);
		appointment.setOPEMR(currentDoctor.getEMRType());
		intent.putParcelableArrayListExtra(MemberConstant.APPOINTMENT_LIST, appointments);
		intent.putExtra(MemberConstant.APPOINTMENT_LIST_INDEX, position - 1);
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
		case ACTION_ASSIGN:
			// 候诊状态，显示分配
			Intent intent = new Intent(WaitingRomActivity.this, DoctorSelectListActivity.class);
			intent.putExtra(MemberConstant.APPOINTMENT, appointment);
			startActivityForResult(intent, MemberConstant.APPIONTMENT_ASSIGN_REQUEST_CODE);
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