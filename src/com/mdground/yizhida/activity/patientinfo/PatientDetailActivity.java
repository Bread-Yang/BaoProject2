package com.mdground.yizhida.activity.patientinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.ImageActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.symptom.SymptomActivity;
import com.mdground.yizhida.adapter.PatientInfoPageAdapter;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.bean.Symptom;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.SymptomDao;
import com.mdground.yizhida.view.CircleImageView;
import com.mdground.yizhida.view.PatientBasicLayout;

/**
 * 病人详情界面从搜索到此界面
 * 
 * @author Administrator
 * 
 */
public class PatientDetailActivity extends PatientCommonActivity implements OnClickListener, PatientDetailView {

	private PatientBasicLayout patientBasicLayout;
	private ArrayList<View> viewContainter;
	private PatientInfoPageAdapter mPatientInfoPageAdapter;
	private ImageView mBackImg;
	private TextView tvOpterator;
	private CircleImageView circleHeadImage;
	// 操作按钮
	private Button btnOpt;

	private PatientDetailPresenter presenter;

	private Employee loginEmployee;
	private Patient mPatient;
	private int patientId;

	private SymptomDao mSymptomDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);
		findView();
		initMemberData();
		initView();
		setListener();
	}

	@Override
	public void findView() {
		super.findView();
		tvOpterator = (TextView) findViewById(R.id.edit);
		circleHeadImage = (CircleImageView) findViewById(R.id.headimg);
		mBackImg = (ImageView) findViewById(R.id.back);

		// 操作按钮
		btnOpt = (Button) findViewById(R.id.btn_opt);
	}

	@Override
	public void initView() {
		super.initView();
		viewContainter = new ArrayList<View>();
		patientBasicLayout = new PatientBasicLayout(this);
		viewContainter.add(patientBasicLayout);
		TvTabOne.setText(R.string.basic_info_string);
		mPatientInfoPageAdapter = new PatientInfoPageAdapter(viewContainter);
		viewpager.setAdapter(mPatientInfoPageAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.getPatient(patientId);
	}

	@Override
	public void initMemberData() {
		super.initMemberData();
		Intent intent = getIntent();
		if (intent != null) {
			patientId = intent.getIntExtra(MemberConstant.PATIENT_ID, 0);
		}

		loginEmployee = ((MedicalAppliction) getApplicationContext()).getLoginEmployee();

		presenter = new PatientDetailPresenterImpl(this);
		mSymptomDao = SymptomDao.getInstance(this);
	}

	@Override
	public void setListener() {
		super.setListener();
		tvOpterator.setOnClickListener(this);
		mBackImg.setOnClickListener(this);
		circleHeadImage.setOnClickListener(this);
		btnOpt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		case R.id.edit: {
			Intent intent = new Intent(PatientDetailActivity.this, PatientEditActivity.class);
			intent.putExtra(MemberConstant.PATIENT, mPatient);
			startActivityForResult(intent, MemberConstant.PATIENT_REQUEST_CODE);
			break;
		}
		case R.id.btn_opt:
			createAppointment(mPatient);
			break;
		case R.id.headimg: {
			Intent intent = new Intent(this, ImageActivity.class);
			if (mPatient == null) {
				return;
			}
			intent.putExtra(MemberConstant.EMPLOYEE_PHOTO_URL, mPatient.getPhotoUrl());
			intent.putExtra(MemberConstant.EMPLOYEE_GENDER, mPatient.getGender());
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE && resultCode == MemberConstant.APPIONTMENT_RESOULT_CODE) {
			setResult(resultCode, data);
			finish();// 只传递结果不处理结果值
		} else if (requestCode == MemberConstant.PATIENT_REQUEST_CODE && resultCode == MemberConstant.PATIENT_RESOULT_CODE) {
			if (data != null) {
				mPatient = data.getParcelableExtra(MemberConstant.PATIENT);
				updateView(mPatient, 0);
			}
		}
	}

	@Override
	protected void updateView(Patient patient, long appiontmentNo) {
		super.updateView(patient, appiontmentNo);
		patientBasicLayout.initData(patient);

		if (patient.getGender() == 1) {// 1代表男
			circleHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_man));
		} else {
			circleHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_lady));
		}

		circleHeadImage.loadImage(patient.getPhotoUrl());
	}

	@Override
	public void showPaitent(Patient patient) {
		this.mPatient = patient;
		updateView(patient, 0);
	}

	/**
	 * 创建一个挂号类
	 * 
	 * @param patient
	 * @return
	 */
	private void createAppointment(Patient patient) {

		List<Symptom> symptoms = mSymptomDao.getSymptoms();

		AppointmentInfo appointmentInfo = new AppointmentInfo();
		appointmentInfo.setOPDate(new Date());
		appointmentInfo.setPatientID(patient.getPatientID());

		Intent intent = new Intent();

		if ((loginEmployee.getEmployeeRole() & Employee.DOCTOR) != 0) {
			appointmentInfo.setDoctorID(loginEmployee.getEmployeeID());
			appointmentInfo.setOPEMR(loginEmployee.getEMRType());
			appointmentInfo.setDoctorName(loginEmployee.getEmployeeName());
			appointmentInfo.setClinicID(loginEmployee.getClinicID());
			if (symptoms != null && symptoms.size() != 0) {
				intent.setClass(this, SymptomActivity.class);
			} else {
				presenter.saveAppointment(appointmentInfo);
				return;
			}
		} else {
			if (symptoms == null || symptoms.size() == 0) {
				// 跳转到医生列表
				intent.setClass(this, DoctorSelectListActivity.class);
			} else {
				intent.setClass(this, SymptomActivity.class);
			}
		}

		intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
		startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
	}

	@Override
	public void finishResult(int appiontmentResoultCode, AppointmentInfo appointmentInfo) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
		setResult(appiontmentResoultCode, intent);
		finish();
	}
}
