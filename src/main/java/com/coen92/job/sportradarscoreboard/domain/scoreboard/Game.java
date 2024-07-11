package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

public record Game(Team home, Team away) {
    private static GameId gameId;
    @Setter
    private static GameStatus gameStatus;

    public Game {
        gameId = new GameId(UUID.randomUUID());
        gameStatus = GameStatus.Started;
        // todo: add new score initialization with 0:0
    }

    public GameId getGameId() {
        return gameId;
    }

    private enum GameStatus {
        Started, Ongoing, Finished
    }

    // todo: probably getting some actual score of the game (score as double element Map<TeamId, Integer>)?
    //  and current status of the game: Ongoing, Finished...

    // game is the same game if playing the same teams (with consideration of home/away place)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;
        return Objects.equals(home, game.home) && Objects.equals(away, game.away);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(home);
        result = 31 * result + Objects.hashCode(away);
        return result;
    }
}
