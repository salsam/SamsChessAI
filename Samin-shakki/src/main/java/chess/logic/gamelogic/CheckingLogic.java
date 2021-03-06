package chess.logic.gamelogic;

import chess.domain.Game;
import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import static chess.domain.board.Player.getOpponent;
import chess.domain.board.ChessBoardCopier;
import static chess.domain.board.ChessBoardCopier.undoMove;
import static chess.domain.board.Klass.*;
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
        return game.getChessBoard().threatenedSquares(getOpponent(player)).contains(playersKing.getLocation());
    }

    public static boolean checkIfChecked(ChessBoard board, Player player) {
        Piece playersKing = board.getKings().get(player);
        if (playersKing == null) {
            return false;
        }
        board.updateThreatenedSquares(getOpponent(player));
        return board.threatenedSquares(getOpponent(player)).contains(playersKing.getLocation());
    }

    /**
     * Checks whether or not player is checkmated in this game.
     *
     * @param player player who is possibly checkmated.
     * @return true if player is checkmated. Else false.
     */
    public boolean checkMate(Player player) {
        ChessBoard backUp = ChessBoardCopier.copy(game.getChessBoard());
        int movesTillDraw = game.getMovesTillDraw();
        for (Piece piece : game.getChessBoard().getPieces(player)) {
            if (piece.isTaken()) {
                continue;
            }

            Square from = piece.getLocation();
            game.getChessBoard().updateThreatenedSquares(getOpponent(player));
            for (Square possibility : game.getChessBoard().getMovementLogic().possibleMoves(piece, game.getChessBoard())) {
                game.getChessBoard().getMovementLogic().move(piece, possibility, game);
                game.getChessBoard().updateThreatenedSquares(getOpponent(player));
                if (!checkIfChecked(player)) {
                    undoMove(backUp, game, from, possibility, movesTillDraw);
                    return false;
                }
                undoMove(backUp, game, from, possibility, movesTillDraw);
            }
        }

        return true;
    }

    public static boolean checkMate(GameSituation gameSit, Player player) {
        ChessBoard backUp = ChessBoardCopier.copy(gameSit.getChessBoard());
        int movesTillDraw = gameSit.getMovesTillDraw();
        for (Piece piece : gameSit.getChessBoard().getPieces(player)) {
            if (piece.isTaken()) {
                continue;
            }

            Square from = piece.getLocation();
            gameSit.getChessBoard().updateThreatenedSquares(getOpponent(player));
            for (Square possibility : gameSit.getChessBoard().getMovementLogic().possibleMoves(piece, gameSit.getChessBoard())) {
                gameSit.getChessBoard().getMovementLogic().move(piece, possibility, gameSit);
                gameSit.getChessBoard().updateThreatenedSquares(getOpponent(player));
                if (!checkIfChecked(gameSit.getChessBoard(), player)) {
                    undoMove(backUp, gameSit, from, possibility, movesTillDraw);
                    return false;
                }
                undoMove(backUp, gameSit, from, possibility, movesTillDraw);
            }
        }

        return true;
    }

    /**
     * Returns whether or not chosen player has any legal moves.
     *
     * @param player chosen player
     * @return true player has no legal moves otherwise false.
     */
    public boolean stalemate(Player player) {
        if (game.getChessBoard().getPieces(player).isEmpty()) {
            return true;
        }
        if (game.getChessBoard().getPieces(player).stream()
                .filter((piece) -> !(piece.isTaken()))
                .anyMatch((piece) -> (!game.getChessBoard().getMovementLogic()
                        .possibleMoves(piece, game.getChessBoard()).isEmpty()))) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if there's insufficient material on board for checkmate.
     *
     * @return true if checkmate is impossible with pieces on board, otherwise
     * false.
     */
    public boolean insufficientMaterial() {
        boolean[][] hasBishopKnight = new boolean[2][2];
        for (int i = 0; i < 2; i++) {
            for (Piece p : game.getChessBoard().getPieces(Player.values()[i])) {
                if (p.isTaken()) {
                    continue;
                }

                if (p.getKlass() == PAWN || p.getKlass() == QUEEN || p.getKlass() == ROOK) {
                    return false;
                } else if (p.getKlass() == BISHOP) {
                    if (hasBishopKnight[i][0] == true) {
                        return false;
                    }
                    hasBishopKnight[i][0] = true;
                } else if (p.getKlass() == KNIGHT) {
                    if (hasBishopKnight[i][1] == true) {
                        return false;
                    }
                    hasBishopKnight[i][1] = true;
                }
            }
        }

        if (hasBishopKnight[0][0] == false && hasBishopKnight[0][1] == false) {
            if (hasBishopKnight[1][0] == false || hasBishopKnight[1][1] == false) {
                return true;
            }
        } else if (hasBishopKnight[1][0] == false && hasBishopKnight[1][1] == false) {
            if (hasBishopKnight[0][0] == false || hasBishopKnight[0][1] == false) {
                return true;
            }
        }

        return false;
    }
}
