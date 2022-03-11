package comparators;

import entities.Producer;

import java.util.Comparator;

public final class RenewableEnergyComparator implements Comparator<Producer> {
    @Override
    public int compare(Producer o1, Producer o2) {
        boolean isRenewableO1 = o1.getEnergyType().isRenewable();
        boolean isRenewableO2 = o2.getEnergyType().isRenewable();

        if (isRenewableO1 && !isRenewableO2) {
            return -1;
        } else if (!isRenewableO1 && isRenewableO2) {
            return 1;
        }

        return 0;
    }
}
