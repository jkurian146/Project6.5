package strategy;

public class ChainStrategy {
  private final IStrategy curr;
  private final IStrategy next;
  public ChainStrategy(IStrategy curr, IStrategy next) {
    this.curr = curr;
    this.next = next;
  }
}
