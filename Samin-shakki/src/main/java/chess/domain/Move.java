package chess.domain;

import chess.domain.board.Square;
import chess.domain.board.Piece;
import java.util.Objects;

/**
 *
 * @author sami
 */
public class Move {

    private Piece piece;
    private Square target;

    public Move(Piece piece, Square target) {
        this.piece = piece;
        this.target = target;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Square getTarget() {
        return target;
    }

    public void setTarget(Square target) {
        this.target = target;
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
        final Move other = (Move) obj;
        if (!Objects.equals(this.piece, other.piece)) {
            return false;
        }
        if (!Objects.equals(this.target, other.target)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + this.piece + ", " + this.target + ")";
    }

}
