package chess.gui.actionlisteners;

import chess.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author sami
 */
public class DifficultySetter implements ActionListener {

    private MainFrame mf;
    private JRadioButton isBlack;
    private JTextField text;

    public DifficultySetter(MainFrame mf, JTextField text, JRadioButton isBlack) {
        this.isBlack = isBlack;
        this.mf = mf;
        this.text = text;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        long timeLimit;
        try {
            timeLimit = Long.parseLong(text.getText());
            if (timeLimit >= 50) {
                if (isBlack.isSelected()) {
                    mf.getGameWindow().getInputProcessor().setAiDifficulty(1, timeLimit);
                } else {
                    mf.getGameWindow().getInputProcessor().setAiDifficulty(0, timeLimit);
                }
            }
        } catch (Exception e) {
        }
    }

}
