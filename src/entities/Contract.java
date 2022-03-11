package entities;

public final class Contract {
    private final Consumer consumer;
    private final Distributor distributor;

    private int price;
    private int remainedContractMonths;

    public Contract(final Consumer consumer, final Distributor distributor,
                    final int price, final int contractMonths) {
        this.consumer = consumer;
        this.distributor = distributor;
        this.price = price;
        this.remainedContractMonths = contractMonths;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public void monthPassed() {
        this.remainedContractMonths--;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }
}
