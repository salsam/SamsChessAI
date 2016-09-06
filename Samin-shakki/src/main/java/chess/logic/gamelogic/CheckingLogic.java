package chess.logic.gamelogic;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import static chess.domain.board.Player.getOpponent;
import chess.domain.board.ChessBoardCopier;
import static chess.domain.board.ChessBoardCopier.undoMove;
import chess.domain.board.Square;
import chess.domain.board.Piece;

/**
 * This class is responsible for checking if king is checked, checkmated or
 * stalemated in the game given to it as parameter. After checking that it will
 * act according to situation to continue game flow.
 *
 * @author sami
 */
public class CheckingLogic {

    /**
     * GameSituation that this class will check for check, checkmate and
     * stalemate.
     */
    private GameSituation game;

    /**
     * Creates a new CheckingLogic-object for given game.
     *
     * @param game game that this checkingLogic will be applied for.
     */
    public CheckingLogic(GameSituation game) {
        this.game = game;
    }

    /**
     * Checks if player is both checked and checkmated.
     *
     * @param player player whose game situation is being checked
     * @return true if player is both checked and checkmated
     */
    public boolean checkIfCheckedAndMated(Player player) {
        return checkIfChecked(player) && checkMate(player);
    }

    /**
     * Updates set containing all squares that opponent threatens and then
     * checks if player's king is on one of those.
     *
     * @param player player being checked
     * @return true if player's king is threatened by opposing piece
     */
    public boolean checkIfChecked(Player player) {
        Piece playersKing = game.getChessBoard().getKings().get(player);
        if (playersKing == null) {
            return false;
        }
        game.getChessBoard().updateThreatenedSquares(getOpponent(player));
        return game.getChessBoard().threatenedSquares(getOpponent(player)).contains(game.getChessBoard().getSquare(playersKing.getColumn(), playersKing.getRow()));
    }

    /**
     * Checks whether or not player is checkmated in this game.
     *
     * @param player player who is possibly checkmated.
     * @return true if player is checkmated. Else false.
     */
    public boolean checkMate(Player player) {
        ChessBoard backUp = ChessBoardCopier.copy(game.getChessBoard());
        for (Piece piece : game.getChessBoard().getPieces(player)) {
            if (piece.isTaken()) {
                continue;
            }

            Square from = game.getChessBoard().getSquare(piece.getColumn(), piece.getRow());
            game.getChessBoard().updateThreatenedSquares(getOpponent(player));
            for (Square possibility : game.getChessBoard().getMovementLogic().possibleMoves(piece, game.getChessBoard())) {
                game.getChessBoard().getMovementLogic().move(piece, possibility, game);
                game.getChessBoard().updateThreatenedSquares(getOpponent(player));
                if (!checkIfChecked(player)) {
                    undoMove(backUp, game, from, possibility);
                    return false;
                }
                undoMove(backUp, game, from, possibility);
            }
        }

        game.setContinues(false);

        return true;
    }

    /**
     * Returns whether or not chosen player has any legal moves.
     *
     * @param player chosen player
     * @return true player has no legal moves otherwise false.
     */
    public boolean stalemate(Player player) {
        if (game.getChessBoard().getPieces(player).stream()
                .filter((piece) -> !(piece.isTaken()))
                .anyMatch((piece) -> (!game.getChessBoard().getMovementLogic()
                        .possibleMoves(piece, game.getChessBoard()).isEmpty()))) {
            return false;
        }
        game.setContinues(false);
        return true;
    }
}
