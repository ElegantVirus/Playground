package com.some.playground.visitors;

import com.some.playground.attractions.PlaySite;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Kid {
    private String name;
    private int age;
    private String ticketNumber;
    private boolean vip;
    private boolean willingToWait;
    private List<KidsHistory> historyList;

    public Kid(String name, int age, String ticketNumber, boolean vip, boolean willingToWait) {
        this.name = name;
        this.age = age;
        this.ticketNumber = ticketNumber;
        this.vip = vip;
        this.willingToWait = willingToWait;
        historyList = new ArrayList<>();
    }

    public static class KidsHistory {
        @Getter
        private PlaySite playSite;
        private LocalDateTime startTime;
        @Setter
        private LocalDateTime endTime;

        public KidsHistory(PlaySite playSite, LocalDateTime startTime) {
            this.playSite = playSite;
            this.startTime = startTime;
        }

        public Duration getDurationSpent() {
            if (endTime != null && startTime != null) {
                return Duration.between(startTime, endTime);
            } else {
                return Duration.ofDays(0);
            }
        }
    }
}
