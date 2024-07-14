package com.coen92.job.sportradarscoreboard.application

import com.coen92.job.sportradarscoreboard.domain.scoreboard.*
import com.coen92.job.sportradarscoreboard.infrastructure.InMemoryScoreBoardRepositoryImpl
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository
import lombok.AllArgsConstructor
import lombok.Setter
import org.spockframework.util.Pair
import spock.lang.Specification
import spock.lang.Subject

class ScoreBoardServiceSpec extends Specification {
    ScoreBoardRepository repository = new InMemoryScoreBoardRepositoryImpl()

    @Subject
    ScoreBoardService service = new ScoreBoardService(repository)

    private ScoreBoardId scoreBoardId

    def setup() {
        scoreBoardId = service.initEmptyScoreBoard()
    }

    def cleanup() {
        repository.clean()
    }

    def 'should init empty scoreboard'() {
        when: 'empty score board is initialized'

        then: 'score board with no games exists'
            repository.get(scoreBoardId).isEmpty()
    }

    def 'should init start game for home team and away team'() {
        given: 'home and away teams'
            var homeTeamId = new TeamId(UUID.randomUUID())
            var awayTeamId = new TeamId(UUID.randomUUID())
            var homeTeam = new Team(homeTeamId, 'homeTeam')
            var awayTeam = new Team(awayTeamId, 'awayTeam')

        when: 'game being initialized for teams'
            def gameId = service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)

        then: 'new game with initial score is started'
            def startedGame = repository.get(scoreBoardId)
                .getScoreBoardGames().games.stream()
                .filter(game -> gameId == game.gameId)
                .findAny()

            !startedGame.empty
            def result = startedGame.get()
            Game.GameStatus.Started == result.gameStatus
            homeTeamId == result.home.teamId()
            awayTeamId == result.away.teamId()
            result.getCurrentScore() == Map.of(homeTeamId, 0, awayTeamId, 0)
    }

    def 'should update score for home team and away team'() {
        given: 'ongoing game on scoreboard'
            var homeTeamId = new TeamId(UUID.randomUUID())
            var awayTeamId = new TeamId(UUID.randomUUID())
            var homeTeam = new Team(homeTeamId, 'homeTeam')
            var awayTeam = new Team(awayTeamId, 'awayTeam')

            def gameId = service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)

        when: 'this game\'s score is being updated'
            service.updateGameResultOnScoreBoard(scoreBoardId, gameId, 1, 0)

        then: 'new score for this game is set'
            def updatedGame = repository.get(scoreBoardId)
                    .getScoreBoardGames().games.stream()
                    .filter(game -> gameId == game.gameId)
                    .findAny()

            !updatedGame.empty
            def result = updatedGame.get()
            Game.GameStatus.Ongoing == result.gameStatus
            homeTeamId == result.home.teamId()
            awayTeamId == result.away.teamId()
            result.getCurrentScore() == Map.of(homeTeamId, 1, awayTeamId, 0)
    }

    def 'should remove finished game from score board'() {
        given: 'ongoing game on scoreboard'
            var homeTeamId = new TeamId(UUID.randomUUID())
            var awayTeamId = new TeamId(UUID.randomUUID())
            var homeTeam = new Team(homeTeamId, 'homeTeam')
            var awayTeam = new Team(awayTeamId, 'awayTeam')

            def gameId = service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)

        when: 'game is being finished'
            service.finishGameOnScoreBoard(scoreBoardId, gameId)

        then: 'game is removed from scoreboard'
            def finishedGame = repository.get(scoreBoardId)
                    .getScoreBoardGames().games.stream()
                    .filter(game -> gameId == game.gameId)
                    .findAny()

            finishedGame.empty
    }

    // todo: finish implementation of displaying games for score board below
    def 'should return ordered ongoing games to score board'() {
        given: 'ongoing games on scoreboard'
            List<MatchPair> gamesToStart = createGames()
            def initialized = initGamesForScoreBoard(gamesToStart)
            List<MatchPair> finalScoreGames = setGamesFinalResults(initialized)
            def finalized = updateGamesOnScoreBoard(finalScoreGames)

        when: 'games summary is queried'
            def summary = service.getGamesSummary(scoreBoardId)

        then: 'returns games by their total score descending'
            summary == expectedOrderGamesSummary(finalized)
    }

    def 'should order games with same total score by most recent started match'() {
        given: 'multiple ongoing games with same total score'
            List<MatchPair> gamesToStart = createGames()
            def initialized = initGamesForScoreBoard(gamesToStart)
            List<MatchPair> sameScoreGames = setGamesFinalResults(initialized) // todo: apply differentiation for games update time
            def totalScored = updateGamesOnScoreBoard(sameScoreGames)

        when: 'games summary is queried'
            def summary = service.getGamesSummary(scoreBoardId)

        then: 'games are returned by most recent one'
            summary == expectedOrderGamesSummary(totalScored)
    }


    private List<MatchPair> initGamesForScoreBoard(MatchPair... matches) {
        if (matches == null || matches.toList().isEmpty())
            return List.of()
        for (var matchPair : matches) {
            def gameId = service.initGameForScoreBoard(scoreBoardId, matchPair.getHomeTeam(), matchPair.getAwayTeam())
            matchPair.gameId = gameId
        }
        return matches
    }

    private List<MatchPair> updateGamesOnScoreBoard(MatchPair... matches) {
        if (matches == null || matches.toList().isEmpty())
            return List.of()
        for(var matchPair : matches) {
            service.updateGameResultOnScoreBoard(scoreBoardId, matchPair.getGameId(), matchPair.getHomeTeamScore(), matchPair.getAwayTeamScore())
        }
        return matches
    }

    private void finishGamesOnScoreBoard(GameId... gameIds) {
        for (var gameId : gameIds) {
            service.finishGameOnScoreBoard(scoreBoardId, gameId)
        }
    }

    List<MatchPair> createGames() {
        // todo: implementation - mock games to initialize
        null
    }

    void expectedOrderGamesSummary(List<MatchPair> matchPairs) {
        // todo: create ordering method based on requirements
    }

    List<MatchPair> setGamesFinalResults(List<MatchPair> matchPairs) {
        // todo: implementation for setting games result
        null
    }


    @AllArgsConstructor
    private class MatchPair {
        @Setter
        private GameId gameId
        private final Pair<Team, Integer> homeTeam
        private final Pair<Team, Integer> awayTeam

        GameId getGameId() {
            return this.gameId
        }

        Team getHomeTeam() {
            return homeTeam.first()
        }

        Integer getHomeTeamScore() {
            return homeTeam.second()
        }

        Team getAwayTeam() {
            return awayTeam.first()
        }

        Integer getAwayTeamScore() {
            return awayTeam.second()
        }
    }
}
