import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.ReadOnlyReversiModel;
import model.ReversiHexModel;
import model.ReversiModel;
import view.ReversiTextualView;

/**
 * Test class for Reversi Textual View.
 */
public class ReversiTextualViewTests {
  ReversiModel model;
  ReversiTextualView textualView;
  StringBuilder sb;

  @Before
  public void init() {
    this.model = new ReversiHexModel();
    this.sb = new StringBuilder();
    this.textualView = new ReversiTextualView(model, sb);
  }

  // Testing the starting game state with a board size that has an odd number as its middle index.
  @Test
  public void testBoardViewWithOddMiddleInteger() {
    model.startGame(11);

    Assert.assertEquals("     - - - - - -     \n" +
            "    - - - - - - -    \n" +
            "   - - - - - - - -   \n" +
            "  - - - - - - - - -  \n" +
            " - - - - X O - - - - \n" +
            "- - - - O - X - - - -\n" +
            " - - - - X O - - - - \n" +
            "  - - - - - - - - -  \n" +
            "   - - - - - - - -   \n" +
            "    - - - - - - -    \n" +
            "     - - - - - -     \n", textualView.toString());

  }

  // Testing the starting game state with a board size that has an even number as its middle index.
  @Test
  public void testBoardViewWithAnEvenMiddleInteger() {
    model.startGame(13);

    Assert.assertEquals("      - - - - - - -      \n" +
            "     - - - - - - - -     \n" +
            "    - - - - - - - - -    \n" +
            "   - - - - - - - - - -   \n" +
            "  - - - - - - - - - - -  \n" +
            " - - - - - O X - - - - - \n" +
            "- - - - - X - O - - - - -\n" +
            " - - - - - O X - - - - - \n" +
            "  - - - - - - - - - - -  \n" +
            "   - - - - - - - - - -   \n" +
            "    - - - - - - - - -    \n" +
            "     - - - - - - - -     \n" +
            "      - - - - - - -      \n", textualView.toString());

  }

  // Testing the view of the model after making some moves, and ultimately winning the game.
  @Test
  public void testViewAfterMakingMoves() {
    model.startGame(7);
    Assert.assertEquals("   - - - -   \n" +
            "  - - - - -  \n" +
            " - - X O - - \n" +
            "- - O - X - -\n" +
            " - - X O - - \n" +
            "  - - - - -  \n" +
            "   - - - -   \n", textualView.toString());

    model.makeMove(2, 2);
    Assert.assertEquals("   - - - -   \n" +
            "  - - - - -  \n" +
            " - X X O - - \n" +
            "- - X - X - -\n" +
            " - - X O - - \n" +
            "  - - - - -  \n" +
            "   - - - -   \n", textualView.toString());

    model.makeMove(5, 2);
    Assert.assertEquals("   - - - -   \n" +
            "  - - - - -  \n" +
            " - X X O O - \n" +
            "- - X - O - -\n" +
            " - - X O - - \n" +
            "  - - - - -  \n" +
            "   - - - -   \n", textualView.toString());

    model.makeMove(6, 2);
    Assert.assertEquals("   - - - -   \n" +
            "  - - - - -  \n" +
            " - X X X X X \n" +
            "- - X - O - -\n" +
            " - - X O - - \n" +
            "  - - - - -  \n" +
            "   - - - -   \n", textualView.toString());

    model.pass();
    model.makeMove(5, 4);
    Assert.assertEquals("   - - - -   \n" +
            "  - - - - -  \n" +
            " - X X X X X \n" +
            "- - X - X - -\n" +
            " - - X X X - \n" +
            "  - - - - -  \n" +
            "   - - - -   \n", textualView.toString());

    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testTinyBoard() {
    model.startGame(5);

    Assert.assertEquals("  - - -  \n" +
            " - O X - \n" +
            "- X - O -\n" +
            " - O X - \n" +
            "  - - -  \n", textualView.toString());
  }

  @Test
  public void testHugeBoard() {
    model.startGame(25);

    Assert.assertEquals("            - - - - - - - - - - - - -            \n" +
            "           - - - - - - - - - - - - - -           \n" +
            "          - - - - - - - - - - - - - - -          \n" +
            "         - - - - - - - - - - - - - - - -         \n" +
            "        - - - - - - - - - - - - - - - - -        \n" +
            "       - - - - - - - - - - - - - - - - - -       \n" +
            "      - - - - - - - - - - - - - - - - - - -      \n" +
            "     - - - - - - - - - - - - - - - - - - - -     \n" +
            "    - - - - - - - - - - - - - - - - - - - - -    \n" +
            "   - - - - - - - - - - - - - - - - - - - - - -   \n" +
            "  - - - - - - - - - - - - - - - - - - - - - - -  \n" +
            " - - - - - - - - - - - O X - - - - - - - - - - - \n" +
            "- - - - - - - - - - - X - O - - - - - - - - - - -\n" +
            " - - - - - - - - - - - O X - - - - - - - - - - - \n" +
            "  - - - - - - - - - - - - - - - - - - - - - - -  \n" +
            "   - - - - - - - - - - - - - - - - - - - - - -   \n" +
            "    - - - - - - - - - - - - - - - - - - - - -    \n" +
            "     - - - - - - - - - - - - - - - - - - - -     \n" +
            "      - - - - - - - - - - - - - - - - - - -      \n" +
            "       - - - - - - - - - - - - - - - - - -       \n" +
            "        - - - - - - - - - - - - - - - - -        \n" +
            "         - - - - - - - - - - - - - - - -         \n" +
            "          - - - - - - - - - - - - - - -          \n" +
            "           - - - - - - - - - - - - - -           \n" +
            "            - - - - - - - - - - - - -            \n", textualView.toString());
    textualView.render();
  }
}


