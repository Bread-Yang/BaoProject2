package com.mdground.yizhida.screen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 命令实体
 * 
 * @author qinglong.huang
 * 
 */
public class Command implements Parcelable {
	public static final int ACTION_CALL = 1;
	public static final int ACTION_REPORT_STATUS = 2;
	public static final int ACTION_VAILD = 3;

	// Action 1=叫号 2=状态上报(开始、结束预约)，3=验证连接诊所
	private int Action;
	private int ClinicID;
	private String PatientName;
	private int OPNo;
	private String DoctorName;
	private int OPID;
	private int DoctorID;
	private int OPStatus;

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public String getPatientName() {
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public int getOPNo() {
		return OPNo;
	}

	public void setOPNo(int oPNo) {
		OPNo = oPNo;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public int getAction() {
		return Action;
	}

	public void setAction(int action) {
		Action = action;
	}

	public int getOPID() {
		return OPID;
	}

	public void setOPID(int oPID) {
		OPID = oPID;
	}

	public int getDoctorID() {
		return DoctorID;
	}

	public void setDoctorID(int doctorID) {
		DoctorID = doctorID;
	}

	public int getOPStatus() {
		return OPStatus;
	}

	public void setOPStatus(int oPStatus) {
		OPStatus = oPStatus;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public Command(Parcel source) {
		this.Action = source.readInt();
		this.ClinicID = source.readInt();
		this.PatientName = source.readString();
		this.OPNo = source.readInt();
		this.DoctorName = source.readString();
		this.OPID = source.readInt();
		this.DoctorID = source.readInt();
		this.OPStatus = source.readInt();
	}

	public Command() {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Action);
		dest.writeInt(ClinicID);
		dest.writeString(PatientName);
		dest.writeInt(OPNo);
		dest.writeString(DoctorName);
		dest.writeInt(OPID);
		dest.writeInt(DoctorID);
		dest.writeInt(OPStatus);
	}

	public static final Parcelable.Creator<Command> CREATOR = new Parcelable.Creator<Command>() {

		@Override
		public Command createFromParcel(Parcel source) {
			return new Command(source);
		}

		@Override
		public Command[] newArray(int size) {
			return new Command[size];
		}
	};

}
