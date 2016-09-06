package chess.gui;

import chess.domain.GameSituation;
import chess.logic.inputprocessing.InputProcessor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author sami
 */
public class GraphicalUserInterface implements Runnable {

    private Map<String, JFrame> frames;

    public GraphicalUserInterface(InputProcessor inputProcessor, GameSituation game) {
        frames = new HashMap();
        frames.put("game", new GameWindow(inputProcessor, game));
        frames.put("main", new MainFrame(frames.get("game")));
        frames.put("endingScreen", new EndingScreen(frames));
        inputProcessor.setFrames(frames);
    }

    @Override
    public void run() {
        frames.get("main").setVisible(true);
    }
}
