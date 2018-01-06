package domain;

import java.math.BigDecimal;

public final class Dime extends Coin
{
    public static final String DIME = "dime";
    public Dime() {
        super(DIME, new BigDecimal("0.10"));
    }
}
