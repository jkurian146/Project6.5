package view;

import java.io.IOException;
import java.util.List;
import discs.Disc;
import discs.DiscColor;
import model.ReadOnlyReversiModel;

/**
 * Represents a text view for a Reversi game.
 */
public class ReversiTextualView implements ReversiView {

  private final ReadOnlyReversiModel reversiModel;
  private Appendable appendable;

  public ReversiTextualView(ReadOnlyReversiModel reversiModel, Appendable appendable) {
    this.reversiModel = reversiModel;
    this.appendable = appendable;
  }


  @Override
  public String toString() {
    updateBoard();
    return this.appendable.toString();
  }

  // a method that updates a board.
  private void updateBoard() {
    if (!appendable.toString().isEmpty()) {
      this.appendable = new StringBuilder();
    }
    int nullCount = 0;
    for (int i = 0; i < this.reversiModel.getDimensions(); i++) {
      if (i != 0) {
        this.addToStringBuilder("\n");
      }
      for (int j = 0; j < this.reversiModel.getDimensions(); j++) {
        try {
          Disc currentDisc = this.reversiModel.getDiscAt(j, i);
          if (currentDisc.getColor() == DiscColor.BLACK) {
            this.addToStringBuilder("X ");
          } else if (currentDisc.getColor() == DiscColor.WHITE) {
            this.addToStringBuilder("O ");
          } else if (currentDisc.getColor() == DiscColor.FACEDOWN) {
            this.addToStringBuilder("- ");
          }
        } catch (IllegalArgumentException iae) {
          nullCount++;
        }
      }
    }
    String boardWithoutExtraSpace = trimOriginalSpaces();
    this.appendable = new StringBuilder(boardWithoutExtraSpace);
    String boardAdjusted = addNSpacesToFrontAndBack();
    this.appendable = new StringBuilder(boardAdjusted);
  }

  // a method that adjusts a text view by adding the desired amount of
  // spaces to the front and back
  private String addNSpacesToFrontAndBack() {
    StringBuilder res = new StringBuilder();
    List<String> lines = List.of(this.appendable.toString().split("\n"));
    for (int i = 0; i < lines.size(); i++) {
      int spacesToAdd = Math.abs((this.reversiModel.getDimensions() / 2) - i);
      String spaces = createNSpaces(spacesToAdd);
      String stringWithSpacesBeginning = spaces.concat(lines.get(i)).replace("\n","");
      String stringWithSpacesAtEnd = stringWithSpacesBeginning.concat(spaces);
      stringWithSpacesAtEnd += "\n";
      res.append(stringWithSpacesAtEnd);
    }
    return res.toString();
  }

  // helper that creates a string of n spaces.
  private String createNSpaces(int spacesToAdd) {
    return " ".repeat(Math.max(0, spacesToAdd));
  }

  // a helper that trims spaces from the end of an unadjusted text view
  private String trimOriginalSpaces() {
    StringBuilder res = new StringBuilder();
    String[] lines = this.appendable.toString().split("\n");
    for (String l: lines) {
      res.append(l.trim()).append("\n");
    }
    return res.toString();
  }

  // a method that appends text to an appendable.
  private void addToStringBuilder(String text) {
    try {
      this.appendable.append(text);
    } catch (IOException e) {
      throw new IllegalStateException("Can't write to buffer");
    }
  }

  @Override
  public void render() {
    System.out.println(this);
  }
}