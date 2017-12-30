package chess.gui.boarddrawing;

import chess.domain.Game;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author sami
 */
public class ChessBoardDrawer extends JPanel {
    
    private Game game;
    private int sideLength;
    private PieceDrawer pieceDrawer;
    
    public ChessBoardDrawer(Game game, int sideLength) {
        this.game = game;
        this.sideLength = sideLength;
        this.pieceDrawer = new PieceDrawer();
        super.setBackground(Color.CYAN);
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);
        
        for (int i = 0; i < game.getSituation().getChessBoard().getTable().length; i++) {
            for (int j = 0; j < game.getSituation().getChessBoard().getTable()[0].length; j++) {
                if (game.getInput().getPossibilities() != null
                        && game.getInput().getPossibilities().contains(game.getSquare(i, j))) {
                    graphics.setColor(Color.red);
                } else if (game.getLastMove() != null && ((game.getLastMove().getFrom().getColumn() == i && game.getLastMove().getFrom().getRow() == j)
                        || (game.getLastMove().getTargetColumn() == i && game.getLastMove().getTargetRow() == j))) {
                    graphics.setColor(Color.blue);
                } else if ((i + j) % 2 == 0) {
                    graphics.setColor(Color.LIGHT_GRAY);
                } else {
                    graphics.setColor(Color.DARK_GRAY);
                }
                graphics.fillRect(sideLength * i + 1, sideLength * j + 1, sideLength - 1, sideLength - 1);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(sideLength * i, sideLength * j, sideLength, sideLength);
                
                if (game.getSquare(i, j).containsAPiece()) {
                    pieceDrawer.draw(game.getSquare(i, j).getPiece(), graphics, sideLength);
                }
            }
        }
    }
    
}
