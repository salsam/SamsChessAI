package chess.logic.movementlogic.piecemovers;

import chess.domain.Coordinates;
import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.ChessBoard;
import java.util.Set;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import java.util.HashSet;

public abstract class PieceMover {

    /**
     * Returns set containing all squares given piece threatens on given
     * chessboard.
     *
     * @param piece given piece
     * @param board given board
     * @return set containing containing all squares given piece threatens on
     * given chessboard
     */
    public abstract Set<Coordinates> threatenedSquares(Piece piece, ChessBoard board);

    /**
     * Returns a set of squares given piece can legally move to.
     *
     * @param piece given piece
     * @param board ChessBoard on which given piece moves on
     * @return list containing all squares given piece can legally move to
     */
    public Set<Coordinates> possibleMoves(Piece piece, ChessBoard board) {
        Set<Coordinates> moves = new HashSet();

        threatenedSquares(piece, board).stream()
                .filter((targetSquare) -> (legalToMoveTo(piece, targetSquare, board)))
                .forEach((targetSquare) -> moves.add(targetSquare));

        return moves;
    }
    /**
     * Returns set of moves chosen piece can make on board.
     * @param piece to be moved.
     * @param board
     * @return set of moves chosen piece can make on board.
     */
    public Set<Move> possibleMovements(Piece piece, ChessBoard board) {
        Set<Move> moves = new HashSet();

        threatenedSquares(piece, board).stream()
                .filter((targetSquare) -> (legalToMoveTo(piece, targetSquare, board)))
                .forEach((targetSquare) -> moves.add(new Move(piece, targetSquare, board)));

        return moves;
    }

    protected void addDiagonalPossibilities(Coordinates current, ChessBoard board, Set<Coordinates> possibilities) {
        possibilitiesToDirection(current, board, possibilities, 1, 1);
        possibilitiesToDirection(current, board, possibilities, 1, -1);
        possibilitiesToDirection(current, board, possibilities, -1, 1);
        possibilitiesToDirection(current, board, possibilities, -1, -1);
    }

    protected void addHorizontalPossibilities(Coordinates current, ChessBoard board, Set<Coordinates> possibilities) {
        possibilitiesToDirection(current, board, possibilities, 0, 1);
        possibilitiesToDirection(current, board, possibilities, 0, -1);
    }

    protected void addVerticalPossibilities(Coordinates current, ChessBoard board, Set<Coordinates> possibilities) {
        possibilitiesToDirection(current, board, possibilities, 1, 0);
        possibilitiesToDirection(current, board, possibilities, -1, 0);
    }

    protected Set<Coordinates> possibilities(Coordinates location, int[] columnChange, int[] rowChange, ChessBoard board) {
        Set<Coordinates> possibilities = new HashSet();

        for (int i = 0; i < columnChange.length; i++) {
            int newColumn = location.getColumn() + columnChange[i];
            int newRow = location.getRow() + rowChange[i];

            if (!board.withinTable(newColumn, newRow)) {
                continue;
            }

            Coordinates target = board.getSquare(newColumn, newRow).getLocation();
            possibilities.add(target);
        }

        return possibilities;
    }

    protected boolean legalToMoveTo(Piece piece, Coordinates target, ChessBoard board) {
        if (!board.getSquare(target).containsAPiece()) {
            return true;
        }
        return piece.getOwner() != board.getSquare(target).getPiece().getOwner();
    }

    /**
     * Moves this piece to target location on the given board. If this piece
     * takes an opposing piece, that will be removed from its owner on board.
     *
     * @param piece piece to be moved.
     * @param target Square where this piece will be moved.
     * @param sit situation being changed.
     */
    public void move(Piece piece, Coordinates target, GameSituation sit) {
        Square from = sit.getChessBoard().getSquare(piece.getColumn(), piece.getRow());
        Square to=sit.getChessBoard().getSquare(target);
        //sit.updateHashForMoving(from, target);
        sit.decrementMovesTillDraw();

        from.setPiece(null);
        if (to.containsAPiece()) {
            sit.refresh50MoveRule();
            to.getPiece().setTaken(true);
        }
        to.setPiece(piece);

        piece.setColumn(target.getColumn());
        piece.setRow(target.getRow());
    }
    
    /**
     * Commits selected move. If this piece
     * takes an opposing piece, that will be removed from its owner on board.
     *
     * BreadAndButter movement without any class specific actions.
     * 
     * @param move to be made.
     * @param sit situation being changed.
     */
    public void commitMove(Move move, GameSituation sit) {
        Coordinates from = move.getFrom();
        Coordinates target = move.getTarget();
        //sit.updateHashForMoving(from, move.getTarget());
        sit.decrementMovesTillDraw();

        sit.getChessBoard().getSquare(from).setPiece(null);
        if (sit.getChessBoard().getSquare(target).containsAPiece()) {
            sit.refresh50MoveRule();
            sit.getChessBoard().getSquare(target).getPiece().setTaken(true);
        }
        sit.getChessBoard().getSquare(target).setPiece(move.getPiece());

        move.getPiece().setColumn(move.getTargetColumn());
        move.getPiece().setRow(move.getTargetRow());
    }

    private void possibilitiesToDirection(Coordinates current, ChessBoard board, Set<Coordinates> possibilities, int columnChange, int rowChange) {
        int newColumn = current.getColumn() + columnChange;
        int newRow = current.getRow() + rowChange;

        while (board.withinTable(newColumn, newRow)) {
            Coordinates target = board.getSquare(newColumn, newRow).getLocation();
            possibilities.add(target);

            if (board.getSquare(target).containsAPiece()) {
                break;
            }
            newColumn = target.getColumn() + columnChange;
            newRow = target.getRow() + rowChange;
        }
    }
}
