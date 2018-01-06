package domain;

import java.math.BigDecimal;

public class Penny extends Coin  {
    public static final String PENNY = "penny";
    public Penny() {
        super(PENNY, new BigDecimal("0.01"));
    }
}
