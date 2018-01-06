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

public class LoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(LoginServlet.class);

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		try {
			String username = request.getParameter("username");
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			User user = new User(username);
			VendingMachine vendingMachine = VendingMachineImpl.getInstance();
			vendingMachine.insertMoney(user, new Money("0.00"));
			response.sendRedirect("/Vending/main");
		} catch (Exception e) {
			log.error("Unexpected error", e);
			response.sendError(500);
		}
		
	}
	
	
}