package chess.domain.board;

import chess.domain.datastructures.MyArrayList;
import chess.domain.datastructures.MyHashMap;
import chess.domain.datastructures.MyHashSet;
import chess.logic.movementlogic.MovementLogic;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for keeping track of the current situation on
 * board. This class also offers methods to access every piece on board and
 * Squares they threaten or can move to.
 *
 * @author sami
 */
public class ChessBoard {

    /**
     * Table containing all the squares on the game table
     */
    private Square[][] table;
    /**
     * List of all pieces that white owns.
     */
    private List<Piece> whitePieces;
    /**
     * List of all pieces that black owns.
     */
    private List<Piece> blackPieces;
    /**
     * Set containing all squares that black threatens.
     */
    private Set<Square> squaresThreatenedByBlack;
    /**
     * Set containing all squares that white threatens.
     */
    private Set<Square> squaresThreatenedByWhite;
    /**
     * MovementLogic used to see which moves are legal on this ChessBoard.
     */
    private MovementLogic movementLogic;
    /**
     * Map containing positions of both kings.
     */
    private Map<Player, Piece> kings;

    /**
     * Creates a new chessboard with given movement logic.
     *
     * @param movementLogic movement logic to be applied on this chessboard.
     */
    public ChessBoard(MovementLogic movementLogic) {
        initializeBoard();
        this.movementLogic = movementLogic;
        this.blackPieces = new MyArrayList<>();
        this.whitePieces = new MyArrayList<>();
        this.squaresThreatenedByBlack = new MyHashSet();
        this.squaresThreatenedByWhite = new MyHashSet();
        this.kings = new MyHashMap();
    }

    /**
     * Initializes a new 8x8 board.
     */
    private void initializeBoard() {
        table = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.table[i][j] = new Square(i, j);
            }
        }
    }

    /**
     * Returns the Square[][] saved to field board.
     *
     * @return Reference to Square[][] that is saved to field board.
     */
    public Square[][] getTable() {
        return table;
    }

    public MovementLogic getMovementLogic() {
        return movementLogic;
    }

    /**
     * Sets the board given as parameter to field board.
     *
     * @param newBoard Square[][] to be saved to field board.
     */
    public void setTable(Square[][] newBoard) {
        this.table = newBoard;
    }

    /**
     * Returns a map with references from each player to their king.
     *
     * @return map with references from each player to their king.
     */
    public Map<Player, Piece> getKings() {
        return this.kings;
    }

    /**
     * Updates the Squares that player's pieces threaten to corresponding field.
     * This methods uses the MovementLogic given to it in constructor to check
     * which squares are currently threatened by player.
     *
     * @param player Player whose corresponding field you want to update.
     */
    public void updateThreatenedSquares(Player player) {
        if (player == Player.WHITE) {
            squaresThreatenedByWhite = movementLogic.squaresThreatenedByPlayer(Player.WHITE, this);
        } else {
            squaresThreatenedByBlack = movementLogic.squaresThreatenedByPlayer(Player.BLACK, this);
        }
    }

    /**
     * Returns a list containing all pieces currently on board and owned by the
     * player.
     *
     * @param player player whose pieces you want.
     * @return list containing all pieces owned by the player.
     */
    public List<Piece> getPieces(Player player) {
        if (player == Player.WHITE) {
            return whitePieces;
        } else {
            return blackPieces;
        }
    }

    public void setBlackPieces(List<Piece> blackPieces) {
        this.blackPieces = blackPieces;
    }

    public void setWhitePieces(List<Piece> whitePieces) {
        this.whitePieces = whitePieces;
    }

    /**
     * Returns the Square at given location on board.
     *
     * @param column Column of the Square you want.
     * @param row Row of the Square you want.
     * @return Square at given location.
     */
    public Square getSquare(int column, int row) {
        return table[column][row];
    }

    /**
     * Checks if the given location is on the chessboard.
     *
     * @param column Column of the Square being checked.
     * @param row Row of the Square being checked.
     * @return true if given coordinates are within the table.
     */
    public boolean withinTable(int column, int row) {
        if (column < 0 || column >= table.length) {
            return false;
        }
        return !(row < 0 || row >= table[0].length);
    }

    /**
     * Returns a set containing all Squares that player threatens.
     *
     * @param player Player
     * @return set containing all squares player threatens
     */
    public Set<Square> threatenedSquares(Player player) {
        if (player == Player.WHITE) {
            return squaresThreatenedByWhite;
        } else {
            return squaresThreatenedByBlack;
        }
    }

    public void printTable() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (table[j][i].getPiece() == null) {
                    System.out.print("NU");
                } else {
                    System.out.print(table[j][i].getPiece().getKlass().toString().substring(0, 2));
                }
            }
            System.out.println("");
        }
    }
}
