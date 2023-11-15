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
        if (!moveIsAdjacentToCorner(moveFromPosition,this.cornerMap) && this.strategyType == StrategyType.AVOIDCORNER) {
          this.positionMoveMap.put(position,moveFromPosition);
        }
        // go for corners
        else if (this.strategyType == StrategyType.GOFORCORNER) {
          List<List<List<Integer>>> moveInMap = this.positionMoveMap.get(position);
          if (moveInMap == null) {
            this.positionMoveMap.put(position,moveFromPosition);
          } else {
            int mapClosest = getClosestCoordinateToCorner(moveInMap,this.cornerMap);
            int currClosest = getClosestCoordinateToCorner(moveFromPosition,this.cornerMap);
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
            getLongestAndMostUpLeftFromMap(this.positionMoveMap) :
            getMoveWithClosestCoordinateFromMap(this.positionMoveMap,this.cornerMap);
  }


  @Override
  public StrategyType getStrategyType() {
    return this.strategyType;
  }

}