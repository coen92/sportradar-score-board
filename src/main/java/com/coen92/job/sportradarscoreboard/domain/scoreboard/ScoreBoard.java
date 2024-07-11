package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.exception.GameAlreadyOnScoreBoardException;
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

    public void startNewGame(Team home, Team away) {
         scoreBoardGames.initNewGame(home, away);
    }

    @Getter
    @Setter
    private static class ScoreBoardGames {
        private List<Game> games;

        public ScoreBoardGames() {
            this.games = new ArrayList<>();
        }

        public void initNewGame(Team home, Team away) {
            var game = new Game(home, away);
            games.stream().filter(g -> g.equals(game))
                    .findAny()
                    .ifPresent(GameAlreadyOnScoreBoardException::new);
            games.add(game);
        }
    }
}
