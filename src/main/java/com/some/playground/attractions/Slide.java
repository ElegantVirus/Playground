package com.some.playground.attractions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Slide extends PlaySite {
    public Slide(int maxKids, int snapshotInterval) {
        super(maxKids, snapshotInterval);
    }
}
