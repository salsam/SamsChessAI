package chess.logic.gamelogic;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardCopier;
import chess.domain.board.Player;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.domain.board.Square;
import chess.logic.chessboardinitializers.*;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.movementlogic.MovementLogic;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class CheckingLogicTest {

    private static CheckingLogic cl;
    private static GameSituation game;
    private static MovementLogic ml;
    private static EmptyBoardInitializer emptyinit;

    public CheckingLogicTest() {
        emptyinit = new EmptyBoardInitializer();
        ml = new MovementLogic();
        game = new GameSituation(emptyinit, ml);
        cl = new CheckingLogic(game);
    }

    @Before
    public void setUp() {
        game.reset();
        putPieceOnBoard(game.getChessBoard(), new Piece(KING, 0, 0, Player.WHITE, "wk"));
    }

    @Test
    public void checkIfCheckedReturnFalseIfKingIsNotChecked() {
        assertFalse(cl.checkIfChecked(Player.WHITE));
    }

    @Test
    public void checkIfCheckedReturnsTrueIfKingIsChecked() {
        putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 1, 1, Player.BLACK, "bq"));
        assertTrue(cl.checkIfChecked(Player.WHITE));
    }

    @Test
    public void checkMateIsFalseIfNotChecked() {
        assertFalse(cl.checkMate(Player.WHITE));
    }

    @Test
    public void checkMateTrueIfKingCheckedAndCheckCannotBePrevented() {
        putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 1, 1, Player.BLACK, "bq"));
        putPieceOnBoard(game.getChessBoard(), new Piece(KING, 2, 2, Player.BLACK, "bk"));
        assertTrue(cl.checkMate(Player.WHITE));
    }

    @Test
    public void checkMateFalseIfKingCheckedButCheckingPieceCanBeTaken() {
        putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 1, 1, Player.BLACK, "bq"));
        assertFalse(cl.checkMate(Player.WHITE));
    }

    @Test
    public void chessBoardIsNotAffectedByCheckingIfKingIsCheckMated() {
        putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 6, 6, Player.BLACK, "bq"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 1, 6, Player.BLACK, "br1"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 6, 1, Player.BLACK, "br2"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 4, 1, Player.WHITE, "wr"));
        ChessBoard copy = ChessBoardCopier.copy(game.getChessBoard());
        assertFalse(cl.checkMate(Player.WHITE));
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(copy, game.getChessBoard()));
    }

    @Test
    public void chessBoardIsNotAffectedByCheckingIfKingIsCheckMatedInComplexSituation() {
        ChessBoardInitializer stdinit = new StandardChessBoardInitializer();
        stdinit.initialize(game.getChessBoard());
        MovementLogic mvl = game.getChessBoard().getMovementLogic();

        mvl.move(game.getChessBoard().getPiece(4, 6), new Square(4, 5), game);
        mvl.move(game.getChessBoard().getPiece(5, 1), new Square(5, 3), game);
        mvl.move(game.getChessBoard().getPiece(1, 6), new Square(1, 5), game);
        mvl.move(game.getChessBoard().getPiece(6, 1), new Square(6, 3), game);
        mvl.move(game.getChessBoard().getPiece(3, 7), new Square(7, 3), game);
        game.getChessBoard().updateThreatenedSquares(Player.WHITE);
        ChessBoard backUp = ChessBoardCopier.copy(game.getChessBoard());
        assertTrue(cl.checkMate(Player.BLACK));

        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(backUp, game.getChessBoard()));
    }

    @Test
    public void checkMateFalseIfCheckCanBeBlocked() {
        putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 6, 6, Player.BLACK, "bq"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 1, 6, Player.BLACK, "br1"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 6, 1, Player.BLACK, "br2"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 4, 1, Player.WHITE, "wr"));
        assertFalse(cl.checkMate(Player.WHITE));
    }

    @Test
    public void checkMateFalseIfKingCanMoveToUnthreatenedSquare() {
        putPieceOnBoard(game.getChessBoard(), new Piece(PAWN, 1, 1, Player.BLACK, "bp1"));
        putPieceOnBoard(game.getChessBoard(), new Piece(PAWN, 2, 2, Player.BLACK, "bp2"));
        assertFalse(cl.checkMate(Player.WHITE));
    }

    @Test
    public void checkMateFalseInComplexSituationWhereKingThreatenedByProtectedPieceButCanBeAvoided() {
        game = new GameSituation(new StandardChessBoardInitializer(), ml);
        Piece whiteKing = game.getChessBoard().getKings().get(Player.WHITE);
        Piece whitePawn =  game.getChessBoard().getPiece(5, 1);
        Piece blackPawn1 = game.getChessBoard().getPiece(5, 6);
        Piece blackPawn2 = game.getChessBoard().getPiece(4, 6);

        ml.move(whitePawn, new Square(5, 2), game);
        ml.move(blackPawn1, new Square(5, 4), game);
        ml.move(whiteKing, new Square(5, 1), game);
        ml.move(blackPawn2, new Square(4, 4), game);
        ml.move(whiteKing, new Square(6, 2), game);
        ml.move(blackPawn1, new Square(5, 3), game);
        assertFalse(cl.checkMate(Player.WHITE));
        game = new GameSituation(emptyinit, ml);
    }

    @Test
    public void staleMateTrueIfKingNotCheckedAndThereIsNoLegalMoves() {
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 1, 7, Player.BLACK, "br1"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 7, 1, Player.BLACK, "br2"));
        game.getChessBoard().updateThreatenedSquares(Player.BLACK);
        assertTrue(cl.stalemate(Player.WHITE));
    }

    @Test
    public void staleMateFalseIfKingCanMoveLegally() {
        assertFalse(cl.stalemate(Player.WHITE));
    }

    @Test
    public void staleMateFalseIfThereIsSomeOtherPieceThatCanMoveLegally() {
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 1, 7, Player.BLACK, "br1"));
        putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 7, 1, Player.BLACK, "br2"));
        putPieceOnBoard(game.getChessBoard(), new Piece(PAWN, 4, 4, Player.WHITE, "wp"));
        game.getChessBoard().updateThreatenedSquares(Player.BLACK);
        assertFalse(cl.stalemate(Player.WHITE));
    }
}
