package chess.domain;

/**
 *This class only contains coordinate information for pieces and squares.
 * 
 * This class exists to avoid confusion for using squares as both coordinate objects as chessboard objects.
 * @author sami
 */
public class Coordinates {
    private int column;
    private int row;

    public Coordinates(int column, int row) {
        this.column = column;
        this.row = row;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.column;
        hash = 67 * hash + this.row;
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
        final Coordinates other = (Coordinates) obj;
        if (this.column != other.column) {
            return false;
        }
        if (this.row != other.row) {
            return false;
        }
        return true;
    }
    
    @Override
    public Coordinates clone() {
        return new Coordinates(column, row);
    }

    @Override
    public String toString() {
        return "(" + column + "," + row + ")";
    }
}
