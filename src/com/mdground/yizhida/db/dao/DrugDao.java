package com.mdground.yizhida.db.dao;

import java.util.List;

import com.mdground.yizhida.bean.Drug;

import android.content.Context;

/**
 * 药物数据
 * 
 */
public class DrugDao extends BaseDao<Drug> {

	private static DrugDao instance = null;

	protected DrugDao(Context context) {
		super(context);
	}

	public static DrugDao getInstance(Context context) {
		if (instance == null) {
			instance = new DrugDao(context);
		}

		return instance;
	}

	public void saveDrugs(List<Drug> drugs) {
		if (drugs == null) {
			return;
		}

		for (int i = 0; i < drugs.size(); i++) {
			List<Drug> ds = getDb().findAllByWhere(Drug.class, "DrugID = " + drugs.get(i).getDrugID());
			if (ds.size() > 0) {
				continue;
			}
			save(drugs.get(i));
		}
	}
	
	public Drug getDrugByDrugID(int drugID) {
		List<Drug> list = getDb().findAllByWhere(Drug.class, "DrugID = " + drugID);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<Drug> getDrugByDrugName(String drugName) {
		return getDb().findAllByWhere(Drug.class, "DrugName like '%" + drugName + "%'");
	}

	public List<Drug> getDrugByClinicID(int clinicID) {
		return getDb().findAllByWhere(Drug.class, "ClinicID = " + clinicID);
	}

	public List<Drug> getDrugByDrugNameAndClinicID(String drugName, int clinicID) {
		return getDb().findAllByWhere(Drug.class, "DrugName like '%" + drugName + "%' and ClinicID = " + clinicID);
	}

	public List<Drug> getDrugByClinicIDAndTypeID(int clinicID, int typeID) {
		return getDb().findAllByWhere(Drug.class, "ClinicID = " + clinicID + " and TypeID = " + typeID);
	}

	public List<Drug> getDrugByDrugNameAndClinicIDAndTypeID(String drugName, int clinicID, int typeID) {
		return getDb().findAllByWhere(Drug.class,
				"DrugName like '%" + drugName + "%' and ClinicID = " + clinicID + " and TypeID = " + typeID);
	}

	public List<Drug> getAllDrug() {
		return getDb().findAll(Drug.class);
	}

	public void deleteAllDrug() {
		getDb().deleteAll(Drug.class);
	}

	public void deleteAllDrugByClinicID(int ClinicID) {
		getDb().deleteByWhere(Drug.class, "ClinicID = " + ClinicID);
	}

}
