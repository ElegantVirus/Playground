package com.some.playground.attractions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Carousel extends PlaySite {
    public Carousel(int maxKids, int snapshotInterval) {
        super(maxKids, snapshotInterval);
    }
}
