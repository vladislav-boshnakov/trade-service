package observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.Data;
import model.Trade;
import repository.PersistenceLayer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Data
public class CumulativeMovingAverageObserver implements Observer<Trade> {

    private final PersistenceLayer persistenceLayer;

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onNext(Trade trade) {
        BigDecimal prevCma = persistenceLayer.getAverageTradePriceForInstrument(trade.getSymbol());
        int tradeCount = persistenceLayer.getTradeCountForInstrument(trade.getSymbol());

        BigDecimal cmaCalc = prevCma.add(trade
                .getValue()
                .subtract(prevCma)
                .divide(BigDecimal.valueOf(tradeCount + 1), RoundingMode.DOWN),
                MathContext.DECIMAL64
        );

        persistenceLayer.incrementTradeCountForInstrument(trade);
        persistenceLayer.setAverageTradePriceForInstrument(trade.getSymbol(), cmaCalc);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
