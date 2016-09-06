package chess.gui.boarddrawing;

import chess.domain.datastructures.Pair;
import static chess.io.ImageLoader.getImage;
import chess.domain.board.Player;
import chess.domain.datastructures.MyHashMap;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.*;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;

/**
 *
 * @author sami
 */
public class PieceDrawer {

    private Map<Pair, Image> images;

    public PieceDrawer() {
        images = new MyHashMap();
        addImages();
    }

    private void addImages() {
        images.put(new Pair(BISHOP, Player.BLACK), getImage("blackBishop1.png"));
        images.put(new Pair(BISHOP, Player.WHITE), getImage("whiteBishop1.png"));
        images.put(new Pair(KING, Player.BLACK), getImage("blackKing1.png"));
        images.put(new Pair(KING, Player.WHITE), getImage("whiteKing1.png"));
        images.put(new Pair(KNIGHT, Player.BLACK), getImage("blackKnight1.png"));
        images.put(new Pair(KNIGHT, Player.WHITE), getImage("whiteKnight1.png"));
        images.put(new Pair(PAWN, Player.BLACK), getImage("blackPawn1.png"));
        images.put(new Pair(PAWN, Player.WHITE), getImage("whitePawn1.png"));
        images.put(new Pair(QUEEN, Player.BLACK), getImage("blackQueen1.png"));
        images.put(new Pair(QUEEN, Player.WHITE), getImage("whiteQueen1.png"));
        images.put(new Pair(ROOK, Player.BLACK), getImage("blackRook1.png"));
        images.put(new Pair(ROOK, Player.WHITE), getImage("whiteRook1.png"));
    }

    /**
     * Draws a picture of chosen piece. Picture depends on the class of chosen
     * piece as well as the owner of piece.
     *
     * @param piece piece to be drawn.
     * @param graphics Graphics object used to draw the image.
     * @param sideLength length of each square's sides.
     */
    public void draw(Piece piece, Graphics graphics, int sideLength) {
        int x = piece.getColumn() * sideLength;
        int y = piece.getRow() * sideLength;
        graphics.drawImage(images.get(new Pair(piece.getKlass(), piece.getOwner())), x, y, sideLength, sideLength, null);
    }
}
