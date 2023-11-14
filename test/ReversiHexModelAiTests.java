import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import discs.DiscColor;
import discs.DiscType;
import model.GameState;
import model.MoveDirection;
import model.MoveRules;
import model.ReversiHexModel;
import model.ReversiHexModelAI;
import model.ReversiModel;
import strategy.StrategyType;
import view.ReversiGUI;

public class ReversiHexModelAiTests {

  @Test
  public void testSetUpMirror() {

  }

  @Test
  public void testMaximizeSelectsLargestLeft() {
    ReversiHexModelAI rhmai = new ReversiHexModelAI(StrategyType.MAXIMIZE);
    rhmai.startGame(7);
    rhmai.makeMove(2, 2);
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(1, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(2, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(3, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(4, 2).getColor());
  }

  @Test
  public void testMaximizeStrategyChoosesMoveWithMostCaptures() {
    ReversiHexModelAI model = new ReversiHexModelAI(StrategyType.MAXIMIZE);
    model.startGame(7);

    model.makeMove(3, 5);

    // The move with the most captures, should result in the following discs to be captured.
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 6).getColor());

    // other legal moves for the AI (white) that should not have been made
    // as they capture fewer discs than the one shown above, which is the maxCapture move.
    Assert.assertNotEquals(DiscColor.WHITE, model.getDiscAt(5, 4).getColor());
    Assert.assertNotEquals(DiscColor.WHITE, model.getDiscAt(2, 2).getColor());
    Assert.assertNotEquals(DiscColor.WHITE, model.getDiscAt(3, 1).getColor());

    model.makeMove(2, 4);
    // this max move captured four discs, the closest move captures 3 discs, which is tested below
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 3).getColor());

    Assert.assertNotEquals(DiscColor.WHITE, model.getDiscAt(5, 2).getColor());
    Assert.assertNotEquals(DiscColor.WHITE, model.getDiscAt(4, 3).getColor());
    Assert.assertNotEquals(DiscColor.WHITE, model.getDiscAt(4, 4).getColor());

  }

  @Test
  public void testAIPassesWhenItHasNoMoves() {
    // could have used mock to simplify,
    // but this way we get to test functionality and see that everything is working

    ReversiHexModelAI model = new ReversiHexModelAI(StrategyType.MAXIMIZE);
    model.startGame(9);

    model.makeMove(4, 2);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 1).getColor());


    model.makeMove(4, 1);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 4).getColor());

    model.makeMove(2, 5);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 6).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 3).getColor());

    model.makeMove(5, 5);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 3).getColor());

    model.makeMove(4, 7);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 8).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 7).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 6).getColor());

    model.makeMove(3, 0);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 1).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 0).getColor());

    model.makeMove(5, 1);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 5).getColor());

    model.makeMove(2, 6);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 1).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 6).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 7).getColor());

    model.makeMove(0, 5);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 4).getColor());

    model.makeMove(6, 2);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(6, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(6, 1).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 1).getColor());

    model.makeMove(1, 3);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(0, 3).getColor());

    model.makeMove(2, 2);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(2, 2).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(1, 1).getColor());

    model.makeMove(2, 1);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(6, 5).getColor());

    model.makeMove(6, 6);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 7).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(6, 6).getColor());

    model.makeMove(7, 5);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(6, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(7, 4).getColor());

    model.makeMove(6, 3);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(7, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(7, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(6, 3).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 3).getColor());

    model.makeMove(3, 7);
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(5, 4).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 5).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(4, 6).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 7).getColor());
    Assert.assertEquals(DiscColor.WHITE, model.getDiscAt(3, 8).getColor());

    // Here the AI has no moves, it passes back to black
    model.makeMove(6, 7);
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(6, 6).getColor());
    Assert.assertEquals(DiscColor.BLACK, model.getDiscAt(6, 7).getColor());

    // black makes a move and then the game ends, because both players have no moves
    model.makeMove(2, 7);
    Assert.assertTrue(model.isGameOver());

    Assert.assertEquals(19, model.getScore(model.currentTurn()));
    Assert.assertEquals(23, model.getScore(model.getOpponent(model.currentTurn())));

    Assert.assertEquals(GameState.STALEMATE, model.getCurrentGameState());

    ReversiGUI gui = new ReversiGUI(model);
    gui.render();
  }

  // test that MaximizeStrat passes when no moves left

  @Test
  public void testAdjacentCornerCellsNotSelected () {
    ReversiHexModelAI rhmai = new ReversiHexModelAI(StrategyType.AVOIDCORNER);
    rhmai.startGame(7);
    rhmai.makeMove(2,2);
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(2,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(3,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(4,4).getColor());
    // another valid move at on (1,2),(2,2),(3,2),(4,2), but 1,2 is
    // adjacent to a corner so the ai selects (2,4), (3,4), (4,4)
    Assert.assertEquals(MoveRules.applyShiftBasedOnDirection(1,2, MoveDirection.DOWNLEFT),
            new ArrayList<>(Arrays.asList(0,3)));
    rhmai.makeMove(3,5);
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(2,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(3,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(4,4).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(5,4).getColor());
    // another plausible valid move is
    // (3,4), (2,3), (2,2), (1,1) but (1,1) is adjacent to the top left corner
    Assert.assertEquals(MoveRules.applyShiftBasedOnDirection(1,1, MoveDirection.UPRIGHT),
            new ArrayList<>(Arrays.asList(2,0)));
    rhmai.makeMove(5,2);
  }

  @Test
  public void testAIPassesAvoidCorners() {
    ReversiModel model = new ReversiHexModelAI(StrategyType.AVOIDCORNER);
    model.startGame(7);
    model.makeMove(2,4);
    model.makeMove(5,4);
    model.makeMove(3,6);
  }

}
