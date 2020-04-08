package com.some.playground.attractions;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DoubleSwings extends PlaySite {
    public DoubleSwings(int snapshotInterval) {
        super(2, snapshotInterval);
    }

    @Override
    BigDecimal getUtilizationPercents() {
        if (getPlayingKids().size() == getMaxKids()) {
            return BigDecimal.valueOf(100);
        } else {
            return BigDecimal.valueOf(0);
        }
    }
}
