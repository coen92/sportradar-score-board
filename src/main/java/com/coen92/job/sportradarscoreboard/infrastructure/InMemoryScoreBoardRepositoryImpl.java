package com.coen92.job.sportradarscoreboard.infrastructure;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardAggregate;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardId;

import java.util.HashMap;
import java.util.Map;

public class InMemoryScoreBoardRepositoryImpl implements ScoreBoardRepository {
    private final Map<ScoreBoardId, ScoreBoardAggregate> inMemory = new HashMap<>();

    @Override
    public ScoreBoardAggregate get(ScoreBoardId scoreBoardId) {
        return inMemory.get(scoreBoardId);
    }

    @Override
    public void save(ScoreBoardAggregate scoreBoard) {
        inMemory.put(scoreBoard.getScoreBoardId(), scoreBoard);
    }

    public void clean() {
        inMemory.clear();
    }
}
