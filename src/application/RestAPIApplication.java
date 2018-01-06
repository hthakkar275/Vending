package application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import api.AccountFacade;

//@ApplicationPath("/Vending/rest")
public class RestAPIApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public RestAPIApplication() {
		singletons.add(new AccountFacade());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}