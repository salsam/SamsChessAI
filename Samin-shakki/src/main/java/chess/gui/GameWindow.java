package chess.gui;

import chess.domain.Game;
import chess.gui.actionlisteners.ChessBoardListener;
import chess.gui.boarddrawing.ChessBoardDrawer;
import chess.logic.inputprocessing.InputProcessor;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author sami
 */
public class GameWindow extends JFrame {

    private Game game;
    private ChessBoardDrawer cbd;
    private JLabel textArea;

    public GameWindow(Game game) {
        this.game = game;
        this.setPreferredSize(new Dimension(450, 550));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createComponents(this.getContentPane());
        this.pack();
        this.setVisible(false);
    }

    public Game getGame() {
        return game;
    }

    public InputProcessor getInputProcessor() {
        return game.getInput();
    }

    private void createComponents(Container container) {
        textArea = new JLabel(game.getSituation().whoseTurn() + "'s turn.");
        textArea.setFont(new Font("Serif", Font.PLAIN, 20));
        textArea.setMaximumSize(new Dimension(300, 100));
        textArea.setAlignmentX(CENTER_ALIGNMENT);
        textArea.setAlignmentY(TOP_ALIGNMENT);
        game.getInput().setTextArea(textArea);
        
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if (ke.getKeyChar()=='r') {
                    game.restart();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
            
        };

        cbd = new ChessBoardDrawer(game.getInput(), game, 50);
        cbd.setMaximumSize(new Dimension(400, 400));
        cbd.setAlignmentX(CENTER_ALIGNMENT);
        cbd.setAlignmentY(CENTER_ALIGNMENT);
        cbd.addMouseListener(new ChessBoardListener(game.getInput(), cbd, 50));
        cbd.addKeyListener(kl);
        this.addKeyListener(kl);
        
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(textArea);
        container.add(cbd);
    }

    @Override
    public void repaint() {
        super.repaint();
    }

}
