package com.some.playground;

import com.some.playground.attractions.*;
import com.some.playground.visitors.Kid;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class ManagerTest {
    private final Manager playgroundManager = new Manager();

    @Test
    public void testAddingPlaySites() {
        playgroundManager.addPlaySite(new Carousel(5, 2));
        playgroundManager.addPlaySite(new Slide(10, 1));
        playgroundManager.addPlaySite(new DoubleSwings(3));
        playgroundManager.addPlaySite(new DoubleSwings(3));
        playgroundManager.addPlaySite(new BallPit(15, 4));
        assert playgroundManager.getPlaySiteList().size() == 5;
        assert playgroundManager.getPlaySiteList().get(0) instanceof Carousel;
    }

    @Test
    public void kidWhoDoesNotWannaWait() throws IllegalAccessException {
        playgroundManager.addPlaySite(new Slide(1, 3));
        PlaySite slide = playgroundManager.getPlaySiteList().get(0);
        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, true);
        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, false);
        playgroundManager.addKidToThePlaySite(slide, simpleKid1);
        try {
            playgroundManager.addKidToThePlaySite(slide, simpleKid2);
        } catch (IllegalAccessException e) {
            assert e.getMessage().equals("The kid does not accept to wait in the queue!");
        }
    }

    @Test
    public void testQueueWithVipKids() throws IllegalAccessException {
        playgroundManager.addPlaySite(new Slide(1, 3));
        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, true);
        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, true);
        Kid simpleKid3 = new Kid("Simple3", 6, "3", false, true);
        Kid simpleKid4 = new Kid("Simple4", 6, "4", false, true);
        Kid simpleKid5 = new Kid("Simple5", 6, "5", false, true);
        Kid simpleKid6 = new Kid("Simple6", 6, "6", false, true);
        Kid vipKid7 = new Kid("Vip7", 6, "7", true, true);
        Kid vipKid8 = new Kid("Vip8", 6, "8", true, true);
        PlaySite slide = playgroundManager.getPlaySiteList().get(0);
        playgroundManager.addKidToThePlaySite(slide, simpleKid1);
        playgroundManager.addKidToThePlaySite(slide, simpleKid2);
        playgroundManager.addKidToThePlaySite(slide, simpleKid3);
        playgroundManager.addKidToThePlaySite(slide, simpleKid4);
        playgroundManager.addKidToThePlaySite(slide, simpleKid5);
        playgroundManager.addKidToThePlaySite(slide, simpleKid6);
        playgroundManager.addKidToThePlaySite(slide, vipKid7);
        playgroundManager.addKidToThePlaySite(slide, vipKid8);
        assert playgroundManager.getPlaySitesPlayingKids(slide).size() == 1;

        List<Kid> PlaySitesKidsInQueue = playgroundManager.getPlaySitesKidsInQueue(slide);
        assert PlaySitesKidsInQueue.size() == 7;
        assert PlaySitesKidsInQueue.get(0).equals(vipKid7);
        assert PlaySitesKidsInQueue.get(4).equals(vipKid8);
    }

    @Test
    public void testOnePlaySiteWithSnapshots() throws IllegalAccessException, InterruptedException {
        playgroundManager.addPlaySite(new DoubleSwings(2));
        PlaySite doubleSwings = playgroundManager.getPlaySiteList().get(0);

        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, false);
        playgroundManager.addKidToThePlaySite(doubleSwings, simpleKid1);
        synchronized (this) {
            wait(5000);
        }
        List<PlaySite.PlaySiteSnapshot> snapshots = playgroundManager.getSnapshotsOfThePlaySite(doubleSwings);
        assert snapshots.size() == 3;
        assert snapshots.get(0).getUtilization().equals(BigDecimal.valueOf(0));

        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, false);
        playgroundManager.addKidToThePlaySite(doubleSwings, simpleKid2);
        synchronized (this) {
            wait(5000);
        }
        snapshots = playgroundManager.getSnapshotsOfThePlaySite(doubleSwings);
        assert snapshots.get(4).getUtilization().equals(BigDecimal.valueOf(100));
        playgroundManager.removeKidFromThePlaySite(doubleSwings, simpleKid1);

        List<Kid.KidsHistory> historyOfTheKid = playgroundManager.getHistoryOfTheKid(simpleKid1);
        assert historyOfTheKid.size() == 1;
        assert historyOfTheKid.get(0).getDurationSpent().getSeconds() == 10;

        assert playgroundManager.getTotalKidsOnPlaySite(doubleSwings) == 2;
        assert playgroundManager.getTotalKidsOnAllPlaySites() == 2;
    }

    @Test
    public void testCoupleOfPlaySites() throws IllegalAccessException {
        playgroundManager.addPlaySite(new DoubleSwings(2));
        playgroundManager.addPlaySite(new Carousel(4, 2));
        playgroundManager.addPlaySite(new Carousel(6, 2));
        playgroundManager.addPlaySite(new BallPit(10, 2));
        PlaySite doubleSwings = playgroundManager.getPlaySiteList().get(0);
        PlaySite carousel1 = playgroundManager.getPlaySiteList().get(1);
        PlaySite carousel2 = playgroundManager.getPlaySiteList().get(2);
        PlaySite ballPit = playgroundManager.getPlaySiteList().get(3);

        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, true);
        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, true);
        Kid simpleKid3 = new Kid("Simple3", 6, "3", false, true);
        Kid simpleKid4 = new Kid("Simple4", 6, "4", false, true);
        playgroundManager.addKidToThePlaySite(doubleSwings, simpleKid1);
        playgroundManager.addKidToThePlaySite(doubleSwings, simpleKid2);
        playgroundManager.addKidToThePlaySite(carousel1, simpleKid3);
        playgroundManager.addKidToThePlaySite(ballPit, simpleKid4);
        playgroundManager.removeKidFromThePlaySite(doubleSwings, simpleKid2);
        playgroundManager.addKidToThePlaySite(carousel1, simpleKid2);
        playgroundManager.removeKidFromThePlaySite(carousel1, simpleKid2);
        playgroundManager.removeKidFromThePlaySite(ballPit, simpleKid4);
        playgroundManager.addKidToThePlaySite(ballPit, simpleKid4);

        assert playgroundManager.getTotalKidsOnPlaySite(carousel2) == 0;
        assert playgroundManager.getTotalKidsOnPlaySite(doubleSwings) == 2;
        assert playgroundManager.getTotalKidsOnPlaySite(carousel1) == 2;
        assert playgroundManager.getTotalKidsOnPlaySite(ballPit) == 1;
        assert playgroundManager.getTotalKidsOnAllPlaySites() == 4;

        assert playgroundManager.getHistoryOfTheKid(simpleKid2).get(0).getPlaySite() instanceof DoubleSwings;
        assert playgroundManager.getHistoryOfTheKid(simpleKid2).get(1).getPlaySite() instanceof Carousel;
    }

}
