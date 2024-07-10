package com.coen92.job.sportradarscoreboard.application

import spock.lang.Specification
import spock.lang.Subject

class ScoreBoardServiceSpec extends Specification {

    @Subject
    ScoreBoardService service = new  ScoreBoardService()


    def 'should init start game for home team and away team'() {
        given: 'home and away teams'

        when: 'game being initialized for teams'

        then: "new game with initial score is started"

    }

    def 'should add brand new game to score board'() {
        given: 'home and away teams'

        when: 'game being initialized for teams'

        then: "initial score is added to scoreboard"

    }

    def 'should update score for home team and away team'() {
        given: 'ongoing game on scoreboard'

        when: 'this game\'s score is being updated'

        then: "new score for this game is set"

    }

    def 'should remove finished game from score board'() {
        given: 'ongoing game on scoreboard'

        when: 'game is being finished'

        then: "game is removed from scoreboard"

    }

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
