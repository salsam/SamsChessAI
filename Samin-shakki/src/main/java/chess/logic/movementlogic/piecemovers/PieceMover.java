package chess.logic.movementlogic.piecemovers;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import java.util.Set;
import chess.domain.board.Square;
import chess.domain.datastructures.MyHashSet;
import chess.domain.board.Piece;

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
    public abstract Set<Square> threatenedSquares(Piece piece, ChessBoard board);

    /**
     * Returns a list of squares given piece can legally move to.
     *
     * @param piece given piece
     * @param board ChessBoard on which given piece moves on
     * @return list containing all squares given piece can legally move to
     */
    public Set<Square> possibleMoves(Piece piece, ChessBoard board) {
        Set<Square> moves = new MyHashSet();

        threatenedSquares(piece, board).stream()
                .filter((move) -> (legalToMoveTo(piece, move, board)))
                .forEach((move) -> moves.add(move));

        return moves;
    }

    protected void addDiagonalPossibilities(Square current, ChessBoard board, Set<Square> possibilities) {
        possibilitiesToDirection(current, board, possibilities, 1, 1);
        possibilitiesToDirection(current, board, possibilities, 1, -1);
        possibilitiesToDirection(current, board, possibilities, -1, 1);
        possibilitiesToDirection(current, board, possibilities, -1, -1);
    }

    protected void addHorizontalPossibilities(Square current, ChessBoard board, Set<Square> possibilities) {
        possibilitiesToDirection(current, board, possibilities, 0, 1);
        possibilitiesToDirection(current, board, possibilities, 0, -1);
    }

    protected void addVerticalPossibilities(Square current, ChessBoard board, Set<Square> possibilities) {
        possibilitiesToDirection(current, board, possibilities, 1, 0);
        possibilitiesToDirection(current, board, possibilities, -1, 0);
    }

    protected Set<Square> possibilities(Square location, int[] columnChange, int[] rowChange, ChessBoard board) {
        Set<Square> possibilities = new MyHashSet();

        for (int i = 0; i < columnChange.length; i++) {
            int newColumn = location.getColumn() + columnChange[i];
            int newRow = location.getRow() + rowChange[i];

            if (!board.withinTable(newColumn, newRow)) {
                continue;
            }

            Square target = board.getSquare(newColumn, newRow);
            possibilities.add(target);
        }

        return possibilities;
    }

    protected boolean legalToMoveTo(Piece piece, Square target, ChessBoard board) {

        if (!target.containsAPiece()) {
            return true;
        }

        return piece.getOwner() != target.getPiece().getOwner();
    }

    /**
     * Moves this piece to target location on the given board. If this piece
     * takes an opposing piece, that will be removed from its owner on board.
     *
     * @param piece piece to be moved.
     * @param target Square where this piece will be moved.
     * @param sit situation being changed.
     */
    public void move(Piece piece, Square target, GameSituation sit) {

        Square from = sit.getChessBoard().getSquare(piece.getColumn(), piece.getRow());
        sit.updateHashForMoving(from, target);

        from.setPiece(null);
        if (target.containsAPiece()) {
            target.getPiece().setTaken(true);
        }
        target.setPiece(piece);

        piece.setColumn(target.getColumn());
        piece.setRow(target.getRow());
    }

    private void possibilitiesToDirection(Square current, ChessBoard board, Set<Square> possibilities, int columnChange, int rowChange) {
        int newColumn = current.getColumn() + columnChange;
        int newRow = current.getRow() + rowChange;

        while (board.withinTable(newColumn, newRow)) {
            Square target = board.getSquare(newColumn, newRow);
            possibilities.add(target);

            if (target.containsAPiece()) {
                break;
            }
            newColumn = target.getColumn() + columnChange;
            newRow = target.getRow() + rowChange;
        }
    }
}
