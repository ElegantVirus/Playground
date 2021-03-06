package com.some.playground.attractions;

import com.some.playground.visitors.Kid;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/*
   "Some not yet known play sites can have different calculation
   implementations, this possible extension requirement should reflect in design."
   The new classes can extend PlaySite to function properly in the context.
 */
@Getter
@Setter
public abstract class PlaySite {
    private List<Kid> waitingKids;
    private List<Kid> playingKids;
    private List<Kid> kidsWhoHaveVisited;
    private List<PlaySiteSnapshot> playSiteSnapshotList;
    private int maxKids;
    private int snapshotInterval;

    public PlaySite(int maxKids, int snapshotInterval) {
        waitingKids = new ArrayList<>();
        playingKids = new ArrayList<>();
        kidsWhoHaveVisited = new ArrayList<>();
        playSiteSnapshotList = new ArrayList<>();
        this.maxKids = maxKids;
        this.snapshotInterval = snapshotInterval;
        registerSnapshot();
    }

    /**
     * Play site utilization is calculated differently for each play site
     * Most of play sites utilization is percent of capacity taken.
     * Utilization is measured in percents
     *
     * @return utilization percents.
     */
    BigDecimal getUtilizationPercents() {
        return BigDecimal.valueOf((getPlayingKids().size() * 100) / getMaxKids());
    }

    public boolean tooManyKids() {
        return playingKids.size() == maxKids;
    }

    /**
     * it should be possible to enqueue kid or receive negative result
     * if kid does not accept waiting in queue.
     *
     * @param kid the kid.
     * @throws IllegalAccessException if the kid is impatient.
     */
    public void addKid(Kid kid) throws IllegalAccessException {
        if (tooManyKids()) {
            if (kid.isWillingToWait()) {
                if (kid.isVip()) {
                    makeVipWait(kid);
                } else {
                    waitingKids.add(kid);
                }
            } else {
                throw new IllegalAccessException("The kid does not accept to wait in the queue!");
            }
        } else {
            startKidsHistoryRecord(kid);
            kidsWhoHaveVisited.add(kid);
            playingKids.add(kid);
        }
    }

    private void startKidsHistoryRecord(Kid kid) {
        Kid.KidsHistory kidsHistory = new Kid.KidsHistory(this, LocalDateTime.now());
        kid.getHistoryList().add(kidsHistory);
    }

    /**
     * This implementation assumes that the ticket works as a subscription
     * for all rides and you can have either a normal one or a vip one.
     * It is also assumed that if the vip kid is in the back of the queue
     * and another one comes along, the latter vip kid will be put after the first vip kid.
     *
     * @param kid the kid.
     */
    private void makeVipWait(Kid kid) {
        Kid lastVipKid = getLastVipKid();

        if (lastVipKid == null) {
            waitingKids.add(0, kid);
        } else {
            int index = waitingKids.indexOf(lastVipKid);
            if (index + 4 > waitingKids.size()) {
                waitingKids.add(kid);
            } else {
                waitingKids.add(index + 4, kid);
            }
        }
    }

    private Kid getLastVipKid() {
        return waitingKids
                .stream()
                .filter(Kid::isVip)
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public void removeKid(Kid kid) {
        if (playingKids.contains(kid)) {
            playingKids.remove(kid);
            endKidsHistoryRecord(kid);
        }
    }

    private void endKidsHistoryRecord(Kid kid) {
        kid.getHistoryList().get(kid.getHistoryList().size() - 1).setEndTime(LocalDateTime.now());
    }

    private void registerSnapshot() {
        PlaySite playSite = this;
        TimerTask task = new TimerTask() {
            public void run() {
                playSiteSnapshotList.add(new PlaySiteSnapshot(playSite, getUtilizationPercents()));
            }
        };

        new Timer().scheduleAtFixedRate(task, 0, snapshotInterval * 1000);
    }

    @Value
    public static class PlaySiteSnapshot {
        PlaySite playSite;
        BigDecimal utilization;
    }
}
