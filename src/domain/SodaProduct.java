package domain;

import exceptions.InvalidMoney;

public class SodaProduct {
	private final String name;
	private final Money cost;
	private final String costAsStr;
	
	public SodaProduct(String name, String costStr) throws InvalidMoney {
		this.name = name;
		this.cost = new Money(costStr);
		this.costAsStr = costStr;
	}

	public SodaProduct(SodaProduct soda) {
		this.name = soda.getName();
		this.costAsStr = soda.getCostAsStr();
		this.cost = soda.getCost();
	}
	
	public String getName() {
		return name;
	}

	public Money getCost() {
		return cost;
	}

	protected String getCostAsStr()  {
		return costAsStr;
	}
	
}
