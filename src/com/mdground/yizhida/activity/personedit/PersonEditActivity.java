package com.mdground.yizhida.activity.personedit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.http.Header;

import com.baidu.android.common.logging.Log;
import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.AddressActivity;
import com.mdground.yizhida.activity.ImageActivity;
import com.mdground.yizhida.activity.QrcodeActivity;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.fileserver.SaveFile;
import com.mdground.yizhida.api.server.global.SaveEmployee;
import com.mdground.yizhida.api.server.global.SaveEmployeePhoto;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.util.GetPhotoPop;
import com.mdground.yizhida.util.LocationUtils;
import com.mdground.yizhida.util.ToolPicture;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonEditActivity extends BaseActivity implements OnClickListener {

	private TextView TvPersonName;
	private TextView TvAccount;
	private TextView TvAddress;
	private TextView TvSex;
	private TextView TvBirthday;
	private TextView TvIdCard;
	private TextView TvPracticeCode;
	private TextView TvPracticeCategory;
	private TextView TvPracticeScope;
	private TextView TvCollege;
	private TextView TvProfession;
	private TextView TvEducation;
	private TextView TvResume;

	private RelativeLayout headlayout;
	private RelativeLayout namelayout;
	private RelativeLayout addresslayout;
	private RelativeLayout sexlayout;
	private RelativeLayout birthlayout;
	private RelativeLayout collegelayout;
	private RelativeLayout professionlayout;
	private RelativeLayout resumelayout;
	private RelativeLayout qrcodelayout;

	private ImageView IvBack;
	private CircleImageView headImage;
	private Employee employee;

	private GetPhotoPop mGetPhotoPop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_edit_layout);
		findView();
		initMemberData();
		setListener();
		initView();
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
		TvPersonName = (TextView) this.findViewById(R.id.name_value);
		TvAccount = (TextView) this.findViewById(R.id.account_value);
		TvAddress = (TextView) this.findViewById(R.id.address_value);
		TvSex = (TextView) this.findViewById(R.id.sex_value);
		TvBirthday = (TextView) this.findViewById(R.id.birth_value);
		TvIdCard = (TextView) this.findViewById(R.id.id_card);
		TvPracticeCode = (TextView) this.findViewById(R.id.practice_code_value);
		TvPracticeCategory = (TextView) this.findViewById(R.id.practice_category_value);
		TvPracticeScope = (TextView) this.findViewById(R.id.practice_scope_value);
		TvCollege = (TextView) this.findViewById(R.id.college_value);
		TvProfession = (TextView) this.findViewById(R.id.profession_value);
		TvEducation = (TextView) this.findViewById(R.id.education_value);
		TvResume = (TextView) this.findViewById(R.id.resume_value);

		IvBack = (ImageView) this.findViewById(R.id.back);
		headImage = (CircleImageView) this.findViewById(R.id.head_img);

		headlayout = (RelativeLayout) this.findViewById(R.id.headlayout);
		namelayout = (RelativeLayout) this.findViewById(R.id.namelayout);
		addresslayout = (RelativeLayout) this.findViewById(R.id.addresslayout);
		sexlayout = (RelativeLayout) this.findViewById(R.id.sexlayout);
		birthlayout = (RelativeLayout) this.findViewById(R.id.birthlayout);
		collegelayout = (RelativeLayout) this.findViewById(R.id.collegelayout);
		professionlayout = (RelativeLayout) this.findViewById(R.id.professionlayout);
		resumelayout = (RelativeLayout) this.findViewById(R.id.resumelayout);
		qrcodelayout = (RelativeLayout) findViewById(R.id.erweimalayout);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initView();
	}

	@Override
	public void initView() {
		MedicalAppliction medicalAppliction = (MedicalAppliction) this.getApplication();
		employee = medicalAppliction.getLoginEmployee();

		if (employee != null && employee.getGender() == 1) {// 1代表男
			headImage.setImageResource(R.drawable.head_man);
		} else {
			headImage.setImageResource(R.drawable.head_lady);
		}

		if (employee.getPhotoSID() > 0) {
			headImage.loadImage(employee.getPhotoUrl());
		}
		TvPersonName.setText(employee.getEmployeeName());
		TvAccount.setText(employee.getLoginID());
		// 使用组合
		StringBuffer sb = new StringBuffer();
		if (employee.getProvinceID() != -1) {
			sb.append(LocationUtils.getInstance(this).getNameFromId(employee.getProvinceID()));
		}
		if (employee.getCityID() != -1) {
			sb.append(LocationUtils.getInstance(this).getNameFromId(employee.getCityID()));
		}
		if (employee.getStreet() != null) {
			sb.append(employee.getStreet());
		}
		TvAddress.setText(sb.toString());
		TvSex.setText(employee.getGenderStr());
		TvBirthday
				.setText(DateUtils.getDateString(employee.getDOB(), new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)));
		TvIdCard.setText(employee.getIDCard());
		if (employee.getZhiYeNo() == null || employee.getZhiYeNo().equals("")) {
			TvPracticeCode.setText("未知");
		} else {
			TvPracticeCode.setText(employee.getZhiYeNo());
		}
		TvPracticeCategory.setText(employee.getZhiYeType());
		TvPracticeScope.setText(employee.getZhiYeRange());
		TvCollege.setText(employee.getGraduateSchool());
		TvProfession.setText(employee.getSpecialtyName());
		TvEducation.setText(employee.getEMRType());
		TvResume.setText(employee.getResume());
	}

	@Override
	public void initMemberData() {
		// TODO Auto-generated method stub
		mGetPhotoPop = new GetPhotoPop(this);
	}

	@Override
	public void setListener() {
		IvBack.setOnClickListener(this);
		headlayout.setOnClickListener(this);
		namelayout.setOnClickListener(this);
		addresslayout.setOnClickListener(this);
		sexlayout.setOnClickListener(this);
		birthlayout.setOnClickListener(this);
		collegelayout.setOnClickListener(this);
		professionlayout.setOnClickListener(this);
		resumelayout.setOnClickListener(this);
		qrcodelayout.setOnClickListener(this);
		headImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.head_img:
			Intent headImageIntent = new Intent(this, ImageActivity.class);
			headImageIntent.putExtra(MemberConstant.EMPLOYEE_PHOTO_URL, employee.getPhotoUrl());
			headImageIntent.putExtra(MemberConstant.EMPLOYEE_GENDER, employee.getGender());
			startActivity(headImageIntent);
			break;
		case R.id.headlayout:
			if (mGetPhotoPop.isShowing()) {
				mGetPhotoPop.dismiss();
			} else {
				mGetPhotoPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.namelayout:
			Intent nameIntent = new Intent(this, PersonEditInfoActivity.class);
			nameIntent.putExtra(MemberConstant.EMPLOYEE_UPDATE, MemberConstant.UPDATE_NAME);
			this.startActivity(nameIntent);
			break;
		case R.id.sexlayout:
			Intent sexIntent = new Intent(this, PersonEditInfoActivity.class);
			sexIntent.putExtra(MemberConstant.EMPLOYEE_UPDATE, MemberConstant.UPDATE_SEX);
			startActivityForResult(sexIntent, MemberConstant.EMPLOYEE_GENDER_REQUEST);
			break;
		case R.id.addresslayout:
			Intent intent = new Intent(this, AddressActivity.class);
			intent.putExtra(MemberConstant.LOCATION_CONTURY_ID, employee.getCountryID());
			intent.putExtra(MemberConstant.LOCATION_PROVINCE_ID, employee.getProvinceID());
			intent.putExtra(MemberConstant.LOCATION_CITY_ID, employee.getCityID());
			intent.putExtra(MemberConstant.LOCATION_DISTRICT_ID, employee.getDistrictID());
			intent.putExtra(MemberConstant.LOCATION_STREET, employee.getStreet());
			this.startActivityForResult(intent, MemberConstant.LOCATION_REQUEST_CODE);

			break;
		case R.id.birthlayout:
			Intent birthIntent = new Intent(this, PersonEditInfoActivity.class);
			birthIntent.putExtra(MemberConstant.EMPLOYEE_UPDATE, MemberConstant.UPDATE_BIRTHDAY);
			this.startActivity(birthIntent);
			break;
		case R.id.collegelayout:
			Intent collegeIntent = new Intent(this, PersonEditInfoActivity.class);
			collegeIntent.putExtra(MemberConstant.EMPLOYEE_UPDATE, MemberConstant.UPDATE_GRADUATE_SCHOOL);
			this.startActivity(collegeIntent);
			break;
		case R.id.resumelayout:
			Intent resumeIntent = new Intent(this, PersonEditInfoActivity.class);
			resumeIntent.putExtra(MemberConstant.EMPLOYEE_UPDATE, MemberConstant.UPDATE_RESUME);
			this.startActivity(resumeIntent);
			break;
		case R.id.professionlayout:
			Intent professionIntent = new Intent(this, PersonEditInfoActivity.class);
			professionIntent.putExtra(MemberConstant.EMPLOYEE_UPDATE, MemberConstant.UPDATE_SPECIALTY_NAME);
			this.startActivity(professionIntent);
			break;
		case R.id.erweimalayout:
			Intent qrcodeIntent = new Intent(this, QrcodeActivity.class);
			qrcodeIntent.putExtra(MemberConstant.EMPLOYEE_QRCODE, employee.getQRCode());
			qrcodeIntent.putExtra(MemberConstant.EMPLOYEE_PHOTO_URL, employee.getPhotoUrl());
			qrcodeIntent.putExtra(MemberConstant.EMPLOYEE_NAME, employee.getEmployeeName());
			qrcodeIntent.putExtra(MemberConstant.EMPLOYEE_CLINIC_NAME, employee.getClinicName());
			this.startActivity(qrcodeIntent);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (MemberConstant.LOCATION_REQUEST_CODE == requestCode && resultCode == MemberConstant.LOCATION_RESOULT_CODE
				&& intent != null) {
			employee.setCountryID(intent.getIntExtra(MemberConstant.LOCATION_CONTURY_ID, 86));
			employee.setCityID(intent.getIntExtra(MemberConstant.LOCATION_CITY_ID, -1));
			employee.setProvinceID(intent.getIntExtra(MemberConstant.LOCATION_PROVINCE_ID, -1));
			employee.setDistrictID(intent.getIntExtra(MemberConstant.LOCATION_DISTRICT_ID, -1));
			employee.setStreet(intent.getStringExtra(MemberConstant.LOCATION_STREET));
			employee.setAddress(intent.getStringExtra(MemberConstant.LOCATION_ADDRESS));
			TvAddress.setText(employee.getAddress());
			saveEmployee();
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
		} else if (requestCode == GetPhotoPop.PHOTO_REQUEST_CUT) {
			if (intent != null) {
				final Bitmap bitmap = intent.getParcelableExtra("data");
				headImage.post(new Runnable() {
					@Override
					public void run() {
						headImage.setImageBitmap(bitmap);
					}
				});
				headImage.setImageBitmap(bitmap);

			}
			if (mGetPhotoPop.getTempFile().exists()) {
				updatePhoto(mGetPhotoPop.getTempFile(), employee.getPhotoID());
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
					employee.setPhotoSID(photoID);
					employee.setPhotoID(photoID);
					saveEmployeePhoto();
				} else {
					Toast.makeText(PersonEditActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(PersonEditActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGetPhotoPop.getTempFile().exists()) {
			mGetPhotoPop.getTempFile().delete();
		}
	}

	// 更新用户图片
	private void saveEmployeePhoto() {
		SaveEmployeePhoto savePhoto = new SaveEmployeePhoto(this);
		savePhoto.saveEmployeePhoto(employee.getPhotoID(), employee.getPhotoSID(), new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					ImageLoader.getInstance().clearDiskCache();
					ImageLoader.getInstance().clearMemoryCache();
					headImage.loadImage(employee.getPhotoUrl());
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

	private void saveEmployee() {
		SaveEmployee saveEmployee = new SaveEmployee(this);
		saveEmployee.saveEmployee(employee, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					showToast("保存成功");
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
				showToast("保存失败");

			}
		});
	}

}
