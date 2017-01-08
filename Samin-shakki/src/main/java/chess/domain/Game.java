package chess.domain;

import chess.logic.chessboardinitializers.StandardChessBoardInitializer;
import chess.logic.inputprocessing.InputProcessor;
import chess.logic.movementlogic.MovementLogic;

/**
 *
 * @author sami
 */
public class Game {

    private GameSituation situation;
    private InputProcessor input;
    private boolean continues;
    private Thread[] aiThreads;

    public Game() {
        this.situation = new GameSituation(new StandardChessBoardInitializer(), new MovementLogic());
        this.input = new InputProcessor();
        this.continues = false;
        aiThreads = new Thread[2];
    }

    public void start() {
        this.continues = true;

        for (int i = 0; i < 2; i++) {
            createThreadForAI(i);
        }
    }

    private void createThreadForAI(int i) {
        if (situation.getAis()[i]) {
            aiThreads[i] = new Thread() {
                public void run() {
                    while (continues) {
                        if (situation.getTurn() % 2 == i) {
                            input.makeBestMoveAccordingToAILogic(situation);
                            input.getFrames().get("game").repaint();
                        }
                    }
                }
            };
            aiThreads[i].start();
        }
    }

    public void stop() {
        for (int i = 0; i < 2; i++) {
            if (aiThreads[i] != null) {
                try {
                    aiThreads[i].join();
                } catch (InterruptedException e) {
                    System.out.println("AIThread" + i + " was interrupted before game ended!");
                }
            }
        }
    }

    public void restart() {
        for (int i = 0; i < 2; i++) {
            input.getAis()[i].reset();
        }
        situation.reset();
        start();
    }

}
