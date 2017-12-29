package chess.domain.board;

import chess.domain.Coordinates;
import java.util.Objects;

/**
 * Square class is responsible for keeping track of its location and possible
 * piece situated on it.
 *
 * @author samisalo
 */
public class Square {

    private Coordinates location;
    /**
     * Piece that is situated on this square, null if there's no piece.
     */
    private Piece piece;

    /**
     * Creates a new square with given row and column.Field piece will be null
     * if there's no piece on a square so initialized as null.
     *
     * @param column column of created square
     * @param row row of created square
     */
    public Square(int column, int row) {
        this.location=new Coordinates(column, row);
    }
    
    public Square(Coordinates location) {
        this.location=location;
    }
    
    public Coordinates getLocation() {
        return this.location;
    }

    public int getColumn() {
        return this.location.getColumn();
    }

    public int getRow() {
        return this.location.getRow();
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Square square = (Square) obj;

        if (!this.location.equals(square.location)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.location);
        return hash;
    }

    /**
     * Returns true if Square contains a piece. Square contains a piece if field
     * piece doesn't refer to null or taken piece.
     *
     * @return true if Square contains a piece
     */
    public boolean containsAPiece() {
        if (piece == null) {
            return false;
        }
        return !piece.isTaken();
    }

    @Override
    public String toString() {
        return "(" + this.location + ")";
    }

    /**
     * Returns a field to field copy of this square.
     *
     * @return a deep copy of this square
     */
    @Override
    public Square clone() {
        Square clone = new Square(this.location);

        if (this.piece != null) {
            clone.setPiece(this.piece.clone());
        }

        return clone;
    }

}
