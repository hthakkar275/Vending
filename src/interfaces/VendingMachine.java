package interfaces;

import java.util.Map;

import domain.Coin;
import domain.Employee;
import domain.Money;
import domain.SodaProduct;
import domain.User;
import exceptions.AuthorizationException;
import exceptions.EmptyProductException;
import exceptions.InsufficientDepositException;
import exceptions.InvalidMoney;
import exceptions.InvalidMoneyDenominationsException;
import exceptions.MultipleProductDispenseException;

public interface VendingMachine {
	public void addSodaProduct(Employee emp, SodaProduct soda, int count) throws AuthorizationException;
	public void insertMoney(User user, Money money) throws InvalidMoneyDenominationsException;
	public SodaProduct makeSelection(User user, String sodaProductName) 
		throws InsufficientDepositException, EmptyProductException, MultipleProductDispenseException;
	public Coin[] coinReturn(User user);
	public int getSodaProductCount(String productName);
	public Map<String, Integer> getInventory();
	public Money getAccountBalance(String username) throws InvalidMoney;
}
