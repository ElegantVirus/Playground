package com.some.playground.attractions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BallPit extends PlaySite {
    public BallPit(int maxKids, int snapshotInterval) {
        super(maxKids, snapshotInterval);
    }
}
