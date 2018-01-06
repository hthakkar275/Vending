package application;

/**
 * The entry point for starting the local InvestOne application, as used when running the application within an IDE.
 */
public final class Main {

	private static final String minimumJavaVersion = "1.8.0_05";

	private Main() {
		// Utility Class
	}

	public static void main(String[] args) {

		
		// Force a minimum Java version
		String javaVersion = System.getProperty("java.version");
		if (javaVersion.compareTo(minimumJavaVersion) < 0) {
			System.err.format("Not started because the Java version %s does not meet the minimum version of %s%n",
					javaVersion, minimumJavaVersion);
			return;
		}

		new EmbeddedTomcat().run();
	}
}
