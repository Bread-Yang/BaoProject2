package com.mdground.yizhida.db.dao;

import java.util.List;

import com.mdground.yizhida.bean.DrugCategory;

import android.content.Context;

/**
 * 药物分类数据
 * 
 */
public class DrugCategoryDao extends BaseDao<DrugCategory> {

	private static DrugCategoryDao instance = null;

	protected DrugCategoryDao(Context context) {
		super(context);
	}

	public static DrugCategoryDao getInstance(Context context) {
		if (instance == null) {
			instance = new DrugCategoryDao(context);
		}

		return instance;
	}

	public void saveDrugCategories(List<DrugCategory> drugCategories) {
		if (drugCategories == null) {
			return;
		}

		for (int i = 0; i < drugCategories.size(); i++) {
			List<DrugCategory> ds = getDb().findAllByWhere(DrugCategory.class,
					"TypeID = " + drugCategories.get(i).getTypeID());
			if (ds.size() > 0) {
				continue;
			}
			save(drugCategories.get(i));
		}
	}

	public List<DrugCategory> getDrugCateogriesByClinicID(int clinicID) {
		return getDb().findAllByWhere(DrugCategory.class, "ClinicID = " + clinicID);
	}

	public void deleteAllDrugCagegories() {
		getDb().deleteAll(DrugCategory.class);
	}

	public void deleteAllDrugCagegoriesByClinicID(int ClinicID) {
		getDb().deleteByWhere(DrugCategory.class, "ClinicID = " + ClinicID);
	}

}
