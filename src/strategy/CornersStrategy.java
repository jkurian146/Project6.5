package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import model.BoardUtils;
import model.ReadOnlyReversiModel;
import player.Player;
import player.PlayerTurn;


public class CornersStrategy extends AbstractStrategy {
  private final HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap;
  private final List<List<List<Integer>>> positionsForBfs;
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
    int firstRow = 0;
    int middle = reversiModel.getDimensions() / 2;
    int lastRow = reversiModel.getDimensions() - 1;
    HashMap<Integer,List<Integer>> res = new HashMap<>();
    for (int i = 0; i < reversiModel.getDimensions(); i++) {
      for (int j = 0; j < reversiModel.getDimensions(); j++) {
        if (i == firstRow || i == lastRow) {
          if (j != 0) {
           if ((reversiModel.getDiscAt(i,j-1) == null && reversiModel.getDiscAt(i,j) != null) ||
                   (reversiModel.getDiscAt(i,j+1) == null && reversiModel.getDiscAt(i,j) != null)) {
             ArrayList<Integer> corner = new ArrayList<>(Arrays.asList(i,j));
             res.put(1,corner);
           }
          }
        }
        else if (i == middle && (j == 0 || j == reversiModel.getDimensions() - 1)) {
          ArrayList<Integer> corner = new ArrayList<>(Arrays.asList(i,j));
          res.put(1,corner);
        }
      }
    }
    return res;
  }
  @Override
  public List<Integer> executeStrategy() {
    for (List<List<Integer>> innerList: this.positionsForBfs) {
      for (List<Integer> position: innerList) {
        List<List<List<Integer>>> moveFromPosition = BoardUtils.bfs(reversiModel,
                position.get(0),position.get(1));
        // if the move doesn't contain a corner and we want to avoid corners we want to add it
        if (!moveContainsCorners(moveFromPosition) && this.isAvoidCorners) {
          this.updateMap(position,moveFromPosition);
        }
        // if the move contains corners and we don't want to avoid corners we want to add it
        else if (moveContainsCorners(moveFromPosition) && !this.isAvoidCorners) {
          this.updateMap(position,moveFromPosition);
        }
      }
    }
    return getLongestAndMostUpLeftFromMap(this.positionMoveMap);
  }

  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }

  private void updateMap(List<Integer> pos ,List<List<List<Integer>>> move) {
    if (this.positionMoveMap.get(pos) == null) {
      this.positionMoveMap.put(pos,move);
    } else {
      List<Integer> moveInMapUpLeftMost = getUpLeftMostInMove(this.positionMoveMap.get(pos));
      List<Integer> currMoveUpLeftMost = getUpLeftMostInMove(move);
      int mapX = moveInMapUpLeftMost.get(0);
      int mapY = moveInMapUpLeftMost.get(1);
      int currX = currMoveUpLeftMost.get(0);
      int currY = currMoveUpLeftMost.get(1);
      if (currX < mapX || (currX == mapX && currY < mapY)) {
        positionMoveMap.put(pos,move);
      }
    }
  }
  private boolean moveContainsCorners(List<List<List<Integer>>> moveFromPosition) {
    for (List<List<Integer>> inner: moveFromPosition) {
      for (List<Integer> pos: inner) {
        if (posInMap(pos)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean posInMap(List<Integer> pos) {
    for (List<Integer> val: this.cornerMap.values()) {
      if (pos.equals(val)) {
        return true;
      }
    }
    return false;
  }
}
