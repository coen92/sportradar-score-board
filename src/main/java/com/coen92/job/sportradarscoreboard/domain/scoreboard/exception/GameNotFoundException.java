package com.coen92.job.sportradarscoreboard.domain.scoreboard.exception;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.GameId;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(GameId gameId) {
        super(STR."Game of id \{gameId} not found!");
    }
}
