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

public class MaximizeCaptureStrategyMock extends AbstractStrategy {
  public HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap;
  private final StrategyType strategyType;
  public ReadOnlyReversiModel reversiModel;
  public PlayerTurn player;


  public MaximizeCaptureStrategyMock(ReadOnlyReversiModel reversiModel) {
    super(reversiModel, reversiModel.currentTurn());
    this.strategyType = StrategyType.MAXIMIZE;
    this.positionMoveMap = new HashMap<>();
    this.reversiModel = reversiModel;

  }

  @Override
  public List<Integer> executeStrategy() {
    HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap = new HashMap<>();
    List<List<Integer>> validPositions = getPositionsForBFS();

    for (List<Integer> position : validPositions) {
      List<List<List<Integer>>> moveFromPosition = BoardUtils.bfs(this.reversiModel,
              position.get(0), position.get(1));
      // add a position and the move set to the map if the position doesn't exist
      if (positionMoveMap.get(position) == null) {
        positionMoveMap.put(position, moveFromPosition);
      } else {
        // if a position doesn't exist get from the map and calculate which one
        // is the longest first in case of tie, choose the most up-left
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
    this.positionMoveMap = positionMoveMap;
    return getLongestAndMostUpLeftFromMap(positionMoveMap);
  }

  public String availableMoves() {
    this.executeStrategy();
    StringBuilder result = new StringBuilder();

    for (Map.Entry<List<Integer>, List<List<List<Integer>>>> entry : this.positionMoveMap.entrySet()) {
      List<List<Integer>> seenCoordinates = new ArrayList<>();
      List<Integer> move = entry.getKey();

      for (List<List<Integer>> list : entry.getValue()) {
        for (List<Integer> coordinate : list) {
          if (!containsCoordinate(seenCoordinates, coordinate)) {
            seenCoordinates.add(coordinate);
          }
        }
      }

      int discsCaptured = seenCoordinates.size();

      // Append only if discs captured is greater than 0
      if (discsCaptured > 0) {
        result.append("Move: ").append(move.toString())
                .append(", discs Captured: ").append(discsCaptured);

        // Append each captured move
        result.append(", Captured Moves: [");
        for (List<Integer> capturedMove : seenCoordinates) {
          result.append(capturedMove.toString()).append(", ");
        }
        // Remove the last comma and space, and close the bracket
        if (!seenCoordinates.isEmpty()) {
          result.setLength(result.length() - 2); // Remove the last ", "
        }
        result.append("]\n");
      }
    }

    return result.toString().trim();
  }

  private boolean containsCoordinate(List<List<Integer>> seenCoordinates, List<Integer> coordinate) {
    for (List<Integer> seenCoordinate : seenCoordinates) {
      if (seenCoordinate.get(0).equals(coordinate.get(0)) && seenCoordinate.get(1).equals(coordinate.get(1))) {
        return true;
      }
    }
    return false;
  }



  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }

}