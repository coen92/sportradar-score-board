package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record Game(Team home, Team away, GameStatus gameStatus, Integer homeScore, Integer awayScore) {
    private static GameId gameId;
    @Getter
    private final static Instant startedAt = Instant.now();

    public Game(Team home, Team away) {
        this(home, away, GameStatus.Started, 0, 0);
        gameId = new GameId(UUID.randomUUID());
    }

    public GameId getGameId() {
        return gameId;
    }

    public Map<TeamId, Integer> getCurrentScore() {
        return Map.of(home.teamId(), homeScore, away.teamId(), awayScore);
    }

    private enum GameStatus {
        Started, Ongoing, Finished
    }

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
