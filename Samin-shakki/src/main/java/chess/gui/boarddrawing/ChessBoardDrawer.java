package chess.gui.boarddrawing;

import chess.domain.GameSituation;
import chess.logic.inputprocessing.InputProcessor;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author sami
 */
public class ChessBoardDrawer extends JPanel {

    private GameSituation game;
    private InputProcessor guiLogic;
    private int sideLength;
    private PieceDrawer pieceDrawer;

    public ChessBoardDrawer(InputProcessor guiLogic, GameSituation game, int sideLength) {
        this.game = game;
        this.guiLogic = guiLogic;
        this.sideLength = sideLength;
        this.pieceDrawer = new PieceDrawer();
        super.setBackground(Color.CYAN);
    }

    public GameSituation getGame() {
        return game;
    }

    public void setGame(GameSituation game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);

        for (int i = 0; i < game.getChessBoard().getTable().length; i++) {
            for (int j = 0; j < game.getChessBoard().getTable()[0].length; j++) {
                if (guiLogic.getPossibilities() != null && guiLogic.getPossibilities().contains(game.getChessBoard().getSquare(i, j))) {
                    graphics.setColor(Color.red);
                } else if ((i + j) % 2 == 0) {
                    graphics.setColor(Color.LIGHT_GRAY);
                } else {
                    graphics.setColor(Color.DARK_GRAY);
                }
                graphics.fillRect(sideLength * i + 1, sideLength * j + 1, sideLength - 1, sideLength - 1);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(sideLength * i, sideLength * j, sideLength, sideLength);

                if (game.getChessBoard().getSquare(i, j).containsAPiece()) {
                    pieceDrawer.draw(game.getChessBoard().getSquare(i, j).getPiece(), graphics, sideLength);
                }
            }
        }
    }

}
