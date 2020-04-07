package com.some.playground.attractions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class Ballpit extends PlaySite {

    public Ballpit(int maxKids, int snapshotInterval) {
        super(maxKids, snapshotInterval);
    }

    @Override
    BigDecimal getUtilizationPercents() {
        return BigDecimal.valueOf((this.playingKids.size() * 100) / this.maxKids);
    }
}