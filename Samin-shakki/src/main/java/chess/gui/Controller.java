package chess.gui;

import chess.domain.GameSituation;
import chess.logic.ailogic.AILogic;
import chess.logic.chessboardinitializers.StandardChessBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
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
        } else if (cmd.equals("REPAINT")) {
            frames.get("game").repaint();
        } else if (cmd.equals("RESTART")) {
            GameWindow gameWindow = (GameWindow) frames.get("game");
            boolean[] players = gameWindow.getGame().getAis();
            gameWindow.getGame().reset();
            gameWindow.getGame().setBlackAI(players[0]);
            gameWindow.getGame().setWhiteAI(players[1]);
            gameWindow.getGame().setContinues(true);
            AILogic[] ai = gameWindow.getInputProcessor().getAis();
            ai[0].reset();
            ai[1].reset();
            gameWindow.repaint();
            frames.get("endingScreen").setVisible(false);
        } else if (cmd.equals("STARTAIVAI")) {
            MainFrame main = (MainFrame) frames.get("main");
            main.setVisible(false);
            main.getGameWindow().getGame().setBlackAI(true);
            main.getGameWindow().getGame().setWhiteAI(true);
            main.getGameWindow().getGame().setContinues(true);
            main.getGameWindow().setVisible(true);
            frames.get("adc").dispose();
        }
    }

}
