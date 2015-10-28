package com.mdground.yizhida.activity.waiting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetAppointmentInfoListByDoctor;
import com.mdground.yizhida.api.server.clinic.UpdateAppointment;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.screen.Command;
import com.mdground.yizhida.screen.ScreenManager;
import com.mdground.yizhida.util.AppointmentHelper;

public class WaitingRoomPresenterImpl implements WaitingRoomPresenter {

	WaitingRoomView mView;

	public WaitingRoomPresenterImpl(WaitingRoomView view) {
		this.mView = view;
	}

	@Override
	public void getAppointmentInfoListByDoctor(final int status, int doctorId) {
		GetAppointmentInfoListByDoctor getApp = new GetAppointmentInfoListByDoctor((Context) mView);
		getApp.getAppointmentInfoListByDoctor(status, doctorId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<AppointmentInfo> appointmentInfos = response
							.getContent(new TypeToken<List<AppointmentInfo>>() {
					});
					if ((status & AppointmentInfo.STATUS_WATTING) != 0) {
						appointmentInfos = AppointmentHelper.sortAppoint(appointmentInfos);
						appointmentInfos = AppointmentHelper.groupAppointment(status, appointmentInfos,
								new Comparator<Integer>() {

							@Override
							public int compare(Integer lhs, Integer rhs) {
								return lhs.compareTo(rhs);
							}
						});
					} else {
						appointmentInfos = AppointmentHelper.sort2Appoint(appointmentInfos);
						appointmentInfos = AppointmentHelper.groupAppointment(status, appointmentInfos,
								new Comparator<Integer>() {

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
		UpdateAppointment update = new UpdateAppointment((Context) mView);
		update.updateAppointment(appointment.getOPID(), status, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					mView.updateStatusComplete();
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

	/**
	 * 将预约排序，先按照就诊状态排序，再按照挂号id排序
	 * 
	 * @return
	 */
	private List<AppointmentInfo> sortAppoint(List<AppointmentInfo> appointments) {
		Collections.sort(appointments, new Comparator<AppointmentInfo>() {

			@Override
			public int compare(AppointmentInfo lhs, AppointmentInfo rhs) {
				Integer lStatus = 0;
				if ((lhs.getOPStatus() & AppointmentInfo.STATUS_HAS_PAID) == AppointmentInfo.STATUS_HAS_PAID) {
					lStatus = lhs.getOPStatus() | AppointmentInfo.STATUS_HAS_PAID;
				} else {
					lStatus = lhs.getOPStatus();
				}

				Integer rStatus = 0;
				if ((lhs.getOPStatus() & AppointmentInfo.STATUS_HAS_PAID) == AppointmentInfo.STATUS_HAS_PAID) {
					rStatus = rhs.getOPStatus() | AppointmentInfo.STATUS_HAS_PAID;
				} else {
					rStatus = rhs.getOPStatus();
				}

				int ret = rStatus.compareTo(lStatus);
				if (ret == 0) {
					ret = lhs.getOPNo().compareTo(rhs.getOPNo());
				}
				return ret;
			}
		});
		return appointments;
	}

	/**
	 * 已就诊和过号排序，降序
	 * 
	 * @param appointments
	 * @return
	 */
	private List<AppointmentInfo> sort2Appoint(List<AppointmentInfo> appointments) {
		Collections.sort(appointments, new Comparator<AppointmentInfo>() {

			@Override
			public int compare(AppointmentInfo lhs, AppointmentInfo rhs) {
				return rhs.getOPNo().compareTo(lhs.getOPNo());
			}
		});
		return appointments;
	}

	@Override
	public void callPatient(AppointmentInfo appointment, String doctorName) {
		if (!ScreenManager.getInstance().isConnected() || appointment.getOPStatus() != AppointmentInfo.STATUS_WATTING) { // 只有处于"候诊中"的状态才能叫
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
	public boolean isConnectScreen() {
		return ScreenManager.getInstance().isConnected();
	}
}
