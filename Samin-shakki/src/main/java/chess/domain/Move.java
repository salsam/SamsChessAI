package chess.domain;

import chess.domain.board.Square;
import chess.domain.board.Piece;
import java.util.Objects;

/**
 * One move made by a player. Knows piece being moved, square that movement starts from as well as the end square.
 *
 * @author sami
 */
public class Move {

    private Piece piece;
    private Square from;
    private Square target;

    public Move(Piece piece, Square target) {
        this.piece = piece;
        this.target = target;
    }

    public Move(Piece piece, Square target, Game game) {
        this.piece = piece;
        this.target = target;
        this.from = new Square(piece.getColumn(), piece.getRow());
    }

    public Move(Piece piece, int column, int row, Game game) {
        this.piece = piece;
        this.from = new Square(piece.getColumn(), piece.getRow());
        this.target = new Square(column, row);
    }

    public Square getFrom() {
        return from;
    }

    public void setFrom(Square from) {
        this.from = from;
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

    public int getTargetColumn() {
        return this.target.getColumn();
    }

    public int getTargetRow() {
        return this.target.getRow();
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
