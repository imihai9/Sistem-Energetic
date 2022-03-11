package factories;

import entities.Distributor;
import entities.Producer;
import strategies.EnergyChoiceStrategy;
import strategies.EnergyChoiceStrategyType;
import strategies.GreenStrategy;
import strategies.PriceStrategy;
import strategies.QuantityStrategy;

import java.util.List;

public final class EnergyChoiceStrategyFactory {
    private EnergyChoiceStrategyFactory() {
    }

    /**
     * Factory method for creating an EnergyChoice Strategy
     */
    public static EnergyChoiceStrategy createStrategy(EnergyChoiceStrategyType strategyType,
                                                      List<Producer> producerList,
                                                      Distributor distributor) {
        if (strategyType.equals(EnergyChoiceStrategyType.GREEN)) {
            return new GreenStrategy(producerList, distributor);
        } else if (strategyType.equals(EnergyChoiceStrategyType.PRICE)) {
            return new PriceStrategy(producerList, distributor);
        } else if (strategyType.equals(EnergyChoiceStrategyType.QUANTITY)) {
            return new QuantityStrategy(producerList, distributor);
        }

        return null;
    }
}
