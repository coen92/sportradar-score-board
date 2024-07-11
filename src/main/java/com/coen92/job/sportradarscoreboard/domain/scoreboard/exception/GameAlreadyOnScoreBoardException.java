package com.coen92.job.sportradarscoreboard.domain.scoreboard.exception;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.Game;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.Team;

public class GameAlreadyOnScoreBoardException extends RuntimeException {
    public GameAlreadyOnScoreBoardException(Team home, Team away) {
    }

    public GameAlreadyOnScoreBoardException(Game game) {

    }
}
