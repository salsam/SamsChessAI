package chess.logic.chessboardinitializers;

import chess.domain.board.ChessBoard;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.KING;
import chess.domain.board.Player;

/**
 * All classes that inherit this abstract class are used to initialize different
 * starting situations on chessboard like empty board or standard starting
 * positions.
 *
 * @author sami
 */
public abstract class ChessBoardInitializer {

    /**
     * This method will be used to initialize chessboard meaning setting up
     * starting positions depending on which ChessBoardInitializer is used..
     *
     * @param board board to initialized
     */
    public abstract void initialize(ChessBoard board);

    /**
     * Adds the piece on target square to list of pieces its owner owns. Also
     * adds a reference to Map Kings if the piece is of King class.
     *
     * @param target Square
     * @param chessBoard ChessBoard to which piece will be added
     */
    
    public static void addPieceToOwner(Square target, ChessBoard chessBoard) {
        if (chessBoard.getPiece(target) != null) {
            Piece piece = chessBoard.getPiece(target);
            if (piece.getKlass() == KING) {
                chessBoard.getKings().put(piece.getOwner(), piece);
            }
            chessBoard.getPieces(piece.getOwner()).add(piece);
        }
    }

    /**
     * Removes target piece from its owner's owned pieces list.
     *
     * @param piece The piece you want to remove.
     * @param chessBoard ChessBoard where piece will be removed from
     */
    public static void removePieceFromOwner(Piece piece, ChessBoard chessBoard) {
        chessBoard.getPieces(piece.getOwner()).remove(piece);
    }

    protected void clearBoard(ChessBoard board) {
        board.getPieces(Player.BLACK).clear();
        board.getPieces(Player.WHITE).clear();
        for (int i = 0; i < board.columnAmount; i++) {
            for (int j = 0; j < board.rowAmount; j++) {
                if (board.getPiece(i,j) != null) {
                    //Removing pieces one by one is inefficient
                    //removePieceFromOwner(board.getPiece(i, j), board);
                    board.setPiece(i,j, null);
                }
            }
        }
    }

    /**
     * Makes the square piece is located on refer to piece thus adding piece on
     * chessboard and adds piece to list of owner's pieces.
     *
     * @param board board Piece will be placed on.
     * @param piece piece Piece to be placed.
     */
    public static void putPieceOnBoard(ChessBoard board, Piece piece) {
        if (board.withinTable(piece.getColumn(), piece.getRow())) {
            board.setPiece(piece.getColumn(), piece.getRow(),piece);
            addPieceToOwner(new Square(piece.getColumn(), piece.getRow()), board);
        }
    }
}
