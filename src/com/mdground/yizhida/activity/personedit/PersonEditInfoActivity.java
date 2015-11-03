package com.mdground.yizhida.activity.personedit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.Header;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.SaveEmployee;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.dialog.BirthdayDatePickerDialog;
import com.mdground.yizhida.util.DateUtils;

public class PersonEditInfoActivity extends BaseActivity implements OnClickListener, OnDateSetListener, OnFocusChangeListener {

	private TextView TvSave;
	private TextView TvContentTitle;
	private TextView TvContentEditTitle;
	private EditText EtContent;
	private ImageView IvBack;
	private LinearLayout editLayout;
	private RelativeLayout resumeLayout;
	private WebView TvResumeContent;
	private LinearLayout sexLayout;

	private RelativeLayout sexManLayout;
	private RelativeLayout sexGirlLayout;

	private ImageView IvManSelected;
	private ImageView IvGirlSelected;

	private DatePickerDialog datePickerDialog;

	private int updateCode;

	private Employee employee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info_edit);
		findView();
		handIntent();
		initMemberData();
		setListener();
		initView();
	}

	@Override
	public void findView() {
		TvSave = (TextView) this.findViewById(R.id.save);
		TvContentTitle = (TextView) this.findViewById(R.id.edit_title);
		TvContentEditTitle = (TextView) this.findViewById(R.id.content_input_title);
		EtContent = (EditText) this.findViewById(R.id.content);
		IvBack = (ImageView) this.findViewById(R.id.back);
		resumeLayout = (RelativeLayout) this.findViewById(R.id.resume_layout);
		TvResumeContent = (WebView) this.findViewById(R.id.resume_content);
		editLayout = (LinearLayout) this.findViewById(R.id.edit_layout);
		sexLayout = (LinearLayout) this.findViewById(R.id.sex_layout);
		sexManLayout = (RelativeLayout) this.findViewById(R.id.select_man);
		sexGirlLayout = (RelativeLayout) this.findViewById(R.id.select_girl);
		IvManSelected = (ImageView) this.findViewById(R.id.man_selected);
		IvGirlSelected = (ImageView) this.findViewById(R.id.girl_selected);

	}

	private void handIntent() {
		updateCode = this.getIntent().getIntExtra(MemberConstant.EMPLOYEE_UPDATE, 0);
	}

	@Override
	public void initView() {
		MedicalAppliction medicalAppliction = (MedicalAppliction) this.getApplication();
		employee = medicalAppliction.getLoginEmployee();
		switch (updateCode) {
		case MemberConstant.UPDATE_NAME:
			setTitleText("姓名");
			EtContent.setText(employee.getEmployeeName());
			break;
		case MemberConstant.UPDATE_ADDRESS:
			break;
		case MemberConstant.UPDATE_GRADUATE_SCHOOL:
			setTitleText("毕业院校");
			EtContent.setText(employee.getGraduateSchool());
			break;
		case MemberConstant.UPDATE_SPECIALTY_NAME:
			setTitleText("毕业专业");
			EtContent.setText(employee.getSpecialtyName());
			break;
		case MemberConstant.UPDATE_SEX:
			setTitleText("性别");
			TvSave.setVisibility(View.GONE);
			editLayout.setVisibility(View.GONE);
			sexLayout.setVisibility(View.VISIBLE);
			if (employee.getGender() == 1) {
				IvGirlSelected.setVisibility(View.GONE);
				IvManSelected.setVisibility(View.VISIBLE);
			} else {
				IvGirlSelected.setVisibility(View.VISIBLE);
				IvManSelected.setVisibility(View.GONE);
			}
			break;
		case MemberConstant.UPDATE_BIRTHDAY:
			setTitleText("生日");
			TvContentEditTitle.setVisibility(View.GONE);
			EtContent.setText(DateUtils.getDateString(employee.getDOB(), new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)));
			EtContent.setFocusable(false);
			showDialog();
			break;
		case MemberConstant.UPDATE_RESUME:
			TvSave.setVisibility(View.GONE);
			resumeLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			TvResumeContent.loadDataWithBaseURL(null, employee.getResume(), "text/html", "UTF-8", null);
			setTitleText("个人简历");
		}

	}

	private void setTitleText(String title) {
		TvContentTitle.setText(title);
		TvContentEditTitle.setText(title);
	}

	@Override
	public void initMemberData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		TvSave.setOnClickListener(this);
		TvContentEditTitle.setOnClickListener(this);
		IvBack.setOnClickListener(this);
		EtContent.setOnFocusChangeListener(this);
		sexManLayout.setOnClickListener(this);
		sexGirlLayout.setOnClickListener(this);
		EtContent.setOnClickListener(this);
	}

	public void showDialog() {
		Calendar calendar = Calendar.getInstance();
		if (employee != null) {
			calendar.setTime(employee.getDOB());
		}
		if (datePickerDialog == null) {
			datePickerDialog = new BirthdayDatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		} else {
			datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		}
		datePickerDialog.show();
	}

	public void saveEmployeeInfo() {

		String content = EtContent.getText().toString();
		switch (updateCode) {
		case MemberConstant.UPDATE_NAME:
			if (EtContent.getText() == null || EtContent.getText().toString().equals("")) {
				Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
				return;
			}
			employee.setEmployeeName(content);
			break;
		case MemberConstant.UPDATE_ADDRESS:
			break;
		case MemberConstant.UPDATE_GRADUATE_SCHOOL:
			if (EtContent.getText() == null || EtContent.getText().toString().equals("")) {
				Toast.makeText(this, "请填写学校", Toast.LENGTH_SHORT).show();
				return;
			}
			employee.setGraduateSchool(content);
			break;
		case MemberConstant.UPDATE_SPECIALTY_NAME:
			if (EtContent.getText() == null || EtContent.getText().toString().equals("")) {
				Toast.makeText(this, "请填写专业", Toast.LENGTH_SHORT).show();
				return;
			}
			employee.setSpecialtyName(content);
			break;
		case MemberConstant.UPDATE_BIRTHDAY:
			employee.setDOB(DateUtils.toDate(content, new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)));
			break;
		}

		SaveEmployee saveEmployee = new SaveEmployee(this);
		saveEmployee.saveEmployee(employee, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					MedicalAppliction medicalAppliction = (MedicalAppliction) PersonEditInfoActivity.this.getApplication();
					medicalAppliction.setLoginEmployee(employee);
					PersonEditInfoActivity.this.finish();
				} else {
					requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.select_girl:
			IvManSelected.setVisibility(View.GONE);
			IvGirlSelected.setVisibility(View.VISIBLE);
			employee.setGender(2);
			saveEmployeeInfo();
			break;
		case R.id.select_man:
			IvManSelected.setVisibility(View.VISIBLE);
			IvGirlSelected.setVisibility(View.GONE);
			employee.setGender(1);
			saveEmployeeInfo();
			break;
		case R.id.save:
			saveEmployeeInfo();
			break;
		case R.id.content:
			if (updateCode == MemberConstant.UPDATE_BIRTHDAY) {
				showDialog();
			}
			break;
		}

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		EtContent.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus && updateCode == MemberConstant.UPDATE_BIRTHDAY) {
			showDialog();
		}

	}

}
