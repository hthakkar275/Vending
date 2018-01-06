package impl;

import java.util.HashMap;
import java.util.Map;

import domain.SodaCompany;

public class SodaCompanyDirectory   {

    private Map<String, SodaCompany> sodaCompanies;
    private static SodaCompanyDirectory instance;
    
    private SodaCompanyDirectory() {
        sodaCompanies = new HashMap<String, SodaCompany>();
    }
    
    public static SodaCompanyDirectory getSodaCompanyDirectory() {
        if (instance == null) {
            synchronized(SodaCompanyDirectory.class) {
                if (instance == null) {
                    instance = new SodaCompanyDirectory();
                }
            }            
        }
        return instance;
    }
    
    public SodaCompany find(String name) {
        SodaCompany company = sodaCompanies.get(name);
        return company;
    }
    
    public void add(SodaCompany company) {
        sodaCompanies.put(company.getName(), company);
    }
}
