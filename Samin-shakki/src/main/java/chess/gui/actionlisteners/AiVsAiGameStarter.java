package chess.gui.actionlisteners;

import chess.gui.AiVsAiDifficultyChooser;
import chess.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author sami
 */
public class AiVsAiGameStarter implements ActionListener {

    private MainFrame main;
    private AiVsAiDifficultyChooser adc;

    public AiVsAiGameStarter(MainFrame main, AiVsAiDifficultyChooser adc) {
        this.adc = adc;
        this.main = main;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        main.setVisible(false);
        main.getGameWindow().getGame().setBlackAI(true);
        main.getGameWindow().getGame().setWhiteAI(true);
        main.getGameWindow().setVisible(true);
        adc.dispose();
    }
}
