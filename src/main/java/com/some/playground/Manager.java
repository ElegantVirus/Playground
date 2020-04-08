package com.some.playground;

import com.some.playground.attractions.PlaySite;
import com.some.playground.visitors.Kid;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class Manager {
    private List<PlaySite> playSiteList;

    public Manager() {
        playSiteList = new ArrayList<>();
    }

    public void addPlaySite(PlaySite playSite) {
        playSiteList.add(playSite);
    }

    public void addKidToThePlaySite(PlaySite playSite, Kid kid) throws IllegalAccessException {
        playSite.addKid(kid);
    }

    public void removeKidFromThePlaySite(PlaySite playSite, Kid kid) {
        playSite.removeKid(kid);
    }

    public List<Kid.KidsHistory> getHistoryOfTheKid(Kid kid) {
        return kid.getHistoryList();
    }

    public List<PlaySite.PlaySiteSnapshot> getSnapshotsOfThePlaySite(PlaySite playSite) {
        return playSite.getPlaySiteSnapshotList();
    }

    public int getTotalKidsOnPlaySite(PlaySite playSite) {
        return (int) playSite.getKidsWhoHaveVisited().stream().distinct().count();
    }

    public int getTotalKidsOnAllPlaySites() {
        return (int) playSiteList
                .stream()
                .map(PlaySite::getKidsWhoHaveVisited)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    public List<Kid> getPlaySitesPlayingKids(PlaySite playSite) {
        return playSite.getPlayingKids();
    }

    public List<Kid> getPlaySitesKidsInQueue(PlaySite playSite) {
        return playSite.getWaitingKids();
    }
}
