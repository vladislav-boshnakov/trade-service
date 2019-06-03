package manager;

import io.reactivex.Observable;
import model.Trade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.PersistenceLayer;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TradeManagerTest {

    private static final String FIRST_INSTRUMENT = "XYZ LN";
    private static final String SECOND_INSTRUMENT = "123 LN";
    private static final String THIRD_INSTRUMENT = "951 LN";


    private Observable<Trade> incomingTradeStream;
    private TradeManager tradeManager;
    private PersistenceLayer persistenceLayer = new PersistenceLayer();

    @Before
    public void setUp() {
        tradeManager = new TradeManager(persistenceLayer);
    }

    @After
    public void tearDown() {
        tradeManager = null;
        incomingTradeStream = null;
    }

    @Test
    public void testAverageTradePrice_Correct() {
        //given
        Trade[] testTrades = giveMeDefaultTestTrades();
        incomingTradeStream = Observable.fromArray(testTrades);

        //when
        tradeManager.handle(incomingTradeStream);

        //then
        assertEquals("Average of first instrument is correct",
                BigDecimal.valueOf(189385.36), persistenceLayer.getAverageTradePriceForInstrument(FIRST_INSTRUMENT));

        //For some reason BigDecimal.valueOf(498850.00) gives 498850.0
        //but the string constructor works, I'm not going to figure it out here
        //however usually I would spend time to find out what exactly is going on
        assertEquals("Average of second instrument is correct",
                new BigDecimal("498850.00"), persistenceLayer.getAverageTradePriceForInstrument(SECOND_INSTRUMENT));

        assertEquals("Average of third insrument is correct",
                BigDecimal.valueOf(213030.00), persistenceLayer.getAverageTradePriceForInstrument(THIRD_INSTRUMENT));
    }

    @Test
    public void testAverageTradePrice_noTrades(){
        //given
        incomingTradeStream = Observable.empty();

        //when
        tradeManager.handle(incomingTradeStream);

        //then
        assertEquals("Average of first instrument is zero",
                BigDecimal.ZERO, persistenceLayer.getAverageTradePriceForInstrument(FIRST_INSTRUMENT));
        assertEquals("Average of second instrument is zero",
                BigDecimal.ZERO, persistenceLayer.getAverageTradePriceForInstrument(SECOND_INSTRUMENT));
        assertEquals("Average of third insrument is zero",
                BigDecimal.ZERO, persistenceLayer.getAverageTradePriceForInstrument(THIRD_INSTRUMENT));
    }

    @Test
    public void testLargestTrade_Correct(){
        //given
        Trade[] testTrades = giveMeDefaultTestTrades();
        incomingTradeStream = Observable.fromArray(testTrades);

        //when
        tradeManager.handle(incomingTradeStream);

        //then
        assertEquals("Largest Trade for instrument one is correct",
                testTrades[0], persistenceLayer.getLargestTradeForInstrument(FIRST_INSTRUMENT));
        assertEquals("Largest Trade for instrument two is correct",
                testTrades[4], persistenceLayer.getLargestTradeForInstrument(SECOND_INSTRUMENT));
        assertEquals("Largest Trade for instrument three is correct",
                testTrades[5], persistenceLayer.getLargestTradeForInstrument(THIRD_INSTRUMENT));

    }

    @Test
    public void testLargestTrade_noTrades(){
        //given
        incomingTradeStream = Observable.empty();

        //when
        tradeManager.handle(incomingTradeStream);

        //then
        assertNull("Largest Trade for instrument one is null", persistenceLayer.getLargestTradeForInstrument(FIRST_INSTRUMENT));
        assertNull("Largest Trade for instrument two is null", persistenceLayer.getLargestTradeForInstrument(SECOND_INSTRUMENT));
        assertNull("Largest Trade for instrument three is null", persistenceLayer.getLargestTradeForInstrument(THIRD_INSTRUMENT));

    }

    //===================== Helper Methods =============================//
    private Trade[] giveMeDefaultTestTrades() {

        //Three trades of the same type to test average
        Trade trade11 = new Trade(1481107485791L, FIRST_INSTRUMENT, BigDecimal.valueOf(200.00), 1000, "A");
        Trade trade12 = new Trade(1481107485791L, FIRST_INSTRUMENT, BigDecimal.valueOf(200.01), 968, "A");
        Trade trade13 = new Trade(1481107485792L, FIRST_INSTRUMENT, BigDecimal.valueOf(198.80), 878, "A");

        //Two trades with different flags
        Trade trade21 = new Trade(1481107485797L, SECOND_INSTRUMENT, BigDecimal.valueOf(19.87), 20000, "A");
        Trade trade22 = new Trade(1481107485798L, SECOND_INSTRUMENT, BigDecimal.valueOf(20.01), 30000, "B");

        //Simple trade
        Trade trade3 = new Trade(1481107485891L, THIRD_INSTRUMENT, BigDecimal.valueOf(2130.3), 100, "Z");

        return new Trade[]{trade11, trade12, trade13, trade21, trade22, trade3};
    }
}
