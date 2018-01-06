package domain;

import java.math.BigDecimal;

public final class Quarter extends Coin
{
    public static final String QUARTER = "quarter";
    public Quarter()  {
        super(QUARTER, new BigDecimal("0.25"));
    }
    
}
