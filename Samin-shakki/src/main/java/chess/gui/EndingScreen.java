package chess.gui;

import chess.gui.actionlisteners.WindowCloser;
import chess.gui.actionlisteners.GameRestarter;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class EndingScreen extends JFrame {

    public EndingScreen(Map<String, JFrame> frames) throws HeadlessException {
        this.setPreferredSize(new Dimension(300, 300));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents(this.getContentPane(), frames);
        this.pack();
        this.setVisible(false);
    }

    private void initComponents(Container container, Map<String, JFrame> frames) {
        JButton playAgain = new JButton("Play again");
        playAgain.addActionListener(new GameRestarter(frames));
        playAgain.setMaximumSize(new Dimension(300, 150));

        JButton exit = new JButton("Exit");
        exit.addMouseListener(new WindowCloser(frames));
        exit.setMaximumSize(new Dimension(300, 150));

        container.setPreferredSize(new Dimension(300, 300));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(playAgain);
        container.add(exit);
    }

}
