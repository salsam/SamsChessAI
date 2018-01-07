package chess.domain;

import chess.domain.board.Piece;
import chess.domain.board.Square;
import chess.logic.chessboardinitializers.StandardChessBoardInitializer;
import chess.logic.inputprocessing.InputProcessor;
import chess.logic.movementlogic.MovementLogic;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author sami
 */
public class Game {

    private boolean[] ais;
    private boolean continues;
    private boolean AIisComputing;
    private GameSituation situation;
    private InputProcessor input;
    private LinkedList<Move> moves;
    private Thread aiThread;

    public Game() {
        this.ais = new boolean[2];
        this.AIisComputing = false;
        this.situation = new GameSituation(new StandardChessBoardInitializer(), new MovementLogic());
        this.input = new InputProcessor(this);
        this.continues = false;
        this.moves = new LinkedList();
    }

    public boolean getContinues() {
        return continues;
    }

    public void setContinues(boolean continues) {
        this.continues = continues;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void cancelLastMove() {
        if (!moves.isEmpty()) {
            moves.removeLast();
        }
    }

    public GameSituation getSituation() {
        return situation;
    }

    public InputProcessor getInput() {
        return input;
    }

    public boolean getAIisComputing() {
        return this.AIisComputing;
    }

    public boolean isAIsTurn() {
        return ais[situation.getTurn() % 2];
    }

    public void setWhiteAI(boolean ai) {
        ais[1] = ai;
    }

    public void setBlackAI(boolean ai) {
        ais[0] = ai;
    }

    public Move lastMove() {
        if (moves.isEmpty()) {
            return null;
        }
        return moves.getLast();
    }

    public Set<Square> possibleMovesOnMainBoard(Piece piece) {
        return situation.getChessBoard().getMovementLogic().possibleMoves(piece,
                situation.getChessBoard());
    }

    public void moveOnMainBoard(Piece piece, Square target) {
        situation.getChessBoard().getMovementLogic().move(piece, target, situation);
    }

    public void start() {
        this.continues = true;

        aiThread = new Thread() {
            @Override
            public void run() {
                while (continues && !interrupted()) {
                    while (!isAIsTurn()) {
                        try {
                            wait();
                        } catch (Exception e) {
                        };
                    }
                    if (isAIsTurn()) {
                        AIisComputing = true;
                        input.updateScreen();
                        try {
                            sleep(200);
                        } catch (Exception e) {
                        };
                        long start=System.currentTimeMillis();
                        input.makeBestMoveAccordingToAILogic();
                        //System.out.println("Movement took: " + (System.currentTimeMillis()-start));
                        AIisComputing = false;
                    }
                }
            }
        };
        aiThread.start();
    }

    public void stop() {
        continues = false;
        aiThread.interrupt();
    }

    public void restart() {
        for (int i = 0; i < 2; i++) {
            input.getAis()[i].reset();
        }
        moves.clear();
        situation.reset();
        input.updateTextArea();
        input.updateScreen();
        start();
    }

}
