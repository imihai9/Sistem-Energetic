package factories;

import entities.Distributor;
import fileio.input.DistributorInput;

public final class DistributorFactory {
    private static DistributorFactory instance = null;

    private DistributorFactory() {
    }

    public static DistributorFactory getInstance() {
        if (instance == null) {
            instance = new DistributorFactory();
        }
        return instance;
    }

    public Distributor createDistributor(final String entityType,
                                         final DistributorInput distributorInput) {
        if (entityType.equals("basic distributor")) {
            return new Distributor(distributorInput);
        } else {
            // maybe there are different type of customers in phase 2?
            return null;
        }
    }
}
