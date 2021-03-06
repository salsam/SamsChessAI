package chess.domain.board;

import chess.logic.movementlogic.MovementLogic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
     * Table containing all the pieces on the game table
     */
    private Piece[][] table;
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
    
    public final int columnAmount=8;
    public final int rowAmount=8;

    /**
     * Creates a new chessboard with given movement logic.
     *
     * @param movementLogic movement logic to be applied on this chessboard.
     */
    public ChessBoard(MovementLogic movementLogic) {
        initializeBoard();
        this.movementLogic = movementLogic;
        this.blackPieces = new ArrayList<>();
        this.whitePieces = new ArrayList<>();
        this.squaresThreatenedByBlack = new HashSet();
        this.squaresThreatenedByWhite = new HashSet();
        this.kings = new HashMap();
    }

    /**
     * Initializes a new 8x8 board.
     */
    private void initializeBoard() {
        table= new Piece[8][8];
    }

    public Piece[][] getTable() {
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
    public void setTable(Piece[][] newBoard) {
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
    
    public boolean squareIsOccupied(Square square) {
        return getPiece(square)!=null && !getPiece(square).isTaken();
    }
    
    public boolean squareIsOccupied(int col, int row) {
        return getPiece(col, row)!=null && !getPiece(col, row).isTaken();
    }

    public Piece getPiece(Square square) {
        return table[square.getColumn()][square.getRow()];
    }
    
    public Piece getPiece(int column, int row) {
        return table[column][row];
    }
    
    public void setPiece(Square coords, Piece piece) {
        table[coords.getColumn()][coords.getRow()]=piece;
    }
    
    public void setPiece(int col, int row, Piece piece) {
        table[col][row]=piece;
    }

    /**
     * Checks if the given location is on the chessboard.
     *
     * @param column Column of the Square being checked.
     * @param row Row of the Square being checked.
     * @return true if given coordinates are within the table.
     */
    public boolean withinTable(int column, int row) {
        if (column < 0 || column >= columnAmount) {
            return false;
        }
        return !(row < 0 || row >= rowAmount);
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
                if (table[j][i] == null) {
                    System.out.print("NU");
                } else {
                    System.out.print(table[j][i].getKlass().toString().substring(0, 2));
                }
            }
            System.out.println("");
        }
    }
}
