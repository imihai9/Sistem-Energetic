package simulation;

import entities.Consumer;
import entities.Contract;
import entities.Distributor;
import entities.Producer;
import factories.EnergyChoiceStrategyFactory;
import fileio.input.Input;
import strategies.EnergyChoiceStrategy;
import updates.DistributorChange;
import updates.MonthlyUpdate;
import updates.ProducerChange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Simulation {
    private final Input inputData;
    private final int round;

    // Lists of entities loaded and updated at each round Simulation

    private final List<Distributor> distributorsList;
    private final List<Producer> producersList;
    private List<Consumer> consumersList;

    public Simulation(final Input inputData, final int round) {
        this.inputData = inputData;
        this.round = round;

        // Getting the lists of the entities that are still in the game
        this.distributorsList = inputData.getDistributorList().stream()
                .filter(d -> !d.isBankrupt())
                .sorted(Comparator.comparingInt(Distributor::getId))
                .collect(Collectors.toList());
        this.consumersList = inputData.getConsumerList().stream()
                .filter(c -> !c.isBankrupt())
                .sorted(Comparator.comparingInt(Consumer::getId))
                .collect(Collectors.toList());

        // List of producers - sorted by their ids
        this.producersList = inputData.getProducerList().stream()
                .sorted(Comparator.comparingInt(Producer::getId))
                .collect(Collectors.toList());
    }

    /**
     * Checks users that don't have a contract / have an expired contract
     * Renews their contracts
     */
    void processInvalidContracts() {
        List<Consumer> consumersToRenew = new ArrayList<>();

        for (Consumer consumer : consumersList) {
            Contract contract = consumer.getContract();
            if (contract == null) {
                consumersToRenew.add(consumer);
            } else if (contract.getRemainedContractMonths() == 0) {
                Distributor distributor = contract.getDistributor();

                if (distributor != null) {
                    distributor.removeContract(consumer.getContract());
                }
                consumersToRenew.add(consumer);
            }
        }

        for (Consumer consumer : consumersToRenew) {
            consumer.chooseContract(distributorsList);
        }
    }

    /**
     * Destroys contracts of bankrupt distributors
     */
    void processBankruptDistributors(final List<Distributor> bankruptDistributors) {
        for (Distributor distributor : bankruptDistributors) {
            distributor.declareBankruptcy();
            removeProducers(distributor);
        }
    }

    /**
     * Update the remaining months of each contract
     */
    void monthPassed() {
        for (Distributor distributor : distributorsList) {
            for (Contract contract : distributor.getContractList()) {
                contract.monthPassed();
            }
        }
    }

    /**
     * Reads cost changes and new consumers for the current round
     */
    void readMonthlyUpdates() {
        MonthlyUpdate monthlyUpdate = inputData.getMonthlyUpdateList().get(round - 1);

        List<DistributorChange> distributorChanges = monthlyUpdate.getDistributorChanges();
        List<ProducerChange> producerChanges = monthlyUpdate.getProducerChanges();

        if (!distributorChanges.isEmpty()) {
            for (DistributorChange distribChange : distributorChanges) {
                Distributor distributor = inputData.getDistributorList().stream()
                        .filter(d -> d.getId() == distribChange.getId())
                        .findFirst()
                        .orElse(null);

                // Update changes in distributors
                if (distributor != null) {
                    distributor.setInfrastructureCost(distribChange.getInfrastructureCost());
                }
            }
        }

        if (!producerChanges.isEmpty()) {
            for (ProducerChange producerChange : producerChanges) {
                Producer producer = inputData.getProducerList().stream()
                        .filter(p -> p.getId() == producerChange.getId())
                        .findFirst()
                        .orElse(null);

                // Update changes in producers & notify distributor clients about them

                if (producer != null) {
                    producer.setEnergyPerDistributor(producerChange.getEnergyPerDistributor());
                }
            }
        }

        List<Consumer> newConsumers = monthlyUpdate.getNewConsumers();

        if (!newConsumers.isEmpty()) {
            for (Consumer consumer : newConsumers) {
                inputData.addToConsumerList(consumer);
            }

            // Update local consumer list
            this.consumersList = inputData.getConsumerList().stream()
                    .filter(c -> !c.isBankrupt())
                    .collect(Collectors.toList());
        }
    }

    void chooseProducer(Distributor distributor) {
        // Choose & add producers to current distributor
        EnergyChoiceStrategy strategy = EnergyChoiceStrategyFactory.createStrategy(
                distributor.getProducerStrategy(), producersList, distributor);

        if (strategy != null) {
            distributor.setProducers(strategy.chooseProducers());
        }

        // Add current distributor to its producers' lists
        for (Producer producer : distributor.getProducers()) {
            producer.addObserver(distributor);
        }
    }

    void removeProducers(Distributor distributor) {
        for (Producer producer : distributor.getProducers()) {
            producer.removeObserver(distributor);
        }

        // Remove old producers from distributor
        distributor.setProducers(null);
    }

    /**
     * Driver function for the round simulation
     */
    public void execute() {
        List<Distributor> bankruptDistributors = new ArrayList<>();

        if (round == 0) {
            for (Distributor distributor : distributorsList) {
                // Choosing the initial producers for each distributor
                chooseProducer(distributor);
                // Calculating the initial prod & contract costs for each distributor (no clients)
                distributor.calculateProductionCost();
                distributor.calculateContractCost();
            }

            // Choosing the contract for each customer
            for (Consumer consumer : consumersList) {
                consumer.chooseContract(distributorsList);
            }
        } else {
            // Reading updates
            readMonthlyUpdates();

            // Recalculate contract costs
            for (Distributor distributor : distributorsList) {
                distributor.calculateContractCost();
            }

            // Process bankrupt distributors
            processBankruptDistributors(bankruptDistributors);

            // Process invalid contracts (for new consumers / consumers with a bankrupt distributor)
            processInvalidContracts();
        }

        for (Consumer consumer : consumersList) {
            // Consumers receive salary
            consumer.receiveMonthlyIncome();
        }

        // List of distributors that went bankrupt this month, and their clients must
        // choose another contract next month
        bankruptDistributors.clear();

        for (Distributor distributor : distributorsList) {
            // Distributors receive the payments from the consumers
            // Distributors pay the monthly fees
            distributor.processPayments();

            // Check if distributor has gone bankrupt
            if (distributor.isBankrupt()) {
                bankruptDistributors.add(distributor);
            }
        }

        monthPassed();

        // Gets all distributors that must change their producers this round
        List<Distributor> distributorsNewProducer = distributorsList.stream()
                .filter(Distributor::getPickProducer)
                .sorted(Comparator.comparingInt(Distributor::getId))
                .collect(Collectors.toList());

        for (Distributor distributor : distributorsNewProducer) {
            // Remove distributor from their old producers
            removeProducers(distributor);
            // Choose new producers
            chooseProducer(distributor);

            // Unset "must choose producer" flag
            distributor.unsetPickProducer();
            distributor.calculateProductionCost();
        }

        if (round >= 1) {
            for (Producer producer : producersList) {
                producer.logMonthlyStat(round);
            }
        }

    }
}
