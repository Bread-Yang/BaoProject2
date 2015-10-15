package com.mdground.yizhida.activity.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.login.LoginActivity;
import com.mdground.yizhida.api.bean.DoctorAppointmentCount;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.dialog.AppointmentDialog;
import com.mdground.yizhida.util.AppManager;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.util.PreferenceUtils;
import com.mdground.yizhida.view.DragLayout;
import com.mdground.yizhida.view.DragLayout.DragListener;
import com.tencent.android.tpush.XGPushConfig;

public class MainActivity extends FragmentActivity implements OnClickListener, MainView, DragListener {

	private Fragment tabWaiting;
	private Fragment tabPersonCenter;

	// private RelativeLayout containerLayout;
	private RelativeLayout RlWaitingRoom;
	private RelativeLayout RlPersonCenter;

	private ImageView IvWaitingRoom;
	private TextView TvWaitingRoom;

	private ImageView IvPersonCenter;
	private TextView TvPersonCenter;

	private Fragment currentFragment;
	private DragLayout dragLayout;

	// private MainAdapter mMainAdapter;
	// private Fragment waitingRoomFragment;

	// private Fragment personCenterFragment;

	private Resources resources;

	private TextView tvWaittingCount;
	private TextView tvPassedCount;
	private TextView tvFinishedCount;
	private TextView tvTotalCount;

	private TextView tvWeek;
	private TextView tvDate;

	private AppointmentDialog appointmentDialog;

	/** 角色 **/
	private int role;
	private Employee loginEmployee;

	private int nWaitingBtnfocusRes = 0;
	private int nWaitingBtnUnfocusRes = 0;

	private MainPresenter presenter;

	FragmentManager fragmentManager;

	/** 消息处理 **/
	protected Handler mainHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.what) {
			case MemberConstant.OPEN_LEFT_MENU:
				dragLayout.open();
				break;
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 设置所有activity为竖直
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		findView();
		initMemberData();
		initView();
		setListener();
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	private void findView() {
		RlWaitingRoom = (RelativeLayout) this.findViewById(R.id.wait_button);
		RlPersonCenter = (RelativeLayout) this.findViewById(R.id.person_button);
		// containerLayout = (RelativeLayout)
		// this.findViewById(R.id.layout_content);
		dragLayout = (DragLayout) this.findViewById(R.id.drag_layout);

		IvWaitingRoom = (ImageView) this.findViewById(R.id.waiting_room_img);
		TvWaitingRoom = (TextView) this.findViewById(R.id.waiting_room_txt);
		IvPersonCenter = (ImageView) this.findViewById(R.id.person_center_img);
		TvPersonCenter = (TextView) this.findViewById(R.id.person_center_txt);

		tvWaittingCount = (TextView) findViewById(R.id.waiting_count);
		tvPassedCount = (TextView) findViewById(R.id.passed_count);
		tvFinishedCount = (TextView) findViewById(R.id.finished_count);
		tvTotalCount = (TextView) findViewById(R.id.total_count);
		tvWeek = (TextView) findViewById(R.id.week);
		tvDate = (TextView) findViewById(R.id.date);
	}

	private void initView() {
		if ((role & Employee.NURSE) == Employee.NURSE) {
			// 禁用向右拖动打开
			dragLayout.setCanOpen(false);
		} else {
			dragLayout.setCanOpen(true);
		}
		resources = this.getResources();

		initViewByRole(role);
		switchWaitingTab();
		// 设置日期
		Date date = new Date();
		tvWeek.setText(DateUtils.getWeekDay(date));
		tvDate.setText(DateUtils.getMouthDay(date));
	}

	// 初始化资源
	private void initViewByRole(int role) {
		if ((role & Employee.NURSE) == Employee.NURSE) {
			tabWaiting = new NurseHomeFragment();
			nWaitingBtnfocusRes = R.drawable.home_sel;
			nWaitingBtnUnfocusRes = R.drawable.home_nor;
			TvWaitingRoom.setText(R.string.nurse_waiting_string);
		} else {
			tabWaiting = new DoctorHomeFragment();
			nWaitingBtnfocusRes = R.drawable.waiting_room_sel;
			nWaitingBtnUnfocusRes = R.drawable.nopeople;
			TvWaitingRoom.setText(R.string.doctor_waiting_string);
		}

		tabPersonCenter = new PersonCenterFragment();
	}

	private void switchWaitingTab() {
		if (tabWaiting == null) {
			return;
		}

		IvWaitingRoom.setImageResource(nWaitingBtnfocusRes);
		TvWaitingRoom.setTextColor(resources.getColor(R.color.mysel2_text));

		IvPersonCenter.setImageResource(R.drawable.me_nor);
		TvPersonCenter.setTextColor(resources.getColor(R.color.mynor_text));

		switchContent(currentFragment, tabWaiting);
		currentFragment = tabWaiting;

		PreferenceUtils.setPrefInt(this, MemberConstant.SHOW_FRAGMENT_PAGE, R.id.wait_button);
	}

	private void switchPersonTab() {
		if (tabPersonCenter == null) {
			return;
		}

		IvPersonCenter.setImageResource(R.drawable.me_sel);
		TvPersonCenter.setTextColor(resources.getColor(R.color.mysel2_text));

		IvWaitingRoom.setImageResource(nWaitingBtnUnfocusRes);
		TvWaitingRoom.setTextColor(resources.getColor(R.color.mynor_text));

		switchContent(currentFragment, tabPersonCenter);
		currentFragment = tabPersonCenter;
		PreferenceUtils.setPrefInt(this, MemberConstant.SHOW_FRAGMENT_PAGE, R.id.person_button);
	}

	public void switchContent(Fragment from, Fragment to) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (to == null) {
			return;
		}

		if (from == null || !from.isAdded()) {
			transaction.add(R.id.layout_content, to).commit();
			return;
		}

		if (!to.isAdded()) { // 先判断是否被add过
			transaction.hide(from).add(R.id.layout_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
		}
	}

	private void initMemberData() {
		Intent intent = getIntent();
		role = intent.getIntExtra(MemberConstant.EMPLOYEE_ROLE, 0);
		presenter = new MainPresenterImpl(this);

		presenter.getChiefComplaintTemplateList();
		presenter.getEmployeeList();
		presenter.updateDeviceToken(XGPushConfig.getToken(getApplicationContext()));
		appointmentDialog = new AppointmentDialog(this);
		fragmentManager = getSupportFragmentManager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wait_button: {
			switchWaitingTab();
			break;
		}
		case R.id.person_button: {
			switchPersonTab();
			break;
		}

		}
	}

	private void setListener() {
		RlWaitingRoom.setOnClickListener(this);
		RlPersonCenter.setOnClickListener(this);
		dragLayout.setDragListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE && data != null) {
			AppointmentInfo appointmentInfo = data.getParcelableExtra(MemberConstant.APPOINTMENT);
			if (appointmentInfo == null) {
				return;
			}
			switch (resultCode) {
			case MemberConstant.APPIONTMENT_NEXT_RESOULT_CODE:
				if ((tabWaiting instanceof DoctorHomeFragment) && data != null) {
					DoctorHomeFragment df = (DoctorHomeFragment) tabWaiting;
					df.nextAppiontment(appointmentInfo.getOPID());
				}
				break;
			case MemberConstant.APPIONTMENT_REQUEST_CODE:// 医生端只提示一个，预约成功,护士端提示挂号成功提示框
				if ((role & Employee.DOCTOR) == Employee.DOCTOR) {
					// Toast.makeText(this, "挂号成功：" +
					// appointmentInfo.getAppointmentNo(),
					// Toast.LENGTH_SHORT).show();
				} else {
					appointmentDialog.show(appointmentInfo);
				}
				break;
			case MemberConstant.APPIONTMENT_RESOULT_ASSIGN:
				Toast.makeText(this, "分配成功：" + appointmentInfo.getOPNo(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		} else if (requestCode == MemberConstant.PASSWORD_REQUEST_CODE && resultCode == RESULT_OK) {
			// 修改密码成功
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			// 清空登陆信息
			((MedicalAppliction) getApplication()).setLoginEmployee(null);
			// MedicalHttpAPI.getInstace().emplyee = null;
			startActivity(intent);
			finish();
			PreferenceUtils.setPrefString(MainActivity.this, MemberConstant.PASSWORD, "");
			PreferenceUtils.setPrefInt(MainActivity.this, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_OUT);
		}
	}

	@Override
	public void onOpen() {
		Doctor doctor = new Doctor();
		MedicalAppliction appliction = (MedicalAppliction) getApplication();
		loginEmployee = appliction.getLoginEmployee();
		doctor.setDoctorID(loginEmployee.getEmployeeID());
		List<Doctor> doctors = new ArrayList<Doctor>();
		doctors.add(doctor);
		presenter.getAppointmentCountForDoctor(doctors);
	}

	@Override
	public void onClose() {

	}

	@Override
	public void onDrag(float percent) {

	}

	@Override
	public void setAppointmentCount(DoctorAppointmentCount count) {
		// TODO Auto-generated method stub
		// 更新到界面上
		tvFinishedCount.setText(String.valueOf(count.getFinishedCount()));
		tvPassedCount.setText(String.valueOf(count.getMissedCount()));
		tvWaittingCount.setText(String.valueOf(count.getWaitingCount()));
		tvTotalCount.setText(String.valueOf(count.getTotalCount()));
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
