package application;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmbeddedTomcat {

	private static Log log = LogFactory.getLog(EmbeddedTomcat.class);

	public void run() {
		try {
			long startTime = System.currentTimeMillis();

			// Initialization.
			String userDir = System.getProperty("user.dir");
			int httpsPortNumber = Integer.parseInt(System.getProperty("mercury.callback.port", "3443"));
			int httpPortNumber = 12345;
			SimpleFormatter simpleFormatter = new SimpleFormatter() {
				public String format(LogRecord logRecord) {
					if (logRecord.getLoggerName().matches("(org.apache.catalina|org.apache.tomcat).*")
							&& logRecord.getLevel() == Level.INFO) {
						return "";
					}
					if (logRecord.getLoggerName().matches("org.apache.tomcat.util.scan.StandardJarScanner") 
							&& logRecord.getThrown() instanceof java.io.FileNotFoundException) {
						return "";
					}
					if (logRecord.getThrown() != null) {
						logRecord.getThrown().printStackTrace(System.err);
					}
					return String.format("%5s %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS  00-000 %3$s: %4$s%n", logRecord
							.getLevel(), new Date(logRecord.getMillis()),
							logRecord.getLoggerName().replaceFirst(".*\\.(.*)", "$1"), logRecord.getMessage());
				}
			};
			Logger.getLogger("").getHandlers()[0].setFormatter(simpleFormatter);

			log.info(String.format("Starting on port %d.", httpsPortNumber));

			// Setup Tomcat.
			Tomcat tomcat = new Tomcat();
			File file = new File("workingstore");
			file.mkdirs();
			tomcat.setBaseDir(file.getAbsolutePath());
			tomcat.setSilent(true);
			tomcat.noDefaultWebXmlPath();
			tomcat.setPort(httpPortNumber);
			String webappDir = userDir + "/webapp";
			Context InvestOneContext = tomcat.addWebapp("/Vending", webappDir);
			enableAccessLogs(InvestOneContext);
			InvestOneContext.setUseHttpOnly(false);

			// Setup SSL Off-loading
			//addSecureConnector(httpsPortNumber, tomcat);

			// Start Tomcat.
			Logger.getLogger("").getHandlers()[0].setFormatter(simpleFormatter);
			tomcat.start();
			if (tomcat.getConnector().getState() != LifecycleState.STARTED) {
				throw new Exception("Tomcat not properly started");
			}
			log.info(String.format("Started on port %d in %d seconds. %n%n", httpPortNumber,
				(System.currentTimeMillis() - startTime) / 1000));
			Thread.sleep(Long.MAX_VALUE);

			// Used to verify graceful un-deployment of the applications.
			// Set the prior sleep time to a smaller value to actually exercise this code.
			InvestOneContext.stop();
			Thread.sleep(30000);

		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}

	private void addSecureConnector(int securePort, Tomcat tomcat) {
		Connector connector = new Connector();
		connector.setPort(securePort);
		connector.setSecure(true);
		connector.setScheme("https");
		connector.setAttribute("keyAlias", "local");
		connector.setAttribute("keystorePass", "localkey");
		String userDir = System.getProperty("user.dir");
		connector.setAttribute("keystoreFile", userDir + "/src/dev/resources/.keystore");
		connector.setAttribute("clientAuth", "false");
		connector.setAttribute("sslProtocol", "TLS");
		connector.setAttribute("SSLEnabled", true);
		tomcat.getService().addConnector(connector);
	}

	/**
	 * Enables the access logs if the system property "access.logs.enable" is set to "true". The access log file is
	 * generated in the "MercuryEmbeddedTomcat/logs" with the name pattern investone_access_log_<day>.log. Here <day> is
	 * the day of the month.
	 * 
	 * @param InvestOneContext
	 *            the web app context
	 */
	private void enableAccessLogs(Context InvestOneContext) {
		String enableAccessLog = System.getProperty("access.logs.enable", "false");
		if ("true".equalsIgnoreCase(enableAccessLog)) {
			AccessLogValve accessLog = new AccessLogValve();

			// Rotate on date changes.
			accessLog.setRotatable(true);
			accessLog.setFileDateFormat("dd");
			accessLog.setDirectory("../logs");
			accessLog.setPrefix("Investone_access_log_");
			accessLog.setSuffix(".txt");
			// More patterns in the API documentation for AccessLogValve.
			accessLog.setPattern("%t %m %U %D %s");
			accessLog.setEnabled(true);
			((StandardContext) InvestOneContext).addValve(accessLog);
		}
	}
}