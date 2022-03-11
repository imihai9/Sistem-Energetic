package utils;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class Utils {
    private Utils() {
    }

    /**
     * @param distributorsList - the given distributors list
     * @return - the distributor with the lowest cost
     */
    public static Distributor getMinDistributor(final List<Distributor> distributorsList) {
        return Collections.min(distributorsList,
                Comparator.comparing(Distributor::getContractCost));
    }

    /**
     * @param producers - the given producers list
     * @param neededKW  - the energy KW goal
     * @return - a list with the first few producers that can produce the given 'neededKW'
     */
    public static List<Producer> selectProducersByNeededKW(List<Producer> producers, int neededKW) {
        List<Producer> chosenProducers = new ArrayList<>();
        int currentEnergyKW = 0;

        Iterator<Producer> it = producers.iterator();

        while (currentEnergyKW < neededKW && it.hasNext()) {
            Producer currentProd = it.next();
            chosenProducers.add(currentProd);
            currentEnergyKW += currentProd.getEnergyPerDistributor();
        }

        return chosenProducers;
    }
}
