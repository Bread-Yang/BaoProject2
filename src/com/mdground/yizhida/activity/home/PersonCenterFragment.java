package com.mdground.yizhida.activity.home;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.AboutActivity;
import com.mdground.yizhida.activity.ContactActivity;
import com.mdground.yizhida.activity.ProtocolActivity;
import com.mdground.yizhida.activity.base.BaseFragment;
import com.mdground.yizhida.activity.income.IncomeActivity;
import com.mdground.yizhida.activity.login.LoginActivity;
import com.mdground.yizhida.activity.password.VerifyCodeActivity;
import com.mdground.yizhida.activity.personedit.PersonEditActivity;
import com.mdground.yizhida.activity.rota.RotaActivity;
import com.mdground.yizhida.activity.schedule.ScheduleTableActivity;
import com.mdground.yizhida.activity.screen.ConnectScreenActivity;
import com.mdground.yizhida.activity.wechat.WechatBindActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetDrugListByClinic;
import com.mdground.yizhida.api.server.clinic.GetDrugTypeList;
import com.mdground.yizhida.api.server.clinic.GetFeeTemplateList;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Drug;
import com.mdground.yizhida.bean.DrugCategory;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Fee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.DrugCategoryDao;
import com.mdground.yizhida.db.dao.DrugDao;
import com.mdground.yizhida.db.dao.FeeDao;
import com.mdground.yizhida.util.PreferenceUtils;
import com.mdground.yizhida.view.CircleImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonCenterFragment extends BaseFragment implements OnClickListener, PersonCenterView {
	private static final String TAG = PersonCenterFragment.class.getSimpleName();
	private View mainView;

	private RelativeLayout personLayout;
	private CircleImageView IvPersonIcon;
	private TextView TvPersonName;
	private TextView TvHospital;
	private TextView TvDuty;
	private TextView tvScheduling;
	private TextView tvConnectScreen;
	private TextView tv_bind_wechat;
	private TextView TvContactUs;
	private TextView TvServiceProtocol;
	private TextView TvUpdateVersion;
	private TextView TvAbout;
	private TextView TvChangePassword;
	private TextView TvExit;
	private TextView TvIncome;
	private TextView tv_sync_data;

	private Employee loginEmployee;

	private PersonCenterPresenter presenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_person_center, null);
		findView();
		initView();
		setListener();
		return mainView;
	}

	private void initView() {
		MedicalAppliction medicalAppliction = (MedicalAppliction) this.getActivity().getApplication();
		loginEmployee = medicalAppliction.getLoginEmployee();
		presenter = new PersonCenterPresenterImpl(this);
		if (loginEmployee == null) {
			return;
		}

		// if ((loginEmployee.getEmployeeRole() & Employee.DOCTOR) != 0) {
		// tvConnectScreen.setVisibility(View.VISIBLE);
		// }

		if ((loginEmployee.getEmployeeRole() & Employee.Scheduling) != 0) {
			tvScheduling.setVisibility(View.VISIBLE);
		}

		if ((loginEmployee.getEmployeeRole() & Employee.DOCTOR) != 0) {
			tv_sync_data.setVisibility(View.VISIBLE);
		} else {
			tv_sync_data.setVisibility(View.GONE);
		}

		TvPersonName.setText(loginEmployee.getEmployeeName());
		TvHospital.setText(loginEmployee.getClinicName());
		if (loginEmployee.getGender() == 1) {
			IvPersonIcon.setImageResource(R.drawable.head_man);
		} else {
			IvPersonIcon.setImageResource(R.drawable.head_lady);
		}
		IvPersonIcon.loadImage(loginEmployee.getPhotoUrl());
	}

	private void findView() {
		personLayout = (RelativeLayout) mainView.findViewById(R.id.person_layout);
		IvPersonIcon = (CircleImageView) mainView.findViewById(R.id.person_icon);
		TvPersonName = (TextView) mainView.findViewById(R.id.name);
		TvHospital = (TextView) mainView.findViewById(R.id.hospital);
		TvDuty = (TextView) mainView.findViewById(R.id.duty_title);
		TvContactUs = (TextView) mainView.findViewById(R.id.contact_us);
		TvServiceProtocol = (TextView) mainView.findViewById(R.id.service_protocol);
		TvUpdateVersion = (TextView) mainView.findViewById(R.id.update_version);
		TvAbout = (TextView) mainView.findViewById(R.id.about_yidiguan);
		TvChangePassword = (TextView) mainView.findViewById(R.id.change_password);
		TvExit = (TextView) mainView.findViewById(R.id.exit);
		TvIncome = (TextView) mainView.findViewById(R.id.income_title);
		tvScheduling = (TextView) mainView.findViewById(R.id.tv_schedule);
		tvConnectScreen = (TextView) mainView.findViewById(R.id.connect_screen_title);
		tv_bind_wechat = (TextView) mainView.findViewById(R.id.tv_bind_wechat);
		tv_sync_data = (TextView) mainView.findViewById(R.id.tv_sync_data);
	}

	private void setListener() {
		personLayout.setOnClickListener(this);
		TvDuty.setOnClickListener(this);
		TvContactUs.setOnClickListener(this);
		TvServiceProtocol.setOnClickListener(this);
		TvUpdateVersion.setOnClickListener(this);
		TvAbout.setOnClickListener(this);
		TvChangePassword.setOnClickListener(this);
		TvExit.setOnClickListener(this);
		TvIncome.setOnClickListener(this);
		tvScheduling.setOnClickListener(this);
		tvConnectScreen.setOnClickListener(this);
		tv_bind_wechat.setOnClickListener(this);
		tv_sync_data.setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		initView();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.person_layout:
			intent.setClass(getActivity(), PersonEditActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.duty_title:
			intent.setClass(getActivity(), RotaActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.tv_schedule:
			intent.setClass(getActivity(), ScheduleTableActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.connect_screen_title:
			intent.setClass(getActivity(), ConnectScreenActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.tv_bind_wechat:
			intent.setClass(getActivity(), WechatBindActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.income_title:
			intent.setClass(getActivity(), IncomeActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.tv_sync_data:
			new GetDrugListByClinic(getActivity()).getDrugListByClinic(new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					List<Drug> list = response.getContent(new TypeToken<List<Drug>>() {
					});
					
					DrugDao drugDao = DrugDao.getInstance(getActivity());
					
					drugDao.deleteAllDrugByClinicID(loginEmployee.getClinicID());
					drugDao.saveDrugs(list);
					
					Toast.makeText(getActivity(), R.string.sync_data_success, Toast.LENGTH_SHORT).show();;
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
					hidProgress();
				}
			});
			
			new GetDrugTypeList(getActivity()).getDrugTypeList(new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					List<DrugCategory> list = response.getContent(new TypeToken<List<DrugCategory>>() {
					});
					
					DrugCategoryDao drugCagegoryDao = DrugCategoryDao.getInstance(getActivity());
					
					drugCagegoryDao.deleteAllDrugCagegoriesByClinicID(loginEmployee.getClinicID());
					drugCagegoryDao.saveDrugCategories(list);
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
					hidProgress();
				}
			});
			
			new GetFeeTemplateList(getActivity()).getFeeTemplateList(new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					List<Fee> list = response.getContent(new TypeToken<List<Fee>>() {
					});
					
					FeeDao feeDao = FeeDao.getInstance(getActivity());
					
					feeDao.deleteAllFeesByClinicID(loginEmployee.getClinicID());
					feeDao.saveFees(list);
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
					hidProgress();
				}
			});
			break;
		case R.id.contact_us:
			intent.setClass(this.getActivity(), ContactActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.service_protocol:
			intent.setClass(this.getActivity(), ProtocolActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.update_version:
			break;
		case R.id.about_yidiguan:
			intent.setClass(this.getActivity(), AboutActivity.class);
			this.getActivity().startActivity(intent);
			break;
		case R.id.change_password:
			intent.setClass(this.getActivity(), VerifyCodeActivity.class);
			intent.putExtra(MemberConstant.PHONE, loginEmployee.getWorkPhone());
			this.getActivity().startActivityForResult(intent, MemberConstant.PASSWORD_REQUEST_CODE);
			break;
		case R.id.exit:
			final AlertDialog myDialog = new AlertDialog.Builder(getActivity()).setMessage("是否退出当前账号？")
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							PreferenceUtils.setPrefInt(getActivity(), MemberConstant.LOGIN_STATUS,
									MemberConstant.LOGIN_OUT);
							PreferenceUtils.setPrefString(getActivity(), MemberConstant.PASSWORD, "");
							Intent intent = new Intent();
							intent.setClass(getActivity(), LoginActivity.class);
							getActivity().startActivity(intent);
							getActivity().finish();
							presenter.loginOut();
						}
					}).setNeutralButton("取消", null).create();
			myDialog.show();
			break;
		}

	}

}