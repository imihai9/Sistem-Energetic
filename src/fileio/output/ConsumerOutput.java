package fileio.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.Consumer;

@JsonPropertyOrder({"id", "isBankrupt", "budget"})
public final class ConsumerOutput {
    private final int id;
    private final boolean isBankrupt;
    private final int budget;

    public ConsumerOutput(final Consumer consumer) {
        this.id = consumer.getId();
        this.isBankrupt = consumer.isBankrupt();
        this.budget = consumer.getBudget();
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("isBankrupt")
    public boolean isBankrupt() {
        return isBankrupt;
    }

    @JsonProperty("budget")
    public int getBudget() {
        return budget;
    }
}
