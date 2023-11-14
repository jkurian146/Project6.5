package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import discs.DiscColor;

public class BoardUtils {


  public static List<List<List<Integer>>> bfs(ReadOnlyReversiModel rorm, int destX, int destY) {
    List<List<List<Integer>>> res = new ArrayList<>();
    res.add(bfsHelper(rorm,destX,destY,MoveDirection.LEFT,new ArrayList<>(), true));
    res.add(bfsHelper(rorm,destX,destY,MoveDirection.RIGHT, new ArrayList<>(), true));
    res.add(bfsHelper(rorm,destX,destY,MoveDirection.UPLEFT, new ArrayList<>(), true));
    res.add(bfsHelper(rorm,destX,destY,MoveDirection.UPRIGHT, new ArrayList<>(), true));
    res.add(bfsHelper(rorm,destX,destY,MoveDirection.DOWNLEFT, new ArrayList<>(), true));
    res.add(bfsHelper(rorm,destX,destY,MoveDirection.DOWNRIGHT, new ArrayList<>(), true));
    return res;
  }

  // A helper for bfs that determines coordinates for a move in a certain direction
  // returns an empty list if there are no moves for that direction
  private static List<List<Integer>> bfsHelper(ReadOnlyReversiModel rorm,int x, int y, MoveDirection moveDirection,
                                        List<List<Integer>> res, boolean firstPass) {

    while (true) {
      res.add(Arrays.asList(x,y));
      // class invariant: only the current player can alter the board (make a move)
      // the class invariant is enforced here because we are getting a color
      // purely based on which player turn it currently is.
      DiscColor playerTurnColor = rorm.getPlayerColor(rorm.currentTurn());
      List<Integer> nextPos = MoveRules.applyShiftBasedOnDirection(x,y,moveDirection);
      int nextPosX = nextPos.get(0);
      int nextPosY = nextPos.get(1);
      x = nextPosX;
      y = nextPosY;
      if (!rorm.checkValidCoordinates(nextPosX, nextPosY)) {
        return new ArrayList<>();
      }
      if (firstPass) {
        DiscColor opponentTurnColor = rorm.getPlayerColor(rorm.getOpponent(rorm.currentTurn()));
        if (opponentTurnColor == rorm.getDiscAt(nextPosX, nextPosY).getColor()) {
          res.add(Arrays.asList(x, y));
          res.add(Arrays.asList(nextPosX, nextPosY));
        } else {
          return new ArrayList<>();
        }
        firstPass = false;
      } else {
        if (rorm.getDiscAt(nextPosX,nextPosY).getColor() == playerTurnColor) {
          return res;
        } else if (rorm.getDiscAt(nextPosX,nextPosY).getColor() == DiscColor.FACEDOWN) {
          return new ArrayList<>();
        } else {
          res.add(Arrays.asList(nextPosX,nextPosY));
        }
      }
    }
  }

}
