package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

// todo: probably main aggregate containing ongoing games...
@Getter
@AllArgsConstructor
public class ScoreBoard {
    private final ScoreBoardId scoreBoardId;
}
