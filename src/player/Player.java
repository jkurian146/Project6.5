package player;

import java.util.HashMap;
import java.util.Map;

import discs.DiscColor;
import strategy.IStrategy;

public class Player {
  private final PlayerTurn playerTurn;
  private IStrategy iStrategy;
  private final Map<PlayerTurn, DiscColor> playerColorMap;
  public Player(PlayerTurn playerTurn) {
    this.playerTurn = playerTurn;
    this.playerColorMap = setUpColorMap();

  }
  public Player(PlayerTurn playerTurn, IStrategy iStrategy) {
    this.playerTurn = playerTurn;
    this.iStrategy = iStrategy;
    this.playerColorMap = setUpColorMap();
  }

  private Map<PlayerTurn, DiscColor> setUpColorMap() {
    HashMap<PlayerTurn, DiscColor> temp = new HashMap<>();
    temp.put(PlayerTurn.PLAYER1, DiscColor.BLACK);
    temp.put(PlayerTurn.PLAYER2, DiscColor.WHITE);
    return temp;
  }

  public PlayerTurn getPlayerTurn() {
    return this.playerTurn;
  }

  public DiscColor getPlayerColor() {
    return this.playerColorMap.get(this.getPlayerTurn());
  }
  public IStrategy getIStrategy() {
    return this.iStrategy;
  }
}
