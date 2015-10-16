package com.mdground.yizhida.activity.home;

import java.util.Comparator;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetAppointmentInfoListByDoctor;
import com.mdground.yizhida.api.server.clinic.UpdateAppointment;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.screen.Command;
import com.mdground.yizhida.screen.ConnectStausListener;
import com.mdground.yizhida.screen.ScreenManager;
import com.mdground.yizhida.util.AppointmentHelper;

public class DoctorHomePresenterImpl implements DoctorHomePresenter, ConnectStausListener {
	DoctorHomeView mView;
	Context context;

	public DoctorHomePresenterImpl(DoctorHomeView view) {
		this.mView = view;
		Fragment fragment = (Fragment) view;
		this.context = fragment.getActivity();
	}

	@Override
	public void getAppointmentCountForDoctor() {

	}

	@Override
	public void getAppointmentInfoListByDoctor(final int status, int doctorId) {
		GetAppointmentInfoListByDoctor getApp = new GetAppointmentInfoListByDoctor(context);
		getApp.getAppointmentInfoListByDoctor(status, doctorId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
//					L.e(this, "getAppointmentInfoListByDoctor response.getContent : " + response.getContent());
					List<AppointmentInfo> appointmentInfos = response.getContent(new TypeToken<List<AppointmentInfo>>() {
					});
					if ((status & AppointmentInfo.STATUS_WATTING) != 0) {
						appointmentInfos = AppointmentHelper.sortAppoint(appointmentInfos);
						appointmentInfos = AppointmentHelper.groupAppointment(appointmentInfos, new Comparator<Integer>() {
 
							@Override
							public int compare(Integer lhs, Integer rhs) {
								return lhs.compareTo(rhs);
							}
						});
					} else {
						appointmentInfos = AppointmentHelper.sort2Appoint(appointmentInfos);
						appointmentInfos = AppointmentHelper.groupAppointment(appointmentInfos, new Comparator<Integer>() {

							@Override
							public int compare(Integer lhs, Integer rhs) {
								return rhs.compareTo(lhs);
							}
						});
					}

					mView.refresh(status, appointmentInfos);
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
				mView.refreshComplete();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mView.refresh(status, null);
			}
		});
	}

	@Override
	public void updateAppointmentByAssign() {

	}

	@Override
	public void updateAppointment(AppointmentInfo appointment, int status) {
		UpdateAppointment update = new UpdateAppointment(context);
		update.updateAppointment(appointment.getOPID(), status, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					mView.updateComplete();
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

	/**
	 * 是否连接到导诊屏
	 */
	@Override
	public boolean isConnectScreen() {
		return ScreenManager.getInstance().isConnected();
	}

	/**
	 * 叫号
	 */
	@Override
	public void callPatient(AppointmentInfo appointment, String doctorName) {
		if (!ScreenManager.getInstance().isConnected()) {
			// mView.showToast("请重新连接导诊屏");
			return;
		}

		Command command = new Command();
		command.setAction(Command.ACTION_CALL);
		command.setClinicID(appointment.getClinicID());
		command.setDoctorID(appointment.getDoctorID());
		command.setDoctorName(doctorName);
		command.setOPID(appointment.getOPID());
		command.setOPNo(appointment.getOPNo());
		command.setOPStatus(appointment.getOPStatus());
		command.setPatientName(appointment.getPatientName());

		Gson gson = new Gson();
		ScreenManager.getInstance().sendMessage(gson.toJson(command));
		mView.showToast("正在叫号...");

	}

	@Override
	public void onConnected() {
		mView.refreshView();
	}

	@Override
	public void onDisconnect() {
		mView.refreshView();
	}

	@Override
	public void onReceive(String msg) {

	}

	@Override
	public void onConnecting() {

	}

	@Override
	public void onConnecFailed() {
	}

	@Override
	public void addScreenCallBack() {
		ScreenManager.getInstance().addConnectListener(this);
	}

	@Override
	public void removeScreenCallBack() {
		ScreenManager.getInstance().removeConnectListener(this);
	}

}
