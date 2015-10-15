package com.mdground.yizhida.db.dao;

import java.util.List;

import android.content.Context;

import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.db.dao.inter.IEmployeeDao;

/**
 * 员工表操作类
 * 
 * @author qinglong.huang
 * 
 */
public class EmployeeDao extends BaseDao<Employee> implements IEmployeeDao {

	private static EmployeeDao instance = null;

	protected EmployeeDao(Context context) {
		super(context);
	}

	public static EmployeeDao getInstance(Context context) {
		if (instance == null) {
			instance = new EmployeeDao(context);
		}
		return instance;
	}

	@Override
	public void deleteAll() {
		deleteAll(Employee.class);
	}

	public void saveEmployeess(List<Employee> employees) {
		if (employees == null) {
			return;
		}
		for (int i = 0; i < employees.size(); i++) {
			List<Employee> employeeList = getDb().findAllByWhere(Employee.class, "EmployeeID = " + employees.get(i).getEmployeeID());
			if (employeeList == null || employeeList.size() == 0) {
				save(employees.get(i));
			}
		}
	}

	public List<Employee> getAll() {
		return getDb().findAll(Employee.class);
	}
}
