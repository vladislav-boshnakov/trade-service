package repository;

import model.Trade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is an abstraction over some persistence layer
 * I don't think that I would need a full database considering
 * this entire project is going to showcase some event driven reactive stuff
 * and a little bit of data processing
 * <p>
 * For the record, in an actual system you'd probably want to come up with some sort
 * of persistence strategy for your data derivations, and queries should be run on them.
 *
 * @author Vladislav Boshnakov
 */
public class PersistenceLayer {

    private Map<String, BigDecimal> averageTradePriceForInstruments = new HashMap<>();
    private Map<String, Trade> largestTradeForInstrument = new HashMap<>();
    private Map<String, Integer> tradeCountForInstrument = new HashMap<>();

    public void setAverageTradePriceForInstrument(String i, BigDecimal newAvg) {
        this.averageTradePriceForInstruments.put(i, newAvg);
    }

    public BigDecimal getAverageTradePriceForInstrument(String i) {
        return averageTradePriceForInstruments.getOrDefault(i, BigDecimal.ZERO);
    }

    public void setLargestTradeForInstrument(Trade t) {
        this.largestTradeForInstrument.put(t.getSymbol(), t);
    }

    public Trade getLargestTradeForInstrument(String i) {
        return this.largestTradeForInstrument.get(i);
    }

    // Seriously doubting someone would have a piece of code like this
    // just run a count query on the DB indexed column
    public void incrementTradeCountForInstrument(Trade t) {
        //To satisfy the task we need to keep a count of trades for the base instrument
        //and to make things easier I'm also adding the ability to query
        //for a symbol with flags in this same method, a little messy but quick

        String instrumentWithFlag = t.getSymbol() + " " + t.getFlags();

        //account for missing data
        this.tradeCountForInstrument.putIfAbsent(t.getSymbol(), 0);
        this.tradeCountForInstrument.putIfAbsent(instrumentWithFlag, 0);

        //We will use this for our moving average
        int oldValue = this.tradeCountForInstrument.get(t.getSymbol());
        this.tradeCountForInstrument.replace(t.getSymbol(), oldValue, ++oldValue);

        //we will use this to satisfy the customer query
        int oldValueFlag = this.tradeCountForInstrument.get(instrumentWithFlag);
        this.tradeCountForInstrument.replace(instrumentWithFlag, oldValueFlag, ++oldValueFlag);
    }

    public int getTradeCountForInstrument(String symbol, String flag) {
        return this.tradeCountForInstrument.get(symbol + " " + flag);
    }

    public int getTradeCountForInstrument(String symbol){
        return  this.tradeCountForInstrument.getOrDefault(symbol, 0);
    }

}
