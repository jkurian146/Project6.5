package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import discs.Disc;
import discs.DiscColor;
import model.BoardUtils;
import model.MoveDirection;
import model.MoveRules;
import model.ReadOnlyReversiModel;
import player.PlayerTurn;


public class CornersStrategy extends AbstractStrategy {
  private final HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap;
  private final List<List<Integer>> positionsForBfs;
  private final HashMap<Integer, List<Integer>> cornerMap;
  private final StrategyType strategyType;

  public CornersStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player, boolean isAvoidCorners) {
    super(reversiModel,player,isAvoidCorners);

    this.positionMoveMap = new HashMap<>();
    this.positionsForBfs = getPositionsForBFS();
    this.cornerMap = setUpCornerMap();
    this.strategyType = (isAvoidCorners) ? StrategyType.AVOIDCORNER : StrategyType.GOFORCORNER;
  }
  @Override
  public boolean equals() {
    return false;
  }
  private HashMap<Integer, List<Integer>> setUpCornerMap() {
    int first = 0;
    int middle = this.reversiModel.getDimensions() / 2;
    int last = this.reversiModel.getDimensions() - 1;
    HashMap<Integer, List<Integer>> res = new HashMap<>();
    int corner = 0;
    for (int i = 0; i < this.reversiModel.getDimensions(); i++) {
      for (int j = 0; j < this.reversiModel.getDimensions(); j++) {
        boolean isCorner = false;
        try {
          Disc prev = reversiModel.getDiscAt(i - 1,j);
          Disc next = reversiModel.getDiscAt(i + 1,j);
        } catch (IllegalArgumentException iae) {
          isCorner = true;
        }
        try {
          if ((j == first || j == last) && isCorner) {
            Disc curr = reversiModel.getDiscAt(i, j);
            res.put(corner, new ArrayList<>(Arrays.asList(i, j)));
            corner++;
          } else if (j == middle && isCorner) {
            Disc curr = reversiModel.getDiscAt(i, j);
            res.put(corner, new ArrayList<>(Arrays.asList(i, j)));
            corner++;
          }
        } catch (IllegalArgumentException iae) {

        }
      }
    }
    return res;
  }
  @Override
  public List<Integer> executeStrategy() {
    List<List<Integer>> validPositions = getPositionsForBFS();
      // this inner list will contain all adjacent cells to a non-empty opposite color
      for (List<Integer> position : validPositions) {
        // every possible move from a singular position
        List<List<List<Integer>>> moveFromPosition = BoardUtils.bfs(super.reversiModel,
                position.get(0), position.get(1));
        // avoid cells adjacent to corners
        if (!moveIsAdjacentToCorner(moveFromPosition) && this.strategyType == StrategyType.AVOIDCORNER) {
          this.positionMoveMap.put(position,moveFromPosition);
        }
        // go for corners
        else if (this.strategyType == StrategyType.GOFORCORNER) {
          List<List<List<Integer>>> moveInMap = this.positionMoveMap.get(position);
          if (moveInMap == null) {
            this.positionMoveMap.put(position,moveFromPosition);
          } else {
            int mapClosest = getClosestCoordinateToCorner(moveInMap);
            int currClosest = getClosestCoordinateToCorner(moveFromPosition);
            if (currClosest < mapClosest) {
              this.positionMoveMap.put(position,moveFromPosition);
            }
          }
        }
      }
    this.positionMoveMap.entrySet().removeIf(entry ->
            this.reversiModel.getDiscAt(entry.getKey().get(0), entry.getKey().get(1)).getColor()
                    != DiscColor.FACEDOWN);
    this.positionMoveMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    return (this.strategyType == StrategyType.AVOIDCORNER) ?
            getLongestAndMostUpLeftFromMap(this.positionMoveMap) : getMoveWithClosestCoordinateFromMap(this.positionMoveMap);
  }

  private List<Integer> getMoveWithClosestCoordinateFromMap(HashMap<List<Integer>,
          List<List<List<Integer>>>> positionMoveMap) {
    List<Integer> closestMoveToCorner = new ArrayList<>();
    int closestDistanceToACorner = Integer.MAX_VALUE;
    for (Map.Entry<List<Integer>, List<List<List<Integer>>>> entry : positionMoveMap.entrySet()) {
      int currX = entry.getKey().get(0);
      int currY = entry.getKey().get(1);
      for (List<Integer> corner: this.cornerMap.values()) {
        int cornerX = corner.get(0);
        int cornerY = corner.get(1);
        int xDistance = Math.abs(cornerX - currX);
        int yDistance = Math.abs(cornerY - currY);
        if (xDistance + yDistance < closestDistanceToACorner) {
          closestDistanceToACorner = xDistance + yDistance;
          closestMoveToCorner = entry.getKey();
        } else if (xDistance + yDistance == closestDistanceToACorner) {

          int mapX = entry.getKey().get(0);
          int mapY = entry.getKey().get(1);
          if (currX < mapX || (currX == mapX && currY < mapY)) {
            closestMoveToCorner = entry.getKey();
          }
        }
      }
    }
    return closestMoveToCorner;
  }

  // closest is determined by the sum of the distance from a corner
  private int getClosestCoordinateToCorner(List<List<List<Integer>>> move) {
    int closestDistanceToACorner = Integer.MAX_VALUE;
    for (List<List<Integer>> innerList: move) {
      for (List<Integer> pos: innerList) {
        int x = pos.get(0);
        int y = pos.get(1);
        for (List<Integer> corner: this.cornerMap.values()) {
          int cornerX = corner.get(0);
          int cornerY = corner.get(1);
          int xDistance = Math.abs(cornerX - x);
          int yDistance = Math.abs(cornerY - y);
          if (xDistance + yDistance < closestDistanceToACorner) {
            closestDistanceToACorner = xDistance + yDistance;
          }
        }
      }
    }
    return 0;
  }
  private boolean moveIsAdjacentToCorner(List<List<List<Integer>>> moveFromPosition) {
    for (List<List<Integer>> innerList: moveFromPosition) {
      for (List<Integer> position: innerList) {
        if (adjacentPositionInMap(position)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean adjacentPositionInMap(List<Integer> position) {
    int x = position.get(0);
    int y = position.get(1);
    for (MoveDirection md: MoveDirection.values()) {
      List<Integer> adjacentPos = MoveRules.applyShiftBasedOnDirection(x,y,md);
      if (this.cornerMap.values().contains(adjacentPos)) {
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