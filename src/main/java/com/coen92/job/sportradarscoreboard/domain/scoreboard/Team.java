package com.coen92.job.sportradarscoreboard.domain.scoreboard;


import java.util.Objects;

public record Team(TeamId teamId, String teamName) {

    // teams are the same teams if they have the same teamId; teamName can be changed/updated each season
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;
        return Objects.equals(teamId, team.teamId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(teamId);
        result = 31 * result;
        return result;
    }
}
