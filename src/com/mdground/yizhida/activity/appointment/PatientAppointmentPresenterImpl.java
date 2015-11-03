package com.mdground.yizhida.activity.appointment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetOfficeVisitInfo;
import com.mdground.yizhida.api.server.clinic.GetOfficeVisitList;
import com.mdground.yizhida.api.server.clinic.GetPatient;
import com.mdground.yizhida.api.server.clinic.SaveAppointment;
import com.mdground.yizhida.api.server.clinic.UpdateAppointment;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Anamnesis;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.screen.Command;
import com.mdground.yizhida.screen.ConnectStausListener;
import com.mdground.yizhida.screen.ScreenManager;

import android.content.Context;

public class PatientAppointmentPresenterImpl implements PatientAppointmentPresenter, ConnectStausListener {
	private PatientAppointmentView mView;
	private Context context;

	public PatientAppointmentPresenterImpl(PatientAppointmentView view) {
		this.mView = view;
		context = (Context) view;
	}

	@Override
	public void updateAppointment(AppointmentInfo appointment, final int status) {

		UpdateAppointment update = new UpdateAppointment(context);
		update.updateAppointment(appointment.getOPID(), status, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					mView.updateViewSatus(status);
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
	public void getPatientDetail(int patientId) {
		GetPatient getPatient = new GetPatient(context);
		getPatient.getPatient(patientId, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					Patient patient = response.getContent(Patient.class);
					mView.updateViewData(patient);
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

	@Override
	public void getPatientAppointmentDetail(int opID) {
		GetOfficeVisitInfo request = new GetOfficeVisitInfo(context);
		
		request.getOfficeVisitInfo(opID, new RequestCallBack() {
			
			@Override
			public void onSuccess(ResponseData response) {
				mView.updateAppointmentDetail(response.getContent());
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

	// 保存预约
	@Override
	public void saveAppointment(AppointmentInfo appointment) {
		SaveAppointment saveAppointment = new SaveAppointment(context);
		saveAppointment.saveAppointment(appointment, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					mView.updateViewSatus(AppointmentInfo.STATUS_WATTING);
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
	public void callPatient(AppointmentInfo appointment, String doctorName) {
		if (!ScreenManager.getInstance().isConnected()) {
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
	public void reportOpStatus(AppointmentInfo appointment, String doctorName) {
		if (!ScreenManager.getInstance().isConnected()) {
			return;
		}

		Command command = new Command();
		command.setAction(Command.ACTION_REPORT_STATUS);
		command.setClinicID(appointment.getClinicID());
		command.setDoctorID(appointment.getDoctorID());
		command.setDoctorName(doctorName);
		command.setOPID(appointment.getOPID());
		command.setOPNo(appointment.getOPNo());
		command.setOPStatus(appointment.getOPStatus());
		command.setPatientName(appointment.getPatientName());

		Gson gson = new Gson();
		ScreenManager.getInstance().sendMessage(gson.toJson(command));
	}

	@Override
	public boolean isConnected() {
		return ScreenManager.getInstance().isConnected();
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
		mView.showCallButton(true);
	}

	@Override
	public void onDisconnect() {
		mView.showCallButton(false);
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
