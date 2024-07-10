package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import java.util.UUID;

public record Game(Team home, Team away) {
    private static GameId gameId;

    public Game {
        gameId = new GameId(UUID.randomUUID());
    }

    public GameId getGameId() {
        return gameId;
    }

    // todo: probably getting some actual score of the game
    //  and current status of the game: Ongoing, Finished...
}
