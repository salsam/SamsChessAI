package chess.gui;

import chess.domain.Game;
import static chess.domain.board.Klass.BISHOP;
import static chess.domain.board.Klass.KNIGHT;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.domain.board.Piece;
import static chess.logic.gamelogic.PromotionLogic.promote;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class PromotionPopup extends JDialog {

    public PromotionPopup(Game game, Piece promotee) {
        this.setPreferredSize(new Dimension(400, 300));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initComponents(this.getContentPane(), game, promotee);
        this.pack();
        this.setVisible(true);
        this.setAlwaysOnTop(true);
    }

    private void initComponents(Container cont, Game game, Piece promotee) {
        JLabel label = new JLabel("Which class would you like to promote your pawn?");
        JButton bishop = new JButton("Bishop");
        bishop.setPreferredSize(new Dimension(250, 100));
        JButton knight = new JButton("Knight");
        knight.setPreferredSize(new Dimension(250, 50));
        JButton queen = new JButton("Queen");
        queen.setPreferredSize(new Dimension(250, 50));
        JButton rook = new JButton("Rook");
        rook.setPreferredSize(new Dimension(250, 50));
        PromotionPopup pr = this;

        bishop.addActionListener((ActionEvent ae) -> {
            promote(game.getSituation(), promotee, BISHOP);
            game.setContinues(true);
            pr.dispose();
        });

        knight.addActionListener((ActionEvent ae) -> {
            promote(game.getSituation(), promotee, KNIGHT);
            game.setContinues(true);
            pr.dispose();
        });

        queen.addActionListener((ActionEvent ae) -> {
            promote(game.getSituation(), promotee, QUEEN);
            game.setContinues(true);
            pr.dispose();
        });

        rook.addActionListener((ActionEvent ae) -> {
            promote(game.getSituation(), promotee, ROOK);
            game.setContinues(true);
            pr.dispose();
        });

        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.add(label);
        cont.add(bishop);
        cont.add(knight);
        cont.add(queen);
        cont.add(rook);
    }
}
