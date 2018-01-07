#Todo-list
* Restructure running the program, AI's should have their own threads and copies of board to play with, which start running as game is started.
* General refactorization of code into smaller functions and classes. For example AILogic should have AI interface so multiple versions easier to use.0
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



Class specifics:
* Add is ended method to game.
* Add methods for synchronizing two games

AILogic:
* Fix bad movements

AI in general:
* Also end recursion if game has ended
