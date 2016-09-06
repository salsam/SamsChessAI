package chess.logic.gamelogic;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Piece;

/**
 * This class provides methods to check if player owns targeted piece, whether
 * certain movement is legal and whether given input is in allowed form.
 *
 * @author sami
 */
public class LegalityChecker {

    /**
     * ChessBoard that this LegalityChecker will use when checking legality of
     * movement or choosing piece.
     */
    private ChessBoard board;

    /**
     * Creates a new LegalityChecker for given chessboard.
     *
     * @param board board which this checker will be checking
     */
    public LegalityChecker(ChessBoard board) {
        this.board = board;
    }

    /**
     * Sets parameter to be referenced from field board.
     *
     * @param board board to be referenced from field board
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Checks whether player owns a piece on the square of which location is
     * given as input. If player owns a piece on target square, returns input.
     * If not, then returns empty string "".
     *
     * @param player player whose piece we're choosing.
     * @param column x-coordinate of target square
     * @param row y-coordinate of target square
     * @return input if player does own a piece on target square, else empty
     * string
     */
    public Boolean checkPlayerOwnsPieceOnTargetSquare(Player player, int column, int row) {

        if (!board.getSquare(column, row).containsAPiece()) {
            return false;
        }

        if (board.getSquare(column, row).getPiece().getOwner() != player) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether input corresponds a square that chosen piece can legally
     * move to. If it's true, returns input as it was given. Otherwise returns
     * empty string "".
     *
     * @param piece piece chosen for movement.
     * @param column column of target square.
     * @param row row of target square.
     * @return input if input corresponds a possible square. Else empty string
     * "".
     */
    public boolean checkThatMovementIsLegal(Piece piece, int column, int row) {
        return board.getMovementLogic().possibleMoves(piece, board).contains(board.getSquare(column, row));
    }
}
