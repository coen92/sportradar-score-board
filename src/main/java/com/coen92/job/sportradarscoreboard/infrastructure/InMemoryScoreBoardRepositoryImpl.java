package com.coen92.job.sportradarscoreboard.infrastructure;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoard;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardId;

import java.util.HashMap;
import java.util.Map;

public class InMemoryScoreBoardRepositoryImpl implements ScoreBoardRepository {
    private Map<ScoreBoardId, ScoreBoard> inMemory = new HashMap<>();

    @Override
    public ScoreBoard get(ScoreBoardId scoreBoardId) {
        return inMemory.get(scoreBoardId);
    }

    @Override
    public void save(ScoreBoard scoreBoard) {
        inMemory.put(scoreBoard.getScoreBoardId(), scoreBoard);
    }
}
