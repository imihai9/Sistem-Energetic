package fileio.input;

import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import updates.MonthlyUpdate;

import java.util.Collections;
import java.util.List;

public final class Input {
    // initial data
    private final int numberOfTurns;
    private final List<Consumer> consumerList;
    private final List<Distributor> distributorList;
    private final List<Producer> producerList;

    // update data
    private final List<MonthlyUpdate> monthlyUpdateList;

    public Input() {
        numberOfTurns = 0;
        consumerList = null;
        distributorList = null;
        producerList = null;
        monthlyUpdateList = null;
    }

    public Input(final int numberOfTurns, final List<Consumer> consumerList,
                 final List<Distributor> distributorList,
                 final List<Producer> producerList,
                 final List<MonthlyUpdate> monthlyUpdateList) {
        this.numberOfTurns = numberOfTurns;
        this.consumerList = consumerList;
        this.distributorList = distributorList;
        this.producerList = producerList;
        this.monthlyUpdateList = monthlyUpdateList;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public List<Consumer> getConsumerList() {
        assert consumerList != null;
        return Collections.unmodifiableList(consumerList);
    }

    public void addToConsumerList(final Consumer consumer) {
        assert consumerList != null;
        consumerList.add(consumer);
    }

    public List<Distributor> getDistributorList() {
        assert distributorList != null;
        return Collections.unmodifiableList(distributorList);
    }

    public List<Producer> getProducerList() {
        assert producerList != null;
        return Collections.unmodifiableList(producerList);
    }

    public List<MonthlyUpdate> getMonthlyUpdateList() {
        return monthlyUpdateList;
    }

}
