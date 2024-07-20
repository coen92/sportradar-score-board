package com.coen92.job.sportradarscoreboard.domain.scoreboard.exception;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.Team;

public class GameInitialisationFailureException extends RuntimeException {
    public GameInitialisationFailureException(Team home, Team away) {
        super(STR."Game can't be initialized for the same teams: \'\{home.teamName()}\'(id: \{home.teamId()}) vs. \'\{away.teamName()}\'(\{away.teamId()})");
    }
}
