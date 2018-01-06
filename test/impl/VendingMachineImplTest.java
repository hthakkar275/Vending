package impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Employee;
import domain.Money;
import domain.SodaCompany;
import domain.SodaProduct;
import domain.User;
import exceptions.AuthorizationException;
import exceptions.InsufficientDepositException;
import exceptions.InvalidMoney;

public class VendingMachineImplTest {

	VendingMachineImpl testMachine;
    Employee employee; 
    Employee imposter;
    User userA;
    User userB;   
	
	@Before
	public void setUp() throws Exception {
        SodaCompanyDirectory companies = SodaCompanyDirectory.getSodaCompanyDirectory();
        SodaCompany abcCompany = new SodaCompany("ABC Bottling Corporation");
        companies.add(abcCompany);
        employee = new Employee("John Q Public", abcCompany.getName());
        abcCompany.addEmployee(employee);
        imposter = new Employee("John Q Imposter", abcCompany.getName());
        userA = new User("Jane Customer");
        userB = new User("Mike Buyer");   
        testMachine = (VendingMachineImpl) VendingMachineImpl.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
    public void testStory1NormalScenario() {
        try {
            SodaProduct coke = new SodaProduct("Coca-Cola", "1.25");
            SodaProduct pepsi = new SodaProduct("Pepsi", "1.15");
            testMachine.addSodaProduct(employee, coke, 125);
            testMachine.addSodaProduct(employee, pepsi, 115);
        }
        catch (Exception e) {
            fail("Unexpected exception");
        }
        int cokeInventory = testMachine.allProductInventory.get("Coca-Cola").count;
        int pepsiInventory = testMachine.allProductInventory.get("Pepsi").count;
        assertEquals(125, cokeInventory);
        assertEquals(115, pepsiInventory);
    }
	
	@Test
	public void testStory1NormalScenario2() {
	    try {
	        SodaProduct coke = new SodaProduct("Coca-Cola", "1.25");
	        SodaProduct pepsi = new SodaProduct("Pepsi", "1.15");
	        testMachine.addSodaProduct(employee, coke, 125);
	        testMachine.addSodaProduct(employee, pepsi, 115);
            testMachine.addSodaProduct(employee, coke, 100);
            testMachine.addSodaProduct(employee, pepsi, 100);
	    }
	    catch (Exception e) {
	        fail("Unexpected exception");
	    }
	    int cokeInventory = testMachine.allProductInventory.get("Coca-Cola").count;
	    int pepsiInventory = testMachine.allProductInventory.get("Pepsi").count;
	    assertEquals(225, cokeInventory);
	    assertEquals(215, pepsiInventory);
	}

	@Test
	public void testStory1ExceptionScenario()  {
	    boolean authorizationExceptionRcvd = false;
        try {
            SodaProduct coke = new SodaProduct("Coca-Cola", "1.25");
            testMachine.addSodaProduct(imposter, coke, 125);
        }
        catch (AuthorizationException e) {
            authorizationExceptionRcvd = true;
        }
        catch (InvalidMoney e) {
            fail("Unexpected exception");
        }
        assertTrue("Did not receive AuthorizationException", authorizationExceptionRcvd);
    }	

	@Test 
	public void testInsertMoneyNormalScenario1()  {
	    Money expectedUserABalance = null;
	    Money expectedUserBBalance = null;	    
	    try {
	        testMachine.insertMoney(userA, new Money("1.00"));
	        testMachine.insertMoney(userA, new Money("1.45"));
	        testMachine.insertMoney(userB, new Money("0.05"));
	        testMachine.insertMoney(userB, new Money("0.25"));
	        testMachine.insertMoney(userB, new Money("0.60"));
	        expectedUserABalance = new Money("2.45");
	        expectedUserBBalance = new Money(".90");
	    }
	    catch (Exception e) {
	        fail("Unexpected exception");
	    }
	    Money userABalance = testMachine.accounts.get(userA.getName());
	    Money userBBalance = testMachine.accounts.get(userB.getName());
	    int userACmpResult = userABalance.compareTo(expectedUserABalance);
	    int userBCmpResult = userBBalance.compareTo(expectedUserBBalance);
	    assertEquals(0, userACmpResult);
	    assertEquals(0, userBCmpResult);
	}
	
	@Test 
	public void testInsertMoneyExceptionScenario1() {
	    boolean exceptionRcvd = false;
        Money expectedUserABalance = null;      
	    try {	        
	        expectedUserABalance = new Money("1.00");
	        testMachine.insertMoney(userA, expectedUserABalance);
	        testMachine.insertMoney(userA, new Money("1.23"));
	    }
	    catch (Exception e) {
	        exceptionRcvd = true;
	    }
        Money userABalance = testMachine.accounts.get(userA.getName());
        int userACmpResult = userABalance.compareTo(expectedUserABalance);
        assertTrue("Did not receive InvalidMoneyDenominationException", exceptionRcvd);
        assertEquals(0, userACmpResult);
	}
	
	@Test 
	public void testInsertMoneyExceptionScenario2() {
	    Money expectedUserABalance = null;
	    Money userAMoneyToAdd = null;
	    try {           
	        userAMoneyToAdd = new Money("1.00");
	        expectedUserABalance = new Money("1.00");
	        testMachine.insertMoney(userA, userAMoneyToAdd);
	        userAMoneyToAdd.add(new Money("2.45"));
	    }
	    catch (Exception e) {
            fail("Unexpected exception");
	    }
	    Money userABalance = testMachine.accounts.get(userA.getName());
	    int userACmpResult = userABalance.compareTo(expectedUserABalance);
	    assertEquals(0, userACmpResult);
	}
	
	@Test
	public void testMakeSelectionNormalScenario1() {
	    Money expectedBalanceAfterSelection = null;
	    SodaProduct soda = null;
        try {
            expectedBalanceAfterSelection = new Money("0.00");
            SodaProduct coke = new SodaProduct("Coca-Cola", "1.25");
            testMachine.addSodaProduct(employee, coke, 125);
            testMachine.insertMoney(userA, new Money("1.00"));
            testMachine.insertMoney(userA, new Money("0.25"));
            soda = testMachine.makeSelection(userA, "Coca-Cola");
        }
        catch (Exception e) {
            fail("Unexpected exception");
        }
        Money userABalance = testMachine.accounts.get(userA.getName());
        int userACmpResult = userABalance.compareTo(expectedBalanceAfterSelection);
        int cokeInventory = testMachine.allProductInventory.get("Coca-Cola").count;
        assertEquals(124, cokeInventory);
        assertEquals(0, userACmpResult);
        assertTrue("Received different soda product than expected", soda.getName().equals("Coca-Cola")); 
	}
	
    @Test
    public void testMakeSelectionExceptionScenario1() {
        boolean exceptionRcvd = false;
        Money expectedBalanceAfterSelection = null;
        try {
            expectedBalanceAfterSelection = new Money("1.00");
            SodaProduct coke = new SodaProduct("Coca-Cola", "1.25");
            testMachine.addSodaProduct(employee, coke, 125);
            testMachine.insertMoney(userA, new Money("1.00"));
            testMachine.makeSelection(userA, "Coca-Cola");
        }
        catch (InsufficientDepositException e) {
            exceptionRcvd = true;
        }
        catch (Exception e) {
            fail("Unexpected exception");
        }
        Money userABalance = testMachine.accounts.get(userA.getName());
        int userACmpResult = userABalance.compareTo(expectedBalanceAfterSelection);
        int cokeInventory = testMachine.allProductInventory.get("Coca-Cola").count;
        assertTrue("Expected InsufficientDepositException", exceptionRcvd);
        assertEquals(125, cokeInventory);
        assertEquals(0, userACmpResult);
    }

}
