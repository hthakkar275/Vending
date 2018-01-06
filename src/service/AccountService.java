package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.Account;

public class AccountService {
	
	private List<Account> accounts;
	private static AccountService self = new AccountService();
	
	public static AccountService getInstance() {
		return self;
	}
	
	private AccountService() {
		accounts = new ArrayList<Account>();
		
		Account account = new Account();
		account.setAccountId(1001);
		account.setFirstName("Joe");
		account.setLastName("Smith");
		account.setCurrentBalance(new BigDecimal("32.00"));
		accounts.add(account);
		
		account = new Account();
		account.setAccountId(1002);
		account.setFirstName("John");
		account.setLastName("Doe");
		account.setCurrentBalance(new BigDecimal("22.00"));
		accounts.add(account);

		account = new Account();
		account.setAccountId(1003);
		account.setFirstName("Jane");
		account.setLastName("Doe");
		account.setCurrentBalance(new BigDecimal("5.00"));
		accounts.add(account);
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	public List<Account> getAccount(long accountId) {
		return accounts.stream().filter((acct) -> acct.getAccountId() == accountId).collect(Collectors.toList());
	}
}
