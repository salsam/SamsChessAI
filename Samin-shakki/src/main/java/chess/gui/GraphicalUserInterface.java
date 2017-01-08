package chess.gui;

import chess.domain.Game;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author sami
 */
public class GraphicalUserInterface implements Runnable {

    private Map<String, JFrame> frames;

    public GraphicalUserInterface(Game game) {

        frames = new HashMap();
        Controller controller = new Controller(frames);
        frames.put("game", new GameWindow(game));
        frames.put("main", new MainFrame(frames.get("game"), controller));
        frames.put("endingScreen", new EndingScreen(controller));
        game.getInput().setFrames(frames);
    }

    @Override
    public void run() {
        frames.get("main").setVisible(true);
    }
}
