package com.coen92.job.sportradarscoreboard.application;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.*;
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ScoreBoardService {
    private final ScoreBoardRepository repository;

    void initGame(Team home, Team away) {
        // todo: add implementation for initializing new game process
    }

    ScoreBoard updateGameResult(ScoreBoardId scoreBoardId, Game game) {
        // todo: add implementation for updating ongoing game result
        return null;
    }

    void finishGame(ScoreBoardId scoreBoardId, GameId gameId) {
        // todo: add implementation for finishing game
    }
}
