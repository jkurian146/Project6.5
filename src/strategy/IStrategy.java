package strategy;

import java.util.List;

public interface IStrategy {
  boolean equals();
  int hashCode();
  List<Integer> executeStrategy();

  StrategyType getStrategyType();
}
