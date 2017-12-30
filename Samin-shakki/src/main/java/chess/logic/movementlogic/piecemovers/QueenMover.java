/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template column, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.logic.movementlogic.piecemovers;

import chess.domain.board.ChessBoard;
import java.util.Set;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import java.util.HashSet;

/**
 * This class is responsible for all queen-related movement logic.
 *
 * @author sami
 */
public class QueenMover extends PieceMover {

    /**
     * Creates a new QueenMover-object.
     */
    public QueenMover() {
    }

    /**
     * Return a list containing all squares that this queen threatens.
     *
     * @param board board on which this queen moves
     * @return list containing all squares this queen threatens
     */
    @Override
    public Set<Square> threatenedSquares(Piece piece, ChessBoard board) {
        Set<Square> possibilities = new HashSet<>();
        addDiagonalPossibilities(piece.getLocation(), board, possibilities);
        addHorizontalPossibilities(piece.getLocation(), board, possibilities);
        addVerticalPossibilities(piece.getLocation(), board, possibilities);

        return possibilities;
    }
}
