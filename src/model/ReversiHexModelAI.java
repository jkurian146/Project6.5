package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import discs.Disc;
import discs.DiscColor;
import discs.DiscType;
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

  public ReversiHexModelAI(StrategyType strategyType) {
    super();
    this.strategyType = strategyType;
    this.player1 = new Player(PlayerTurn.PLAYER1);
    //this.player2 = createAI();
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
    this.moveNonAi(x,y);
    this.moveAi();
  }

  private void moveNonAi(int x, int y) {
    super.makeMove(x,y);
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
      pass();
    } else {
      super.makeMove(aiMove.get(0),aiMove.get(1));
    }
  }
  @Override
  public void pass() {
    if (super.pt == this.player1.getPlayerTurn()) {
      super.pt = this.player2.getPlayerTurn();
    } else {
      super.pt = this.player1.getPlayerTurn();
    }
  }
}
