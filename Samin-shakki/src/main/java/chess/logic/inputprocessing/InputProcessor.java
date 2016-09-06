package chess.logic.inputprocessing;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardCopier;
import static chess.domain.board.Player.getOpponent;
import chess.domain.board.Square;
import chess.domain.GameSituation;
import chess.domain.Move;
import static chess.domain.board.ChessBoardCopier.undoMove;
import chess.domain.board.Piece;
import chess.logic.ailogic.AILogic;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This is responsible for connecting graphical user interface to other logic
 * classes. Class offers methods update text given to players and process
 * information given by graphical user interface to move pieces accordingly in
 * game.
 *
 * @author sami
 */
public class InputProcessor {

    /**
     * JLabel that this InputProcessor will update with messages for players.
     */
    private JLabel textArea;
    /**
     * Map containing all frames in the GUI so this class can open EndingScreen.
     */
    private Map<String, JFrame> frames;
    /**
     * Piece that has been chosen for movement.
     */
    private Piece chosen;
    /**
     * Squares that chosen piece can move to.
     */
    private Set<Square> possibilities;

    private AILogic[] ais;

    /**
     * Creates a new InputProcessor-object.
     */
    public InputProcessor() {
        ais = new AILogic[2];
        this.ais[0] = new AILogic();
        this.ais[1] = new AILogic();
    }

    public Piece getChosen() {
        return chosen;
    }

    public void setChosen(Piece chosen) {
        this.chosen = chosen;
    }

    public void setFrames(Map<String, JFrame> frames) {
        this.frames = frames;
    }

    public void setAiDifficulty(int whose, long timeLimit) {
        this.ais[whose].setTimeLimit(timeLimit);
    }

    public void setTextArea(JLabel textArea) {
        this.textArea = textArea;
    }

    public Set<Square> getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(Set<Square> possibilities) {
        this.possibilities = possibilities;
    }

    public AILogic[] getAis() {
        return this.ais;
    }

    /**
     * Processes input given by ChessBoardListener to do correct action in the
     * game.
     *
     * @param column column that was clicked
     * @param row row that was clicked
     * @param game game which is going on
     */
    public void processClick(int column, int row, GameSituation game) {
        if (!game.getContinues()) {
            return;
        }

        if (game.getAis()[game.getTurn() % 2]) {
            makeBestMoveAccordingToAILogic(game);
        } else if (game.getChessBoard().withinTable(column, row)) {
            if (chosen != null && possibilities.contains(game.getChessBoard().getSquare(column, row))) {
                moveToTargetLocation(column, row, game);
            } else if (game.getChecker().checkPlayerOwnsPieceOnTargetSquare(game.whoseTurn(), column, row)) {
                setChosen(game.getChessBoard().getSquare(column, row).getPiece());
            }
            if (chosen != null) {
                possibilities = game.getChessBoard().getMovementLogic().possibleMoves(chosen, game.getChessBoard());
            }
        }
    }

    private void makeBestMoveAccordingToAILogic(GameSituation game) {
        ais[game.getTurn() % 2].findBestMoves(game);
        Move move = ais[game.getTurn() % 2].getBestMove();
        setChosen(move.getPiece());
        moveToTargetLocation(move.getTarget().getColumn(),
                move.getTarget().getRow(), game);
    }

    private void moveToTargetLocation(int column, int row, GameSituation game) {
        ChessBoard backUp = ChessBoardCopier.copy(game.getChessBoard());
        Square target = game.getChessBoard().getSquare(column, row);
        Square from = game.getChessBoard().getSquare(chosen.getColumn(), chosen.getRow());

        game.getChessBoard().getMovementLogic().move(chosen, target, game);
        chosen = null;
        possibilities = null;

        if (game.getCheckLogic().checkIfChecked(game.whoseTurn())) {
            undoMove(backUp, game, from, target);
            return;
        }

        startNextTurn(game);
    }

    private void startNextTurn(GameSituation game) {
        game.nextTurn();
        textArea.setText(game.whoseTurn() + "'s turn.");
        if (game.getCountOfCurrentSituation() >= 3) {
            game.setContinues(false);
            textArea.setText("Third repetition of situation. Game ended as a draw!");
            frames.get("endingScreen").setVisible(true);
        }
        if (game.getCheckLogic().checkIfChecked(game.whoseTurn())) {
            textArea.setText(textArea.getText() + " Check!");
            if (game.getCheckLogic().checkMate(game.whoseTurn())) {
                textArea.setText("Checkmate! " + getOpponent(game.whoseTurn()) + " won!");
                frames.get("endingScreen").setVisible(true);
            }
        } else if (game.getCheckLogic().stalemate(game.whoseTurn())) {
            textArea.setText("Stalemate! Game ended as a draw!");
            frames.get("endingScreen").setVisible(true);
        }
    }

}
