package chess.domain;

import chess.domain.board.ChessBoard;
import chess.domain.board.Piece;
import java.util.Objects;

/**
 * One move made by a player. Knows piece being moved, square that movement
 * starts from as well as the end square.
 *
 * @author sami
 */
public class Move {

    private Piece piece;
    private Coordinates from;
    private Coordinates target;

    //Warning! Ensure you know where piece actually is!
    public Move(Piece piece, Coordinates target) {
        this.piece = piece;
        this.target = target;
        this.from = piece.getLocation();
    }

    //Deprecated!
    public Move(Piece piece, Coordinates target, ChessBoard board) {
        this.piece = piece;
        this.target = target;
        this.from = piece.getLocation();
    }

    public Move(Piece piece, Coordinates target, Game game) {
        this.piece = piece;
        this.target = target;
        this.from = piece.getLocation();
    }

    public Move(Piece piece, int column, int row, Game game) {
        this.piece = piece;
        this.from = piece.getLocation();
        this.target = new Coordinates(column, row);
    }

    public Coordinates getFrom() {
        return from;
    }

    public void setFrom(Coordinates from) {
        this.from = from;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Coordinates getTarget() {
        return target;
    }

    public void setTarget(Coordinates target) {
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
        return "(" + this.piece + ", from " + this.from + " to " + this.target + ")";
    }

}
