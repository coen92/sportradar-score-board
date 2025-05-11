package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameAlreadyOnScoreBoardException;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameDurationConstraintToFinishException;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Getter
public class ScoreBoardAggregate {
    private static final int GAME_DURATION_TO_FINISH_IN_MINUTES = 0; // should be 90 in original implementation + injection of @Bean Clock
    private final ScoreBoardId scoreBoardId;
    private final ScoreBoardGames scoreBoardGames;

    public ScoreBoardAggregate() {
        scoreBoardId = new ScoreBoardId(UUID.randomUUID());
        this.scoreBoardGames = new ScoreBoardGames();
    }

    public GameId startNewGame(Team home, Team away) {
        return scoreBoardGames.startGame(home, away);
    }

    public void updateGameScore(GameId gameId, Integer homeTeamScore, Integer awayTeamScore) {
        var game = scoreBoardGames.getGame(gameId);
        game.updateScore(homeTeamScore, awayTeamScore);
        scoreBoardGames.addGame(game);
    }

    public boolean isEmpty() {
        return this.getScoreBoardGames().getGames().isEmpty();
    }

    public void finishGame(GameId gameId) {
        var game = scoreBoardGames.getGame(gameId);
        if (game.getCurrentGameDuration() < GAME_DURATION_TO_FINISH_IN_MINUTES)
            throw new GameDurationConstraintToFinishException(GAME_DURATION_TO_FINISH_IN_MINUTES);
        scoreBoardGames.removeGame(game.endGame());
    }

    public List<Game> displayCurrentGamesWithResult() {
        return scoreBoardGames.getOrderedGames();
    }

    @Getter
    @Setter
    private static class ScoreBoardGames {
        private List<Game> games;
        private final Comparator<Game> highesttotalScoreComparator = new GameTotalScoreComparator().getByHighestTotalScore();
        private final Comparator<Game> earliestStartComparator = new GameStartTimeComparator().getByEarliestStartDate();

        public ScoreBoardGames() {
            this.games = new ArrayList<>();
        }

        public GameId startGame(Team home, Team away) {
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

        public void removeGame(Game game) {
            if (game.getGameStatus() == Game.GameStatus.Finished) {
                games.remove(game);
            }
        }

        public List<Game> getOrderedGames() {
            games.sort(highesttotalScoreComparator.thenComparing(earliestStartComparator));
            return games;
        }
    }
}
