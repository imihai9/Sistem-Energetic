package updates;

import entities.Consumer;

import java.util.List;

public final class MonthlyUpdate {
    private final List<DistributorChange> distributorChanges;
    private final List<ProducerChange> producerChanges;
    private List<Consumer> newConsumers;

    public MonthlyUpdate(final List<Consumer> newConsumers,
                         final List<DistributorChange> distributorChanges,
                         final List<ProducerChange> producerChanges) {
        this.newConsumers = newConsumers;
        this.distributorChanges = distributorChanges;
        this.producerChanges = producerChanges;
    }

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<Consumer> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<DistributorChange> getDistributorChanges() {
        return distributorChanges;
    }

    public List<ProducerChange> getProducerChanges() {
        return producerChanges;
    }
}
