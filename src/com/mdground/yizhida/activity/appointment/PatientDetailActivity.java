package com.mdground.yizhida.activity.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.ImageActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.patientinfo.PatientCommonActivity;
import com.mdground.yizhida.activity.patientinfo.PatientEditActivity;
import com.mdground.yizhida.adapter.PatientInfoPageAdapter;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.SymptomDao;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.view.CircleImageView;
import com.mdground.yizhida.view.PatientBasicLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 病人预约详情界面
 * 
 * @author Administrator
 * 
 */
public class PatientDetailActivity extends PatientCommonActivity
		implements OnClickListener, PatientDetailView {

	private static final int OPT_PASS = 1;
	private static final int OPT_REQUEUE = 2;
	private static final int OPT_ASSIGN = 3;
	private static final int OPT_STOP = 4;
	private static final int OPT_START = 5;

	private PatientBasicLayout patientBasicLayout;
	private ArrayList<View> viewContainter;
	private PatientInfoPageAdapter mPatientInfoPageAdapter;
	private ImageView mBackImg;
	private RelativeLayout rlt_title;
	private TextView tvTitle;
	private TextView tvOpterator;
	private ListView anamnesisListView;
	private Employee loginEmployee;
	private ArrayList<AppointmentInfo> appointmentList;
	private int appointment_index;
	private AppointmentInfo appointmentInfo;
	private Patient mPatient;

	private CircleImageView circleHeadImage;

	private PatientDetailPresenter presenter;

	// 预约状态
	private int opStatus;
	private int role;

	private Button btnOpt;
	private Button btnCall, btn_patient_detail;
	private RelativeLayout layoutButtons;

	private Dialog dialog_patient_detail;

	// dao
	SymptomDao mSymptomDao;

	// 控制屏幕是否常亮
	PowerManager.WakeLock wl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMemberData();
		setScreenOn();
		setContentView(R.layout.activity_patient_detail);
		findView();
		initView();
		setListener();
	}

	@Override
	public void findView() {
		super.findView();
		rlt_title = (RelativeLayout) findViewById(R.id.rlt_title);
		tvTitle = (TextView) findViewById(R.id.title);
		tvOpterator = (TextView) findViewById(R.id.opterator);
		circleHeadImage = (CircleImageView) findViewById(R.id.headimg);

		// 操作按钮
		btnOpt = (Button) findViewById(R.id.btn_opt);
		btnCall = (Button) findViewById(R.id.btn_call);
		btn_patient_detail = (Button) findViewById(R.id.btn_patient_detail);
		layoutButtons = (RelativeLayout) findViewById(R.id.layout_opt_buttons);

	}

	@Override
	public void initView() {
		super.initView();
		viewContainter = new ArrayList<View>();
		patientBasicLayout = new PatientBasicLayout(this);
		// 初始化dialog及dialog里面的控件
		dialog_patient_detail = new Dialog(this, R.style.patient_detail_dialog);
		dialog_patient_detail.setContentView(patientBasicLayout);

		patientBasicLayout.findViewById(R.id.iv_edit).setOnClickListener(this);

		// 设置dialog弹入弹出的动画
		Window window = dialog_patient_detail.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
		window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
		window.setAttributes(wlp);

		mBackImg = (ImageView) findViewById(R.id.back);

		anamnesisListView = (ListView) getLayoutInflater().inflate(R.layout.viewpager_item_anamnesis, null);

		viewContainter.add(anamnesisListView);
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_allergic_history, null));
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_medication_compliance, null));
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_other_information, null));
		mPatientInfoPageAdapter = new PatientInfoPageAdapter(viewContainter);
		viewpager.setAdapter(mPatientInfoPageAdapter);

		updateInterface();
		
		if (appointmentList.size() == 1) {
			btn_left_arrow.setVisibility(View.INVISIBLE);
			btn_right_arrow.setVisibility(View.INVISIBLE);
		} else {
			if (appointment_index == 0) {
				btn_left_arrow.setVisibility(View.INVISIBLE);
			} else if (appointment_index == appointmentList.size() - 1) {
				btn_right_arrow.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private void updateInterface() {
		initTitleBar();
		initTitleOpt();
		if ((role & Employee.DOCTOR) != 0) {
			initOptButtons();
		}
	}

	private void initTitleBar() {
		if (opStatus == 0) {
			// titleLayout.setBackgroundResource(R.drawable.top_bg1);
		} else if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			rlt_title.setBackgroundResource(R.drawable.top_bg2);
			tvTitle.setText("就诊中");
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_green_bg);
			btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_green);
			btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_green);
		} else if ((opStatus & AppointmentInfo.STATUS_WATTING) != 0) {
			rlt_title.setBackgroundResource(R.drawable.top_bg1);
			tvTitle.setText("候诊中");
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_blue_bg);
			btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_blue);
			btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_blue);
		} else if ((opStatus & AppointmentInfo.STATUS_PASSED) != 0) {
			tvTitle.setText("已过号");
			rlt_title.setBackgroundResource(R.drawable.top_bg3);
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_black_bg);
			btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_black);
			btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_black);
		} else if ((opStatus & AppointmentInfo.STATUS_FINISH) != 0) {
			rlt_title.setBackgroundResource(R.drawable.top_bg3);
			tvTitle.setText("已就诊");
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_black_bg);
			btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_black);
			btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_black);
		}
	}

	// 初始化
	private void initTitleOpt() {
		if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			if ((role & Employee.DOCTOR) != 0) {
				tvOpterator.setText("过号");
				tvOpterator.setVisibility(View.VISIBLE);
				tvOpterator.setTag(OPT_PASS);
			} else {
				tvOpterator.setVisibility(View.GONE);
			}
		} else if ((opStatus & AppointmentInfo.STATUS_WATTING) != 0) {
			if ((role & Employee.DOCTOR) != 0) {
				tvOpterator.setText("过号");
				tvOpterator.setTag(OPT_PASS);
			} else {
				tvOpterator.setText("分配");
				tvOpterator.setTag(OPT_ASSIGN);
			}
		} else if ((opStatus & AppointmentInfo.STATUS_PASSED) != 0) {
			if ((role & Employee.NURSE) != 0) {
				tvOpterator.setText("重新排队");
				tvOpterator.setTag(OPT_REQUEUE);
			} else {
				tvOpterator.setVisibility(View.GONE);
			}
		} else if ((opStatus & AppointmentInfo.STATUS_FINISH) != 0) {
			tvOpterator.setVisibility(View.GONE);
		}
	}

	private void initOptButtons() {
		btnCall.setBackgroundResource(R.drawable.bg_button_registration);
		if (presenter.isConnected()) {
			btnCall.setVisibility(View.VISIBLE);
		} else {
			btnCall.setVisibility(View.GONE);
		}

		if ((role & Employee.DOCTOR) != 0 && (opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			btnOpt.setText("结束");
			btnOpt.setTag(OPT_STOP);
			btnCall.setVisibility(View.GONE);
			btnOpt.setBackgroundResource(R.drawable.bg_button_stop);
			layoutButtons.setVisibility(View.VISIBLE);
		} else
			if ((opStatus & AppointmentInfo.STATUS_WATTING) != 0 || (opStatus & AppointmentInfo.STATUS_PASSED) != 0) {
			btnOpt.setText("开始");
			btnOpt.setTag(OPT_START);
			btnOpt.setBackgroundResource(R.drawable.bg_button_start);
			layoutButtons.setVisibility(View.VISIBLE);
		} else {
			layoutButtons.setVisibility(View.GONE);
		}
	}

	@Override
	public void initMemberData() {
		super.initMemberData();
		Intent intent = getIntent();
		if (intent != null) {
//			this.appointmentInfo = intent.getParcelableExtra(MemberConstant.APPOINTMENT);
			intent.setExtrasClassLoader(AppointmentInfo.class.getClassLoader());
			this.appointmentList = intent.getParcelableArrayListExtra(MemberConstant.APPOINTMENT_LIST);
			appointment_index = intent.getIntExtra(MemberConstant.APPOINTMENT_LIST_INDEX, 0);
			this.appointmentInfo = this.appointmentList.get(appointment_index);
			opStatus = this.appointmentInfo.getOPStatus();
		}

		loginEmployee = ((MedicalAppliction) getApplicationContext()).getLoginEmployee();
		if (loginEmployee != null) {
			role = loginEmployee.getEmployeeRole();
		}

		presenter = new PatientDetailPresenterImpl(this);
		presenter.getPatientDetail(appointmentInfo.getPatientID());

		mSymptomDao = SymptomDao.getInstance(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.addScreenCallBack();
	}

	@Override
	protected void onPause() {
		super.onPause();
		presenter.removeScreenCallBack();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.removeScreenCallBack();
	}

	@Override
	public void setListener() {
		super.setListener();
		mBackImg.setOnClickListener(this);
		tvOpterator.setOnClickListener(this);
		circleHeadImage.setOnClickListener(this);
		
		btn_left_arrow.setOnClickListener(this);
		btn_right_arrow.setOnClickListener(this);

		btnOpt.setOnClickListener(this);
		btnCall.setOnClickListener(this);
		btn_patient_detail.setOnClickListener(this);

		TvTabOne.setOnClickListener(this);
		TvTabTwo.setOnClickListener(this);
		TvTabThree.setOnClickListener(this);
		TvTabFour.setOnClickListener(this);
	}

	protected void updateView(Patient patient) {
		if (patient == null) {
			return;
		}

		long appointmentNo = 0;
		if (appointmentInfo != null) {
			appointmentNo = appointmentInfo.getOPNo();
		}

		if (patient.getGender() == 1) {// 1代表男
			circleHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_man));
		} else {
			circleHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_lady));
		}

		if (patient.getPhotoSID() != 0) {
			circleHeadImage.loadImage(patient.getPhotoUrl());
		}

		super.updateView(patient, appointmentNo);
		patientBasicLayout.initData(patient);

		presenter.getPatientMedicalHistory(appointmentInfo.getPatientID());
	}

	/**
	 * 下一个预约
	 */
	private void nextAppiontment(AppointmentInfo appointmentInfo) {
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
		this.setResult(MemberConstant.APPIONTMENT_NEXT_RESOULT_CODE, intent);
		finish();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_left_arrow:
			int index = appointmentList.indexOf(appointmentInfo);
			if (index != -1 && index != 0) {
				appointmentInfo = appointmentList.get(index - 1);
				opStatus = this.appointmentInfo.getOPStatus();
				presenter.getPatientDetail(appointmentInfo.getPatientID());
			}
			if (index - 1 == 0) {
				btn_left_arrow.setVisibility(View.INVISIBLE);
			} else {
				btn_left_arrow.setVisibility(View.VISIBLE);
			}
			btn_right_arrow.setVisibility(View.VISIBLE);
			updateInterface();
			break;
		case R.id.btn_right_arrow:
			int curerntIndex = appointmentList.indexOf(appointmentInfo);
			if (curerntIndex != -1 && curerntIndex != appointmentList.size() - 1) {
				appointmentInfo = appointmentList.get(curerntIndex + 1);
				opStatus = this.appointmentInfo.getOPStatus();
				presenter.getPatientDetail(appointmentInfo.getPatientID());
			}
			if (curerntIndex + 1 == appointmentList.size() - 1) {
				btn_right_arrow.setVisibility(View.INVISIBLE);
			} else {
				btn_right_arrow.setVisibility(View.VISIBLE);
			}
			btn_left_arrow.setVisibility(View.VISIBLE);
			updateInterface();
			break;
		case R.id.tv_tab_one:
			handleTab(0);
			viewpager.setCurrentItem(0);
			break;
		case R.id.tv_tab_two:
			handleTab(1);
			viewpager.setCurrentItem(1);
			break;
		case R.id.tv_tab_three:
			handleTab(2);
			viewpager.setCurrentItem(2);
			break;
		case R.id.tv_tab_four:
			handleTab(3);
			viewpager.setCurrentItem(3);
			break;
		case R.id.back:
			onBackPressed();
			break;
		case R.id.headimg:
			if (mPatient == null) {
				return;
			}
			Intent headImageIntent = new Intent(this, ImageActivity.class);
			headImageIntent.putExtra(MemberConstant.EMPLOYEE_PHOTO_URL, mPatient.getPhotoUrl());
			headImageIntent.putExtra(MemberConstant.EMPLOYEE_GENDER, mPatient.getGender());
			startActivity(headImageIntent);
			break;
		case R.id.btn_call:
			presenter.callPatient(appointmentInfo, appointmentInfo.getDoctorName());
			break;
		case R.id.btn_patient_detail:
			dialog_patient_detail.show();
			break;
		case R.id.iv_edit:
			dialog_patient_detail.dismiss();
			Intent intent = new Intent(PatientDetailActivity.this, PatientEditActivity.class);
			intent.putExtra(MemberConstant.PATIENT, mPatient);
			startActivityForResult(intent, MemberConstant.PATIENT_REQUEST_CODE);
			break;
		default:
			Object obj = v.getTag();
			if (obj instanceof Integer) {
				dealOpt((Integer) obj);
			}
			break;
		}
	}

	private void dealOpt(Integer opt) {
		if (opt == null) {
			return;
		}
		switch (opt) {
		case OPT_PASS:
			passAppointment();
			break;
		case OPT_ASSIGN:
			assignAppointment(appointmentInfo);
			break;
		case OPT_REQUEUE:
			presenter.updateAppointment(appointmentInfo, AppointmentInfo.STATUS_REAPPOINT);
			break;
		case OPT_START:
			presenter.updateAppointment(appointmentInfo, AppointmentInfo.STATUS_DIAGNOSING);
			break;
		case OPT_STOP:
			presenter.updateAppointment(appointmentInfo, AppointmentInfo.STATUS_FINISH);
			break;

		default:
			break;
		}
	}

	// 过号
	private void passAppointment() {
		/*
		 * if ((System.currentTimeMillis() -
		 * appintmentInfo.getCreateTime().getTime()) < (long) 30 * (long) 60 *
		 * (long) 1000) { Toast.makeText(this, appintmentInfo.getTaStr() +
		 * "预约才不到半小时,请手下留情", Toast.LENGTH_SHORT).show(); return; }
		 */

		final AlertDialog myDialog = new AlertDialog.Builder(this).setMessage("确定过号？")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						presenter.updateAppointment(appointmentInfo, AppointmentInfo.STATUS_PASSED);
					}
				}).setNeutralButton("取消", null).create();
		myDialog.show();

	}

	// 分配
	private void assignAppointment(AppointmentInfo appintmentInfo) {
		// 跳到分配界面
		Intent intent = new Intent(this, DoctorSelectListActivity.class);
		intent.putExtra(MemberConstant.APPOINTMENT, appintmentInfo);
		startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE) {
			switch (resultCode) {
			case MemberConstant.APPIONTMENT_RESOULT_CODE:
				setResult(resultCode, data);
				finish();// 只传递结果不处理结果值
				break;
			case MemberConstant.APPIONTMENT_RESOULT_ASSIGN:
				nextAppiontment(appointmentInfo);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void updateViewSatus(int status) {
		// 上报状态
		appointmentInfo.setOPStatus(status);
		presenter.reportOpStatus(appointmentInfo, appointmentInfo.getDoctorName());
		if ((status & AppointmentInfo.STATUS_REAPPOINT) != 0 || (status & AppointmentInfo.STATUS_PASSED) != 0
				|| (status & AppointmentInfo.STATUS_FINISH) != 0) {
			nextAppiontment(appointmentInfo);
			return;
		}

		setScreenOn();

		this.opStatus = status;
		initTitleBar();
		initTitleOpt();
		initOptButtons();
	}

	@Override
	public void updateViewData(Patient patient) {
		this.mPatient = patient;
		updateView(patient);
	}

	@Override
	public void updateAnamnesisData(List<Anamnesis> anamnesisList) {
		anamnesisListView.setAdapter(new AnamnesisAdapter(anamnesisList));
	}

	@Override
	public void showCallButton(boolean visable) {
		if (visable && (appointmentInfo.getOPStatus() & AppointmentInfo.STATUS_WATTING) != 0) {
			btnCall.setVisibility(View.VISIBLE);
		} else {
			btnCall.setVisibility(View.GONE);
		}
	}

	public void setScreenOn() {
		if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	private class AnamnesisAdapter extends BaseAdapter {

		private List<Anamnesis> anamnesisList;

		public AnamnesisAdapter(List<Anamnesis> anamnesisList) {
			this.anamnesisList = anamnesisList;
		}

		@Override
		public int getCount() {
			return anamnesisList.size();
		}

		@Override
		public Object getItem(int position) {
			return anamnesisList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_anamnesis, null);
				holder = new ViewHolder();
				
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tv_symptom = (TextView) convertView.findViewById(R.id.tv_symptom);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			Anamnesis anamnesis = anamnesisList.get(position);
			
			holder.tv_date.setText(DateUtils.getDateString(anamnesis.getVisitTime(), new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)));
			holder.tv_symptom.setText(anamnesis.getDiagnosisName());
			
			return convertView;
		}

		private class ViewHolder {
			TextView tv_date;
			TextView tv_symptom;
		}

	}
}
