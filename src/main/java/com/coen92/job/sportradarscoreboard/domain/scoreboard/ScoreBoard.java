package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameAlreadyOnScoreBoardException;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
public class ScoreBoard {
    private final ScoreBoardId scoreBoardId;
    private final ScoreBoardGames scoreBoardGames;

    public ScoreBoard() {
        scoreBoardId = new ScoreBoardId(UUID.randomUUID());
        this.scoreBoardGames = new ScoreBoardGames();
    }

    public GameId startNewGame(Team home, Team away) {
        return scoreBoardGames.initNewGame(home, away);
    }

    public void updateGameScore(GameId gameId, Integer homeTeamScore, Integer awayTeamScore) {
        var game = scoreBoardGames.getGame(gameId);
        game.updateScore(homeTeamScore, awayTeamScore);
        scoreBoardGames.addGame(game);
    }

    public boolean isEmpty() {
        return this.getScoreBoardGames().getGames().isEmpty();
    }

    @Getter
    @Setter
    private static class ScoreBoardGames {
        private List<Game> games;

        public ScoreBoardGames() {
            this.games = new ArrayList<>();
        }

        public GameId initNewGame(Team home, Team away) {
            var game = new Game(home, away);
            games.stream().filter(g -> g.equals(game))
                    .findAny()
                    .ifPresent(GameAlreadyOnScoreBoardException::new);
            games.add(game);
            return game.getGameId();
        }

        public Game getGame(GameId gameId) {
            return games.stream().
                    filter(game -> gameId.equals(game.getGameId()))
                    .findFirst()
                    .orElseThrow(() -> new GameNotFoundException(gameId));
        }

        public void addGame(Game game) {
            games.remove(game);
            games.add(game);
        }
    }
}
