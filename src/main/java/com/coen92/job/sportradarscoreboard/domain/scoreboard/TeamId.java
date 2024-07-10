package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.ValueObject;

import java.util.UUID;

public class TeamId extends ValueObject<UUID> {
    protected TeamId(UUID value) {
        super(value);
    }
}
