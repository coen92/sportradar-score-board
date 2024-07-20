package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameInitialisationFailureException;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public final class Game {
    private final GameId gameId;
    private final Team home;
    private final Team away;
    private GameStatus gameStatus;
    private Integer homeScore;
    private Integer awayScore;
    private final Instant startedAt = Instant.now();
    private Instant modifiedAt = Instant.now();

    public Game(Team home, Team away) {
        this(home, away, GameStatus.Started, 0, 0);
    }

    private Game(Team home, Team away, GameStatus gameStatus, Integer homeScore, Integer awayScore) {
        if (home.equals(away))
            throw new GameInitialisationFailureException(home, away);
        this.gameId = new GameId(UUID.randomUUID());
        this.home = home;
        this.away = away;
        this.gameStatus = gameStatus;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Map<TeamId, Integer> getCurrentScore() {
        return Map.of(home.teamId(), homeScore, away.teamId(), awayScore);
    }

    public Integer getTotalScore() {
        return this.homeScore + this.awayScore;
    }

    public void updateScore(Integer homeTeamScore, Integer awayTeamScore) {
        homeScore = homeTeamScore;
        awayScore = awayTeamScore;
        gameStatus = GameStatus.Ongoing;
        modifiedAt = Instant.now();
    }

    public Game endGame() {
        modifiedAt = Instant.now();
        gameStatus = GameStatus.Finished;
        return this;
    }

    public long getCurrentGameDuration() {
        return Duration.between(startedAt, Instant.now()).toMinutes();
    }

    enum GameStatus {
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
