/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template column, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.logic.movementlogic.piecemovers;

import chess.domain.Coordinates;
import chess.domain.board.ChessBoard;
import java.util.Set;
import chess.domain.board.Piece;
import java.util.HashSet;

/**
 * This class is responsible of movement calculation of bishops.
 *
 * @author sami
 */
public class BishopMover extends PieceMover {

    /**
     * Creates a new BishopMover-object that contains bishop's movement related
     * logic.
     */
    public BishopMover() {
    }

    /**
     * Return a list containing all squares that this bishop threatens.
     *
     * @param bishop chosen bishop
     * @param board board where this bishop moves
     * @return list containing all squares this bishop threatens
     */
    @Override
    public Set<Coordinates> threatenedSquares(Piece bishop, ChessBoard board) {
        Set<Coordinates> possibilities = new HashSet<>();
        addDiagonalPossibilities(bishop.getLocation(), board, possibilities);
        return possibilities;
    }

}
