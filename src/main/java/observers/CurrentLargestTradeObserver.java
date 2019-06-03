package observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.Data;
import model.Trade;
import repository.PersistenceLayer;

@Data
public class CurrentLargestTradeObserver implements Observer<Trade> {
    private final PersistenceLayer persistenceLayer;

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onNext(Trade trade) {
        Trade currentLargestTrade = persistenceLayer.getLargestTradeForInstrument(trade.getSymbol());

        if(currentLargestTrade == null || trade.compareTo(currentLargestTrade) > 0){
            persistenceLayer.setLargestTradeForInstrument(trade);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
