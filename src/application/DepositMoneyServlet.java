package application;

import impl.VendingMachineImpl;
import interfaces.VendingMachine;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import domain.Money;
import domain.User;

public class DepositMoneyServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DepositMoneyServlet.class);

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		try {
			HttpSession session = request.getSession();
			if (session == null) {
				response.sendRedirect("/Vending/login");
			} else {
				VendingMachine vendingMachine = VendingMachineImpl.getInstance();
				String username = (String) session.getAttribute("username");
			    User user = new User(username);
				String moneyStr = request.getParameter("money");
				Money money;
				money = new Money(moneyStr);
				log.info("Username = " + username);
				log.info("Money Input = " + moneyStr);
				log.info("Money Object = " + money.toString());
				vendingMachine.insertMoney(user, money);
				response.sendRedirect("/Vending/main");
			}
		} catch (Exception e) {
			log.error("Unexpected error", e);
			response.sendError(500);
		}
	}
}