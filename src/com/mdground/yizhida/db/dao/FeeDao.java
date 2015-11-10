package com.mdground.yizhida.db.dao;

import java.util.List;

import com.mdground.yizhida.bean.Fee;

import android.content.Context;

/**
 * 费用数据
 * 
 */
public class FeeDao extends BaseDao<Fee> {

	private static FeeDao instance = null;

	protected FeeDao(Context context) {
		super(context);
	}

	public static FeeDao getInstance(Context context) {
		if (instance == null) {
			instance = new FeeDao(context);
		}

		return instance;
	}

	public void saveFees(List<Fee> fees) {
		if (fees == null) {
			return;
		}

		for (int i = 0; i < fees.size(); i++) {
			List<Fee> ds = getDb().findAllByWhere(Fee.class, "FTID = " + fees.get(i).getFTID());
			if (ds.size() > 0) {
				continue;
			}
			save(fees.get(i));
		}
	}

	public List<Fee> getFeesByClinicID(int clinicID) {
		return getDb().findAllByWhere(Fee.class, "ClinicID = " + clinicID);
	}

	public void deleteAllFee() {
		getDb().deleteAll(Fee.class);
	}

	public void deleteAllFeesByClinicID(int ClinicID) {
		getDb().deleteByWhere(Fee.class, "ClinicID = " + ClinicID);
	}

}
