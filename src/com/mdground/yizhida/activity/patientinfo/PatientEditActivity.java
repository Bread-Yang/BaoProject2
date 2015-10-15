package com.mdground.yizhida.activity.patientinfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.common.logging.Log;
import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.AddressActivity;
import com.mdground.yizhida.activity.ImageActivity;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.activity.symptom.SymptomActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.fileserver.SaveFile;
import com.mdground.yizhida.api.server.global.SaveEmployeePhoto;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.bean.Symptom;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.SymptomDao;
import com.mdground.yizhida.dialog.BirthdayDatePickerDialog;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.util.GetPhotoPop;
import com.mdground.yizhida.util.LocationUtils;
import com.mdground.yizhida.util.RegexUtil;
import com.mdground.yizhida.util.ToolPicture;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PatientEditActivity extends BaseActivity implements OnClickListener, OnDateSetListener, PatientEditView {
	// private static final String TAG = "PatientEditActivity";
	private EditText EtPositionValue;
	private EditText EtCompanyValue;
	private EditText EtMzValue;
	private TextView TvMarryValue;
	private EditText EtYbValue;
	private EditText EtSbValue;
	private EditText EtInnerCodeValue;
	private EditText EtEmailValue;
	private EditText EtRelationshipValue;
	private EditText EtConactValue;
	private EditText EtUrgencyConcat;
	private EditText EtIdCardValue;
	private EditText EtPhoneValue;
	private EditText EtEnglishValue;
	private EditText EtNameValue;

	private TextView TvAddressValue;
	private TextView TvBirthdayValue;
	private TextView TvSexValue;
	private TextView TvFinish;

	private RelativeLayout addressLayout;
	private RelativeLayout marryLayout;
	private RelativeLayout birthdayLayout;
	private RelativeLayout sexLayout;
	private RelativeLayout headLayout;

	private LinearLayout mainLayout;

	private CircleImageView IvHeadImage;
	private ImageView IvBack;

	private View sexView;
	private TextView TvManText;
	private TextView TvGrilText;

	private View marrayView;
	private TextView TvMarrayText;
	private TextView TvUnMarrayText;
	private TextView TvBreakMarrayText;

	private GetPhotoPop mGetPhotoPop;
	private PopupWindow mPopupWindow;
	private DatePickerDialog datePickerDialog;;

	private Patient mPatient;
	private Employee loginEmployee;// 登陆账号
	private PatientEditPresenter presenter;

	private SymptomDao mSymptomDao;

	private boolean isCreate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_person_layout);
		findView();
		initMemberData();
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mGetPhotoPop.isShowing()) {
			mGetPhotoPop.dismiss();
		}
	}

	@Override
	public void findView() {

		IvHeadImage = (CircleImageView) this.findViewById(R.id.head_image);
		IvBack = (ImageView) this.findViewById(R.id.back);

		addressLayout = (RelativeLayout) this.findViewById(R.id.address_layout);
		marryLayout = (RelativeLayout) this.findViewById(R.id.marry_layout);
		birthdayLayout = (RelativeLayout) this.findViewById(R.id.birthlayout);
		sexLayout = (RelativeLayout) this.findViewById(R.id.sexlayout);
		headLayout = (RelativeLayout) this.findViewById(R.id.headlayout);
		mainLayout = (LinearLayout) this.findViewById(R.id.add_person_main_layout);

		sexView = LayoutInflater.from(this).inflate(R.layout.select_sex_layout, null);
		TvManText = (TextView) sexView.findViewById(R.id.man_text);
		TvGrilText = (TextView) sexView.findViewById(R.id.girl_text);

		marrayView = LayoutInflater.from(this).inflate(R.layout.select_marry_layout, null);
		TvMarrayText = (TextView) marrayView.findViewById(R.id.marry_text);
		TvUnMarrayText = (TextView) marrayView.findViewById(R.id.unmarry_text);
		TvBreakMarrayText = (TextView) marrayView.findViewById(R.id.break_marry_text);

		TvAddressValue = (TextView) this.findViewById(R.id.address_value);
		TvBirthdayValue = (TextView) this.findViewById(R.id.birthday_value);
		TvSexValue = (TextView) this.findViewById(R.id.sex_value);
		TvFinish = (TextView) this.findViewById(R.id.finish);

		EtPositionValue = (EditText) this.findViewById(R.id.position_value);
		EtCompanyValue = (EditText) this.findViewById(R.id.company_value);
		EtMzValue = (EditText) this.findViewById(R.id.mz_value);
		TvMarryValue = (TextView) this.findViewById(R.id.marry_value);
		EtYbValue = (EditText) this.findViewById(R.id.yb_value);
		EtSbValue = (EditText) this.findViewById(R.id.sb_value);
		EtInnerCodeValue = (EditText) this.findViewById(R.id.inner_code);
		EtEmailValue = (EditText) this.findViewById(R.id.email_value);
		EtRelationshipValue = (EditText) this.findViewById(R.id.relationship_value);
		EtConactValue = (EditText) this.findViewById(R.id.concact_value);
		EtUrgencyConcat = (EditText) this.findViewById(R.id.urgency_concact_value);
		EtIdCardValue = (EditText) this.findViewById(R.id.idcard_value);
		EtPhoneValue = (EditText) this.findViewById(R.id.phone_value);
		EtEnglishValue = (EditText) this.findViewById(R.id.english_name);
		EtNameValue = (EditText) this.findViewById(R.id.name_value);

	}

	@Override
	public void initView() {
		if (mPatient == null) {
			return;
		}

		if (mPatient.getGender() == 1) {// 1代表男
			IvHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_man));
		} else {
			IvHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_lady));
		}
		IvHeadImage.loadImage(mPatient.getPhotoUrl());

		EtNameValue.setText(mPatient.getPatientName());
		EtEnglishValue.setText(mPatient.getForeignName());
		TvSexValue.setText(mPatient.getGenderStr());
		TvBirthdayValue.setText(DateUtils.getDateString(mPatient.getDOB(), new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)));
		EtPhoneValue.setText(mPatient.getPhone());
		EtIdCardValue.setText(mPatient.getIDCard());
		EtUrgencyConcat.setText(mPatient.getEmergencyName());// 紧急联系人
		EtConactValue.setText(mPatient.getEmergencyPhone());// 联系人电话
		EtRelationshipValue.setText(mPatient.getEmergencyRelationship());// 紧急联系人关系
		EtEmailValue.setText(mPatient.getEmail());
		EtInnerCodeValue.setText(mPatient.getPatientCode());// 内部编号
		EtSbValue.setText(mPatient.getSSN());
		EtYbValue.setText(mPatient.getMSN());// 医保
		TvMarryValue.setText(mPatient.getMarriedStr());
		EtMzValue.setText(mPatient.getNation());// 民族
		StringBuffer sb = new StringBuffer();
		if (mPatient.getProvinceID() != -1) {
			sb.append(LocationUtils.getInstance(this).getNameFromId(mPatient.getProvinceID()));
		}
		if (mPatient.getCityID() != -1) {
			sb.append(LocationUtils.getInstance(this).getNameFromId(mPatient.getCityID()));
		}
		if (mPatient.getStreet() != null) {
			sb.append(mPatient.getStreet());
		}
		TvAddressValue.setText(sb.toString());

		EtCompanyValue.setText(mPatient.getCompanyName());
		EtPositionValue.setText(mPatient.getTitle());
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		if (intent != null) {
			mPatient = intent.getParcelableExtra(MemberConstant.PATIENT);
		}

		if (mPatient.getPatientID() == 0) {
			isCreate = true;
		}

		mGetPhotoPop = new GetPhotoPop(this);
		if (mGetPhotoPop.getTempFile().exists()) {
			mGetPhotoPop.getTempFile().delete();
		}
		loginEmployee = ((MedicalAppliction) getApplication()).getLoginEmployee();
		presenter = new PatientEditPresenterImpl(this);
		mSymptomDao = SymptomDao.getInstance(this);
	}

	@Override
	public void setListener() {
		IvBack.setOnClickListener(this);
		TvFinish.setOnClickListener(this);
		addressLayout.setOnClickListener(this);
		TvManText.setOnClickListener(this);
		TvGrilText.setOnClickListener(this);
		marryLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		birthdayLayout.setOnClickListener(this);
		findViewById(R.id.birthday_value).setOnClickListener(this);

		TvMarrayText.setOnClickListener(this);
		TvUnMarrayText.setOnClickListener(this);
		TvBreakMarrayText.setOnClickListener(this);

		TvBirthdayValue.setOnClickListener(this);
		TvSexValue.setOnClickListener(this);

		headLayout.setOnClickListener(this);
		IvHeadImage.setOnClickListener(this);
	}

	/**
	 * 创建病人信息
	 * 
	 * @return
	 */
	private Patient productPatientDetail() {
		if (EtNameValue.getText() == null || EtNameValue.getText().toString().equals("")) {
			findViewById(R.id.name_notify).setVisibility(View.VISIBLE);
			showToast("请输入姓名");
			return null;
		} else {
			findViewById(R.id.name_notify).setVisibility(View.GONE);
			mPatient.setPatientName(EtNameValue.getText().toString());
		}

		mPatient.setForeignName(EtEnglishValue.getText().toString());
		if (TvBirthdayValue.getText() == null || TvBirthdayValue.getText().toString().equals("")) {
			findViewById(R.id.birthday_notify).setVisibility(View.VISIBLE);
			showToast("请输入出生日期");
			return null;
		} else {
			findViewById(R.id.birthday_notify).setVisibility(View.GONE);
			mPatient.setDOB(DateUtils.toDate(TvBirthdayValue.getText().toString(), new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)));
		}

		if (EtPhoneValue.getText() == null || EtPhoneValue.getText().toString().equals("")) {
			// findViewById(R.id.phone_notify).setVisibility(View.VISIBLE);
			// showToast("请输入手机号码");
			// return null;
			mPatient.setPhone("");
		} else {
			if (/*!RegexUtil.isPhoneNumber(EtPhoneValue.getText().toString())*/ EtPhoneValue.getText().toString() == null || EtPhoneValue.getText().toString().length() != 11) {
				showToast("手机格式不支持");
				findViewById(R.id.phone_notify).setVisibility(View.VISIBLE);
				return null;
			}
			findViewById(R.id.phone_notify).setVisibility(View.GONE);
			mPatient.setPhone(EtPhoneValue.getText().toString());
		}

		if (EtIdCardValue.getText() == null || EtIdCardValue.getText().toString().equals("")) {
			// findViewById(R.id.idcard_notify).setVisibility(View.VISIBLE);
			// showToast("请输入身份证号码");
			// return null;
			mPatient.setIDCard("");
		} else {
			findViewById(R.id.idcard_notify).setVisibility(View.GONE);
			mPatient.setIDCard(EtIdCardValue.getText().toString());
		}
		// 紧急联系人
		mPatient.setEmergencyName(EtUrgencyConcat.getText().toString());
		mPatient.setEmergencyPhone(EtConactValue.getText().toString());
		mPatient.setEmergencyRelationship(EtRelationshipValue.getText().toString());
		mPatient.setClinicID(loginEmployee.getClinicID());
		if (EtEmailValue.getText().toString() != null && !EtEmailValue.getText().toString().equals("")
				&& !RegexUtil.isEmail(EtEmailValue.getText().toString())) {
			EtEmailValue.requestFocus();
			findViewById(R.id.email_notify).setVisibility(View.VISIBLE);
			showToast("邮箱格式不支持");
			return null;
		} else {
			findViewById(R.id.email_notify).setVisibility(View.GONE);
		}
		mPatient.setEmail(EtEmailValue.getText().toString());

		mPatient.setPatientCode(EtInnerCodeValue.getText().toString());
		mPatient.setSSN(EtSbValue.getText().toString());
		mPatient.setMSN(EtYbValue.getText().toString());
		mPatient.setNation(EtMzValue.getText().toString());
		mPatient.setAddress(TvAddressValue.getText().toString());
		mPatient.setCompanyName(EtCompanyValue.getText().toString());
		mPatient.setTitle(EtPositionValue.getText().toString());
		if (mPatient.getRegistrationTime() == null) {
			mPatient.setRegistrationTime(new Date());
		}

		if (mPatient.getStatus() == 0) {
			mPatient.setStatus(1);
		}

		return mPatient;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (MemberConstant.LOCATION_REQUEST_CODE == requestCode && resultCode == MemberConstant.LOCATION_RESOULT_CODE && intent != null) {
			mPatient.setCountryID(intent.getIntExtra(MemberConstant.LOCATION_CONTURY_ID, 86));
			mPatient.setCityID(intent.getIntExtra(MemberConstant.LOCATION_CITY_ID, -1));
			mPatient.setProvinceID(intent.getIntExtra(MemberConstant.LOCATION_PROVINCE_ID, -1));
			mPatient.setDistrictID(intent.getIntExtra(MemberConstant.LOCATION_DISTRICT_ID, -1));
			mPatient.setStreet(intent.getStringExtra(MemberConstant.LOCATION_STREET));
			mPatient.setAddress(intent.getStringExtra(MemberConstant.LOCATION_ADDRESS));
			TvAddressValue.setText(mPatient.getAddress());
		} else if (MemberConstant.APPIONTMENT_REQUEST_CODE == requestCode && resultCode == MemberConstant.APPIONTMENT_RESOULT_CODE && intent != null) {
			setResult(resultCode, intent);
			finish();
		} else if (requestCode == GetPhotoPop.PHOTO_REQUEST_GALLERY) {// 从相册返回的数据
			if (intent != null) {
				Uri uri = intent.getData();
				mGetPhotoPop.setImageUri(uri);
				mGetPhotoPop.cropImageUri(mGetPhotoPop.getImageUri());
			}
		} else if (requestCode == GetPhotoPop.PHOTO_REQUEST_CAREMA) {// 从相机返回的数据
			if (new File(mGetPhotoPop.getRealFilePath(this, mGetPhotoPop.getImageUri())).exists()) {
				String path = Tools.getPathFromUri(this, mGetPhotoPop.getImageUri());
				int orientation = ToolPicture.readPictureDegree(path);// 获取旋转角度
				Log.i("HQL", "orientation : " + orientation);
				Bitmap bitmap = null;
				if (Math.abs(orientation) > 0) {
					Bitmap bit = ToolPicture.getBitmapFromUri(mGetPhotoPop.getImageUri(), this);
					bitmap = ToolPicture.rotaingBitmap(orientation, bit);
					mGetPhotoPop.cropImageUri(
							Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null)));
				} else {
					mGetPhotoPop.cropImageUri(mGetPhotoPop.getImageUri());
				}
			}
		} else if (requestCode == GetPhotoPop.PHOTO_REQUEST_CUT) {// 从剪切图片返回的数据
			if (intent != null) {
				Bitmap bitmap = intent.getParcelableExtra("data");
				IvHeadImage.setImageBitmap(bitmap);
			}
		}
	}

	private void updatePhoto(File file, long fileId) {
		SaveFile saveFile = new SaveFile(this);
		saveFile.saveFile(file, fileId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					int photoID = Integer.parseInt(response.getContent());
					mPatient.setPhotoSID(photoID);
					mPatient.setPhotoID(photoID);
					saveEmployeePhoto();
				} else {
					Toast.makeText(PatientEditActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStart() {
				getLoadingDialog().initText("正在上传...");
				showProgress();
			}

			@Override
			public void onFinish() {
				hidProgress();
				mGetPhotoPop.getTempFile().delete();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Toast.makeText(PatientEditActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	// 更新用户图片
	private void saveEmployeePhoto() {
		SaveEmployeePhoto savePhoto = new SaveEmployeePhoto(this);
		savePhoto.saveEmployeePhoto(mPatient.getPhotoID(), mPatient.getPhotoSID(), new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					ImageLoader.getInstance().clearDiskCache();
					ImageLoader.getInstance().clearMemoryCache();
					presenter.savePatient(mPatient);
				} else {
					requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {
				showProgress();
			}

			@Override
			public void onFinish() {
				hidProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				showToast(R.string.request_error);
			}
		});
	}

	private void showPopuWindow(int viewId) {
		View view = null;
		if (viewId == R.id.marry_layout) {
			view = marrayView;
		} else if (viewId == R.id.sexlayout) {
			view = sexView;
		}
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setOutsideTouchable(false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setContentView(view);
			mPopupWindow.setAnimationStyle(R.style.AnimBottom);
			mPopupWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
			mPopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {

				}
			});

		}

		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return;
		} else {

		}
		mPopupWindow.setContentView(view);
		mPopupWindow.showAtLocation(mainLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_image: {
			Intent intent = new Intent(this, ImageActivity.class);
			if (mGetPhotoPop.getTempFile().exists()) {
				intent.putExtra(MemberConstant.EMPLOYEE_PHOTO_URL, "file://" + mGetPhotoPop.getTempFile().getPath());
			} else {
				intent.putExtra(MemberConstant.EMPLOYEE_PHOTO_URL, mPatient.getPhotoUrl());
			}
			intent.putExtra(MemberConstant.EMPLOYEE_GENDER, mPatient.getGender());
			startActivity(intent);
			break;
		}
		case R.id.headlayout:
			if (mGetPhotoPop.isShowing()) {
				mGetPhotoPop.dismiss();
			} else {
				mGetPhotoPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.man_text:
			mPatient.setGender(1);
			TvSexValue.setText("男");
			mPopupWindow.dismiss();
			showPhoto(1);
			break;
		case R.id.girl_text:
			mPatient.setGender(2);
			TvSexValue.setText("女");
			mPopupWindow.dismiss();
			showPhoto(2);
			break;
		case R.id.marry_text:
			mPatient.setMarried(1);
			TvMarryValue.setText("已婚");
			mPopupWindow.dismiss();
			break;
		case R.id.unmarry_text:
			mPatient.setMarried(2);
			TvMarryValue.setText("单身");
			mPopupWindow.dismiss();
			break;
		case R.id.break_marry_text:
			mPatient.setMarried(3);
			TvMarryValue.setText("离异");
			mPopupWindow.dismiss();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.sexlayout:
			showPopuWindow(R.id.sexlayout);
			break;
		case R.id.marry_layout:
			showPopuWindow(R.id.marry_layout);
			break;
		case R.id.birthday_value:
		case R.id.birthlayout:
			showDialog();
			break;
		case R.id.address_layout:
			Intent intent = new Intent(this, AddressActivity.class);
			intent.putExtra(MemberConstant.LOCATION_CONTURY_ID, mPatient.getCountryID());
			intent.putExtra(MemberConstant.LOCATION_PROVINCE_ID, mPatient.getProvinceID());
			intent.putExtra(MemberConstant.LOCATION_CITY_ID, mPatient.getCityID());
			intent.putExtra(MemberConstant.LOCATION_DISTRICT_ID, mPatient.getDistrictID());
			intent.putExtra(MemberConstant.LOCATION_STREET, mPatient.getStreet());
			this.startActivityForResult(intent, MemberConstant.LOCATION_REQUEST_CODE);
			break;
		case R.id.finish:
			final Patient patientDetail = productPatientDetail();
			if (patientDetail == null) {
				return;
			}
			if (mGetPhotoPop.getTempFile().exists()) {
				updatePhoto(mGetPhotoPop.getTempFile(), mPatient.getPhotoID());
			} else {
				presenter.savePatient(patientDetail);
			}
			break;
		}
	}

	public void showPhoto(int gender) {
		if (mPatient.getPhotoSID() == 0) {
			if (gender == 1) {// 1代表男
				IvHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_man));
			} else {
				IvHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.head_lady));
			}
		}
	}

	public void showDialog() {
		Calendar calendar = Calendar.getInstance();
		if (mPatient.getDOB() != null) {
			calendar.setTime(mPatient.getDOB());
		}

		if (datePickerDialog == null) {
			datePickerDialog = new BirthdayDatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		} else {
			datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		}
		datePickerDialog.show();
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

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		TvBirthdayValue.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGetPhotoPop.getTempFile().exists()) {
			mGetPhotoPop.getTempFile().delete();
		}
	}

	@Override
	public void finishSave(final Patient patient) {
		if (isCreate) {
			Dialog dialog = new AlertDialog.Builder(PatientEditActivity.this).setMessage("保存成功，是否需要为此用户挂号")
					.setNegativeButton("挂号", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							createAppointment(patient);
						}
					}).setPositiveButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							setResult(MemberConstant.APPIONTMENT_RESOULT_CODE);
							finish();
						}
					}).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		} else {
			finish();
		}
	}

	@Override
	public void finishResult(int appiontmentResoultCode, AppointmentInfo appointmentInfo) {
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
		setResult(appiontmentResoultCode, intent);
		finish();
	}

}
