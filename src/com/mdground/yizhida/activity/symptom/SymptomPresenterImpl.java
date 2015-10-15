package com.mdground.yizhida.activity.symptom;

import org.apache.http.Header;

import android.content.Context;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveAppointment;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.constant.MemberConstant;

public class SymptomPresenterImpl implements SymptomPresenter {
	SymptomView mView;

	public SymptomPresenterImpl(SymptomView view) {
		this.mView = view;
	}

	@Override
	public void getChiefComplaintTemplateList() {

	}

	@Override
	public void saveAppointment(final AppointmentInfo appointment) {
		SaveAppointment saveAppointment = new SaveAppointment((Context) mView);
		saveAppointment.saveAppointment(appointment, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					AppointmentInfo appointmentInfo = response.getContent(AppointmentInfo.class);
					appointmentInfo.setOPEMR(appointment.getOPEMR());
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
