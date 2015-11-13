package com.mdground.yizhida.activity.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.ImageActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.patientinfo.PatientCommonActivity;
import com.mdground.yizhida.activity.patientinfo.PatientEditActivity;
import com.mdground.yizhida.activity.prescription.PatientPrescriptionActivity;
import com.mdground.yizhida.adapter.PatientInfoPageAdapter;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.ChiefComplaint;
import com.mdground.yizhida.bean.Diagnosis;
import com.mdground.yizhida.bean.Drug;
import com.mdground.yizhida.bean.DrugUse;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.OfficeVisitFee;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.bean.VitalSigns;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.DrugDao;
import com.mdground.yizhida.db.dao.SymptomDao;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.util.FeeUtil;
import com.mdground.yizhida.util.StringUtil;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.util.ViewUtil;
import com.mdground.yizhida.view.CircleImageView;
import com.mdground.yizhida.view.PatientBasicLayout;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
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
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 病人预约详情界面
 * 
 * @author Administrator
 * 
 */
public class PatientAppointmentActivity extends PatientCommonActivity
		implements OnClickListener, PatientAppointmentView {

	private static final int OPT_PASS = 1;
	private static final int OPT_REQUEUE = 2;
	private static final int OPT_ASSIGN = 3;
	private static final int OPT_STOP = 4;
	private static final int OPT_START = 5;
	private static final int OPT_CALL = 6;

	private final int ANIMATION_DURATION = 100;

	private LinearLayout llt_btn, llt_signs, llt_chief_complaint, llt_diagnosis, llt_prescription, llt_amount;

	private RelativeLayout rlt_title, rlt_signs, rlt_chief_complaint, rlt_diagnosis, rlt_prescription,
			rlt_common_patient_layout;

	private ScrollView sc_treating_patient_layout;

	private ImageView mBackImg, iv_collapse, iv_open;

	private Button btn_one, btn_two, btn_patient_detail;

	private TextView tvTitle, tvOpterator, tv_temperature, tv_height, tv_weight, tv_bmi, tv_heartbeat, tv_breath,
			tv_blood_pressure, tv_blood_glucose, tv_chief_complaint, tv_diagnosis, tv_amount;

	private ListView anamnesisListView, lv_drug_use;

	private CircleImageView circleHeadImage;

	private Dialog dialog_patient_detail;

	private PatientBasicLayout patientBasicLayout;
	private ArrayList<View> viewContainter;
	private PatientInfoPageAdapter mPatientInfoPageAdapter;

	private Employee loginEmployee;
	private ArrayList<AppointmentInfo> appointmentList;
	private int appointment_index;
	private AppointmentInfo mAppointment;
	private Patient mPatient;
	private PatientAppointmentPresenter presenter;

	// 预约状态
	private int opStatus;
	private int role;

	// dao
	SymptomDao mSymptomDao;

	// 控制屏幕是否常亮
	PowerManager.WakeLock wl;

	private int height_of_llt_btn;

	private VitalSigns mVitalSigns;

	private ChiefComplaint mChiefComplaint;

	private ArrayList<Diagnosis> mDiagnosisList;

	private DrugDao mDrugDao;

	private Diagnosis mDiagnosisAddTemplate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMemberData();
		setScreenOn();
		setContentView(R.layout.activity_patient_appointment);
		findView();
		initView();
		setListener();
	}

	@Override
	public void findView() {
		super.findView();

		llt_btn = (LinearLayout) findViewById(R.id.llt_btn);
		llt_signs = (LinearLayout) findViewById(R.id.llt_signs);
		llt_chief_complaint = (LinearLayout) findViewById(R.id.llt_chief_complaint);
		llt_diagnosis = (LinearLayout) findViewById(R.id.llt_diagnosis);
		llt_prescription = (LinearLayout) findViewById(R.id.llt_prescription);
		llt_amount = (LinearLayout) findViewById(R.id.llt_amount);

		rlt_title = (RelativeLayout) findViewById(R.id.rlt_title);
		rlt_signs = (RelativeLayout) findViewById(R.id.rlt_signs);
		rlt_chief_complaint = (RelativeLayout) findViewById(R.id.rlt_chief_complaint);
		rlt_diagnosis = (RelativeLayout) findViewById(R.id.rlt_diagnosis);
		rlt_prescription = (RelativeLayout) findViewById(R.id.rlt_prescription);
		rlt_common_patient_layout = (RelativeLayout) findViewById(R.id.rlt_common_patient_layout);

		sc_treating_patient_layout = (ScrollView) findViewById(R.id.sc_treating_patient_layout);

		tvTitle = (TextView) findViewById(R.id.title);
		tvOpterator = (TextView) findViewById(R.id.opterator);
		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_height = (TextView) findViewById(R.id.tv_height);
		tv_weight = (TextView) findViewById(R.id.tv_weight);
		tv_bmi = (TextView) findViewById(R.id.tv_bmi);
		tv_heartbeat = (TextView) findViewById(R.id.tv_heartbeat);
		tv_breath = (TextView) findViewById(R.id.tv_breath);
		tv_blood_pressure = (TextView) findViewById(R.id.tv_blood_pressure);
		tv_blood_glucose = (TextView) findViewById(R.id.tv_blood_glucose);
		tv_chief_complaint = (TextView) findViewById(R.id.tv_chief_complaint);
		tv_diagnosis = (TextView) findViewById(R.id.tv_diagnosis);
		tv_amount = (TextView) findViewById(R.id.tv_amount);

		lv_drug_use = (ListView) findViewById(R.id.lv_drug_use);

		iv_collapse = (ImageView) findViewById(R.id.iv_collapse);
		iv_open = (ImageView) findViewById(R.id.iv_open);

		circleHeadImage = (CircleImageView) findViewById(R.id.headimg);

		// 操作按钮
		btn_one = (Button) findViewById(R.id.btn_one);
		btn_two = (Button) findViewById(R.id.btn_two);
		btn_patient_detail = (Button) findViewById(R.id.btn_patient_detail);
	}

	@Override
	public void initView() {
		super.initView();
		mDrugDao = DrugDao.getInstance(getApplicationContext());

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

		View anamnesisLayout = getLayoutInflater().inflate(R.layout.viewpager_item_anamnesis, null);
		anamnesisListView = (ListView) anamnesisLayout.findViewById(R.id.listview);

		viewContainter.add(anamnesisLayout);
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_allergic_history, null));
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_medication_compliance, null));
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_other_information, null));
		mPatientInfoPageAdapter = new PatientInfoPageAdapter(viewContainter);
		viewpager.setAdapter(mPatientInfoPageAdapter);

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

		// 开始或者结束之后,显示体征,主诉,诊断等信息
		if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0 || (opStatus & AppointmentInfo.STATUS_FINISH) != 0) {
			iv_collapse.setVisibility(View.VISIBLE);
			sc_treating_patient_layout.setVisibility(View.VISIBLE);
			rlt_common_patient_layout.setVisibility(View.GONE);

			if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0 && (role & Employee.DOCTOR) != 0) {
				setImageViewVisibility(rlt_signs, true);
				setImageViewVisibility(rlt_chief_complaint, true);
				setImageViewVisibility(rlt_diagnosis, true);
				setImageViewVisibility(rlt_prescription, true);
				setImageViewVisibility(llt_signs, true);
				setImageViewVisibility(llt_chief_complaint, true);
				setImageViewVisibility(llt_diagnosis, true);
				setImageViewVisibility(llt_prescription, true);
			} else {
				setImageViewVisibility(rlt_signs, false);
				setImageViewVisibility(rlt_chief_complaint, false);
				setImageViewVisibility(rlt_diagnosis, false);
				setImageViewVisibility(rlt_prescription, false);
				setImageViewVisibility(llt_signs, false);
				setImageViewVisibility(llt_chief_complaint, false);
				setImageViewVisibility(llt_diagnosis, false);
				setImageViewVisibility(llt_prescription, false);
			}
		} else {
			iv_collapse.setVisibility(View.INVISIBLE);
			sc_treating_patient_layout.setVisibility(View.GONE);
			rlt_common_patient_layout.setVisibility(View.VISIBLE);
		}
	}

	private void updateInterface() {
		initTitleBar();
		initTitleOpt();
		// if ((role & Employee.DOCTOR) != 0) {
		initOptButtons();
		// }
	}

	private void initTitleBar() {
		if (opStatus == 0) {
			// titleLayout.setBackgroundResource(R.drawable.top_bg1);
		} else if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			rlt_title.setBackgroundResource(R.drawable.top_bg2);
			tvTitle.setText("就诊中");

			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_green_bg);
			btn_left_arrow.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.left_arrow_green), null, null, null);
			btn_right_arrow.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.right_arrow_green), null);
			// btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_green);
			// btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_green);

		} else if ((opStatus & AppointmentInfo.STATUS_WATTING) != 0) {
			rlt_title.setBackgroundResource(R.drawable.top_bg1);
			tvTitle.setText("候诊中");
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_blue_bg);
			btn_left_arrow.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.left_arrow_blue), null, null, null);
			btn_right_arrow.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.right_arrow_blue), null);
			// btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_blue);
			// btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_blue);
		} else if ((opStatus & AppointmentInfo.STATUS_PASSED) != 0) {
			tvTitle.setText("已过号");
			rlt_title.setBackgroundResource(R.drawable.top_bg3);
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_black_bg);
			btn_left_arrow.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.left_arrow_black), null, null, null);
			btn_right_arrow.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.right_arrow_black), null);
			// btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_black);
			// btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_black);
		} else if ((opStatus & AppointmentInfo.STATUS_FINISH) != 0) {
			rlt_title.setBackgroundResource(R.drawable.top_bg3);
			tvTitle.setText("已就诊");
			TvAppionmentNo.setBackgroundResource(R.drawable.shape_patient_num_black_bg);
			btn_left_arrow.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.left_arrow_black), null, null, null);
			btn_right_arrow.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.right_arrow_black), null);
			// btn_left_arrow.setBackgroundResource(R.drawable.left_arrow_black);
			// btn_right_arrow.setBackgroundResource(R.drawable.right_arrow_black);
		}

		// 处于开始状态,则获取主诉,诊断,体征等信息
		if ((mAppointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0
				|| (mAppointment.getOPStatus() & AppointmentInfo.STATUS_FINISH) != 0) {
			presenter.getPatientAppointmentDetail(mAppointment.getOPID());

			if ((mAppointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
				if (sc_treating_patient_layout.getVisibility() == View.VISIBLE) {
					llt_amount.setVisibility(View.VISIBLE);
					iv_open.setVisibility(View.GONE);
				}
			} else {
				llt_amount.setVisibility(View.GONE);
			}
		} else {
			llt_amount.setVisibility(View.GONE);
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
			tvOpterator.setText("过号");
			tvOpterator.setTag(OPT_PASS);
			tvOpterator.setVisibility(View.VISIBLE);
			// if ((role & Employee.DOCTOR) != 0) {
			// tvOpterator.setText("过号");
			// tvOpterator.setTag(OPT_PASS);
			// } else {
			// tvOpterator.setText("分配");
			// tvOpterator.setTag(OPT_ASSIGN);
			// }
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
		btn_two.setBackgroundResource(R.drawable.selector_bg_button_blue_swell);
		if (presenter.isConnected()) {
			btn_two.setVisibility(View.VISIBLE);
			btn_two.setTag(OPT_CALL);
		} else {
			btn_two.setVisibility(View.GONE);
		}

		if ((role & Employee.DOCTOR) != 0 && (opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			btn_one.setText("结束");
			btn_one.setTag(OPT_STOP);
			btn_two.setVisibility(View.GONE);
			btn_one.setBackgroundResource(R.drawable.selector_bg_button_red_rectangle);
			llt_btn.setVisibility(View.VISIBLE);
		} else if ((role & Employee.NURSE) != 0) {
			if ((opStatus & AppointmentInfo.STATUS_WATTING) != 0
					&& (opStatus & AppointmentInfo.STATUS_DIAGNOSING) == 0) {
				llt_btn.setVisibility(View.VISIBLE);

				btn_one.setBackgroundResource(R.drawable.selector_bg_button_blue_swell);
				btn_one.setTag(OPT_CALL);
				btn_one.setText(R.string.opt_call);

				if (!presenter.isConnected()) {
					btn_one.setVisibility(View.GONE);
				}

				btn_two.setVisibility(View.VISIBLE);
				btn_two.setBackgroundResource(R.drawable.selector_bg_button_black_swell);
				btn_two.setTag(OPT_ASSIGN);
				btn_two.setText(R.string.opt_assign);
			}
		} else if ((opStatus & AppointmentInfo.STATUS_WATTING) != 0
				|| (opStatus & AppointmentInfo.STATUS_PASSED) != 0) {
			btn_one.setText("开始");
			btn_one.setTag(OPT_START);
			btn_one.setBackgroundResource(R.drawable.selector_bg_button_green_rectangle);
			llt_btn.setVisibility(View.VISIBLE);
		}
	}

	private void setImageViewVisibility(ViewGroup viewGroup, boolean isShow) {
		int count = viewGroup.getChildCount();
		for (int i = 0; i <= count; i++) {
			View v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				setImageViewVisibility((ViewGroup) v, isShow);
			} else if (v instanceof ImageView) {
				v.setVisibility(isShow ? View.VISIBLE : View.GONE);
			}
		}
	}

	@Override
	public void initMemberData() {
		super.initMemberData();
		Intent intent = getIntent();
		if (intent != null) {
			intent.setExtrasClassLoader(AppointmentInfo.class.getClassLoader());
			this.appointmentList = intent.getParcelableArrayListExtra(MemberConstant.APPOINTMENT_LIST);
			appointment_index = intent.getIntExtra(MemberConstant.APPOINTMENT_LIST_INDEX, 0);
			this.mAppointment = this.appointmentList.get(appointment_index);
			opStatus = this.mAppointment.getOPStatus();
		}

		loginEmployee = ((MedicalAppliction) getApplicationContext()).getLoginEmployee();
		if (loginEmployee != null) {
			role = loginEmployee.getEmployeeRole();
		}

		presenter = new PatientAppointmentPresenterImpl(this);

		mSymptomDao = SymptomDao.getInstance(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.getPatientDetail(mAppointment.getPatientID());
		presenter.addScreenCallBack();
		updateInterface();
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

		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);
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
		if (mAppointment != null) {
			appointmentNo = mAppointment.getOPNo();
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

		// 既往史
		presenter.getPatientMedicalHistory(mAppointment.getPatientID());
	}

	/**
	 * 下一个预约
	 */
	private void nextAppiontment(AppointmentInfo appointmentInfo, boolean isCallNext) {
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
		intent.putExtra(MemberConstant.APPOINTMENT_CALL_NEXT, isCallNext);
		this.setResult(MemberConstant.APPIONTMENT_NEXT_RESOULT_CODE, intent);
		finish();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlt_signs:
		case R.id.llt_signs: {
			if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0 && (role & Employee.DOCTOR) != 0) {
				Intent intent = new Intent(this, PatientVitalSignsActivity.class);
				intent.putExtra(MemberConstant.APPOINTMENT_VITAL_SIGNS, mVitalSigns);
				startActivity(intent);
			}
			break;
		}
		case R.id.rlt_chief_complaint:
		case R.id.llt_chief_complaint: {
			if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0 && (role & Employee.DOCTOR) != 0) {
				Intent intent = new Intent(this, PatientChiefComplaintActivity.class);
				intent.putExtra(MemberConstant.APPOINTMENT_CHIEF_COMPLAINT, mChiefComplaint);
				startActivity(intent);
			}
			break;
		}
		case R.id.rlt_diagnosis:
		case R.id.llt_diagnosis: {
			if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0 && (role & Employee.DOCTOR) != 0) {
				Intent intent = new Intent(this, PatientDiagnosisActivity.class);
				intent.putExtra(MemberConstant.APPOINTMENT_DIAGNOSIS_LIST, mDiagnosisList);
				intent.putExtra(MemberConstant.APPOINTMENT_DIAGNOSIS_ADD_TEMPLATE, mDiagnosisAddTemplate);
				startActivity(intent);
			}
			break;
		}
		case R.id.rlt_prescription:
		case R.id.llt_prescription: {
			if ((opStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0 && (role & Employee.DOCTOR) != 0) {
				Intent intent = new Intent(this, PatientPrescriptionActivity.class);
				startActivity(intent);
			}
			break;
		}
		case R.id.btn_left_arrow:
			int index = appointmentList.indexOf(mAppointment);
			if (index != -1 && index != 0) {
				mAppointment = appointmentList.get(index - 1);
				opStatus = this.mAppointment.getOPStatus();
				presenter.getPatientDetail(mAppointment.getPatientID());
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
			int curerntIndex = appointmentList.indexOf(mAppointment);
			if (curerntIndex != -1 && curerntIndex != appointmentList.size() - 1) {
				mAppointment = appointmentList.get(curerntIndex + 1);
				opStatus = this.mAppointment.getOPStatus();
				presenter.getPatientDetail(mAppointment.getPatientID());
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
		case R.id.btn_patient_detail:
			dialog_patient_detail.show();
			break;
		case R.id.iv_edit:
			dialog_patient_detail.dismiss();
			Intent intent = new Intent(PatientAppointmentActivity.this, PatientEditActivity.class);
			intent.putExtra(MemberConstant.PATIENT, mPatient);
			startActivityForResult(intent, MemberConstant.PATIENT_REQUEST_CODE);
			break;
		case R.id.iv_collapse: {
			rlt_common_patient_layout.setVisibility(View.VISIBLE);

			final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llt_btn.getLayoutParams();

			height_of_llt_btn = llt_btn.getHeight();
			ValueAnimator animator = ValueAnimator.ofInt(height_of_llt_btn, 0).setDuration(ANIMATION_DURATION);
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					lp.height = (Integer) animation.getAnimatedValue();
					llt_btn.setLayoutParams(lp);
				}
			});
			animator.start();

			translateLayout(sc_treating_patient_layout, false);

			iv_collapse.setVisibility(View.INVISIBLE);
			iv_open.setVisibility(View.VISIBLE);
			llt_amount.setVisibility(View.GONE);
			break;
		}
		case R.id.iv_open: {
			final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llt_btn.getLayoutParams();

			ValueAnimator animator = ValueAnimator.ofInt(0, height_of_llt_btn).setDuration(ANIMATION_DURATION);
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					lp.height = (Integer) animation.getAnimatedValue();
					llt_btn.setLayoutParams(lp);
				}
			});
			animator.start();
			translateLayout(sc_treating_patient_layout, true);

			iv_collapse.setVisibility(View.VISIBLE);
			iv_open.setVisibility(View.GONE);
			if ((mAppointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
				llt_amount.setVisibility(View.VISIBLE);
			}
			break;
		}
		default:

			Object obj = v.getTag();
			if (obj instanceof Integer)

			{
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
		case OPT_CALL:
			presenter.callPatient(mAppointment, mAppointment.getDoctorName());
			break;
		case OPT_PASS:
			passAppointment();
			break;
		case OPT_ASSIGN:
			assignAppointment(mAppointment);
			break;
		case OPT_REQUEUE:
			presenter.updateAppointment(mAppointment, AppointmentInfo.STATUS_REAPPOINT);
			break;
		case OPT_START:
			presenter.updateAppointment(mAppointment, AppointmentInfo.STATUS_DIAGNOSING);
			break;
		case OPT_STOP:
			if (MedicalAppliction.mDrugUseMap.size() == 0 && MedicalAppliction.mOfficeVisitFeeMap.size() == 0) {
				Toast.makeText(getApplicationContext(), R.string.no_presciption, Toast.LENGTH_SHORT).show();
				return;
			}
			presenter.updateAppointment(mAppointment, AppointmentInfo.STATUS_FINISH);
			break;

		default:
			break;
		}
	}

	private void translateLayout(View layout, boolean isShow) {
		TranslateAnimation animation;
		if (isShow) {
			layout.setVisibility(View.VISIBLE);
			animation = new TranslateAnimation(0, 0, layout.getHeight(), 0);
			animation.setDuration(ANIMATION_DURATION);
			layout.setAnimation(animation);
		} else {
			animation = new TranslateAnimation(0, 0, 0, layout.getHeight());
			animation.setDuration(ANIMATION_DURATION);
			layout.setAnimation(animation);
			layout.setVisibility(View.GONE);
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

		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY); // 当前时间

		currentHour = currentHour / 2 + 1;

		if (mAppointment.getOPDatePeriod() > currentHour) {
			Toast.makeText(getApplicationContext(), R.string.current_time_no_appointment_time, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		final AlertDialog myDialog = new AlertDialog.Builder(this).setMessage("确定过号？")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						presenter.updateAppointment(mAppointment, AppointmentInfo.STATUS_PASSED);
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
				nextAppiontment(mAppointment, false);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void updateViewSatus(int status) {
		// 上报状态
		mAppointment.setOPStatus(status);
		presenter.reportOpStatus(mAppointment, mAppointment.getDoctorName());
		if ((status & AppointmentInfo.STATUS_REAPPOINT) != 0 || (status & AppointmentInfo.STATUS_PASSED) != 0
				|| (status & AppointmentInfo.STATUS_FINISH) != 0) {
			nextAppiontment(mAppointment, true);
			return;
		}

		// 处于开始状态,则获取主诉,诊断,体征等信息
		if ((mAppointment.getOPStatus() & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			presenter.getPatientAppointmentDetail(mAppointment.getOPID());
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
		if (anamnesisList.size() == 0) {
			anamnesisListView.setVisibility(View.INVISIBLE);
		} else {
			anamnesisListView.setVisibility(View.VISIBLE);
		}
		anamnesisListView.setAdapter(new AnamnesisAdapter(anamnesisList));
	}

	@Override
	public void updateAppointmentDetail(String jsonString) {
		try {
			JSONObject content = new JSONObject(jsonString);

			int clinicID = content.getInt("ClinicID");
			int doctorID = content.getInt("DoctorID");
			int patientID = content.getInt("PatientID");
			int visitID = content.getInt("VisitID");

			// 体征
			String vitalSign = content.getString("VitalSign");

			if (!StringUtil.isEmpty(vitalSign)) {
				mVitalSigns = new Gson().fromJson(vitalSign, VitalSigns.class);

				rlt_signs.setVisibility(View.GONE);
				llt_signs.setVisibility(View.VISIBLE);

				tv_temperature.setText(Tools.getFormat(getApplicationContext(), R.string.temperature_unit,
						mVitalSigns.getBodyTemperature()));
				if (mVitalSigns.getBodyTemperature() >= 38) {
					tv_temperature.setTextColor(getResources().getColor(R.color.red));
				} else if (mVitalSigns.getBodyTemperature() <= 36) {
					tv_temperature.setTextColor(getResources().getColor(R.color.blue));
				} else {
					tv_temperature.setTextColor(getResources().getColor(R.color.black));
				}
				
				tv_height.setText(
						Tools.getFormat(getApplicationContext(), R.string.height_unit, mVitalSigns.getHeight()));
				tv_weight.setText(
						Tools.getFormat(getApplicationContext(), R.string.weight_unit, mVitalSigns.getWeight()));
				tv_bmi.setText(String.valueOf(mVitalSigns.getBMI()));
				
				tv_heartbeat.setText(
						Tools.getFormat(getApplicationContext(), R.string.heartbeat_unit, mVitalSigns.getHeartbeat()));
				if (mVitalSigns.getHeartbeat() >= 160) {
					tv_heartbeat.setTextColor(getResources().getColor(R.color.red));
				} else if (mVitalSigns.getHeartbeat() <= 60) {
					tv_heartbeat.setTextColor(getResources().getColor(R.color.blue));
				} else {
					tv_heartbeat.setTextColor(getResources().getColor(R.color.black));
				}
				
				tv_breath.setText(
						Tools.getFormat(getApplicationContext(), R.string.breath_unit, mVitalSigns.getBreathing()));
				if (mVitalSigns.getBreathing() >= 24) {
					tv_breath.setTextColor(getResources().getColor(R.color.red));
				} else if (mVitalSigns.getBreathing() <= 12) {
					tv_breath.setTextColor(getResources().getColor(R.color.blue));
				} else {
					tv_breath.setTextColor(getResources().getColor(R.color.black));
				}
				
				tv_blood_pressure.setText(Tools.getFormat(getApplicationContext(), R.string.blood_pressure_unit,
						mVitalSigns.getSystolicPressure(), mVitalSigns.getDiastolicBloodPressure()));
				float bloodPressure = mVitalSigns.getSystolicPressure() / mVitalSigns.getDiastolicBloodPressure();
				if (bloodPressure >= (140 / 90f)) {
					tv_blood_pressure.setTextColor(getResources().getColor(R.color.red));
				} else if (bloodPressure <= (90 / 60f)) {
					tv_blood_pressure.setTextColor(getResources().getColor(R.color.blue));
				} else {
					tv_blood_pressure.setTextColor(getResources().getColor(R.color.black));
				}
				
				tv_blood_glucose.setText(Tools.getFormat(getApplicationContext(), R.string.blood_glucose_unit,
						mVitalSigns.getBloodGlucose()));
				if (mVitalSigns.getBloodGlucose() >= 200) {
					tv_blood_glucose.setTextColor(getResources().getColor(R.color.red));
				} else if (mVitalSigns.getBloodGlucose() <= 60) {
					tv_blood_glucose.setTextColor(getResources().getColor(R.color.blue));
				} else {
					tv_blood_glucose.setTextColor(getResources().getColor(R.color.black));
				}
			} else {
				rlt_signs.setVisibility(View.VISIBLE);
				llt_signs.setVisibility(View.GONE);

				mVitalSigns = new VitalSigns();
				mVitalSigns.setClinicID(clinicID);
				mVitalSigns.setDoctorID(doctorID);
				mVitalSigns.setPatientID(patientID);
				mVitalSigns.setVisitID(visitID);

				mVitalSigns.setBodyTemperature(37.0f);
				mVitalSigns.setHeight(100);
				mVitalSigns.setWeight(50);
				mVitalSigns.setBMI(50.0f);

				mVitalSigns.setHeartbeat(70);
				mVitalSigns.setBreathing(15);
				mVitalSigns.setSystolicPressure(140);
				mVitalSigns.setDiastolicBloodPressure(70);
				mVitalSigns.setBloodGlucose(110);
			}

			// 主诉
			String chiefComplaint = content.getString("ChiefComplaint");

			if (!StringUtil.isEmpty(chiefComplaint)) {
				mChiefComplaint = new Gson().fromJson(chiefComplaint, ChiefComplaint.class);

				if (!StringUtil.isEmpty(mChiefComplaint.getDescription())) {
					rlt_chief_complaint.setVisibility(View.GONE);
					llt_chief_complaint.setVisibility(View.VISIBLE);

					tv_chief_complaint.setText(mChiefComplaint.getDescription());
				}
			} else {
				rlt_chief_complaint.setVisibility(View.VISIBLE);
				llt_chief_complaint.setVisibility(View.GONE);

				mChiefComplaint = new ChiefComplaint();

				mChiefComplaint.setClinicID(clinicID);
				mChiefComplaint.setPatientID(patientID);
				mChiefComplaint.setVisitID(visitID);
			}

			// 新增诊断的模版
			mDiagnosisAddTemplate = new Diagnosis();
			mDiagnosisAddTemplate.setClinicID(clinicID);
			mDiagnosisAddTemplate.setDoctorID(doctorID);
			mDiagnosisAddTemplate.setPatientID(patientID);
			mDiagnosisAddTemplate.setVisitID(visitID);

			// 诊断
			String diagnosis = content.getString("Diagnosis");

			if (!StringUtil.isEmpty(diagnosis)) {
				mDiagnosisList = new Gson().fromJson(diagnosis, new TypeToken<ArrayList<Diagnosis>>() {
				}.getType());
				rlt_diagnosis.setVisibility(View.GONE);
				llt_diagnosis.setVisibility(View.VISIBLE);

				String allDiagnosis = "";
				for (int i = 0; i < mDiagnosisList.size(); i++) {
					allDiagnosis += mDiagnosisList.get(i).getDiagnosisName();
					if (i != mDiagnosisList.size() - 1) {
						allDiagnosis += "\n";
					}
				}
				tv_diagnosis.setText(allDiagnosis);
			} else {
				rlt_diagnosis.setVisibility(View.VISIBLE);
				llt_diagnosis.setVisibility(View.GONE);
			}

			// 新增处方用法的模版
			MedicalAppliction.mDrugUseAddTemplate = new DrugUse();
			MedicalAppliction.mDrugUseAddTemplate.setClinicID(clinicID);
			MedicalAppliction.mDrugUseAddTemplate.setDoctorID(doctorID);
			MedicalAppliction.mDrugUseAddTemplate.setPatientID(patientID);
			MedicalAppliction.mDrugUseAddTemplate.setVisitID(visitID);
			MedicalAppliction.mDrugUseAddTemplate.setDays(1);

			MedicalAppliction.mDrugUseMap.clear();
			MedicalAppliction.mOfficeVisitFeeMap.clear();

			// 处方
			String drugUseListString = content.getString("DrugUseList");

			if (!StringUtil.isEmpty(drugUseListString)) {
				ArrayList<DrugUse> drugUseList = new Gson().fromJson(drugUseListString,
						new TypeToken<ArrayList<DrugUse>>() {
						}.getType());

				for (DrugUse item : drugUseList) {
					MedicalAppliction.mDrugUseMap.put(String.valueOf(item.getDrugID()), item);
				}
			}

			// 费用
			String feeListString = content.getString("FeeList");

			if (!StringUtil.isEmpty(feeListString)) {
				ArrayList<OfficeVisitFee> mOfficeVisitFeeList = new Gson().fromJson(feeListString,
						new TypeToken<ArrayList<OfficeVisitFee>>() {
						}.getType());

				for (OfficeVisitFee item : mOfficeVisitFeeList) {
					MedicalAppliction.mOfficeVisitFeeMap.put(item.getFeeType(), item);
				}
			}

			if (!StringUtil.isEmpty(drugUseListString) || !StringUtil.isEmpty(feeListString)) {
				rlt_prescription.setVisibility(View.GONE);
				llt_prescription.setVisibility(View.VISIBLE);

				if (!StringUtil.isEmpty(drugUseListString)) {
					lv_drug_use.setAdapter(new DrugUseAdapter());

					ViewUtil.setListViewHeightBasedOnChildren(lv_drug_use);
				}

				FeeUtil.calculateAmountAndShowInTextView(tv_amount);
			} else {
				rlt_prescription.setVisibility(View.VISIBLE);
				llt_prescription.setVisibility(View.GONE);
			}

		} catch (

		JSONException e)

		{
			e.printStackTrace();

			L.e(PatientAppointmentActivity.this, "出错了");
		}

	}

	@Override
	public void showCallButton(boolean visable) {
		if (visable && (mAppointment.getOPStatus() & AppointmentInfo.STATUS_WATTING) != 0) {
			btn_two.setVisibility(View.VISIBLE);
		} else {
			btn_two.setVisibility(View.GONE);
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

			holder.tv_date.setText(DateUtils.getDateString(anamnesis.getVisitTime(),
					new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)));
			holder.tv_symptom.setText(anamnesis.getDiagnosisName());

			return convertView;
		}

		private class ViewHolder {
			TextView tv_date;
			TextView tv_symptom;
		}

	}

	private class DrugUseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return MedicalAppliction.mDrugUseMap.size();
		}

		@Override
		public Object getItem(int position) {
			return MedicalAppliction.mDrugUseMap.values().toArray()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// @Override
		// public boolean isEnabled(int position) {
		// return false;
		// }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_patient_appointment_prescription_drug, null);

				holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
				holder.tv_drug_name = (TextView) convertView.findViewById(R.id.tv_drug_name);
				holder.tv_specification = (TextView) convertView.findViewById(R.id.tv_specification);
				holder.tv_unit_price = (TextView) convertView.findViewById(R.id.tv_unit_price);
				holder.tv_single_dosage = (TextView) convertView.findViewById(R.id.tv_single_dosage);
				holder.tv_direction = (TextView) convertView.findViewById(R.id.tv_direction);
				holder.tv_frequency = (TextView) convertView.findViewById(R.id.tv_frequency);
				holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			DrugUse drugUse = (DrugUse) MedicalAppliction.mDrugUseMap.values().toArray()[position];

			if (drugUse.getGroupNo() == 0) {
				holder.tv_group_name.setText(R.string.not_yet_group);
			} else {
				holder.tv_group_name.setText(getResources().getString(R.string.group) + drugUse.getGroupNo());
			}

			holder.tv_drug_name.setText(drugUse.getDrugName() + "  x" + drugUse.getSaleQuantity());

			Drug drug = mDrugDao.getDrugByDrugID(drugUse.getDrugID());
			if (drug != null) {
				holder.tv_specification.setText(drug.getSpecification());
			} else {
				holder.tv_specification.setText("");
			}
			holder.tv_unit_price.setText(String.format("%.02f", drugUse.getSalePrice() / 100f)
					+ getResources().getString(R.string.yuan) + "/" + drugUse.getSaleUnit());

			holder.tv_single_dosage.setText(drugUse.getTake() + drugUse.getTakeUnit());

			holder.tv_direction.setText(drugUse.getTakeType());

			holder.tv_frequency.setText(drugUse.getFrequency());

			holder.tv_comment.setText(drugUse.getRemark());

			return convertView;
		}

		class ViewHolder {
			TextView tv_group_name, tv_drug_name, tv_specification, tv_unit_price, tv_single_dosage, tv_direction,
					tv_frequency, tv_comment;
		}
	}
}
