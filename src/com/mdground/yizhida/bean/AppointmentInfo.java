package com.mdground.yizhida.bean;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mdground.yizhida.util.DateUtils;

/**
 * 预约信息 /// <summary> /// 等待支付 /// </summary> NotPaid = 1,
 * 
 * /// <summary> /// 成功预约 /// </summary> HasPaid = 2,
 * 
 * /// <summary> /// 正在候诊（医生端/护士端挂号则直接进入此阶段） /// </summary> Waiting = 4,
 * 
 * /// <summary> /// 就诊中 /// </summary> Working = 8,
 * 
 * /// <summary> /// 完成预约 /// </summary> Finished = 16,
 * 
 * /// <summary> /// 取消预约 /// </summary> Cancel = 32,
 * 
 * /// <summary> /// 预约过号（支持重新排队） /// </summary> Missed = 64,
 * 
 * /// <summary> /// 预约过期（不支持重新排队） /// </summary> Expired = 128,
 * 
 * /// <summary> /// 重新排队 /// </summary> ReWaiting = 256
 * 
 * @author Vincent
 * 
 */
@Table(name = "appointment")
public class AppointmentInfo implements Parcelable {

	@Transient
	public static final int DIAGNOSING = 0;
	@Transient
	public static final int WATTING = 1;
	@Transient
	public static final int FINISH = 2;
	@Transient
	public static final int PASSED = 3;
	@Transient
	public static final int UNKNOW = 4;
	@Transient
	public static final int GROUP = 5;
	@Transient
	public static final int EMPTY = 6;

	/* 等待支付 */
	@Transient
	public static final int STATUS_NOT_PAID = 1;
	// / 成功预约
	@Transient
	public static final int STATUS_HAS_PAID = 2;
	/* 候诊 */
	@Transient
	public static final int STATUS_WATTING = 4;
	/*
	 * 就诊中 请求的数据为，已支付就诊中的状态
	 */
	@Transient
	public static final int STATUS_DIAGNOSING = 8;
	/* 已诊断 */
	@Transient
	public static final int STATUS_FINISH = 16;

	// / 取消预约
	@Transient
	public static final int STATUS_CANCEL = 32;

	// / 预约过号（支持重新排队）
	@Transient
	public static final int STATUS_PASSED = 64;

	// / 预约过期（不支持重新排队）
	@Transient
	public static final int STATUS_INVALID = 128;

	// / 重新排队
	@Transient
	public static final int STATUS_REAPPOINT = 256;

	@Id
	private int id;

	@SerializedName("OPNo")
	@Expose
	private int OPNo;

	@SerializedName("OPStatus")
	@Expose
	private int OPStatus;
	
	@SerializedName("OPType")
	@Expose
	private int OPType;

	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;

	@SerializedName("DoctorID")
	@Expose
	private int DoctorID;

	@SerializedName("OPID")
	@Expose
	private int OPID;//

	@SerializedName("PatientDOB")
	@Expose
	private Date PatientDOB;// 病人生日

	@SerializedName("PatientGender")
	@Expose
	private int PatientGender = 1;// 默认为男

	@SerializedName("PatientID")
	@Expose
	private int PatientID;

	@SerializedName("PatientName")
	@Expose
	private String PatientName;

	@SerializedName("Remark")
	@Expose
	private String Remark = "";// 症状类型

	@SerializedName("WaitingCount")
	@Expose
	private long WaitingCount;

	@SerializedName("OPDate")
	@Expose
	private Date OPDate;

	@SerializedName("CreateTime")
	@Expose
	private Date CreateTime;

	@SerializedName("DoctorName")
	@Expose
	private String DoctorName;

	@SerializedName("OPDatePeriod")
	@Expose
	private int OPDatePeriod;

	// 预约医生的电子专业
	@Transient
	private String OPEMR;

	@Transient
	private int type = UNKNOW;
	
	@Transient
	private int group_num = 0;

	public AppointmentInfo() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 根据病人出生计算
	 * 
	 * @return
	 */
	public String getPatientAge() {
		int day = DateUtils.daysBetween(PatientDOB);
		Log.i("HQL", "day = " + day);
		if (day == 0) {
			return "今天";
		} else if (day < 30) {
			return (day) + "天";
		} else if (day < 365) {
			return (day / 30) + "月";
		} else if (day < 5 * 365) {
			int mounth = DateUtils.calcAgeMonthByBrithday(PatientDOB);
			int age = DateUtils.calcAgeByBrithday(PatientDOB);
			if (mounth == 0) {
				return age + "岁";
			} else {
				return age + "岁" + mounth + "个月";
			}
		} else {
			return DateUtils.calcAgeByBrithday(PatientDOB) + "岁";
		}

	}

	public String getPatientGenderStr() {
		if (PatientGender == 1) {
			return "男";
		} else if (PatientGender == 2) {
			return "女";
		}

		return "";
	}

	public String getTaStr() {
		if (PatientGender == 1) {
			return "他";
		} else if (PatientGender == 2) {
			return "她";
		}

		return "未知";
	}

	public Integer getOPNo() {
		return OPNo;
	}

	public void setOPNo(int oPNo) {
		OPNo = oPNo;
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

	public int getOPID() {
		return OPID;
	}

	public void setOPID(int oPID) {
		OPID = oPID;
	}

	public Date getPatientDOB() {
		return PatientDOB;
	}

	public void setPatientDOB(Date patientDOB) {
		PatientDOB = patientDOB;
	}

	public int getPatientGender() {
		return PatientGender;
	}

	public void setPatientGender(int patientGender) {
		PatientGender = patientGender;
	}

	public int getPatientID() {
		return PatientID;
	}

	public void setPatientID(int patientID) {
		PatientID = patientID;
	}

	public String getPatientName() {
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public long getWaitingCount() {
		return WaitingCount;
	}

	public void setWaitingCount(long waitingCount) {
		WaitingCount = waitingCount;
	}

	public Date getOPDate() {
		return OPDate;
	}

	public void setOPDate(Date oPDate) {
		OPDate = oPDate;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public int getOPDatePeriod() {
		return OPDatePeriod;
	}

	public void setOPDatePeriod(int oPDatePeriod) {
		OPDatePeriod = oPDatePeriod;
	}

	public String getOPEMR() {
		return OPEMR;
	}

	public void setOPEMR(String oPEMR) {
		OPEMR = oPEMR;
	}

	public int getType() {
		if (type == GROUP || type == EMPTY) {
			return type;
		}

		if ((OPStatus & AppointmentInfo.STATUS_DIAGNOSING) != 0) {
			return DIAGNOSING;
		} else if ((OPStatus & AppointmentInfo.STATUS_WATTING) != 0) {
			return WATTING;
		} else if ((OPStatus & AppointmentInfo.STATUS_FINISH) != 0) {
			return FINISH;
		} else if ((OPStatus & AppointmentInfo.STATUS_PASSED) != 0) {
			return PASSED;
		} else {
			return UNKNOW;
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGroup_num() {
		return group_num;
	}

	public void setGroup_num(int group_num) {
		this.group_num = group_num;
	}

	@Override
	public String toString() {
		return "AppointmentInfo [AppointmentNo=" + OPNo + ", AppointmentStatus=" + OPStatus + ", ClinicID=" + ClinicID + ", DoctorID=" + DoctorID
				+ ", OPID=" + OPID + ", PatientDOB=" + PatientDOB + ", PatientGender=" + PatientGender + ", PatientID=" + PatientID
				+ ", PatientName=" + PatientName + ", Remark=" + Remark + ", WaitingCount=" + WaitingCount + ", AppointmentDate=" + OPDate
				+ ", CreateTime=" + CreateTime + ", DoctorName=" + DoctorName + "]";
	}

	public AppointmentInfo(Parcel source) {
		this.OPNo = source.readInt();
		this.OPStatus = source.readInt();
		this.OPType = source.readInt();
		this.ClinicID = source.readInt();
		this.DoctorID = source.readInt();
		this.OPID = source.readInt();
		long tmpDOB = source.readLong();
		this.PatientDOB = tmpDOB == -1 ? null : new Date(tmpDOB);
		this.PatientGender = source.readInt();
		this.PatientID = source.readInt();
		this.PatientName = source.readString();
		this.Remark = source.readString();
		this.WaitingCount = source.readLong();
		long tmpOPDate = source.readLong();
		this.OPDate = tmpOPDate == -1 ? null : new Date(tmpOPDate);
		long tmpCreateTime = source.readLong();
		this.CreateTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
		this.DoctorName = source.readString();
		this.OPDatePeriod = source.readInt();
		this.OPEMR = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(OPNo);
		dest.writeInt(OPStatus);
		dest.writeInt(OPType);
		dest.writeInt(ClinicID);
		dest.writeInt(DoctorID);
		dest.writeInt(OPID);
		if (PatientDOB == null) {
			dest.writeLong(0);
		} else {
			dest.writeLong(PatientDOB.getTime());
		}
		dest.writeInt(PatientGender);
		dest.writeInt(PatientID);
		dest.writeString(PatientName);
		dest.writeString(Remark);
		dest.writeLong(WaitingCount);
		if (OPDate == null) {
			dest.writeLong(0);
		} else {
			dest.writeLong(OPDate.getTime());
		}

		if (CreateTime == null) {
			dest.writeLong(0);
		} else {
			dest.writeLong(CreateTime.getTime());
		}
		dest.writeString(DoctorName);
		dest.writeInt(OPDatePeriod);
		dest.writeString(OPEMR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<AppointmentInfo> CREATOR = new Parcelable.Creator<AppointmentInfo>() {

		@Override
		public AppointmentInfo createFromParcel(Parcel source) {
			return new AppointmentInfo(source);
		}

		@Override
		public AppointmentInfo[] newArray(int size) {
			return new AppointmentInfo[size];
		}
	};
}
