/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template column, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.logic.movementlogic.piecemovers;

import chess.domain.Coordinates;
import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.ChessBoard;
import java.util.Set;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import java.util.HashSet;

/**
 * This class is responsible for all rook-related movement logic.
 *
 * @author sami
 */
public class RookMover extends PieceMover {

    /**
     * Creates a new RookMover-object.
     */
    public RookMover() {
    }

    /**
     * This method moves rook on the board and saves true to field hasBeenMoved.
     *
     * @param target square this rook is moving to.
     */
    @Override
    public void move(Piece piece, Coordinates target, GameSituation sit) {
        piece.setHasBeenMoved(true);
        super.move(piece, target, sit);
    }
    
    /**
     * Moves rook on the board and saves true to field hasBeenMoved.
     *
     * @param move to be made.
     */
    @Override
    public void commitMove(Move move, GameSituation sit) {
        move.getPiece().setHasBeenMoved(true);
        super.commitMove(move, sit);
    }

    /**
     * Return a list containing all squares that this rook threatens.
     *
     * @param board board on which this rook moves
     * @return list containing all squares this rook threatens
     */
    @Override
    public Set<Coordinates> threatenedSquares(Piece piece, ChessBoard board) {
        Set<Coordinates> possibilities = new HashSet<>();
        addHorizontalPossibilities(board.getSquare(piece.getColumn(), piece.getRow()).getLocation(), board, possibilities);
        addVerticalPossibilities(board.getSquare(piece.getColumn(), piece.getRow()).getLocation(), board, possibilities);

        return possibilities;
    }
}
