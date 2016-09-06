package chess.gui.actionlisteners;

import chess.gui.MainFrame;
import chess.gui.SideChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

/**
 *
 * @author sami
 */
public class GameVsAiStarter implements ActionListener {

    private MainFrame main;
    private SideChooser sc;
    private JRadioButton isBlack;

    public GameVsAiStarter(MainFrame main, SideChooser sc, JRadioButton isBlack) {
        this.isBlack = isBlack;
        this.main = main;
        this.sc = sc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        main.setVisible(false);
        if (isBlack.isSelected()) {
            main.getGameWindow().getGame().setWhiteAI(true);
        } else {
            main.getGameWindow().getGame().setBlackAI(true);
        }
        main.getGameWindow().setVisible(true);
        sc.dispose();
    }
}
