/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template column, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.domain.board;

/**
 * This enum contains all possible players of chess and the direction their
 * pawns move towards. Also allows you to get player's opponent.
 *
 * @author sami
 */
public enum Player {

    WHITE(-1), BLACK(1);
    private int direction;

    private Player(int direction) {
        this.direction = direction;
    }

    /**
     * Returns direction that given player's pawn move towards.
     *
     * @return direction player's pawns move to.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Returns the opponent of player given as parameter.
     *
     * @param player Player whose opponent you want.
     * @return opposing player.
     */
    public static Player getOpponent(Player player) {
        if (player == Player.WHITE) {
            return Player.BLACK;
        } else {
            return Player.WHITE;
        }
    }

}
