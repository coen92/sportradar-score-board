package com.coen92.job.sportradarscoreboard.infrastructure;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardAggregate;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardId;


public interface ScoreBoardRepository {
    ScoreBoardAggregate get(ScoreBoardId scoreBoardId);

    void save(ScoreBoardAggregate scoreBoard);
}
