package manager;

import io.reactivex.Observable;
import lombok.Data;
import model.Trade;
import observers.CumulativeMovingAverageObserver;
import observers.CurrentLargestTradeObserver;
import repository.PersistenceLayer;

@Data
public class TradeManager {
    private final PersistenceLayer persistenceLayer;

    public void handle(Observable<Trade> incomingTradeStream) {
        incomingTradeStream
                .doOnEach(new CumulativeMovingAverageObserver(persistenceLayer))
                .doOnEach(new CurrentLargestTradeObserver(persistenceLayer))
                .blockingSubscribe();
    }
}
