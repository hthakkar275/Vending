package domain;

import java.math.BigDecimal;

public class Coin  {
    private final BigDecimal value;
    private final String name;
    
    protected Coin(String name, BigDecimal value)  {
        this.value = value;
        this.name = name;
    }
    
    public Coin()  {
        value = new BigDecimal("0.0");
        name = "";
    }
    
    public String getCoinType() {
        return name;
    }
    
    public BigDecimal getValue() {
        return value;
    }
}
