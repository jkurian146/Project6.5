package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import discs.Disc;
import discs.DiscColor;
import model.MoveDirection;
import model.MoveRules;
import model.ReadOnlyReversiModel;
import player.PlayerTurn;

public abstract class AbstractStrategy implements IStrategy {

  protected final ReadOnlyReversiModel reversiModel;
  protected final PlayerTurn player;
  protected boolean isAvoidCorners;
  private HashMap<List<Integer>, Integer> coordinateMap = new HashMap<>();

  public AbstractStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player) {
    this.reversiModel = reversiModel;
    this.player = player;
  }

  public AbstractStrategy(ReadOnlyReversiModel reversiModel, PlayerTurn player, boolean isAvoidCorners) {
    this.reversiModel = reversiModel;
    this.player = player;
    this.isAvoidCorners = isAvoidCorners;
  }

  protected List<List<Integer>> getPositionsForBFS() {
    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < this.reversiModel.getDimensions(); i++) {
      for (int j = 0; j < this.reversiModel.getDimensions(); j++) {
        try {
          DiscColor currentDiscColor = this.reversiModel.getDiscAt(i,j).getColor();
          if (currentDiscColor == DiscColor.FACEDOWN) {
            System.out.println("X: " + i + " Y: " + j);
            res.add(new ArrayList<>(Arrays.asList(i,j)));
          }
        } catch (IllegalArgumentException iae) {
          // we encountered a null cell do nothing
        }
      }
    }
    return res;
  }

  private DiscColor getOppositeColor(DiscColor discColor) {
    switch (discColor) {
      case FACEDOWN:
        return DiscColor.FACEDOWN;
      case WHITE:
        return DiscColor.BLACK;
      case BLACK:
        return DiscColor.WHITE;
    }
    return null;
  }

  private List<List<Integer>> getAllAdjacent(int i, int j)  {
    List<List<Integer>> res = new ArrayList<>();
    for (MoveDirection moveDir : MoveDirection.values()) {
      List<Integer> currCoordinate = MoveRules.applyShiftBasedOnDirection(i, j, moveDir);
      int x = currCoordinate.get(0);
      int y = currCoordinate.get(1);
      if (this.coordinateMap.get(new ArrayList<>(Arrays.asList(x, y))) == null) {
        this.coordinateMap.put(new ArrayList<>(Arrays.asList(x, y)), 1);
        try {
          Disc currDisc = this.reversiModel.getDiscAt(x, y);
          if (currDisc.getColor() == DiscColor.FACEDOWN) {
            res.add(new ArrayList<>(Arrays.asList(x, y)));
          }
        } catch (IllegalArgumentException iae) {

        }
      }
    }
    return res;
  }

  protected int getLengthOfMove(List<List<List<Integer>>> move) {
    int length = 0;
    for (List<List<Integer>> innerList : move) {
        length += innerList.size();
    }
    return length;
  }

  protected List<Integer> getLongestAndMostUpLeftFromMap(HashMap<List<Integer>, List<List<List<Integer>>>> positionMoveMap) {
    List<List<Integer>> res = new ArrayList<>();
    int largestMoveLength = Integer.MIN_VALUE;
    for (Map.Entry<List<Integer>, List<List<List<Integer>>>> entry : positionMoveMap.entrySet()) {
      int currMoveLength = this.getLengthOfMove(entry.getValue());
      if (currMoveLength > largestMoveLength) {
        largestMoveLength = currMoveLength;
        res.clear();
        res.add(entry.getKey());
      } else if (currMoveLength == largestMoveLength) {
        res.add(entry.getKey());
      }
    }
    // get up most left if number of rows is more than 1
    if (res.size() == 1) {
      return res.get(0);
    } else {
      int mostLeftX = Integer.MAX_VALUE;
      int mostLeftY = Integer.MAX_VALUE;
      for (List<Integer> pos: res) {
        int currX = pos.get(0);
        int currY = pos.get(1);
        if (currX < mostLeftX || (currX == mostLeftX && currY < mostLeftY)) {
          mostLeftX = currX;
          mostLeftY = currY;
        }
      }
      return new ArrayList<>(Arrays.asList(mostLeftX,mostLeftY));
    }
  }
  protected List<Integer> getUpLeftMostInMove(List<List<List<Integer>>> move) {
    int mostLeftX = Integer.MAX_VALUE;
    int mostLeftY = Integer.MAX_VALUE;
    for (List<List<Integer>> inner : move) {
      for (List<Integer> position : inner) {
        int currX = position.get(0);
        int currY = position.get(1);
        if (currX < mostLeftX || (currX == mostLeftX && currY < mostLeftY)) {
          mostLeftX = currX;
          mostLeftY = currY;
        }
      }
    }
    return new ArrayList<>(Arrays.asList(mostLeftX,mostLeftY));
  }
}
