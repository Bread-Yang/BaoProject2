package com.mdground.yizhida.db.dao;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DaoConfig;
import android.content.Context;

import com.mdground.yizhida.db.dao.inter.IBaseDao;

public class BaseDao<T> implements IBaseDao<T> {
	private static FinalDb db;

	public BaseDao(Context context) {
		if (db == null) {
			DaoConfig daoConfig = new DaoConfig();
			daoConfig.setContext(context);
			daoConfig.setDbName("yideguan.db");
			daoConfig.setDebug(false);
			db = FinalDb.create(daoConfig);
		}
	}

	@Override
	public void deleteAll(Class<? extends T> clazz) {
		db.deleteAll(clazz);
	}

	@Override
	public void save(T t) {
		db.save(t);
	}

	@Override
	public T getById(int id, Class<? extends T> clazz) {
		return db.findById(id, clazz);
	}

	public FinalDb getDb() {
		return db;
	}
}
