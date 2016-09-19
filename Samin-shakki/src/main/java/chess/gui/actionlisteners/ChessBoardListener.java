package chess.gui.actionlisteners;

import chess.gui.boarddrawing.ChessBoardDrawer;
import chess.logic.inputprocessing.InputProcessor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author sami
 */
public class ChessBoardListener implements MouseListener {

    private ChessBoardDrawer board;
    private int sideLength;
    private InputProcessor logic;

    public ChessBoardListener(InputProcessor guiLogic, ChessBoardDrawer board, int sideLength) {
        this.board = board;
        this.logic = guiLogic;
        this.sideLength = sideLength;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int column = (e.getX()) / sideLength;
        int row = (e.getY()) / sideLength;
        logic.processClick(column, row, board.getGame());
        board.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
