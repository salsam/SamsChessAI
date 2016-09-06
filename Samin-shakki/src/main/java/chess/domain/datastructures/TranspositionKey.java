package chess.domain.datastructures;

import chess.domain.board.Player;
import java.util.Objects;

/**
 * This class is used as keys in transposition tables.
 *
 * @author sami
 */
public class TranspositionKey {

    private Player whoseTurn;
    private long hashedBoard;
    private boolean saved;

    public TranspositionKey(Player whoseTurn, long hash) {
        this.whoseTurn = whoseTurn;
        this.hashedBoard = hash;
        this.saved = false;
    }

    public Player getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(Player second) {
        this.whoseTurn = second;
    }

    public long getHashedBoard() {
        return hashedBoard;
    }

    public void setHashedBoard(long hashedBoard) {
        this.hashedBoard = hashedBoard;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.whoseTurn);
        hash = 47 * hash + (int) (this.hashedBoard ^ (this.hashedBoard >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TranspositionKey other = (TranspositionKey) obj;
        if (this.hashedBoard != other.getHashedBoard()) {
            return false;
        }
        if (this.whoseTurn != other.getWhoseTurn()) {
            return false;
        }
        return true;
    }

}
