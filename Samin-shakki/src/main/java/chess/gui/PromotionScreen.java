package chess.gui;

import chess.domain.GameSituation;
import static chess.domain.board.Klass.BISHOP;
import static chess.domain.board.Klass.KNIGHT;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.domain.board.Piece;
import static chess.logic.gamelogic.PromotionLogic.promote;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class PromotionScreen extends JFrame {

    public PromotionScreen(GameSituation sit, Piece promotee) {
        this.setPreferredSize(new Dimension(450, 300));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents(this.getContentPane(), sit, promotee);
        this.pack();
        this.setVisible(true);
        this.setAlwaysOnTop(true);
    }

    private void initComponents(Container cont, GameSituation sit, Piece promotee) {
        JLabel label = new JLabel("Which class would you like to promote your pawn?");
        JButton bishop = new JButton("Bishop");
        bishop.setPreferredSize(new Dimension(50, 50));
        JButton knight = new JButton("Knight");
        knight.setPreferredSize(new Dimension(50, 50));
        JButton queen = new JButton("Queen");
        queen.setPreferredSize(new Dimension(50, 50));
        JButton rook = new JButton("Rook");
        rook.setPreferredSize(new Dimension(50, 50));
        PromotionScreen pr = this;

        bishop.addActionListener((ActionEvent ae) -> {
            promote(sit, promotee, BISHOP);
            pr.dispose();
        });

        knight.addActionListener((ActionEvent ae) -> {
            promote(sit, promotee, KNIGHT);
            pr.dispose();
        });

        queen.addActionListener((ActionEvent ae) -> {
            promote(sit, promotee, QUEEN);
            pr.dispose();
        });

        rook.addActionListener((ActionEvent ae) -> {
            promote(sit, promotee, ROOK);
            pr.dispose();
        });

        cont.add(label);
        cont.add(bishop);
        cont.add(knight);
        cont.add(queen);
        cont.add(rook);

    }
}
