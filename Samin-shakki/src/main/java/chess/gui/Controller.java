package chess.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author sami
 */
public class Controller implements ActionListener {

    private Map<String, JFrame> frames;

    public Controller(Map<String, JFrame> frames) {
        this.frames = frames;
    }

    public MainFrame getMain() {
        return (MainFrame) frames.get("main");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand().toUpperCase().trim();

        if (cmd.equals("AIVAI")) {
            frames.get("main").setVisible(false);
            frames.put("adc", new AiVsAiDifficultyChooser(this));
        } else if (cmd.equals("EXIT")) {
            frames.values().stream().forEach(frame -> {
                frame.dispose();
            });
        } else if (cmd.equals("PVAI")) {
            frames.get("main").setVisible(false);
            SideChooser sc = new SideChooser((MainFrame) frames.get("main"));
        } else if (cmd.equals("PVP")) {
            frames.get("main").setVisible(false);
            frames.get("game").setVisible(true);
            ((GameWindow) frames.get("game")).getGame().start();
        } else if (cmd.equals("REPAINT")) {
            frames.get("game").repaint();
        } else if (cmd.equals("RESTART")) {
            ((GameWindow) frames.get("game")).getGame().restart();
            frames.get("endingScreen").setVisible(false);
        } else if (cmd.equals("STARTAIVAI")) {
            MainFrame main = (MainFrame) frames.get("main");
            main.setVisible(false);
            main.getGameWindow().getGame().setBlackAI(true);
            main.getGameWindow().getGame().setWhiteAI(true);
            main.getGameWindow().setVisible(true);
            frames.get("adc").dispose();
            main.getGameWindow().getGame().start();
        }
    }

}
