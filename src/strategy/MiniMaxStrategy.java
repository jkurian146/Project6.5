package strategy;

import java.util.List;
import model.ReadOnlyReversiModel;
import model.ReversiModel;
import player.Player;
import player.PlayerTurn;

public class MiniMaxStrategy extends AbstractStrategy {

  private final StrategyType strategyType;
  public MiniMaxStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    super(reversiModel,player);

    this.strategyType = StrategyType.MINIMAX;
  }

  @Override
  public List<Integer> executeStrategy() {
    List<ReadOnlyReversiModel> gameStates = this.reversiModel.getGameStates();
    ReadOnlyReversiModel getLastState = gameStates.get(gameStates.size() - 2);
    List<List<Integer>> moves = this.reversiModel.getMoves();
    List<Integer> lastMove = moves.get(moves.size() - 1);
    if (lastMove.equals(executeMaximize(getLastState))) {

    } else if (lastMove.equals(executeAvoidCorners(getLastState))) {
      executeAvoidCorners(this.reversiModel);
    } else if (lastMove.equals(executeGoForCorner(getLastState))) {
      executeGoForCorner(this.reversiModel);
    } else {
      executeMaximize(this.reversiModel);
    }
    return null;
  }

  private static List<Integer> executeGoForCorner(ReadOnlyReversiModel reversiModel) {
    CornersStrategy goForCorner = new CornersStrategy(reversiModel,reversiModel.currentTurn(), false);
    return goForCorner.executeStrategy();
  }

  private static List<Integer> executeAvoidCorners(ReadOnlyReversiModel reversiModel) {
    CornersStrategy avoidCorner = new CornersStrategy(reversiModel,reversiModel.currentTurn(), true);
    return avoidCorner.executeStrategy();
  }

  private static List<Integer> executeMaximize(ReadOnlyReversiModel reversiModel) {
    MaximizeCaptureStrategy maximizeCaptureStrategy = new MaximizeCaptureStrategy(reversiModel,
            reversiModel.currentTurn());
    return maximizeCaptureStrategy.executeStrategy();
  }
  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }


}