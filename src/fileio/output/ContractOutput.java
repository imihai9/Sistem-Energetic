package fileio.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.Contract;

@JsonPropertyOrder({"consumerId", "price", "remainedContractMonths"})
public final class ContractOutput {
    private final int consumerId;
    private final int price;
    private final int remainedContractMonths;

    public ContractOutput(final Contract contract) {
        this.consumerId = contract.getConsumer().getId();
        this.price = contract.getPrice();
        this.remainedContractMonths = contract.getRemainedContractMonths();
    }

    @JsonProperty("consumerId")
    public int getConsumerId() {
        return consumerId;
    }

    @JsonProperty("price")
    public int getPrice() {
        return price;
    }

    @JsonProperty("remainedContractMonths")
    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }
}
