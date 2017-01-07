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
    private GameSituation game;
    private InputProcessor input;

    public GraphicalUserInterface(InputProcessor inputProcessor, GameSituation game) {
        this.game = game;
        this.input = inputProcessor;

        frames = new HashMap();
        Controller controller = new Controller(frames);
        frames.put("game", new GameWindow(inputProcessor, game));
        frames.put("main", new MainFrame(frames.get("game"), controller));
        frames.put("endingScreen", new EndingScreen(controller));
        inputProcessor.setFrames(frames);
    }

    @Override
    public void run() {
        frames.get("main").setVisible(true);
    }
}
