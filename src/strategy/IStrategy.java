package strategy;

import java.util.List;

import model.ReadOnlyReversiModel;

public interface IStrategy {

  List<Integer> executeStrategy();

  StrategyType getStrategyType();

}
