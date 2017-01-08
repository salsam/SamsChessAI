package chess.domain;

import chess.domain.board.Square;
import chess.domain.board.Piece;
import java.util.Objects;

/**
 * One move made by a player.
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
        this.from = game.getSituation().getChessBoard().getSquare(piece.getColumn(), piece.getRow());
    }

    public Move(Piece piece, int column, int row, Game game) {
        this.piece = piece;
        this.from = game.getSituation().getChessBoard().getSquare(piece.getColumn(), piece.getRow());
        this.target = game.getSituation().getChessBoard().getSquare(column, row);
    }

    public Square getFrom() {
        return from;
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public void setFrom(Game game) {
        this.from = game.getSituation().getChessBoard().getSquare(piece.getColumn(), piece.getRow());
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
