package chess.logic.inputprocessing;

import chess.domain.Game;
import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardCopier;
import static chess.domain.board.Player.getOpponent;
import chess.domain.board.Square;
import chess.domain.Move;
import static chess.domain.board.ChessBoardCopier.undoMove;
import chess.domain.board.Klass;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import chess.domain.board.Piece;
import chess.logic.ailogic.AILogic;
import chess.logic.gamelogic.PromotionLogic;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This is responsible for connecting graphical user interface to other logic
 * classes. Class offers methods to update text given to players and process
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
     * Map containing all frames in the GUI so this class repaint game and open
     * ending screen.
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

    private Game game;

    /**
     * Creates a new InputProcessor-object.
     */
    public InputProcessor(Game game) {
        ais = new AILogic[2];
        this.ais[0] = new AILogic();
        this.ais[1] = new AILogic();
        this.game = game;
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

    public Map<String, JFrame> getFrames() {
        return this.frames;
    }

    public void setAiDifficulty(int whose, long timeLimit) {
        this.ais[whose].setTimeLimit(timeLimit);
    }

    public void setTextArea(JLabel textArea) {
        this.textArea = textArea;
    }

    public String getText() {
        return this.textArea.getText();
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
     */
    public void processClick(int column, int row) {
        if (!game.getContinues() || game.isAIsTurn()) {
            return;
        }

        if (game.getSituation().getChessBoard().withinTable(column, row)) {
            if (chosen != null && possibilities.contains(new Square(column, row))) {
                game.addMove(new Move(chosen, column, row));
                moveToTargetLocation(column, row, false);
            } else if (game.getSituation().getChecker().checkPlayerOwnsPieceOnTargetSquare(
                    game.getSituation().whoseTurn(), column, row)) {
                setChosen(game.getSituation().getChessBoard().getPiece(column, row));
            }
            if (chosen != null) {
                possibilities = game.possibleMovesOnMainBoard(chosen);
            }
        }
        updateScreen();
    }

    public void updateScreen() {
        frames.get("game").repaint();
    }

    public Move makeBestMoveAccordingToAILogic() {
        ais[game.getSituation().getTurn() % 2].findBestMoves(game.getSituation());
        Move move = ais[game.getSituation().getTurn() % 2].getBestMove();
        setChosen(move.getPiece());
        moveToTargetLocation(move.getTargetColumn(), move.getTargetRow(), true);
        return move;
    }

    private void moveToTargetLocation(int column, int row, boolean aisTurn) {
        ChessBoard backUp = ChessBoardCopier.copy(game.getSituation().getChessBoard());
        Square target = new Square(column, row);
        Square from = new Square(chosen.getColumn(), chosen.getRow());

        game.moveOnMainBoard(chosen, target);
        handlePromotion(aisTurn);

        chosen = null;
        possibilities = null;

        if (game.getSituation().getCheckLogic().checkIfChecked(game.getSituation().whoseTurn())) {
            undoMove(backUp, game.getSituation(), from, target);
            game.cancelLastMove();
            return;
        }

        game.getSituation().nextTurn();
        updateTextArea();
    }

    private void handlePromotion(boolean aisTurn) {
        if (chosen.getKlass() == PAWN && chosen.isAtOpposingEnd()) {
            if (aisTurn) {
                PromotionLogic.promote(game.getSituation(), chosen, QUEEN);
            } else {
                Object[] options = {"Bishop",
                    "Knight",
                    "Queen",
                    "Rook"
                };
                int n = JOptionPane.showOptionDialog(frames.get("game"),
                        "Which class would you like to promote your pawn?",
                        "Promotion screen",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);
                Klass[] klasses= {Klass.BISHOP, Klass.KNIGHT, Klass.QUEEN, Klass.ROOK};
                PromotionLogic.promote(game.getSituation(), chosen, klasses[n]);
            }
        }
    }

    public void updateTextArea() {
        textArea.setText(game.getSituation().whoseTurn() + "'s turn.");
        boolean ended = false;
        //Third repetition is currently disabled due to hashing errors!
        //To fix change <0 to game.getSituation().getCountOfCurrentSituation()>=3
        if (false) {
            System.out.println(game.getSituation().getCountOfCurrentSituation());
            game.stop();
            textArea.setText("Third repetition of situation. Game ended as a draw!");
            ended = true;
        } else if (game.getSituation().getMovesTillDraw() < 1) {
            game.stop();
            textArea.setText("50-move rule reached. Game ended as a draw!");
            ended = true;
        } else if (game.getSituation().getCheckLogic().checkIfChecked(game.getSituation().whoseTurn())) {
            textArea.setText(textArea.getText() + " Check!");
            if (game.getSituation().getCheckLogic().checkMate(game.getSituation().whoseTurn())) {
                textArea.setText("Checkmate! " + getOpponent(game.getSituation().whoseTurn()) + " won!");
                game.stop();
                ended = true;
            }
        } else if (game.getSituation().getCheckLogic().stalemate(game.getSituation().whoseTurn())) {
            game.stop();
            textArea.setText("Stalemate! Game ended as a draw!");
            ended = true;
        } else if (game.getSituation().getCheckLogic().insufficientMaterial()) {
            game.stop();
            textArea.setText("Insufficient material! Game ended as a draw!");
            ended = true;
        }
        if (ended) {
            frames.get("endingScreen").setVisible(true);
        }
    }

}
