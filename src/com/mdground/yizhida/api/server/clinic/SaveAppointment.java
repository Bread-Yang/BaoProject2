package com.mdground.yizhida.api.server.clinic;

import java.util.Date;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Employee;

public class SaveAppointment extends ClinicRequest {
	private static final String FUNCTION_NAME = "SaveAppointment";

	public SaveAppointment(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	// 保存预约
	public void saveAppointment(AppointmentInfo appointmentInfo, RequestCallBack callBack) {
		if (appointmentInfo == null) {
			return;
		}
		setRequestCallBack(callBack);
		MdgAppliction appliction = null;
		if (mContext != null && mContext.getApplicationContext() instanceof MdgAppliction) {
			appliction = (MdgAppliction) mContext.getApplicationContext();
		} else {
			throw new RuntimeException("please create MdgAppliction !!!");
		}

		if (appliction == null) {
			return;
		}

		Employee loginEmployee = appliction.getLoginEmployee();
		Appointment appointment = new Appointment(appointmentInfo, loginEmployee.getEmployeeID(), loginEmployee.getEmployeeRole());
		AppointmentQuery query = new AppointmentQuery();
		query.Appointment = appointment;
		query.Appointment.ClinicID = loginEmployee.getClinicID();
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String queryStr = gson.toJson(query);

		RequestData data = getData();
		data.setQueryData(queryStr);

		pocess();
	}

	private class AppointmentQuery {
		Appointment Appointment;

	}

	protected class Appointment {
		private int DoctorID;
		private String DoctorName;
		private int PatientID;
		private Date OPDate;
		private int OPStatus;
		private int OPType;
		private String Remark;
		private String OPEMR;
		private float Lat;
		private float Lng;
		private int ClinicID;
		private int CreatedBy;
		private int CreatedRole;

		public Appointment(AppointmentInfo appointmentInfo, int createBy, int createRole) {
			this.DoctorID = appointmentInfo.getDoctorID();
			this.DoctorName = appointmentInfo.getDoctorName();
			this.PatientID = appointmentInfo.getPatientID();
			this.OPDate = appointmentInfo.getOPDate();
			this.OPStatus = AppointmentInfo.STATUS_WATTING;
			this.OPType = 2;
			this.Remark = appointmentInfo.getRemark();
			this.OPEMR = appointmentInfo.getOPEMR();
			this.Lat = 123;
			this.Lng = 123;
			this.ClinicID = appointmentInfo.getClinicID();
			this.CreatedBy = createBy;
			this.CreatedRole = createRole;
		}

		public int getDoctorID() {
			return DoctorID;
		}

		public void setDoctorID(int doctorID) {
			DoctorID = doctorID;
		}

		public String getDoctorName() {
			return DoctorName;
		}

		public void setDoctorName(String doctorName) {
			DoctorName = doctorName;
		}

		public int getPatientID() {
			return PatientID;
		}

		public void setPatientID(int patientID) {
			PatientID = patientID;
		}

		public Date getOPDate() {
			return OPDate;
		}

		public void setOPDate(Date oPDate) {
			OPDate = oPDate;
		}

		public int getOPStatus() {
			return OPStatus;
		}

		public void setOPStatus(int oPStatus) {
			OPStatus = oPStatus;
		}

		public int getOPType() {
			return OPType;
		}

		public void setOPType(int oPType) {
			OPType = oPType;
		}

		public String getRemark() {
			return Remark;
		}

		public void setRemark(String remark) {
			Remark = remark;
		}

		public String getOPEMR() {
			return OPEMR;
		}

		public void setOPEMR(String oPEMR) {
			OPEMR = oPEMR;
		}

		public float getLat() {
			return Lat;
		}

		public void setLat(float lat) {
			Lat = lat;
		}

		public float getLng() {
			return Lng;
		}

		public void setLng(float lng) {
			Lng = lng;
		}

		public int getClinicID() {
			return ClinicID;
		}

		public void setClinicID(int clinicID) {
			ClinicID = clinicID;
		}

		public int getCreatedBy() {
			return CreatedBy;
		}

		public void setCreatedBy(int createdBy) {
			CreatedBy = createdBy;
		}

		public int getCreatedRole() {
			return CreatedRole;
		}

		public void setCreatedRole(int createdRole) {
			CreatedRole = createdRole;
		}

	}
}
