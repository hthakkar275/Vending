package domain;

import java.util.HashMap;
import java.util.Map;

public class SodaCompany {
	private String name;
	private Map<String, Employee> employees;
	
	public SodaCompany(String name)  {
		this.setName(name);
		employees = new HashMap<String, Employee>();
	}
	
	public void addEmployee(Employee employee)  {
	    employee.setCompanyName(name);
		employees.put(employee.getName(), employee);
	}
	
	public void removeEmployee(Employee employee) {	   
		employees.remove(employee.getName());				
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public boolean isEmployed(Employee emp) {
	    Employee foundEmployee = employees.get(emp.getName());
	    if (foundEmployee == null) {
	        return false;
	    }
	    else { 
	        return true;
	    }
	}
}
