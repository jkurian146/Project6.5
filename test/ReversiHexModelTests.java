import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import discs.Disc;
import discs.DiscColor;
import discs.DiscType;
import discs.GameDisc;
import model.GameState;
import model.ReversiHexModel;
import model.ReversiModel;
import player.PlayerTurn;
import view.ReversiView;
import view.ReversiTextualView;

/**
 * This class is for testing the intended behavior of ReversiHexModel.
 */
public class ReversiHexModelTests {
  ReversiModel model;
  PlayerTurn player1;
  PlayerTurn player2;
  ReversiView textualView;

  @Before
  public void initData() {
    this.model = new ReversiHexModel();
    this.player1 = PlayerTurn.PLAYER1;
    this.player2 = PlayerTurn.PLAYER2;
    this.textualView = new ReversiTextualView(model, new StringBuilder());
  }

  @Test
  public void testStartGameWithInvalidBoardSizes() {
    int lessThan3 = 1;
    int three = 3;
    int evenNumber = 6;

    // Assert that starting the game with invalid board sizes throw the expected exceptions
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(lessThan3));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(three));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.startGame(evenNumber));

    // Assert that attempting to start the game after
    // it has already been started throws an exception
    model.startGame(7);
    Assert.assertThrows(IllegalStateException.class, () -> model.startGame(7));
  }

  @Test
  public void testAttemptToDoSomethingWhileGameNotStarted() {
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(0, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.isGameOver());
    Assert.assertThrows(IllegalStateException.class, () -> model.currentTurn());
    Assert.assertThrows(IllegalStateException.class, () -> model.getDiscAt(0, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.isDiscFlipped(0, 0));
    Assert.assertThrows(IllegalStateException.class, () -> model.pass());
  }

  @Test
  public void testStartGameTwice() {
    model.startGame(7);
    Assert.assertThrows(IllegalStateException.class, () ->  model.startGame(7));
  }

  @Test
  public void testCurrentTurnTogglePlayerAndPass() {
    model.startGame(7);

    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
    model.pass();
    Assert.assertEquals(player1, model.currentTurn());
    model.pass();
    Assert.assertEquals(player2, model.currentTurn());
  }

  @Test
  public void testSimpleMoves() {
    model.startGame(7);
    model.pass();
    // up right
    model.makeMove(3,1);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2,3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3,2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3,1).getColor());
    // up left
    model.makeMove(3,0);
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(3,0).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(3,1).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(4,2).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(4,3).getColor());
    model.pass();
    // down left
    model.makeMove(2,4);
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(2,4).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(2,3).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(3,2).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(3,1).getColor());
    // move left
    model.makeMove(1,4);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4,4).getColor());
    // down right
    model.makeMove(3,5);
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(2,3).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(3,4).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(3,5).getColor());
    // move right
    model.makeMove(5,4);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1,4).getColor());

  }

  // negative tests
  @Test
  public void testMoveToMiddle() {
    model.startGame(7);
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(3,3));
  }

  @Test
  public void testMoveToNonFlipped() {
    model.startGame(7);
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(4,3));
  }

  @Test
  public void testMoveNotJumpingEmpty() {
    model.startGame(7);
    Assert.assertThrows(IllegalStateException.class, () -> model.makeMove(4,1));
  }

  @Test
  public void testUserProvidedInvalidCoordinate() {
    model.startGame(7);
    Assert.assertThrows(IllegalArgumentException.class, () -> model.makeMove(6,0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.makeMove(1,0));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.makeMove(6,6));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.makeMove(1,6));
    Assert.assertThrows(IllegalArgumentException.class, () -> model.makeMove(-1,1));
  }

  @Test
  public void integrationMoves() {
    model.startGame(7);
    Assert.assertEquals(PlayerTurn.PLAYER1, model.currentTurn());

    model.makeMove(2,2);
    textualView.render();
    Assert.assertEquals(GameState.ONGOING, model.getCurrentGameState());

    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(2,2).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(2, 3).getColor());

    model.makeMove(5,2);
    textualView.render();

    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5,2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4,3).getColor());

    model.makeMove(6,2);
    textualView.render();

    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(6,2).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(5,2).getColor());

    model.pass();
    Assert.assertEquals(PlayerTurn.PLAYER1, model.currentTurn());
    model.makeMove(5, 4);
    textualView.render();

    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(5,4).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(4,3).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(4,4).getColor());
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(GameState.PLAYER1WIN, model.getCurrentGameState());
  }

  @Test
  public void testIsGameOverWhenPassedTwice() {
    model.startGame(7);
    model.makeMove(2,2);
    model.pass();
    model.pass();
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(GameState.STALEMATE, model.getCurrentGameState());
  }

  @Test
  public void testIsGameOverWhenNotPassedTwice() {
    model.startGame(7);
    model.pass();
    Assert.assertEquals(GameState.ONGOING, model.getCurrentGameState());
    model.makeMove(5,2);
    model.pass();
    Assert.assertFalse(model.isGameOver());
    Assert.assertEquals(GameState.ONGOING, model.getCurrentGameState());
  }

  @Test
  public void testGameIsOverWhenNoValidMovesForEither() {
    Disc[][] mockBoard = new Disc[5][5];
    mockBoard[0][3] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[1][2] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[2][2] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[3][1] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[4][1] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    ReversiModel model = new ReversiHexModel(mockBoard);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(GameState.STALEMATE,model.getCurrentGameState());
  }

  @Test
  public void testGameIsOverBoardIsFull() {
    Disc[][] mockBoard = new Disc[5][5];
    mockBoard[0][1] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[0][2] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[0][3] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[1][0] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[1][1] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[1][3] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[2][0] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[2][1] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[2][2] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[2][3] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[2][4] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[3][0] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[3][1] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[3][2] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[3][3] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[4][1] = new GameDisc(DiscType.HEXDISC,DiscColor.WHITE);
    mockBoard[4][2] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    mockBoard[4][3] = new GameDisc(DiscType.HEXDISC,DiscColor.BLACK);
    ReversiModel model = new ReversiHexModel(mockBoard);
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(GameState.STALEMATE, model.getCurrentGameState());
  }

  // Tests that no disc is unintentionally captured when making a move.
  // Tests this by making a move and then checking that all discs
  // that should not be captured are unaffected.
  @Test
  public void testMakeMoveDoesNotCaptureDiscsUnintentionally() {
    model.startGame(7);

    // for readability
    DiscColor b = DiscColor.BLACK;
    DiscColor w = DiscColor.WHITE;
    DiscColor f = DiscColor.FACEDOWN;

    // First check color of every game disc.
    // Will refer back to these tests after each move,
    // checking that only intended discs are captured.

    // Starting pieces
    Assert.assertEquals(b, model.getDiscAt(4, 3).getColor());
    Assert.assertEquals(b, model.getDiscAt(3, 2).getColor());
    Assert.assertEquals(b, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(w, model.getDiscAt(4, 4).getColor());
    Assert.assertEquals(w, model.getDiscAt(4, 2).getColor());
    Assert.assertEquals(w, model.getDiscAt(2, 3).getColor());

    // FaceDown/empty discs, these are not yet captured.
    // Tests are Sorted by row of discs.
    Assert.assertEquals(f, model.getDiscAt(2, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(0, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 6).getColor());

    // There are 36 discs in total. checking to see that no discs were unaccounted for
    // by subtracting the line# of the first disc test from the line# of the last disc test.
    // Adding 3 to 36 to account for spaces in between code.
    Assert.assertEquals(36 + 3, 402 - 363);


    // Now that all discs on the game board have been checked, a move will be made.
    model.makeMove(2, 2);
    textualView.render();

    // The captured discs from this move.
    Assert.assertEquals(b, model.getDiscAt(2, 2).getColor());
    Assert.assertEquals(b, model.getDiscAt(2, 3).getColor());

    // Now check all other discs have the same color as before.
    Assert.assertEquals(b, model.getDiscAt(4, 3).getColor());
    Assert.assertEquals(b, model.getDiscAt(3, 2).getColor());
    Assert.assertEquals(b, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(w, model.getDiscAt(4, 4).getColor());
    Assert.assertEquals(w, model.getDiscAt(4, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(0, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 6).getColor());

    Assert.assertEquals(36 + 2, 453 - 415);

    model.makeMove(5, 2);
    textualView.render();

    // The captured discs from this move.
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 3).getColor());

    // All other discs.
    Assert.assertEquals(b, model.getDiscAt(2, 2).getColor());
    Assert.assertEquals(b, model.getDiscAt(2, 3).getColor());
    Assert.assertEquals(b, model.getDiscAt(3, 2).getColor());
    Assert.assertEquals(b, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(w, model.getDiscAt(4, 4).getColor());
    Assert.assertEquals(w, model.getDiscAt(4, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 0).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 1).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 2).getColor());
    Assert.assertEquals(f, model.getDiscAt(0, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 3).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(6, 4).getColor());
    Assert.assertEquals(f, model.getDiscAt(1, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 5).getColor());
    Assert.assertEquals(f, model.getDiscAt(2, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(3, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(4, 6).getColor());
    Assert.assertEquals(f, model.getDiscAt(5, 6).getColor());

    Assert.assertEquals(36 + 2, 499 - 461);
  }

  @Test
  public void moveToNull() {
    model.startGame(7);
    Assert.assertThrows(IllegalStateException.class, () ->
            model.makeMove(5,0));
  }


}