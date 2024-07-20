package com.coen92.job.sportradarscoreboard.application

import com.coen92.job.sportradarscoreboard.domain.scoreboard.*
import com.coen92.job.sportradarscoreboard.infrastructure.InMemoryScoreBoardRepositoryImpl
import com.coen92.job.sportradarscoreboard.infrastructure.ScoreBoardRepository
import lombok.Setter
import org.spockframework.util.Pair
import spock.lang.Specification
import spock.lang.Subject

class ScoreBoardServiceSpec extends Specification {
    public static final int START_TIME_DELAY_IN_MILLIS = 10
    public static final int MATCH_SCORE = 5

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

    def 'should return ordered ongoing games to score board'() {
        given: 'ongoing games on scoreboard'
            List<MatchPair> gamesToStart = createGames(List.of(
                    new Team(new TeamId(UUID.randomUUID()), 'teamA'),
                    new Team(new TeamId(UUID.randomUUID()), 'teamB'),
                    new Team(new TeamId(UUID.randomUUID()), 'teamC')
            ))
            def initialized = initGamesForScoreBoard(gamesToStart)
            List<MatchPair> finalScoreGames = setGamesFinalResults(initialized)
            def finalized = updateGamesOnScoreBoard(finalScoreGames)

        when: 'games summary is queried'
            def summary = service.getGamesSummary(scoreBoardId)

        then: 'returns games by their total score descending'
            !summary.empty
            summary.size() == finalized.size()
            summary.first.getTotalScore() >= summary.get(1).getTotalScore()
            summary.get(1).getTotalScore() >= summary.get(2).getTotalScore()
            summary.get(2).getTotalScore() >= summary.last.getTotalScore()
    }

    def 'should order games with same total score by most recent started match'() {
        given: 'multiple ongoing games with same total score'
            List<MatchPair> gamesToStart = createGames(List.of(
                    new Team(new TeamId(UUID.randomUUID()), 'teamA'),
                    new Team(new TeamId(UUID.randomUUID()), 'teamB'),
                    new Team(new TeamId(UUID.randomUUID()), 'teamC')
            ))
            def initialized = initGamesForScoreBoard(gamesToStart)
            List<MatchPair> sameScoreGames = setGamesFinalResultsWithDelay(initialized)
            def finalized = updateGamesOnScoreBoard(sameScoreGames)

        when: 'games summary is queried'
            def summary = service.getGamesSummary(scoreBoardId)

        then: 'games are returned by most recent one'
            !summary.empty
            summary.size() == finalized.size()

            def firstGame = summary.first
            def secondGame = summary.get(1)
            def thirdGame = summary.get(2)
            def fourthGame = summary.last

            firstGame.getTotalScore() == secondGame.getTotalScore()
            secondGame.getTotalScore() == thirdGame.getTotalScore()
            thirdGame.getTotalScore() == fourthGame.getTotalScore()

            firstGame.startedAt > secondGame.startedAt
            secondGame.startedAt > thirdGame.startedAt
            thirdGame.startedAt > fourthGame.startedAt
    }


    private List<MatchPair> initGamesForScoreBoard(Collection<MatchPair> matches) {
        for (var matchPair : matches) {
            def gameId = service.initGameForScoreBoard(scoreBoardId, matchPair.getHomeTeam(), matchPair.getAwayTeam())
            matchPair.gameId = gameId
        }
        return matches
    }

    private List<MatchPair> updateGamesOnScoreBoard(Collection<MatchPair> matches) {
        for(var matchPair : matches) {
            service.updateGameResultOnScoreBoard(scoreBoardId, matchPair.getGameId(), matchPair.getHomeTeamScore(), matchPair.getAwayTeamScore())
        }
        return matches
    }

    List<MatchPair> createGames(Collection<Team> teams) {
        List<MatchPair> matches = new ArrayList<>()
        teams.forEach(team -> {
            var homeTeam = team
            teams.forEach(other -> {
                addTwoDifferentTeamsForMatch(homeTeam, other, matches)
            })
        })
        matches
    }

    List<MatchPair> setGamesFinalResults(List<MatchPair> matchPairs) {
        List<MatchPair> matches = new ArrayList<>()
        matchPairs.forEach(pair -> {
            matches.add(randomizeMatchResultScore(pair))
        })
        matches
    }

    List<MatchPair> setGamesFinalResultsWithDelay(List<MatchPair> matchPairs) {
        List<MatchPair> matches = new ArrayList<>()
        matchPairs.forEach(pair -> addMatchesWithDelay(pair, matches))
        matches
    }

    private synchronized void addMatchesWithDelay(MatchPair pair, List<MatchPair> matches) {
        wait(START_TIME_DELAY_IN_MILLIS) // this will slow down a bit execution of the tests when this method is used - depending on the constant value
        matches.add(setMatchResultWithSameScore(pair, MATCH_SCORE))
    }

    MatchPair startedMatch(Team homeTeam, Team awayTeam) {
        return new MatchPair(homeTeam, awayTeam)
    }

    void addTwoDifferentTeamsForMatch(Team home, Team away, List<MatchPair> matches) {
        if (home != away) {
            def match = startedMatch(home, away)
            matches.add(match)
        }
    }

    MatchPair randomizeMatchResultScore(MatchPair pair) {
        def randomScore = setRandomResultFromRange(0, 9)
        return new MatchPair(pair.gameId, pair.homeTeam, pair.awayTeam, randomScore, randomScore)
    }

    MatchPair setMatchResultWithSameScore(MatchPair pair, int score) {
        return new MatchPair(pair.gameId, pair.homeTeam, pair.awayTeam, score, score)
    }

    int setRandomResultFromRange(int min, int max) {
        Random random = new Random()
        return random.ints(min, max).findFirst().getAsInt()
    }


    private class MatchPair {
        @Setter
        private GameId gameId
        private final Pair<Team, Integer> homeTeam
        private final Pair<Team, Integer> awayTeam

        private MatchPair(Team homeTeam, Team awayTeam) {
            this.homeTeam = Pair.of(homeTeam, 0)
            this.awayTeam = Pair.of(awayTeam, 0)
        }

        private MatchPair(GameId id, Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
            gameId = id
            this.homeTeam = Pair.of(homeTeam, homeScore)
            this.awayTeam = Pair.of(awayTeam, awayScore)
        }

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
