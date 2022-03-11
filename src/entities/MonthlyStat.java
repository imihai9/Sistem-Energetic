package entities;

import java.util.Collections;
import java.util.List;

public final class MonthlyStat {
    private final int month;
    private final List<Integer> distributorsIds;

    public MonthlyStat(final int month, final List<Integer> distributorsIds) {
        this.month = month;
        this.distributorsIds = distributorsIds;
    }

    public int getMonth() {
        return month;
    }

    public List<Integer> getDistributorsIds() {
        return Collections.unmodifiableList(distributorsIds);
    }
}
