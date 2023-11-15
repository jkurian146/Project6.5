
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import discs.Disc;
import player.Player;
import player.PlayerTurn;
import strategy.CornersStrategy;
import strategy.IStrategy;
import strategy.MaximizeCaptureStrategy;
import strategy.MiniMaxStrategy;
import strategy.StrategyType;

public class ReversiHexModelAI extends ReversiHexModel implements ReversiModel {

  private final StrategyType strategyType;
  private final Player player1;
  private Player player2;
  private List<ReadOnlyReversiModel> gameStates;
  private List<List<Integer>> allMoves;
  private boolean firstRun;

  public ReversiHexModelAI(StrategyType strategyType) {
    super();
    this.strategyType = strategyType;
    this.player1 = new Player(PlayerTurn.PLAYER1);
    this.gameStates = new ArrayList<>();
    this.allMoves = new ArrayList<>();
    this.firstRun = true;
  }

  private Player createAI() {
    switch (this.strategyType) {
      case MINIMAX:
        return new Player(PlayerTurn.PLAYER2, new MiniMaxStrategy(this,PlayerTurn.PLAYER2));
      case MAXIMIZE:
        return new Player(PlayerTurn.PLAYER2, new MaximizeCaptureStrategy(this,PlayerTurn.PLAYER2));
      case AVOIDCORNER:
        return new Player(PlayerTurn.PLAYER2, new CornersStrategy(this,
                PlayerTurn.PLAYER2,true));
      case GOFORCORNER:
        return new Player(PlayerTurn.PLAYER2, new CornersStrategy(this,
                PlayerTurn.PLAYER2,false));
      default:
        throw new IllegalStateException("Can't Create an AI without a strategy");
    }
  }
  @Override
  public void makeMove(int x, int y) {
    if (!this.isGameOver()) {
      if (this.firstRun) {
        this.gameStates.add(this);
        this.moveNonAi(x, y);
        this.gameStates.add(this);
        this.moveAi();
        this.gameStates.add(this);
        this.firstRun = false;
      } else {
        this.moveNonAi(x, y);
        this.gameStates.add(this);
        this.moveAi();
        this.gameStates.add(this);
        this.firstRun = false;
      }
    } else {
      throw new IllegalStateException("Can't call move after game is over");
    }
  }

  private void moveNonAi(int x, int y) {
    super.makeMove(x,y);
    this.allMoves.add(new ArrayList<>(Arrays.asList(x,y)));
    if (this.player2 == null) {
      this.player2 = createAI();
    }
  }

  private void moveAi() {
    IStrategy iStrategy = this.player2.getIStrategy();
    List<Integer> aiMove = iStrategy.executeStrategy();
    boolean inBounds = true;
    try {
      this.getDiscAt(aiMove.get(0),aiMove.get(1));
    }
    catch (IllegalArgumentException iae) {
      inBounds = false;
    }
    if (!inBounds) {
      this.pass();
    } else {
      try {
        super.makeMove(aiMove.get(0), aiMove.get(1));
        this.allMoves.add(new ArrayList<>(Arrays.asList(aiMove.get(0),
                aiMove.get(1))));
      } catch (IllegalStateException | IllegalArgumentException ise) {
        this.pass();
      }
    }
  }
  @Override
  public void pass() {
    if (super.pt == this.player1.getPlayerTurn()) {
      super.pt = this.player2.getPlayerTurn();
      this.moveAi();
    } else {
      super.pt = this.player1.getPlayerTurn();
    }
    this.allMoves.add(new ArrayList<>(Arrays.asList(-1)));
    this.playerAction.append("pass\n");
  }

  @Override
  public List<ReadOnlyReversiModel> getGameStates() {
    return this.gameStates;
  }

  @Override
  public List<List<Integer>> getMoves() {
    return this.allMoves;
  }
}