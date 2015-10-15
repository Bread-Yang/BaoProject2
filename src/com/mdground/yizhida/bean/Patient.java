package com.mdground.yizhida.bean;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mdground.yizhida.util.DateUtils;

public class Patient implements Parcelable {
	@SerializedName("Address")
	@Expose
	private String Address;
	@SerializedName("CityID")
	@Expose
	private int CityID = -1;// 默认值为-1
	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;
	@SerializedName("CompanyName")
	@Expose
	// 病人公司名
	private String CompanyName;
	@SerializedName("CountryID")
	@Expose
	private int CountryID = 86;// 默认为中国
	@SerializedName("CreatedBy")
	@Expose
	private long CreatedBy;
	@SerializedName("CreatedRole")
	@Expose
	// 创建人角色
	private long CreatedRole;
	@SerializedName("DOB")
	@Expose
	// 生日
	private Date DOB;
	@SerializedName("DistrictID")
	@Expose
	// 行政区
	private int DistrictID = -1;// 默认值为-1
	@SerializedName("Email")
	@Expose
	private String Email;
	@SerializedName("EmergencyName")
	@Expose
	// 紧急联系人名
	private String EmergencyName;
	@SerializedName("EmergencyPhone")
	@Expose
	private String EmergencyPhone;
	@SerializedName("EmergencyRelationship")
	@Expose
	private String EmergencyRelationship;
	@SerializedName("EnterType")
	@Expose
	// android 传2
	private long EnterType = 2L;// 默认为2， 1表示ios 2表示android
	@SerializedName("ForeignName")
	@Expose
	// 外国名
	private String ForeignName;
	@SerializedName("Gender")
	@Expose
	private int Gender = 1;// 默认为男
	@SerializedName("IDCard")
	@Expose
	// 身份证
	private String IDCard;
	@SerializedName("MSN")
	@Expose
	// 医保
	private String MSN;
	@SerializedName("Married")
	@Expose
	// 已婚 1单身 2 已婚 3离异
	private int Married = 1;// 默认为未婚
	@SerializedName("Nation")
	@Expose
	// 名族
	private String Nation;
	@SerializedName("PatientCode")
	@Expose
	//
	private String PatientCode;
	@SerializedName("PatientGUID")
	@Expose
	//
	private int PatientGUID = 0;
	@SerializedName("PatientID")
	@Expose
	// 创建时填0 病人id
	private int PatientID;
	@SerializedName("PatientName")
	@Expose
	private String PatientName;
	@SerializedName("Phone")
	@Expose
	private String Phone;
	@SerializedName("PhotoID")
	@Expose
	private int PhotoID;

	@SerializedName("PhotoSID")
	@Expose
	private int PhotoSID;
	@SerializedName("ProvinceID")
	@Expose
	private int ProvinceID = -1;// 默认值为-1
	@SerializedName("RegistrationTime")
	@Expose
	// 当前时间
	private Date RegistrationTime;
	@SerializedName("SSN")
	@Expose
	// 社保
	private String SSN;
	@SerializedName("Status")
	@Expose
	// 1非就诊状态
	private int Status;
	@SerializedName("Street")
	@Expose
	private String Street;
	@SerializedName("Title")
	@Expose
	// 职位
	private String Title;
	@SerializedName("UpdatedBy")
	@Expose
	private long UpdatedBy;

	@SerializedName("UpdatedTime")
	@Expose
	private String UpdatedTime;

	@Expose
	private String SupportName;

	@Expose
	private String SupportPhone;

	/**
	 * 
	 * @return The Address
	 */
	public String getAddress() {
		return Address;
	}

	public String getSupportName() {
		return SupportName;
	}

	public void setSupportName(String supportName) {
		SupportName = supportName;
	}

	public String getSupportPhone() {
		return SupportPhone;
	}

	public void setSupportPhone(String supportPhone) {
		SupportPhone = supportPhone;
	}

	/**
	 * 
	 * @param Address
	 *            The Address
	 */
	public void setAddress(String Address) {
		this.Address = Address;
	}

	/**
	 * 
	 * @return The CityID
	 */
	public int getCityID() {
		return CityID;
	}

	/**
	 * 
	 * @param CityID
	 *            The CityID
	 */
	public void setCityID(int CityID) {
		this.CityID = CityID;
	}

	/**
	 * 
	 * @return The ClinicID
	 */
	public int getClinicID() {
		return ClinicID;
	}

	/**
	 * 
	 * @param ClinicID
	 *            The ClinicID
	 */
	public void setClinicID(int ClinicID) {
		this.ClinicID = ClinicID;
	}

	/**
	 * 
	 * @return The CompanyName
	 */
	public String getCompanyName() {
		return CompanyName;
	}

	/**
	 * 
	 * @param CompanyName
	 *            The CompanyName
	 */
	public void setCompanyName(String CompanyName) {
		this.CompanyName = CompanyName;
	}

	/**
	 * 
	 * @return The CountryID
	 */
	public int getCountryID() {
		return CountryID;
	}

	/**
	 * 
	 * @param CountryID
	 *            The CountryID
	 */
	public void setCountryID(int CountryID) {
		this.CountryID = CountryID;
	}

	/**
	 * 
	 * @return The CreatedBy
	 */
	public long getCreatedBy() {
		return CreatedBy;
	}

	/**
	 * 
	 * @param CreatedBy
	 *            The CreatedBy
	 */
	public void setCreatedBy(long CreatedBy) {
		this.CreatedBy = CreatedBy;
	}

	/**
	 * 
	 * @return The CreatedRole
	 */
	public long getCreatedRole() {
		return CreatedRole;
	}

	/**
	 * 
	 * @param CreatedRole
	 *            The CreatedRole
	 */
	public void setCreatedRole(long CreatedRole) {
		this.CreatedRole = CreatedRole;
	}

	/**
	 * 
	 * @return The DOB
	 */
	public Date getDOB() {
		return DOB;
	}

	/**
	 * 
	 * @param DOB
	 *            The DOB
	 */
	public void setDOB(Date DOB) {
		this.DOB = DOB;
	}

	/**
	 * 
	 * @return The DistrictID
	 */
	public int getDistrictID() {
		return DistrictID;
	}

	/**
	 * 
	 * @param DistrictID
	 *            The DistrictID
	 */
	public void setDistrictID(int DistrictID) {
		this.DistrictID = DistrictID;
	}

	/**
	 * 
	 * @return The Email
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * 
	 * @param Email
	 *            The Email
	 */
	public void setEmail(String Email) {
		this.Email = Email;
	}

	/**
	 * 
	 * @return The EmergencyName
	 */
	public String getEmergencyName() {
		return EmergencyName;
	}

	/**
	 * 
	 * @param EmergencyName
	 *            The EmergencyName
	 */
	public void setEmergencyName(String EmergencyName) {
		this.EmergencyName = EmergencyName;
	}

	/**
	 * 
	 * @return The EmergencyPhone
	 */
	public String getEmergencyPhone() {
		return EmergencyPhone;
	}

	/**
	 * 
	 * @param EmergencyPhone
	 *            The EmergencyPhone
	 */
	public void setEmergencyPhone(String EmergencyPhone) {
		this.EmergencyPhone = EmergencyPhone;
	}

	/**
	 * 
	 * @return The EmergencyRelationship
	 */
	public String getEmergencyRelationship() {
		return EmergencyRelationship;
	}

	/**
	 * 
	 * @param EmergencyRelationship
	 *            The EmergencyRelationship
	 */
	public void setEmergencyRelationship(String EmergencyRelationship) {
		this.EmergencyRelationship = EmergencyRelationship;
	}

	/**
	 * 
	 * @return The
	 */
	public long getEnterType() {
		return (long) 2;
	}

	/**
	 * 
	 * @param EnterType
	 *            The EnterType
	 */
	public void setEnterType(long EnterType) {
		this.EnterType = EnterType;
	}

	/**
	 * 
	 * @return The ForeignName
	 */
	public String getForeignName() {
		return ForeignName;
	}

	/**
	 * 
	 * @param ForeignName
	 *            The ForeignName
	 */
	public void setForeignName(String ForeignName) {
		this.ForeignName = ForeignName;
	}

	/**
	 * 
	 * @return The Gender
	 */
	public int getGender() {
		return Gender;
	}

	public String getGenderStr() {
		if (Gender == 2) {
			return "女";
		}
		return "男";
	}

	/**
	 * 
	 * @param Gender
	 *            The Gender
	 */
	public void setGender(int Gender) {
		this.Gender = Gender;
	}

	/**
	 * 
	 * @return The IDCard
	 */
	public String getIDCard() {
		return IDCard;
	}

	/**
	 * 
	 * @param IDCard
	 *            The IDCard
	 */
	public void setIDCard(String IDCard) {
		this.IDCard = IDCard;
	}

	/**
	 * 
	 * @return The MSN
	 */
	public String getMSN() {
		return MSN;
	}

	/**
	 * 
	 * @param MSN
	 *            The MSN
	 */
	public void setMSN(String MSN) {
		this.MSN = MSN;
	}

	/**
	 * 
	 * @return The Married
	 */
	public int getMarried() {
		return Married;
	}

	/**
	 * 
	 * @param Married
	 *            The Married
	 */
	public void setMarried(int Married) {
		this.Married = Married;
	}

	/**
	 * 
	 * @return The Nation
	 */
	public String getNation() {
		return Nation;
	}

	/**
	 * 
	 * @param Nation
	 *            The Nation
	 */
	public void setNation(String Nation) {
		this.Nation = Nation;
	}

	/**
	 * 
	 * @return The PatientCode
	 */
	public String getPatientCode() {
		return PatientCode;
	}

	/**
	 * 
	 * @param PatientCode
	 *            The PatientCode
	 */
	public void setPatientCode(String PatientCode) {
		this.PatientCode = PatientCode;
	}

	/**
	 * 
	 * @return The PatientGUID
	 */
	public int getPatientGUID() {
		return 0;
	}

	/**
	 * 
	 * @param PatientGUID
	 *            The PatientGUID
	 */
	public void setPatientGUID(int PatientGUID) {
		this.PatientGUID = PatientGUID;
	}

	/**
	 * 
	 * @return The PatientID
	 */
	public int getPatientID() {
		return PatientID;
	}

	/**
	 * 
	 * @param PatientID
	 *            The PatientID
	 */
	public void setPatientID(int PatientID) {
		this.PatientID = PatientID;
	}

	/**
	 * 
	 * @return The PatientName
	 */
	public String getPatientName() {
		return PatientName;
	}

	/**
	 * 
	 * @param PatientName
	 *            The PatientName
	 */
	public void setPatientName(String PatientName) {
		this.PatientName = PatientName;
	}

	/**
	 * 
	 * @return The Phone
	 */
	public String getPhone() {
		return Phone;
	}

	/**
	 * 
	 * @param Phone
	 *            The Phone
	 */
	public void setPhone(String Phone) {
		this.Phone = Phone;
	}

	/**
	 * 
	 * @return The PhotoID
	 */
	public int getPhotoID() {
		return PhotoID;
	}

	/**
	 * 
	 * @param PhotoID
	 *            The PhotoID
	 */
	public void setPhotoID(int PhotoID) {
		this.PhotoID = PhotoID;
	}

	/**
	 * 
	 * @return The ProvinceID
	 */
	public int getProvinceID() {
		return ProvinceID;
	}

	/**
	 * 
	 * @param ProvinceID
	 *            The ProvinceID
	 */
	public void setProvinceID(int ProvinceID) {
		this.ProvinceID = ProvinceID;
	}

	/**
	 * 
	 * @return The RegistrationTime
	 */
	public Date getRegistrationTime() {
		return RegistrationTime;
	}

	/**
	 * 
	 * @param RegistrationTime
	 *            The RegistrationTime
	 */
	public void setRegistrationTime(Date RegistrationTime) {
		this.RegistrationTime = RegistrationTime;
	}

	/**
	 * 
	 * @return The SSN
	 */
	public String getSSN() {
		return SSN;
	}

	/**
	 * 
	 * @param SSN
	 *            The SSN
	 */
	public void setSSN(String SSN) {
		this.SSN = SSN;
	}

	/**
	 * 
	 * @return The Status
	 */
	public int getStatus() {
		return Status;
	}

	/**
	 * 
	 * @param Status
	 *            The Status
	 */
	public void setStatus(int Status) {
		this.Status = Status;
	}

	/**
	 * 
	 * @return The Street
	 */
	public String getStreet() {
		return Street;
	}

	/**
	 * 
	 * @param Street
	 *            The Street
	 */
	public void setStreet(String Street) {
		this.Street = Street;
	}

	/**
	 * 
	 * @return The Title
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * 
	 * @param Title
	 *            The Title
	 */
	public void setTitle(String Title) {
		this.Title = Title;
	}

	/**
	 * 
	 * @return The UpdatedBy
	 */
	public long getUpdatedBy() {
		return UpdatedBy;
	}

	/**
	 * 
	 * @param UpdatedBy
	 *            The UpdatedBy
	 */
	public void setUpdatedBy(long UpdatedBy) {
		this.UpdatedBy = UpdatedBy;
	}

	/**
	 * 
	 * @return The UpdatedTime
	 */
	public String getUpdatedTime() {
		return UpdatedTime;
	}

	/**
	 * 
	 * @param UpdatedTime
	 *            The UpdatedTime
	 */
	public void setUpdatedTime(String UpdatedTime) {
		this.UpdatedTime = UpdatedTime;
	}

	public int getPhotoSID() {
		return PhotoSID;
	}

	public void setPhotoSID(int photoSID) {
		this.PhotoSID = photoSID;
	}

	public String getMarriedStr() {
		String marriedStr = "单身";
		switch (Married) {
		case 1:
			marriedStr = "单身";
			break;
		case 2:
			marriedStr = "已婚";
			break;
		case 3:
			marriedStr = "离异";
			break;

		default:
			break;
		}
		return marriedStr;
	}

	public String getAgeStr() {
		int day = DateUtils.daysBetween(DOB);
		Log.i("HQL", "day = " + day);
		if (day == 0) {
			return "今天";
		} else if (day < 30) {
			return (day) + "天";
		} else if (day < 365) {
			return (day / 30) + "月";
		} else if (day < 5 * 365) {
			int mounth = DateUtils.calcAgeMonthByBrithday(DOB);
			int age = DateUtils.calcAgeByBrithday(DOB);
			if (mounth == 0) {
				return age + "岁";
			} else {
				return age + "岁" + mounth + "个月";
			}
		} else {
			return DateUtils.calcAgeByBrithday(DOB) + "岁";
		}

	}

	public String getPhotoUrl() {
		return "image://" + ClinicID + "." + PhotoSID;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.Address);
		dest.writeInt(this.CityID);
		dest.writeInt(this.ClinicID);
		dest.writeString(this.CompanyName);
		dest.writeInt(this.CountryID);
		dest.writeLong(this.CreatedBy);
		dest.writeLong(this.CreatedRole);
		dest.writeLong(DOB != null ? DOB.getTime() : -1);
		dest.writeInt(this.DistrictID);
		dest.writeString(this.Email);
		dest.writeString(this.EmergencyName);
		dest.writeString(this.EmergencyPhone);
		dest.writeString(this.EmergencyRelationship);
		dest.writeLong(this.EnterType);
		dest.writeString(this.ForeignName);
		dest.writeInt(this.Gender);
		dest.writeString(this.IDCard);
		dest.writeString(this.MSN);
		dest.writeInt(this.Married);
		dest.writeString(this.Nation);
		dest.writeString(this.PatientCode);
		dest.writeInt(this.PatientGUID);
		dest.writeInt(this.PatientID);
		dest.writeString(this.PatientName);
		dest.writeString(this.Phone);
		dest.writeInt(this.PhotoID);
		dest.writeInt(this.PhotoSID);
		dest.writeInt(this.ProvinceID);
		dest.writeLong(DOB != null ? RegistrationTime.getTime() : -1);
		dest.writeString(this.SSN);
		dest.writeInt(this.Status);
		dest.writeString(this.Street);
		dest.writeString(this.Title);
		dest.writeLong(this.UpdatedBy);
		dest.writeString(this.UpdatedTime);
		dest.writeString(this.SupportName);
		dest.writeString(this.SupportPhone);
	}

	public Patient() {
	}

	protected Patient(Parcel in) {
		this.Address = in.readString();
		this.CityID = in.readInt();
		this.ClinicID = in.readInt();
		this.CompanyName = in.readString();
		this.CountryID = in.readInt();
		this.CreatedBy = in.readLong();
		this.CreatedRole = in.readLong();
		long tmpDOB = in.readLong();
		this.DOB = tmpDOB == -1 ? null : new Date(tmpDOB);
		this.DistrictID = in.readInt();
		this.Email = in.readString();
		this.EmergencyName = in.readString();
		this.EmergencyPhone = in.readString();
		this.EmergencyRelationship = in.readString();
		this.EnterType = in.readLong();
		this.ForeignName = in.readString();
		this.Gender = in.readInt();
		this.IDCard = in.readString();
		this.MSN = in.readString();
		this.Married = in.readInt();
		this.Nation = in.readString();
		this.PatientCode = in.readString();
		this.PatientGUID = in.readInt();
		this.PatientID = in.readInt();
		this.PatientName = in.readString();
		this.Phone = in.readString();
		this.PhotoID = in.readInt();
		this.PhotoSID = in.readInt();
		this.ProvinceID = in.readInt();
		long tmpRegistrationTime = in.readLong();
		this.RegistrationTime = tmpRegistrationTime == -1 ? null : new Date(tmpRegistrationTime);
		this.SSN = in.readString();
		this.Status = in.readInt();
		this.Street = in.readString();
		this.Title = in.readString();
		this.UpdatedBy = in.readLong();
		this.UpdatedTime = in.readString();
		this.SupportName = in.readString();
		this.SupportPhone = in.readString();
	}

	public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
		public Patient createFromParcel(Parcel source) {
			return new Patient(source);
		}

		public Patient[] newArray(int size) {
			return new Patient[size];
		}
	};
}
