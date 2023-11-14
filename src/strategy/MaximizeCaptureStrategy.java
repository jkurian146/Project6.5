package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.BoardUtils;
import model.ReadOnlyReversiModel;
import player.Player;
import player.PlayerTurn;

public class MaximizeCaptureStrategy extends AbstractStrategy {

  private final StrategyType strategyType;
  public MaximizeCaptureStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    super(reversiModel,player);
    this.strategyType = StrategyType.MAXIMIZE;
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
    HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap = new HashMap<>();
    List<List<List<Integer>>> validPositions = getPositionsForBFS();

    for (List<List<Integer>> innerList : validPositions) {
      // this inner list will contain all adjacent cells to a non-empty opposite color
      for (List<Integer> position : innerList) {
        List<List<List<Integer>>> moveFromPosition = BoardUtils.bfs(this.reversiModel,
                position.get(0), position.get(1));
        // add a position and the move set to the map if the position doesn't exist
        if (positionMoveMap.get(position) == null) {
          positionMoveMap.put(position, moveFromPosition);
        } else {
          // if a position doesn't exist get from the map and calculate which one is the longest first in case of tie
          // choose the most up-left
          List<List<List<Integer>>> moveInMap = positionMoveMap.get(position);
          int lengthOfMoveInMap = getLengthOfMove(moveInMap);
          int lengthOfMoveFromPos = getLengthOfMove(moveFromPosition);
          if (lengthOfMoveFromPos > lengthOfMoveInMap) {
            positionMoveMap.put(position, moveFromPosition);
          } else if (lengthOfMoveFromPos == lengthOfMoveInMap) {
            List<Integer> mapMoveMostUpLeft = getUpLeftMostInMove(moveInMap);
            List<Integer> posMoveMostUpLeft = getUpLeftMostInMove(moveFromPosition);
            int mapX = mapMoveMostUpLeft.get(0);
            int mapY = mapMoveMostUpLeft.get(1);
            int currX = posMoveMostUpLeft.get(0);
            int currY = posMoveMostUpLeft.get(1);
            if (currX < mapX || (currX == mapX && currY < mapY)) {
              positionMoveMap.put(position, moveFromPosition);
            }
          }

        }
      }
    }
    return getLongestAndMostUpLeftFromMap(positionMoveMap);
  }

  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }


}