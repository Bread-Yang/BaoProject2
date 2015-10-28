package com.mdground.yizhida.activity.searchpatient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.patientinfo.PatientDetailActivity;
import com.mdground.yizhida.activity.patientinfo.PatientEditActivity;
import com.mdground.yizhida.activity.symptom.SymptomActivity;
import com.mdground.yizhida.adapter.SearchDetailAdapter;
import com.mdground.yizhida.adapter.SearchSimpleAdapter;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.bean.Symptom;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.SymptomDao;
import com.mdground.yizhida.view.ResizeLinearLayout;
import com.mdground.yizhida.view.ResizeLinearLayout.OnResizeListener;

@SuppressLint("NewApi")
public class SearchPatientActivity extends BaseActivity implements OnItemClickListener, SearchPatientView, OnEditorActionListener, OnClickListener,
		OnResizeListener {
	
	/** 取消搜索 **/
	private TextView TvCancelSearch;
	/** 清除搜索关键字 **/
	private ImageView IvClearSearchKey;
	/** 关键字输入框 **/
	private EditText EtSearchKey;
	/** 添加用户 **/
	private RelativeLayout addPersonLayout;
	/** 搜索提醒 **/
	private LinearLayout searchPromptLayout;
	/** 搜索结果 **/
	private ListView LvSearchResult;

	private ResizeLinearLayout searchRootLayout;

	private TextView TvSearchPromptTitle;
	private TextView TvSearchPromptValue;

	private SearchSimpleAdapter searchSimpleAdapter;
	private SearchDetailAdapter<Patient> searchDetailAdapter;
	private Employee loginEmployee;
	private List<Patient> patients = new ArrayList<Patient>();// 搜索结果保存

	private boolean isDetail;

	private SymptomDao mSymptomDao;
	private SearchPatientPresenter presenter;
	
	Handler mHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			showDetail(isDetail);
			return false;
		}
	});

	/**
	 * ListView直接点击挂号时回掉此函数
	 */
	private SearchDetailAdapter.AppiontmentCallBack mAppiontmentCallBack = new SearchDetailAdapter.AppiontmentCallBack() {

		@Override
		public void onAppiontment(Patient patient) {
			createAppointment(patient);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		findView();
		initMemberData();
		initView();
		setListener();
	}

	@Override
	public void findView() {
		TvCancelSearch = (TextView) this.findViewById(R.id.cancel);
		IvClearSearchKey = (ImageView) this.findViewById(R.id.clear_edit);
		EtSearchKey = (EditText) this.findViewById(R.id.search_edit);
		TvSearchPromptTitle = (TextView) this.findViewById(R.id.search_prompt_title);
		TvSearchPromptValue = (TextView) this.findViewById(R.id.search_prompt_value);
		addPersonLayout = (RelativeLayout) this.findViewById(R.id.add_person_layout);
		addPersonLayout.setVisibility(View.GONE);
		searchPromptLayout = (LinearLayout) this.findViewById(R.id.search_prompt);
		LvSearchResult = (ListView) this.findViewById(R.id.search_result_listview);
		searchRootLayout = (ResizeLinearLayout) findViewById(R.id.layout_root_search_patient);
		searchRootLayout.setOnResizeListener(this);
	}

	@Override
	public void initView() {
		LvSearchResult.setAdapter(searchSimpleAdapter);
		LvSearchResult.setEmptyView(searchPromptLayout);
		handleEmpty(false);
	}

	@Override
	public void initMemberData() {
		MedicalAppliction app = (MedicalAppliction) getApplication();
		this.loginEmployee = app.getLoginEmployee();
		// 请求症状列表
		searchSimpleAdapter = new SearchSimpleAdapter(this);
		searchDetailAdapter = new SearchDetailAdapter<Patient>(this, mAppiontmentCallBack);
		searchSimpleAdapter.setDataList(patients);
		searchDetailAdapter.setDataList(patients);

		mSymptomDao = SymptomDao.getInstance(this);
		presenter = new SearchPatientPresenterImpl(this);
	}

	@Override
	public void setListener() {
		LvSearchResult.setOnItemClickListener(this);
		addPersonLayout.setOnClickListener(this);
		EtSearchKey.setOnEditorActionListener(this);
		TvCancelSearch.setOnClickListener(this);
		IvClearSearchKey.setOnClickListener(this);

		EtSearchKey.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 每次搜索都先清空数据再发起请求
				patients.clear();
				if (TextUtils.isEmpty(EtSearchKey.getText().toString())) {
					handleEmpty(false);
					return;
				}
				IvClearSearchKey.setVisibility(View.VISIBLE);
				presenter.searchPatient(EtSearchKey.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mHandler.sendEmptyMessage(0);
			}
		});

	}

	public void handleEmpty(boolean isAfterSearch) {
		if (isAfterSearch) {
			addPersonLayout.setVisibility(View.VISIBLE);
			TvSearchPromptTitle.setText(R.string.no_match_patient_title);
			TvSearchPromptValue.setText(R.string.no_match_patient_value);
		} else {
			TvSearchPromptTitle.setText(R.string.search_prompt_title);
			TvSearchPromptValue.setText(R.string.search_prompt_value);
		}
	}

	public void showDetail(boolean isDetail) {
		this.isDetail = isDetail;
		if (patients.size() == 0) {
			handleEmpty(true);
		}

		if (isDetail) {
			addPersonLayout.setVisibility(View.VISIBLE);
			LvSearchResult.setAdapter(searchDetailAdapter);
		} else {
			addPersonLayout.setVisibility(View.GONE);
			LvSearchResult.setAdapter(searchSimpleAdapter);
		}
//		if (!isDestroyed()) {
		if (!isFinishing()) {
			searchDetailAdapter.notifyDataSetChanged();
			searchSimpleAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Patient patient = patients.get(position);
		if (parent == null) {
			return;
		}
		if (isDetail) {
			Intent intent = new Intent(SearchPatientActivity.this, PatientDetailActivity.class);
			intent.putExtra(MemberConstant.PATIENT_ID, patient.getPatientID());
			startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
		} else {
			addPersonLayout.setVisibility(View.VISIBLE);
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			presenter.searchPatient(patient.getPatientName());
			EtSearchKey.setText(patients.get(position).getPatientName());
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// 表示按了键盘，使键盘消失，此时要显示详细的
		if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			if (EtSearchKey.getText().length() > 0) {
				presenter.searchPatient(EtSearchKey.getText().toString());
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			onBackPressed();
			break;
		case R.id.clear_edit:
			v.setVisibility(View.GONE);
			EtSearchKey.setText("");
			break;
		case R.id.add_person_layout:
			Intent intent = new Intent(this, PatientEditActivity.class);
			Patient patient = new Patient();
			patient.setClinicID(loginEmployee.getClinicID());
			patient.setPatientName(EtSearchKey.getText().toString());
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -30);
			patient.setDOB(c.getTime());
			patient.setRegistrationTime(c.getTime());
			intent.putExtra(MemberConstant.PATIENT, patient);
			intent.putExtra("isAdd", true);
			startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
			break;
		}
	}

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

	// 只传递结果，不处理结果值
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE && resultCode == MemberConstant.APPIONTMENT_RESOULT_CODE) {
			setResult(resultCode, data);
			finish();
		}
	}

	@Override
	public void updateResult(List<Patient> patients) {
		this.patients.clear();
		if (patients == null || patients.size() == 0) {
			handleEmpty(true);
		} else {
			this.patients.addAll(patients);
//			showDetail(isDetail);
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void finishResult(int appiontmentResoultCode, AppointmentInfo appointment) {
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointment);
		setResult(appiontmentResoultCode, intent);
		finish();
	}

	@Override
	public void OnResize(int w, final int h, int oldw, final int oldh) {
		int offset = oldh - h;
		if (offset > 0) {
			isDetail = false;
		} else {
			isDetail = true;
		}
		
		if (this.patients.size() == 0) {
			return;
		}
		
//		showDetail(isDetail);
		mHandler.sendEmptyMessage(0);
	}
}
