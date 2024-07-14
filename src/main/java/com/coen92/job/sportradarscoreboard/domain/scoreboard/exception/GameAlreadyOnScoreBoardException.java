package com.coen92.job.sportradarscoreboard.domain.scoreboard.exception;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.Game;

public class GameAlreadyOnScoreBoardException extends RuntimeException {
    public GameAlreadyOnScoreBoardException(Game game) {
        super(STR."Game for the teams: \{game.getHome().teamName()} vs. \{game.getAway().teamName()} already on scoreboard!");
    }
}
