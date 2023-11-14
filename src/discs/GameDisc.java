package discs;

import java.util.Objects;

/**
 * A GameDisc is a single cell on a reversi board.
 * When Implemented it has a color and a type
 */
public class GameDisc implements Disc {
  private final DiscType type;
  private DiscColor color;

  public GameDisc(DiscType type, DiscColor tileColor) {
    this.type = type;
    this.color = tileColor;
  }

  @Override
  public DiscColor getColor() {
    return this.color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    GameDisc other = (GameDisc) o;
    return  other.color == this.color && other.type == this.type;

  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.type);
  }
}