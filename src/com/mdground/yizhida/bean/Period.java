package com.mdground.yizhida.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 诊所工作时间
 * 
 * @author Vincent
 * 
 */
public class Period {

	@SerializedName("BeginTime")
	@Expose
	private Long BeginTime;
	@SerializedName("ClinicID")
	@Expose
	private Long ClinicID;
	@SerializedName("EndTime")
	@Expose
	private Long EndTime;
	@SerializedName("PeriodID")
	@Expose
	private Long PeriodID;
	@SerializedName("PeriodName")
	@Expose
	private String PeriodName;
	@SerializedName("PeriodType")
	@Expose
	private Long PeriodType;
	@SerializedName("UpdatedTime")
	@Expose
	private String UpdatedTime;

	/**
	 * 
	 * @return The BeginTime
	 */
	public Long getBeginTime() {
		return BeginTime;
	}

	/**
	 * 
	 * @param BeginTime
	 *            The BeginTime
	 */
	public void setBeginTime(Long BeginTime) {
		this.BeginTime = BeginTime;
	}

	/**
	 * 
	 * @return The ClinicID
	 */
	public Long getClinicID() {
		return ClinicID;
	}

	/**
	 * 
	 * @param ClinicID
	 *            The ClinicID
	 */
	public void setClinicID(Long ClinicID) {
		this.ClinicID = ClinicID;
	}

	/**
	 * 
	 * @return The EndTime
	 */
	public Long getEndTime() {
		return EndTime;
	}

	/**
	 * 
	 * @param EndTime
	 *            The EndTime
	 */
	public void setEndTime(Long EndTime) {
		this.EndTime = EndTime;
	}

	/**
	 * 
	 * @return The PeriodID
	 */
	public Long getPeriodID() {
		return PeriodID;
	}

	/**
	 * 
	 * @param PeriodID
	 *            The PeriodID
	 */
	public void setPeriodID(Long PeriodID) {
		this.PeriodID = PeriodID;
	}

	/**
	 * 
	 * @return The PeriodName
	 */
	public String getPeriodName() {
		return PeriodName;
	}

	/**
	 * 
	 * @param PeriodName
	 *            The PeriodName
	 */
	public void setPeriodName(String PeriodName) {
		this.PeriodName = PeriodName;
	}

	/**
	 * 
	 * @return The PeriodType
	 */
	public Long getPeriodType() {
		return PeriodType;
	}

	/**
	 * 
	 * @param PeriodType
	 *            The PeriodType
	 */
	public void setPeriodType(Long PeriodType) {
		this.PeriodType = PeriodType;
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

}
