package chess.domain.board;

/**
 * Square class is responsible for keeping track of its location.
 *
 * @author samisalo
 */
public class Square {

    /**
     * Column of this square.
     */
    private int column;
    /**
     * Row of this square.
     */
    private int row;

    /**
     * Creates a new square with given row and column.Field piece will be null
     * if there's no piece on a square so initialized as null.
     *
     * @param column column of created square
     * @param row row of created square
     */
    public Square(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Square square = (Square) obj;

        if (this.column != square.getColumn()) {
            return false;
        }

        if (this.row != square.getRow()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.column;
        hash = 47 * hash + this.row;
        return hash;
    }

    @Override
    public String toString() {
        return "(" + column + "," + row + ")";
    }

    /**
     * Returns a field to field copy of this square.
     *
     * @return a deep copy of this square
     */
    @Override
    public Square clone() {
        return new Square(this.column, this.row);
    }

}
