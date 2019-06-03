package model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Trade implements Comparable<Trade> {
    private final long timestamp;
    private final String symbol;
    private final BigDecimal price;
    private final int size;
    private final String flags;

    @Override
    public int compareTo(Trade o) {
        return this.getValue().compareTo(o.getValue());
    }

    public BigDecimal getValue(){
        return price.multiply(BigDecimal.valueOf(size));
    }
}
