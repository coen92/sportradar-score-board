package com.coen92.job.sportradarscoreboard.domain.scoreboard;

import com.coen92.job.sportradarscoreboard.domain.ValueObject;

import java.util.UUID;

public class GameId extends ValueObject<UUID> {
    protected GameId(UUID value) {
        super(value);
    }
}
