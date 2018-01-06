package application;

import impl.VendingMachineImpl;
import interfaces.VendingMachine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(MainServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			HttpSession session = request.getSession();
			if (session == null) {
				response.sendRedirect("/Vending/login");
			} else {
				String username = (String) session.getAttribute("username");
				PrintWriter out = response.getWriter();
				
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<body>");
				
				out.println("<h2>Soda Machine</h2>");
				out.println("<br\\><br\\>");
				VendingMachine vendingMachine = VendingMachineImpl.getInstance();
				Map<String, Integer> inventory = vendingMachine.getInventory();
				Set<String> products = inventory.keySet();
				for (String product : products) {
					out.println("<div>");
					StringBuilder productDescription = new StringBuilder();
					productDescription.append("<h4>");
					if (inventory.get(product) <= 0) {
						productDescription.append("<del>");
						productDescription.append(product);
						productDescription.append("/<del>");
					} else {
						productDescription.append(product);
					}
					productDescription.append("</h4>");
					out.println(productDescription.toString());
				}
				out.println("<form method=\"post\" action=\"/Vending/deposit\">");
				log.info("Username = " + username);
				log.info("User balance = " + vendingMachine.getAccountBalance(username).toString());
				out.println("Balance: " + vendingMachine.getAccountBalance(username).toString());
				out.println("<br\\>Insert Money: <input type=\"text\" name=\"money\"><br\\><br\\>");
				out.println("<input type=\"submit\" value=\"Insert Money\">");
				out.println("</form>");
				
				out.println("<form method=\"post\" action=\"/Vending/product\">");
				out.println("<br\\>Select Product: <input type=\"text\" name=\"product\"><br\\><br\\>");
				out.println("<input type=\"submit\" value=\"Dispense\">");
				out.println("</form>");

				out.println("</html>");
				out.println("</body>");
				out.close();
			}
		} catch (Exception e) {
			log.error("Unexpected error", e);
			response.sendError(500);
		}
		
	}
	
}