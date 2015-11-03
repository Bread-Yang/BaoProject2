package com.mdground.yizhida.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VitalSigns implements Parcelable {

	private int VisitID;
	private int ClinicID;
	private int DoctorID;
	private int PatientID;
	private float BodyTemperature;
	private int Weight;
	private int Height;
	private int Heartbeat;
	private int Breathing;
	private int BloodGlucose;
	private float BMI;
	private int SystolicPressure;
	private int DiastolicBloodPressure;
	
	public VitalSigns() {
	}

	public int getVisitID() {
		return VisitID;
	}

	public void setVisitID(int visitID) {
		VisitID = visitID;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
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

	public float getBodyTemperature() {
		return BodyTemperature;
	}

	public void setBodyTemperature(float bodyTemperature) {
		BodyTemperature = bodyTemperature;
	}

	public int getWeight() {
		return Weight;
	}

	public void setWeight(int weight) {
		Weight = weight;
	}

	public int getHeight() {
		return Height;
	}

	public void setHeight(int height) {
		Height = height;
	}

	public int getHeartbeat() {
		return Heartbeat;
	}

	public void setHeartbeat(int heartbeat) {
		Heartbeat = heartbeat;
	}

	public int getBreathing() {
		return Breathing;
	}

	public void setBreathing(int breathing) {
		Breathing = breathing;
	}

	public int getBloodGlucose() {
		return BloodGlucose;
	}

	public void setBloodGlucose(int bloodGlucose) {
		BloodGlucose = bloodGlucose;
	}

	public float getBMI() {
		return BMI;
	}

	public void setBMI(float bMI) {
		BMI = bMI;
	}

	public int getSystolicPressure() {
		return SystolicPressure;
	}

	public void setSystolicPressure(int systolicPressure) {
		SystolicPressure = systolicPressure;
	}

	public int getDiastolicBloodPressure() {
		return DiastolicBloodPressure;
	}

	public void setDiastolicBloodPressure(int diastolicBloodPressure) {
		DiastolicBloodPressure = diastolicBloodPressure;
	}

	public VitalSigns(Parcel source) {
		this.VisitID = source.readInt();
		this.ClinicID = source.readInt();
		this.DoctorID = source.readInt();
		this.PatientID = source.readInt();
		this.BodyTemperature = source.readFloat();
		this.Weight = source.readInt();
		this.Height = source.readInt();
		this.Heartbeat = source.readInt();
		this.Breathing = source.readInt();
		this.BloodGlucose = source.readInt();
		this.BMI = source.readFloat();
		this.SystolicPressure = source.readInt();
		this.DiastolicBloodPressure = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(VisitID);
		dest.writeInt(ClinicID);
		dest.writeInt(DoctorID);
		dest.writeInt(PatientID);
		dest.writeFloat(BodyTemperature);
		dest.writeInt(Weight);
		dest.writeInt(Height);
		dest.writeInt(Heartbeat);
		dest.writeInt(Breathing);
		dest.writeInt(BloodGlucose);
		dest.writeFloat(BMI);
		dest.writeInt(SystolicPressure);
		dest.writeInt(DiastolicBloodPressure);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<VitalSigns> CREATOR = new Parcelable.Creator<VitalSigns>() {

		@Override
		public VitalSigns createFromParcel(Parcel source) {
			return new VitalSigns(source);
		}

		@Override
		public VitalSigns[] newArray(int size) {
			return new VitalSigns[size];
		}
	};
}
