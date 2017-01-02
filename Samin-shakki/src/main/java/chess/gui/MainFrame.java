package chess.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class MainFrame extends JFrame {

    private GameWindow gameWindow;

    public MainFrame(JFrame gameWindow, Controller cont) {

        this.gameWindow = (GameWindow) gameWindow;
        this.setPreferredSize(new Dimension(450, 500));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents(this.getContentPane(), cont);
        this.pack();
        this.setVisible(false);

    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    private void initComponents(Container container, Controller cont) {
        JLabel head = new JLabel("Chess");
        head.setFont(new Font("Serif", Font.BOLD, 50));
        head.setMaximumSize(new Dimension(250, 100));
        head.setAlignmentX(CENTER_ALIGNMENT);
        head.setAlignmentY(TOP_ALIGNMENT);

        JButton start = new JButton("New game");
        start.setMaximumSize(new Dimension(250, 200));
        start.setAlignmentX(CENTER_ALIGNMENT);
        start.setActionCommand("PVP");
        start.addActionListener(cont);

        JButton startVsAi = new JButton("New game vs AI");
        startVsAi.setMaximumSize(new Dimension(250, 200));
        startVsAi.setAlignmentX(CENTER_ALIGNMENT);
        startVsAi.setActionCommand("PVAI");
        startVsAi.addActionListener(cont);

        JButton startAiVsAi = new JButton("New AI vs AI game");
        startAiVsAi.setMaximumSize(new Dimension(250, 200));
        startAiVsAi.setAlignmentX(CENTER_ALIGNMENT);
        startAiVsAi.setActionCommand("AIVAI");
        startAiVsAi.addActionListener(cont);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(head);
        container.add(start);
        container.add(startVsAi);
        container.add(startAiVsAi);
    }

}
