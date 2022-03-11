package strategies;

import entities.Producer;

import java.util.List;

public interface EnergyChoiceStrategy {
    List<Producer> chooseProducers();
}
