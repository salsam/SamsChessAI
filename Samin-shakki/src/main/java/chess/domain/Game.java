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
    private GameSituation situation;
    private InputProcessor input;
    private LinkedList<Move> moves;
    private Thread aiThread;

    public Game() {
        this.ais = new boolean[2];
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

    public GameSituation getSituation() {
        return situation;
    }

    public InputProcessor getInput() {
        return input;
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
        return moves.getLast();
    }

    public Square getSquare(int column, int row) {
        return situation.getChessBoard().getSquare(column, row);
    }

    public Set<Coordinates> possibleMovesOnMainBoard(Piece piece) {
        return situation.getChessBoard().getMovementLogic().possibleMoves(piece,
                situation.getChessBoard());
    }

    public void moveOnMainBoard(Piece piece, Coordinates target) {
        situation.getChessBoard().getMovementLogic().move(piece, target, situation);
    }

    public void start() {
        this.continues = true;

        aiThread = new Thread() {
            @Override
            public void run() {
                while (continues && !interrupted()) {
                    while(!isAIsTurn()) {
                        try {
                            wait();
                        } catch (Exception e) {};
                    }
                    if (isAIsTurn()) {
                        moves.add(input.makeBestMoveAccordingToAILogic());
                        input.updateScreen();
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
        input.getFrames().get("game").repaint();
        start();
    }

}
