package com.mdground.yizhida.api.bean;

import java.util.Date;

/**
 * 收入
 * 
 * @author qinglong.huang
 * 
 */
public class IncomeStatisticInfo {
	private int ClinicID;
	private int EmlopyeeID;
	private float IncomeSID;
	private float MonthIncome;
	private float TotalIncome;
	private float TotalWithdrawal;
	private Date UpdatedTime;
	private float WeekIncome;
	private float YesterdayIncome;

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getEmlopyeeID() {
		return EmlopyeeID;
	}

	public void setEmlopyeeID(int emlopyeeID) {
		EmlopyeeID = emlopyeeID;
	}

	public float getIncomeSID() {
		return IncomeSID;
	}

	public void setIncomeSID(float incomeSID) {
		IncomeSID = incomeSID;
	}

	public float getMonthIncome() {
		return MonthIncome;
	}

	public void setMonthIncome(float monthIncome) {
		MonthIncome = monthIncome;
	}

	public float getTotalIncome() {
		return TotalIncome;
	}

	public void setTotalIncome(float totalIncome) {
		TotalIncome = totalIncome;
	}

	public float getTotalWithdrawal() {
		return TotalWithdrawal;
	}

	public void setTotalWithdrawal(float totalWithdrawal) {
		TotalWithdrawal = totalWithdrawal;
	}

	public Date getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		UpdatedTime = updatedTime;
	}

	public float getWeekIncome() {
		return WeekIncome;
	}

	public void setWeekIncome(float weekIncome) {
		WeekIncome = weekIncome;
	}

	public float getYesterdayIncome() {
		return YesterdayIncome;
	}

	public void setYesterdayIncome(float yesterdayIncome) {
		YesterdayIncome = yesterdayIncome;
	}

}
