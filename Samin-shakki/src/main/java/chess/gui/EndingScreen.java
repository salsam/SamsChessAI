package chess.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class EndingScreen extends JFrame {

    public EndingScreen(Controller controller) throws HeadlessException {
        this.setPreferredSize(new Dimension(300, 300));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents(this.getContentPane(), controller);
        this.pack();
        this.setVisible(false);
    }

    private void initComponents(Container container, Controller cont) {
        JButton playAgain = new JButton("Play again");
        playAgain.setActionCommand("RESTART");
        playAgain.addActionListener(cont);
        playAgain.setMaximumSize(new Dimension(300, 150));

        JButton exit = new JButton("Exit");
        exit.setActionCommand("EXIT");
        exit.addActionListener(cont);
        exit.setMaximumSize(new Dimension(300, 150));

        container.setPreferredSize(new Dimension(300, 300));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(playAgain);
        container.add(exit);
    }

}
