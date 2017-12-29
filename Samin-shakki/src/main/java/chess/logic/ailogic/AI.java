package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;

/**
 *
 * @author sami
 */
public interface AI {
    public void findBestMoves(GameSituation sit);
    public Move getBestMove();
}
