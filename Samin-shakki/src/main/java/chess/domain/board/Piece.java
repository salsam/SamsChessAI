package chess.domain.board;

import static chess.domain.board.Player.BLACK;
import java.util.Objects;

/**
 * This represents chess pieces.
 *
 * @author sami
 */
public class Piece {

    private Klass klass;
    private int column;
    private int row;
    private String pieceCode;
    private Player owner;
    private boolean taken;
    private boolean hasBeenMoved;
    private boolean movedTwoSquaresLastTurn;

    public Piece(Klass klass, int column, int row, Player owner, String pieceCode) {
        this.klass = klass;
        this.column = column;
        this.row = row;
        this.pieceCode = pieceCode;
        this.owner = owner;
    }

    public Klass getKlass() {
        return klass;
    }

    public void setKlass(Klass klass) {
        this.klass = klass;
    }

    public boolean isHasBeenMoved() {
        return hasBeenMoved;
    }

    public void setHasBeenMoved(boolean hasBeenMoved) {
        this.hasBeenMoved = hasBeenMoved;
    }

    public boolean isMovedTwoSquaresLastTurn() {
        return movedTwoSquaresLastTurn;
    }

    public void setMovedTwoSquaresLastTurn(boolean movedTwoSquaresLastTurn) {
        this.movedTwoSquaresLastTurn = movedTwoSquaresLastTurn;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getPieceCode() {
        return pieceCode;
    }

    public void setPieceCode(String pieceCode) {
        this.pieceCode = pieceCode;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    /**
     * Tests if this chess piece is deeply equal to the one given as parameter.
     *
     * @param other chess peice compared to.
     * @return true if this piece is deeply equal to other one. Else false.
     */
    public boolean deepEquals(Piece other) {
        if (other == null) {
            return false;
        }
        if (!this.pieceCode.equals(other.getPieceCode())) {
            return false;
        }

        if (this.klass != other.getKlass()) {
            return false;
        }

        if (this.column != other.getColumn()) {
            return false;
        }
        if (this.row != other.getRow()) {
            return false;
        }

        if (this.owner != other.getOwner()) {
            return false;
        }

        if (this.taken != other.isTaken()) {
            return false;
        }

        if (this.hasBeenMoved != other.isHasBeenMoved()) {
            return false;
        }

        return this.movedTwoSquaresLastTurn == other.isMovedTwoSquaresLastTurn();
    }

    /**
     * Makes this chess piece deeply equal to other one by manually changing all
     * fields to be equal.
     *
     * @param other piece that this will become identical with.
     */
    public void makeDeeplyEqualTo(Piece other) {
        klass = other.getKlass();
        column = other.getColumn();
        row = other.getRow();
        owner = other.getOwner();
        taken = other.isTaken();
        hasBeenMoved = other.isHasBeenMoved();
        movedTwoSquaresLastTurn = other.isMovedTwoSquaresLastTurn();
        pieceCode = other.getPieceCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.pieceCode);
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
        final Piece other = (Piece) obj;
        if (!Objects.equals(this.pieceCode, other.pieceCode)) {
            return false;
        }
        return true;
    }

    @Override
    public Piece clone() {
        Piece ret = new Piece(klass, column, row, owner, pieceCode);
        ret.setTaken(taken);
        ret.setHasBeenMoved(hasBeenMoved);
        ret.setMovedTwoSquaresLastTurn(movedTwoSquaresLastTurn);
        return ret;
    }

    /**
     * Checks if this piece is at opposing end of chessboard. This is used to
     * check if pawn is valid for promotion.
     *
     * @return true if piece is at opposing end of chessboard; else false.
     */
    public boolean isAtOpposingEnd() {
        if (owner == BLACK) {
            return row == 7;
        }
        return row == 0;
    }

    @Override
    public String toString() {
        return "piececode: " + pieceCode + " class: " + klass + " location: ("
                + column + "," + row + ") owner: " + owner + " taken: " + taken
                + " hasbeenmoved: " + hasBeenMoved + " moved2squares: " + movedTwoSquaresLastTurn;
    }

}
