package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.ValueObject;

import java.util.UUID;

public class ScoreBoardId extends ValueObject<UUID> {
    protected ScoreBoardId(UUID value) {
        super(value);
    }
}
