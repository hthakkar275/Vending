package domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import exceptions.InvalidMoney;

public final class Money  {
	private static Log log = LogFactory.getLog(Money.class);
	private static final BigDecimal HUNDRED = new BigDecimal("100");
	private static final BigDecimal QUARTER_CENTS = new BigDecimal("25");
	private static final BigDecimal DIME_CENTS = new BigDecimal("10");
	private static final BigDecimal NICKEL_CENTS = new BigDecimal("5");
	private final BigDecimal value;
	private final BigDecimal quarters;
	private final BigDecimal dimes;
	private final BigDecimal nickels;
	private final BigDecimal pennies;
	
	public Money(String valueStr) throws InvalidMoney {	
		log.info("Constructor with arg " + valueStr);
		this.value = new BigDecimal(valueStr).stripTrailingZeros();
		if ((this.value.scale() > 2) || (value.toBigInteger().intValue() < 0)) {
            throw new InvalidMoney("Money must be a positive two or less decimal point precision value");
		}
        BigDecimal cents = value.multiply(HUNDRED);
		log.info("cents " + cents);

        quarters = cents.divide(QUARTER_CENTS, 0, RoundingMode.DOWN);
        cents = cents.subtract(quarters.multiply(QUARTER_CENTS));
        dimes = cents.divide(DIME_CENTS, 0, RoundingMode.DOWN);
        cents = cents.subtract(dimes.multiply(DIME_CENTS));
        nickels = cents.divide(NICKEL_CENTS, 0, RoundingMode.DOWN);
        cents = cents.subtract(nickels.multiply(NICKEL_CENTS));
        pennies = cents;        
	}
	
	public Money(BigDecimal money) throws InvalidMoney  {
	    if ((money.stripTrailingZeros().scale() > 2) || (money.toBigInteger().intValue() < 0)) {
	        throw new InvalidMoney("Money must be a positive two or less decimal point precision value");
	    }
	    this.value = money;
        BigDecimal cents = value.multiply(HUNDRED);
        quarters = cents.divide(QUARTER_CENTS, 0, RoundingMode.DOWN);
        cents = cents.subtract(quarters.multiply(QUARTER_CENTS));
        dimes = cents.divide(DIME_CENTS, 0, RoundingMode.DOWN);
        cents = cents.subtract(dimes.multiply(DIME_CENTS));
        nickels = cents.divide(NICKEL_CENTS, 0, RoundingMode.DOWN);
        cents = cents.subtract(nickels.multiply(NICKEL_CENTS));
        pennies = cents;        
	}
	
	public Money(Money money) throws InvalidMoney {
	    this(money.getValue());
	}
	
	public Money()  {
		value = new BigDecimal("0.00");
		quarters = new BigDecimal("0");
		dimes = new BigDecimal("0");
		nickels = new BigDecimal("0");
		pennies = new BigDecimal("0");
	}
	
	public Money add(Money money) throws InvalidMoney {
	    BigDecimal thisValue = this.value;
	    BigDecimal otherValue = money.getValue();
	    Money newMoney = new Money(thisValue.add(otherValue));
		return newMoney;
	}
	
	public Money subtract(Money money) throws InvalidMoney {
        BigDecimal thisValue = this.value;
        BigDecimal otherValue = money.getValue();
        Money newMoney = new Money(thisValue.subtract(otherValue));
        return newMoney;
	}
	
	public Coin[] toCoins() {	  
	    int qCount = getQuartersCount();
	    int dCount = getDimesCount();
	    int nCount = getNickelsCount();
	    int pCount = getPenniesCount();
	    int totalCoinCount = qCount + dCount + nCount + pCount;
	    Coin[] coins = new Coin[totalCoinCount];
	    totalCoinCount = 0;
	    for (int count = 0; count < qCount; count++) {
	        coins[totalCoinCount++] = new Quarter();
	    }
	    for (int count = 0; count < dCount; count++) {
            coins[totalCoinCount++] = new Dime();
	    }
	    for (int count = 0; count < nCount; count++) {
	        coins[totalCoinCount++] = new Nickel();
	    }
	    for (int count = 0; count < pCount; count++) {
	        coins[totalCoinCount++] = new Penny();
	    }
	    return coins;
	}
	
	public int getPenniesCount() {
		return pennies.intValue();
	}
	public int getQuartersCount() {
		return quarters.intValue();
	}
	public int getDimesCount() {
		return dimes.intValue();
	}
	public int getNickelsCount() {
		return nickels.intValue();
	}
	public BigDecimal getValue() {
		return value;
	}
    public int compareTo(Money rhs) {
        BigDecimal lhsValue = this.value;
        BigDecimal rhsValue = rhs.value;
        int cmpResult = lhsValue.compareTo(rhsValue);
        return cmpResult;
    }
    public String toString() {
    	String valueStr = "0.00";
    	BigDecimal value = new BigDecimal("0");
    	value.setScale(2);
    	Coin[] coins = toCoins();
    	log.info("Coins count = " + coins.length);
    	for (Coin coin : coins) {
    		log.info("Adding coin value " + coin.getValue());
    		value = value.add(coin.getValue());
    	}
		log.info("final value " + value);

    	valueStr = value.toString();
    	return valueStr;
    }
}
