package impl;

import interfaces.VendingMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import domain.Coin;
import domain.Employee;
import domain.Money;
import domain.SodaCompany;
import domain.SodaProduct;
import domain.User;
import exceptions.AuthorizationException;
import exceptions.EmptyProductException;
import exceptions.InsufficientDepositException;
import exceptions.InvalidMoney;
import exceptions.InvalidMoneyDenominationsException;
import exceptions.MultipleProductDispenseException;

public class VendingMachineImpl implements VendingMachine {

	private static VendingMachineImpl self = null;
	
	static {
		self = new VendingMachineImpl();
		try {
	        Employee emp = new Employee("Hemant", "Hemant Inc");
	        SodaCompanyDirectory.getSodaCompanyDirectory().add(new SodaCompany("Hemant Inc"));
	        SodaCompanyDirectory.getSodaCompanyDirectory().find("Hemant Inc").addEmployee(emp);

	        SodaProduct coke = new SodaProduct("CocaCola", "0.75");
	        self.addSodaProduct(emp, coke, 50);

	        SodaProduct pepsi = new SodaProduct("Pepsi", "0.75");
	        self.addSodaProduct(emp, pepsi, 50);

	        SodaProduct sprite = new SodaProduct("Sprit", "0.95");
	        self.addSodaProduct(emp, sprite, 50);

	        SodaProduct dietCoke = new SodaProduct("Diet Coke", "0.80");
	        self.addSodaProduct(emp, dietCoke, 50);

	        SodaProduct water = new SodaProduct("Diet Coke", "1.00");
	        self.addSodaProduct(emp, water, 50);

	        SodaProduct gatorade = new SodaProduct("Diet Coke", "1.25");
	        self.addSodaProduct(emp, gatorade, 50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected class SodaProductInventory {
		final SodaProduct sodaProduct;
		int count;
		
		public SodaProductInventory(SodaProduct sodaProduct)  {
			this.sodaProduct = sodaProduct;
			count = 0;
		}
		
		public synchronized void addCount(int countToAdd) {
			count += countToAdd;
		}
		
		public int getCount() {
			return count;
		}	
		
		public synchronized void reduceCount(int countToReduce) {
		    count -= countToReduce;
		}
		
		public SodaProduct getSodaProduct() {
			return sodaProduct;
		}
	}

	private String id;
	protected ConcurrentMap<String, SodaProductInventory> allProductInventory;
	protected ConcurrentMap<String, Money> accounts;
	protected ConcurrentMap<String, Semaphore> multipleSelectionGuards;
	
	public static VendingMachine getInstance() {
		return self;
	}
	
	private VendingMachineImpl() {
		allProductInventory = new ConcurrentHashMap<String, SodaProductInventory>();
		accounts = new ConcurrentHashMap<String, Money>();
		multipleSelectionGuards = new ConcurrentHashMap<String, Semaphore>();
	}
	
	private VendingMachineImpl(String id, int unlockPin) {
		this();
		setId(id);
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public synchronized int getSodaProductCount(String productName) {
		int count = 0;
		SodaProductInventory productInventory = allProductInventory.get(productName);
		if (productInventory != null) {
			count = productInventory.getCount();
		}
		return count;
	}
	
	protected Money getCurrentBalance(User user) {
		Money balance = accounts.get(user.getName());
		if (balance == null) {
		    try {
                balance = new Money("0.00");
            }
            catch (InvalidMoney e) {
                
            }
		}
		else {
		    try {
                balance = new Money(balance);
            }
            catch (InvalidMoney e) {                
            }
		}
		return balance;
	}
	
	public void addSodaProduct(Employee emp, SodaProduct soda, int count) throws AuthorizationException   {
	    String companyName = emp.getCompanyName();
	    SodaCompany company = SodaCompanyDirectory.getSodaCompanyDirectory().find(companyName);
	    if ((company == null) || (!company.isEmployed(emp))) {
	        throw new AuthorizationException("Unauthorized to stock the machine");
	    }
		SodaProductInventory productInventory = allProductInventory.get(soda.getName());
		if (productInventory == null) {
			SodaProductInventory newProductInventory = new SodaProductInventory(soda);	
			productInventory = allProductInventory.putIfAbsent(soda.getName(), newProductInventory);		
			if (productInventory == null) {
				productInventory = newProductInventory;
			}
		}
		productInventory.addCount(count);
	}
	
	public synchronized void insertMoney(User user, Money money) throws InvalidMoneyDenominationsException {
		if (money.getPenniesCount() > 0) {
		    throw new InvalidMoneyDenominationsException("Pennies not accepted");
		}
		Money balance = getCurrentBalance(user);
		try {
		    accounts.put(user.getName(), balance.add(money));
        }
        catch (InvalidMoney e) {
           // This shouldn't happen
        }
	}

	public SodaProduct makeSelection(User user, String sodaProductName)
			throws InsufficientDepositException, EmptyProductException, MultipleProductDispenseException 
	{
		SodaProduct soda = null;
		// Check if the user's previous selection is still in the
		// process of being dispensed. If so, skip this request as
		// the user may have just accidentally pressed the selection
		// button multiple times in rapid succession for the same 
		// intended selection.
	    if (isPreviousSelectionDispenseInProgress(user)) {
	        throw new MultipleProductDispenseException("Previous selection is being dispensed");	        
	    }
	    try {
	    	// Need to keep this in synchronized block to prevent 
	    	// the soda product inventory from going negative when
	    	// multiple users race for the last can of soda. There
	    	// is already protection from race conditions involving
	    	// multiple request from the same user above.
	    	synchronized(this) {
				SodaProductInventory productInventory = allProductInventory.get(sodaProductName);
				if ((productInventory == null) || (productInventory.getCount() == 0)) {
					throw new EmptyProductException(sodaProductName + " is empty");
				}
				Money sodaCost = productInventory.getSodaProduct().getCost();
				Money userBalance = getCurrentBalance(user);
				if (sodaCost.compareTo(userBalance) > 0) {
				    throw new InsufficientDepositException("Insufficient balance available to purchase selected product");
				}
				try {
		            userBalance = userBalance.subtract(sodaCost);
		            accounts.put(user.getName(), userBalance);
		            productInventory.reduceCount(1);
		            soda = new SodaProduct(productInventory.getSodaProduct());
		        }
		        catch (InvalidMoney e) {
		        }
	    	}
	    }
	    finally {
	    	this.completedDispensingSelection(user);
	    }
        return soda;
	}

	private boolean isPreviousSelectionDispenseInProgress(User user) 
	{
	    boolean previousSelectionInProgress = false;
	    Semaphore guard = getMultipleSelectionGuard(user);
	    if (!guard.tryAcquire()) {
	        previousSelectionInProgress = true;
	    }
	    return previousSelectionInProgress;   
	}
	
	private void completedDispensingSelection(User user) 
	{
	    Semaphore guard = getMultipleSelectionGuard(user);
	    guard.release();
	}
	
	private Semaphore getMultipleSelectionGuard(User user) 
	{
	    Semaphore guard = this.multipleSelectionGuards.get(user.getName());
	    if (guard == null) {
	        synchronized(this) {
	        	guard = this.multipleSelectionGuards.get(user.getName());
        	    if (guard == null) {
        	        guard = new Semaphore(1);
        	    }
        	    this.multipleSelectionGuards.put(user.getName(), guard);
	        }
	    }
	    return guard;
	}
	
    public synchronized Coin[] coinReturn(User user)
    {
    	Coin[] coins;
        Money userBalance = accounts.get(user.getName());
        if (userBalance == null)  {
        	coins = new Money().toCoins();
        }
        else  {
        	coins = userBalance.toCoins();
        	accounts.remove(user.getName());
        }
        return coins;
        
    }

	@Override
	public Map<String, Integer> getInventory() {
		Set<String> sodaNames = allProductInventory.keySet();
		Map<String, Integer> inventory = new HashMap<String, Integer>();
		for (String soda : sodaNames) {
			inventory.put(soda, allProductInventory.get(soda).getCount()); 
		}
		return inventory;
	}

	@Override
	public Money getAccountBalance(String username) throws InvalidMoney {
		return new Money(accounts.get(username));
	}

}
