package fileio.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.Distributor;
import strategies.EnergyChoiceStrategyType;

import java.util.List;

@JsonPropertyOrder({"id", "energyNeededKW", "contractCost", "budget", "producerStrategy",
        "isBankrupt", "contracts"})
public final class DistributorOutput {
    private final int id;
    private final int energyNeededKW;
    private final int contractCost;
    private final int budget;
    private final EnergyChoiceStrategyType producerStrategy;
    private final boolean isBankrupt;
    private final List<ContractOutput> contracts;

    public DistributorOutput(final Distributor distributor, final List<ContractOutput> contracts) {
        this.id = distributor.getId();
        this.energyNeededKW = distributor.getEnergyNeededKW();
        this.contractCost = distributor.getContractCost();
        this.budget = distributor.getBudget();
        this.producerStrategy = distributor.getProducerStrategy();
        this.isBankrupt = distributor.isBankrupt();
        this.contracts = contracts;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("energyNeededKW")
    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    @JsonProperty("contractCost")
    public int getContractCost() {
        return contractCost;
    }

    @JsonProperty("budget")
    public int getBudget() {
        return budget;
    }

    @JsonProperty("producerStrategy")
    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    @JsonProperty("isBankrupt")
    public boolean isBankrupt() {
        return isBankrupt;
    }

    @JsonProperty("contracts")
    public List<ContractOutput> getContracts() {
        return contracts;
    }
}
