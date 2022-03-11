package fileio.output;

import entities.Consumer;
import entities.Contract;
import entities.Distributor;
import entities.MonthlyStat;
import entities.Producer;

import java.util.ArrayList;
import java.util.List;

public final class Output {
    private final List<ConsumerOutput> consumers;
    private final List<DistributorOutput> distributors;
    private final List<ProducerOutput> producers;

    public Output(final List<Consumer> consumerList, final List<Distributor> distributorList,
                  final List<Producer> producerList) {
        consumers = new ArrayList<>();
        distributors = new ArrayList<>();
        producers = new ArrayList<>();

        for (Consumer consumer : consumerList) {
            ConsumerOutput consumerOutput = new ConsumerOutput(consumer);
            this.consumers.add(consumerOutput);
        }

        for (Distributor distributor : distributorList) {
            List<ContractOutput> contracts = new ArrayList<>();

            for (Contract contract : distributor.getContractList()) {
                ContractOutput contractOutput = new ContractOutput(contract);
                contracts.add(contractOutput);
            }

            DistributorOutput distributorOutput = new DistributorOutput(distributor, contracts);
            this.distributors.add(distributorOutput);
        }

        for (Producer producer : producerList) {
            List<MonthlyStatOutput> monthlyStats = new ArrayList<>();

            for (MonthlyStat monthlyStat : producer.getMonthlyStats()) {
                MonthlyStatOutput monthlyStatOutput = new MonthlyStatOutput(monthlyStat);
                monthlyStats.add(monthlyStatOutput);
            }

            ProducerOutput producerOutput = new ProducerOutput(producer, monthlyStats);
            this.producers.add(producerOutput);
        }
    }

    public List<ConsumerOutput> getConsumers() {
        return consumers;
    }

    public List<DistributorOutput> getDistributors() {
        return distributors;
    }

    public List<ProducerOutput> getEnergyProducers() {
        return producers;
    }
}
