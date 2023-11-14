package strategy;

import java.util.List;
import model.ReadOnlyReversiModel;
import player.Player;
import player.PlayerTurn;

public class MiniMaxStrategy extends AbstractStrategy {

  private final StrategyType strategyType;
  public MiniMaxStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    super(reversiModel,player);
    this.strategyType = StrategyType.MINIMAX;
  }
  @Override
  public boolean equals() {
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public List<Integer> executeStrategy() {
    List<List<List<Integer>>> validPositions = getPositionsForBFS();
    return null;
  }

  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }


}