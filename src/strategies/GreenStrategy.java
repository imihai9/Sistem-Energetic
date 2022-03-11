package strategies;

import comparators.RenewableEnergyComparator;
import entities.Distributor;
import entities.Producer;
import utils.Utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class GreenStrategy implements EnergyChoiceStrategy {
    private final List<Producer> producerList;
    private final Distributor distributor;

    public GreenStrategy(List<Producer> producerList, Distributor distributor) {
        this.producerList = producerList;
        this.distributor = distributor;
    }

    /**
     * Filters the initial producers list to exclude the ones with maxDistributors reached
     * Sorts the producers by renewable energy, then by price, quantity, id
     *
     * @return - a list with the first few producers that satisfy the distributor's needs
     */
    @Override
    public List<Producer> chooseProducers() {
        List<Producer> sortedProducers = producerList.stream()
                .filter(p -> p.getDistributors().size() < p.getMaxDistributors())
                .sorted(new RenewableEnergyComparator()
                        .thenComparing(Producer::getPriceKW)
                        .thenComparing(Comparator.comparingInt(Producer::getEnergyPerDistributor)
                                .reversed())
                        .thenComparing(Producer::getId))
                .collect(Collectors.toList());

        return Utils.selectProducersByNeededKW(sortedProducers, distributor.getEnergyNeededKW());
    }
}
