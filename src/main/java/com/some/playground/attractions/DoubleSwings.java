package com.some.playground.attractions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class DoubleSwings extends PlaySite {

    public DoubleSwings(int maxKids, int snapshotInterval) {
        super(maxKids, snapshotInterval);
    }

    @Override
    BigDecimal getUtilizationPercents() {
        if (this.playingKids.size() == this.maxKids) {
            return BigDecimal.valueOf(100);
        } else {
            return BigDecimal.valueOf(0);
        }
    }
}
