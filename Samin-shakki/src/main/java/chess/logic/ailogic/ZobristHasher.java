package chess.logic.ailogic;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import static chess.domain.board.Klass.*;
import java.util.Random;
import java.util.Set;

/**
 * This class is used to hash chessboard situations by Zobrist-hashing. Hashes
 * will then be used for transformation tables to speed up negamax.
 *
 * @author sami
 */
public class ZobristHasher {

    private long[][] squareHashes;

    /**
     * Creates a new ZobristHasher-object giving each square+piece combination a
     * random long value. Assumes no same hashes were received as chance is
     * miniscule. 14 different kinds of pieces can be on each square as empty
     * square and king that can castle vs king that can't are their own
     * alternatives. Indices for different pieces are 0 for empty and 13 for
     * king that can castle. Then 1-6 for white and 7-12 for black pieces. 1 is
     * pawn, 2 is rook, 3 is knight, 4 is bishop, 5 is queen and 6 is king.
     */
    public ZobristHasher() {
        squareHashes = new long[64][14];
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 14; j++) {
                squareHashes[i][j] = random.nextLong();
            }
        }
    }

    /**
     * Returns index associated with piece that square contains. This index can
     * then be used to check hash of given square on given board.
     *
     * @param board chessboard that square is on.
     * @param sq square of which piece is being checked.
     * @return index of piece placed on this square.
     */
    private int numberOfPieceAtSquare(ChessBoard board, Square square) {
        Square sq = board.getSquare(square.getColumn(), square.getRow());
        if (!sq.containsAPiece()) {
            return 0;
        }
        int ret = 0;

        if (sq.getPiece().getOwner() == Player.BLACK) {
            ret += 6;
        }

        if (sq.getPiece().getKlass() == PAWN) {
            ret += 1;
        } else if (sq.getPiece().getKlass() == ROOK) {
            ret += 2;
        } else if (sq.getPiece().getKlass() == KNIGHT) {
            ret += 3;
        } else if (sq.getPiece().getKlass() == BISHOP) {
            ret += 4;
        } else if (sq.getPiece().getKlass() == QUEEN) {
            ret += 5;
        } else if (kingCanCastle(board, sq)) {
            ret = 13;
        } else {
            ret += 6;
        }

        return ret;

    }

    private boolean kingCanCastle(ChessBoard board, Square sq) {
        Set kingPossibleMoves = board.getMovementLogic().possibleMoves(sq.getPiece(), board);

        if (sq.getColumn() == 4) {
            if (kingPossibleMoves.contains(board.getSquare(sq.getColumn() - 2, sq.getRow()))) {
                return true;
            } else if (kingPossibleMoves.contains(board.getSquare(sq.getColumn() + 2, sq.getRow()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Hashes given chessboard using previously generated square hashes and
     * bitwise XOR-operation. Chance of having two different board situations
     * with same hash is miniscule and thus ignored.
     *
     * @param board chessboard to be hashed.
     * @return hash of given chessboard.
     */
    public long hash(ChessBoard board) {
        long hash = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                hash ^= squareHashes[8 * i + j][numberOfPieceAtSquare(board, board.getSquare(i, j))];
            }
        }

        return hash;
    }

    /**
     * Updates hash of given chessboard when moving a piece from square from to
     * square to. First XORs out the piece in square from, then XORs in empty
     * square to from. Afterwards XORs out piece in square to and then XORs in
     * moved piece at square to.
     *
     * @param hash old hash of chessboard.
     * @param board chessboard of which hash is being updated.
     * @param from square that moved piece is on.
     * @param to square that moved piece is moved to.
     * @return new hash of chessboard after movement.
     */
    public long getHashAfterMove(long hash, ChessBoard board, Square from, Square to) {
        hash ^= squareHashes[8 * from.getColumn() + from.getRow()][numberOfPieceAtSquare(board, from)];
        hash ^= squareHashes[8 * from.getColumn() + from.getRow()][0];
        hash ^= squareHashes[8 * to.getColumn() + to.getRow()][numberOfPieceAtSquare(board, to)];
        hash ^= squareHashes[8 * to.getColumn() + to.getRow()][numberOfPieceAtSquare(board, from)];
        return hash;
    }

    /**
     * Updates hash for given chessboard to hash of situation before move was
     * made.
     *
     * @param hash old hash of chessboard.
     * @param board chessboard of which hash is being updated.
     * @param backup backup of chessboard before move was made.
     * @param from square that moved piece was situated on.
     * @param to square piece was moved to.
     * @return hash of chessboard before move was made.
     */
    public long getHashBeforeMove(long hash, ChessBoard board, ChessBoard backup, Square from, Square to) {
        hash ^= squareHashes[8 * to.getColumn() + to.getRow()][numberOfPieceAtSquare(board, to)];
        hash ^= squareHashes[8 * to.getColumn() + to.getRow()][numberOfPieceAtSquare(backup, to)];
        hash ^= squareHashes[8 * from.getColumn() + from.getRow()][0];
        hash ^= squareHashes[8 * from.getColumn() + from.getRow()][numberOfPieceAtSquare(backup, from)];
        return hash;
    }

    /**
     * Updates hash for removing piece at chosen square on given chessboard.
     *
     * @param hash old hash of given chessboard.
     * @param board chessboard on which piece is on.
     * @param location square that piece being taken is placed on.
     * @return hash after piece is taken.
     */
    public long getHashAfterPieceIsTaken(long hash, ChessBoard board, Square location) {
        hash ^= squareHashes[8 * location.getColumn() + location.getRow()][numberOfPieceAtSquare(board, location)];
        hash ^= squareHashes[8 * location.getColumn() + location.getRow()][0];
        return hash;
    }

    /**
     * Updates hash for changing class of piece at location from pawn to queen.
     *
     * @param hash old hash of given chessboard.
     * @param board chessboard on which piece is on.
     * @param location square that piece being promoted is placed on.
     * @return hash after piece is promoted.
     */
    public long getHashAfterPromotion(long hash, ChessBoard board, Square location) {
        int pieceType = numberOfPieceAtSquare(board, location);
        hash ^= squareHashes[8 * location.getColumn() + location.getRow()][pieceType];
        hash ^= squareHashes[8 * location.getColumn() + location.getRow()][pieceType + 4];
        return hash;
    }

    /**
     * Updates hash for changing class of piece at location from queen to pawn.
     *
     * @param hash old hash of given chessboard.
     * @param board chessboard on which piece is on.
     * @param location square that piece being demoted is placed on.
     * @return hash after piece is demoted.
     */
    public long getHashBeforePromotion(long hash, ChessBoard board, Square location) {
        int pieceType = numberOfPieceAtSquare(board, location);
        hash ^= squareHashes[8 * location.getColumn() + location.getRow()][pieceType];
        hash ^= squareHashes[8 * location.getColumn() + location.getRow()][pieceType - 4];
        return hash;
    }

}
