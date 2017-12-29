#Todo-list
* Restructure running the program, AI's should have their own threads and copies of board to play with, which start running as game is started.
* General refactorization of code into smaller functions and classes. For example AILogic should have AI interface so multiple versions easier to use.0
* Add static exchange evaluation to improve AI
* Add coloring to last move so it's much easier to spot.
* Improve evaluation function by adding king safety/pawn structure
* Add memory for moves and function that allows players to undo their moves
* Add menu that allows players to restart game/return to main menu during game.
* Fix evaluation sometimes stopping at first level (sometimes also breaks test aiWillNotInitiateLosingTrades, could be useful for debugging)
* Is there sitll problems with Zobrist hashing causing hash collisions? Recheck that is functions as it should, if necessary consider alternatives.
* MovementLogic should be game specific not chessboard specific!
