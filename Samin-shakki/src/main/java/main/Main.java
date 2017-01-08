package main;

import chess.domain.Game;
import chess.gui.GraphicalUserInterface;

/**
 *
 * @author sami
 */
public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        GraphicalUserInterface gui = new GraphicalUserInterface(game);
        gui.run();
    }
}
