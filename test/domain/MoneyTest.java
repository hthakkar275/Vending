package domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Coin;
import domain.Dime;
import domain.Money;
import domain.Nickel;
import domain.Penny;
import domain.Quarter;

import exceptions.InvalidMoney;

public class MoneyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMoney1() {
	    Coin[] coins = new Coin[0];
	    Money testMoney = null;
		try {
            testMoney = new Money("1.16");
            coins = testMoney.toCoins();
        }
        catch (InvalidMoney e) {
            fail("Unexpected InvalidMoney exception");
        }	
        assertEquals(4, testMoney.getQuartersCount());
        assertEquals(1, testMoney.getDimesCount());
        assertEquals(1, testMoney.getNickelsCount());
        assertEquals(1, testMoney.getPenniesCount());
	}
	
	@Test
	public void testMoney2() {
	    Coin[] coins = new Coin[0];
	    Money testMoney = null;
	    try {
	        testMoney = new Money("000.4300");
	        coins = testMoney.toCoins();
	    }
	    catch (InvalidMoney e) {
	        fail("Unexpected InvalidMoney exception");
	    }   
        assertEquals(1, testMoney.getQuartersCount());
        assertEquals(1, testMoney.getDimesCount());
        assertEquals(1, testMoney.getNickelsCount());
        assertEquals(3, testMoney.getPenniesCount());
	}

    @Test
    public void testMoney3() {
        Coin[] coins = new Coin[0];
        try {
            Money testMoney = new Money(".44");
            coins = testMoney.toCoins();
        }
        catch (InvalidMoney e) {
            fail("Unexpected InvalidMoney exception");
        }       
    }


	@Test
	public void testMoney4() {
	    boolean invalidMoney = false;
	    try {
	        Money testMoney = new Money("1.156");
	    }
        catch (InvalidMoney e) {
            invalidMoney = true;
        }       
        assertTrue("Expected InvalidMoney exception", invalidMoney);
	}
	
	@Test
	public void testMoney5() {
	    boolean invalidMoney = false;
	    try {
	        Money testMoney = new Money("-1.16");
	    }
	    catch (InvalidMoney e) {
	        invalidMoney = true;
	    }       
	    assertTrue("Expected InvalidMoney exception", invalidMoney);
	}

	@Test
	public void testMoneyComparisonEqual() {
	    Money testMoney = null;
	    Money cmpMoney = null;
	    try  {
	        testMoney = new Money("2.48");
	        cmpMoney = new Money("2.48");
	    }
        catch (InvalidMoney e) {
            fail("Unexpected InvalidMoney exception");
        }       
        int cmpResult = testMoney.compareTo(cmpMoney);
        assertEquals(0, cmpResult);	    
	}
	
	@Test 
	public void testMoneyForComparisonLessThan() {
        Money testMoney = null;
        Money cmpMoney = null;
        try  {
            testMoney = new Money("1.48");
            cmpMoney = new Money("2.48");
        }
        catch (InvalidMoney e) {
            fail("Unexpected InvalidMoney exception");
        }       
        int cmpResult = testMoney.compareTo(cmpMoney);
        assertEquals(-1, cmpResult);     
	}
	
	@Test 
	public void testMoneyForComparisonGreaterThan() {
	    Money testMoney = null;
	    Money cmpMoney = null;
	    try  {
	        testMoney = new Money("3.48");
	        cmpMoney = new Money("2.48");
	    }
	    catch (InvalidMoney e) {
	        fail("Unexpected InvalidMoney exception");
	    }       
	    int cmpResult = testMoney.compareTo(cmpMoney);
	    assertEquals(1, cmpResult);     
	}

	@Test
	public void testToCoins() {
	   Money testMoney = null;
       try  {
            testMoney = new Money("1.17");
        }
        catch (InvalidMoney e) {
            fail("Unexpected InvalidMoney exception");
        }       
        Coin[] coins = testMoney.toCoins();
        int qCount = 0;
        int dCount = 0; 
        int nCount = 0;
        int pCount = 0;
        for (Coin aCoin : coins) {
            if (aCoin.getCoinType().equals(Quarter.QUARTER)) {
                qCount++;
            }
            if (aCoin.getCoinType().equals(Dime.DIME)) {
                dCount++;
            }
            if (aCoin.getCoinType().equals(Nickel.NICKEL)) {
                nCount++;
            }
            if (aCoin.getCoinType().equals(Penny.PENNY)) {
                pCount++;
            }
        }
        assertEquals(4, qCount);
        assertEquals(1, dCount);
        assertEquals(1, nCount);
        assertEquals(2, pCount);

	}
}
