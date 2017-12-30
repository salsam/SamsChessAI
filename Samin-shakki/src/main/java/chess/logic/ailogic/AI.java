package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;

/**
 *
 * @author sami
 */
public interface AI {
    public Move findBestMove(GameSituation situation);
    public void setTimeLimit(long time);
    public void reset();
}
