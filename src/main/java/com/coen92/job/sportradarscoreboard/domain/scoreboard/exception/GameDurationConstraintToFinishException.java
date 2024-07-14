package com.coen92.job.sportradarscoreboard.domain.scoreboard.exception;

public class GameDurationConstraintToFinishException extends RuntimeException {
    public GameDurationConstraintToFinishException(int durationInMinutes) {
        super(STR."Game must last at least \{durationInMinutes} minutes to be finished!");
    }
}
