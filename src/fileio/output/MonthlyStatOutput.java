package fileio.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import entities.MonthlyStat;

import java.util.List;

@JsonPropertyOrder({"month", "distributorsIds"})
public final class MonthlyStatOutput {
    private final int month;
    private final List<Integer> distributorsIds;

    public MonthlyStatOutput(MonthlyStat monthlyStat) {
        this.month = monthlyStat.getMonth();
        this.distributorsIds = monthlyStat.getDistributorsIds();
    }

    @JsonProperty("month")
    public int getMonth() {
        return month;
    }

    @JsonProperty("distributorsIds")
    public List<Integer> getDistributorsIds() {
        return distributorsIds;
    }
}
