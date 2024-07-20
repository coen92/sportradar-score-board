package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import java.util.Comparator;

public class GameStartTimeComparator implements Comparator<Game> {
    @Override
    public int compare(Game o1, Game o2) {
        if(o1.getStartedAt() != o2.getStartedAt())
            return o1.getStartedAt().compareTo(o2.getStartedAt());
        return 0;
    }

    public Comparator<Game> getByEarliestStartDate() {
        return this.reversed();
    }

    public Comparator<Game> getByLatestStartDate() {
        return this;
    }
}
