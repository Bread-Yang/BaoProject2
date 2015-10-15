package com.mdground.yizhida.activity.patientinfo;

import org.apache.http.Header;

import android.content.Context;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetPatient;
import com.mdground.yizhida.api.server.clinic.SaveAppointment;
import com.mdground.yizhida.api.server.clinic.UpdateAppointment;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.constant.MemberConstant;

public class PatientDetailPresenterImpl implements PatientDetailPresenter {

	private PatientDetailView mView;

	public PatientDetailPresenterImpl(PatientDetailView mView) {
		this.mView = mView;
	}

	/**
	 * 獲取病人詳情
	 */
	@Override
	public void getPatient(int patientId) {
		GetPatient getPatient = new GetPatient((Context) mView);
		getPatient.getPatient(patientId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					Patient patient = response.getContent(Patient.class);
					mView.showPaitent(patient);
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

			}
		});
	}

	@Override
	public void updateAppointment(AppointmentInfo appointment, int status) {

		UpdateAppointment update = new UpdateAppointment((Context) mView);
		update.updateAppointment(appointment.getOPID(), status, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					// mView.updateView(status);
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

			}
		});

	}

	@Override
	public void saveAppointment(AppointmentInfo appointment) {
		SaveAppointment saveAppointment = new SaveAppointment((Context) mView);
		saveAppointment.saveAppointment(appointment, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					AppointmentInfo appointmentInfo = response.getContent(AppointmentInfo.class);
					mView.finishResult(MemberConstant.APPIONTMENT_RESOULT_CODE, appointmentInfo);
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
