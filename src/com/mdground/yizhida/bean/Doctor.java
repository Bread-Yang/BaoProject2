package com.mdground.yizhida.bean;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 医生实体
 * 
 * @author Vincent
 * 
 */
@Table(name = "Doctor")
public class Doctor implements Parcelable {

	@SerializedName("ClinicID")
	@Expose
	private long ClinicID;

	@SerializedName("EmployeeID")
	@Expose
	private long EmployeeID;
	
	@SerializedName("EmployeeName")
	@Expose
	private String EmployeeName;
	
	@SerializedName("Gender")
	@Expose
	private int Gender;
	
	@SerializedName("PhotoSID")
	@Expose
	private long PhotoSID;
	
	@SerializedName("PhotoURL")
	@Expose
	private String PhotoURL;
	
	@SerializedName("UpdatedTime")
	@Expose
	private Date UpdatedTime;
	
	@SerializedName("WorkStatus")
	@Expose
	private long WorkStatus;
	
	@SerializedName("DoctorID")
	@Expose
	@Id(column = "id")
	private int DoctorID;
	
	@SerializedName("DoctorName")
	@Expose
	private String DoctorName;
	
	@SerializedName("EMRType")
	@Expose
	private String EMRType;

	@Transient
	private int waittingCount;

	@Transient
	private boolean isSelected = false;

	public Doctor() {
		// TODO Auto-generated constructor stub
	}

	public long getClinicID() {
		return ClinicID;
	}



	public void setClinicID(long clinicID) {
		this.ClinicID = clinicID;
	}



	public long getEmployeeID() {
		return EmployeeID;
	}



	public void setEmployeeID(long employeeID) {
		EmployeeID = employeeID;
	}



	public String getEmployeeName() {
		return EmployeeName;
	}



	public void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}



	public int getGender() {
		return Gender;
	}



	public void setGender(int gender) {
		Gender = gender;
	}



	public long getPhotoSID() {
		return PhotoSID;
	}



	public void setPhotoSID(long photoSID) {
		PhotoSID = photoSID;
	}



	public String getPhotoURL() {
		return PhotoURL;
	}
	
	public String getPhotoSIDURL() {
		return "image://" + ClinicID + "." + PhotoSID;
	}



	public void setPhotoURL(String photoURL) {
		PhotoURL = photoURL;
	}



	public Date getUpdatedTime() {
		return UpdatedTime;
	}



	public void setUpdatedTime(Date updatedTime) {
		UpdatedTime = updatedTime;
	}



	public long getWorkStatus() {
		return WorkStatus;
	}



	public void setWorkStatus(long workStatus) {
		WorkStatus = workStatus;
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



	public String getEMRType() {
		return EMRType;
	}



	public void setEMRType(String eMRType) {
		EMRType = eMRType;
	}



	public Doctor(Parcel source) {
		this.EmployeeID = source.readLong();
		this.EmployeeName = source.readString();
		this.ClinicID = source.readLong();
		this.DoctorID = source.readInt();
		this.DoctorName = source.readString();
		this.EMRType = source.readString();
		this.Gender = source.readInt();
		this.PhotoSID = source.readLong();
		this.PhotoURL = source.readString();
		long tmpDOB = source.readLong();
		this.UpdatedTime = tmpDOB == -1 ? null : new Date(tmpDOB);
		this.WorkStatus = source.readLong();
		this.waittingCount = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.EmployeeID);
		dest.writeString(this.EmployeeName);
		dest.writeLong(ClinicID);
		dest.writeInt(DoctorID);
		dest.writeString(DoctorName);
		dest.writeString(EMRType);
		dest.writeInt(Gender);
		dest.writeLong(PhotoSID);
		dest.writeString(PhotoURL);
		dest.writeLong(UpdatedTime != null ? UpdatedTime.getTime() : -1);
		dest.writeLong(WorkStatus);
		dest.writeInt(waittingCount);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getWaittingCount() {
		return waittingCount;
	}

	public void setWaittingCount(int waittingCount) {
		this.waittingCount = waittingCount;
	}

	public static final Parcelable.Creator<Doctor> CREATOR = new Parcelable.Creator<Doctor>() {

		@Override
		public Doctor createFromParcel(Parcel source) {
			return new Doctor(source);
		}

		@Override
		public Doctor[] newArray(int size) {
			return new Doctor[size];
		}
	};

}
