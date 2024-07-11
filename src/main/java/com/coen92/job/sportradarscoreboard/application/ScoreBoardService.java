package com.coen92.job.sportradarscoreboard.application;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.*;
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ScoreBoardService {
    private final ScoreBoardRepository repository;

    ScoreBoardId initEmptyScoreBoard() {
        ScoreBoard scoreBoard = new ScoreBoard();
        repository.save(scoreBoard);
        return scoreBoard.getScoreBoardId();
    }

    void initGameForScoreBoard(ScoreBoardId scoreBoardId, Team home, Team away) {
        ScoreBoard scoreBoard = repository.get(scoreBoardId);
        if (scoreBoard == null)
            throw new IllegalStateException(STR."ScoreBoard of id \{scoreBoardId} does not exist!"); // 404 Http Status
        scoreBoard.startNewGame(home, away);
        repository.save(scoreBoard);
    }

    void updateGameResultOnScoreBoard(ScoreBoardId scoreBoardId, Game game) {
        // todo: add implementation for updating ongoing game result

    }

    void finishGameOnScoreBoard(ScoreBoardId scoreBoardId, GameId gameId) {
        // todo: add implementation for finishing game
    }
}
