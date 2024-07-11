package com.coen92.job.sportradarscoreboard.infrastructure;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoard;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardId;

public class InMemoryScoreBoardRepositoryImpl implements ScoreBoardRepository {
    @Override
    public ScoreBoard get(ScoreBoardId scoreBoardId) {
        // todo: add implementation for getting data from memory storage
        return null;
    }

    @Override
    public void save(ScoreBoard scoreBoard) {
        // todo: add implementation for saving score board in memory storage
    }
}
