package com.mdground.yizhida.db.dao.inter;

import java.util.List;

import com.mdground.yizhida.bean.Employee;

public interface IEmployeeDao {

	public void saveEmployeess(List<Employee> employees);

	public void deleteAll();
}
