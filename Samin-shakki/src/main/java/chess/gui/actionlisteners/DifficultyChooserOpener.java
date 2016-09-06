package chess.gui.actionlisteners;

import chess.gui.AiVsAiDifficultyChooser;
import chess.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author sami
 */
public class DifficultyChooserOpener implements ActionListener {

    private MainFrame main;

    public DifficultyChooserOpener(MainFrame main) {
        this.main = main;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        main.setVisible(false);
        AiVsAiDifficultyChooser adc = new AiVsAiDifficultyChooser(main);
        adc.setVisible(true);
    }

}
