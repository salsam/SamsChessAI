#Todo-list
* Restructure running the program, AI's should have their own threads and copies of game to play with, which start running as game is started.
* General refactorization of code into smaller functions and classes. For example AILogic should have AI interface so multiple versions easier to use.
* Add static exchange evaluation to improve AI
* Improve evaluation function by adding king safety/pawn structure
* Add memory for moves and function that allows players to undo their moves
* Add menu that allows players to restart game/return to main menu during game.
* Fix evaluation sometimes stopping at first level (sometimes also breaks test aiWillNotInitiateLosingTrades, could be useful for debugging)
* Is there still problems with Zobrist hashing causing hash collisions? Recheck that is functions as it should, if necessary consider alternatives.
* MovementLogic should be game specific not chessboard specific!
* More testing for classes. Will be easier after refactorization.
* Is it really good idea to have each piece know it's location? Is this necessary? Main benefit is easy looping through piece list.
* Fix last move changing when trying illegal move (when checked).
* Fix AI sometimes stopping looking for better move to early. (Recursion depth 1)
* Repainting now works "immediately" by waiting 100ms before AI starts doing anything. Better solution would be nice.
* AIs should have their own game objects which will be synchronized each turn. This fixes repainting as well as allows AI to use players turn for computing next move.
* To compare results from different levels, choose move with highest associated value from best move for last completed BFS and first incomplete. Alternative would be always taking best from last complete.
* Add option to suggest to end game as a draw (could even make AI agreed if and only if it evaluates current situation negative)
* Add method that moves a piece and cancels movement if players king is left checked.



*MORE TESTS!


Class specifics:

Game:
* Add is ended method
* Add methods for synchronizing two games

AIs:
* Add own game objects that will be synchronized each turn
* Add possibility of using opponents turn for computation

AILogic:
* Fix bad movements, does AI still perform overly bad movements?
* Only loop through moves once, use possibleMovements for computation
* Don't loop through pieces and then through all possible moves by the piece
* Moves can be ordered as well more easily afterwards
* Consider all ending conditions using game.isEnded etc.
* Currently move ordering not in use, add ordering: winning captures, neutral captures, losing captures, positional move

GameSituationEvaluator:
* Add king safety by considering threatened squares near king
* Consider all ending conditions including material limit and 50 turn limit

AI in general:
* Also end recursion if game has ended

CheckingLgic:
* Should static methods be preferred over non-static? Would alleviate number of unnecessary function calls but object version better for possible future gamemodes.

GUI improvements:
* Consider checks when showing possible moves.
* Better end game screen to make game result more clear.
* Option to quit/restart/main menu for gamewindow

##Fixing moves till draw
* Currently each time a move is tested, moves till draw decreases even when move gets reverted
* Fix by memorizing movesTillDraw before movement and resetting to old value with undo.
