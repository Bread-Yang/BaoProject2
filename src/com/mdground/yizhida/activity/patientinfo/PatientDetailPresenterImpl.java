package com.mdground.yizhida.activity.patientinfo;

import java.util.Collections;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.appointment.PatientAppointmentPresenterImpl;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetOfficeVisitList;
import com.mdground.yizhida.api.server.clinic.GetPatient;
import com.mdground.yizhida.api.server.clinic.SaveAppointment;
import com.mdground.yizhida.api.server.clinic.UpdateAppointment;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.constant.MemberConstant;

public class PatientDetailPresenterImpl implements PatientDetailPresenter {

	private PatientDetailView mView;
	private Context context;

	public PatientDetailPresenterImpl(PatientDetailView mView) {
		this.mView = mView;
		context = (Context) mView;
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

	@Override
	public void getPatientMedicalHistory(int patientId) {
		
		GetOfficeVisitList request = new GetOfficeVisitList(context);

		request.getOfficeVisitList(patientId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				L.e(PatientDetailPresenterImpl.this, "既往史的message : " + response.getContent());
				List<Anamnesis> list = response.getContent(new TypeToken<List<Anamnesis>>() {
				});
				
				Collections.sort(list);
				
				mView.updateAnamnesisData(list);
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
