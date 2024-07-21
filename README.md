# SPORTRADAR: SCORE BOARD

_'Who needs a Readme if you have self-explanatory, well-written, sexy code. Readmes are for sissies'_ 

~ Confucius

## General Information
The Application is written for meeting the business requirements of 'Live Football World Cup Scoreboard' library job application task given to me by SportRadar company.
It has been written using TDD approach with consideration of Score Board bounded context which was discovered during the development. Due to that, I introduced general aggregate of ScoreBoardAggregate class that is a guardian of a rules given in the task.

Application itself doesn't have entry point (in form of any REST Api or Event listeners), Api, input layer as it was not the part of the task and I am a lazy-ass programmer (not really, but let's not be so serious, shall we?). It is in the autonomy of the future implementation to add this layer in consideration of applicable contract with the Client of my application.
I don't know if I have followed [SOLID](https://www.freecodecamp.org/news/solid-principles-for-better-software-design/) principles as I tend to forget all theses wise-sounding acronyms, which are no-more no-less than 'just do it reasonably right mate' rules. Especially that meaning of them (acronyms) you can easily Google out and after reading they don't change your life, blow your mind or don't even sound like they're some hidden wisdom that are bringing to you totally new perspective on coding.

Anyway...

I tried to write down the application in Hexagonal architecture (without Api layer). Entry point to the domain layer - that tries to model domain entities and value objects accordingly to what they are meant to do/keep - is ScoreBoardService (general application service for my discovered aggregate) class.
If Api layer is introduced it should be injected in it - to any controller classes that should trigger the processes around score boards.
ScoreBoardService class contains 5 public<sup>1</sup> methods:
- _initEmptyScoreBoard()_ - responsible for initial initialization of new ScoreBoard; during the development I decided this one would be needed as kind-of entry-point for further operation on existing ScoreBoardAggregate (saved in repository). It returns ID of successfully initialized ScoreBoard for further use.
- _initGameForScoreBoard(props...)_ - responsible for initialization of brand new game for ScoreBoard as it is stated in _task requirements_<sup>2<sup>. It returns ID of a newly created Game.
- _updateGameResultOnScoreBoard(props...)_ - responsible for updating the ongoing game results on the ScoreBoard as it is stated in _task requirements_<sup>2<sup>.
- _finishGameOnScoreBoard(props...)_ - responsible for finishing the ongoing game. Finishing game should remove it from the ScoreBoard as it is stated in _task requirements_<sup>2<sup>.
- _getGamesSummary(props...)_ - responsible for listing out the current in-progress games of the ScoreBoard (finished games should not be displayed<sup>3</sup>). Listing out ongoing (and started<sup>4</sup>) games should be in the order as it is stated in _task requirements_<sup>2<sup>.

These methods after successful execution are passing updated state of ScoreBoardAggregate to persistence layer through ScoreBoardRepository interface, that for now simply _saves_ and _gets_ the state of the aggregate. There's a place for improvement here with introduction of [locking mechanisms](https://www.linkedin.com/pulse/database-locking-strategies-balancing-act-between-speed-manish-joshi-6f9qc/) on database - to prevent concurrent execution and updating the state of the aggregate with no synchronisation safeguard -, but right now it's InMemory database as HashMap, so let's not overcomplicate things here. Especially it is an job application task and I have my passions to follow instead of sitting in front of my MacBook all-day-long.

## Improvements to-do:
As I mentioned I tried to deliver the MVP that meets task requirements, following general code principles that in my opinion makes this code well-written, understandable, possible to evolve and introduce improvements in simple way.

As for _improvements_ I can already see:
- filling gap in test cases:
  - tests for ScoreBoardAggregate class itself; they would in simpler manner check business rules that aggregate should keep guard
  - un-happy path tests cases with checking on introduced custom Exceptions
  - simple unit tests supporting customization of equals & hashCode methods for domain entities
  - tests cases showing the removal finished games from the ScoreBoard summary 
- introduction of locking mechanisms and transactional execution of 5 public methods of ScoreBoardService
- additional business logic, f.e.:
  - game/match can be finished only it passes required 90 minutes of time
- introduction of Clock @Bean to keep track of time regarding the machine/time-zone of the server this application is running on
- re-check the naming in the code - improve self-explaintoriness of the code
- in-code documentation when it is reasonable to add
- combining _Started_ and _Ongoing_ Game statuses and adding additional check on finishing game method - we should guard the rule that only initialized games should be finished.
- re-check of Game class and re-think the possibility of combining some parameters there in composed Value Objects (and keep the constraint in regards to those in their constructors!)


## Links & annotations
<sup>1</sup> package-private for now - different encapsulation is not needed due to non-existence of Api layer.
<sup>2<sup> task requirements - from PDF file given to me by SportRadar company.
<sup>3</sup> finished games not being displayed in games summary should be covered in test cases.
<sup>4</sup> _ongoing_ and _started_ game statuses are a bit of overengineering in current state, and they should be combined to one status.
