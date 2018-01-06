package domain;

import java.math.BigDecimal;

public final class Nickel extends Coin
{
    public static final String NICKEL = "nickel";

    public Nickel() {
        super(NICKEL, new BigDecimal("0.10"));
    }
}
