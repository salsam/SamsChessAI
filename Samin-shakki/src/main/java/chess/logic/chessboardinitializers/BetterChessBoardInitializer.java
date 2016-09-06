package chess.logic.chessboardinitializers;

import chess.domain.board.Piece;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import static chess.domain.board.Klass.*;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;

/**
 * This class is used to initialize standard starting positions of chess. Using
 * method initialize.
 *
 * @author samisalo
 */
public class BetterChessBoardInitializer extends ChessBoardInitializer {

    /**
     * Initializes the ChessBoard given as parameter with standard chess
     * starting positions.
     *
     * @param board ChessBoard to initialized.
     */
    @Override
    public void initialize(ChessBoard board) {
        clearBoard(board);
        initialiseKingsAndQueens(board);
        initialiseRooks(board);
        initialiseBishops(board);
        initialiseKnights(board);
        initialisePawns(board);
    }

    private void initialiseBishops(ChessBoard board) {
        putPieceOnBoard(board, new Piece(BISHOP, 2, 7, Player.WHITE, "wb1"));
        putPieceOnBoard(board, new Piece(BISHOP, 5, 7, Player.WHITE, "wb2"));
        putPieceOnBoard(board, new Piece(BISHOP, 2, 0, Player.BLACK, "bb1"));
        putPieceOnBoard(board, new Piece(BISHOP, 5, 0, Player.BLACK, "bb2"));
    }

    private void initialiseKingsAndQueens(ChessBoard board) {
        putPieceOnBoard(board, new Piece(KING, 4, 7, Player.WHITE, "wk"));
        putPieceOnBoard(board, new Piece(KING, 4, 0, Player.BLACK, "bk"));
        putPieceOnBoard(board, new Piece(QUEEN, 3, 7, Player.WHITE, "wq"));
        putPieceOnBoard(board, new Piece(QUEEN, 3, 0, Player.BLACK, "bq"));
    }

    private void initialiseKnights(ChessBoard board) {
        putPieceOnBoard(board, new Piece(KNIGHT, 1, 7, Player.WHITE, "wn1"));
        putPieceOnBoard(board, new Piece(KNIGHT, 6, 7, Player.WHITE, "wn2"));
        putPieceOnBoard(board, new Piece(KNIGHT, 1, 0, Player.BLACK, "bn1"));
        putPieceOnBoard(board, new Piece(KNIGHT, 6, 0, Player.BLACK, "bn2"));
    }

    private void initialiseRooks(ChessBoard board) {
        putPieceOnBoard(board, new Piece(ROOK, 0, 7, Player.WHITE, "wr1"));
        putPieceOnBoard(board, new Piece(ROOK, 7, 7, Player.WHITE, "wr2"));
        putPieceOnBoard(board, new Piece(ROOK, 0, 0, Player.BLACK, "br1"));
        putPieceOnBoard(board, new Piece(ROOK, 7, 0, Player.BLACK, "br2"));
    }

    private void initialisePawns(ChessBoard board) {
        for (int i = 0; i < 8; i++) {
            putPieceOnBoard(board, new Piece(PAWN, i, 6, Player.WHITE, "wp".concat(i + "")));
            putPieceOnBoard(board, new Piece(PAWN, i, 1, Player.BLACK, "bp".concat(i + "")));

        }
    }
}
