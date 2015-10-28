package com.mdground.yizhida.activity.patientinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.ImageActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.symptom.SymptomActivity;
import com.mdground.yizhida.adapter.PatientInfoPageAdapter;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.bean.Symptom;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.SymptomDao;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.view.CircleImageView;
import com.mdground.yizhida.view.PatientBasicLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
	private TextView tv_emergency;
	private ListView anamnesisListView;
	private CircleImageView circleHeadImage;
	// 操作按钮
	private Button btnOpt, btn_patient_detail;

	private PatientDetailPresenter presenter;

	private Employee loginEmployee;
	private Patient mPatient;
	private int patientId;

	private Dialog dialog_patient_detail;

	private SymptomDao mSymptomDao;
	
	private boolean isEmergency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_detail);
		findView();
		initMemberData();
		initView();
		setListener();
	}

	@Override
	public void findView() {
		super.findView();
		tv_emergency = (TextView) findViewById(R.id.tv_emergency);
		circleHeadImage = (CircleImageView) findViewById(R.id.headimg);
		mBackImg = (ImageView) findViewById(R.id.back);

		// 操作按钮
		btnOpt = (Button) findViewById(R.id.btn_opt);
		btn_patient_detail = (Button) findViewById(R.id.btn_patient_detail);

		btn_left_arrow.setVisibility(View.INVISIBLE);
		btn_right_arrow.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initView() {
		super.initView();
		viewContainter = new ArrayList<View>();
		patientBasicLayout = new PatientBasicLayout(this);
		// 初始化dialog及dialog里面的控件
		dialog_patient_detail = new Dialog(this, R.style.patient_detail_dialog);
		dialog_patient_detail.setContentView(patientBasicLayout);

		ImageView iv_edit = (ImageView) patientBasicLayout.findViewById(R.id.iv_edit);
		iv_edit.setImageResource(R.drawable.box_delete);
		iv_edit.setOnClickListener(this);

		// 设置dialog弹入弹出的动画
		Window window = dialog_patient_detail.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
		window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
		window.setAttributes(wlp);

		View anamnesisLayout = getLayoutInflater().inflate(R.layout.viewpager_item_anamnesis, null);
		anamnesisListView = (ListView) anamnesisLayout.findViewById(R.id.listview);

		viewContainter.add(anamnesisLayout);
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_allergic_history, null));
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_medication_compliance, null));
		viewContainter.add(getLayoutInflater().inflate(R.layout.viewpager_item_other_information, null));
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
		tv_emergency.setOnClickListener(this);
		mBackImg.setOnClickListener(this);
		circleHeadImage.setOnClickListener(this);
		btnOpt.setOnClickListener(this);
		btn_patient_detail.setOnClickListener(this);

		TvTabOne.setOnClickListener(this);
		TvTabTwo.setOnClickListener(this);
		TvTabThree.setOnClickListener(this);
		TvTabFour.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
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
		case R.id.tv_emergency: {
			isEmergency = true;
			createAppointment(mPatient);
//			Intent intent = new Intent(PatientDetailActivity.this, PatientEditActivity.class);
//			intent.putExtra(MemberConstant.PATIENT, mPatient);
//			startActivityForResult(intent, MemberConstant.PATIENT_REQUEST_CODE);
			break;
		}
		case R.id.btn_opt:
			createAppointment(mPatient);
			break;
		case R.id.btn_patient_detail:
			dialog_patient_detail.show();
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
		case R.id.iv_edit:
			dialog_patient_detail.dismiss();
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
			finish();// 只传递结果不处理结果值
		} else if (requestCode == MemberConstant.PATIENT_REQUEST_CODE
				&& resultCode == MemberConstant.PATIENT_RESOULT_CODE) {
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

		presenter.getPatientMedicalHistory(patientId);
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
		
		if (isEmergency) {
			appointmentInfo.setEmergency(true);
		}

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

}
