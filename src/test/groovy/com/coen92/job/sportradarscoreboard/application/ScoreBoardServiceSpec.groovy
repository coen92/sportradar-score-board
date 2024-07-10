package com.coen92.job.sportradarscoreboard.application

import com.coen92.job.sportradarscoreboard.domain.scoreboard.Game
import com.coen92.job.sportradarscoreboard.domain.scoreboard.GameId
import com.coen92.job.sportradarscoreboard.domain.scoreboard.ScoreBoard
import com.coen92.job.sportradarscoreboard.domain.scoreboard.Team
import com.coen92.job.sportradarscoreboard.infrastructure.InMemoryScoreBoardRepositoryImpl
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository
import spock.lang.Specification
import spock.lang.Subject

class ScoreBoardServiceSpec extends Specification {

    ScoreBoardRepository repository = new InMemoryScoreBoardRepositoryImpl()

    @Subject
    ScoreBoardService service = new  ScoreBoardService(repository)


    def 'should init start game for home team and away team'() {
        given: 'home and away teams'
            var homeTeam = new Team()
            var awayTeam = new Team()

        when: 'game being initialized for teams'
            def scoreBoardId = service.initGame(homeTeam, awayTeam) // todo: probably Game instead teams...?

        then: "new game with initial score is started"
            def result = repository.get(scoreBoardId)
            // todo: assert there's Ongoing game

    }

    def 'should add brand new game to score board'() {
        given: 'home and away teams'
            var homeTeam = new Team()
            var awayTeam = new Team()

        when: 'game being initialized for teams'
            def scoreBoardId = service.initGame(homeTeam, awayTeam)

        then: "initial score is added to scoreboard"
            def result = repository.get(scoreBoardId)
            // todo: assert the Ongoing game's score is 0:0
    }

    def 'should update score for home team and away team'() {
        given: 'ongoing game on scoreboard'
            var homeTeam = new Team()
            var awayTeam = new Team()
            def scoreBoardId = service.initGame(homeTeam, awayTeam)
            var game = new Game(homeTeam, awayTeam) // todo: here team scores should have updated values

            def initial = repository.get(scoreBoardId)
            // todo: assertion game score is 0:0

        when: 'this game\'s score is being updated'
            service.updateGameResult(scoreBoardId, game)

        then: "new score for this game is set"
            def updated = repository.get(scoreBoardId)
            // todo: assertion game score is
    }

    def 'should remove finished game from score board'() {
        given: 'ongoing game on scoreboard'
            var homeTeam = new Team()
            var awayTeam = new Team()
            def scoreBoardId = service.initGame(homeTeam, awayTeam)
            var gameId = new Game(homeTeam, awayTeam).getGameId()

            def initial = repository.get(scoreBoardId)
            // todo: assertion game exists

        when: 'game is being finished'
            service.finishGame(scoreBoardId, gameId)

        then: "game is removed from scoreboard"
            def updated = repository.get(scoreBoardId)
            // todo: assertion game is not on scoreboard anymore
    }

    // todo: below two tests will impact "then" blocks on above tests - for later implementation
    def 'should return ordered ongoing games to score board'() {
        given: 'ongoing games on scoreboard'

        when: 'games summary is queried'

        then: "returns games by their total score descending"

    }

    def 'should order games with same total score by most recent started match'() {
        given: 'multiple ongoing games with same total score'

        when: 'games summary is queried'

        then: "games are returned by most recent one"

    }
}
