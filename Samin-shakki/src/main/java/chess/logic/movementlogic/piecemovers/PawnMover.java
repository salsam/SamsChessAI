package chess.logic.movementlogic.piecemovers;

import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.ChessBoard;
import java.util.Set;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;
import chess.domain.board.Player;
import java.util.HashSet;

/**
 * This class is responsible for containing all pawn-related movement logic.
 *
 * @author sami
 */
public class PawnMover extends PieceMover {

    /**
     * Creates a new PawnMover-object.
     */
    public PawnMover() {
    }

    /**
     * This method moves pawns on board to target square. If pawn moves two
     * squares that is saved to field movedTwoSquaresLastTurn and thus this pawn
     * will be en passantable on opponent's next turn. Also if movement is en
     * passant, piece in the square one step back from target will be removed.
     * En passant is spotted from target square being empty and in different
     * column as moving pawn.
     *
     * @param piece pawn to be moved.
     * @param target square that pawn is moving to.
     */
    @Override
    public void move(Piece piece, Square target, GameSituation sit) {

        piece.setHasBeenMoved(true);

        if (Math.abs(piece.getRow() - target.getRow()) == 2) {
            piece.setMovedTwoSquaresLastTurn(true);
        }

        if (!sit.getChessBoard().squareIsOccupied(target) && target.getColumn() != piece.getColumn()) {
            Square enPassanted = new Square(target.getColumn(), piece.getRow());
            sit.updateHashForTakingPiece(enPassanted);
            if (sit.getChessBoard().getPiece(enPassanted) == null) {
                System.out.println(enPassanted);
                System.out.println(piece);
                System.out.println(target);
            }
            sit.getChessBoard().getPiece(enPassanted).setTaken(true);
        }

        super.move(piece, target, sit);
        sit.refresh50MoveRule();
    }

    /**
     * This method moves pawns on board to target square. If pawn moves two
     * squares that is saved to field movedTwoSquaresLastTurn and thus this pawn
     * will be en passantable on opponent's next turn. Also if movement is en
     * passant, piece in the square one step back from target will be removed.
     * En passant is spotted from target square being empty and in different
     * column as moving pawn.
     *
     * @param move to be made.
     */
    @Override
    public void commitMove(Move move, GameSituation sit) {

        move.getPiece().setHasBeenMoved(true);

        if (Math.abs(move.getFrom().getRow() - move.getTargetRow()) == 2) {
            move.getPiece().setMovedTwoSquaresLastTurn(true);
        }

        if (!sit.getChessBoard().squareIsOccupied(move.getTarget()) && move.getTargetColumn() != move.getFrom().getColumn()) {
            Square enPassanted = new Square(move.getTargetColumn(), move.getFrom().getRow());
            sit.updateHashForTakingPiece(enPassanted);
            sit.getChessBoard().getPiece(enPassanted).setTaken(true);
        }

        super.commitMove(move, sit);
        sit.refresh50MoveRule();
    }

    /**
     * Return a list containing all squares that this pawn threatens. En passant
     * is a special move in chess which only pawns can perform. It means that if
     * opposing pawn moves two squares to be next to your own pawn, on your next
     * turn your pawn can take it as if it had only moved one square.
     *
     * @param piece chosen pawn
     * @param board board on which this pawn moves
     * @return list containing all squares this pawn threatens
     */
    @Override
    public Set<Square> threatenedSquares(Piece piece, ChessBoard board) {
        Set<Square> squares = new HashSet();
        int[] columnChange = new int[]{1, -1};
        int column = piece.getColumn();
        int row = piece.getRow() + piece.getOwner().getDirection();

        for (int i = 0; i < 2; i++) {
            if (board.withinTable(column + columnChange[i], row)) {
                Square target = new Square(column + columnChange[i], row);
                squares.add(target);
            }
        }

        addPossibleEnPassant(piece, board, squares);

        return squares;
    }

    private void addPossibleEnPassant(Piece piece, ChessBoard board, Set<Square> squares) {
        Square target;
        int[] columnChange = new int[]{1, -1};

        for (int i = 0; i < 2; i++) {
            if (board.withinTable(piece.getColumn() + columnChange[i], piece.getRow())) {
                target = new Square(piece.getColumn() + columnChange[i], piece.getRow());

                if (targetContainsAnEnemyPawn(piece.getOwner(), target, board)) {
                    Piece opposingPawn = board.getPiece(target);
                    if (opposingPawn.isMovedTwoSquaresLastTurn()) {
                        squares.add(new Square(target.getColumn(), target.getRow() + piece.getOwner().getDirection()));
                    }
                }
            }
        }
    }

    private void addEnPassant(Piece piece, ChessBoard board, Set<Move> moves) {
        Square target;
        int[] columnChange = new int[]{1, -1};

        for (int i = 0; i < 2; i++) {
            if (board.withinTable(piece.getColumn() + columnChange[i], piece.getRow())) {
                target = new Square(piece.getColumn() + columnChange[i], piece.getRow());

                if (targetContainsAnEnemyPawn(piece.getOwner(), target, board)) {
                    Piece opposingPawn = board.getPiece(target);
                    if (opposingPawn.isMovedTwoSquaresLastTurn()) {
                        moves.add(new Move(piece,
                                new Square(target.getColumn(),
                                        target.getRow() + piece.getOwner().getDirection())));
                    }
                }
            }
        }
    }

    private boolean targetContainsAnEnemyPawn(Player player, Square target, ChessBoard board) {
        if (board.getPiece(target) == null) {
            return false;
        }

        if (board.getPiece(target).isTaken()) {
            return false;
        }

        if (board.getPiece(target).getOwner() == player) {
            return false;
        }

        return board.getPiece(target).getKlass() == PAWN;
    }

    /**
     * Returns a list containing all squares this pawn can legally move to. That
     * means squares diagonally forward (to pawn's owner's direction) of this
     * pawn where is an opposing piece to be taken as well as square straight
     * forward if it doesn't contain a piece. If it's pawn's first movement,
     * then pawn can move up to two squares forward if there's no pieces of
     * either owner on the way.
     *
     * @param piece chosen pawn
     * @param board chessboard on which movement happens
     * @return a list containing all squares this pawn can legally move to.
     */
    @Override
    public Set<Square> possibleMoves(Piece piece, ChessBoard board) {
        Set<Square> moves = new HashSet<>();
        int newrow = piece.getRow() + piece.getOwner().getDirection();

        if (addSquareIfWithinTableAndEmpty(board, piece.getColumn(), newrow, moves)) {
            if (!piece.isHasBeenMoved()) {
                newrow += piece.getOwner().getDirection();
                addSquareIfWithinTableAndEmpty(board, piece.getColumn(), newrow, moves);
            }
        }

        addPossibilitiesToTakeOpposingPieces(piece, board, moves);

        return moves;
    }

    @Override
    public Set<Move> possibleMovements(Piece piece, ChessBoard board) {
        Set<Move> moves = new HashSet<>();
        int newrow = piece.getRow() + piece.getOwner().getDirection();

        if (addMoveIfWithinTableAndEmpty(board, piece, piece.getColumn(), newrow, moves)) {
            if (!piece.isHasBeenMoved()) {
                newrow += piece.getOwner().getDirection();
                addMoveIfWithinTableAndEmpty(board, piece, piece.getColumn(), newrow, moves);
            }
        }

        addMovesToTakeOpposingPieces(piece, board, moves);

        return moves;
    }

    private void addPossibilitiesToTakeOpposingPieces(Piece piece, ChessBoard board, Set<Square> moves) {
        threatenedSquares(piece, board).stream().filter(i -> legalToMoveTo(piece, i, board))
                .filter(i -> board.squareIsOccupied(i))
                .forEach(i -> moves.add(i));
        addPossibleEnPassant(piece, board, moves);
    }

    private void addMovesToTakeOpposingPieces(Piece piece, ChessBoard board, Set<Move> moves) {
        threatenedSquares(piece, board).stream().filter(i -> legalToMoveTo(piece, i, board))
                .filter(to -> board.squareIsOccupied(to))
                .forEach(to -> moves.add(new Move(piece, to)));
        addEnPassant(piece, board, moves);
    }

    private boolean addSquareIfWithinTableAndEmpty(ChessBoard board, int column, int row, Set<Square> moves) {
        if (board.withinTable(column, row)) {
            Square target = new Square(column, row);

            if (!board.squareIsOccupied(target)) {
                moves.add(target);
                return true;
            }
        }
        return false;
    }

    private boolean addMoveIfWithinTableAndEmpty(ChessBoard board, Piece piece, int column, int row, Set<Move> moves) {
        if (board.withinTable(column, row)) {
            Square target = new Square(column, row);

            if (!board.squareIsOccupied(target)) {
                moves.add(new Move(piece, target));
                return true;
            }
        }
        return false;
    }
}
