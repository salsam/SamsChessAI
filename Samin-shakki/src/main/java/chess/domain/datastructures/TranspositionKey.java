package chess.domain.datastructures;

import chess.domain.board.Player;
import java.util.Objects;

/**
 * This class is used as keys in transposition tables.
 * 
 * Note:Doesn't account for earlier met situations!
 *
 * @author sami
 */
public class TranspositionKey {

    private Player whoseTurn;
    private long hashedBoard;
    private int height;
    private int turnsTillDraw;

    public TranspositionKey(Player whoseTurn, long hash) {
        this.whoseTurn = whoseTurn;
        this.hashedBoard = hash;
    }

    public TranspositionKey(Player whoseTurn, long hash, int height, int turnsTillDraw) {
        this.whoseTurn = whoseTurn;
        this.hashedBoard = hash;
        this.height = height;
        this.turnsTillDraw = turnsTillDraw;
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

    public int getTurnsTillDraw() {
        return turnsTillDraw;
    }

    public void setTurnsTillDraw(int turnsTillDraw) {
        this.turnsTillDraw = turnsTillDraw;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.whoseTurn);
        hash = 83 * hash + (int) (this.hashedBoard ^ (this.hashedBoard >>> 32));
        hash = 83 * hash + this.height;
        hash = 83 * hash + this.turnsTillDraw;
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
        if (this.hashedBoard != other.hashedBoard) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (this.turnsTillDraw != other.turnsTillDraw) {
            return false;
        }
        if (this.whoseTurn != other.whoseTurn) {
            return false;
        }
        return true;
    }

}
