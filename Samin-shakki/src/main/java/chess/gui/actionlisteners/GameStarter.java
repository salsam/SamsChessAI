package chess.gui.actionlisteners;

import chess.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author sami
 */
public class GameStarter implements ActionListener {

    private MainFrame main;

    public GameStarter(MainFrame main) {
        this.main = main;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        main.setVisible(false);
        main.getGameWindow().setVisible(true);
    }

}
