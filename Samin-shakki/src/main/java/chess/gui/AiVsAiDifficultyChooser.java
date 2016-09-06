package chess.gui;

import chess.gui.actionlisteners.AiVsAiGameStarter;
import chess.gui.actionlisteners.DifficultySetter;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class AiVsAiDifficultyChooser extends JFrame {

    public AiVsAiDifficultyChooser(MainFrame main) throws HeadlessException {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(330, 330));
        initComponents(this.getContentPane(), main);
        this.pack();
        this.setVisible(false);
    }

    private void initComponents(Container cont, MainFrame main) {
        JPanel topPanel = topPanel(main);
        JButton start = new JButton("Start game");
        start.setAlignmentX(CENTER_ALIGNMENT);
        start.addActionListener(new AiVsAiGameStarter(main, this));
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.add(topPanel);
        cont.add(start);

    }

    private JPanel topPanel(MainFrame main) {
        JPanel ret = new JPanel(new GridLayout(2, 0, 20, 20));
        ret.setPreferredSize(new Dimension(600, 150));
        JPanel whiteSide = createAiDifficultyChooser(main, "white");
        JPanel blackSide = createAiDifficultyChooser(main, "black");
        ret.add(blackSide);
        ret.add(whiteSide);

        return ret;
    }

    private JPanel createAiDifficultyChooser(MainFrame main, String side) {
        JPanel adc = new JPanel();
        adc.setPreferredSize(new Dimension(300, 150));
        JTextArea text = new JTextArea("Write how long (in milliseconds) "
                + "can " + side + " ai\nthink each turn and press change difficulty\nto "
                + "adjust ai's difficulty upwards from 50.");
        text.setEditable(false);
        text.setMaximumSize(new Dimension(300, 100));
        JTextField tf = new JTextField("1000");
        tf.setMaximumSize(new Dimension(50, 50));
        tf.setAlignmentX(CENTER_ALIGNMENT);
        JButton ok = new JButton("Change difficulty");
        ok.setAlignmentX(CENTER_ALIGNMENT);
        JRadioButton isBlack = new JRadioButton();
        if (side.equals("white")) {
            isBlack.setSelected(true);
        }
        ok.addActionListener(new DifficultySetter(main, tf, isBlack));
        this.add(text);
        this.add(tf);
        this.add(ok);
        return adc;
    }

}
