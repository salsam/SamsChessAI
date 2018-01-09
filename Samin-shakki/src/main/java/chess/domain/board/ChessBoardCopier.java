package chess.domain.board;

import chess.domain.GameSituation;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.PAWN;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.addPieceToOwner;
import java.util.ArrayList;

/**
 * This class is used to make copies of ChessBoards with method copy as well as
 * make two chessboards deeply equal or undo a move by making its start point
 * and end point equal to backup.
 *
 * @author sami
 */
public class ChessBoardCopier {

    /**
     * Creates a new ChessBoardCopier-object.
     */
    public ChessBoardCopier() {
    }

    /**
     * Returns a new ChessBoard that deeply equals the one given as parameter.
     *
     * @param board chessboard to be copied.
     * @return a deep copy of given ChessBoard.
     */
    public static ChessBoard copy(ChessBoard board) {
        ChessBoard copy = new ChessBoard(board.getMovementLogic());
        copy.setTable(copyPieceTable(board.getTable()));
        setPieces(copy);

        return copy;
    }

    private static Piece[][] copyPieceTable(Piece[][] table) {
        Piece[][] copyTable = new Piece[table.length][table[0].length];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j] != null) {
                    copyTable[i][j] = table[i][j].clone();
                }
            }
        }
        return copyTable;
    }

    private static void setPieces(ChessBoard board) {
        board.setBlackPieces(new ArrayList());
        board.setWhitePieces(new ArrayList());

        for (int i = 0; i < board.columnAmount; i++) {
            for (int j = 0; j < board.rowAmount; j++) {
                addPieceToOwner(new Square(i, j), board);
            }
        }
    }

    /**
     * This method is used to make first ChessBoard object given as parameter
     * deeply equal to second one without creating new objects. Thus this method
     * changes each field in first board to be equal to second one's.
     *
     * @param board ChessBoard to be changed to deeply equal second one
     * @param chessboard ChessBoard to be 'copied'
     */
    public static void revertOldSituation(ChessBoard board, ChessBoard chessboard) {
        clearBoardOfPieces(board);
        copyPieceList(board, chessboard);
    }

    private static void copyPieceList(ChessBoard board, ChessBoard chessboard) {
        for (Player player : Player.values()) {
            board.getPieces(player).clear();
            for (Piece playersPiece : chessboard.getPieces(player)) {
                ChessBoardInitializer.putPieceOnBoard(board, playersPiece.clone());
            }
        }
    }

    private static void clearBoardOfPieces(ChessBoard board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPiece(i,j, null);
            }

        }
        board.getPieces(Player.BLACK).clear();
        board.getPieces(Player.WHITE).clear();
    }

    /**
     * Offers a more effective way of reverting a single move instead of whole
     * series of moves.
     *
     * @param backUp backup of situation before move was made.
     * @param sit game situation after move was made.
     * @param from square that moved piece moved from.
     * @param to square that moved piece moved to.
     */
    public static void undoMove(ChessBoard backUp, GameSituation sit, Square from, Square to) {
        Piece old = backUp.getPiece(from);
        sit.decrementCountOfCurrentBoardSituation();
        if (old.getKlass() != sit.getChessBoard().getPiece(to).getKlass()) {
            sit.updateHashForUndoingPromotion(to);
        }
        sit.updateHashForUndoingMove(backUp, from, to);

        sit.getChessBoard().setPiece(from, sit.getChessBoard().getPiece(to));
        sit.getChessBoard().getPiece(from).makeDeeplyEqualTo(old);

        handleDestination(backUp, to, sit, from);
    }
    
    /**
     * Undo move and set movesTillDraw.
     * @param backUp
     * @param sit
     * @param from
     * @param to
     * @param movesTillDraw 
     */
    public static void undoMove(ChessBoard backUp, GameSituation sit, Square from, Square to, int movesTillDraw) {
        undoMove(backUp, sit, from, to);
        sit.setMovesTillDraw(movesTillDraw);
    }

    private static void handleDestination(ChessBoard backUp, Square to, GameSituation sit, Square from) {
        Piece taken = backUp.getPiece(to);
        if (taken != null) {
            putTakenPieceBackOnBoard(sit.getChessBoard(), taken, to);
        } else {
            sit.getChessBoard().setPiece(to, null);
            handleCastling(from, to, sit, backUp);
            handleEnPassant(from, to, sit, backUp);
        }
    }

    private static void putTakenPieceBackOnBoard(ChessBoard board, Piece taken, Square to) {
        for (Piece piece : board.getPieces(taken.getOwner())) {
            if (piece.getPieceCode().equals(taken.getPieceCode())) {
                piece.makeDeeplyEqualTo(taken);
                board.setPiece(to, piece);
                break;
            }
        }
    }

    /**
     * If move was enpassant, reverts taken pawn enpassant back on chessboard.
     *
     * @param from square moved from.
     * @param to square moved to.
     * @param sit current game situation.
     * @param backUp backup of chessboard before move.
     */
    private static void handleEnPassant(Square from, Square to, GameSituation sit, ChessBoard backUp) {
        if (sit.getChessBoard().getPiece(from).getKlass() == PAWN && from.getColumn() != to.getColumn()) {
            Square enpassanted = new Square(to.getColumn(), from.getRow());
            Piece taken = backUp.getPiece(enpassanted);
            putTakenPieceBackOnBoard(sit.getChessBoard(), taken, enpassanted);
            sit.reHashBoard(false);
        }
    }

    /**
     * If move was castling, reverts the rook that was moved back to its old
     * position on chessboard. If moved piece was king and it was moved 2
     * squares horizontally, we know it was castled. Thus after moving king back
     * original location, we also move relevant rook back to original location.
     *
     * @param from square moved from.
     * @param to square moved to.
     * @param sit current game situation.
     * @param backUp backup of situation before move.
     */
    private static void handleCastling(Square from, Square to, GameSituation sit, ChessBoard backUp) {
        if (sit.getChessBoard().getPiece(from).getKlass() == KING) {
            if (from.getColumn() - to.getColumn() == -2) {
                Square rookFrom = new Square(7, from.getRow());
                Square rookTo = new Square(to.getColumn() - 1, to.getRow());
                sit.incrementCountOfCurrentBoardSituation();
                undoMove(backUp, sit, rookFrom, rookTo);
            } else if (from.getColumn() - to.getColumn() == 2) {
                Square rookFrom = new Square(0, from.getRow());
                Square rookTo = new Square(to.getColumn() + 1, to.getRow());
                sit.incrementCountOfCurrentBoardSituation();
                undoMove(backUp, sit, rookFrom, rookTo);
            }
        }
    }

    /**
     * Checks if both chessboard have deeply equal pieces on same squares.
     *
     * @param cb1 first chessboard.
     * @param cb2 second chessboard.
     * @return true if all squares contain deeply equal pieces.
     */
    public static boolean chessBoardsAreDeeplyEqual(ChessBoard cb1, ChessBoard cb2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cb1.getPiece(i, j) == null) {
                    if (cb2.getPiece(i, j) != null) {
                        return false;
                    }
                } else if (!cb1.getPiece(i, j).deepEquals(cb2.getPiece(i, j))) {
                    return false;
                }
            }
        }

        return true;
    }
}
