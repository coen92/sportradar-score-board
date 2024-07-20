package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import java.util.Comparator;

public class GameTotalScoreComparator implements Comparator<Game> {
    @Override
    public int compare(Game o1, Game o2) {
        if(!o1.getTotalScore().equals(o2.getTotalScore()))
            return o1.getTotalScore().compareTo(o2.getTotalScore());
        return 0;
    }

    public Comparator<Game> getByHighestTotalScore() {
        return this.reversed();
    }

    public Comparator<Game> getByLowestTotalScore() {
        return this;
    }
}
