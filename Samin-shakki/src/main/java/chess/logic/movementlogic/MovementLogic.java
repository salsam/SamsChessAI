package chess.logic.movementlogic;

import chess.domain.Coordinates;
import chess.domain.board.Piece;
import chess.domain.GameSituation;
import chess.domain.Move;
import chess.logic.movementlogic.piecemovers.*;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import static chess.domain.board.Klass.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for movement related logic. Class offers methods to
 * get possible moves and threatened squares of a Piece or Player on ChessBoard
 * given as parameter. Also offers method to move piece to target location on
 * chessboard.
 *
 * @author sami
 */
public class MovementLogic {

    /**
     * Object used to move Bishops on ChessBoard.
     */
    private BishopMover bishopMover;
    /**
     * Object used to move Kings on ChessBoard.
     */
    private KingMover kingMover;
    /**
     * Object used to move Knights on ChessBoard.
     */
    private KnightMover knightMover;
    /**
     * Object used to move Pawns on ChessBoard.
     */
    private PawnMover pawnMover;
    /**
     * Object used to move Queens on ChessBoard.
     */
    private QueenMover queenMover;
    /**
     * Object used to move Rooks on ChessBoard.
     */
    private RookMover rookMover;

    /**
     * Creates a new MovementLogic initializing mover-objects for each chess
     * piece-class.
     */
    public MovementLogic() {
        bishopMover = new BishopMover();
        kingMover = new KingMover();
        knightMover = new KnightMover();
        pawnMover = new PawnMover();
        queenMover = new QueenMover();
        rookMover = new RookMover();
    }

    /**
     * Uses corresponding mover to returns a set containing all squares given
     * piece threatens on given board.
     *
     * @param piece piece of which threatened squares are being checked
     * @param board board on which piece is placed
     * @return a set containing all squares given piece threatens on given board
     */
    public Set<Coordinates> threatenedSquares(Piece piece, ChessBoard board) {
        switch (piece.getKlass()) {
            case BISHOP:
                return bishopMover.threatenedSquares(piece, board);
            case KING:
                return kingMover.threatenedSquares(piece, board);
            case KNIGHT:
                return knightMover.threatenedSquares(piece, board);
            case PAWN:
                return pawnMover.threatenedSquares(piece, board);
            case QUEEN:
                return queenMover.threatenedSquares(piece, board);
            case ROOK:
                return rookMover.threatenedSquares(piece, board);
            default:
                break;
        }
        return new HashSet<>();
    }

    /**
     * Uses corresponding mover to return a set containing all squares given
     * piece can move to on given board.
     *
     * @param piece piece of which possible moves are being checked
     * @param board board on which piece is placed
     * @return a set containing all squares given piece can move to on given
     * board
     */
    public Set<Coordinates> possibleMoves(Piece piece, ChessBoard board) {
        switch (piece.getKlass()) {
            case BISHOP:
                return bishopMover.possibleMoves(piece, board);
            case KING:
                return kingMover.possibleMoves(piece, board);
            case KNIGHT:
                return knightMover.possibleMoves(piece, board);
            case PAWN:
                return pawnMover.possibleMoves(piece, board);
            case QUEEN:
                return queenMover.possibleMoves(piece, board);
            case ROOK:
                return rookMover.possibleMoves(piece, board);
            default:
                break;
        }
        return new HashSet<>();
    }

    /**
     * Uses corresponding mover to return a set containing all movements
     * possible.
     *
     * @param piece piece of which possible moves are being checked
     * @param board board on which piece is placed
     * @return a set containing all squares given piece can move to on given
     * board
     */
    public Set<Move> possibleMovements(Piece piece, ChessBoard board) {
        switch (piece.getKlass()) {
            case BISHOP:
                return bishopMover.possibleMovements(piece, board);
            case KING:
                return kingMover.possibleMovements(piece, board);
            case KNIGHT:
                return knightMover.possibleMovements(piece, board);
            case PAWN:
                return pawnMover.possibleMovements(piece, board);
            case QUEEN:
                return queenMover.possibleMovements(piece, board);
            case ROOK:
                return rookMover.possibleMovements(piece, board);
            default:
                break;
        }
        return new HashSet<>();
    }

    /**
     * Uses corresponding PieceMover to move given piece to target square on
     * given board.
     *
     * @param piece piece to be moved.
     * @param target square where piece will be moved to.
     * @param sit situation before move.
     */
    public void move(Piece piece, Coordinates target, GameSituation sit) {

        switch (piece.getKlass()) {
            case BISHOP:
                bishopMover.move(piece, target, sit);
                break;
            case KING:
                kingMover.move(piece, target, sit);
                break;
            case KNIGHT:
                knightMover.move(piece, target, sit);
                break;
            case PAWN:
                pawnMover.move(piece, target, sit);
                break;
            case QUEEN:
                queenMover.move(piece, target, sit);
                break;
            case ROOK:
                rookMover.move(piece, target, sit);
                break;
        }

        sit.incrementCountOfCurrentBoardSituation();
    }

    /*
    *Commit selected move by moving specified piece from selected square to target square.
     */
    public void commitMove(Move move, GameSituation sit) {
        switch (move.getPiece().getKlass()) {
            case BISHOP:
                bishopMover.commitMove(move, sit);
                break;
            case KING:
                kingMover.commitMove(move, sit);
                break;
            case KNIGHT:
                knightMover.commitMove(move, sit);
                break;
            case PAWN:
                pawnMover.commitMove(move, sit);
                break;
            case QUEEN:
                queenMover.commitMove(move, sit);
                break;
            case ROOK:
                rookMover.commitMove(move, sit);
                break;
        }

        sit.incrementCountOfCurrentBoardSituation();
    }

    /**
     * Returns set containing all squares that given player's pieces threaten on
     * given board.
     *
     * @param player given player
     * @param board given chessboard
     * @return set containing all squares that given player's pieces threaten
     */
    public Set<Coordinates> squaresThreatenedByPlayer(Player player, ChessBoard board) {
        Set<Coordinates> threatenedSquares = new HashSet();
        board.getPieces(player).stream()
                .filter(owned -> !owned.isTaken())
                .forEach(piece -> {
                    threatenedSquares.addAll(threatenedSquares(piece, board));
                });
        return threatenedSquares;
    }

    /**
     * Returns set containing all squares that given player's pieces can move to
     * on given board.
     *
     * @param player given player
     * @param board given chessboard
     * @return set containing all squares that given player's pieces can move to
     */
    public Set<Coordinates> possibleMovesByPlayer(Player player, ChessBoard board) {
        Set<Coordinates> possibleMoves = new HashSet();
        board.getPieces(player).stream()
                .filter(owned -> !owned.isTaken())
                .forEach(piece -> {
                    possibleMoves.addAll(possibleMoves(piece, board));
                });
        return possibleMoves;
    }

    /**
     * Returns set containing all movements that given player can commit.
     *
     * @param player given player
     * @param board given chessboard
     * @return set containing all squares that given player's pieces can move to
     */
    public Set<Move> possibleMovementsByPlayer(Player player, ChessBoard board) {
        Set<Move> possibleMovements = new HashSet();
        board.getPieces(player).stream()
                .filter(owned -> !owned.isTaken())
                .forEach(piece -> {
                    possibleMovements.addAll(possibleMovements(piece, board));
                });
        return possibleMovements;
    }

}
