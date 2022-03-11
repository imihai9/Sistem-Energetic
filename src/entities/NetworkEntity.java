package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NetworkEntity {
    protected int id;
    protected int budget;
    protected boolean bankrupt;

    public final int getId() {
        return id;
    }

    public final void setId(final int id) {
        this.id = id;
    }

    public final int getBudget() {
        return budget;
    }

    @JsonProperty("initialBudget")
    public final void setBudget(final int budget) {
        this.budget = budget;
    }

    public final boolean isBankrupt() {
        return bankrupt;
    }

    public final void setBankrupt(final boolean bankrupt) {
        this.bankrupt = bankrupt;
    }
}
