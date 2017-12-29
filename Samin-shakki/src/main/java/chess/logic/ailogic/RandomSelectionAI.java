package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.ChessBoard;
import chess.domain.board.Piece;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.logic.movementlogic.MovementLogic;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author sami
 */
public class RandomSelectionAI implements AI {

    private Move bestMove;
    private MovementLogic movementLogic;
    private Random random;

    public RandomSelectionAI(MovementLogic movementLogic) {
        this.movementLogic = movementLogic;
        this.random = new Random();
    }

    @Override
    public void findBestMoves(GameSituation sit) {
        Player p = sit.whoseTurn();
        Set<Move> possibilities = movementLogic.possibleMovementsByPlayer(p, sit.getChessBoard());
        int i = random.nextInt(possibilities.size());
        int j = 0;
        for (Move m : possibilities) {
            if (i == j) {
                bestMove = m;
                break;
            }
            j++;
        }
    }

    @Override
    public Move getBestMove() {
        return bestMove;
    }

}
