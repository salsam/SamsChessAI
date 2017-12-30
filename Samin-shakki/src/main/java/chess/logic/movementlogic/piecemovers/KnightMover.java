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

/**
 * This class is responsible for all knight-related movement logic.
 *
 * @author sami
 */
public class KnightMover extends PieceMover {

    /**
     * Creates a new KnightMover-object.
     */
    public KnightMover() {
    }

    /**
     * Return a list containing all squares that given knight threatens.
     *
     * @param piece knight
     * @param board board where this knight moves
     * @return list containing all squares given knight threatens
     */
    @Override
    public Set<Square> threatenedSquares(Piece piece, ChessBoard board) {
        int[] rowChange = new int[]{-2, -2, -1, -1, 1, 1, 2, 2};
        int[] columnChange = new int[]{1, -1, 2, -2, 2, -2, 1, -1};

        return possibilities(board.getSquare(piece.getColumn(), piece.getRow()), columnChange, rowChange, board);
    }
}
