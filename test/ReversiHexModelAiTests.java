import org.junit.Assert;
import org.junit.Test;

import discs.DiscColor;
import discs.DiscType;
import model.ReversiHexModel;
import model.ReversiHexModelAI;
import strategy.StrategyType;
import view.ReversiGUI;

public class ReversiHexModelAiTests {

  @Test
  public void testSetUpMirror() {

  }

  // test that MaximizeStrat works
  // test that MaximizeStrat select up-left
  // test that MaximizeStrat passes when no moves left
  @Test
  public void testMaximizeSelectsLargestLeft() {
    ReversiHexModelAI rhmai = new ReversiHexModelAI(StrategyType.MAXIMIZE);
    rhmai.startGame(7);
    rhmai.makeMove(2, 2);
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(1,2).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(2,2).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(3,2).getColor());
    Assert.assertEquals(DiscColor.WHITE, rhmai.getDiscAt(4,2).getColor());
  }

  @Test
  public void testMaximizeSelections() {
    ReversiHexModelAI rhmai = new ReversiHexModelAI(StrategyType.MAXIMIZE);

  }

  @Test
  public void testMaximizePassesNoMoves() {
    ReversiHexModelAI rhmai = new ReversiHexModelAI(StrategyType.AVOIDCORNER);
    rhmai.startGame(7);
    rhmai.makeMove(2, 2);
  }
}
