package entities;

import fileio.input.ProducerInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Producer extends NetworkEntity implements Subject {
    private final EnergyType energyType;
    private final int maxDistributors;
    private final double priceKW;
    private final List<Distributor> distributors;
    private final List<MonthlyStat> monthlyStats;
    private int energyPerDistributor;

    public Producer(final ProducerInput producerInput) {
        this.id = producerInput.getId();
        this.energyType = producerInput.getEnergyType();
        this.maxDistributors = producerInput.getMaxDistributors();
        this.priceKW = producerInput.getPriceKW();
        this.energyPerDistributor = producerInput.getEnergyPerDistributor();

        this.monthlyStats = new ArrayList<>();
        this.distributors = new ArrayList<>();
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    /**
     * Changes energy per distributor, notifies the distributors
     */
    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
        this.notifyObservers();
    }

    public List<Distributor> getDistributors() {
        return Collections.unmodifiableList(distributors);
    }

    public List<MonthlyStat> getMonthlyStats() {
        return Collections.unmodifiableList(monthlyStats);
    }

    /**
     * Adds a distributor to the observers list
     */
    @Override
    public void addObserver(Distributor distributor) {
        this.distributors.add(distributor);
    }

    /**
     * Removes a distributor from the observers list
     */
    @Override
    public void removeObserver(Distributor distributor) {
        for (Distributor currDistributor : distributors) {
            if (currDistributor.equals(distributor)) {
                distributors.remove(currDistributor);
                return;
            }
        }
    }

    /**
     * Notifies distributor clients about energy
     */
    @Override
    public void notifyObservers() {
        for (Distributor distributor : distributors) {
            distributor.update();
        }
    }

    /**
     * Adds new entry to the monthlyStats log for the current month (logs current distributorsIds)
     *
     * @param month - current month
     */
    public void logMonthlyStat(final int month) {
        // Get distributor ids in ascending order
        List<Integer> distributorsIds = distributors.stream()
                .map(Distributor::getId)
                .sorted().collect(Collectors.toList());

        MonthlyStat monthlyStat = new MonthlyStat(month, new ArrayList<>(distributorsIds));
        this.monthlyStats.add(monthlyStat);
    }
}
