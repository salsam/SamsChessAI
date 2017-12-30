package chess.gui.boarddrawing;

import chess.domain.Game;
import chess.domain.GameSituation;
import chess.domain.board.Square;
import chess.logic.inputprocessing.InputProcessor;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author sami
 */
public class ChessBoardDrawer extends JPanel {

    private Game game;
    private InputProcessor guiLogic;
    private int sideLength;
    private PieceDrawer pieceDrawer;

    public ChessBoardDrawer(InputProcessor guiLogic, Game game, int sideLength) {
        this.game = game;
        this.guiLogic = guiLogic;
        this.sideLength = sideLength;
        this.pieceDrawer = new PieceDrawer();
        super.setBackground(Color.CYAN);
    }

    public GameSituation getGame() {
        return game.getSituation();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);

        for (int i = 0; i < game.getSituation().getChessBoard().columnAmount; i++) {
            for (int j = 0; j < game.getSituation().getChessBoard().rowAmount; j++) {
                if (guiLogic.getPossibilities() != null && guiLogic.getPossibilities().contains(new Square(i, j))) {
                    graphics.setColor(Color.red);
                } else if (game.lastMove() != null && (game.lastMove().getFrom().equals(new Square(i, j))
                        || game.lastMove().getTarget().equals(new Square(i, j)))) {
                    graphics.setColor(Color.blue);
                } else if ((i + j) % 2 == 0) {
                    graphics.setColor(Color.LIGHT_GRAY);
                } else {
                    graphics.setColor(Color.DARK_GRAY);
                }
                graphics.fillRect(sideLength * i + 1, sideLength * j + 1, sideLength - 1, sideLength - 1);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(sideLength * i, sideLength * j, sideLength, sideLength);

                if (game.getSituation().getChessBoard().squareIsOccupied(i, j)) {
                    pieceDrawer.draw(game.getSituation().getChessBoard().getPiece(i, j), graphics, sideLength);
                }
            }
        }
    }

}
