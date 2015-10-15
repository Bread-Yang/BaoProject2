package com.mdground.yizhida.activity.home;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.bean.DoctorWaittingCount;
import com.mdground.yizhida.api.server.clinic.GetAppointmentCountForWaiting;
import com.mdground.yizhida.api.server.global.GetDoctorList;
import com.mdground.yizhida.bean.Doctor;

public class NurseHomePresenterImpl implements NurseHomePresenter {

	NurseHomeView mView;
	Context context;

	public NurseHomePresenterImpl(NurseHomeView mView) {
		this.mView = mView;
		if (mView instanceof Fragment) {
			Fragment frg = (Fragment) mView;
			this.context = frg.getActivity();
		}
	}

	@Override
	public void getDoctorList() {
		GetDoctorList getDoctorList = new GetDoctorList(context);
		getDoctorList.getDoctorList(new RequestCallBack() {

			@Override
			public void onStart() {
				mView.showProgress();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onFinish() {
				mView.hidProgress();
				mView.refreshComplete();
			}

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<Doctor> doctors = response.getContent(new TypeToken<List<Doctor>>() {
					});
					mView.updateDoctorListView(doctors);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}
		});

	}

	@Override
	public void getWaitingCountForDoctorList(List<Doctor> doctorList) {
		GetAppointmentCountForWaiting getWaiting = new GetAppointmentCountForWaiting(context);
		getWaiting.getAppointmentCountForWaiting(doctorList, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<DoctorWaittingCount> waittingCount = response.getContent(new TypeToken<List<DoctorWaittingCount>>() {
					});
					mView.updateWaitingCount(waittingCount);
				} else {
					mView.requestError(response.getCode(), response.getMessage());
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.showToast(R.string.request_error);
			}
		});
	}
}
