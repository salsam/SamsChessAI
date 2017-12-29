package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.Player;
import chess.logic.movementlogic.MovementLogic;
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
    public Move findBestMove(GameSituation situation) {
        Player p = situation.whoseTurn();
        Set<Move> possibilities = movementLogic.possibleMovementsByPlayer(p, situation.getChessBoard());
        int i = random.nextInt(possibilities.size());
        int j = 0;
        for (Move m : possibilities) {
            if (i == j) {
                return m;
            }
            j++;
        }
        return null;
    }

    @Override
    public void reset() {
    }
}
