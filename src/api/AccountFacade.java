package api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import application.DepositMoneyServlet;
import domain.Account;
import service.AccountService;

@Path("/rest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountFacade {

	private static Log log = LogFactory.getLog(AccountFacade.class);

	AccountService accountService = AccountService.getInstance();
	
	public AccountFacade() {
		log.info("Entering AccountFacade");
	}
	
	@GET
	@Path("/account/{accountId}")
	public List<Account> getAccount(@PathParam("accountId") long accountId) {
		return accountService.getAccount(accountId);
	}

	@GET
	@Path("/accounts")
	public List<Account> getAccounts() {
		return accountService.getAccounts();
	}

}