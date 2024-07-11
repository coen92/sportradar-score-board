package com.coen92.job.sportradarscoreboard.infrastructure;

import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoard;
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardId;


public interface ScoreBoardRepository {
    ScoreBoard get(ScoreBoardId scoreBoardId);

    void save(ScoreBoard scoreBoard);
}
