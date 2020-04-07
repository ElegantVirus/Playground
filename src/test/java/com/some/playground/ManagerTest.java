package com.some.playground;

import com.some.playground.attractions.*;
import com.some.playground.visitors.Kid;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class ManagerTest {
    final Manager playgroundManager = new Manager();

    @Test
    public void testAddingPlaysites() {
        playgroundManager.addPlaysite(new Carousel(5, 2));
        playgroundManager.addPlaysite(new Slide(10, 1));
        playgroundManager.addPlaysite(new DoubleSwings(2, 3));
        playgroundManager.addPlaysite(new DoubleSwings(2, 3));
        playgroundManager.addPlaysite(new Ballpit(15, 4));
        assert playgroundManager.getPlaySiteList().size() == 5;
        assert playgroundManager.getPlaySiteList().get(0) instanceof Carousel;
    }

    @Test
    public void kidWhoDoesntWannaWait() throws IllegalAccessException {
        playgroundManager.addPlaysite(new Slide(1, 3));
        PlaySite slide = playgroundManager.getPlaySiteList().get(0);
        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, true);
        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, false);
        playgroundManager.addKidToThePlaysite(slide, simpleKid1);
        try {
            playgroundManager.addKidToThePlaysite(slide, simpleKid2);
        } catch (IllegalAccessException e) {
            assert e.getMessage().equals("The kid does not accept to wait in the queue!");
        }
    }

    @Test
    public void testQueueWithVipKids() throws IllegalAccessException {
        playgroundManager.addPlaysite(new Slide(1, 3));
        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, true);
        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, true);
        Kid simpleKid3 = new Kid("Simple3", 6, "3", false, true);
        Kid simpleKid4 = new Kid("Simple4", 6, "4", false, true);
        Kid simpleKid5 = new Kid("Simple5", 6, "5", false, true);
        Kid simpleKid6 = new Kid("Simple6", 6, "6", false, true);
        Kid vipKid7 = new Kid("Vip7", 6, "7", true, true);
        Kid vipKid8 = new Kid("Vip8", 6, "7", true, true);
        PlaySite slide = playgroundManager.getPlaySiteList().get(0);
        playgroundManager.addKidToThePlaysite(slide, simpleKid1);
        playgroundManager.addKidToThePlaysite(slide, simpleKid2);
        playgroundManager.addKidToThePlaysite(slide, simpleKid3);
        playgroundManager.addKidToThePlaysite(slide, simpleKid4);
        playgroundManager.addKidToThePlaysite(slide, simpleKid5);
        playgroundManager.addKidToThePlaysite(slide, simpleKid6);
        playgroundManager.addKidToThePlaysite(slide, vipKid7);
        playgroundManager.addKidToThePlaysite(slide, vipKid8);
        assert playgroundManager.getPlaysitesPlayingKids(slide).size() == 1;

        List<Kid> playsitesKidsInQueue = playgroundManager.getPlaysitesKidsInQueue(slide);
        assert playsitesKidsInQueue.size() == 7;
        assert playsitesKidsInQueue.get(0).equals(vipKid7);
        assert playsitesKidsInQueue.get(4).equals(vipKid8);
    }

    @Test
    public void testOnePlaysite() throws IllegalAccessException, InterruptedException {
        playgroundManager.addPlaysite(new DoubleSwings(2, 2));
        PlaySite doubleSwings = playgroundManager.getPlaySiteList().get(0);

        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, false);
        playgroundManager.addKidToThePlaysite(doubleSwings, simpleKid1);
        synchronized (this) {
            wait(5000);
        }
        List<PlaySite.PlaysiteSnapshot> snapshots = playgroundManager.getSnapshotsOfThePlaysite(doubleSwings);
        assert snapshots.size() == 3;
        assert snapshots.get(0).getUtilization().equals(BigDecimal.valueOf(0));

        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, false);
        playgroundManager.addKidToThePlaysite(doubleSwings, simpleKid2);
        synchronized (this) {
            wait(5000);
        }
        snapshots = playgroundManager.getSnapshotsOfThePlaysite(doubleSwings);
        assert snapshots.get(4).getUtilization().equals(BigDecimal.valueOf(100));
        playgroundManager.removeKidFromThePlaysite(doubleSwings, simpleKid1);

        List<Kid.KidsHistory> historyOfTheKid = playgroundManager.getHistoryOfTheKid(simpleKid1);
        assert historyOfTheKid.size() == 1;
        assert historyOfTheKid.get(0).getDurationSpent().getSeconds() == 10;

        assert playgroundManager.getTotalKidsOnPlaysite(doubleSwings) == 2;
        assert playgroundManager.getTotalKidsOnAllPlaysites() == 2;
    }

    @Test
    public void testCoupleOfPlaysites() throws IllegalAccessException {
        playgroundManager.addPlaysite(new DoubleSwings(2, 2));
        playgroundManager.addPlaysite(new Carousel(4, 2));
        playgroundManager.addPlaysite(new Carousel(6, 2));
        playgroundManager.addPlaysite(new Ballpit(10, 2));
        PlaySite doubleSwings = playgroundManager.getPlaySiteList().get(0);
        PlaySite carousel1 = playgroundManager.getPlaySiteList().get(1);
        PlaySite carousel2 = playgroundManager.getPlaySiteList().get(2);
        PlaySite ballpit = playgroundManager.getPlaySiteList().get(3);

        Kid simpleKid1 = new Kid("Simple1", 6, "1", false, true);
        Kid simpleKid2 = new Kid("Simple2", 6, "2", false, true);
        Kid simpleKid3 = new Kid("Simple3", 6, "3", false, true);
        Kid simpleKid4 = new Kid("Simple4", 6, "4", false, true);
        playgroundManager.addKidToThePlaysite(doubleSwings, simpleKid1);
        playgroundManager.addKidToThePlaysite(doubleSwings, simpleKid2);
        playgroundManager.addKidToThePlaysite(carousel1, simpleKid3);
        playgroundManager.addKidToThePlaysite(ballpit, simpleKid4);
        playgroundManager.removeKidFromThePlaysite(doubleSwings, simpleKid2);
        playgroundManager.addKidToThePlaysite(carousel1, simpleKid2);
        playgroundManager.removeKidFromThePlaysite(carousel1, simpleKid2);
        playgroundManager.removeKidFromThePlaysite(ballpit, simpleKid4);
        playgroundManager.addKidToThePlaysite(ballpit, simpleKid4);

        assert playgroundManager.getTotalKidsOnPlaysite(carousel2) == 0;
        assert playgroundManager.getTotalKidsOnPlaysite(doubleSwings) == 2;
        assert playgroundManager.getTotalKidsOnPlaysite(carousel1) == 2;
        assert playgroundManager.getTotalKidsOnPlaysite(ballpit) == 1;
        assert playgroundManager.getTotalKidsOnAllPlaysites() == 4;
    }

}
