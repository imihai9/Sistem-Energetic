package fileio.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.EnergyType;
import entities.Producer;

import java.util.List;

@JsonPropertyOrder({"id", "maxDistributors", "priceKW", "energyType", "energyPerDistributor",
        "monthlyStats"})

public final class ProducerOutput {
    private final int id;
    private final int maxDistributors;
    private final double priceKW;
    private final EnergyType energyType;
    private final int energyPerDistributor;
    private final List<MonthlyStatOutput> monthlyStats;

    public ProducerOutput(final Producer producer, final List<MonthlyStatOutput> monthlyStats) {
        this.id = producer.getId();
        this.maxDistributors = producer.getMaxDistributors();
        this.priceKW = producer.getPriceKW();
        this.energyType = producer.getEnergyType();
        this.energyPerDistributor = producer.getEnergyPerDistributor();
        this.monthlyStats = monthlyStats;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("maxDistributors")
    public int getMaxDistributors() {
        return maxDistributors;
    }

    @JsonProperty("priceKW")
    public double getPriceKW() {
        return priceKW;
    }

    @JsonProperty("energyType")
    public EnergyType getEnergyType() {
        return energyType;
    }

    @JsonProperty("energyPerDistributor")
    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    @JsonProperty("monthlyStats")
    public List<MonthlyStatOutput> getMonthlyStats() {
        return monthlyStats;
    }
}
