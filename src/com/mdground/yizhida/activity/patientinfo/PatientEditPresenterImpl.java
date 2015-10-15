package com.mdground.yizhida.activity.patientinfo;

import java.io.File;

import org.apache.http.Header;

import android.content.Context;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveAppointment;
import com.mdground.yizhida.api.server.clinic.SavePatient;
import com.mdground.yizhida.api.server.fileserver.SaveFile;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.constant.MemberConstant;

public class PatientEditPresenterImpl implements PatientEditPresenter {

	private PatientEditView mView;

	public PatientEditPresenterImpl(PatientEditView view) {
		this.mView = view;
	}

	@Override
	public void savePatient(Patient patient) {

		SavePatient savePatient = new SavePatient((Context) mView);
		savePatient.savePatient(patient, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					Patient patient = response.getContent(Patient.class);
					mView.finishSave(patient);
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
	public void saveAppointment(AppointmentInfo appointmentInfo) {
		SaveAppointment saveAppointment = new SaveAppointment((Context) mView);
		saveAppointment.saveAppointment(appointmentInfo, new RequestCallBack() {

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
	public void uploadPhoto(Patient patient, File file) {
		SaveFile saveFile = new SaveFile((Context) mView);
		saveFile.saveFile(file, patient.getPhotoID(), new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {

				} else {
					mView.requestError(response.getCode(), response.getMessage());
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
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void uploadPhotoSID(final Patient patient, File file) {
		SaveFile saveFile = new SaveFile((Context) mView);
		saveFile.saveFile(file, patient.getPhotoSID(), new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					saveEmployeePhoto(patient.getPhotoID(), patient.getPhotoSID());
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void saveEmployeePhoto(int PhotoID, int PhotoSID) {

	}

}
