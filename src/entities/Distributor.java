package entities;

import fileio.input.DistributorInput;
import strategies.EnergyChoiceStrategyType;
import utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Distributor extends NetworkEntity implements Observer {
    private final int energyNeededKW;
    private final List<Contract> contractList;
    private List<Producer> producers;

    private int contractLength;
    private int infrastructureCost;
    private int productionCost;
    private int contractCost;
    private EnergyChoiceStrategyType producerStrategyType;

    private boolean pickProducer; // boolean flag, active if the current distributor
    // is supposed to choose a new producer in the current round

    public Distributor(final DistributorInput distributorInput) {
        bankrupt = false;
        contractCost = 0;
        contractList = new ArrayList<>();

        this.id = distributorInput.getId();
        this.budget = distributorInput.getInitialBudget();
        this.energyNeededKW = distributorInput.getEnergyNeededKW();
        this.contractLength = distributorInput.getContractLength();
        this.infrastructureCost = distributorInput.getInitialInfrastructureCost();
        this.productionCost = 0;
        this.producerStrategyType = distributorInput.getProducerStrategy();

        this.pickProducer = false;
    }

    // Getters & setters

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public List<Producer> getProducers() {
        return Collections.unmodifiableList(producers);
    }

    public void setProducers(List<Producer> producers) {
        this.producers = producers;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public int getContractCost() {
        return contractCost;
    }

    public List<Contract> getContractList() {
        return Collections.unmodifiableList(contractList);
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategyType;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategyType = producerStrategy;
    }

    /**
     * Sets the internal "must pick a producer" flag to true
     */
    public void setPickProducer() {
        this.pickProducer = true;
    }

    /**
     * Sets the internal "must pick a producer" flag to false
     */
    public void unsetPickProducer() {
        this.pickProducer = false;
    }

    public boolean getPickProducer() {
        return pickProducer;
    }

    @Override
    public void update() {
        setPickProducer();
    }

    // Methods

    /**
     * Adds contract to the distributor's contract list
     *
     * @param contract the given contract
     */
    public void addContract(final Contract contract) {
        contractList.add(contract);
    }

    /**
     * Removes contract from the distributor's contract list
     *
     * @param contract the given contract
     */
    public void removeContract(final Contract contract) {
        this.contractList.remove(contract);
    }

    /**
     * @return the profit
     */
    public int calculateProfit() {
        return (int) Math.round(Math.floor(Constants.PROFIT_FACTOR * productionCost));
    }

    /**
     * Generates the new production cost for the current distributor
     */
    public void calculateProductionCost() {
        double cost = 0;
        for (Producer producer : producers) {
            cost = cost + producer.getEnergyPerDistributor() * producer.getPriceKW();
        }

        this.productionCost = (int) Math.round(Math.floor(cost / Constants.PROD_COST_FACTOR));
    }

    /**
     * Wrapper function for contract cost calculation
     */
    public void calculateContractCost() {
        if (contractList.isEmpty()) {
            this.contractCost = calculateInitialContractCost();
        } else {
            this.contractCost = calculateFinalContractCost();
        }
    }

    /**
     * Calculates contract cost (for no clients)
     */
    private int calculateInitialContractCost() {
        return infrastructureCost + productionCost + this.calculateProfit();
    }

    /**
     * Calculates contract cost (for number of clients not zero)
     */
    private int calculateFinalContractCost() {
        return (int) Math.round(Math.floor((float) infrastructureCost / contractList.size())
                + productionCost + this.calculateProfit());
    }

    public void payInfrastructureCost() {
        this.budget -= infrastructureCost;
    }

    public void payProductionCost() {
        this.budget -= productionCost * contractList.size();
    }

    public void receiveClientsPayment(final int totalMonthPayments) {
        this.budget += totalMonthPayments;
    }

    /**
     * Declares bankruptcy; destroys contracts it has with its customers
     */
    public void declareBankruptcy() {
        this.bankrupt = true;
        Iterator<Contract> it = this.contractList.iterator();
        while (it.hasNext()) {
            Contract contract = it.next();

            // Remove contract from the consumers
            contract.getConsumer().nullifyContract();
            it.remove();
        }
    }

    /**
     * Process payments received from consumers, pay fees and possibly declare bankruptcy.
     */
    public void processPayments() {
        // List of ids of consumers that will be disconnected
        List<Integer> idsToDisconnect = new ArrayList<>();

        int totalPayments = 0;

        // Verify clients
        for (Contract contract : this.getContractList()) {
            Consumer consumer = contract.getConsumer();

            // To pass warning, even though we're sure the consumer exist
            if (consumer == null) {
                continue;
            }

            boolean hasPostponed = consumer.hasPostponed();
            boolean hasPaid = consumer.payContractCost();

            if (hasPaid) {
                if (hasPostponed) {
                    totalPayments += (int) Math.round(Math.floor(Constants.POSTPONED_FACTOR
                            * contract.getPrice()));
                } else {
                    totalPayments += contract.getPrice();
                }
            } else {
                if (consumer.isBankrupt()) {
                    consumer.nullifyContract();
                    idsToDisconnect.add(consumer.getId());
                }
            }
        }

        // Distributor receives fees from clients
        this.receiveClientsPayment(totalPayments);

        // Distributor pays costs
        this.payInfrastructureCost();
        this.payProductionCost();

        this.contractList
                .removeIf(contract -> idsToDisconnect.contains(contract.getConsumer().getId()));

        if (this.getBudget() < 0) {
            this.setBankrupt(true);
        }
    }
}
