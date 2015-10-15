package com.mdground.yizhida.db.dao.inter;

public interface IBaseDao<T> {

	public void deleteAll(Class<? extends T> clazz);

	public void save(T t);

	public T getById(int id, Class<? extends T> clazz);
}
