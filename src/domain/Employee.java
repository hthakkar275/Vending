package domain;

public class Employee {
	private String name;
	private String companyName;
	
	public Employee()  {
		
	}
	
	public Employee(String employeeName, String companyName) {
		this.name = employeeName;
		this.companyName = companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String p_companyName){
        companyName = p_companyName;
    }
	
	
}
