package com.mdground.yizhida.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OfficeVisitFee implements Parcelable {
	
	private int ClinicID;
	
	private int FeeID;
	
	private int VisitID;
	
	private int DoctorID;
	
	private int PatientID;

	private String FeeType;
	
	private int TotalFee;

	public OfficeVisitFee() {
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getFeeID() {
		return FeeID;
	}

	public void setFeeID(int feeID) {
		FeeID = feeID;
	}

	public int getVisitID() {
		return VisitID;
	}

	public void setVisitID(int visitID) {
		VisitID = visitID;
	}

	public int getDoctorID() {
		return DoctorID;
	}

	public void setDoctorID(int doctorID) {
		DoctorID = doctorID;
	}

	public int getPatientID() {
		return PatientID;
	}

	public void setPatientID(int patientID) {
		PatientID = patientID;
	}

	public String getFeeType() {
		return FeeType;
	}

	public void setFeeType(String feeType) {
		FeeType = feeType;
	}

	public int getTotalFee() {
		return TotalFee;
	}

	public void setTotalFee(int totalFee) {
		TotalFee = totalFee;
	}
	
	public OfficeVisitFee(Parcel source) {
		this.ClinicID = source.readInt();
		this.FeeID = source.readInt();
		this.VisitID = source.readInt();
		this.DoctorID = source.readInt();
		this.PatientID = source.readInt();
		this.FeeType = source.readString();
		this.TotalFee = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ClinicID);
		dest.writeInt(FeeID);
		dest.writeInt(VisitID);
		dest.writeInt(DoctorID);
		dest.writeInt(PatientID);
		dest.writeString(FeeType);
		dest.writeInt(TotalFee);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<OfficeVisitFee> CREATOR = new Parcelable.Creator<OfficeVisitFee>() {

		@Override
		public OfficeVisitFee createFromParcel(Parcel source) {
			return new OfficeVisitFee(source);
		}

		@Override
		public OfficeVisitFee[] newArray(int size) {
			return new OfficeVisitFee[size];
		}
	};
}
