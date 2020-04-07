package com.some.playground;

import com.some.playground.attractions.PlaySite;
import com.some.playground.visitors.Kid;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Manager {
    List<PlaySite> playSiteList;

    public Manager() {
        this.playSiteList = new ArrayList<>();
    }

    public void addPlaysite(PlaySite playSite) {
        playSiteList.add(playSite);
    }

    public void addKidToThePlaysite(PlaySite playSite, Kid kid) throws IllegalAccessException {
        playSite.addKid(kid);
    }

    public void removeKidFromThePlaysite(PlaySite playSite, Kid kid) {
        playSite.removeKid(kid);
    }

    public List<Kid.KidsHistory> getHistoryOfTheKid(Kid kid) {
        return kid.getHistoryList();
    }

    public List<PlaySite.PlaysiteSnapshot> getSnapshotsOfThePlaysite(PlaySite playSite) {
        return playSite.getPlaysiteSnapshotList();
    }

    public int getTotalKidsOnPlaysite(PlaySite playSite) {
        return (int) playSite.getKidsWhoHaveVisited().stream().distinct().count();
    }

    public int getTotalKidsOnAllPlaysites() {
        return (int) playSiteList
                .stream()
                .flatMap(playSite -> playSite.getKidsWhoHaveVisited().stream())
                .distinct()
                .count();
    }

    public List<Kid> getPlaysitesPlayingKids(PlaySite playSite) {
        return playSite.getPlayingKids();
    }

    public List<Kid> getPlaysitesKidsInQueue(PlaySite playSite) {
        return playSite.getWaitingKids();
    }
}
