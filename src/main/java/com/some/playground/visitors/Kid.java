package com.some.playground.visitors;

import com.some.playground.attractions.PlaySite;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Kid {
    private String name;
    private int age;
    private String ticketNumber;
    private boolean vip;
    private boolean waiting;
    private List<KidsHistory> historyList;

    public Kid(String name, int age, String ticketNumber, boolean vip, boolean waiting) {
        this.name = name;
        this.age = age;
        this.ticketNumber = ticketNumber;
        this.vip = vip;
        this.waiting = waiting;
        historyList = new ArrayList<>();
    }

    @Data
    public static class KidsHistory {
        PlaySite playSite;
        LocalDateTime startTime;
        LocalDateTime endTime;
        Duration durationSpent;

        public Duration getDurationSpent() {
            if (endTime != null && startTime != null) {
                return Duration.between(startTime, endTime);
            } else {
                return Duration.ofDays(0);
            }
        }
    }
}
