package com.mdground.yizhida.activity.searchpatient;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveAppointment;
import com.mdground.yizhida.api.server.clinic.SearchPatientList;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.constant.MemberConstant;

public class SearchPatientPresenterImpl implements SearchPatientPresenter {

	SearchPatientView mView;

	public SearchPatientPresenterImpl(SearchPatientView view) {
		this.mView = view;
	}

	@Override
	public void searchPatient(String keyword) {
		SearchPatientList searchPatient = new SearchPatientList((Context) mView);
		searchPatient.searchPatient(keyword, new RequestCallBack() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onSuccess(ResponseData response) {
				if (response != null && response.getCode() == ResponseCode.Normal.getValue()) {
					List<Patient> patients = response.getContent(new TypeToken<List<Patient>>() {
					});
					mView.updateResult(patients);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}
		});

	}

	@Override
	public void saveAppointment(AppointmentInfo appointmentInfo) {

		SaveAppointment saveAppointment = new SaveAppointment((Context) mView);
		saveAppointment.saveAppointment(appointmentInfo, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					AppointmentInfo appointment = response.getContent(AppointmentInfo.class);
					mView.finishResult(MemberConstant.APPIONTMENT_RESOULT_CODE, appointment);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {
				mView.showProgress();
			}

			@Override
			public void onFinish() {
				mView.hidProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}
		});

	}

}
