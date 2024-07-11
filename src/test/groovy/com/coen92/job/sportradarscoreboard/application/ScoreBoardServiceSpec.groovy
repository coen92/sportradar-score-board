package com.coen92.job.sportradarscoreboard.application

import com.coen92.job.sportradarscoreboard.domain.scoreboard.Game
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoardId
import com.coen92.job.sportradarscoreboard.domain.scoreboard.Team
import com.coen92.job.sportradarscoreboard.domain.scoreboard.TeamId
import com.coen92.job.sportradarscoreboard.infrastructure.InMemoryScoreBoardRepositoryImpl
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository
import spock.lang.Specification
import spock.lang.Subject

class ScoreBoardServiceSpec extends Specification {
    ScoreBoardRepository repository = new InMemoryScoreBoardRepositoryImpl()

    @Subject
    ScoreBoardService service = new ScoreBoardService(repository)


    def 'should init empty scoreboard'() {
        when: 'empty score board is initialized'
        def scoreBoardId = service.initEmptyScoreBoard()

        then: 'score board with no games exists'
        repository.get(scoreBoardId).isEmpty()
    }

    def 'should init start game for home team and away team'() {
        given: 'home and away teams'
            var scoreBoardId = service.initEmptyScoreBoard()
            var homeTeamId = new TeamId(UUID.randomUUID())
            var awayTeamId = new TeamId(UUID.randomUUID())
            var homeTeam = new Team(homeTeamId, 'homeTeam')
            var awayTeam = new Team(awayTeamId, 'awayTeam')

        when: 'game being initialized for teams'
            def gameId = service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)

        then: 'new game with initial score is started'
            def initGame = repository.get(scoreBoardId)
                .getScoreBoardGames().games.stream()
                .filter(game -> gameId.equals(game.gameId))
                .findAny()

            !initGame.empty
            def result = initGame.get()
            Game.GameStatus.Started == result.gameStatus
            homeTeamId == result.home.teamId()
            awayTeamId == result.away.teamId()
            result.getCurrentScore() == Map.of(homeTeamId, 0, awayTeamId, 0)
    }

    def 'should add brand new game to score board'() {
        given: 'home and away teams'
        var scoreBoardId = new ScoreBoardId(UUID.randomUUID())
        var homeTeam = new Team()
        var awayTeam = new Team()

        when: 'game being initialized for teams'
        service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)

        then: 'initial score is added to scoreboard'
        def result = repository.get(scoreBoardId)
        // todo: assert the Ongoing game's score is 0:0
    }

    def 'should update score for home team and away team'() {
        given: 'ongoing game on scoreboard'
        var scoreBoardId = new ScoreBoardId(UUID.randomUUID())
        var homeTeam = new Team()
        var awayTeam = new Team()
        service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)
        var game = new Game(homeTeam, awayTeam) // todo: here team scores should have updated values

        def initial = repository.get(scoreBoardId)
        // todo: assertion game score is 0:0

        when: 'this game\'s score is being updated'
        service.updateGameResultOnScoreBoard(scoreBoardId, game)

        then: 'new score for this game is set'
        def updated = repository.get(scoreBoardId)
        // todo: assertion game score is
    }

    def 'should remove finished game from score board'() {
        given: 'ongoing game on scoreboard'
        var scoreBoardId = new ScoreBoardId(UUID.randomUUID())
        var homeTeam = new Team()
        var awayTeam = new Team()
        service.initGameForScoreBoard(scoreBoardId, homeTeam, awayTeam)
        var gameId = new Game(homeTeam, awayTeam).getGameId()

        def initial = repository.get(scoreBoardId)
        // todo: assertion game exists

        when: 'game is being finished'
        service.finishGameOnScoreBoard(scoreBoardId, gameId)

        then: 'game is removed from scoreboard'
        def updated = repository.get(scoreBoardId)
        // todo: assertion game is not on scoreboard anymore
    }

    // todo: below two tests will impact 'then' blocks on above tests - for later implementation
    def 'should return ordered ongoing games to score board'() {
        given: 'ongoing games on scoreboard'

        when: 'games summary is queried'

        then: 'returns games by their total score descending'

    }

    def 'should order games with same total score by most recent started match'() {
        given: 'multiple ongoing games with same total score'

        when: 'games summary is queried'

        then: 'games are returned by most recent one'

    }
}
