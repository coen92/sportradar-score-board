package com.coen92.job.sportradarscoreboard.application;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.*;
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ScoreBoardService {
    private final ScoreBoardRepository repository;

    ScoreBoardId initEmptyScoreBoard() {
        var scoreBoard = new ScoreBoardAggregate();
        repository.save(scoreBoard);
        return scoreBoard.getScoreBoardId();
    }

    GameId initGameForScoreBoard(ScoreBoardId scoreBoardId, Team home, Team away) {
        var scoreBoard = findScoreBoard(scoreBoardId);
        var startedGameId = scoreBoard.startNewGame(home, away);
        repository.save(scoreBoard);
        return startedGameId;
    }

    void updateGameResultOnScoreBoard(ScoreBoardId scoreBoardId, GameId gameId, Integer homeTeamScore, Integer awayTeamScore) {
        var scoreBoard = findScoreBoard(scoreBoardId);
        scoreBoard.updateGameScore(gameId, homeTeamScore, awayTeamScore);
        repository.save(scoreBoard);
    }

    void finishGameOnScoreBoard(ScoreBoardId scoreBoardId, GameId gameId) {
        var scoreBoard = findScoreBoard(scoreBoardId);
        scoreBoard.finishGame(gameId);
        repository.save(scoreBoard);
    }

    Object getGamesSummary(ScoreBoardId scoreBoardId) {
        var scoreBoard = findScoreBoard(scoreBoardId);
        return scoreBoard.displayGamesWithResult();
    }


    private ScoreBoardAggregate findScoreBoard(ScoreBoardId scoreBoardId) {
        var scoreBoard = repository.get(scoreBoardId);
        if (scoreBoard == null)
            throw new IllegalStateException(STR."ScoreBoard of id \{scoreBoardId} does not exist!"); // 404 Http Status
        return scoreBoard;
    }
}
